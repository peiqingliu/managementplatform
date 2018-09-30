package com.wuqianqian.springcloud.gateway.service;

import com.netflix.zuul.context.RequestContext;

/**
 * @author liupeqing
 * @date 2018/9/26 16:19
 *
 * 往消息通道发送消息
 */
public interface LogService {

    /**
     * 往消息通道发消息
     */
    void send(RequestContext requestContext);

}
