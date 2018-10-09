package com.wuqianqian.springcloud.gateway.service.impl;

import com.wuqianqian.core.commons.constants.SecurityConstant;
import com.wuqianqian.core.commons.jwt.JwtUtil;
import com.wuqianqian.springcloud.StringHelper;
import com.wuqianqian.springcloud.gateway.service.PermissionService;
import com.wuqianqian.system.api.feignApi.PermissionFeignApi;
import com.wuqianqian.system.api.module.AuthPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  权限接口实现
 * @author liupeqing
 * @date 2018/9/26 20:41
 */
@Slf4j
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

    /**
     * 具体Admin工程实现  虽然是另一个工程的bean，但是通过引入jar,启动类上进行包扫描，可以注入进来
     */
    @Autowired
    private PermissionFeignApi permissionFeignApi;

    /**
     * redis 工厂类
     */
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 它主要用来做类URLs字符串匹配
     */
    private AntPathMatcher antPathMatcher	= new AntPathMatcher();


    /**
     * Authentication 认证的实例
     * @param request
     * @param authentication
     * @return
     */
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        // options 跨域配置，现在处理是通过前端配置代理，不使用这种方式，存在风险
        /*
         * if (HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) { return true; }
         */
        /**
         *  通过Authentication.getPrincipal()的返回类型是Object，但很多情况下其返回的其实是一个UserDetails的实例
         */
        Object principal = authentication.getPrincipal();
        /**
         * 获得所有的角色
         */
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = (List<SimpleGrantedAuthority>) authentication.getAuthorities();
        boolean hasPermission = false;
        if (null == principal) return hasPermission;
        if (CollectionUtils.isEmpty(simpleGrantedAuthorities)) return hasPermission;
        //从请求中获取token
        String token = JwtUtil.getToken(request);
        if (StringHelper.isBlank(token)){
            log.warn("==> gateway|permissionService 未获取到Header Authorization");
            return hasPermission;
        }
        if (!"anonymousUser".equals(principal.toString())){
            RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
            redisTokenStore.setPrefix(SecurityConstant.PREFIX);
            /**
             * 根据token获取OAuth2AccessToken
             * 校验token
             * AccessToken是有时效性的
             */
            OAuth2AccessToken accessToken = redisTokenStore.readAccessToken(token);
            if (null == accessToken || accessToken.isExpired()){  //不存在或者过期
                log.warn("==> gateway|permissionService token 过期或者不存在");
                return hasPermission;
            }
        }
        Set<AuthPermission> authPermissions = new HashSet<AuthPermission>();
        for (SimpleGrantedAuthority authority : simpleGrantedAuthorities){
            authPermissions.addAll(permissionFeignApi.findMenuByRole(authority.getAuthority()));
        }
        // 网关处理是否拥有菜单权限，菜单下的功能权限校验由调用子模块负责
        String requestURI = request.getRequestURI();
        for (AuthPermission authPermission : authPermissions){
            //antPathMatcher.match(a,b) 用后面的一个与前面的进行匹配 能匹配上返回true
            if (StringHelper.isNotBlank(requestURI) && antPathMatcher.match(authPermission.getUrl(),requestURI)){
                hasPermission = true;
                break;
            }
        }
        return hasPermission;
    }
}
