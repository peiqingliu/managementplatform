package com.wuqianqian.business.admin.web.controller;

import com.wuqianqian.business.admin.beans.RoleDeptBean;
import com.wuqianqian.business.admin.domain.Role;
import com.wuqianqian.business.admin.service.PermissionService;
import com.wuqianqian.business.admin.service.RoleService;
import com.wuqianqian.business.commons.permisssion.Functional;
import com.wuqianqian.business.commons.permisssion.Module;
import com.wuqianqian.business.commons.web.BaseController;
import com.wuqianqian.business.commons.web.aop.PrePermissions;
import com.wuqianqian.core.configuration.ApiTag;
import com.wuqianqian.springcloud.R;
import com.wuqianqian.springcloud.StringHelper;
import com.wuqianqian.springcloud.page.PageBean;
import com.wuqianqian.springcloud.page.PageParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author liupeqing
 * @date 2018/9/29 15:39
 */
@Api(value = "角色信息管理",tags = ApiTag.TAG_DEFAULT)
@RestController
@RequestMapping("/role")
@PrePermissions(value = Module.ROLE)
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 通过部门id获取角色列表
     * @param deptId
     * @return
     */
    @ApiOperation(value = "查询",notes = "通过部门id获取角色列表")
    @ApiImplicitParam(name = "deptId", value = "", required = true, dataType = "int", paramType = "path")
    @GetMapping("/findRoleList/{deptId}")
    @PrePermissions(value = Functional.VIEW)
    public R<List<Role>> findRoleList(@PathVariable("depetId") Integer deptId){
        return new R<List<Role>>().data(roleService.findRoleListByDeptId(deptId));
    }


    /**
     * 查询角色列表
     * @return
     */
    @ApiOperation(value = "查询", notes = "查询角色列表数据")
    @GetMapping(value = "/listAll")
    @PrePermissions(value = Functional.VIEW)
    public R<List<Role>> getRoleList() {
        return new R<List<Role>>().data(roleService.getRoleList());
    }

    /**
     * 分页查询
     * @param request
     * @param role
     * @param pageParams
     * @return
     */
    @ApiOperation(value = "查询", notes = "角色分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "role", value = "角色实体", required = true, dataType = "Role"),
            @ApiImplicitParam(name = "pageParams", value = "分页pageParams", required = true, dataType = "PageParams") })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @PrePermissions(value = Functional.VIEW)
    public R<PageBean<RoleDeptBean>> list(HttpServletRequest request, Role role, PageParams pageParams) {
        PageBean<RoleDeptBean> pageData = this.roleService.findAll(pageParams, role);
        return new R<PageBean<RoleDeptBean>>().data(pageData);
    }

    @ApiOperation(value = "新增" , notes = "角色新增")
    @ApiImplicitParam(name = "role",value = "角色实体",required = true,dataType = "Role")
    @PostMapping("/add")
    public R<Boolean> add(HttpServletRequest request,@RequestBody Role role){
        if (null == role) return new R<Boolean>().failure("角色信息为空!");

        //检验权限编码是否存在
        Role exRole = this.roleService.findRoleByCode(role.getRoleCode());
        if (exRole != null) return new R<Boolean>().failure("角色编码以存在!");
        if (null == role.getDeptId()) new R<Boolean>().failure("请选择角色所属部门");
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        role.setStatu(0);
        Role updateObj = roleService.saveRoleAndDept(role);
        return new R<Boolean>().data(null != updateObj);
    }


    @ApiOperation(value = "修改", notes = "角色", produces = "application/json")
    @ApiImplicitParam(name = "role", value = "", required = true, dataType = "Dict")
    @RequestMapping(value = "/upd", method = RequestMethod.POST)
    @PrePermissions(value = Functional.UPD)
    public R<Boolean> upd(HttpServletRequest request, @RequestBody Role role) {
        if (null == role || null == role.getRoleId() || role.getRoleId() <= 0)
            return new R<Boolean>().failure("角色信息为空");
        role.setUpdateTime(new Date());
        if (null == role.getDeptId()) return new R<Boolean>().failure("请选择角色所属部门");

        Role updateObj = roleService.saveRoleAndDept(role);
        return new R<Boolean>().data(null != updateObj);
    }

    @ApiOperation(value = "删除", notes = "角色")
    @ApiImplicitParam(name = "roleId", value = "", required = true, dataType = "int", paramType = "path")
    @RequestMapping(value = "/del/{roleId}", method = RequestMethod.POST)
    @PrePermissions(value = Functional.DEL)
    public R<Boolean> del(HttpServletRequest request, @PathVariable Integer roleId) {
        if (null == roleId || roleId.intValue() <= 0)
            return new R<Boolean>().failure("角色roleId为空");
        boolean isDel = roleService.delById(roleId);
        return new R<Boolean>().data(isDel);
    }

    @RequestMapping(value = "/updRoleMenuPermission", method = RequestMethod.POST, consumes = "application/json")
    @PrePermissions(value = Functional.UPD)
    public R<Boolean> updRoleMenuPermission(HttpServletRequest request,
                                            @RequestBody Map<String, Object> params) {
        String roleCode = params.get("roleCode") + "";
        if (StringHelper.isBlank(roleCode)) return new R<Boolean>().failure("权限编码不能为空");
        String permissionstr = params.get("permissions") + "";
        String[] permissions = permissionstr.split(",");
        if (null == permissions || permissions.length <= 0) permissions = new String[] {};
        boolean isDel = permissionService.updateRoleMenuPermissions(roleCode, permissions);
        return new R<Boolean>().data(isDel);
    }
}
