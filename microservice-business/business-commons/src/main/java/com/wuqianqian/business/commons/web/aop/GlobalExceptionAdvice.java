package com.wuqianqian.business.commons.web.aop;

import com.wuqianqian.springcloud.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author liupeqing
 * @date 2018/9/27 11:52
 * @ControllerAdvice + @ExceptionHandler 进行全局的 Controller 层异常处理
 *
 * 优点：将 Controller 层的异常和数据校验的异常进行统一处理，减少模板代码，减少编码量，提升扩展性和可维护性。
 * 缺点：只能处理 Controller 层未捕获（往外抛）的异常，对于 Interceptor（拦截器）层的异常，Spring 框架层的异常，就无能为力了。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public R<Exception> exception(Exception e){
        log.info("全局异常信息:{}" + e.getMessage());
        return new  R<Exception>().data(e).failure(e.getMessage());
    }
}
