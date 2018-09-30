package com.wuqianqian.business.admin.repository;

import com.wuqianqian.business.admin.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author liupeqing
 * @date 2018/9/27 17:03
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer>,
        QueryDslPredicateExecutor<UserRole> {
}
