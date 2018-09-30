package com.wuqianqian.business.admin.beans;

import com.wuqianqian.business.admin.domain.User;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liupeqing
 * @date 2018/9/27 17:21
 */
@Data
public class UserBean implements Serializable {


    private static final long	serialVersionUID	= 4100476652382025202L;

    /**
     * 用户基本信息
     */
    private User                user;

    /**
     * 权限标识集合
     */
    private String[]			permissions;

    /**
     * 角色集合
     */
    private String[]			roles;


}
