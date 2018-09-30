package com.wuqianqian.core.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *  所有需要过滤URL的配置，urls.collects 模式
 *
 * @ConditionalOnExpression("${test.enabled:true}")
 *  在配置文件中设置一个enable，当这个配置为true的时候，才进行相关的配置类的初始化。
 *
 * @author liupeqing
 * @date 2018/9/28 13:47
 */
@Configuration
@ConfigurationProperties(prefix = "urls")
@ConditionalOnExpression("!'${urls}'.isEmpty()")  //非空的情况下才初始化该类
public class IgnoreUrlsConfiguration {

    private List<String> collects	= new ArrayList<>();

    public List<String> getCollects() {
        return collects;
    }

    public void setCollects(List<String> collects) {
        this.collects = collects;
    }
}
