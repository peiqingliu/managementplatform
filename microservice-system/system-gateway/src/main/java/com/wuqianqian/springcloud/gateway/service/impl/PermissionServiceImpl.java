package com.wuqianqian.springcloud.gateway.service.impl;

import com.wuqianqian.springcloud.gateway.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 *  权限接口实现
 * @author liupeqing
 * @date 2018/9/26 20:41
 */
@Slf4j
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        return false;
    }
}
