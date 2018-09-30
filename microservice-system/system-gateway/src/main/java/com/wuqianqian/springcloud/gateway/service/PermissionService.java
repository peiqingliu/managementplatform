package com.wuqianqian.springcloud.gateway.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liupeqing
 * @date 2018/9/26 20:39
 *
 * 权限接口
 */
public interface PermissionService {

    /**
     * 判断是否有权限
     * @param request
     * @param authentication
     * @return
     * Authentication 鉴定
     */
    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
