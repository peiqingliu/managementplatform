package com.wuqianqian.business.admin.repository;

import com.wuqianqian.business.admin.domain.LogInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author liupeqing
 * @date 2018/9/27 16:59
 */
@Repository
public interface LogInfoRepository extends JpaRepository<LogInfo, Long>,
        QueryDslPredicateExecutor<LogInfo> {
}
