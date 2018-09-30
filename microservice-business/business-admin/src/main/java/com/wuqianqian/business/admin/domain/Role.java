package com.wuqianqian.business.admin.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 角色实体类
 * @author liupeqing
 * @date 2018/9/27 16:10
 */
@Entity
@Table(name = Role.TABLE_NAME)
@Data
public class Role implements Serializable {
    private static final long	serialVersionUID	= -5794622871292709802L;

    public static final String	TABLE_NAME			= "t_sys_role";

    /**
     * 角色ID
     */
    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private Integer				roleId;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    private String				roleName;

    /**
     * 角色编码，唯一
     */
    @Column(name = "role_code")
    private String				roleCode;

    /**
     * 描述
     */
    @Column(name = "role_desc")
    private String				roleDesc;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date				updateTime;

    /**
     * 0-正常，1-删除
     */
    @Column(name = "statu")
    private Integer				statu				= 0;

    /**
     *  @Transient表示该属性并非一个到数据库表的字段的映射,ORM框架将忽略该属性.
     * 如果一个属性并非数据库表的字段映射, 就务必将其标示为@Transient,否则,ORM框架默认其注解为@Basic
     */
    @Transient
    private Integer				deptId;
}
