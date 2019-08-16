package io.dtc.essearch.configuration;

import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ElasticsearchConfig store all needed information regarding elasticsearch
 */
@Configuration
public class ElasticsearchConfig {
    
    /**
     * This attribute is used to identify ip format, 2 mean host:port
     */
    private static final int ADDRESS_LENGTH = 2;
    /**
     * this attribute is used to identify http or https
     */
    private static final String HTTP_SCHEME = "http";

    /**
     * this attribute is used to store list ip from configuration, ex: 127.0.0.1:9200,127.0.0.1:9300
     */
    @Value("${elasticsearch.ip}")
    private String[] elasticsearchIp;

    @Bean
    public RestClientBuilder restClientBuilder() {
        HttpHost[] hosts = Arrays.stream(elasticsearchIp)
                .map(this::makeHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);
        System.out.println(Arrays.toString(hosts));
        return RestClient.builder(hosts);
    }

    @Bean
    public RestHighLevelClient client(@Autowired RestClientBuilder restClientBuilder) {
        restClientBuilder.setMaxRetryTimeoutMillis(60000);
        return new RestHighLevelClient(restClientBuilder);
    }

    /**
     * This method is used to pare
     * @param s ip string with format host:port, ex: 127.0.0.1:9200
     * @return HttpHost
     */
    private HttpHost makeHttpHost(String s) {
        assert StringUtils.isNotEmpty(s);
        String[] address = s.split(":");
        if (address.length == ADDRESS_LENGTH) {
            String ip = address[0];
            int port = Integer.parseInt(address[1]);
            return new HttpHost(ip, port, HTTP_SCHEME);
        } else {
            return null;
        }
    }
}