package com.huntkey.rx.sceo.common.utils;

import com.huntkey.rx.commons.utils.eco.EcoSystemUtil;
import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.edm.entity.EnterpriseEntity;
import com.huntkey.rx.edm.entity.JobpositionEntity;
import com.huntkey.rx.sceo.common.constant.Constant;
import com.huntkey.rx.sceo.common.entity.EcosystemSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户信息辅助工具类
 * Created by lulx on 2017/12/13 0013 下午 15:38
 */
public class UserUtil {

    private static Logger logger = LoggerFactory.getLogger(UserUtil.class);

    /**
     * 获取当前用户ID
     *
     * @return
     */
    public static String getUserId() {
        try {
            HttpServletRequest requestThread = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String authThread = requestThread.getHeader(Constant.TOKEN_TRANSFER_KEY);
            Map<String, String> usermap = EcoSystemUtil.ifAuthUsable(authThread);
            String userId = usermap.get("id");
            return userId;
        } catch (Exception e) {
            logger.error("getUserId error: " + e.getMessage(), e);
            throw new RuntimeException("getUserId error: " + e.getMessage());
        }
    }

    /**
     * 获取当前获取的session信息
     *
     * @return
     */
    public static EcosystemSession getEcosystem() {
        try {
            HttpServletRequest requestThread = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            EcosystemSession ecosystemSession = SessionUtils.getSession(requestThread);
            return ecosystemSession;
        } catch (Exception e) {
            logger.error("getEcosystem error: " + e.getMessage(), e);
            throw new RuntimeException("getEcosystem error: " + e.getMessage());
        }
    }

    /**
     * 获取当前用户的企业信息
     *
     * @return
     */
    public static EnterpriseEntity getEnterprise() {
        try {
            EcosystemSession ecosystem = getEcosystem();
            return ecosystem.getCurrentStatus().getCurrentEnterprise();
        } catch (Exception e) {
            logger.error("getEnterprise error: " + e.getMessage(), e);
            throw new RuntimeException("getEnterprise error: " + e.getMessage());
        }
    }

    /**
     * 获取当前用户的岗位信息
     *
     * @return
     */
    public static JobpositionEntity getPosition() {
        try {
            EcosystemSession ecosystem = getEcosystem();
            return ecosystem.getCurrentStatus().getCurrentPosition();
        } catch (Exception e) {
            logger.error("getPosition error: " + e.getMessage(), e);
            throw new RuntimeException("getPosition error: " + e.getMessage());
        }
    }

    /**
     * 切换到当前企业的数据源
     */
    public static void switchDataSource() {
        try {
            EnterpriseEntity enterprise = getEnterprise();
            if (StringUtil.isNullOrEmpty(enterprise)) {
                throw new RuntimeException("get enterprise msg null");
            }
            String dbName = enterprise.getEnte_sceo_url();
            if (StringUtil.isNullOrEmpty(dbName)) {
                throw new RuntimeException("get enterprise SceoUrl null");
            }
            DynamicDataSourceUtil.setDataSource(dbName);
            logger.info("switch datasource dbName: {}", dbName);
        } catch (Exception e) {
            logger.error("switchDataSource error: " + e.getMessage(), e);
            throw new RuntimeException("switchDataSource error: " + e.getMessage());
        }
    }

}
