package com.wuqianqian.business.admin.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wuqianqian.business.admin.service.PermissionService;
import com.wuqianqian.business.commons.utils.RequestUtil;
import com.wuqianqian.business.commons.web.aop.PrePermissions;
import com.wuqianqian.business.commons.web.config.PermissionConfiguration;
import com.wuqianqian.core.commons.jwt.JwtUtil;
import com.wuqianqian.core.configuration.JwtConfiguration;
import com.wuqianqian.core.exception.CheckedException;
import com.wuqianqian.springcloud.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义一个拦截器  用来验证  功能权限切面验证
 * 是HandlerInterceptor的子类
 * @author liupeqing
 * @date 2018/9/28 20:25
 */
@Slf4j
@Configuration
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ObjectMapper objectMapper;

    //自动注入的时候  注入不进来  因为是在不同的模块
    private PermissionConfiguration permissionConfiguration = new PermissionConfiguration();

    private JwtConfiguration jwtConfiguration = new JwtConfiguration();

    /**
     * This implementation always returns {@code true}.
     * preHandle: 在执行controller处理之前执行，返回值为boolean ,
     * 返回值为true时接着执行postHandle和afterCompletion，如果我们返回false则中断执行
     *
     *  该方法将在请求处理之前进行调用，只有该方法返回true，才会继续执行后续的Interceptor和Controller，
     *  当返回值为true 时就会继续调用下一个Interceptor的preHandle 方法，
     *  如果已经是最后一个Interceptor的时候就会是调用当前请求的Controller方法；
     *  一般重写该方法  进行权限控制
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (permissionConfiguration.isEnabled()) return true;
        //判断handler 是否是HandlerMethod的相同的类  或者父类
        if (!handler.getClass().isAssignableFrom(HandlerMethod.class)) return true;
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        final Method method = handlerMethod.getMethod();
        final Class<?> clazz = method.getDeclaringClass();

        String requestURI = request.getRequestURI();
        String modulePermission = "";
        // 为了规范，如果class上面没有设置@PrePermissions则不通过
        if (!clazz.isAnnotationPresent(PrePermissions.class)){
            log.error("请求[" + requestURI + "]模块上未设置权限,请设置注解@PrePermissions权限！");
            R<Boolean> responseNoPermission = new R<Boolean>()
                    .failure("请求[\" + requestURI + \"]模块上未设置权限,请设置注解@PrePermissions权限！").data(false);
            this.handleWithResponse(response,responseNoPermission);
            return false;
        }

        //获取到注解
        PrePermissions clazzPermissions = clazz.getAnnotation(PrePermissions.class);
        if (!clazzPermissions.required()) return true;
        //第一个权限
        modulePermission = clazzPermissions.value()[0];
        // 为了规范：方法上没设置权限的请求则不通过
        if (!method.isAnnotationPresent(PrePermissions.class)){
            log.error("请求[" + requestURI + "]方法上未正确设置权限,请设置注解@PrePermissions权限！");
            R<Boolean> responseWithR = new R<Boolean>().failure(
                    "请求[" + requestURI + "]方法上未正确设置权限,请设置注解@PrePermissions权限！").data(false);
            this.handleWithResponse(response, responseWithR);
            return false;
        }

        //得到方法上的权限注解
        PrePermissions prePermissions = method.getAnnotation(PrePermissions.class);
        String[] permissions = prePermissions.value();
        if (null == permissions || permissions.length == 0) {
            log.error("请求[" + requestURI + "]方法上未正确设置权限,请设置注解@PrePermissions权限！");
            R<Boolean> responseWithR = new R<Boolean>().failure(
                    "请求[" + requestURI + "]方法上未正确设置权限,请设置注解@PrePermissions权限！").data(false);
            this.handleWithResponse(response, responseWithR);
            return false;
        }

        //验证是否有功能权限 从请求中获取角色列表
        List<String> roleList = JwtUtil.getRole(request,jwtConfiguration.getJwtkey());
        if (null == request || roleList.size() < 0 ) {
            R<Boolean> responseWithR = new R<Boolean>().failure("请求[" + requestURI + "]权限验证失败！")
                    .data(false);
            this.handleWithResponse(response, responseWithR);
        }
        // 所以角色权限集合
        Set<String> menuPermissions = new HashSet<String>();
        for (String roleCode : roleList){
            //1、add是将传入的参数作为当前List中的一个Item存储，即使你传入一个List也只会另当前的List增加1个元素
            //2、addAll是传入一个List，将此List中的所有元素加入到当前List中，也就是当前List会增加的元素个数为传入的List的大小
            menuPermissions.addAll(this.permissionService.findMenuPermissions(roleCode));
        }

        if (null == menuPermissions || menuPermissions.size() < 0){
            R<Boolean> responseWithR = new R<Boolean>().failure("请求[" + requestURI + "]权限未配置！")
                    .data(false);
            this.handleWithResponse(response, responseWithR);
            return false;
        }

        //遍历
        for (String permission : permissions){
            String valiatePermission = modulePermission + permission;
            log.info("请求[" + requestURI + "],permission:[" + valiatePermission + "]");
            //验证valiatePermission 是否有功能权限
            if (!menuPermissions.contains(valiatePermission)){
                log.info("请求[" + requestURI + "]权限[" + valiatePermission + "]未配置！");
                R<Boolean> responseWithR = new R<Boolean>().failure(
                        "请求[" + requestURI + "]权限[" + valiatePermission + "]未配置！").data(false);
                this.handleWithResponse(response, responseWithR);
                return false;
            }
        }

        return true;
    }

    /**
     * This implementation is empty.
     * postHandle:在执行controller的处理后，在ModelAndView处理前执行
     *
     *  该方法将在请求处理之后，DispatcherServlet进行视图返回渲染之前进行调用，
     *  可以在这个方法中对Controller 处理之后的ModelAndView 对象进行操作
     */
    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
    }

    /**
     * This implementation is empty.
     * afterCompletion ：在DispatchServlet执行完ModelAndView之后执行
     */
    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

    /**
     * This implementation is empty.
     */
    @Override
    public void afterConcurrentHandlingStarted(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
    }


    /**
     * 返回结果
     * @param response
     * @param responseWithR
     */
    public void handleWithResponse(HttpServletResponse response, R<Boolean> responseWithR) {
        try {
            RequestUtil.handleWithResponse(response, this.objectMapper
                    .writeValueAsString(responseWithR));
        } catch (IOException e) {
            e.printStackTrace();
            throw new CheckedException("Failed to response");
        }
    }
}
