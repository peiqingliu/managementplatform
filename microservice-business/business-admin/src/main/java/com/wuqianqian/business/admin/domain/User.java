package com.wuqianqian.business.admin.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户表
 * @author liupeqing
 * @date 2018/9/27 16:18
 */
@Entity
@Table(name = User.TABLE_NAME)
@Data
public class User implements Serializable {

    private static final long		serialVersionUID	= 4401115751257255358L;

    public static final String		TABLE_NAME			= "t_sys_user";

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Integer					userId;

    /**
     * 用户名
     */
    @Column(name = "username")
    private String					username;

    /**
     * 密码
     */
    @Column(name = "password")
    private String					password;

    /**
     * openId
     */
    @Column(name = "open_id")
    private String					openId;

    /**
     * 手机号码
     */
    @Column(name = "mobile")
    private String					mobile;

    /**
     * 头像
     */
    @Column(name = "pic_url")
    private String					picUrl;

    /**
     * 0-正常，1-删除
     */
    @Column(name = "statu")
    private Integer					statu				= 0;

    /**
     * 部门id
     */
    @Column(name = "dept_id")
    private Integer					deptId				= 0;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date					updateTime;

    /**
     * 角色列表
     */
    private transient List<Role> roleList			= new ArrayList<Role>();

    /**
     * 部门名称
     */
    private transient String		deptName;
}
