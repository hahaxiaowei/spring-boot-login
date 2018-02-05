package com.huntkey.rx.sceo.common.entity;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * Created by cjq on 2017/7/4 0004 下午 2:01
 */
public class UserInfo {
    private String phone;
    private String password;
    private String newPassWord;
    private String id;
    private MultipartFile file;

    private String epeoCode;//识别码
    private String epeoPassword;    //密码
    private String epeoNameEn;//英文名
    private String epeoNameCn;//姓名拼音
    private String epeoNameNi;//昵称
    private String epeoFistName;//姓
    private String epeoLastName;//名

    private String epeoGender;//性别
    private String epeoCardNo;//身份证号
    private Date epeoBirth;//出生日期
    private String epeoParty;//政治面貌
    private String epeoNation;//民族
    private String epeoMaried;//婚否
    private String epeoHeight;//身高
    private String epeoWeight;//体重
    private String epeoBlood;//血型
    private String epeoRgtPro;//户籍地
    private String epeoOrigin;//籍贯
    private String epeoResidence;//户籍地址
    private String epeoHomeAddr;//家庭住址
    private String epeoAddr;//现住地址
    private String epeoSecuType;//社保类型
    private String epeoSecuNo;//社保电脑号
    private String epeoTel;//手机号
    private String epeoHtel;//家庭电话
    private String epeoMail;//email
    private String epeoSelfEval;//自我评价
    private Date epeoWorkDate;//首次工作日期
    private String epepUtilMon;//求职意向
    private Byte isDel;//
    private String ecosLogicDel;//
    private String ecosIdresServer;
    private Date cretime;
    private String creuser;
    private Date modtime;
    private String moduser;
    private String ecosCode;
    private String verificatCode;

    public String getVerificatCode() {
        return verificatCode;
    }

    public void setVerificatCode(String verificatCode) {
        this.verificatCode = verificatCode;
    }

    public String getEpeoCode() {
        return epeoCode;
    }

    public void setEpeoCode(String epeoCode) {
        this.epeoCode = epeoCode;
    }

    public String getEpeoPassword() {
        return epeoPassword;
    }

    public void setEpeoPassword(String epeoPassword) {
        this.epeoPassword = epeoPassword;
    }

    public String getEpeoNameEn() {
        return epeoNameEn;
    }

    public void setEpeoNameEn(String epeoNameEn) {
        this.epeoNameEn = epeoNameEn;
    }

    public String getEpeoNameCn() {
        return epeoNameCn;
    }

    public void setEpeoNameCn(String epeoNameCn) {
        this.epeoNameCn = epeoNameCn;
    }

    public String getEpeoNameNi() {
        return epeoNameNi;
    }

    public void setEpeoNameNi(String epeoNameNi) {
        this.epeoNameNi = epeoNameNi;
    }

    public String getEpeoFistName() {
        return epeoFistName;
    }

    public void setEpeoFistName(String epeoFistName) {
        this.epeoFistName = epeoFistName;
    }

    public String getEpeoLastName() {
        return epeoLastName;
    }

    public String getNewPassWord() {
        return newPassWord;
    }

    public void setNewPassWord(String newPassWord) {
        this.newPassWord = newPassWord;
    }

    public void setEpeoLastName(String epeoLastName) {
        this.epeoLastName = epeoLastName;
    }

    public String getEpeoGender() {
        return epeoGender;
    }

    public void setEpeoGender(String epeoGender) {
        this.epeoGender = epeoGender;
    }

    public String getEpeoCardNo() {
        return epeoCardNo;
    }

    public void setEpeoCardNo(String epeoCardNo) {
        this.epeoCardNo = epeoCardNo;
    }

    public Date getEpeoBirth() {
        return epeoBirth;
    }

    public void setEpeoBirth(Date epeoBirth) {
        this.epeoBirth = epeoBirth;
    }

    public String getEpeoParty() {
        return epeoParty;
    }

    public void setEpeoParty(String epeoParty) {
        this.epeoParty = epeoParty;
    }

    public String getEpeoNation() {
        return epeoNation;
    }

    public void setEpeoNation(String epeoNation) {
        this.epeoNation = epeoNation;
    }

    public String getEpeoMaried() {
        return epeoMaried;
    }

