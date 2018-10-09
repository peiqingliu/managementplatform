package com.wuqianqian.springcloud.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.wuqianqian.core.commons.constants.SecurityConstant;
import com.xiaoleilu.hutool.collection.CollectionUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.FORM_BODY_WRAPPER_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 在RateLimitPreFilter 之前执行，否则会出现空指针问题
 * @author liupeqing
 * @date 2018/10/8 19:13
 */
@Component
public class AccessFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FORM_BODY_WRAPPER_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        context.set("startTime",System.currentTimeMillis());
        // 传递 { @link SecurityConstant.ROLE_HEADER } 头部角色权限到下游请求
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null != authentication && !"anonymousUser".equals(authentication.getPrincipal().toString())){
            context.addZuulRequestHeader(SecurityConstant.USER_HEADER,authentication.getName());
            context.addZuulRequestHeader(SecurityConstant.ROLE_HEADER, CollectionUtil.join(authentication.getAuthorities(),","));
        }
        return null;
    }
}
