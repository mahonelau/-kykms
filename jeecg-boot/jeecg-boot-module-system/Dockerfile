FROM ansible/centos7-ansible

ENV container docker

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime; \
cd /etc/yum.repos.d;\
curl -o /etc/yum.repos.d/Centos-7.repo http://mirrors.aliyun.com/repo/Centos-7.repo;\
curl -o /etc/yum.repos.d/epel-7.repo http://mirrors.aliyun.com/repo/epel-7.repo;\
curl -o /etc/yum.repos.d/CentOS7-Base-163.repo http://mirrors.163.com/.help/CentOS7-Base-163.repo;\
yum clean all;\
yum makecache;\
yum install libreoffice.x86_64 -y;

WORKDIR /kykms

ADD ./target/jeecg-boot-module-system-2.4.5.jar ./
ADD ./simsun.ttc /usr/share/fonts

CMD /usr/sbin/init;\
sleep 5;\
java -Djava.security.egd=file:/dev/./urandom -jar jeecg-boot-module-system-2.4.5.jar

EXPOSE 8080
