package com.huntkey.rx.sceo.common.utils;

import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.edm.entity.*;
import com.huntkey.rx.sceo.common.entity.CurrentStatus;
import com.huntkey.rx.sceo.common.entity.EcosystemSession;
import com.huntkey.rx.sceo.starter.util.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

/**
 * Created by caojq on 2017/11/27.
 */
public class SessionUtils {

    private static Logger logger = LoggerFactory.getLogger(SessionUtils.class);


    /**
     * 获取会话服务器地址
     */
    public static String getSessionHost() {
        return "this is sessionHost";
    }

    /**
     * 获取会话识别码
     */
    public static String getSessionCode() {
        return "this is sessionCode";
    }

    /**
     * 获取会话风格
     */
    public static String getSessionStyle() {
        return "this is sessionStyle";
    }

    /**
     * 获取当前语言
     */
    public static String getLanguage() {
        return "this is language";
    }


    /**
     * 获取EcosystemSession
     */
    public static EcosystemSession getSession(ServletRequest request) {
        String tokenKey = EncryptUtil.getTokenKey(request);
        logger.info("tokenKey:" + tokenKey);

        byte[] dataStr = RedisUtils.getInstance().getByteValue((tokenKey).getBytes());
        EcosystemSession ecosystemSession = new EcosystemSession();
        if (!StringUtil.isNullOrEmpty(dataStr)) {
            ecosystemSession = (EcosystemSession) SerializeUtils.deserialize(dataStr);
        }
        return ecosystemSession;
    }
}
