package com.wuqianqian.business.commons.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 生成随机码工具类
 * @author liupeqing
 * @date 2018/9/27 10:37
 */
public class RandomStrUtils {

    // 随机通讯码不重复的时间间隔(ms)
    private static final long		SEQ_NO_NOT_REPEAT_INTERVAL	= 5 * 1000;

    private static Object			lock						= new Object();

    private static RandomStrUtils	instance;

    //将生成的字符串和当前时间存放在map里面
    private Map<String,Long>        randomStrMap                = new ConcurrentHashMap<String,Long>();

    private static final String[]   BASE_STRING                 = new String[]{ "0", "1", "2",
            "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A",
            "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z" };

    private static final int		RANDOM_STRING_LENGTH		= 6;

    //单例模式

    private RandomStrUtils(){}

    public static RandomStrUtils getInstance(){
        synchronized (lock){
            if (null == instance){
                instance = new RandomStrUtils();
            }
        }
        return instance;
    }

    public String getRandomString(){
        Long nowTime = System.currentTimeMillis();
        String randomStr = null;
        synchronized (lock){
            // 生成随机字符串
            randomStr = createRandomString(RANDOM_STRING_LENGTH,nowTime);

            // 删除一分钟前的随机字符串
            //遍历map。如果需要删除的话，需要使用entres.
            Iterator<Map.Entry<String, Long>> it = randomStrMap.entrySet().iterator();
            if (it.hasNext()){
                Map.Entry<String, Long> entry = it.next();
                Long value = entry.getValue();
                if (nowTime - value > SEQ_NO_NOT_REPEAT_INTERVAL){
                    it.remove();
                }
            }
        }
        return randomStr;

    }

    /**
     * 生成随机字符串
     * @return
     */
    private String createRandomString(int len, Long nowTime){
        Random random = new Random();
        int length = BASE_STRING.length;
        String randomStr = "";
        for (int i = 0; i < length; i++){

            //生成一个随机的int值，该值介于[0,n)的区间，也就是0到n之间的随机int值，包含0而不包含n。
            randomStr += BASE_STRING[random.nextInt(length)];
        }
        //System.currentTimeMillis()作为种子  Random本质上生成的是伪随机数，如果使用相同的种子，两个不通的random实例会生成相同的随机数
        random = new Random(System.currentTimeMillis());
        String resultStr = "";
        for (int i = 0; i<len ; i++){
            //charAt(int index)方法是一个能够用来检索特定索引下的字符的String实例的方法.
            resultStr += randomStr.charAt(random.nextInt(randomStr.length() - 1));
        }

        // 判断5秒内是否重复
        Long randomStrCreateTime = randomStrMap.get(resultStr);
        if (randomStrCreateTime != null && nowTime - randomStrCreateTime < SEQ_NO_NOT_REPEAT_INTERVAL){
            resultStr = createRandomString(len,nowTime);
        }
        randomStrMap.put(resultStr,nowTime);
        return resultStr;
    }
}
