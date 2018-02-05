package com.huntkey.rx.sceo.common.constant;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.commons.utils.string.StringUtil;

/**
 * 短信发送状态码
 * Created by lulx on 2017/12/25 0025 下午 14:51
 */
public enum MsgStatusCode {
    SUCCESS(1, "短信发送成功", Result.RECODE_SUCCESS),
    FAILED(0, "短信发送失败", Result.RECODE_ERROR),
    AUTHENTICATIONFAILER(-1, "用户名密码不正确", Result.RECODE_ERROR),
    TZW(-2, "必填选项为空", Result.RECODE_ERROR),
    CONTENTEMPTY(-3, "短信内容0个字节", Result.RECODE_ERROR),
    INVALIDNUMNBER(-4, "0个有效号码", Result.RECODE_ERROR),
    INSUFFICIENTBALANCE(-5, "余额不够", Result.RECODE_ERROR),
    ERRORUSER(-10, "用户被禁用", Result.RECODE_ERROR),
    MESSAGELENGTH(-11, "短信内容过长,短信内容超过500字", Result.RECODE_ERROR),
    NOPERMISSION(-12, "用户无扩展权限", Result.RECODE_ERROR),
    ERRORIP(-13, "IP地址校验错", Result.RECODE_ERROR),
    PARSINGEXCEPTIONS(-14, "内容解析异常", Result.RECODE_ERROR),
    ERROR(-990, "未知错误", Result.RECODE_ERROR),
    ;

    private long code;
    private String errorMsg;
    private Integer resultCode;

    MsgStatusCode(long code, String errorMsg, Integer resultCode) {
        this.code = code;
        this.errorMsg = errorMsg;
        this.resultCode = resultCode;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public static MsgStatusCode getMsgStatusCode(long code){
        if(StringUtil.isNullOrEmpty(code)){
            return MsgStatusCode.FAILED;
        }
        if(code > 0){
            return MsgStatusCode.SUCCESS;
        }
        for (MsgStatusCode msgStatusCode : MsgStatusCode.values()){
            if(msgStatusCode.getCode() == code){
                return msgStatusCode;
            }
        }
        return MsgStatusCode.ERROR;
    }
}
