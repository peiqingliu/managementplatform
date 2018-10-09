package com.wuqianqian.business.admin;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author liupeqing
 * @date 2018/9/27 13:11
 */
@SpringBootApplication
@EnableFeignClients
@EnableHystrix
@EnableTransactionManagement  // 启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
@EnableEurekaClient
@EnableCaching  //开启缓存
// @EnableAsync // 开始对异步任务的支持，并在相应的方法中使用@Async注解来声明一个异步任务
// @EnableScheduling  //定时任务
@ComponentScan(basePackages = { "com.wuqianqian.business.admin", "com.wuqianqian.system.api" })
public class AdminApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AdminApplication.class);
    }



    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class,args);
        System.out.println("hahahah================================================================");
    }


}
