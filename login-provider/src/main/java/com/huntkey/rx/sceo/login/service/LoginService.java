package com.huntkey.rx.sceo.login.service;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.edm.entity.EmployeeEntity;
import com.huntkey.rx.edm.entity.EnterpriseEntity;
import com.huntkey.rx.edm.entity.JobpositionEntity;
import com.huntkey.rx.edm.entity.PeopleEntity;
import com.huntkey.rx.sceo.common.entity.JobpositionVo;
import com.huntkey.rx.sceo.common.entity.UserInfo;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by caojq on 2017/11/21.
 */
@Service
public interface LoginService {

    /**
     * 根据手机号查找用户
     */
    PeopleEntity findUser(String phone);

    /**
     * 根据用户Id，查询当前用户的企业信息
     *
     * @param userId
     * @return
     */
    List<EnterpriseEntity> findEnterpriseList(String userId);

    /**
     * 根据用户Id查询当前用户的岗位信息
     *
     * @param userId
     * @return
     */
    Map<String, List<JobpositionVo>> findJobposition(String userId);

    /**
     * 根据用户Id，查询用户的员工信息
     *
     * @param userId
     * @return
     * @deprecated
     */
    List<EmployeeEntity> findEmployeeEntity(String userId);

    /**
     * 根据用户Id查询当前用户岗位所在的部门
     */
    Map<String, Map<String, JobpositionEntity>> findDepttree(String userId);

    /**
     * 更新当前企业信息
     */
    void updateCurrentEnterprise(Result result, HttpServletRequest request);

    /**
     * 更新当前企业信息
     */
    Result updateCurrentJobInfo(String enterpriseId, String jobId, ServletRequest request);

    /**
     * 用户 登录
     * @param userInfo
     * @return
     */
    Result login(UserInfo userInfo);
}
