package com.huntkey.rx.sceo.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by caojq on 2017/11/27.
 * 会话对象集
 */
public class SessionObjectSet implements Serializable {

    private static final long serialVersionUID = 6541658911128885884L;
    private Map<String, Object> sessionnRelatedObject;//会话关联对象
    private Date lastInteractDate;//最后一次交互时间

    public SessionObjectSet(Map<String, Object> sessionnRelatedObject, Date lastInteractDate) {
        this.sessionnRelatedObject = sessionnRelatedObject;
        this.lastInteractDate = lastInteractDate;
    }

    public Map<String, Object> getSessionnRelatedObject() {
        return sessionnRelatedObject;
    }


    public Date getLastInteractDate() {
        return lastInteractDate;
    }

    public void setSessionnRelatedObject(Map<String, Object> sessionnRelatedObject) {
        this.sessionnRelatedObject = sessionnRelatedObject;
    }

    public void setLastInteractDate(Date lastInteractDate) {
        this.lastInteractDate = lastInteractDate;
    }
}
