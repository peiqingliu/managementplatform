package com.wuqianqian.springcloud.gateway.config;

import com.wuqianqian.core.commons.constants.ServiceIdConstant;
import com.wuqianqian.springcloud.StringHelper;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源配置
 * 用zuul作为分布式系统的网关，同时使用swagger生成文档，想把整个系统的文档整合在同一个页面上
 * @author liupeqing
 * @date 2018/10/8 14:03
 */
@Component
@Primary  //优先使用这个实现类
public class SystemSwaggerResourcesProvider implements SwaggerResourcesProvider {

    private final RouteLocator routeLocator;

    public SystemSwaggerResourcesProvider(RouteLocator routeLocator){
        this.routeLocator = routeLocator;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<SwaggerResource>();
        List<Route> routes = routeLocator.getRoutes();
        routes.stream().forEach(route -> {
            //swagger排除auth模块  使用后者去匹配前者  后者作为标准
            if (StringHelper.contains(route.getId(),ServiceIdConstant.AUTH_SERVICE)){
                resources.add(buildSwaggerResource(route.getId(), route.getFullPath().replace("**", "v2/api-docs")));
            }
        });
        return null;
    }

    private SwaggerResource buildSwaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }

}
