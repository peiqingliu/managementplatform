package com.wuqianqian.business.commons.web.aop;

/**
 * 当该批注修饰一个方法时，在Controller切面中会对请求该方法的用户进行鉴权。
 * class 上面的权限 + "_" + method 上面的权限组合
 * @author liupeqing
 * @date 2018/9/27 12:53
 */
public @interface PrePermissions {

    /**
     * 是否是必须的
     * @return
     */
    boolean required() default true;

    /**
     * 权限数组
     * @return
     */
    String[] value();
}
