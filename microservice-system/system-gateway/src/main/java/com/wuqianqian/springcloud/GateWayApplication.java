package com.wuqianqian.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @author liupeqing
 * @date 2018/9/26 15:46
 *
 *
 * Spring Security默认是禁用注解的，要想开启注解，
 * 需要在继承WebSecurityConfigurerAdapter的类上加@EnableGlobalMethodSecurity注解，
 * 来判断用户对某个控制层的方法是否具有访问权限
 *
 *  @PreAuthorize("hasRole(‘admin‘)") 启用这个注解
 */
@SpringBootApplication
@EnableZuulProxy  //开启网关
@EnableFeignClients
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = {"com.wuqianqian.springcloud", "com.wuqianqian.core.configuration", "com.wuqianqian.system.api"})
public class GateWayApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GateWayApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication.class,args);
    }

    @Bean
    LoadBalancerInterceptor loadBalancerInterceptor(LoadBalancerClient loadBalance) {
        return new LoadBalancerInterceptor(loadBalance);
    }
}
