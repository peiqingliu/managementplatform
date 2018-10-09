package com.wuqianqian.springcloud.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wuqianqian.core.commons.constants.CommonConstant;
import com.wuqianqian.core.commons.constants.SecurityConstant;
import com.wuqianqian.core.exception.ValidateCodeException;
import com.wuqianqian.springcloud.R;
import com.wuqianqian.springcloud.StringHelper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * OncePerRequestFilter 能够确保在一次请求只通过一次filter，而不需要重复执行。
 * 验证码校验，true开启，false关闭校验 更细化可以 clientId 进行区分
 * @author liupeqing
 * @date 2018/10/8 15:25
 */
@Component("validateCodeFilter")
public class ValidateCodeFilter extends OncePerRequestFilter {
    /**
     * 根据业务是否需要对验证码进行验证
     */
    @Value("${security.validate.code:false}")
    private boolean			isValidate;

    /**
     * redis操作工具
     */
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * JOSN OBJECT 操作工具
     */
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (isValidate && (StringUtils.contains(request.getRequestURI(),SecurityConstant.OAUTH_TOKEN_URL) ||
                StringUtils.contains(request.getRequestURI(),SecurityConstant.MOBILE_TOKEN_URL))){
            PrintWriter printWriter = null;
            try{
                checkCode(request,response,filterChain);
            }catch (ValidateCodeException e){
                response.setCharacterEncoding(CommonConstant.UTF8);
                response.setContentType(CommonConstant.CONTENT_TYPE);
                R<String> result = new R<String>().failure(e);
                response.setStatus(478);
                printWriter = response.getWriter();
                printWriter.append(objectMapper.writeValueAsString(result));
            }finally {
                IOUtils.closeQuietly(printWriter);
            }

        }else{ //如果不需要验证  直接往下走就行了
            filterChain.doFilter(request, response);
        }

    }

    private void checkCode(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse, FilterChain filterChain) throws IOException, ServletException {
        String code = httpServletRequest.getParameter("code");
        if (StringHelper.isBlank(code)) throw new ValidateCodeException("请输入验证码!");
        String randomStr = httpServletRequest.getParameter("randomStr");
        if (StringHelper.isBlank(randomStr)){
            randomStr = httpServletRequest.getParameter("mobile");
        }
        String key = SecurityConstant.DEFAULT_CODE_KEY + randomStr;
        if (!redisTemplate.hasKey(key)){
            throw new ValidateCodeException("验证码已过期,请重新获取!");
        }

        Object codeObj = redisTemplate.opsForValue().get(key);
        if (codeObj == null) {
            throw new ValidateCodeException("验证码已过期，请重新获取!");
        }

        String saveCode = codeObj.toString();
        if (StringHelper.isBlank(saveCode)){
            redisTemplate.delete(key);
            throw new ValidateCodeException("验证码已过期，请重新获取!");
        }
        if (!StringHelper.equals(saveCode,code)){
            redisTemplate.delete(key);
            throw new ValidateCodeException("验证码输入错误,请重新输入!");
        }
        redisTemplate.delete(key);
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
