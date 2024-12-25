FROM ubuntu:22.04
MAINTAINER mahone<liuwenxiong@mail.kykms.cn>

RUN apt update;apt install -y libreoffice;\
mkdir /kykms;

COPY ./simsun.ttc /usr/share/fonts
COPY ./target/jeecg-boot-module-system-2.4.5.jar /kykms/

WORKDIR /kykms

EXPOSE 8080
