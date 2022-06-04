FROM mysql:5.7

MAINTAINER hnliuwx@gmail.com

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./km_mysql.sql /docker-entrypoint-initdb.d