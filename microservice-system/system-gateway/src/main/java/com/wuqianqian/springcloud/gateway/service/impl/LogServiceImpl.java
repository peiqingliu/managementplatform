package com.wuqianqian.springcloud.gateway.service.impl;

import com.netflix.zuul.context.RequestContext;
import com.wuqianqian.core.beans.AuthLog;
import com.wuqianqian.core.beans.Log;
import com.wuqianqian.core.commons.constants.CommonConstant;
import com.wuqianqian.core.commons.constants.MqQueueConstant;
import com.wuqianqian.springcloud.StringHelper;
import com.wuqianqian.springcloud.gateway.service.LogService;
import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.util.URLUtil;
import com.xiaoleilu.hutool.io.IoUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;

/**
 * @author liupeqing
 * @date 2018/9/26 16:20
 *
 * 日志异步实现
 */
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    public void send(RequestContext requestContext) {
        //获取请求
        HttpServletRequest request = requestContext.getRequest();
        //获取uri
        String requestUri = request.getRequestURI();
        //获取方法
        String method = request.getMethod();
        //系统级别的日志
        Log sysLog = new Log();
        sysLog.setType(CommonConstant.STATUS_NORMAL);
        sysLog.setRemoteAddr(HttpUtil.getClientIP(request));
        sysLog.setRequestUri(URLUtil.getPath(requestUri));
        sysLog.setMethod(method);
        sysLog.setUserAgent(request.getHeader("user-agent"));
        sysLog.setParams(HttpUtil.toParams(request.getParameterMap()));
        Long startTime =  (Long) requestContext.get("startTime");
        sysLog.setTime(System.currentTimeMillis() - startTime);
        Date currentDate = new Date();
        sysLog.setCreateTime(currentDate);
        sysLog.setUpdateTime(currentDate);
        if (requestContext.get(CommonConstant.SERVICE_ID) != null){
            sysLog.setServiceId(requestContext.get(CommonConstant.SERVICE_ID).toString());
        }

        // 正常发送服务异常解析
        if (requestContext.getResponseStatusCode() != org.apache.http.HttpStatus.SC_OK && null != requestContext.getResponseDataStream()){
            InputStream responseStream = requestContext.getResponseDataStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream inputStream = null;
            InputStream responseDataStream = null;
            byte[] buffer = IoUtil.readBytes(responseStream);
            try {
                baos.write(buffer);
                baos.flush();
                inputStream = new ByteArrayInputStream(baos.toByteArray());
                responseDataStream = new ByteArrayInputStream(baos.toByteArray());
                String response = IoUtil.read(inputStream, CommonConstant.UTF8);
                //日志记录异常情况
                sysLog.setType(CommonConstant.STATUS_LOCK);
                sysLog.setException(response);
                requestContext.setResponseDataStream(responseDataStream);
            }catch (Exception e){
                throw new RuntimeException(e);
            }finally {
                IoUtil.close(responseDataStream);
                IoUtil.close(baos);
                IoUtil.close(responseStream);
            }
        }

        //	 网关内部异常
        Throwable throwable = requestContext.getThrowable();
        if (null != throwable){
            sysLog.setException(throwable.getMessage());
        }

        AuthLog authLog = new AuthLog();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null != authentication && StringHelper.isBlank(authentication.getName())){
            sysLog.setCreateBy(authentication.getName());
            authLog.setLog(sysLog);

            /**发送消息
             *
             * convertAndSend(exchangeName, routingKey, msg) 一般top类型的会用到三个参数  第二个是匹配的路由
             * convertAndSend(queueName, msg)
             *
             */
            rabbitTemplate.convertAndSend(MqQueueConstant.LOG_QUEUE,authLog);
        }
    }
}
