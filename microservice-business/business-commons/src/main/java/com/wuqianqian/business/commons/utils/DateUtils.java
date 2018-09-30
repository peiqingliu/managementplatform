package com.wuqianqian.business.commons.utils;

import com.wuqianqian.springcloud.StringHelper;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * @author liupeqing
 * @date 2018/9/27 10:09
 */
@Slf4j
public class DateUtils {

    private static final String	DEFAULT_CONVERT_PATTERN	= "yyyyMMddHHmmssSSS";

    /**
     * 获取当前日期字符串(以默认格式)
     * @return
     */
    public static String getCurrentTimeStrDefault(){
        return  getCurrentTimeStr(DEFAULT_CONVERT_PATTERN);
    }

    /**
     * 获取指定日期字符串(以默认格式)
     * @param date
     * @return
     */
    public static String getTimeStrDefault(Date date){
        return  getTimeStr(date,DEFAULT_CONVERT_PATTERN);
    }
    /**
     * 获取当前日期字符串
     * @param pattern
     * @return
     */
    public static String getCurrentTimeStr(String pattern){

        if (StringHelper.isBlank(pattern)) return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date());
    }

    /**
     * 获取指定日期的字符串
     * @param date
     * @param pattern
     * @return
     */
    public static String getTimeStr(Date date, String pattern){
        if (null == date || StringHelper.isBlank(pattern)) return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 判断时间字符串是否是默认格式
     * @param dateTimeStr
     * @return
     */
    public static boolean isValidDefaultFormat(String dateTimeStr){
        if (StringHelper.isBlank(dateTimeStr)) return false;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_CONVERT_PATTERN);
        try {
            dateFormat.parse(dateTimeStr);
            return true;
        }catch (Exception e){
            log.error("时间转换错误。" + dateTimeStr);
            return false;
        }
    }
}
