package com.wuqianqian.business.admin.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author liupeqing
 * @date 2018/9/27 15:56
 */
@Table(name = Dict.TABLE_NAME)
@ApiModel(description = "字典信息")
@Entity
@Data
public class Dict implements Serializable {

    private static final long	serialVersionUID	= 6336455104080722637L;

    public static final String	TABLE_NAME			= "t_sys_dict";

    /**
     * 字典id
     */
    @Id
    @GeneratedValue
    @ApiModelProperty("字典id")
    @Column(name = "id")
    private Integer				id;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    @Column(name = "type")
    private String				type;

    /**
     * 数据值
     */
    @ApiModelProperty("字典值")
    @Column(name = "value")
    private String				value;

    /**
     * 标签名
     */
    @ApiModelProperty("标签名")
    @Column(name = "label")
    private String				label;

    /**
     * 0--正常 1--删除
     */
    @ApiModelProperty("0--正常 1--删除")
    @Column(name = "statu")
    private Integer				statu				= 0;
}
