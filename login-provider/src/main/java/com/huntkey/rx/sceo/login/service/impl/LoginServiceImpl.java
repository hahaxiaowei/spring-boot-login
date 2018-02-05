package com.huntkey.rx.sceo.login.service.impl;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.edm.entity.*;
import com.huntkey.rx.sceo.common.entity.*;
import com.huntkey.rx.sceo.common.utils.*;
import com.huntkey.rx.sceo.login.aop.DynamicDataSource;
import com.huntkey.rx.sceo.login.controller.LoginController;
import com.huntkey.rx.sceo.login.service.LoginService;
import com.huntkey.rx.sceo.login.service.UserService;
import com.huntkey.rx.sceo.orm.common.model.OrmParam;
import com.huntkey.rx.sceo.orm.service.OrmService;
import com.huntkey.rx.sceo.starter.util.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by caojq on 2017/11/21.
 */
@Service
public class LoginServiceImpl implements LoginService {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private OrmService ormService;

    @Autowired
    private UserService userService;

    @Value("${redisTimeoutSeconds}")
    String redisTimeoutSeconds;

    @Autowired
    private TelLogin telLogin;

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public PeopleEntity findUser(String phone) {
        OrmParam ormParam = new OrmParam();
        logger.info("查找用户 phone：" + phone);
        ormParam.addWhereParam("epeo_tel", phone);
        String whereExp = "epeo_tel = #{whereParam.epeo_tel}";
        ormParam.setWhereExp(whereExp);
        try {
            List<PeopleEntity> list = ormService.selectBeanList(PeopleEntity.class, ormParam);
            if (StringUtil.isNullOrEmpty(list) || list.size() == 0) {
                logger.error("查找用户为空！" + list);
                return null;
            } else {
                logger.info("查找用户结果：" + list.get(0).getId());
                return list.get(0);
            }
        } catch (Exception e) {
            logger.error("查找用户信息失败：" + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @param userId 自然人的Id
     * @return java.util.List<com.huntkey.rx.edm.entity.EnterpriseEntity>
     * @description 根据自然人的Id查询该自然人的企业信息
     * @method findEnterprise
     */
    @Override
    @DynamicDataSource(isDefaultSource = true)
    public List<EnterpriseEntity> findEnterpriseList(String userId) {
        try {
            List<EnterpriseVo> enterpriseVoList = (List<EnterpriseVo>) userService.queryEnterpriseList(userId).getData();
            List<EnterpriseEntity> enterpriseEntityList = new ArrayList<EnterpriseEntity>();
            if (!StringUtil.isNullOrEmpty(enterpriseVoList) && enterpriseVoList.size() != 0) {
                for (EnterpriseVo enterpriseVo : enterpriseVoList) {
                    enterpriseEntityList.add(enterpriseVo.getEnterpriseEntity());
                }
                return enterpriseEntityList;
            } else {
                logger.info("查询该人的企业信息不存在");
                return enterpriseEntityList;
            }
        } catch (Exception e) {
            logger.error("findEnterpriseList方法执行异常" + e.getMessage() + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @param userId 自然人的id
     * @return java.util.List<com.huntkey.rx.edm.entity.JobpositionEntity>
     * @description 根据自然的Id查询该自然人的岗位信息
     * @method findJobposition
     */
    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Map<String, List<JobpositionVo>> findJobposition(String userId) {
        try {
            //根据userId查到企业信息
            List<EnterpriseVo> enterpriseVoList = (List<EnterpriseVo>) userService.queryEnterpriseList(userId).getData();
            Map<String, List<JobpositionVo>> jobpositionVoMap = new HashMap<String, List<JobpositionVo>>();
            if (!StringUtil.isNullOrEmpty(enterpriseVoList) && enterpriseVoList.size() != 0) {
                for (EnterpriseVo enterpriseVo : enterpriseVoList) {
                    jobpositionVoMap.put(enterpriseVo.getEnterpriseEntity().getEnte_org_code(), enterpriseVo.getJobpositionVos());
                }
                return jobpositionVoMap;
            } else {
                logger.info("查询该人的岗位信息不存在");
                return jobpositionVoMap;
            }
        } catch (Exception e) {
            logger.error("findJobposition方法执行异常" + e.getMessage() + e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EmployeeEntity> findEmployeeEntity(String userId) {
        return null;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Map<String, Map<String, JobpositionEntity>> findDepttree(String userId) {
        try {
            //根据userId查到企业信息
            Map<String, Map<String, JobpositionEntity>> enterpriseVoMapMap = new HashMap<String, Map<String, JobpositionEntity>>();
            List<EnterpriseVo> enterpriseVoList = (List<EnterpriseVo>) userService.queryEnterpriseList(userId).getData();

            if (!StringUtil.isNullOrEmpty(enterpriseVoList) && enterpriseVoList.size() != 0) {
                for (EnterpriseVo enterpriseVo : enterpriseVoList) {
                    Map<String, JobpositionEntity> map = new HashMap<String, JobpositionEntity>();
                    for (JobpositionVo jobpositionVo : enterpriseVo.getJobpositionVos()) {
                        map.put(jobpositionVo.getDepttreeEntity().getMdep_code(), jobpositionVo.getJobpositionEntity());
                    }
                    enterpriseVoMapMap.put(enterpriseVo.getEnterpriseEntity().getEnte_org_code(), map);
                }
                return enterpriseVoMapMap;
            } else {
                logger.info("查询该人的部门信息不存在");
                return enterpriseVoMapMap;
            }
        } catch (Exception e) {
            logger.error("findJobposition方法执行异常" + e.getMessage() + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @description 更新当前企业信息
     */
    @Override
    @DynamicDataSource(isDefaultSource = true)
    public void updateCurrentEnterprise(Result result, HttpServletRequest request) {
        try {
            logger.info("开始更新当前企业信息!");
            EcosystemSession ecosystemSession = SessionUtils.getSession(request);
            EnterpriseVo enterpriseVo = (EnterpriseVo) result.getData();
            EnterpriseEntity enterpriseEntity = enterpriseVo.getEnterpriseEntity();
            String tokenKey = EncryptUtil.getTokenKey(request);
            Map<String, SessionObjectSet> sessionnObjectMap = ecosystemSession.getSessionnObjectSet();
            DynamicDataSourceUtil.setDataSource(enterpriseEntity.getEnte_sceo_url());
            String userId = JwtUtil.getUserId(request);
            //查询
            //1 查询员工信息
            OrmParam ormParamEmp = new OrmParam();
            ormParamEmp.addWhereParam("remp_epeo_obj", userId);
            String whereExpEmp = "remp_epeo_obj = #{whereParam.remp_epeo_obj}";
            ormParamEmp.setWhereExp(whereExpEmp);
            List<EmployeeEntity> employeeEntityList = ormService.selectBeanList(EmployeeEntity.class, ormParamEmp);
            //2 查询岗位列表
            if (employeeEntityList.size() > 0) {
                OrmParam ormParamJobs = new OrmParam();
                ormParamJobs.addWhereParam("rpos_emp", employeeEntityList.get(0).getId());
                String whereExpJobs = "rpos_emp = #{whereParam.rpos_emp}";
                ormParamJobs.setWhereExp(whereExpJobs);
                List<JobpositionEntity> jobpositionEntityList = ormService.selectBeanList(JobpositionEntity.class, ormParamJobs);
                CurrentStatus currentStatus = new CurrentStatus(jobpositionEntityList, new EmployeeEntity(), new JobpositionEntity(), enterpriseEntity, new DepttreeEntity());
                SessionObjectSet sessionObjectSet = new SessionObjectSet(new HashMap<String, Object>(), new Date());
                sessionnObjectMap.put(tokenKey, sessionObjectSet);
                ecosystemSession.setCurrentStatus(currentStatus);

                String redisTimeout[] = redisTimeoutSeconds.split(" ");
                RedisUtils.getInstance().setValue(tokenKey.getBytes(), SerializeUtils.serialize(ecosystemSession),
                        Integer.parseInt(redisTimeout[0]) * Integer.parseInt(redisTimeout[1]) * Integer.parseInt(redisTimeout[2]) * Integer.parseInt(redisTimeout[3]));
                logger.info("成功更新当前企业信息!");
            }

        } catch (Exception e) {
            logger.error("更新当前企业信息失败!");
        }
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result updateCurrentJobInfo(String enterpriseId, String jobId, ServletRequest request) {
        Result result = new Result();
        logger.info("开始更新当前岗位和员工信息!");
        //查询企业
        EnterpriseEntity enterpriseEntity = null;
        try {
            enterpriseEntity = queryEnterprise(enterpriseId);
        } catch (Exception e) {
            logger.error("查询当前企业信息出错!");
        }
        EcosystemSession ecosystemSession = SessionUtils.getSession(request);
        CurrentStatus currentStatus = ecosystemSession.getCurrentStatus();
        if (null == enterpriseEntity) {
            throw new RuntimeException("查询当前企业信息出错, 无法获取当前企业信息  : {} " + enterpriseId);
        }
        DynamicDataSourceUtil.setDataSource(enterpriseEntity.getEnte_sceo_url());
        try {
            //查询岗位信息
            JobpositionEntity jobpositionEntity = ormService.load(JobpositionEntity.class, jobId);
            if (null != jobpositionEntity) {
                currentStatus.setCurrentPosition(jobpositionEntity);
                //查询员工信息
                logger.info("update staff info start");
                EmployeeEntity employeeEntity = jobpositionEntity.loadRpos_emp();
                if (null != employeeEntity) {
                    currentStatus.setCurrentStaff(employeeEntity);
                }
                logger.info("update staff info end");
                //查询部门信息
                DepttreeEntity depttreeEntity = ormService.load(DepttreeEntity.class, jobpositionEntity.getRpos_dept());
                if (null != depttreeEntity) {
                    currentStatus.setDepttreeEntity(depttreeEntity);
                }
            }
            currentStatus.setCurrentEnterprise(enterpriseEntity);
            ecosystemSession.setCurrentStatus(null);
            ecosystemSession.setCurrentStatus(currentStatus);
            String tokenKey = EncryptUtil.getTokenKey(request);
            String redisTimeout[] = redisTimeoutSeconds.split(" ");
            RedisUtils.getInstance().setValue(tokenKey.getBytes(), SerializeUtils.serialize(ecosystemSession),
                    Integer.parseInt(redisTimeout[0]) * Integer.parseInt(redisTimeout[1]) * Integer.parseInt(redisTimeout[2]) * Integer.parseInt(redisTimeout[3]));
            result.setData(ecosystemSession);
            result.setRetCode(Result.RECODE_SUCCESS);
        } catch (Exception e) {
            logger.error("查找岗位和员工信息为空！" + e);
        }
        logger.info("成功更新当前岗位和员工信息!");
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result login(UserInfo userInfo) {
        Result result = new Result();
        try {
            if (StringUtil.isNullOrEmpty(userInfo.getPassword()) || StringUtil.isNullOrEmpty(userInfo.getPhone())) {
                result.setRetCode(Result.RECODE_VALIDATE_ERROR);
                result.setErrMsg("用户名或密码不正确！");
                return result;
            }
            String ps = EncryptUtil.encryptPassWord(userInfo.getPassword());
            userInfo.setPassword(ps);
            Result handler = telLogin.handler(userInfo);
            result = handler;
        } catch (Exception e) {
            logger.error("login error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("login error :" + e.getMessage());
        }
        return result;
    }

    public EnterpriseEntity queryEnterprise(String enterpriseId) throws Exception {
        DynamicDataSourceUtil.setDefaultDataSource();
        logger.info("开始查询企业信息！");
        return ormService.load(EnterpriseEntity.class, enterpriseId);
    }

}
