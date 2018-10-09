package com.wuqianqian.business.admin.web.rpc;

import com.wuqianqian.business.admin.service.UserService;
import com.wuqianqian.business.commons.permisssion.Module;
import com.wuqianqian.business.commons.web.BaseController;
import com.wuqianqian.business.commons.web.aop.PrePermissions;
import com.wuqianqian.springcloud.StringHelper;
import com.wuqianqian.system.api.feignApi.UserFeignApi;
import com.wuqianqian.system.api.module.AuthUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liupeqing
 * @date 2018/10/8 10:41
 */
@Slf4j
@RestController
@PrePermissions(value = Module.API, required = false)
@Api(value = "API - UserFeignApi", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserFeignApiClient extends BaseController implements UserFeignApi {

    @Autowired
    private UserService userService;

    /**
     * 通过用户名查询用户及其角色信息
     */
    @Override
    @ApiOperation(httpMethod = GET, value = "通过用户名获取用户信息")
    @ApiImplicitParam(name = "username", value = "用户username", required = true, dataType = "string", paramType = "path")
    public AuthUser findUserByUsername(@PathVariable("username") String username) {
        if (StringHelper.isBlank(username)) return null;

        return this.userService.findUserByUsername(username);
    }

    /**
     * 通过手机号码查询用户及其角色信息
     */
    @Override
    @ApiOperation(httpMethod = GET, value = "通过手机号获取用户信息")
    @ApiImplicitParam(name = "mobile", value = "用户mobile", required = true, dataType = "string", paramType = "path")
    public AuthUser findUserByMobile(@PathVariable("mobile") String mobile) {
        if (StringHelper.isBlank(mobile)) return null;

        return this.userService.findUserByMobile(mobile);
    }
}
