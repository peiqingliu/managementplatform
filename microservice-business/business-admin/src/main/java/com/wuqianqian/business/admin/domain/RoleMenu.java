package com.wuqianqian.business.admin.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 角色与资源关系表
 * 多对多的关系
 * @author liupeqing
 * @date 2018/9/27 16:14
 */
@Entity
@Table(name = RoleMenu.TABLE_NAME)
@Data
public class RoleMenu implements Serializable {

    private static final long	serialVersionUID	= 8409879328945905867L;

    public static final String	TABLE_NAME			= "t_sys_role_menu";

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer				id;

    /**
     * 角色Id
     */
    @Column(name = "role_id")
    private Integer				roleId;

    /**
     * 菜单id
     */
    @Column(name = "menu_id")
    private Integer				menuId;
}
