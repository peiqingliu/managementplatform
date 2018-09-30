package com.wuqianqian.business.admin.repository;

import com.wuqianqian.business.admin.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author liupeqing
 * @date 2018/9/27 17:03
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>,
        QueryDslPredicateExecutor<User> {

    User findUserByUsername(String username);

    User findUserByMobile(String mobile);

    User findUserByOpenId(String openId);

    User findUserByUserId(Integer userId);
}
