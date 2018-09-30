package com.wuqianqian.core.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author liupeqing
 * @date 2018/9/28 13:53
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "fw.jwt")
public class JwtConfiguration {

    /**
     * JWT key
     */
    private String	jwtkey;

    /**
     * JWT TOKEN PREFIX
     */
    private String	prefix;

    /**
     * JWT AUTH REQUEST URL
     */
    private String	jwtAuthUrl;
}
