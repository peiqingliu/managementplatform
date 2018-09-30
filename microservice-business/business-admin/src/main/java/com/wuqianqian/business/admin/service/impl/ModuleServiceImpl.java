package com.wuqianqian.business.admin.service.impl;

import com.wuqianqian.business.admin.cache.AdminCacheKey;
import com.wuqianqian.business.admin.domain.Module;
import com.wuqianqian.business.admin.domain.QModule;
import com.wuqianqian.business.admin.repository.ModuleRepository;
import com.wuqianqian.business.admin.service.ModuleService;
import com.wuqianqian.business.commons.web.jpa.JPAFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 功能权限接口实现类
 * @author liupeqing
 * @date 2018/9/28 10:55
 */
@Service
@CacheConfig(cacheNames = AdminCacheKey.MODULE_INFO)
public class ModuleServiceImpl extends JPAFactoryImpl implements ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Override
    @Cacheable(key = "'module_list'",unless = "#result eq null")
    public List<Module> getAllList() {
        QModule qModule = QModule.module;

        return this.jpaQueryFactory.selectFrom(qModule).fetch();
    }
}
