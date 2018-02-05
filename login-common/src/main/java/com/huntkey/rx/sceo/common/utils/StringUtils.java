package com.huntkey.rx.sceo.common.utils;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.sceo.common.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by lulx on 2017/10/25 0025 上午 9:35
 */
public class StringUtils {
    private static Logger logger = LoggerFactory.getLogger(StringUtils.class);

    public static String getMapValByKey(Map<String, Object> map, String key) {
        if (map == null) {
            return "";
        }
        Object val = map.get(key);
        return val == null ? "" : val.toString();
    }

    public static Date stringToDate(String Time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(Time);
        } catch (ParseException e) {
            logger.info("stringToDate字符串转日期运行异常" + e.getMessage() + e);
            throw new RuntimeException(e);
        }
        return date;
    }

    /**
     * 验证企业代码是否正确
     *
     * @param code 企业组织机构代码
     * @return
     */
    public static final boolean isValidEntpCode(String code) {

        int[] ws = {3, 7, 9, 10, 5, 8, 4, 2};
        String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String regex = "^([0-9A-Z]){8}-[0-9|X]$";

        if (!code.matches(regex)) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            sum += str.indexOf(String.valueOf(code.charAt(i))) * ws[i];
        }
        logger.info("sum is {}", sum);
        logger.info("sum % 11 is {}", sum % 11);

        int c9 = 11 - (sum % 11);

        String sc9 = String.valueOf(c9);
        if (11 == c9) {
            sc9 = "0";
        } else if (10 == c9) {
            sc9 = "X";
        }
        logger.info("sc9 is {}", sc9);
        return sc9.equals(String.valueOf(code.charAt(9)));
    }

    public static String getEnterpriseDbName(String enteSceoUrl) {
        if (StringUtil.isNullOrEmpty(enteSceoUrl)) {
            return Constant.EDM_DATABASE_KEY;
        }
        String dbName = enteSceoUrl;
        if (!Constant.EDM_DATABASE_KEY.equalsIgnoreCase(enteSceoUrl)) {
            dbName = dbName + Constant.ENTERPRISE_DB_NAME_SUFFIX;
        } else {
            dbName = Constant.EDM_DATABASE_KEY;
        }
        return dbName;
    }

    /**
     * 预处理Result
     *
     * @param preResult
     * @param erroMsg
     * @return
     */
    public static Result preHandleResult(Result preResult, String erroMsg) {
        if (!StringUtil.isNullOrEmpty(preResult)) {
            if (Result.RECODE_SUCCESS.equals(preResult.getRetCode())) {
                return preResult;
            } else {
                preResult.setErrMsg(erroMsg + " : " + preResult.getErrMsg());
                return preResult;
            }
        } else {
            preResult = new Result();
            preResult.setRetCode(Result.RECODE_ERROR);
            preResult.setErrMsg(erroMsg + " : error");
            return preResult;
        }
    }
}
