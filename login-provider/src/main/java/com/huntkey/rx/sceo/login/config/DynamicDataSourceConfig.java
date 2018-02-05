package com.huntkey.rx.sceo.login.config;

import com.huntkey.rx.sceo.orm.config.DynamicDataSourceRegister;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 动态数据源配置
 * Created by lulx on 2017/12/14 0014 下午 15:18
 */
@Configuration
@Import(DynamicDataSourceRegister.class)
public class DynamicDataSourceConfig {
}
