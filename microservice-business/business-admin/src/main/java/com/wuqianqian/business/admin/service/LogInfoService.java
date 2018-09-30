package com.wuqianqian.business.admin.service;

import com.wuqianqian.business.admin.domain.LogInfo;
import com.wuqianqian.springcloud.page.PageBean;
import com.wuqianqian.springcloud.page.PageParams;

/**
 * 日志接口
 * @author liupeqing
 * @date 2018/9/27 17:08
 */
public interface LogInfoService {

    public void saveOrUpdate(LogInfo logInfo);

    /**
     * 日志列表数据
     */
    PageBean<LogInfo> findAll(PageParams pageParams, LogInfo logInfo);

    /**
     * 日志删除
     */
    boolean delById(Long id);
}
