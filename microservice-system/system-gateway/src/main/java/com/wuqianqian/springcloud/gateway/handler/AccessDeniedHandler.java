package com.wuqianqian.springcloud.gateway.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wuqianqian.core.commons.constants.CommonConstant;
import com.wuqianqian.core.commons.constants.MessageConstant;
import com.wuqianqian.core.exception.DeniedException;
import com.wuqianqian.springcloud.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 *
 * 授权拒绝处理器，覆盖默认的OAuth2AccessDeniedHandler 包装失败信息到DeniedException
 * @author liupeqing
 * @date 2018/10/8 15:13
 */
@Slf4j
@Component
public class AccessDeniedHandler extends OAuth2AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException authException) throws IOException, ServletException {
        log.info("授权失败，禁止访问 {}", request.getRequestURI());
        response.setCharacterEncoding(CommonConstant.UTF8);
        response.setContentType(CommonConstant.CONTENT_TYPE);
        R<String> result = new R<String>().failure(new DeniedException(MessageConstant.COMMONS_AUTH_NOTSUPPORT));
        response.setStatus(HttpStatus.SC_FORBIDDEN);
        PrintWriter printWriter = response.getWriter();
        printWriter.append(objectMapper.writeValueAsString(result));
    }
}