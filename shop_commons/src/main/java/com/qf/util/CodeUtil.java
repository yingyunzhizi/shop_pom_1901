package com.qf.util;

/**
 * @version 1.0
 * @data 6/4/2019 19:32
 * @user yingyunzhizi
 */
public class CodeUtil {

    /**
     * 生成随机验证码,1000-9999,因为Math.random是[0,1),所以*9000就是0到无限接近9000,+1000取整就得到了1000-9999
     * @return
     */
    public static int createRandomCode(){
        return (int) (Math.random()*9000+1000);
    }

}


