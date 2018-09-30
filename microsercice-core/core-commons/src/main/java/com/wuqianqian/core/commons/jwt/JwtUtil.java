package com.wuqianqian.core.commons.jwt;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.wuqianqian.core.commons.constants.CommonConstant;
import com.wuqianqian.core.commons.constants.SecurityConstant;
import com.wuqianqian.springcloud.StringHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * JWT相关工具类
 * @author liupeqing
 * @date 2018/9/26 17:15
 */
@Slf4j
public class JwtUtil {
    private static final ThreadLocal<String>	THREAD_LOCAL_USER	= new TransmittableThreadLocal<>();

    //创建token
    public static String createToken(String username, Collection<? extends GrantedAuthority> authorities, boolean isRememberMe){
        long  expiration = isRememberMe ? SecurityConstant.EXPIRATION_REMEMBER : SecurityConstant.EXPIRATION;
        //把角色放在claims  claims本质上就是一个描述
        HashMap<String,Object> map = new HashMap<>();
        map.put(SecurityConstant.JWT_USER_AUTHORITIES,authorities);
        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512,SecurityConstant.SECRET)
                .setClaims(map)
                .setIssuer(SecurityConstant.ISS)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
        return token;
    }


    /**
     * 从token中获取用户名
     * @param token
     * @param jwtkey
     * @return
     */
    public static String getUserName(String token,String jwtkey){
        Claims claims = buildClaims(buildToken(token),jwtkey);
        if (null == claims) return "";
        //String username = claims.getSubject();
        return claims.get(SecurityConstant.JWT_USER_NAME).toString();
    }


    /**
     * 根据用户请求中的token 获取用户名
     *
     * @param request
     *            Request
     * @return ""、username
     */
    public static String getUserName(HttpServletRequest request, String jwtkey) {
        Claims claims = buildClaims(getToken(request), jwtkey);
        if (null == claims) return "";

        return claims.get(SecurityConstant.JWT_USER_NAME).toString();
    }

    /**
     * 从用户请求中获取角色列表
     * @param httpServletRequest
     * @param jwtkey
     * @return
     */
    public static List<String> getRole(HttpServletRequest httpServletRequest,String jwtkey){
        return getRole(getToken(httpServletRequest),jwtkey);

    }

    public static List<String> getRole(String token,String jwtkey){
        Claims claims = buildClaims(buildToken(token),jwtkey);
        if (null == claims) return new ArrayList<>();
        //从claims中获取角色列表
        List<String> roleCode = (List<String>) claims.get(SecurityConstant.JWT_USER_AUTHORITIES);
        return roleCode;
    }
    /**
     * 获取请求中的token
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request){
        String authorization = request.getHeader(CommonConstant.TOKEN_HEADER);
        return buildToken(authorization);
    }

    /**
     * jwt解密
     * Claims 类似于map
     * @param token
     * @param jwtkey
     * @return
     */
    public static Claims buildClaims(String token,String jwtkey){
        if (StringHelper.isBlank(token) || StringHelper.isBlank(jwtkey)) return null;
        String key = "";
        try {
            key = Base64.getEncoder().encodeToString(jwtkey.getBytes());
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            return claims;
        }catch (Exception e){

            log.error("用户TOKEN解析异常,token:{},key:{}", token, key);
        }
        return null;
    }

    /**
     * 截取token
     * @param token
     * @return
     */
    private static String buildToken(String token){
        if (StringHelper.isBlank(token)) return null;
        if (!token.contains(CommonConstant.TOKEN_SPLIT)) return null;
        return StringHelper.substringAfter(token,CommonConstant.TOKEN_SPLIT);
    }

    /**
     * 设置用户信息
     * @param username
     */
    public static void setUser(String username){
        THREAD_LOCAL_USER.set(username);
        MDC.put(CommonConstant.KEY_USER,username);
    }

    /**
     * 从threadlocal中获取用户名
     * @return
     */
    public static String getUser(){
        return THREAD_LOCAL_USER.get();
    }

    /**
     * 如果没有登录，返回null
     *
     * @return 用户名
     */
    public static String getUserName() {
        return THREAD_LOCAL_USER.get();
    }

    /**
     * 清除所有
     */
    public static void clearAll() {
        THREAD_LOCAL_USER.remove();
        MDC.remove(CommonConstant.KEY_USER);
    }
}
