package com.lzh.blog.common.aop;

import java.lang.annotation.*;

/**
 * 日志注解,type表示可以放在类上面，method表示可以放在方法上
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {

    String module() default "";

    String operation() default "";

}
