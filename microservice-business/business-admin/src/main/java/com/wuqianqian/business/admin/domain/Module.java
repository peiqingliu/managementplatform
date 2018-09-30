package com.wuqianqian.business.admin.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 用于方法上标注的权限 {@link Functional }
 * @author liupeqing
 * @date 2018/9/27 16:00
 */
@Data
@Entity
@Table(name = Module.TABLE_NAME)
public class Module implements Serializable {

    private static final long	serialVersionUID	= 2657654159968503284L;

    public static final String	TABLE_NAME			= "t_sys_module";

    /**
     * 部门id
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer				id;

    /**
     * 编码 {@link Functional }
     */
    @Column(name = "code")
    private String				code;

    /**
     * 名称
     */
    @Column(name = "name")
    private String				name;
}
