package com.wuqianqian.business.admin.repository;

import com.wuqianqian.business.admin.domain.Dept;
import com.wuqianqian.business.admin.domain.RoleDept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liupeqing
 * @date 2018/9/27 16:52
 */
@Repository
public interface DeptRepository extends JpaRepository<Dept,Integer>,QueryDslPredicateExecutor<Dept> {

    @Query(value = "select * from " + Dept.TABLE_NAME + "where dept_id in (select dept_id from " + RoleDept.TABLE_NAME + ")" , nativeQuery = true)
    List<Dept> findRoleAndDeptList();
}
