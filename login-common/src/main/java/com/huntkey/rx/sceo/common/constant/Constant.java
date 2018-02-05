package com.huntkey.rx.sceo.common.constant;

public class Constant {
    /**
     * jwt
     */
    public static final String JWT_ID = "jwt";
    public static final String JWT_SECRET = "7786df7fc3a34e26a61c034d5ec8245d";
    public static final int JWT_TTL = 7 * 24 * 60 * 60 * 1000;  //millisecond 过期时间7天
    public static final int JWT_REFRESH_INTERVAL = 55 * 60 * 1000;  //millisecond
    public static final int JWT_REFRESH_TTL = 12 * 60 * 60 * 1000;  //millisecond

    public static final String CONTTRY_CODE = "00000000000000000000000000100000";

    /**
     * redis key值前缀项目识别码
     */
    public static String LOGIN_AUTHENTICATION_ = "LOGIN_AUTHENTICATION_";

    /**
     * EDM数据库代码
     */
    public static String EDM_DATABASE_KEY = "edmdb";

    /**
     * ecodb数据库代码
     */
    public static String ECO_DATABASE_KEY = "ecodb";

    /**
     * 用户默认密码
     */
    public static String DEFAULT_USER_PASSWORD = "123456";

    /**
     * modeler创建数据库版本号
     */
    public static String DEFAULT_EDM_DB_CREATE_VERSION = "V1.0";

    /**
     * 用户token
     */
    public static String TOKEN_TRANSFER_KEY = "Authorization";

    /**
     * 企业数据库用户名后缀
     */
    public static String ENTERPRISE_DB_NAME_SUFFIX = "_edmdb";

    /**
     * 岗位类名
     */
    public static String CLASS_NAME_JOBPOSITION = "jobposition";
}
