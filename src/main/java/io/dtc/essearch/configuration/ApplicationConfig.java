package io.dtc.essearch.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

/**
 * ApplicationConfig read config from application.yml
 */
@Data
@Configuration
public class ApplicationConfig {

    /**
     * This field use as prefix for all index, use it in case of multiple env
     */
    @Value("${elasticsearch.index.prefix}")
    private String elasticsearchIndexPrefix;

}