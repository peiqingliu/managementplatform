package com.wuqianqian.business.admin.service;

import com.wuqianqian.business.admin.beans.RoleDeptBean;
import com.wuqianqian.business.admin.domain.Role;
import com.wuqianqian.springcloud.page.PageBean;
import com.wuqianqian.springcloud.page.PageParams;

import java.util.List;

/**
 * @author liupeqing
 * @date 2018/9/27 17:15
 */
public interface RoleService {

    List<Role> findRoleListByDeptId(Integer deptId);

    List<Role> getRoleList();

    PageBean<RoleDeptBean> findAll(PageParams pageParams, Role role);

    Role saveOrUpdate(Role role);

    Role saveRoleAndDept(Role role);

    boolean delById(Integer roleId);

    Role findRoleByCode(String roleCode);
}
