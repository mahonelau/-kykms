**支持Linux和Windows下运行，推荐CentOS 7

- # CentOS 7

## 安装ElasticSearch
cd /usr/local
wget http://dl.elasticsearch.cn/elasticsearch/elasticsearch-7.6.1-linux-x86_64.tar.gz
tar zxvf elasticsearch-7.6.1-linux-x86_64.tar.gz 

## 创建用户elasticsearch
    adduser elasticsearch
    passwd elasticsearch (yourPWD)
    chown -R elasticsearch elasticsearch-7.6.1

## 配置ES
       cd /usr/local/elasticsearch-7.6.1/config
       vi elasticsearch.yml 增加配置：
       discovery.type: single-node
       network.host: 0.0.0.0
       indices.query.bool.max_clause_count: 102400
       更改Elasticsearch最大内存大小
       vi /usr/local/elasticsearch-7.6.1/config/jvm.options
        -Xms8g 
        -Xmx8g  
        vi /etc/sysctl.conf 
        增加配置vm.max_map_count=262144
        sysctl -p
        　　执行命令sysctl -p生效

## 安装ik分词器
cd  /usr/local/elasticsearch-7.6.1/bin
./elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.6.1/elasticsearch-analysis-ik-7.6.1.zip
    
## 启动es：
    cd /usr/local/elasticsearch-7.6.1/bin
    su elasticsearch
    ./elasticsearch -d
   

- # **Windows环境安装**

## 安装ElasticSearch
下载 [ES7.6.1安装包](http://dl.elasticsearch.cn/elasticsearch/elasticsearch-7.6.1-windows-x86_64.zip)，
解压 zip文件 到ES安装路径，如 C:\software\elasticsearch-7.6.1

## 配置ES
       cd C:\software\elasticsearch-7.6.1\config
###        编辑 elasticsearch.yml 增加配置： 
       discovery.type: single-node
       network.host: 0.0.0.0
       indices.query.bool.max_clause_count: 102400
       更改Elasticsearch最大内存大小
###        编辑/jvm.options
        -Xms8g 
        -Xmx8g  

## 安装ik分词器
下载 [IK分词器](https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.6.1/elasticsearch-analysis-ik-7.6.1.zip)，
解压到 C:\software\elasticsearch-7.6.1\plugins\analysis-ik
    
## 启动es：
    cd C:\software\elasticsearch-7.6.1\config\bin
    执行 elasticsearch.bat
    