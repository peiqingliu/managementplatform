package com.wuqianqian.business.admin.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 部门权限关系表
 * 部门有哪些权限
 * @author liupeqing
 * @date 2018/9/27 16:13
 */
@Entity
@Table(name = RoleDept.TABLE_NAME)
@Data
public class RoleDept implements Serializable {
    private static final long	serialVersionUID	= -1028920072723837099L;

    public static final String	TABLE_NAME			= "t_sys_role_dept";

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer				id;

    @Column(name = "role_id")
    private Integer				roleId;

    @Column(name = "dept_id")
    private Integer				deptId;
}
