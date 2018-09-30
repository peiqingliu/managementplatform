package com.wuqianqian.system.api.module;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liupeqing
 * @date 2018/9/27 17:20
 */
@Data
@NoArgsConstructor  //不需要构造器
@AllArgsConstructor
@ApiModel(description = "用户菜单权限")
public class AuthPermission implements Serializable {

    private static final long	serialVersionUID	= 4566420419542436770L;

    /**
     * 请求URL
     */
    private String				url;

}
