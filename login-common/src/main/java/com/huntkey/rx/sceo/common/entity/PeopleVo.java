package com.huntkey.rx.sceo.common.entity;

import com.huntkey.rx.edm.entity.*;

import java.util.List;

/**
 * Created by lulx on 2017/11/27 0027 下午 14:15
 */
public class PeopleVo {
    private PeopleEntity peopleEntity;
    private String verificatCode;
    private List<EpeoEpeoSkillSetaEntity> epeoEpeoSkillSetaEntities;
    private List<EpeoEpeoCardSetaEntity> epeoEpeoCardSetaEntities;
    private List<EpeoEpeoFamSetaEntity> epeoEpeoFamSetaEntities;
    private List<EpeoEpeoAjobSetaEntity> epeoEpeoAjobSetaEntities;
    private List<StuSetaVo> epeoEpeoStuSetaEntities;
    private List<EpeoEpeoWorkSetaEntity> epeoEpeoWorkSetaEntities;
    private List<EpeoEpeoEnteSetaEntity> epeoEpeoEnteSetaEntities;

    public List<EpeoEpeoSkillSetaEntity> getEpeoEpeoSkillSetaEntities() {
        return epeoEpeoSkillSetaEntities;
    }

    public void setEpeoEpeoSkillSetaEntities(List<EpeoEpeoSkillSetaEntity> epeoEpeoSkillSetaEntities) {
        this.epeoEpeoSkillSetaEntities = epeoEpeoSkillSetaEntities;
    }

    public List<EpeoEpeoCardSetaEntity> getEpeoEpeoCardSetaEntities() {
        return epeoEpeoCardSetaEntities;
    }

    public void setEpeoEpeoCardSetaEntities(List<EpeoEpeoCardSetaEntity> epeoEpeoCardSetaEntities) {
        this.epeoEpeoCardSetaEntities = epeoEpeoCardSetaEntities;
    }

    public List<EpeoEpeoFamSetaEntity> getEpeoEpeoFamSetaEntities() {
        return epeoEpeoFamSetaEntities;
    }

    public void setEpeoEpeoFamSetaEntities(List<EpeoEpeoFamSetaEntity> epeoEpeoFamSetaEntities) {
        this.epeoEpeoFamSetaEntities = epeoEpeoFamSetaEntities;
    }

    public List<EpeoEpeoAjobSetaEntity> getEpeoEpeoAjobSetaEntities() {
        return epeoEpeoAjobSetaEntities;
    }

    public void setEpeoEpeoAjobSetaEntities(List<EpeoEpeoAjobSetaEntity> epeoEpeoAjobSetaEntities) {
        this.epeoEpeoAjobSetaEntities = epeoEpeoAjobSetaEntities;
    }

    public List<StuSetaVo> getEpeoEpeoStuSetaEntities() {
        return epeoEpeoStuSetaEntities;
    }

    public void setEpeoEpeoStuSetaEntities(List<StuSetaVo> epeoEpeoStuSetaEntities) {
        this.epeoEpeoStuSetaEntities = epeoEpeoStuSetaEntities;
    }

    public List<EpeoEpeoWorkSetaEntity> getEpeoEpeoWorkSetaEntities() {
        return epeoEpeoWorkSetaEntities;
    }

    public void setEpeoEpeoWorkSetaEntities(List<EpeoEpeoWorkSetaEntity> epeoEpeoWorkSetaEntities) {
        this.epeoEpeoWorkSetaEntities = epeoEpeoWorkSetaEntities;
    }

    public List<EpeoEpeoEnteSetaEntity> getEpeoEpeoEnteSetaEntities() {
        return epeoEpeoEnteSetaEntities;
    }

    public void setEpeoEpeoEnteSetaEntities(List<EpeoEpeoEnteSetaEntity> epeoEpeoEnteSetaEntities) {
        this.epeoEpeoEnteSetaEntities = epeoEpeoEnteSetaEntities;
    }

    public PeopleEntity getPeopleEntity() {
        return peopleEntity;
    }

    public void setPeopleEntity(PeopleEntity peopleEntity) {
        this.peopleEntity = peopleEntity;
    }

    public String getVerificatCode() {
        return verificatCode;
    }

    public void setVerificatCode(String verificatCode) {
        this.verificatCode = verificatCode;
    }
}
