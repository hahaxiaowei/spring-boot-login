package com.huntkey.rx.sceo.common.entity;

/**
 * Created by cjq on 2018/1/11 0023 上午 08:44
 */
public class AreaVo {
    String id ;
    String gare_name;
    String parentId;
    String [] children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String[] getChildren() {
        return children;
    }

    public void setChildren(String[] children) {
        this.children = children;
    }

    public String getGare_name() {
        return gare_name;
    }

    public void setGare_name(String gare_name) {
        this.gare_name = gare_name;
    }
}
