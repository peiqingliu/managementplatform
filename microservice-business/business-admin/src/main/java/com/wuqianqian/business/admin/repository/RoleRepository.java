package com.wuqianqian.business.admin.repository;

import com.wuqianqian.business.admin.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author liupeqing
 * @date 2018/9/27 17:02
 */
public interface RoleRepository extends JpaRepository<Role, Integer>,
        QueryDslPredicateExecutor<Role> {

    Role findRoleByRoleCode(String roleCode);
}
