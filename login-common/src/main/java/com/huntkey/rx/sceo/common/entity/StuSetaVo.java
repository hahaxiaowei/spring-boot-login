package com.huntkey.rx.sceo.common.entity;

import com.huntkey.rx.edm.entity.EpeoEpeoStuSetaEntity;
import com.huntkey.rx.edm.entity.SchoolEntity;

/**
 * Created by lulx on 2018/1/8 0008 上午 9:19
 */
public class StuSetaVo {
    EpeoEpeoStuSetaEntity stuSetaEntity;
    SchoolEntity schoolEntity;

    public EpeoEpeoStuSetaEntity getStuSetaEntity() {
        return stuSetaEntity;
    }

    public void setStuSetaEntity(EpeoEpeoStuSetaEntity stuSetaEntity) {
        this.stuSetaEntity = stuSetaEntity;
    }

    public SchoolEntity getSchoolEntity() {
        return schoolEntity;
    }

    public void setSchoolEntity(SchoolEntity schoolEntity) {
        this.schoolEntity = schoolEntity;
    }
}
