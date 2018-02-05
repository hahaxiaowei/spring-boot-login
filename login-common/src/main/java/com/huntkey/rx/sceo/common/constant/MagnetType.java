package com.huntkey.rx.sceo.common.constant;

/**
 * 磁贴类型
 * Created by lulx on 2017/12/27 0027 上午 10:32
 */
public enum MagnetType {

    Magnet("1", "驾驶舱磁贴"),
    MetroStart("2", "快速启动磁贴"),;

    private String type;
    private String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    MagnetType(String type, String name) {

        this.type = type;
        this.name = name;
    }
}
