package com.wuqianqian.business.admin.service;

import com.wuqianqian.business.admin.beans.UserBean;
import com.wuqianqian.business.admin.beans.UserForm;
import com.wuqianqian.business.admin.domain.User;
import com.wuqianqian.springcloud.page.PageBean;
import com.wuqianqian.springcloud.page.PageParams;
import com.wuqianqian.system.api.module.AuthUser;

/**
 * @author liupeqing
 * @date 2018/9/27 17:17
 */
public interface UserService {

    public AuthUser findUserByUsername(String username);

    public User findUserByUsername(String username, boolean isLoadRole);

    public AuthUser findUserByMobile(String mobile);

    public void saveImageCode(String randomStr, String text);

    public UserBean findUserInfo(AuthUser user);

    public AuthUser findByUserId(String userId);

    public PageBean<User> findAll(PageParams pageParams, User user);

    public Boolean delByUserId(Integer userId);

    public boolean addUserAndRoleDept(UserForm userForm);

    public boolean updateUserAndRoleDept(UserForm userForm);

    public boolean updateUser(User user);
}
