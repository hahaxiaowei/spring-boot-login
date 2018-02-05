package com.huntkey.rx.sceo.common.entity;

import com.huntkey.rx.edm.entity.DepttreeEntity;
import com.huntkey.rx.edm.entity.EmployeeEntity;
import com.huntkey.rx.edm.entity.EnterpriseEntity;
import com.huntkey.rx.edm.entity.JobpositionEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by caojq on 2017/11/27.
 * 当前状态数据结构，用于在session中记录当前用户的相关状态
 */
public class CurrentStatus implements Serializable {

    private static final long serialVersionUID = -1976421341128232747L;
    /**
     * 当前企业岗位列表(供企业驾驶舱使用)
     */
    private List<JobpositionEntity> currentPositionList;

    /**
     * 当前员工
     */
    private EmployeeEntity currentStaff;
    /**
     * 当前岗位（在岗位驾驶舱使用）
     */
    private JobpositionEntity currentPosition;
    /**
     * 当前企业
     */
    private EnterpriseEntity currentEnterprise;

    /**
     * 当前部门
     */
    private DepttreeEntity depttreeEntity;


    public CurrentStatus(){

    }


    public CurrentStatus(List<JobpositionEntity> currentPositionList,EmployeeEntity currentStaff, JobpositionEntity currentPosition, EnterpriseEntity currentEnterprise, DepttreeEntity depttreeEntity) {
        this.currentPositionList = currentPositionList;
        this.currentStaff = currentStaff;
        this.currentPosition = currentPosition;
        this.currentEnterprise = currentEnterprise;
        this.depttreeEntity = depttreeEntity;
    }

    public EmployeeEntity getCurrentStaff() {
        return currentStaff;
    }

    public JobpositionEntity getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentStaff(EmployeeEntity currentStaff) {
        this.currentStaff = currentStaff;
    }

    public void setCurrentPosition(JobpositionEntity currentPosition) {
        this.currentPosition = currentPosition;
    }


    public DepttreeEntity getDepttreeEntity() {
        return depttreeEntity;
    }

    public void setDepttreeEntity(DepttreeEntity depttreeEntity) {
        this.depttreeEntity = depttreeEntity;
    }

    public EnterpriseEntity getCurrentEnterprise() {
        return currentEnterprise;
    }

    public void setCurrentEnterprise(EnterpriseEntity currentEnterprise) {
        this.currentEnterprise = currentEnterprise;
    }

    public List<JobpositionEntity> getCurrentPositionList() {
        return currentPositionList;
    }

    public void setCurrentPositionList(List<JobpositionEntity> currentPositionList) {
        this.currentPositionList = currentPositionList;
    }
}
