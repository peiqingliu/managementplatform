package com.wuqianqian.business.admin.service;

import com.wuqianqian.business.admin.domain.DeptRelation;

import java.util.List;

/**
 * 部门关联信息接口
 * @author liupeqing
 * @date 2018/9/27 17:05
 */
public interface DeptRelationService {

    List<DeptRelation> findListByPreId(Integer preId);

    void saveOrUpdate(DeptRelation deptRelation);
}
