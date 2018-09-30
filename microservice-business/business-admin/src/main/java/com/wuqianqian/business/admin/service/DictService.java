package com.wuqianqian.business.admin.service;

import com.wuqianqian.business.admin.domain.Dict;
import com.wuqianqian.springcloud.page.PageBean;
import com.wuqianqian.springcloud.page.PageParams;

import java.util.List;

/**
 * 字典接口
 * @author liupeqing
 * @date 2018/9/27 17:07
 */
public interface DictService {

    /**
     * 字典列表数据
     */
    PageBean<Dict> findAll(PageParams pageParams, Dict dict);

    /**
     * 得到字典列表
     */
    List<Dict> getAllList();

    /**
     * 通过id 获取字典
     */
    Dict findById(Integer id);

    /**
     * 通过type 获取字典
     */
    List<Dict> getDictList(String type);

    /**
     * 保存更新字典信息
     */
    Dict saveOrUpdate(Dict dict);

    /**
     * 字典删除
     */
    boolean delById(Integer id);
}
