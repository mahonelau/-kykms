**官方镜像支持本机运行

- # 用官方镜像运行
```
 windows下运行CMD，进入jeecg-boot根路径下面命令启动镜像容器组
 
 docker-compose up -d 
```
访问 http://localhost

- # 本地打包构建后台

1. 通过jeecg-boot-parent项目 maven打包，执行install
2. 本地打包构建前端

修改前端项目的后台域名
   .env.docker
```
VUE_APP_API_BASE_URL=/api
```
3. 在npm菜单下面选择 build:docker
4. 打包Docker容器

运行CMD，进入jeecg-boot根路径下面命令启动镜像容器组

```
 
 docker-compose -f docker-compose-build.yml up -d
```
5. 访问 http://localhost
