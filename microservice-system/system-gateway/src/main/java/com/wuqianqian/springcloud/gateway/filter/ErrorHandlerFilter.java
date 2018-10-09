package com.wuqianqian.springcloud.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.wuqianqian.springcloud.gateway.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.ERROR_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_RESPONSE_FILTER_ORDER;

/**
 * 网关异常统一处理
 * @author liupeqing
 * @date 2018/10/8 19:09
 */
@Component
public class ErrorHandlerFilter extends ZuulFilter {


    @Autowired
    private LogService logService;

    @Override
    public String filterType() {
        return ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return SEND_RESPONSE_FILTER_ORDER + 1;
    }

    /**
     * filter执行策略
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        this.logService.send(RequestContext.getCurrentContext());
        return null;
    }
}
