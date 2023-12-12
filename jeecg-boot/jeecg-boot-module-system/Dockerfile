FROM ansible/centos7-ansible

ENV container docker

RUN yum install -y libreoffice.x86_64
COPY ./simsun.ttc /usr/share/fonts
ADD ./target/jeecg-boot-module-system-2.4.5.jar /usr/local/kykms/

WORKDIR /usr/local/kykms

EXPOSE 8080
