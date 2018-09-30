package com.wuqianqian.core.commons.constants;

/**
 * @author liupeqing
 * @date 2018/9/26 16:57
 *
 *  自定义消息常量 SYSTEM_AUTH 开头对应 system-auth-xxx BUSINESS_ADMIN 开头对应 business-admin-xxx
 */
public interface MessageConstant {

    String	SYSTEM_AUTH_NOTSUPPORT		= "授权模块不可用";

    String	BUSINESS_ADMIN_NOTSUPPORT	= "权限管理模块不可用";

    String	COMMONS_AUTH_NOTSUPPORT		= "授权失败，禁止访问";
}
