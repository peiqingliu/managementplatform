package com.wuqianqian.business.admin.service.impl;

import com.querydsl.core.types.Predicate;
import com.wuqianqian.business.admin.cache.AdminCacheKey;
import com.wuqianqian.business.admin.domain.Dept;
import com.wuqianqian.business.admin.domain.QDept;
import com.wuqianqian.business.admin.repository.DeptRepository;
import com.wuqianqian.business.admin.service.DeptService;
import com.wuqianqian.business.commons.tree.DeptTree;
import com.wuqianqian.business.commons.utils.PageUtils;
import com.wuqianqian.business.commons.web.jpa.JPAFactoryImpl;
import com.wuqianqian.springcloud.StringHelper;
import com.wuqianqian.springcloud.page.PageBean;
import com.wuqianqian.springcloud.page.PageParams;
import com.wuqianqian.springcloud.tree.TreeUtil;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author liupeqing
 * @date 2018/9/27 19:18
 */
@Component
@CacheConfig(cacheNames = AdminCacheKey.DEPT_INFO)  //用于公用的缓存配置
public class DeptServiceImpl extends JPAFactoryImpl implements DeptService {

    @Autowired
    private DeptRepository deptRepository;

    /**
     * 缓存的名称已经制定了
     * @return
     */
    @Override
    @Cacheable(key = "'dept_tree_list'", unless = "#result eq null")
    public List<DeptTree> getDeptTreeList() {
        QDept qDept = QDept.dept;
        List<Dept> list = this.jpaQueryFactory.selectFrom(qDept).where(qDept.statu.eq(0)).fetch();
        if (null == list || list.size() == 0) return new ArrayList<DeptTree>();
        return getDeptTree(list, 0);
    }

    private List<DeptTree> getDeptTree(List<Dept> list, int rootId){
        List<DeptTree> treeList = new ArrayList<DeptTree>();
        for (Dept dept : list) {
            // 排除父节点和自己节点相同的数据
            if (dept.getPid().intValue() == dept.getDeptId().intValue()) continue;
            DeptTree node = new DeptTree();
            node.setId(dept.getDeptId() + "");
            node.setPid(dept.getPid() + "");
            node.setName(dept.getDeptName());
            node.setPos(dept.getPos());
            treeList.add(node);
        }
        return TreeUtil.build(treeList, "0");
    }

    /**
     * unless = "#result eq null "  对结果进行筛选
     * @param id
     * @return
     */
    @Override
    @Cacheable(key = " 'dept_info_' + #id",unless = "#result eq null ")
    public Dept findById(Integer id) {

        if (null == id || id < 0) return null;

        return this.deptRepository.findOne(id);
    }

    /**
     * 每次都会执行改方法，将结果写入缓存
     * @param dept
     * @return
     */
    @Override
    @Transactional  //事务
    @CachePut(key = " 'dept_info_' + #id")
    public Dept saveOrUpdate(Dept dept) {

        if (null == dept) return null;
        return this.deptRepository.saveAndFlush(dept);
    }

    /**
     * 删除缓存  allEntries：true表示清除value中的全部缓存，默认为false。
     * @param id
     * @return
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public boolean delById(Integer id) {
        if ( null == id || id < 0) return false;

        //return this.deptRepository.delete(id); 返回类型不对 而且也不能物理删除  做逻辑删除
        QDept qDept = QDept.dept;
        Long number = this.jpaQueryFactory.update(qDept).set(qDept.statu,0).where(qDept.deptId.eq(id)).execute();
        return null !=number && number > 0;
    }

    /**
     * 其中#p0的意思是指加有@Cacheable注解的方法中的第一个参数
     * @param pageParams
     *            分页参数
     * @param dept
     *            部门bean
     * @return
     */
    @Override
    @Cacheable(key = "'page_dept_' + #p0.currentPage + '_' + #p0.pageSize + '_' + #p1.type + '_' + #p1.label")
    public PageBean<Dept> findAll(PageParams pageParams, Dept dept) {

        QDept qDept = QDept.dept;
        Predicate qNamePredicate = null;
        if (null != dept){
            if (StringHelper.isNoneBlank(dept.getDeptName())){
                qNamePredicate = qDept.deptName.like("%" + dept.getDeptName().trim() + "%");
            }
        }
        Predicate predicate = QDept.dept.deptId.goe(0).and(qNamePredicate);
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "deptId"));
        PageRequest pageRequest = PageUtils.of(pageParams, sort);
        Page<Dept> pageList = deptRepository.findAll(qNamePredicate, pageRequest);
        return  PageUtils.of(pageList);
    }
}
