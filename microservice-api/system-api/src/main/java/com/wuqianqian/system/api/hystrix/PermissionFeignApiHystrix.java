package com.wuqianqian.system.api.hystrix;

import com.wuqianqian.system.api.feignApi.PermissionFeignApi;
import com.wuqianqian.system.api.module.AuthPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author liupeqing
 * @date 2018/10/8 10:28
 */
@Slf4j
@Service
public class PermissionFeignApiHystrix implements PermissionFeignApi {

    @Override
    public Set<AuthPermission> findMenuByRole(String roleCode) {
        log.error("调用{}异常:{}", "findMenuByRole", roleCode);
        return null;
    }
}
