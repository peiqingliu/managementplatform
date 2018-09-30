package com.wuqianqian.business.admin.service.impl;

import com.wuqianqian.business.admin.cache.AdminCacheKey;
import com.wuqianqian.business.admin.domain.*;
import com.wuqianqian.business.admin.repository.RoleMenuPermissionRepository;
import com.wuqianqian.business.admin.repository.RoleMenuRepository;
import com.wuqianqian.business.admin.service.PermissionService;
import com.wuqianqian.business.admin.service.RoleService;
import com.wuqianqian.business.commons.web.jpa.JPAFactoryImpl;
import com.wuqianqian.springcloud.StringHelper;
import com.wuqianqian.springcloud.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author liupeqing
 * @date 2018/9/28 12:41
 */
@Service
public class PermissionServiceImpl extends JPAFactoryImpl implements PermissionService {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuRepository roleMenuRepository;
    @Autowired
    private RoleMenuPermissionRepository roleMenuPermissionRepository;

    @Override
    public Set<String> findMenuPermissions(String roleCode) {

        Set<String> permissions = new  HashSet<String>();
        //查询role
        Role role = this.roleService.findRoleByCode(roleCode);
        if (null == role) return permissions;
        List<RoleMenuPermission> rList = this.findMenuPermissionByRoleId(role.getRoleId());
        if (null == rList || rList.size() == 0) return permissions;

        //使用流的形式对list遍历
        rList.stream().forEach(r -> {
            permissions.add(r.getPermission());
        });

        return permissions;

    }

    @CacheEvict(value = { AdminCacheKey.PERMISSION_INFO, AdminCacheKey.MENU_INFO,
            AdminCacheKey.MODULE_INFO, AdminCacheKey.ROLE_INFO }, allEntries = true)
    @Transactional
    @Override
    public boolean updateRoleMenuPermissions(String roleCode, String... permissions) {
        Role role = roleService.findRoleByCode(roleCode.trim());
        if (null == role) return false;

        // 菜单集合
        Map<Integer, List<String>> roleMenuIdList = new HashMap<Integer, List<String>>();
        for (String permission : permissions) {
            if (!permission.contains("|")) continue;
            String[] menuPermissions = permission.split("\\|");
            Integer menuId = WebUtils.parseStrToInteger(menuPermissions[0], null);
            if (null == menuId || StringHelper.isBlank(menuPermissions[1])) continue;
            if (!roleMenuIdList.containsKey(menuId)) {
                roleMenuIdList.put(menuId, new ArrayList<String>());
            }
            roleMenuIdList.get(menuId).add(menuPermissions[1].trim());
        }

        // 删除RoleMenu和RoleMenuPermission
        this.delRoleMenuPermission(role.getRoleId());

        if (roleMenuIdList.size() > 0) {
            roleMenuIdList.forEach((menuId, menuPermissions) -> {
                RoleMenu menuRole = new RoleMenu();
                menuRole.setMenuId(menuId);
                menuRole.setRoleId(role.getRoleId());
                menuRole = roleMenuRepository.saveAndFlush(menuRole);
                Integer menuRoleId = menuRole.getId();

                menuPermissions.forEach(p -> {
                    String permission = p.trim();
                    if (permission.contains("_")) {
                        RoleMenuPermission roleMenuPermission = new RoleMenuPermission();
                        roleMenuPermission.setRoleMenuId(menuRoleId);
                        roleMenuPermission.setPermission(permission);
                        roleMenuPermissionRepository.saveAndFlush(roleMenuPermission);
                    }
                });
            });
        }

        return true;
    }

    private boolean delRoleMenuPermission(Integer roleId) {
        QRoleMenu qRoleMenu = QRoleMenu.roleMenu;
        List<RoleMenu> roleMeunList = this.jpaQueryFactory.selectFrom(qRoleMenu).where(
                qRoleMenu.roleId.eq(roleId.intValue())).fetch();

        Set<Integer> roleMenuIdList = new HashSet<Integer>();
        roleMeunList.forEach(r -> {
            roleMenuIdList.add(r.getId());
            roleMenuRepository.delete(r.getId());
        });

        Integer[] roleMenuIdArray = new Integer[roleMenuIdList.size()];
        return this.delRoleMenuPermissionByRoleMenuId(roleMenuIdList.toArray(roleMenuIdArray));
    }

    private boolean delRoleMenuPermissionByRoleMenuId(Integer... roleMenuArray) {
        QRoleMenuPermission qRoleMenuPermission = QRoleMenuPermission.roleMenuPermission;
        long num = this.jpaQueryFactory.delete(qRoleMenuPermission).where(
                qRoleMenuPermission.roleMenuId.in(roleMenuArray)).execute();
        return num > 0;
    }

    @Override
    @Cacheable(cacheNames = AdminCacheKey.PERMISSION_INFO, key = "'permission_' + #roleId")
    public List<RoleMenuPermission> findMenuPermissionByRoleId(Integer roleId) {
        if (null == roleId) return null;
        //查询菜单
        QRoleMenu qRoleMenu = QRoleMenu.roleMenu;
        QRoleMenuPermission qRoleMenuPermission = QRoleMenuPermission.roleMenuPermission;
        List<RoleMenuPermission> rList = this.jpaQueryFactory.select(qRoleMenuPermission).from(
                qRoleMenuPermission, qRoleMenu).where(qRoleMenu.roleId.eq(roleId)).where(
                qRoleMenuPermission.roleMenuId.eq(qRoleMenu.id)).fetch();
        return rList;
    }
}
