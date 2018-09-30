package com.wuqianqian.business.admin.listener;

import com.wuqianqian.business.admin.domain.LogInfo;
import com.wuqianqian.business.admin.service.LogInfoService;
import com.wuqianqian.core.beans.AuthLog;
import com.wuqianqian.core.beans.Log;
import com.wuqianqian.core.commons.constants.CommonConstant;
import com.wuqianqian.core.commons.constants.MqQueueConstant;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 日志队列消息监听：消息对象必须是经过序列化操作的对象
 * @RabbitListener 标注在类上面表示当有收到消息的时候，就交给 @RabbitHandler 的方法处理
 * @author liupeqing
 * @date 2018/9/28 19:18
 */
@Component
@RabbitListener(queues = MqQueueConstant.LOG_QUEUE)
public class LogRabbitListener {

    @Autowired
    private LogInfoService logInfoService;

    @RabbitHandler  //有这个方法处理接受到的消息
    public void receive(AuthLog authLog){
        Log sysLog = authLog.getLog();
        //配合logback 第20行 %X{user}  在日志模板logback.xml 中，使用 %X{ }来占位，替换到对应的 MDC 中 key 的值。
        MDC.put(CommonConstant.KEY_USER,authLog.getLog().getCreateBy());
        Date currentDate = new Date();
        if (null == sysLog.getCreateTime()) sysLog.setCreateTime(currentDate);
        if (null == sysLog.getUpdateTime()) sysLog.setUpdateTime(currentDate);

        LogInfo logInfo = new LogInfo();
        //拷贝  将前面的对象的属性拷贝给后面一个对象
        BeanUtils.copyProperties(sysLog,logInfo);
        logInfoService.saveOrUpdate(logInfo);
        MDC.remove(CommonConstant.KEY_USER);
    }


}
