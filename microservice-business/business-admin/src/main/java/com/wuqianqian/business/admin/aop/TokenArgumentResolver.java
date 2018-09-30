package com.wuqianqian.business.admin.aop;

import com.wuqianqian.core.commons.constants.SecurityConstant;
import com.wuqianqian.springcloud.StringHelper;
import com.wuqianqian.system.api.module.AuthRole;
import com.wuqianqian.system.api.module.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义参数解析器
 * 将AuthUser参数转换为用户对象
 * @author liupeqing
 * @date 2018/9/29 11:24
 */
@Component
@Slf4j
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * supportsParameter：用于判定是否需要处理该参数分解，返回true为需要，并会去调用下面的方法resolveArgument。
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //判断参数类型是否是AuthUser  是的话 执行下面方法
        return parameter.getParameterType().equals(AuthUser.class);
    }

    /**
     * resolveArgument：真正用于处理参数分解的方法，返回的Object就是controller方法上的形参对象。
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        String username = request.getHeader(SecurityConstant.USER_HEADER);
        String roles = request.getHeader(SecurityConstant.ROLE_HEADER);
        if (StringHelper.isBlank(username) || StringHelper.isBlank(roles)) {
            log.warn("resolveArgument error username or role is empty");
            return null;
        }

        AuthUser authUser = new AuthUser();
        authUser.setUsername(username);
        List<AuthRole> roleList = new ArrayList<AuthRole>();
        Arrays.stream(roles.split(",")).forEach(role -> {
            AuthRole authRole = new AuthRole();
            authRole.setRoleCode(role);
            roleList.add(authRole);
        });
        authUser.setRoleList(roleList);
        return authUser;
    }
}
