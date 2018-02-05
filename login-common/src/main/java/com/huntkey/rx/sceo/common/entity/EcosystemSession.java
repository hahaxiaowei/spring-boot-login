package com.huntkey.rx.sceo.common.entity;

import com.huntkey.rx.edm.entity.DepttreeEntity;
import com.huntkey.rx.edm.entity.EnterpriseEntity;
import com.huntkey.rx.edm.entity.JobpositionEntity;
import com.huntkey.rx.edm.entity.PeopleEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author caojq
 * @date 2017/11/27
 */
public class EcosystemSession implements Serializable {

    private static final long serialVersionUID = 8861971969514436338L;
    /**
     * 会话服务器地址
     */
    private String sessionHost;

    /**
     * 会话识别码
     */
    private String sessionCode;

    /**
     * 会话语言
     */
    private String language;

    /**
     * 会话风格
     */
    private String sessionStyle;

    /**
     * 自然人对象
     */
    private PeopleEntity people;

    /**
     * 会话对象集合
     */
    private Map<String, SessionObjectSet> sessionnObjectSet;

    /**
     * 当前活跃对象
     */
    private CurrentStatus currentStatus;


    public String getSessionHost() {
        return sessionHost;
    }

    public String getSessionCode() {
        return sessionCode;
    }

    public String getLanguage() {
        return language;
    }

    public String getSessionStyle() {
        return sessionStyle;
    }

    public PeopleEntity getPeople() {
        return people;
    }


    public void setSessionHost(String sessionHost) {
        this.sessionHost = sessionHost;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setSessionStyle(String sessionStyle) {
        this.sessionStyle = sessionStyle;
    }

    public void setPeople(PeopleEntity people) {
        this.people = people;
    }

    public Map<String, SessionObjectSet> getSessionnObjectSet() {
        return sessionnObjectSet;
    }

    public void setSessionnObjectSet(Map<String, SessionObjectSet> sessionnObjectSet) {
        this.sessionnObjectSet = sessionnObjectSet;
    }

    public CurrentStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(CurrentStatus currentStatus) {
        this.currentStatus = currentStatus;
    }
}
