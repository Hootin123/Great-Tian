package com.xtr.api.dto.gateway.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xuewu on 2016/8/3.
 */

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParaName {
    String value() default "";
}
