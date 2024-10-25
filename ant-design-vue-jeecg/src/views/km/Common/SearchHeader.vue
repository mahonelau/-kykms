<template>
  <div :class="theme">
    <router-link :to="{path:indexUrl}">
      <span>
        <span style="float: left;margin: -5px 10px 0 15px">
          <img style="height: 35px;" :src="siteInfo.siteAvatar" alt="logo">
        </span>
        <span class="site-title site-title-txt show-txt url-txt">{{ siteInfo.siteTitle }}</span>
      </span>
    </router-link>

    <span class="header-option" style="float: right">
      <user-menu class="menu-txt"/>
      <files-menu class="top-txt  show-txt" v-if="token!=null"/>
<!--      <span class="url-txt" v-if="token==null" @click="jumpLogin()">立即登录</span>-->
      <span class="top-txt show-txt" @click="jumpManage()">
        <a-space><a-icon type="setting" class="iconOfMenu"/>管理后台</a-space>
      </span>
      <span class="top-txt" v-if="token!=null" @click="jumpTopicPage()">
        <a-space><a-icon type="folder" class="iconOfMenu"/>知识浏览</a-space>
      </span>
      <span class="top-txt" v-if="token!=null" @click="jumpSearchPage()">
        <a-space><a-icon type="search" class="iconOfMenu"/>综合检索</a-space>
      </span>
<!--      小铃铛：站内提醒消息-->
<!--      <div style="float: right;cursor: pointer;color: darkorange">-->
<!--        <header-notice class="action"/>-->
<!--      </div>-->

    </span>
  </div>
</template>

<script>
import UserMenu from '@/components/tools/UserMenu'
import FilesMenu from '@/components/tools/FilesMenu'
import { mixin } from '@/utils/mixin.js'
import Vue from 'vue'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import HeaderNotice from '@/components/tools/HeaderNotice'

export default {
  name: 'SearchHeader',
  mixins: [mixin],
  components: {
    FilesMenu,
    UserMenu,
    HeaderNotice
  },
  data() {
    return {
      token: '',
      siteInfo: {},
      kmConfig: {},
      indexUrl:'/front/DefaultDocSearch'
    }
  },
  props: {
    title: {
      type: '',
      required: false,
      default: ''
    },
    theme: {
      type: '',
      required: false,
      default: 'white'
    }
  },
  created() {
    let token = Vue.ls.get(ACCESS_TOKEN)
    this.siteInfo = this.$store.getters.siteInfo
    this.kmConfig = this.$store.getters.kmConfig
    this.token = token
    if(this.kmConfig !== undefined && this.kmConfig.DefaultPageUseTopicList === '1')
      this.indexUrl = '/front/RecommendTopicList'
  },
  methods: {

    // 进入管理首页
    jumpManage() {
      let token = this.token
      if (token == '' || token == null) {
        this.$router.push('/front/user/login')
      } else {
        let routeData = this.$router.resolve({
          path: '/dashboard/analysis',
          query: {}
        })
        window.open(routeData.href, '_blank')
      }
    },
    // // 跳转到收藏夹
    // jumpKmDocFavouritePage() {
    //   let token = this.token
    //   if (token == '' || token == null) {
    //     this.$router.push('/user/login')
    //   } else {
    //     let routeData = this.$router.resolve({
    //       path: '/km/filemanagement/KmDocFavouriteList',
    //       query: {}
    //     })
    //     window.open(routeData.href, '_blank')
    //   }
    // },
    // 跳转到检索页面
    jumpSearchPage() {
      let token = this.token
      if (token == '' || token == null) {
        this.$router.push('/front/user/login')
      } else {
        this.$router.push('/front/DefaultDocSearch')
      }
    },
    // 目录浏览
    jumpTopicPage() {
      let token = this.token
      if (token == '' || token == null) {
        this.$router.push('/front/user/login')
      } else {
        this.$router.push('/front/RecommendTopicList')
      }
    },
    // 跳转到个人草稿文件夹
    jumpDraftsPage() {
      let token = this.token
      if (token == '' || token == null) {
        this.$router.push('/front/user/login')
      } else {
        let routeData = this.$router.resolve({
          path: '/km/filemanagement/FilesList',
          query: {}
        })
        window.open(routeData.href, '_blank')
      }
    }

  }
}
</script>
<style >
/* 大屏幕 ：大于等于1200px*/
@media (min-width: 1200px) {
  .show-txt {

  }

  .site-title-txt {
    font-size: 20px;
  }
}

/*默认*/
@media (min-width: 980px) {
  .show-txt {

  }

  .site-title-txt {
    font-size: 20px;
  }
}

/* 平板电脑和小屏电脑之间的分辨率 */
@media (min-width: 768px) and (max-width: 979px) {
  .show-txt {

  }

  .site-title-txt {
    font-size: 20px;
  }
}

/* 横向放置的手机和竖向放置的平板之间的分辨率 */
@media (max-width: 767px) {
  .show-txt {
    display: none;
  }

  .site-title-txt {
    font-size: 18px;
  }
}

/* 横向放置的手机及分辨率更小的设备 */
@media (max-width: 480px) {
  .show-txt {
    display: none;
  }

  .site-title-txt {
    font-size: 16px;
  }
}

.top-txt {
  color: white;
  cursor: pointer;
  height: 60px;
  float: right;
  padding: 0px 10px;
}

.top-txt:hover {
  /*border-radius: 2px;*/
  /*height: 60px;*/
  color: darkorange;
  /*background-color: rgba(255, 255, 255, 0.3);*/
}

.url-txt:hover {
  color: darkorange;
}

.site-title {
  font-weight: normal;
  color: white;
  float: left;
  margin-top: -3px;
}

.top-title {
  vertical-align: middle;
  font-size: 20px;
  font-weight: normal;
  color: #000c17;
  padding-left: 10px;
}

.menu-txt {
  line-height: 60px;
  /*marginverticalAlign: 'middle';*/
  font-size: 15px;
  font-weight: normal;
  color: #000c17;
  float: right;
  background: #0a84ff;
}
.iconOfMenu {
  color: darkorange;
}
</style>

