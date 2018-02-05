package com.huntkey.rx.sceo.login.aop;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * 限制某个IP访问某个时间段内请求某个方法的次数
 * Created by lulx on 2018/1/10 0010 上午 9:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface LimitIpRequest {

    /**
     * 限制某时间内ip可访问方法的次数
     *
     * @return
     */
    int limitCount() default 100;

    /**
     * 方法的url
     *
     * @return
     */
    String methodUrl();

    /**
     * 限制时间段
     * 单位为秒 默认值为一分钟
     *
     * @return
     */
    long limitTime() default 60;
}
