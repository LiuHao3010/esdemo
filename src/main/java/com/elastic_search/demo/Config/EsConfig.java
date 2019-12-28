package com.elastic_search.demo.Config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsConfig {
    private RestHighLevelClient client=null;
    @Value("${ES.hostname}")
    private String hostname;
    @Value("${ES.port}")
    private Integer port;
    @Value("${ES.scheme}")
    private String scheme;
    @Bean("client")
    public RestHighLevelClient getClient(){
        client= new RestHighLevelClient(RestClient.builder(new HttpHost(hostname,port,scheme)));
        return client;
    }
}
