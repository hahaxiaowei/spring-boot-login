package com.huntkey.rx.sceo.common.utils;

import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.sceo.common.constant.Constant;
import com.huntkey.rx.sceo.orm.config.DynamicDataSourceContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 数据源切换辅助类
 * <p>
 * 统一管理
 * Created by lulx on 2018/1/2 0002 下午 17:39
 */
public class DynamicDataSourceUtil {
    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceUtil.class);

    /**
     * 设置默认数据源
     */
    public static void setDefaultDataSource() {
        try {
            //DynamicDataSourceContextHolder.clearDataSourceType();
            logger.info("exec createEmployee, dbName : {}; dbList: {}", Constant.ECO_DATABASE_KEY, getDataSourceIds());
            DynamicDataSourceContextHolder.setDataSourceType(Constant.ECO_DATABASE_KEY);
        } catch (Exception e) {
            logger.error("setDefaultDataSource exec error : " + e.getMessage(), e);
            throw new RuntimeException("setDefaultDataSource exec error : " + e.getMessage(), e);
        }
    }

    /**
     * 根据传入的企业url设置数据源
     *
     * @param sceoUrl
     */
    public static void setDataSource(String sceoUrl) {
        if (StringUtil.isNullOrEmpty(sceoUrl)) {
            logger.info("根据传入的企业url设置数据源： sceoUrl is null");
            DynamicDataSourceContextHolder.clearDataSourceType();
            logger.info("set defult dataSource success");
            return;
        }
        try {
            logger.info("sceoUrl : {}", sceoUrl);
            String dbName = StringUtils.getEnterpriseDbName(sceoUrl);
            if(Constant.EDM_DATABASE_KEY.equals(dbName)){
                DynamicDataSourceContextHolder.clearDataSourceType();
                logger.info("set defult dataSource success");
                return;
            }
            logger.info("exec setDataSource, dbName : {}; dbList: {}", dbName, getDataSourceIds());
            DynamicDataSourceContextHolder.setDataSourceType(dbName);
            logger.info("set {} dataSource success", dbName);
        } catch (Exception e) {
            logger.error("setDataSource exec error : " + e.getMessage(), e);
            throw new RuntimeException("setDataSource exec error : " + e.getMessage(), e);
        }
    }

    public static List<String> getDataSourceIds(){
        try {
            List<String> dataSourceIds = DynamicDataSourceContextHolder.dataSourceIds;
            return dataSourceIds;
        } catch (Exception e) {
            logger.error("getDataSourceIds exec error : " + e.getMessage(), e);
            throw new RuntimeException("getDataSourceIds exec error : " + e.getMessage(), e);
        }
    }
}
