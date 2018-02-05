package com.huntkey.rx.sceo.common.entity;

import java.io.Serializable;

/**
 * Created by caojq on 2017/11/27.
 * 登录设备类
 */
public class Facility  implements Serializable {

    private static final long serialVersionUID = 8435242146560274694L;
    private String macAddr; //mac地址
    private String imei;//手机串号
    private String jwt;//token

    public Facility(String jwt) {
        this.jwt = jwt;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public String getImei() {
        return imei;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = result * 31 + jwt.hashCode();
        return result;
    }
}
