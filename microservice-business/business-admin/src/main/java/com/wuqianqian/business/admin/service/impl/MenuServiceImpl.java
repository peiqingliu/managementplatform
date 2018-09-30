package com.wuqianqian.business.admin.service.impl;

import com.wuqianqian.business.admin.cache.AdminCacheKey;
import com.wuqianqian.business.admin.domain.*;
import com.wuqianqian.business.admin.repository.MenuRepository;
import com.wuqianqian.business.admin.service.MenuService;
import com.wuqianqian.business.admin.service.ModuleService;
import com.wuqianqian.business.admin.service.RoleService;
import com.wuqianqian.business.commons.tree.MenuTree;
import com.wuqianqian.business.commons.web.jpa.JPAFactoryImpl;
import com.wuqianqian.core.beans.AuthMenu;
import com.wuqianqian.core.commons.constants.CommonConstant;
import com.wuqianqian.springcloud.StringHelper;
import com.wuqianqian.springcloud.tree.TreeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author liupeqing
 * @date 2018/9/28 10:52
 */
@Service
@CacheConfig(cacheNames = AdminCacheKey.MENU_INFO)
public class MenuServiceImpl extends JPAFactoryImpl implements MenuService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ModuleService moduleService;

    @Override
    public Set<AuthMenu> findMenuByRole(String roleCode) {
        if (StringHelper.isBlank(roleCode)) return null;

        Role role = roleService.findRoleByCode(roleCode.trim());
        if (null == role) return null;

        List<Menu> rList = this.findMenuByRoleId(role.getRoleId());
        if (null == rList || rList.size() == 0) return null;

        Set<AuthMenu> mList = new HashSet<AuthMenu>();
        for (Menu m : rList) {
            AuthMenu authMenu = new AuthMenu();
            BeanUtils.copyProperties(m, authMenu);
            mList.add(authMenu);
        }
        return mList;
    }

    @Override
    public Boolean deleteMenu(Integer menuId, String roleCode) {
        // 删除当前节点 -- 假删除
        QMenu qMenu = QMenu.menu;
        this.jpaQueryFactory.update(qMenu).set(qMenu.statu, CommonConstant.STATUS_DEL).where(
                qMenu.menuId.eq(menuId)).execute();

        // 删除父节点为当前节点的节点 -- 假删除
        this.jpaQueryFactory.update(qMenu).set(qMenu.statu, CommonConstant.STATUS_DEL).where(
                qMenu.pid.eq(menuId)).execute();

        return true;
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public Boolean updateMenuById(Menu sysMenu, String roleCode) {
        if (null == sysMenu || null == sysMenu.getMenuId()) return null;
        menuRepository.saveAndFlush(sysMenu);
        return true;
    }

    @Override
    public List<MenuTree> findRoleMenuTree(String roleCode) {
        Set<AuthMenu> menuList = findMenuByRole(roleCode);
        List<MenuTree> menuTreeList = new ArrayList<MenuTree>();
        menuList.forEach(menu -> {
            menuTreeList.add(new MenuTree(menu));
        });
        return TreeUtil.build(menuTreeList, 0);
    }

    // 目前只支持二级菜单，若有三级，则改递归即可
    @Override
    public List<MenuTree> findAllMenuTree() {
        List<Menu> rList = this.findMenuList();

        List<Module> moduleList = this.moduleService.getAllList();
        List<MenuTree> menuTreeList = new ArrayList<MenuTree>();
        rList.forEach(menu -> {
            if (menu.getPid() != 0 && null != moduleList && moduleList.size() > 0) {
                moduleList.forEach(m -> {
                    String id = menu.getPath() + "_" + m.getCode();
                    menuTreeList.add(new MenuTree(id, menu.getMenuId() + "", m.getName()));
                });
            }
            menuTreeList.add(new MenuTree(menu.getMenuId() + "", menu.getPid() + "", menu
                    .getMenuName()));
        });

        return TreeUtil.build(menuTreeList, "0");
    }

    @Override
    public List<MenuTree> findAllMenuTreeList() {
        List<Menu> rList = this.findMenuList();

        List<MenuTree> menuTreeList = new ArrayList<MenuTree>();
        rList.forEach(menu -> {
            AuthMenu authMenu = new AuthMenu();
            BeanUtils.copyProperties(menu, authMenu);
            menuTreeList.add(new MenuTree(authMenu));
        });

        return TreeUtil.build(menuTreeList, "0");
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean delById(Integer menuId) {
        QMenu qMenu = QMenu.menu;
        long num = this.jpaQueryFactory.delete(qMenu).where(qMenu.menuId.eq(menuId.intValue()))
                .execute();
        return num > 0;
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public Menu saveOrUpdate(Menu menu) {
        if (null == menu) return null;

        return menuRepository.saveAndFlush(menu);
    }

    @Override
    @Cacheable(key = "'menu_list'")
    public List<Menu> findMenuList() {
        QMenu qMenu = QMenu.menu;
        List<Menu> rList = this.jpaQueryFactory.selectFrom(qMenu).fetch();

        return rList;
    }

    @Override
    @Cacheable(key = "'menu_' + #roleId")
    public List<Menu> findMenuByRoleId(Integer roleId) {
        if (null == roleId) return null;

        QRoleMenu qRoleMenu = QRoleMenu.roleMenu;
        QMenu qMenu = QMenu.menu;
        List<Menu> rList = this.jpaQueryFactory.select(qMenu).from(qRoleMenu, qMenu).where(
                qRoleMenu.roleId.eq(roleId)).where(qRoleMenu.menuId.eq(qMenu.menuId)).fetch();
        return rList;
    }
}
