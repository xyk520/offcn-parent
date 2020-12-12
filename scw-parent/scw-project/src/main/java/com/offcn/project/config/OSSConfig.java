package com.offcn.project.config;

import com.offcn.common.utils.OSSTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OSSConfig {

    @Bean
    @ConfigurationProperties(prefix = "oss")
    public OSSTemplate getOssgetTemplate(){
        return new OSSTemplate();
    }
}
