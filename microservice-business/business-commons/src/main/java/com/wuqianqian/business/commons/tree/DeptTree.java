package com.wuqianqian.business.commons.tree;

import com.wuqianqian.springcloud.tree.TreeNode;
import lombok.Data;

/**
 * 部门树形结构
 * @author liupeqing
 * @date 2018/9/27 9:46
 */
@Data
public class DeptTree extends TreeNode {

    private static final long	serialVersionUID	= 2764058970186728117L;

    private String				name;

    private Integer				pos					= 0;
}
