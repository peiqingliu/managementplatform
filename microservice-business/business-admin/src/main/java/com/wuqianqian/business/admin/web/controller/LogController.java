package com.wuqianqian.business.admin.web.controller;

import com.wuqianqian.business.admin.domain.LogInfo;
import com.wuqianqian.business.admin.service.LogInfoService;
import com.wuqianqian.business.commons.permisssion.Functional;
import com.wuqianqian.business.commons.permisssion.Module;
import com.wuqianqian.business.commons.web.BaseController;
import com.wuqianqian.business.commons.web.aop.PrePermissions;
import com.wuqianqian.springcloud.R;
import com.wuqianqian.springcloud.page.PageBean;
import com.wuqianqian.springcloud.page.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志controller
 * @author liupeqing
 * @date 2018/9/29 15:28
 */
@RestController
@RequestMapping("/logs")
@PrePermissions(value = Module.LOG)
public class LogController extends BaseController {


    @Autowired
    private LogInfoService logInfoService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @PrePermissions(value = Functional.VIEW)
    public R<PageBean<LogInfo>> list(HttpServletRequest request, LogInfo logInfo,
                                     PageParams pageParams) {
        PageBean<LogInfo> pageData = this.logInfoService.findAll(pageParams, logInfo);
        return new R<PageBean<LogInfo>>().data(pageData);
    }

    @RequestMapping(value = "/del/{id}", method = RequestMethod.POST)
    @PrePermissions(value = Functional.DEL)
    public R<Boolean> del(HttpServletRequest request, @PathVariable Long id) {
        if (null == id || id <= 0)
            return new R<Boolean>().data(false).failure("日志id为空");

        boolean isDel = this.logInfoService.delById(id);
        return new R<Boolean>().data(isDel);
    }
}
