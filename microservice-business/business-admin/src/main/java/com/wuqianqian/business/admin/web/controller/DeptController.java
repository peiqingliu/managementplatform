package com.wuqianqian.business.admin.web.controller;

import com.wuqianqian.business.admin.domain.Dept;
import com.wuqianqian.business.admin.service.DeptService;
import com.wuqianqian.business.commons.permisssion.Functional;
import com.wuqianqian.business.commons.permisssion.Module;
import com.wuqianqian.business.commons.tree.DeptTree;
import com.wuqianqian.business.commons.web.BaseController;
import com.wuqianqian.business.commons.web.aop.PrePermissions;
import com.wuqianqian.springcloud.R;
import com.wuqianqian.springcloud.StringHelper;
import com.wuqianqian.springcloud.page.PageBean;
import com.wuqianqian.springcloud.page.PageParams;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 部门管理 controller
 * @author liupeqing
 * @date 2018/9/29 15:04
 */
@RestController
@RequestMapping("/dept")
@ApiModel(description = "部门管理服务")
@PrePermissions(value = Module.DEPT)
public class DeptController extends BaseController {

    @Autowired
    private DeptService deptService;

    /**
     * 部门列表
     * @param request
     * @param pageParams
     * @param dept
     * @return
     */
    @ApiOperation(value = "查询", notes = "部门分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dept",value = "部门实体dept",required = true, dataType = "dept"),
            @ApiImplicitParam(name = "pageParams",value = "分页参数",required = true,dataType = "pageParams")
    })
    @GetMapping(value = "/list")
    @PrePermissions(value = Functional.VIEW)
    public R<PageBean<Dept>> list(HttpServletRequest request, PageParams pageParams,Dept dept){
        PageBean<Dept> pageBean = this.deptService.findAll(pageParams,dept);
        return new R<PageBean<Dept>>().data(pageBean);
    }

    /**
     * 根据id获取部门信息
     * @param id
     * @return
     */
    @GetMapping("/find/{id}")
    @ApiImplicitParam(name = "id", value = "", example = "0", required = true, dataType = "int", paramType = "path")
    public R<Dept> find(@PathVariable Integer id) {
        return new R<Dept>().data(deptService.findById(id)).success();
    }

    /**
     * 部门树信息
     * @return
     */
    @GetMapping(value = "/tree")
    @ApiOperation(value = "查询", notes = "部门Tree信息")
    @PrePermissions(value = Functional.VIEW)
    public R<List<DeptTree>> getDeptTreeList() {
        return new R<List<DeptTree>>().data(deptService.getDeptTreeList());
    }


    /**
     * 部门新增
     * @param request
     * @param dept
     * @return
     */
    @ApiOperation(value = "新增", notes = "部门信息")
    @ApiImplicitParam(name = "dept", value = "", required = true, dataType = "Dept")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @PrePermissions(value = Functional.ADD)
    public R<Boolean> add(HttpServletRequest request, @RequestBody Dept dept) {
        if (null == dept) return new R<Boolean>().failure("部门信息不能为空");
        if (null == dept.getPid() || dept.getPid() < 0)
            return new R<Boolean>().failure("上级部门不能为空");
        if (StringHelper.isBlank(dept.getDeptName()))
            return new R<Boolean>().failure("部门名称不能为空");

        dept.setDeptId(null);
        dept.setPos(null != dept.getPos() ? dept.getPos() : 0);
        dept.setCreateTime(new Date());
        dept.setUpdateTime(new Date());
        dept.setStatu(0);
        Dept dbDept = deptService.saveOrUpdate(dept);

        return new R<Boolean>().data(null != dbDept);
    }

    /**
     * 部门更新
     * @param request
     * @param dept
     * @return
     */
    @ApiOperation(value = "修改", notes = "部门信息")
    @ApiImplicitParam(name = "dept", value = "", required = true, dataType = "Dept")
    @RequestMapping(value = "/upd", method = RequestMethod.POST)
    @PrePermissions(value = Functional.UPD)
    public R<Boolean> upd(HttpServletRequest request, @RequestBody Dept dept) {
        if (null == dept) return new R<Boolean>().failure("部门信息不能为空");
        if (null == dept.getDeptId())
            return new R<Boolean>().failure("部门信息不存在");
        if (StringHelper.isBlank(dept.getDeptName()))
            return new R<Boolean>().failure("部门名称不能为空");

        Dept dbDept = deptService.findById(dept.getDeptId());
        if (null == dbDept)
            return new R<Boolean>().failure("部门不存在");

        dbDept.setUpdateTime(new Date());
        dbDept.setStatu(dept.getStatu());
        dbDept.setDeptName(dept.getDeptName());
        dbDept.setPos(null != dept.getPos() ? dept.getPos() : dbDept.getPos());

        Dept exDept = deptService.saveOrUpdate(dbDept);
        return new R<Boolean>().data(null != exDept);
    }

    /**
     * 部门删除
     * @param request
     * @param id
     * @return
     */
    @ApiOperation(value = "删除", notes = "部门信息")
    @ApiImplicitParam(name = "id", value = "", required = true, dataType = "int", paramType = "path")
    @RequestMapping(value = "/del/{id}", method = RequestMethod.POST)
    @PrePermissions(value = Functional.DEL)
    public R<Boolean> del(HttpServletRequest request, @PathVariable Integer id) {
        if (null == id || id.intValue() <= 0) return new R<Boolean>().failure("部门id为空");
        boolean isDel = deptService.delById(id);
        return new R<Boolean>().data(isDel);
    }
}
