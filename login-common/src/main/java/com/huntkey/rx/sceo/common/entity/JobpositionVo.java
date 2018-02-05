package com.huntkey.rx.sceo.common.entity;

import com.huntkey.rx.edm.entity.DepttreeEntity;
import com.huntkey.rx.edm.entity.EmployeeEntity;
import com.huntkey.rx.edm.entity.JobpositionEntity;

import java.io.Serializable;

/**
 * Created by lulx on 2017/11/28 0028 上午 9:05
 */
public class JobpositionVo implements Serializable {
    JobpositionEntity jobpositionEntity;
    DepttreeEntity depttreeEntity;
    EmployeeEntity employeeEntity;

    public EmployeeEntity getEmployeeEntity() {
        return employeeEntity;
    }

    public void setEmployeeEntity(EmployeeEntity employeeEntity) {
        this.employeeEntity = employeeEntity;
    }

    public DepttreeEntity getDepttreeEntity() {
        return depttreeEntity;
    }

    public void setDepttreeEntity(DepttreeEntity depttreeEntity) {
        this.depttreeEntity = depttreeEntity;
    }

    public JobpositionEntity getJobpositionEntity() {
        return jobpositionEntity;
    }

    public void setJobpositionEntity(JobpositionEntity jobpositionEntity) {
        this.jobpositionEntity = jobpositionEntity;
    }
}
