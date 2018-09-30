package com.wuqianqian.business.admin.beans;

import com.wuqianqian.business.commons.tree.MenuTree;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author liupeqing
 * @date 2018/9/28 19:51
 */
@Data
public class MenuTreeBean implements Serializable {

    private static final long	serialVersionUID	= 2707121320504244801L;

    private List<MenuTree> menuList;

    private String[]			permissions;
}
