package com.wuqianqian.business.admin.service;

import com.wuqianqian.business.admin.domain.Dept;
import com.wuqianqian.business.commons.tree.DeptTree;
import com.wuqianqian.springcloud.page.PageBean;
import com.wuqianqian.springcloud.page.PageParams;

import java.util.List;

/**
 * 部门接口
 * @author liupeqing
 * @date 2018/9/27 17:06
 */
public interface DeptService {
    /**
     * 得到部门树形列表数据
     */
    List<DeptTree> getDeptTreeList();

    /**
     * 通过Id查询部门信息
     *
     * @param id
     *            部门id
     */
    Dept findById(Integer id);

    /**
     * 新增部门信息
     *
     * @param dept
     * @return
     *         部门
     */
    Dept saveOrUpdate(Dept dept);

    /**
     * 删除部门信息
     *
     * @param id
     */
    boolean delById(Integer id);

    /**
     * 查询部分分页信息
     *
     * @param pageParams
     *            分页参数
     * @param dept
     *            部门bean
     * @return
     *         分页数据
     */
    PageBean<Dept> findAll(PageParams pageParams, Dept dept);
}