    public void setEpeoMaried(String epeoMaried) {
        this.epeoMaried = epeoMaried;
    }

    public String getEpeoHeight() {
        return epeoHeight;
    }

    public void setEpeoHeight(String epeoHeight) {
        this.epeoHeight = epeoHeight;
    }

    public String getEpeoWeight() {
        return epeoWeight;
    }

    public void setEpeoWeight(String epeoWeight) {
        this.epeoWeight = epeoWeight;
    }

    public String getEpeoBlood() {
        return epeoBlood;
    }

    public void setEpeoBlood(String epeoBlood) {
        this.epeoBlood = epeoBlood;
    }

    public String getEpeoRgtPro() {
        return epeoRgtPro;
    }

    public void setEpeoRgtPro(String epeoRgtPro) {
        this.epeoRgtPro = epeoRgtPro;
    }

    public String getEpeoOrigin() {
        return epeoOrigin;
    }

    public void setEpeoOrigin(String epeoOrigin) {
        this.epeoOrigin = epeoOrigin;
    }

    public String getEpeoResidence() {
        return epeoResidence;
    }

    public void setEpeoResidence(String epeoResidence) {
        this.epeoResidence = epeoResidence;
    }

    public String getEpeoHomeAddr() {
        return epeoHomeAddr;
    }

    public void setEpeoHomeAddr(String epeoHomeAddr) {
        this.epeoHomeAddr = epeoHomeAddr;
    }

    public String getEpeoAddr() {
        return epeoAddr;
    }

    public void setEpeoAddr(String epeoAddr) {
        this.epeoAddr = epeoAddr;
    }

    public String getEpeoSecuType() {
        return epeoSecuType;
    }

    public void setEpeoSecuType(String epeoSecuType) {
        this.epeoSecuType = epeoSecuType;
    }

    public String getEpeoSecuNo() {
        return epeoSecuNo;
    }

    public void setEpeoSecuNo(String epeoSecuNo) {
        this.epeoSecuNo = epeoSecuNo;
    }

    public String getEpeoTel() {
        return epeoTel;
    }

    public void setEpeoTel(String epeoTel) {
        this.epeoTel = epeoTel;
    }

    public String getEpeoHtel() {
        return epeoHtel;
    }

    public void setEpeoHtel(String epeoHtel) {
        this.epeoHtel = epeoHtel;
    }

    public String getEpeoMail() {
        return epeoMail;
    }

    public void setEpeoMail(String epeoMail) {
        this.epeoMail = epeoMail;
    }

    public String getEpeoSelfEval() {
        return epeoSelfEval;
    }

    public void setEpeoSelfEval(String epeoSelfEval) {
        this.epeoSelfEval = epeoSelfEval;
    }

    public Date getEpeoWorkDate() {
        return epeoWorkDate;
    }

    public void setEpeoWorkDate(Date epeoWorkDate) {
        this.epeoWorkDate = epeoWorkDate;
    }

    public String getEpepUtilMon() {
        return epepUtilMon;
    }

    public void setEpepUtilMon(String epepUtilMon) {
        this.epepUtilMon = epepUtilMon;
    }

    public Byte getIsDel() {
        return isDel;
    }

    public void setIsDel(Byte isDel) {
        this.isDel = isDel;
    }

    public String getEcosLogicDel() {
        return ecosLogicDel;
    }

    public void setEcosLogicDel(String ecosLogicDel) {
        this.ecosLogicDel = ecosLogicDel;
    }

    public String getEcosIdresServer() {
        return ecosIdresServer;
    }

    public void setEcosIdresServer(String ecosIdresServer) {
        this.ecosIdresServer = ecosIdresServer;
    }

    public Date getCretime() {
        return cretime;
    }

    public void setCretime(Date cretime) {
        this.cretime = cretime;
    }

    public String getCreuser() {
        return creuser;
    }

    public void setCreuser(String creuser) {
        this.creuser = creuser;
    }

    public Date getModtime() {
        return modtime;
    }

    public void setModtime(Date modtime) {
        this.modtime = modtime;
    }

    public String getModuser() {
        return moduser;
    }

    public void setModuser(String moduser) {
        this.moduser = moduser;
    }

    public String getEcosCode() {
        return ecosCode;
    }

    public void setEcosCode(String ecosCode) {
        this.ecosCode = ecosCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
