package com.wuqianqian.business.admin.repository;

import com.wuqianqian.business.admin.domain.RoleDept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author liupeqing
 * @date 2018/9/27 17:01
 */
@Repository
public interface RoleDeptRepository extends JpaRepository<RoleDept, Integer> {
}
