package com.xtr.comm.annotation;

import java.lang.annotation.*;


/**
 * <p>自定义注解 拦截Controller</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/5 11:08
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemControllerLog {

    //操作
    String operation() default "";

    //所属模块
    String modelName() default "";

}  