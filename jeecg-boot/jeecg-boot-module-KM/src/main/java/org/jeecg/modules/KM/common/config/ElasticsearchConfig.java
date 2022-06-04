package org.jeecg.modules.KM.common.config;


import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "esclient")
@EnableConfigurationProperties(ElasticsearchConfig.class)

public class ElasticsearchConfig {

    public String getMasterHost() {
        return masterHost;
    }

    public void setMasterHost(String masterHost) {
        this.masterHost = masterHost;
    }

    public Integer getMasterPort() {
        return masterPort;
    }

    public void setMasterPort(Integer masterPort) {
        this.masterPort = masterPort;
    }

    private String masterHost;
    private Integer masterPort;

    public String getMasterUserName() {
        return masterUserName;
    }

    public void setMasterUserName(String masterUserName) {
        this.masterUserName = masterUserName;
    }

    public String getMasterUserPwd() {
        return masterUserPwd;
    }

    public void setMasterUserPwd(String masterUserPwd) {
        this.masterUserPwd = masterUserPwd;
    }

    private String masterUserName;
    private String masterUserPwd;

    @Bean
    public RestHighLevelClient restHighLevelClient() {

        HttpHost host=new HttpHost(masterHost, masterPort, HttpHost.DEFAULT_SCHEME_NAME);
        RestClientBuilder builder=RestClient.builder(host);
//        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(masterUserName, masterUserPwd));
//        builder.setHttpClientConfigCallback(f -> f.setDefaultCredentialsProvider(credentialsProvider));
        RestHighLevelClient restClient = new RestHighLevelClient( builder);

        return restClient;
    }
}
