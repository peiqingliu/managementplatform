package com.wuqianqian.business.admin.repository;

import com.wuqianqian.business.admin.domain.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author liupeqing
 * @date 2018/9/27 17:02
 */
@Repository
public interface RoleMenuRepository extends JpaRepository<RoleMenu, Integer> {
}
