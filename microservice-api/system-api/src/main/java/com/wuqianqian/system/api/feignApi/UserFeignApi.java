package com.wuqianqian.system.api.feignApi;

import com.wuqianqian.system.api.hystrix.UserFeignApiHystrix;
import com.wuqianqian.system.api.module.AuthUser;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author liupeqing
 * @date 2018/10/8 10:30
 */
@FeignClient(name = "business-admin-server",fallback = UserFeignApiHystrix.class)
public interface UserFeignApi {

    /**
     * 通过用户名查询用户、角色信息
     *
     * @param username
     *            用户名
     */
    @GetMapping("/api/findUserByUsername/{username}")
    AuthUser findUserByUsername(@PathVariable("username") String username);

    /**
     * 通过手机号查询用户、角色信息
     */
    @GetMapping("/api/findUserByMobile/{mobile}")
    AuthUser findUserByMobile(@PathVariable("mobile") String mobile);
}
