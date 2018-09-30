package com.wuqianqian.business.admin.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 部门实体类
 * @author liupeqing
 * @date 2018/9/27 15:49
 */
@Data
@Entity
@Table(name = Dept.TABLE_NAME)
public class Dept implements Serializable {

    private static final long	serialVersionUID	= -3406853201428676677L;

    public static final String	TABLE_NAME			= "t_sys_dept";

    /**
     * 部门id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  //自增
    @Column(name = "dept_id")
    private Integer             deptId;


    /**
     * 上级部门
     */
    @Column(name = "pid")
    private Integer				pid;

    /**
     * 部门名称
     */
    @Column(name = "dept_name")
    private String				deptName;

    /**
     * 0--正常 1--删除
     */
    @Column(name = "statu")
    private Integer				statu				= 0;

    /**
     * 排序字段
     */
    @Column(name = "pos")
    private Integer				pos;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date				updateTime;

    @Transient
    private Integer				roleId;

    public Dept() {
    }

    public Dept(Integer deptId, Integer pid, String deptName) {
        this.deptId = deptId;
        this.pid = pid;
        this.deptName = deptName;
    }

}
