package com.wuqianqian.business.admin.service.impl;

import com.wuqianqian.business.admin.domain.DeptRelation;
import com.wuqianqian.business.admin.domain.QDeptRelation;
import com.wuqianqian.business.admin.repository.DeptRelationRepository;
import com.wuqianqian.business.admin.service.DeptRelationService;
import com.wuqianqian.business.commons.web.jpa.JPAFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liupeqing
 * @date 2018/9/27 17:23
 */
@Service
public class DeptRelationServiceImpl extends JPAFactoryImpl implements DeptRelationService {

    @Autowired
    private DeptRelationRepository deptRelationRepository;



    @Override
    public List<DeptRelation> findListByPreId(Integer preId) {
        if (null == preId || preId < 0  ) return null;

        //使用querydsl查询
        QDeptRelation qDeptRelation = QDeptRelation.deptRelation;

        //查询并返回结果集
        /**
         * selectFrom方法来简化查询，该方法代替了select&from两个方法，注意：也是仅限单表操作时可以使用。
         * 在一系列的条件都添加完成后，调用fetch方法执行我们的条件查询并且获取对应selectFrom查询实体的类型集合，
         * 要注意一点：这里如果selectFrom参数的实体类型不是UserBean那fetch方法返回集合的类型也不是List<UserBean>。
         *
         * 除了 eq 还有 like等查询条件
         *
         * //querydsl查询实体
         *         QUserBean _Q_user = QUserBean.userBean;
         *
         *         queryFactory
         *                 .update(_Q_user)//更新对象
         *                 //更新字段列表
         *                 .set(_Q_user.name,userBean.getName())
         *                 .set(_Q_user.address,userBean.getAddress())
         *                 .set(_Q_user.age,userBean.getAge())
         *                 .set(_Q_user.pwd,userBean.getPwd())
         *                 //更新条件
         *                 .where(_Q_user.id.eq(userBean.getId()))
         *                 //执行更新
         *                 .execute();
         *
         * //querydsl查询实体
         *         QUserBean _Q_user = QUserBean.userBean;
         *
         *         queryFactory
         *                 //删除对象
         *                 .delete(_Q_user)
         *                 //删除条件
         *                 .where(_Q_user.id.eq(userBean.getId()))
         *                 //执行删除
         *                 .execute();
         *
         * 作者：恒宇少年
         * 链接：https://www.jianshu.com/p/ac388c3c36c2
         * 來源：简书
         * 简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
         */
        return this.jpaQueryFactory
                .selectFrom(qDeptRelation)  //查询源
                .where(qDeptRelation.preId.eq(preId))  //查询条件
                .fetch();  //执行查询并获取结果集
    }

    @Override
    public void saveOrUpdate(DeptRelation deptRelation) {
        this.deptRelationRepository.save(deptRelation);

    }
}
