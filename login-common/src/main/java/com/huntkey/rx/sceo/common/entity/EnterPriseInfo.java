package com.huntkey.rx.sceo.common.entity;

/**
 * Created by sunwei on 2017/11/22 Time:17:22
 */
public class EnterPriseInfo {
    /**
     * 企业全称
     */
    String enteNameCn;

    /**
     *组织机构代码
     */
    String enteOrgCode;

    /**
     * 企业简称
     */
    String enteNickName;

    /**
     * 企业法人代表
     */
    String enteFictPeop;

    /**
     *地址
     */
    String enteAddr;

    /**
     *  网址
     */
    String enteSceoUrl;

    /**
     * 起始日期
     */
    String enteFinaBeg;

    /**
     *数据库用户
     */
    String enteDbuser;

    /**
     * 数据库密码
     */
    String enteDbpassword;


    public String getEnteNameCn() {
        return enteNameCn;
    }

    public void setEnteNameCn(String enteNameCn) {
        this.enteNameCn = enteNameCn;
    }

    public String getEnteOrgCode() {
        return enteOrgCode;
    }

    public void setEnteOrgCode(String enteOrgCode) {
        this.enteOrgCode = enteOrgCode;
    }

    public String getEnteNickName() {
        return enteNickName;
    }

    public void setEnteNickName(String enteNickName) {
        this.enteNickName = enteNickName;
    }

    public String getEnteFictPeop() {
        return enteFictPeop;
    }

    public void setEnteFictPeop(String enteFictPeop) {
        this.enteFictPeop = enteFictPeop;
    }

    public String getEnteAddr() {
        return enteAddr;
    }

    public void setEnteAddr(String enteAddr) {
        this.enteAddr = enteAddr;
    }

    public String getEnteSceoUrl() {
        return enteSceoUrl;
    }

    public void setEnteSceoUrl(String enteSceoUrl) {
        this.enteSceoUrl = enteSceoUrl;
    }

    public String getEnteFinaBeg() {
        return enteFinaBeg;
    }

    public void setEnteFinaBeg(String enteFinaBeg) {
        this.enteFinaBeg = enteFinaBeg;
    }

    public String getEnteDbuser() {
        return enteDbuser;
    }

    public void setEnteDbuser(String enteDbuser) {
        this.enteDbuser = enteDbuser;
    }

    public String getEnteDbpassword() {
        return enteDbpassword;
    }

    public void setEnteDbpassword(String enteDbpassword) {
        this.enteDbpassword = enteDbpassword;
    }

    @Override
    public String toString() {
        return "EnterPriseInfo{" +
                "enteNameCn='" + enteNameCn + '\'' +
                ", enteOrgCode='" + enteOrgCode + '\'' +
                ", enteNickName='" + enteNickName + '\'' +
                ", enteFictPeop='" + enteFictPeop + '\'' +
                ", enteAddr='" + enteAddr + '\'' +
                ", enteSceoUrl='" + enteSceoUrl + '\'' +
                ", enteFinaBeg='" + enteFinaBeg + '\'' +
                ", enteDbuser='" + enteDbuser + '\'' +
                ", enteDbpassword='" + enteDbpassword + '\'' +
                '}';
    }
}
