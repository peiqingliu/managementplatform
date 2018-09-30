package com.wuqianqian.business.admin.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author liupeqing
 * @date 2018/9/27 16:19
 */
@Entity
@Table(name = UserRole.TABLE_NAME)
@Data
public class UserRole implements Serializable {

    private static final long	serialVersionUID	= 8409879328945905867L;

    public static final String	TABLE_NAME			= "t_sys_user_role";

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer				id;

    @Column(name = "user_id")
    private Integer				userId;

    @Column(name = "role_id")
    private Integer				roleId;

}
