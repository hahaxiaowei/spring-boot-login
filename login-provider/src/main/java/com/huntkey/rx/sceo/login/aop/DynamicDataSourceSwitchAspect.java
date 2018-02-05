package com.huntkey.rx.sceo.login.aop;

import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.sceo.common.utils.DynamicDataSourceUtil;
import com.huntkey.rx.sceo.common.utils.StringUtils;
import com.huntkey.rx.sceo.common.utils.UserUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by lulx on 2017/12/27 0027 下午 15:02
 */
@Aspect
@Component
public class DynamicDataSourceSwitchAspect {

    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceSwitchAspect.class);

    @Pointcut("@annotation(com.huntkey.rx.sceo.login.aop.DynamicDataSource)")
    public void annotationPointcut() {
    }

    @Before("annotationPointcut()")
    public void before(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        DynamicDataSource annotation = method.getAnnotation(DynamicDataSource.class);
        boolean isDefaultSource = annotation.isDefaultSource();
        logger.info("isDefaultSource : {}", isDefaultSource);
        if (isDefaultSource) {
            DynamicDataSourceUtil.setDefaultDataSource();
            logger.info("set defult dataSource success");
            return;
        }
        String dataSource = annotation.dataSource();
        logger.info("dataSource : {}", dataSource);
        if (StringUtil.isNullOrEmpty(dataSource)) {
            UserUtil.switchDataSource();
            logger.info("switchDataSource success");
        } else {
            DynamicDataSourceUtil.setDataSource(dataSource);
            logger.info("setDataSourceType success, dataSource : {}", StringUtils.getEnterpriseDbName(dataSource));
        }
    }
}