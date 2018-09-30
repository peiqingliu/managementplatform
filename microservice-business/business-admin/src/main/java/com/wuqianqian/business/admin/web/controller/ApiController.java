package com.wuqianqian.business.admin.web.controller;

import com.wuqianqian.business.admin.beans.UserBean;
import com.wuqianqian.business.admin.domain.Dict;
import com.wuqianqian.business.admin.service.DictService;
import com.wuqianqian.business.admin.service.MenuService;
import com.wuqianqian.business.admin.service.PermissionService;
import com.wuqianqian.business.admin.service.UserService;
import com.wuqianqian.business.commons.permisssion.Module;
import com.wuqianqian.business.commons.web.BaseController;
import com.wuqianqian.business.commons.web.aop.PrePermissions;
import com.wuqianqian.core.beans.AuthMenu;
import com.wuqianqian.core.commons.jwt.JwtUtil;
import com.wuqianqian.core.configuration.JwtConfiguration;
import com.wuqianqian.springcloud.R;
import com.wuqianqian.system.api.module.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 接口
 * 无需经过网关权限验证
 * @author liupeqing
 * @date 2018/9/29 11:40
 */
@RestController
@RequestMapping("/api")
@PrePermissions(value = Module.API,required = false)
public class ApiController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private DictService dictService;

    private JwtConfiguration jwtConfiguration = new JwtConfiguration();

    /**
     * 获取当前的用户信息(角色，权限)
     * @param authUser
     * @return
     */
    @GetMapping("/userInfo")
    public R<UserBean> userBeanR(AuthUser authUser){
        return new R<UserBean>().data(userService.findUserInfo(authUser));
    }

    /**
     * 返回当前用户的树形结构
     * @param request
     * @return
     */
    @GetMapping("/userTree")
    public R<List<Integer>> userTree(HttpServletRequest request){
        String roleCode = JwtUtil.getRole(request,jwtConfiguration.getJwtkey()).get(0);
        Set<AuthMenu> menus = menuService.findMenuByRole(roleCode);
        List<Integer> menuList = new ArrayList<>();
        if (null == menuList || menuList.size() < 0 ) return new R<List<Integer>>().data(menuList);

        menus.stream().forEach(m -> {
            menuList.add(m.getMenuId());
        });
        return new R<List<Integer>>().data(menuList);
    }

    /**
     * 通过角色获取菜单相关权限列表
     * @param roleCode
     * @return
     */
    @GetMapping("/finMenuPermissions/{roleCode}")
    public Set<String> findMenuPermissionsBYColeCode(@PathVariable("roleCode") String roleCode){
        return this.permissionService.findMenuPermissions(roleCode);
    }

    /**
     * 通过type获取字典数据
     */
    @GetMapping("/dictType/{type}")
    public R<List<Dict>> findDictByType(@PathVariable String type) {
        return new R<List<Dict>>().data(dictService.getDictList(type));
    }
}
