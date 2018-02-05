package com.huntkey.rx.sceo.login.aop;

import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.sceo.common.constant.Constant;
import com.huntkey.rx.sceo.common.utils.NetworkUtil;
import com.huntkey.rx.sceo.starter.util.RedisUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Created by lulx on 2018/1/10 0010 上午 9:22
 */
@Aspect
@Component
public class LimitIpRequestAspect {

    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceSwitchAspect.class);

    /**
     * "execution(* com.huntkey.rx.sceo.login.controller.*.*(..)) && @annotation(com.huntkey.rx.sceo.login.aop.LimitIpRequest)"
     */
    @Pointcut("@annotation(com.huntkey.rx.sceo.login.aop.LimitIpRequest)")
    public void annotationPointcut() {
    }

    @Before("annotationPointcut()")
    public void before(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        LimitIpRequest annotation = method.getAnnotation(LimitIpRequest.class);
        String methodUrl = annotation.methodUrl();
        int limitCount = annotation.limitCount();
        Long limitTime = annotation.limitTime();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();

        if (StringUtil.isNullOrEmpty(request)) {
            throw new RuntimeException("无法获取当前请求的HttpServletRequest");
        }

        String ipAddress = NetworkUtil.getIpAddress(request);
        // TODO 应该加Redi分布式锁  但精确度不需要很高
        String urlIpRedisKey = Constant.LOGIN_AUTHENTICATION_ + methodUrl + "_" + ipAddress;
        long urlIpCount = RedisUtils.getInstance().incrCounter(Constant.LOGIN_AUTHENTICATION_ + methodUrl, ipAddress);

        if (1 == urlIpCount) {
            RedisUtils.getInstance().expire(urlIpRedisKey, limitTime.intValue());
        }

        if (urlIpCount > limitCount) {
            logger.error("用户IP[ {} ]访问地址[ {} ]超过了限定的次数[ {} ]", ipAddress, methodUrl, limitCount);
            throw new RuntimeException("您访问接口的次数太频繁，请稍后再试。");
        }
    }
}
