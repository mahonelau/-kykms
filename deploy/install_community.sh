mv /etc/yum.repos.d/CentOS-Base.repo /etc/yum.repos.d/CentOS-Base.repo.backup;
wget -O /etc/yum.repos.d/CentOS-Base.repo http://mirrors.aliyun.com/repo/Centos-7.repo;
wget -O /etc/yum.repos.d/epel.repo http://mirrors.aliyun.com/repo/epel-7.repo;
yum install unzip -y;
wget https://qntest.sihualuo.cn/KM_community.zip
unzip KM_community.zip;

systemctl stop firewalld;
systemctl disable firewalld;
yum clean all;
yum make cache;
yum install docker -y;
yum install docker-compose -y;
systemctl start docker;
systemctl enable docker;
mkdir /elasticsearch;
mkdir /elasticsearch/data;
chmod 777 /elasticsearch/data;
chmod +x *.sh;

docker load -i kykms-mysql.tar;
docker load -i kykms-redis.tar;
docker load -i kykms-ES.tar;
docker-compose -f  docker-compose-local.yml up -d; 

yum install nginx -y;
mv /etc/nginx/nginx.conf /etc/nginx/nginx.conf.bak;
cp ./nginx.conf /etc/nginx/; 
cp ./dist.zip /usr/share/nginx/html/;
cp ./simsun.ttc /usr/share/fonts/;
cd /usr/share/nginx/html;
unzip -o dist.zip;
systemctl enable nginx;
systemctl start nginx;

yum install java-1.8.0 -y;
yum install libreoffice.x86_64 -y;
cd -;
chmod +x *.sh;
./start.sh;
