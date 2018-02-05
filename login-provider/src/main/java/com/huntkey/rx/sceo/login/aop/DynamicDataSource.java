package com.huntkey.rx.sceo.login.aop;

import java.lang.annotation.*;

/**
 * 动态数据源
 * Created by lulx on 2017/12/27 0027 下午 14:35
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicDataSource {

    /**
     * 是否采用默认数据源
     * @return
     */
    boolean isDefaultSource() default false;

    /**
     * 数据源key
     * @return
     */
    String dataSource() default  "";
}
