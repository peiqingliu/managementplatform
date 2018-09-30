package com.wuqianqian.business.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wuqianqian.business.admin.beans.RoleDeptBean;
import com.wuqianqian.business.admin.cache.AdminCacheKey;
import com.wuqianqian.business.admin.domain.QRole;
import com.wuqianqian.business.admin.domain.QRoleDept;
import com.wuqianqian.business.admin.domain.Role;
import com.wuqianqian.business.admin.domain.RoleDept;
import com.wuqianqian.business.admin.repository.RoleDeptRepository;
import com.wuqianqian.business.admin.repository.RoleRepository;
import com.wuqianqian.business.admin.service.RoleService;
import com.wuqianqian.business.commons.utils.PageUtils;
import com.wuqianqian.business.commons.web.jpa.JPAFactoryImpl;
import com.wuqianqian.springcloud.StringHelper;
import com.wuqianqian.springcloud.WebUtils;
import com.wuqianqian.springcloud.page.PageBean;
import com.wuqianqian.springcloud.page.PageParams;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liupeqing
 * @date 2018/9/28 11:00
 */
@Service
@CacheConfig(cacheNames = AdminCacheKey.ROLE_INFO)
public class RoleServiceImpl extends JPAFactoryImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleDeptRepository roleDeptRepository;

    @Override
    @Cacheable(key = " 'role_deptid_' + #deptId")
    public List<Role> findRoleListByDeptId(Integer deptId) {
        if (null == deptId) return null;
        QRole qRole = QRole.role;
        QRoleDept qRoleDept = QRoleDept.roleDept;
        List<Role> list = this.jpaQueryFactory.select(qRole).
                from(qRoleDept,qRole).
                where(qRoleDept.roleId.eq(qRole.roleId)).
                where(qRoleDept.deptId.eq(deptId))
                .fetch();
        return list;
    }

    @Override
    @Cacheable(key = "'role_list'")
    public List<Role> getRoleList() {
        return this.roleRepository.findAll();
    }

    @Override
    @Cacheable(key = "'page_role_' + #p0.currentPage + '_' + #p0.pageSize + '_' + #p1.roleName + '_' + #p1.roleCode")
    public PageBean<RoleDeptBean> findAll(PageParams pageParams, Role role) {


        // 复杂SQL举例查询
        // 总记录数
        StringBuilder countSql = new StringBuilder();
        countSql.append("select count(t1.role_id) from " + Role.TABLE_NAME + " t1 ");

        // 查询语句
        StringBuilder querySql = new StringBuilder();
        querySql.append("select ")
                .append("t1.role_id, t1.role_name, t1.role_code, t1.role_desc, t1.create_time, t1.update_time, t1.statu,")
                .append("t3.dept_id, t3.pid, t3.dept_name ")
                .append("from " + Role.TABLE_NAME + " t1 ")
                .append("left join t_sys_role_dept t2 on t1.role_id = t2.role_id ")
                .append("left join t_sys_dept t3 on t3.dept_id = t2.dept_id ");

        // where语句
        StringBuilder whereSql = new StringBuilder("where 1=1 ");
        if (StringHelper.isNotBlank(role.getRoleName())) {
            whereSql.append("and t1.role_name like ")
                    .append("'%" + role.getRoleName().trim() + "%' escape '!' ");
        }

        if (StringHelper.isNotBlank(role.getRoleCode())) {
            whereSql.append("and t1.role_code like ")
                    .append("'%" + role.getRoleCode().trim() + "%' escape '!' ");
        }

        // 结果集列表
        List<RoleDeptBean> rdList = new ArrayList<RoleDeptBean>();

        Object countResult = this.em.createNativeQuery(countSql.append(whereSql).toString()).getSingleResult();
        Long resultCount = WebUtils.parseStrToLong(countResult + "", 0l);
        if (null != resultCount && resultCount > 0) {
            // order 语句
            StringBuilder orderSql = new StringBuilder("order by t1.update_time desc ");

            Query query = this.em.createNativeQuery(querySql.append(whereSql).append(orderSql).toString())
                    .setFirstResult((pageParams.getCurrentPage() - 1) * pageParams.getPageSize())
                    .setMaxResults(pageParams.getPageSize());

            // 下面转换为map （效率相对差一点）, 否则为 object[]
            query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> dList = query.getResultList();
            if (null != dList) {
                dList.forEach(d -> {
                    RoleDeptBean roleDeptBean = JSONObject.parseObject(JSONObject.toJSONString(d), RoleDeptBean.class);
                    rdList.add(roleDeptBean);
                });
            }
        }

        return PageUtils.of(rdList, resultCount, pageParams.getCurrentPage(), pageParams.getPageSize());
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public Role saveOrUpdate(Role role) {
        if (null == role) return null;

        Role dbRole = this.roleRepository.saveAndFlush(role);

        return dbRole;
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public Role saveRoleAndDept(Role role) {
        if (null == role || null == role.getDeptId() || role.getDeptId() < 0) return null;

        Role dbRole = this.roleRepository.saveAndFlush(role);

        // 删除之前保存的部门角色关系
        QRoleDept qRoleDept = QRoleDept.roleDept;
        this.jpaQueryFactory.delete(qRoleDept)
                .where(qRoleDept.roleId.eq(dbRole.getRoleId()))
                .execute();

        RoleDept roleDept = new RoleDept();
        roleDept.setDeptId(role.getDeptId());
        roleDept.setRoleId(dbRole.getRoleId());
        roleDeptRepository.saveAndFlush(roleDept);

        return dbRole;
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean delById(Integer roleId) {
        if (null == roleId || roleId <= 0) return Boolean.FALSE;

        QRole qRole = QRole.role;
        long num = this.jpaQueryFactory.update(qRole)
                .set(qRole.statu, 1) // 0 正常 1删除
                .where(qRole.roleId.eq(roleId.intValue()))
                .execute();

        return num > 0;
    }

    @Override
    @Cacheable(key = "'role_code_' + #roleCode")
    public Role findRoleByCode(String roleCode) {
        if (StringHelper.isBlank(roleCode)) return null;

        QRole qRole = QRole.role;
        return this.jpaQueryFactory.selectFrom(qRole)
                .where(qRole.roleCode.eq(roleCode.trim()))
                .fetchOne();
    }
}
