package com.qf.aop;

import java.lang.annotation.*;

/**
 * @version 1.0
 * @data 6/5/2019 21:46
 * @user yingyunzhizi
 */
@Documented
@Target(value = ElementType.METHOD)//表示当前注解的作用范围
@Retention(value = RetentionPolicy.RUNTIME)
public @interface IsLogin {
    boolean mustLogin() default false;
}
