package com.wuqianqian.business.admin.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色部门组合信息
 * @author liupeqing
 * @date 2018/9/27 17:16
 */
public class RoleDeptBean implements Serializable {

    private static final long	serialVersionUID	= 1237754912496425638L;

    private Integer				roleId;

    /**
     * 角色名称
     */
    private String				roleName;

    /**
     * 角色编码，唯一
     */
    private String				roleCode;

    /**
     * 描述
     */
    private String				roleDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date				updateTime;

    /**
     * 0-正常，1-删除
     */
    private Integer				statu				= 0;

    /**
     * 部门id
     */
    private Integer				deptId;

    /**
     * 上级部门
     */
    private Integer				pid;

    /**
     * 部门名称
     */
    private String				deptName;
}
