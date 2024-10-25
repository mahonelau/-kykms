const path = require('path')
const CompressionPlugin = require('compression-webpack-plugin')

function resolve(dir) {
  return path.join(__dirname, dir)
}

// const isProd = process.env.NODE_ENV === 'production'
// const isProd = true
// const needCdn = isProd
// 'vue-router': 'VueRouter',
// 'ant': 'Antd'
// vue: 'Vue',


// const externals = {
//   moment: 'moment'
//   }

// cdn 外部扩展，通过 cdn 引入，不会被webpack打包
// const assetsCDN = {
//   // cdn的css链接
//   css: [
//     'http://cdn.jsdelivr.net/npm/ant-design-vue@1.7.2/dist/antd.min.css'
//   ],
//   // cdn的js链接
//   js: [
//     'http://cdn.jsdelivr.net/npm/moment@2.29.4/moment.min.js',
//     'http://cdn.jsdelivr.net/npm/moment@2.29.4/locale/zh-cn.js',
//     'http://cdn.jsdelivr.net/npm/ant-design-vue@1.7.2/dist/antd.min.js'
//   ]
// }

// vue.config.js
module.exports = {
  /*
    Vue-cli3:
    Crashed when using Webpack `import()` #2463
    https://github.com/vuejs/vue-cli/issues/2463
   */

  pages: {
    // // 先配置主页
    // login: {
    //   entry: 'src/loginMain.js',
    //   template: 'public/login.html',
    //   title: '用户登录'
    // },
    // 先配置主页
    front: {
      entry: 'src/frontMain.js',
      template: 'public/frontIndex.html',
      title: '前端'
    },
    // 再配置后台管理页面
    index: {
      entry: 'src/main.js',
      template: 'public/index.html',
      title: '后台管理'
    }
  },

  // 如果你不需要生产环境的 source map，可以将其设置为 false 以加速生产环境构建。
  productionSourceMap: false,
  //打包app时放开该配置
  //publicPath:'./',
  configureWebpack: config => {
    //生产环境取消 console.log
    if (process.env.NODE_ENV === 'production') {
      config.optimization.minimizer[0].options.terserOptions.compress.drop_console = true
    }
    // config.externals = needCdn ? assetsCDN.externals : {}
  },

  chainWebpack: (config) => {

    config.plugins.delete('prefetch')
    // 移除 preload 插件，避免加载多余的资源
    // config.plugins.delete('preload')

    config.resolve.alias
      .set('@$', resolve('src'))
      .set('@api', resolve('src/api'))
      .set('@assets', resolve('src/assets'))
      .set('@comp', resolve('src/components'))
      .set('@views', resolve('src/views'))


    if (process.env.use_analyzer) {
      config.plugin("webpack-bundle-analyzer").use(require("webpack-bundle-analyzer").BundleAnalyzerPlugin)
    }

    //生产环境，开启js\css压缩
    if (process.env.NODE_ENV === 'production') {
        config.plugin('compressionPlugin').use(new CompressionPlugin({
          test: /\.(js|css|less)$/, // 匹配文件名
          threshold: 10240, // 对超过10k的数据压缩
          deleteOriginalAssets: false // 不删除源文件
        }))
    }


// 开启gzip压缩
//     config.plugin('compressionPlugin').use(
//       new CompressionPlugin(
//         {
//           filename: info => {
//             return `${info.path}.gz${info.query}`
//           },
//           algorithm: 'gzip',
//           threshold: 10240, // 只有大小大于该值的资源会被处理 10240
//           test: new RegExp('\\.(' + ['js'].join('|') + ')$'
//           ),
//           minRatio: 0.8, // 只有压缩率小于这个值的资源才会被处理
//           deleteOriginalAssets: false // 删除原文件
//         }
//       )
//     )

    // // 初始化页面的title为配置文件设置的值，public/index.html中的htmlWebpackPlugin.options.title
    // config.plugin('html').tap((args) => {
    //   if (needCdn) {
    //     args[0].cdn = assetsCDN
    //   }
    //   return args
    // })
    // 视为一个外部库，而不将它打包进来
    // config.externals(externals)


    // 配置 webpack 识别 markdown 为普通的文件
    config.module
      .rule('markdown')
      .test(/\.md$/)
      .use()
      .loader('file-loader')
      .end()

    // 编译vxe-table包里的es6代码，解决IE11兼容问题
    config.module
      .rule('vxe')
      .test(/\.js$/)
      .include
        .add(resolve('node_modules/vxe-table'))
        .add(resolve('node_modules/vxe-table-plugin-antd'))
        .end()
      .use()
      .loader('babel-loader')
      .end()

  },

  css: {
    loaderOptions: {
      less: {
        modifyVars: {
          /* less 变量覆盖，用于自定义 ant design 主题 */
          'primary-color': '#1890FF',
          'link-color': '#1890FF',
          'border-radius-base': '4px',
        },
        javascriptEnabled: true,
      }
    }
  },

  devServer: {
    port: 3000,
    proxy: {
     /* '/api': {
        target: 'https://mock.ihx.me/mock/5baf3052f7da7e07e04a5116/antd-pro', //mock API接口系统
        ws: false,
        changeOrigin: true,
        pathRewrite: {
          '/jeecg-boot': ''  //默认所有请求都加了jeecg-boot前缀，需要去掉
        }
      },*/
      '/jeecg-boot': {
        target: 'http://172.16.2.47:8080', //请求本地 需要jeecg-boot后台项目
        ws: false,
        changeOrigin: true
      },
    }
  },

  lintOnSave: undefined
}