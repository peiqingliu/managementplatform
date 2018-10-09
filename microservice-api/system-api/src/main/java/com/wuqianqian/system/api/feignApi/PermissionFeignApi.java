package com.wuqianqian.system.api.feignApi;

import com.wuqianqian.system.api.hystrix.PermissionFeignApiHystrix;
import com.wuqianqian.system.api.module.AuthPermission;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

/**
 * @author liupeqing
 * @date 2018/9/26 20:52
 */
@FeignClient(name = "business-admin-server",fallback = PermissionFeignApiHystrix.class)
public interface PermissionFeignApi {

    /**
     * 通过角色code查询菜单
     * @param roleCode
     * @return
     */
    @GetMapping(value = "/api/findMenuByRole/{roleCode}")
    Set<AuthPermission> findMenuByRole(@PathVariable("roleCode") String roleCode);
}
