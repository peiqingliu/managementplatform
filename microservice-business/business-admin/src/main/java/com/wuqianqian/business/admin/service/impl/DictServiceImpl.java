package com.wuqianqian.business.admin.service.impl;

import com.querydsl.core.types.Predicate;
import com.wuqianqian.business.admin.cache.AdminCacheKey;
import com.wuqianqian.business.admin.domain.Dict;
import com.wuqianqian.business.admin.domain.QDict;
import com.wuqianqian.business.admin.repository.DictRepository;
import com.wuqianqian.business.admin.service.DictService;
import com.wuqianqian.business.commons.utils.PageUtils;
import com.wuqianqian.business.commons.web.jpa.JPAFactoryImpl;
import com.wuqianqian.springcloud.StringHelper;
import com.wuqianqian.springcloud.page.PageBean;
import com.wuqianqian.springcloud.page.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liupeqing
 * @date 2018/9/27 20:43
 */
@Component
@CacheConfig(cacheNames = AdminCacheKey.DICT_INFO)
public class DictServiceImpl extends JPAFactoryImpl implements DictService {

    @Autowired
    private DictRepository dictRepository;

    @Override
    @Cacheable(key = "'page_dict_' + #p0.currentPage + '_' + #p0.pageSize + '_' + #p1.type + '_' + #p1.label")
    public PageBean<Dict> findAll(PageParams pageParams, Dict dict) {
        QDict qDict = QDict.dict;

        //查询条件
        Predicate qLabelPredicate = null;
        Predicate qTypePredicate = null;
        if (null != dict){
            if (StringHelper.isNotBlank(dict.getLabel())){
                qLabelPredicate = qDict.label.like("%" + dict.getLabel() + "%");
            }
            if (StringHelper.isNotBlank(dict.getType())){
                qTypePredicate = qDict.type.like("%" + dict.getType() + "%");
            }

            //goe(0) 表示 id > 0 and  且的意思 查询条件合并
            Predicate predicate = qDict.id.goe(0).and(qLabelPredicate).and(qTypePredicate);
            //根据id 排序
            Sort sort = new Sort(new Sort.Order(Sort.DEFAULT_DIRECTION,"id"));
            PageRequest pageRequest = PageUtils.of(pageParams,sort);
            Page<Dict> pageList = this.dictRepository.findAll(predicate,pageRequest);
            return PageUtils.of(pageList);
        }
        return null;
    }

    @Override
    @Cacheable(key = "dict_list",unless = "#result eq null ")
    public List<Dict> getAllList() {
        QDict qDict = QDict.dict;
        //this.dictRepository.findAll();
        return this.jpaQueryFactory.selectFrom(qDict).fetch();
    }

    @Override
    @Cacheable(key = " 'dict_' + #id",unless = "#result eq null ")
    public Dict findById(Integer id) {
        if (id == null) return null;
        return this.dictRepository.findOne(id);
    }

    @Override
    @Cacheable(key = " 'dict_' + #type",unless = "#result eq null ")
    public List<Dict> getDictList(String type) {
        QDict qDict = QDict.dict;
//        Predicate qTypePredicate = null;
//        if (StringHelper.isNotBlank(type)){
//            qTypePredicate = qDict.type.like("%" + type + "%");
//        }
        return this.jpaQueryFactory.selectFrom(qDict).where(qDict.type.eq("%" + type.trim() + "%")).fetch();
    }

    @Override
    @CachePut
    @Transactional
    public Dict saveOrUpdate(Dict dict) {
        if (null == dict) return null;

         return this.dictRepository.saveAndFlush(dict);
    }

    @Override
    @CacheEvict(key = " 'dict_' + #id",allEntries = true)
    public boolean delById(Integer id) {
        if (null == id) return false;
        QDict qDict = QDict.dict;
        //逻辑删除
        Long num = this.jpaQueryFactory.update(qDict).set(qDict.statu,1).where(qDict.id.eq(id.intValue())).execute();
        return num !=null && num > 0 ;
    }
}
