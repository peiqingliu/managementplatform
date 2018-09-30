package com.wuqianqian.business.commons.web.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * @author liupeqing
 * @date 2018/9/27 12:58
 */
public abstract class JPAFactoryImpl {

    //jpa查询工厂
    protected JPAQueryFactory jpaQueryFactory;

    @Autowired
    protected EntityManager em;

    /**
     * 被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器调用一次，
     * 类似于Servlet的inti()方法。被@PostConstruct修饰的方法会在构造函数之后，init()方法之前运行。
     */
    @PostConstruct
    public void initFactory(){
        jpaQueryFactory = new JPAQueryFactory(em);
    }
}
