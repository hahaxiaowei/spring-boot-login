package com.huntkey.rx.sceo.common.entity;

import com.huntkey.rx.edm.entity.MagnetEntity;
import com.huntkey.rx.edm.entity.RposRposMagnetSetaEntity;

/**
 * Created by lulx on 2018/1/2 0002 上午 9:27
 */
public class MagnetVo {
    MagnetEntity magnetEntity;
    RposRposMagnetSetaEntity magnetSetaEntity;

    public MagnetEntity getMagnetEntity() {
        return magnetEntity;
    }

    public void setMagnetEntity(MagnetEntity magnetEntity) {
        this.magnetEntity = magnetEntity;
    }

    public RposRposMagnetSetaEntity getMagnetSetaEntity() {
        return magnetSetaEntity;
    }

    public void setMagnetSetaEntity(RposRposMagnetSetaEntity magnetSetaEntity) {
        this.magnetSetaEntity = magnetSetaEntity;
    }

}
