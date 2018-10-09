package com.wuqianqian.system.api.hystrix;

import com.wuqianqian.system.api.feignApi.UserFeignApi;
import com.wuqianqian.system.api.module.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author liupeqing
 * @date 2018/10/8 10:44
 */
@Slf4j
@Service
public class UserFeignApiHystrix implements UserFeignApi {
    @Override
    public AuthUser findUserByUsername(String username) {
        log.error("调用{}异常:{}", "findUserByUsername", username);
        return null;
    }

    @Override
    public AuthUser findUserByMobile(String mobile) {
        log.error("调用{}异常:{}", "findUserByMobile", mobile);
        return null;
    }
}
