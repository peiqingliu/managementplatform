package com.wuqianqian.business.commons.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author liupeqing
 * @date 2018/9/27 12:55
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "business.permission")  //把配置类中的信息转换成实体类
public class PermissionConfiguration {

    private boolean	enabled	= true;

    public boolean isEnabled() {
        return enabled;
    }
}
