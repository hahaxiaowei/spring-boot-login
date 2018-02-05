package com.huntkey.rx.sceo.common.entity;

import com.huntkey.rx.edm.entity.EnterpriseEntity;

import java.util.List;

/**
 * Created by lulx on 2017/11/23 0023 下午 15:44
 */
public class EnterpriseVo {
    EnterpriseEntity enterpriseEntity;
    List<JobpositionVo> jobpositionVos;
    String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public EnterpriseEntity getEnterpriseEntity() {
        return enterpriseEntity;
    }

    public void setEnterpriseEntity(EnterpriseEntity enterpriseEntity) {
        this.enterpriseEntity = enterpriseEntity;
    }

    public List<JobpositionVo> getJobpositionVos() {
        return jobpositionVos;
    }

    public void setJobpositionVos(List<JobpositionVo> jobpositionVos) {
        this.jobpositionVos = jobpositionVos;
    }
}
