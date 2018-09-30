package com.wuqianqian.business.admin.repository;

import com.wuqianqian.business.admin.domain.DeptRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author liupeqing
 * @date 2018/9/27 16:19
 */
@Repository
public interface DeptRelationRepository extends JpaRepository<DeptRelation,Integer> {
}
