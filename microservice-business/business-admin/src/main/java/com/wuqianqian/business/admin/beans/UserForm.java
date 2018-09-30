package com.wuqianqian.business.admin.beans;

import lombok.Data;

/**
 * 用户请求表单
 * @author liupeqing
 * @date 2018/9/27 17:22
 */
@Data
public class UserForm {

    private String	username;

    private String	password;

    private String	newpassword;

    private Integer	statu	= 0;

    private Integer	roleId;

    private Integer	userId;

    private Integer	deptId;

    private String	mobile;

}
