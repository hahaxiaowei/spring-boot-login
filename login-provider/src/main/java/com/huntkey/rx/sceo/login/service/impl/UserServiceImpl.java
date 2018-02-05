package com.huntkey.rx.sceo.login.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.commons.utils.uuid.UuidCreater;
import com.huntkey.rx.edm.entity.*;
import com.huntkey.rx.sceo.common.constant.Constant;
import com.huntkey.rx.sceo.common.entity.*;
import com.huntkey.rx.sceo.common.utils.DynamicDataSourceUtil;
import com.huntkey.rx.sceo.common.utils.EncryptUtil;
import com.huntkey.rx.sceo.common.utils.UserUtil;
import com.huntkey.rx.sceo.login.aop.DynamicDataSource;
import com.huntkey.rx.sceo.login.service.CommonService;
import com.huntkey.rx.sceo.login.service.RegisterService;
import com.huntkey.rx.sceo.login.service.UserService;
import com.huntkey.rx.sceo.orm.common.model.OrmParam;
import com.huntkey.rx.sceo.orm.service.OrmService;
import com.huntkey.rx.sceo.starter.util.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by lulx on 2017/11/22 0022 下午 16:29
 */
@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    OrmService ormService;

    @Autowired
    RegisterService registerService;

    @Autowired
    CommonService commonService;

    @Value("${nginxIp}")
    String nginxIp;

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result queryPeopleInfo(String userId) {
        Result result = new Result();
        try {
            PeopleEntity people = ormService.load(PeopleEntity.class, userId);
            if (StringUtil.isNullOrEmpty(people)) {
                result.setErrMsg("无法获取用户信息，错误的用户id ：" + userId);
                result.setRetCode(Result.RECODE_ERROR);
                return result;
            }
            processPeople(people);
            List<EpeoEpeoStuSetaEntity> epeoEpeoStuSetaEntities = people.loadEpeo_stu_set();
            List<EpeoEpeoWorkSetaEntity> epeoEpeoWorkSetaEntities = people.loadEpeo_work_set();
            PeopleVo peopleVo = new PeopleVo();
            peopleVo.setPeopleEntity(people);
            List<StuSetaVo> stuSetaVos = new ArrayList<StuSetaVo>();
            StuSetaVo stuSetaVo = null;
            // TODO 查询school表: school表本应在ecodb中，临时查edmdb
            DynamicDataSourceUtil.setDataSource(Constant.EDM_DATABASE_KEY);
            for (EpeoEpeoStuSetaEntity stuSet : epeoEpeoStuSetaEntities) {
                stuSetaVo = new StuSetaVo();
                stuSetaVo.setStuSetaEntity(stuSet);
                stuSetaVo.setSchoolEntity(stuSet.loadEpeo_rsch());
                stuSetaVos.add(stuSetaVo);
            }
            peopleVo.setEpeoEpeoStuSetaEntities(stuSetaVos);
            peopleVo.setEpeoEpeoWorkSetaEntities(epeoEpeoWorkSetaEntities);
            result.setData(peopleVo);
        } catch (Exception e) {
            logger.error("queryPeopleInfo error :" + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("queryPeopleInfo error :" + e.getMessage());
        }
        return result;
    }

    /**
     * 处理
     * 1. 图片路径添加文件服务器地址
     * 2. 身份证混淆
     * 3. 手机号混淆
     *
     * @param people
     */
    private void processPeople(PeopleEntity people) {
        if (StringUtil.isNullOrEmpty(people)) {
            return;
        }
        String epeoPhotourl = people.getEpeo_photourl();
        epeoPhotourl = StringUtil.isNullOrEmpty(epeoPhotourl) ? epeoPhotourl : nginxIp + epeoPhotourl;
        people.setEpeo_photourl(epeoPhotourl);
        String cardNo = people.getEpeo_card_no();
        if (!StringUtil.isNullOrEmpty(cardNo)) {
            people.setEpeo_card_no(cardNo.replaceAll("(\\d{4})\\d{0,10}(\\d{4})", "$1****$2"));
        }
        String tel = people.getEpeo_tel();
        if (!StringUtil.isNullOrEmpty(tel)) {
            people.setEpeo_tel(tel.replaceAll("(\\d{3})\\d{0,6}(\\d{3})", "$1****$2"));
        }
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result queryEnterpriseList(String userId) {
        Result result = new Result();
        try {
            PeopleEntity people = ormService.load(PeopleEntity.class, userId);
            if (StringUtil.isNullOrEmpty(people)) {
                result.setRetCode(Result.RECODE_ERROR);
                result.setErrMsg("无法查询到用户信息");
                return result;
            }
            processPeople(people);
            // 自然人企业信息
            List<EpeoEpeoEnteSetaEntity> epeoEpeoEnteSetaEntities = people.loadEpeo_ente_set();
            if (StringUtil.isNullOrEmpty(epeoEpeoEnteSetaEntities) || epeoEpeoEnteSetaEntities.size() == 0) {
                result.setRetCode(Result.RECODE_SUCCESS);
                result.setErrMsg("无法查询到用户的企业信息");
                return result;
            }
            List<EnterpriseVo> enterpriseVos = new ArrayList<EnterpriseVo>();
            for (EpeoEpeoEnteSetaEntity peopEpeoEnteSetaE : epeoEpeoEnteSetaEntities) {
                DynamicDataSourceUtil.setDefaultDataSource();
                EnterpriseEntity enterpriseEntity = peopEpeoEnteSetaE.loadEpeo_ente_obj();
                //转换数据源
                DynamicDataSourceUtil.setDataSource(enterpriseEntity.getEnte_sceo_url());
                //自然人员工信息
                OrmParam ormParam = new OrmParam();
                ormParam.addWhereParam("remp_epeo_obj", userId);
                String whereExp = "remp_epeo_obj = #{whereParam.remp_epeo_obj}";
                ormParam.setWhereExp(whereExp);
                List<EmployeeEntity> employeeList = ormService.selectBeanList(EmployeeEntity.class, ormParam);
                List<JobpositionVo> jobpositionVos = new ArrayList<JobpositionVo>();
                if (StringUtil.isNullOrEmpty(employeeList) || employeeList.size() == 0) {
                    EnterpriseVo enterpriseVo = new EnterpriseVo();
                    enterpriseVo.setEnterpriseEntity(enterpriseEntity);
                    enterpriseVo.setJobpositionVos(jobpositionVos);
                    enterpriseVos.add(enterpriseVo);
                    continue;
                }

                for (EmployeeEntity employeeE : employeeList) {
                    OrmParam ormParamJob = new OrmParam();
                    ormParamJob.addColumn("*");
                    ormParamJob.addWhereParam("rpos_emp", employeeE.getId());
                    String whereExpJob = "rpos_emp = #{whereParam.rpos_emp}";
                    ormParamJob.setWhereExp(whereExpJob);
                    List<JobpositionEntity> jobpositionList = ormService.selectBeanList(JobpositionEntity.class, ormParamJob);

                    for (JobpositionEntity jobpositionE : jobpositionList) {
                        jobpositionVos = new ArrayList<JobpositionVo>();
                        DepttreeEntity depttreeEntity = ormService.load(DepttreeEntity.class, jobpositionE.getRpos_dept());
                        JobpositionVo jobpositionVo = new JobpositionVo();
                        jobpositionVo.setDepttreeEntity(depttreeEntity);
                        jobpositionVo.setJobpositionEntity(jobpositionE);
                        jobpositionVos.add(jobpositionVo);
                    }
                }
                EnterpriseVo enterpriseVo = new EnterpriseVo();
                enterpriseVo.setEnterpriseEntity(enterpriseEntity);
                enterpriseVo.setJobpositionVos(jobpositionVos);
                enterpriseVos.add(enterpriseVo);
            }
            result.setData(enterpriseVos);
        } catch (Exception e) {
            logger.error("queryEnterpriseList error :" + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("queryEnterpriseList error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result updatePeopleInfo(PeopleEntity peopleEntity) {
        Result result = new Result();
        try {
            result = preCheckPeople(peopleEntity, result);
            if (!Result.RECODE_SUCCESS.equals(result.getRetCode())) {
                return result;
            }
            peopleEntity.setModuser(UserUtil.getUserId());
            int i = ormService.updateSelective(peopleEntity);
            result.setData(i);
        } catch (Exception e) {
            logger.error("updatePeopleInfo error :" + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("updatePeopleInfo error :" + e.getMessage());
        }
        return result;
    }

    /**
     * 数据校验
     * 锐信号、邮箱、手机号、身份证
     *
     * @param peopleEntity
     * @param result
     * @return
     */
    private Result preCheckPeople(PeopleEntity peopleEntity, Result result) {
        // TODO
        String[] strs = {"手机号", "邮箱", "身份证", "锐信号"};
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("epeo_tel", peopleEntity.getEpeo_tel());
        map.put("epeo_mail", peopleEntity.getEpeo_mail());
        map.put("epeo_card_no", peopleEntity.getEpeo_card_no());
        map.put("epeo_rxnbr", peopleEntity.getEpeo_rxnbr());
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            String str = strs[i++];
            logger.info("str : {} ; i : {}", str, i);
            Map.Entry<String, String> e = iterator.next();
            if (StringUtil.isNullOrEmpty(e.getValue())) {
                continue;
            }
            PeopleEntity peopleByKey = findPeopleByKey(e.getKey(), e.getValue());
            if (!StringUtil.isNullOrEmpty(peopleByKey) && !peopleEntity.getId().equals(peopleByKey.getId())) {
                result.setRetCode(Result.RECODE_ERROR);
                result.setErrMsg(str + "不能重复，请修改");
                return result;
            }
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result queryEnterprise(String enterpriseId, String userId) {
        Result result = new Result();
        if (StringUtil.isNullOrEmpty(enterpriseId) || StringUtil.isNullOrEmpty(userId)) {
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("无法获取企业ID或用户ID");
            return result;
        }
        try {
            // 企业信息
            EnterpriseEntity enterpriseEntity = ormService.load(EnterpriseEntity.class, enterpriseId);
            //转换数据源
            DynamicDataSourceUtil.setDataSource(enterpriseEntity.getEnte_sceo_url());

            //自然人员工信息
            OrmParam ormParam = new OrmParam();
            ormParam.addWhereParam("remp_epeo_obj", userId);
            String whereExp = "remp_epeo_obj = #{whereParam.remp_epeo_obj}";
            ormParam.setWhereExp(whereExp);
            List<EmployeeEntity> employeeList = ormService.selectBeanList(EmployeeEntity.class, ormParam);
            if (StringUtil.isNullOrEmpty(employeeList) || employeeList.size() == 0) {
                result.setRetCode(Result.RECODE_ERROR);
                result.setErrMsg("无法查询到用户的员工信息");
                return result;
            }
            //岗位信息
            List<JobpositionVo> jobpositionVos = new ArrayList<JobpositionVo>();
            for (EmployeeEntity employeeE : employeeList) {
                OrmParam ormParamJob = new OrmParam();
                ormParamJob.addColumn("*");
                ormParamJob.addWhereParam("rpos_emp", employeeE.getId());
                String whereExpJob = "rpos_emp = #{whereParam.rpos_emp}";
                ormParamJob.setWhereExp(whereExpJob);
                List<JobpositionEntity> jobpositionList = ormService.selectBeanList(JobpositionEntity.class, ormParamJob);
                for (JobpositionEntity jobpositionE : jobpositionList) {
                    DepttreeEntity depttreeEntity = ormService.load(DepttreeEntity.class, jobpositionE.getRpos_dept());
                    JobpositionVo jobpositionVo = new JobpositionVo();
                    jobpositionVo.setDepttreeEntity(depttreeEntity);
                    jobpositionVo.setJobpositionEntity(jobpositionE);
                    jobpositionVo.setEmployeeEntity(employeeE);
                    jobpositionVos.add(jobpositionVo);
                }
            }
            EnterpriseVo enterpriseVo = new EnterpriseVo();
            enterpriseVo.setEnterpriseEntity(enterpriseEntity);
            enterpriseVo.setJobpositionVos(jobpositionVos);
            result.setData(enterpriseVo);
        } catch (Exception e) {
            logger.error("queryEnterprise error :" + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("queryEnterprise error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result addPeople(PeopleEntity peopleEntity, String enterpriseId) {
        Result result = new Result();
        try {

            EnterpriseEntity enterpriseEntity = ormService.load(EnterpriseEntity.class, enterpriseId);
            if (StringUtil.isNullOrEmpty(enterpriseEntity)) {
                result.setRetCode(Result.RECODE_ERROR);
                result.setErrMsg("无法获取企业信息 enterpriseId ：" + enterpriseId);
                return result;
            }

            String tel = peopleEntity.getEpeo_tel();
            if (StringUtil.isNullOrEmpty(tel)) {
                result.setErrMsg("手机号不能为空");
                result.setRetCode(Result.RECODE_ERROR);
                return result;
            }

            OrmParam ormParam = new OrmParam();
            ormParam.addWhereParam("epeo_tel", tel);
            String whereExp = "epeo_tel = #{whereParam.epeo_tel}";
            ormParam.setWhereExp(whereExp);
            List<PeopleEntity> peopleEntities = ormService.selectBeanList(PeopleEntity.class, ormParam);
            if (!StringUtil.isNullOrEmpty(peopleEntities) && peopleEntities.size() > 0) {
                result.setErrMsg("使用此手机号用户已存在，请检查用户手机号 ：" + tel);
                result.setRetCode(Result.RECODE_ERROR);
                return result;
            }

            peopleEntity.setId(UuidCreater.uuid());
            peopleEntity.setEpeo_password(EncryptUtil.encryptPassWord(Constant.DEFAULT_USER_PASSWORD));
            String peopId = (String) ormService.insertSelective(peopleEntity);

            //EpeoEpeoEnteSetaEntity epeoEpeoEnteSetaEntity = new EpeoEpeoEnteSetaEntity();
            //epeoEpeoEnteSetaEntity.setPid(peopId);
            //epeoEpeoEnteSetaEntity.setClassName("people");
            //epeoEpeoEnteSetaEntity.setEpeo_ente_obj(enterpriseId);
            //String peopEpeoEnteSetaId = (String) ormService.insertSelective(epeoEpeoEnteSetaEntity);
            //logger.info("将用户Id和企业Id插入到关联表里面，返回的关联表ID为" + peopEpeoEnteSetaId);

            result.setData(peopId);
        } catch (Exception e) {
            logger.error("addPeople error :" + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("addPeople error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result queryPeopleInfoByIdCard(String idCard) {
        Result result = new Result();
        if (StringUtil.isNullOrEmpty(idCard)) {
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("身份证号不能为空");
            return result;
        }
        try {
            OrmParam ormParam = new OrmParam();
            ormParam.addWhereParam("epeo_card_no", idCard);
            String whereExp = "epeo_card_no = #{whereParam.epeo_card_no}";
            ormParam.setWhereExp(whereExp);
            List<PeopleEntity> peopleEntities = ormService.selectBeanList(PeopleEntity.class, ormParam);
            if (StringUtil.isNullOrEmpty(peopleEntities) || peopleEntities.size() == 0) {
                return result;
            }
            PeopleEntity people = peopleEntities.get(0);
            processPeople(people);
            List<EpeoEpeoSkillSetaEntity> epeoEpeoSkillSetaEntities = people.loadEpeo_skill_set();
            List<EpeoEpeoCardSetaEntity> epeoEpeoCardSetaEntities = people.loadEpeo_card_set();
            List<EpeoEpeoFamSetaEntity> epeoEpeoFamSetaEntities = people.loadEpeo_fam_set();
            List<EpeoEpeoAjobSetaEntity> epeoEpeoAjobSetaEntities = people.loadEpeo_ajob_set();
            List<EpeoEpeoStuSetaEntity> epeoEpeoStuSetaEntities = people.loadEpeo_stu_set();
            List<EpeoEpeoWorkSetaEntity> epeoEpeoWorkSetaEntities = people.loadEpeo_work_set();
            List<EpeoEpeoEnteSetaEntity> epeoEpeoEnteSetaEntities = people.loadEpeo_ente_set();
            PeopleVo peopleVo = new PeopleVo();
            peopleVo.setPeopleEntity(people);
            peopleVo.setEpeoEpeoSkillSetaEntities(epeoEpeoSkillSetaEntities);
            peopleVo.setEpeoEpeoCardSetaEntities(epeoEpeoCardSetaEntities);
            peopleVo.setEpeoEpeoFamSetaEntities(epeoEpeoFamSetaEntities);
            peopleVo.setEpeoEpeoAjobSetaEntities(epeoEpeoAjobSetaEntities);
            List<StuSetaVo> stuSetaVos = new ArrayList<StuSetaVo>();
            StuSetaVo stuSetaVo = null;
            // TODO 查询school表: school表本应在ecodb中，临时查edmdb
            DynamicDataSourceUtil.setDataSource(Constant.EDM_DATABASE_KEY);
            for (EpeoEpeoStuSetaEntity stuSet : epeoEpeoStuSetaEntities) {
                stuSetaVo = new StuSetaVo();
                stuSetaVo.setStuSetaEntity(stuSet);
                stuSetaVo.setSchoolEntity(stuSet.loadEpeo_rsch());
                stuSetaVos.add(stuSetaVo);
            }
            peopleVo.setEpeoEpeoStuSetaEntities(stuSetaVos);
            peopleVo.setEpeoEpeoWorkSetaEntities(epeoEpeoWorkSetaEntities);
            peopleVo.setEpeoEpeoEnteSetaEntities(epeoEpeoEnteSetaEntities);
            result.setData(peopleVo);
        } catch (Exception e) {
            logger.error("queryPeopleInfoByTel error :" + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("queryPeopleInfoByTel error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result removePeopleEnteSet(String userId, String enterpriseId) {
        Result result = new Result();
        try {
            PeopleEntity peopleEntity = ormService.load(PeopleEntity.class, userId);
            if (StringUtil.isNullOrEmpty(peopleEntity)) {
                result.setRetCode(Result.RECODE_ERROR);
                result.setErrMsg("无法获取当前用户 userId: " + userId);
                return result;
            }

            EnterpriseEntity enterpriseEntity = ormService.load(EnterpriseEntity.class, enterpriseId);
            if (StringUtil.isNullOrEmpty(enterpriseEntity)) {
                result.setRetCode(Result.RECODE_ERROR);
                result.setErrMsg("无法获取企业信息 enterpriseId ：" + enterpriseId);
                return result;
            }

            OrmParam param = new OrmParam();
            param.setWhereExp(OrmParam.and(param.getEqualXML("pid", userId),
                    param.getEqualXML("epeo_ente_obj", enterpriseId)));
            result.setData(ormService.delete(EpeoEpeoEnteSetaEntity.class, param));
        } catch (Exception e) {
            logger.error("removePeopleEnteSet error :" + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("removePeopleEnteSet error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result addStuSet(EpeoEpeoStuSetaEntity stuSet) {
        Result result = new Result();
        try {
            stuSet.setPid(UserUtil.getUserId());
            stuSet.setClassName("people");
            result.setData(ormService.insertSelective(stuSet));
        } catch (Exception e) {
            logger.error("addStuSet error :" + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("addStuSet error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result delStuSet(String stuSetId) {
        Result result = new Result();
        try {
            result.setData(ormService.delete(EpeoEpeoStuSetaEntity.class, stuSetId));
        } catch (Exception e) {
            logger.error("delStuSet error :" + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("delStuSet error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result addWorkSet(EpeoEpeoWorkSetaEntity workSet) {
        Result result = new Result();
        try {
            workSet.setPid(UserUtil.getUserId());
            workSet.setClassName("people");
            result.setData(ormService.insertSelective(workSet));
        } catch (Exception e) {
            logger.error("addWorkSet error :" + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("addWorkSet error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result delWorkSet(String workSetId) {
        Result result = new Result();
        try {
            result.setData(ormService.delete(EpeoEpeoWorkSetaEntity.class, workSetId));
        } catch (Exception e) {
            logger.error("delWorkSet error :" + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("delWorkSet error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result modWorkSet(EpeoEpeoWorkSetaEntity workSet) {
        Result result = new Result();
        try {
            result.setData(ormService.updateSelective(workSet));
        } catch (Exception e) {
            logger.error("modWorkSet error :" + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("modWorkSet error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public PeopleEntity findPeopleByKey(String key, String val) {
        try {
            OrmParam ormParam = new OrmParam();
            List<String> str = new ArrayList<String>();
            if (StringUtil.isNullOrEmpty(key) || StringUtil.isNullOrEmpty(val)) {
                return null;
            }
            str.add(ormParam.getEqualXML(key, val));
            String whereExp = OrmParam.and(str);
            ormParam.setWhereExp(whereExp);
            List<PeopleEntity> peopleEntities = ormService.selectBeanList(PeopleEntity.class, ormParam);
            if (StringUtil.isNullOrEmpty(peopleEntities) || peopleEntities.size() == 0) {
                return null;
            }
            PeopleEntity peopleEntity = peopleEntities.get(0);
            processPeople(peopleEntity);
            return peopleEntity;
        } catch (Exception e) {
            logger.error("findPeopleByKey error :" + e.getMessage(), e);
        }
        return null;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result modStuSet(EpeoEpeoStuSetaEntity stuSet) {
        Result result = new Result();
        try {
            result.setData(ormService.updateSelective(stuSet));
        } catch (Exception e) {
            logger.error("modStuSet error :" + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("modStuSet error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result addPeopleEnteSet(String userId, String enterpriseId) {
        Result result = new Result();
        try {
            PeopleEntity peopleEntity = ormService.load(PeopleEntity.class, userId);
            if (StringUtil.isNullOrEmpty(peopleEntity)) {
                result.setRetCode(Result.RECODE_ERROR);
                result.setErrMsg("无法获取当前用户");
                return result;
            }

            EnterpriseEntity enterpriseEntity = ormService.load(EnterpriseEntity.class, enterpriseId);
            if (StringUtil.isNullOrEmpty(enterpriseEntity)) {
                result.setRetCode(Result.RECODE_ERROR);
                result.setErrMsg("无法获取企业信息 enterpriseId ：" + enterpriseId);
                return result;
            }

            EpeoEpeoEnteSetaEntity epeoEpeoEnteSetaEntity = new EpeoEpeoEnteSetaEntity();
            epeoEpeoEnteSetaEntity.setPid(userId);
            epeoEpeoEnteSetaEntity.setClassName("people");
            epeoEpeoEnteSetaEntity.setEpeo_ente_obj(enterpriseId);
            String peopEpeoEnteSetaId = (String) ormService.insertSelective(epeoEpeoEnteSetaEntity);
            logger.info("将用户Id和企业Id插入到关联表里面，返回的关联表ID为" + peopEpeoEnteSetaId);

            result.setData(peopEpeoEnteSetaId);
        } catch (Exception e) {
            logger.error("addPeopleEnteSet error :" + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("addPeopleEnteSet error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result delFamSet(String famSetId) {
        Result result = new Result();
        try {
            result.setData(ormService.delete(EpeoEpeoFamSetaEntity.class, famSetId));
        } catch (Exception e) {
            logger.error("delFamSet error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("delFamSet error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result getFamSet(String famSetId) {
        Result result = new Result();
        try {
            result.setData(ormService.load(EpeoEpeoFamSetaEntity.class, famSetId));
        } catch (Exception e) {
            logger.error("getFamSet error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("getFamSet error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result modFamSet(EpeoEpeoFamSetaEntity famSet) {
        Result result = new Result();
        try {
            famSet.setModuser(UserUtil.getUserId());
            result.setData(ormService.updateSelective(famSet));
        } catch (Exception e) {
            logger.error("modFamSet error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("modFamSet error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result addFamSet(EpeoEpeoFamSetaEntity famSet) {
        Result result = new Result();
        try {
            famSet.setCreuser(UserUtil.getUserId());
            famSet.setPid(UserUtil.getUserId());
            famSet.setClassName("people");
            result.setData(ormService.insertSelective(famSet));
        } catch (Exception e) {
            logger.error("addFamSet error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("addFamSet error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result getStuSetList() {
        Result result = new Result();
        try {
            PeopleEntity peopleEntity = ormService.load(PeopleEntity.class, UserUtil.getUserId());
            List<EpeoEpeoStuSetaEntity> epeoEpeoStuSetaEntities = peopleEntity.loadEpeo_stu_set();
            result.setData(epeoEpeoStuSetaEntities);
        } catch (Exception e) {
            logger.error("getStuSetList error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("getStuSetList error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result getWorkSetList() {
        Result result = new Result();
        try {
            PeopleEntity peopleEntity = ormService.load(PeopleEntity.class, UserUtil.getUserId());
            List<EpeoEpeoWorkSetaEntity> workSetaEntities = peopleEntity.loadEpeo_work_set();
            result.setData(workSetaEntities);
        } catch (Exception e) {
            logger.error("getWorkSetList error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("getWorkSetList error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result modTel(UserInfo userInfo) {
        Result result = new Result();
        try {
            if (StringUtil.isNullOrEmpty(userInfo.getEpeoTel())) {
                result.setErrMsg("新手机号不能为空");
                result.setRetCode(Result.RECODE_ERROR);
                return result;
            }
            Boolean verificatCorrect = registerService.isVerificatCorrect(userInfo.getVerificatCode(), userInfo.getEpeoTel());
            if (!verificatCorrect) {
                result.setRetCode(Result.RECODE_ERROR);
                result.setErrMsg("验证码输入错误或输入已超时");
                return result;
            }
            PeopleEntity peopleEntity = ormService.load(PeopleEntity.class, UserUtil.getUserId());
            String newPw = EncryptUtil.encryptPassWord(userInfo.getPassword());
            if (!peopleEntity.getEpeo_password().equals(newPw)) {
                result.setErrMsg("密码输入错误");
                result.setRetCode(Result.RECODE_ERROR);
                return result;
            }
            if (!peopleEntity.getEpeo_card_no().equals(userInfo.getEpeoCardNo())) {
                result.setErrMsg("身份证输入错误");
                result.setRetCode(Result.RECODE_ERROR);
                return result;
            }
            peopleEntity.setModuser(UserUtil.getUserId());
            peopleEntity.setEpeo_tel(userInfo.getEpeoTel());
            result.setData(ormService.updateSelective(peopleEntity));
        } catch (Exception e) {
            logger.error("modTel error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("modTel error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result modPassWord(UserInfo userInfo) {
        Result result = new Result();
        try {
            PeopleEntity peopleEntity = ormService.load(PeopleEntity.class, UserUtil.getUserId());
            if (!peopleEntity.getEpeo_password().equals(EncryptUtil.encryptPassWord(userInfo.getPassword()))) {
                result.setErrMsg("原密码输入错误");
                result.setRetCode(Result.RECODE_ERROR);
                return result;
            }
            if (StringUtil.isNullOrEmpty(userInfo.getNewPassWord())) {
                result.setErrMsg("新密码不能为空");
                result.setRetCode(Result.RECODE_ERROR);
                return result;
            }
            peopleEntity.setModuser(UserUtil.getUserId());
            peopleEntity.setEpeo_password(EncryptUtil.encryptPassWord(userInfo.getNewPassWord()));
            result.setData(ormService.updateSelective(peopleEntity));
        } catch (Exception e) {
            logger.error("modPassWord error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("modPassWord error :" + e.getMessage());
        }
        return result;
    }

    @Override
    public Result mailCode(String mail) {
        Result result = new Result();
        try {
            if(StringUtil.isNullOrEmpty(mail)){
                result.setErrMsg("邮箱不能为空");
                result.setRetCode(Result.RECODE_ERROR);
                return result;
            }
            EmailVo emailVo = new EmailVo();
            List<String> list = new ArrayList<String>();
            list.add(mail);
            emailVo.setMailRecipient(list);
            emailVo.setMailSubject("【嘉源锐信】邮箱修改验证码");
            int randNum = new Random().nextInt(1000000);
            RedisUtils.getInstance().setValue(Constant.LOGIN_AUTHENTICATION_ + mail, String.valueOf(randNum), 60 * 60);
            emailVo.setMailContent("您此次邮箱修改验证码为：" + randNum + ", 有效时间为60分钟，请妥善保管。");
            Result sendEmail = commonService.sendEmail(emailVo);
            logger.info("邮件发送：{}", JSONObject.toJSONString(sendEmail));
            return sendEmail;
        } catch (Exception e) {
            logger.error("mailCode error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("mailCode error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result getFamSetList() {
        Result result = new Result();
        try {
            PeopleEntity peopleEntity = ormService.load(PeopleEntity.class, UserUtil.getUserId());
            List<EpeoEpeoFamSetaEntity> epeoEpeoFamSetaEntities = peopleEntity.loadEpeo_fam_set();
            result.setData(epeoEpeoFamSetaEntities);
        } catch (Exception e) {
            logger.error("getFamSetList error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("getFamSetList error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result modMail(UserInfo userInfo) {
        Result result = new Result();
        try {
            if (StringUtil.isNullOrEmpty(userInfo.getEpeoMail())) {
                result.setErrMsg("新邮箱不能为空");
                result.setRetCode(Result.RECODE_ERROR);
                return result;
            }
            String code = RedisUtils.getInstance().getValue(Constant.LOGIN_AUTHENTICATION_ + userInfo.getEpeoMail());

            if (StringUtil.isNullOrEmpty(code) || !code.equals(userInfo.getVerificatCode())) {
                result.setRetCode(Result.RECODE_ERROR);
                result.setErrMsg("验证码输入错误或输入已超时");
                return result;
            }
            PeopleEntity peopleEntity = ormService.load(PeopleEntity.class, UserUtil.getUserId());
            String newPw = EncryptUtil.encryptPassWord(userInfo.getPassword());
            if (!peopleEntity.getEpeo_password().equals(newPw)) {
                result.setErrMsg("密码输入错误");
                result.setRetCode(Result.RECODE_ERROR);
                return result;
            }
            if (!peopleEntity.getEpeo_card_no().equals(userInfo.getEpeoCardNo())) {
                result.setErrMsg("身份证输入错误");
                result.setRetCode(Result.RECODE_ERROR);
                return result;
            }
            peopleEntity.setModuser(UserUtil.getUserId());
            peopleEntity.setEpeo_mail(userInfo.getEpeoMail());
            result.setData(ormService.updateSelective(peopleEntity));
        } catch (Exception e) {
            logger.error("modMail error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("modMail error :" + e.getMessage());
        }
        return result;
    }
}
