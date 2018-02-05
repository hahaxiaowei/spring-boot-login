package com.huntkey.rx.sceo.login.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created by lulx on 2017/12/27 0027 下午 14:51
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan({"com.huntkey.rx.sceo.login.service.impl", "com.huntkey.rx.sceo.login.controller"})
public class AopConfig {

}
