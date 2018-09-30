package com.wuqianqian.business.admin.web.controller;

import com.google.code.kaptcha.Producer;
import com.wuqianqian.business.admin.service.UserService;
import com.wuqianqian.business.commons.permisssion.Module;
import com.wuqianqian.business.commons.web.aop.PrePermissions;
import com.wuqianqian.core.commons.constants.SecurityConstant;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 图像验证码 controller
 * @author liupeqing
 * @date 2018/9/29 14:53
 */
@Controller
@PrePermissions(value = Module.CODE,required = false)
public class CodeController {


    @Autowired
    private Producer producer;

    @Autowired
    private UserService userService;

    /**
     * 创建验证码
     * @param randomStr
     * @param request
     * @param response
     */
    @GetMapping(SecurityConstant.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/{randomStr}")
    public void createCode(@PathVariable("randomStr") String randomStr, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Cache-control","no-store,no-cache");
        response.setContentType("image/jpeg");
        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        userService.saveImageCode(randomStr,text);
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image,"JPEG",outputStream);
        IOUtils.closeQuietly(outputStream);
    }
}
