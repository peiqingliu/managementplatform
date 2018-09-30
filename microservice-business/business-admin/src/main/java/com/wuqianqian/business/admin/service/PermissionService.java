package com.wuqianqian.business.admin.service;

import com.wuqianqian.business.admin.domain.RoleMenuPermission;

import java.util.List;
import java.util.Set;

/**
 * 权限服务接口
 * @author liupeqing
 * @date 2018/9/27 17:14
 */
public interface PermissionService {

    /**
     * 通过角色获取菜单权限列表
     *
     * @param roleCode
     *            角色
     * @return 权限列表
     */
    Set<String> findMenuPermissions(String roleCode);

    /**
     * 更新角色-菜单-权限关联关系
     *
     * @param roleCode
     *            角色编码
     * @param permissions
     *            权限列表 菜单Id|菜单path_module名称 ex: 2|user_add
     *        String... permissions     叫可变长度参数列表，其语法就是类型后跟…，表示此处接受的参数为0到多个Object类型的对象
     *        调用的时候，可以传递多个参数(数组的形式)
     * @return
     */
    boolean updateRoleMenuPermissions(String roleCode, String... permissions);

    /**
     * 通过roleId 获取相关菜单功能权限
     */
    List<RoleMenuPermission> findMenuPermissionByRoleId(Integer roleId);
}
