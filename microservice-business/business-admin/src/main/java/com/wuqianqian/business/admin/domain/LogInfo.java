package com.wuqianqian.business.admin.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 日志信息
 * @author liupeqing
 * @date 2018/9/27 15:57
 */
@Entity
@Table(name = LogInfo.TABLE_NAME)
@Data
public class LogInfo implements Serializable {

    private static final long	serialVersionUID	= 6924830168001131974L;

    public static final String	TABLE_NAME			= "t_sys_log";

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long				id;

    /**
     * 日志类型
     */
    @Column(name = "type")
    private Integer				type;

    /**
     * 日志标题
     */
    @Column(name = "title")
    private Integer				title;

    /**
     * 服务ID
     */
    @Column(name = "service_id")
    private String				serviceId;

    /**
     * 创建者
     */
    @Column(name = "create_by")
    private String				createBy;

    /**
     * 操作IP地址
     */
    @Column(name = "remote_addr")
    private String				remoteAddr;

    /**
     * 用户代理
     */
    @Column(name = "user_agent")
    private String				userAgent;

    /**
     * 请求URI
     */
    @Column(name = "request_uri")
    private String				requestUri;

    /**
     * 操作方式
     */
    @Column(name = "method")
    private String				method;

    /**
     * 操作提交的数据
     */
    @Column(name = "params")
    private String				params;

    /**
     * 执行时间
     */
    @Column(name = "time")
    private Long				time;

    /**
     * 删除标记
     */
    @Column(name = "statu")
    private Integer				statu				= 0;

    /**
     * 异常信息
     */
    @Column(name = "exception")
    private String				exception;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date				updateTime;

    /**
     * id = 18 丢失精度适配
     */
    private transient String	idView;
}
