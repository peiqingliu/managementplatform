package com.wuqianqian.business.admin.repository;

import com.wuqianqian.business.admin.domain.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author liupeqing
 * @date 2018/9/27 17:00
 */
@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {
}
