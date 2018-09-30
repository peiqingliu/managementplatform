package com.wuqianqian.business.commons.tree;

import com.wuqianqian.core.beans.AuthMenu;
import com.wuqianqian.springcloud.tree.TreeNode;
import lombok.Data;

/**
 * 菜单树形结构
 * @author liupeqing
 * @date 2018/9/27 9:52
 */
@Data
public class MenuTree extends TreeNode {

    private static final long	serialVersionUID	= 3878699444870356572L;


    private String				label;

    private String				path;

    private String				url;


    public MenuTree(){

    }

    public MenuTree(String id,String pid, String label){
        this.id = id;
        this.pid = pid;
        this.label = label;
    }

    public MenuTree(AuthMenu authMenu){
        this.id = authMenu.getMenuId() + "";
        this.pid = authMenu.getPid() + "";
        this.label = authMenu.getMenuName();
        this.url = authMenu.getUrl();
        this.path = authMenu.getPath();
    }
}
