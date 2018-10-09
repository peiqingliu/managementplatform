package com.wuqianqian.business.admin.web.rpc;

import com.wuqianqian.business.admin.service.MenuService;
import com.wuqianqian.business.commons.permisssion.Module;
import com.wuqianqian.business.commons.web.BaseController;
import com.wuqianqian.business.commons.web.aop.PrePermissions;
import com.wuqianqian.core.beans.AuthMenu;
import com.wuqianqian.springcloud.StringHelper;
import com.wuqianqian.system.api.feignApi.PermissionFeignApi;
import com.wuqianqian.system.api.module.AuthPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

/**
 * @author liupeqing
 * @date 2018/10/8 10:33
 */
@RestController
@PrePermissions(value = Module.API,required = false)
@Api(value = "API - PermissionFeignApi", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PermissionFeignApiClient extends BaseController implements PermissionFeignApi {

    @Autowired
    private MenuService menuService;

    @Override
    @ApiOperation(httpMethod = GET, value = "通过角色获取菜单权限")
    @ApiImplicitParam(name = "roleCode", value = "用户roleCode", required = true, dataType = "string", paramType = "path")
    public Set<AuthPermission> findMenuByRole(String roleCode) {

        Set<AuthPermission> permissions = new HashSet<AuthPermission>();
        if (StringHelper.isBlank(roleCode)) return permissions;
        Set<AuthMenu> menus = menuService.findMenuByRole(roleCode);
        if (null == menus || menus.size() == 0) return permissions;
        menus.stream().forEach(m -> permissions.add(new AuthPermission(m.getUrl())));
        return permissions;
    }
}
