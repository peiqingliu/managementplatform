package com.wuqianqian.business.admin.web.controller;

import com.wuqianqian.business.admin.beans.UserForm;
import com.wuqianqian.business.admin.domain.User;
import com.wuqianqian.business.admin.service.UserService;
import com.wuqianqian.business.commons.permisssion.Functional;
import com.wuqianqian.business.commons.permisssion.Module;
import com.wuqianqian.business.commons.web.BaseController;
import com.wuqianqian.business.commons.web.aop.PrePermissions;
import com.wuqianqian.core.configuration.ApiTag;
import com.wuqianqian.springcloud.R;
import com.wuqianqian.springcloud.page.PageBean;
import com.wuqianqian.springcloud.page.PageParams;
import com.wuqianqian.system.api.module.AuthUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户管理 controller
 * @author liupeqing
 * @date 2018/9/29 16:00
 */
@RestController
@Api(value = "用户信息", tags = ApiTag.TAG_DEFAULT)
@RequestMapping("/user")
@PrePermissions(value = Module.USER)
public class UserController extends BaseController {


    @Autowired
    private UserService userService;

    /**
     * 查询用户列表数据
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @PrePermissions(value = Functional.VIEW)
    public R<PageBean<User>> list(HttpServletRequest request, User user, PageParams pageParams) {
        PageBean<User> pageData = this.userService.findAll(pageParams, user);
        return new R<PageBean<User>>().data(pageData);
    }

    /**
     * 通过userId删除数据
     */
    @ApiOperation(value = "删除用户", notes = "根据userId删除用户")
    @ApiImplicitParam(name = "userId", value = "用户userId", required = true, dataType = "int", paramType = "path")
    @RequestMapping(value = "/del/{userId}", method = RequestMethod.POST)
    @PrePermissions(value = Functional.DEL)
    public R<Boolean> list(HttpServletRequest request, @PathVariable("userId") Integer userId) {
        return new R<Boolean>().data(this.userService.delByUserId(userId));
    }

    /**
     * 通过userId 查询信息
     */
    @ApiOperation(value = "查询用户信息", notes = "根据userId查询用户信息")
    @ApiImplicitParam(name = "userId", value = "用户userId", required = true, dataType = "int", paramType = "path")
    @RequestMapping(value = "/find/{userId}", method = RequestMethod.GET)
    @PrePermissions(value = Functional.VIEW)
    public R<AuthUser> findByUserId(HttpServletRequest request,
                                    @PathVariable("userId") Integer userId) {
        AuthUser authUser = this.userService.findByUserId(String.valueOf(userId));
        if (null == authUser) return new R<AuthUser>().failure();
        return new R<AuthUser>().success().data(authUser);
    }

    /**
     * 添加用户
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @PrePermissions(value = Functional.ADD)
    public R<Boolean> add(HttpServletRequest request, @RequestBody UserForm userForm) {
        if (null == userForm.getDeptId()) return new R<Boolean>().failure("请选择部门");
        if (null == userForm.getRoleId()) return new R<Boolean>().failure("请选择角色");
        boolean r = this.userService.addUserAndRoleDept(userForm);
        return new R<Boolean>().data(r);
    }

    /**
     * 修改用户
     */
    @RequestMapping(value = "/upd", method = RequestMethod.POST)
    @PrePermissions(value = Functional.UPD)
    public R<Boolean> upd(HttpServletRequest request, @RequestBody UserForm userForm) {
        if (null == userForm.getUserId()) return new R<Boolean>().failure("用户不存在");
        if (null == userForm.getDeptId()) return new R<Boolean>().failure("请选择部门");
        if (null == userForm.getRoleId()) return new R<Boolean>().failure("请选择角色");

        boolean r = this.userService.updateUserAndRoleDept(userForm);
        return new R<Boolean>().data(r);
    }

    /**
     * 修改用户密码
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @PrePermissions(value = Functional.UPD)
    public R<Boolean> modify(HttpServletRequest request, @RequestBody UserForm userForm) {
        if (null == userForm.getUsername()) return new R<Boolean>().failure("用户名不存在");
        if (null == userForm.getPassword()) return new R<Boolean>().failure("请输入旧密码");
        if (null == userForm.getNewpassword()) return new R<Boolean>().failure("请输入新密码");

        User user = this.userService.findUserByUsername(userForm.getUsername().trim(), false);
        if (null == user) return new R<Boolean>().failure("用户名不存在");

        if (!new BCryptPasswordEncoder().matches(userForm.getPassword().trim(), user.getPassword()))
            return new R<Boolean>().failure("旧密码输入错误！");

        user.setPassword(new BCryptPasswordEncoder().encode(userForm.getNewpassword().trim()));

        boolean r = this.userService.updateUser(user);

        return new R<Boolean>().data(r);
    }
}
