FROM mysql:5.7

MAINTAINER hnliuwx@gmail.com

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./km_mysql_docker.sql /docker-entrypoint-initdb.d