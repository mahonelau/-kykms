**官方镜像支持本机运行

**如需在服务器运行则需要自行构建前端镜像或运行前端服务，并修改配置文件将服务端ip指向服务器真实ip

**Docker环境请自行准备

- # 用官方镜像运行
```
 windows下运行CMD，进入jeecg-boot根路径下面命令启动镜像容器组
 
 docker-compose -f ./docker-compose-official.yml up 
```
访问 http://localhost:3000

- # 本地打包构建后台

1. yml配置文件请确保每一项下面按正确配置
```
server:
  port: 8080
  servlet:
    context-path: /ky
base:
  #上传文件保存的路径
  upload-dir: /kykms/KmDocs
  # libreOffice安装路径
  soffice-path: /usr/bin/soffice
esclient:
  master-host: kykms-ES 
  master-port: 9200
spring:
  datasource:
    dynamic:
      datasource:
        # 数据库登录信息配置
        master:
          url: jdbc:mysql://kykms-mysql:3306/km?characterEncoding=UTF-8&useUnicode=true&useSSL=false&tinyInt1isBit=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
          username: root
          password: root
  #redis 配置
  redis:
    database: 0
    host: kykms-redis
    password: ''
    port: 6379
```
2.  通过jeecg-boot-parent项目 maven打包，执行install
3.  打包Docker容器
```
 运行CMD，进入jeecg-boot根路径下面命令启动镜像容器组
 
 docker-compose up -d
```
4.  访问后台项目（注意要开启swagger）http://localhost:8080/ky/doc.html

- # 本地打包构建前端
1. 修改前端项目的后台域名
.env.production

```
VUE_APP_API_BASE_URL=http://localhost:8080/ky
```

2. 进入ant-design-vue-jeecg根目录

```
yarn run build
```

3. 构建镜像(注意后面的".“)
```
docker build -t nginx:kykms .
```

4. 启动镜像

```
docker run --name kykms-nginx -p 3000:3000 -d nginx:kykms
```
5. 访问前台项目 http://localhost:3000