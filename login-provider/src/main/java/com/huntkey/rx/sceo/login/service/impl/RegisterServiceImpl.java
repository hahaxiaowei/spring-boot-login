package com.huntkey.rx.sceo.login.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.commons.utils.uuid.UuidCreater;
import com.huntkey.rx.edm.entity.*;
import com.huntkey.rx.sceo.common.constant.Constant;
import com.huntkey.rx.sceo.common.entity.EnterpriseVo;
import com.huntkey.rx.sceo.common.utils.BusinessUtils;
import com.huntkey.rx.sceo.common.utils.DynamicDataSourceUtil;
import com.huntkey.rx.sceo.common.utils.EncryptUtil;
import com.huntkey.rx.sceo.common.utils.StringUtils;
import com.huntkey.rx.sceo.login.aop.DynamicDataSource;
import com.huntkey.rx.sceo.login.feign.BizModelerService;
import com.huntkey.rx.sceo.login.service.CommonService;
import com.huntkey.rx.sceo.login.service.RegisterService;
import com.huntkey.rx.sceo.method.register.plugin.entity.ParamsVo;
import com.huntkey.rx.sceo.method.register.plugin.util.ExecUtil;
import com.huntkey.rx.sceo.orm.common.model.DataSourceEntity;
import com.huntkey.rx.sceo.orm.common.model.OrmParam;
import com.huntkey.rx.sceo.orm.service.DataSourceService;
import com.huntkey.rx.sceo.orm.service.OrmService;
import com.huntkey.rx.sceo.starter.util.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sunwei on 2017/11/23
 */
@Service
public class RegisterServiceImpl implements RegisterService {
    Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);

    @Autowired
    private OrmService ormService;

    @Autowired
    BizModelerService bizModelerService;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private CommonService commonService;

    @Value("${baseDb.ip}")
    String baseDbIp;

    @Value("${baseDb.port}")
    String baseDbPort;

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result addUserInfo(PeopleEntity peopleEntity) {
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        try {
            peopleEntity.setId(UuidCreater.uuid());
            peopleEntity.setCreuser("admin");
            peopleEntity.setCreuser("admin");
            Date date = new Date();
            peopleEntity.setModtime(date);
            peopleEntity.setCretime(date);
            peopleEntity.setEpeo_password(EncryptUtil.encryptPassWord(peopleEntity.getEpeo_password()));
            String id = (String) ormService.insert(peopleEntity);
            result.setData(id);
            logger.info("插入自然人基本属性");
        } catch (Exception e) {
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("执行出错");
            logger.error("addUserInfo方法执行异常" + e.getMessage() + e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * @param verificationcode
     * @param epeo_Tel
     * @return java.lang.Boolean
     * @description 校验验证码：验证码正确返回true，验证码错误返回false
     * @method isVerificatCorrect
     */
    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Boolean isVerificatCorrect(String verificationcode, String epeo_Tel) {

        Boolean tag = false;
        try {
            String value = RedisUtils.getInstance().getValue(Constant.LOGIN_AUTHENTICATION_ + epeo_Tel);
            logger.info("查到数据为" + value);
            if (StringUtil.isNullOrEmpty(value)) {
                return tag;
            }
            Map<String, Object> mapRedis = JSON.parseObject(value, Map.class);
            String code = String.valueOf(mapRedis.get("verificatCode"));
            logger.info("redis里面存放到验证码为：" + code);
            if (verificationcode.equals(code)) {
                tag = true;
            }
        } catch (Exception e) {
            logger.error("isVerificatCorrect方法执行异常" + e.getMessage() + e);
            throw new RuntimeException(e);
        }
        return tag;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result getVerificatCode(String phoneNum) {
        Result result = new Result();
        if (StringUtil.isNullOrEmpty(phoneNum)) {
            result.setErrMsg("请填写手机号码！");
            result.setRetCode(Result.RECODE_ERROR);
            return result;
        }
        Result selectPeopleByTelPhone = selectPeopleByTelPhone(phoneNum, "");
        if (StringUtil.isNullOrEmpty(selectPeopleByTelPhone) || !Result.RECODE_SUCCESS.equals(selectPeopleByTelPhone.getRetCode())) {
            return selectPeopleByTelPhone;
        }
        Boolean flag = (Boolean) selectPeopleByTelPhone.getData();
        if (flag) {
            logger.info("手机号已存在");
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("手机号已存在");
            return result;
        }
        try {
            //返回验证码和时间
            Map<String, Object> codeMap = BusinessUtils.getValidVerificatCode(phoneNum);
            Object code = codeMap.get("verificatCode");
            Result sendMsg = commonService.sendMsg(phoneNum, "您此次注册验证码为：" + code + ", 请妥善保管。");
            logger.info("验证码发送结果： {}", sendMsg.toString());
            codeMap.put("verificatCode", "");
            result.setData(codeMap);
            result.setRetCode(Result.RECODE_SUCCESS);
            return result;
        } catch (Exception e) {
            logger.error("方法执行异常" + e.getMessage() + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @param epeoTel
     * @param peopleId
     * @return com.huntkey.rx.edm.entity.PeopleEntity
     * @description 根据用户手机号查询用户信息查到返回true, 否则返回false
     * @method selectPeopleByTelPhone
     */
    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result selectPeopleByTelPhone(String epeoTel, String peopleId) {
        Result result = new Result();
        try {
            if (StringUtil.isNullOrEmpty(epeoTel)) {
                result.setRetCode(Result.RECODE_ERROR);
                result.setErrMsg("手机号不能为空");
                return result;
            }
            OrmParam ormParam = new OrmParam();
            List<String> params = new ArrayList<String>();
            params.add(ormParam.getEqualXML("epeo_tel", epeoTel));
            if (!StringUtil.isNullOrEmpty(peopleId)) {
                params.add(ormParam.getUnequalXML("id", peopleId));
            }
            ormParam.setWhereExp(OrmParam.and(params));
            Boolean tag = true;

            List<PeopleEntity> list = ormService.selectBeanList(PeopleEntity.class, ormParam);
            if (StringUtil.isNullOrEmpty(list) || list.size() == 0) {
                tag = false;
            }
            result.setData(tag);
        } catch (Exception e) {
            logger.error("selectPeopleByTelPhone方法执行异常异常" + e.getMessage() + e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * @param enterpriseVo
     * @return void
     * @description 添加企业信息
     * @method addEnterPrise
     */
    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result addEnterPrise(EnterpriseVo enterpriseVo, String auth) {
        Result result = new Result();
        try {
            EnterpriseEntity enterpriseEntity = enterpriseVo.getEnterpriseEntity();
            String userId = enterpriseVo.getUserId();

            Boolean tag = isOrgCodeOrFullNameOrSceoUrlExist(enterpriseEntity.getEnte_org_code(), enterpriseEntity.getEnte_name_cn(), enterpriseEntity.getEnte_sceo_url());
            logger.info("企业组织机构代码和企业全称和企业Sceo_Url的唯一性判断条件" + tag);
            if (isOrgCodeOrFullNameOrSceoUrlExist(enterpriseEntity.getEnte_org_code(), enterpriseEntity.getEnte_name_cn(), enterpriseEntity.getEnte_sceo_url())) {
                logger.info("企业全称或者企业组织机构代码或者Sceo_url已存在");
                result.setRetCode(Result.RECODE_ERROR);
                result.setErrMsg("企业全称或者企业组织机构代码或者Sceo_url已存在");
                return result;
            }

            Date date = new Date();
            enterpriseEntity.setId(UuidCreater.uuid());
            enterpriseEntity.setModuser(userId);
            enterpriseEntity.setCreuser(userId);
            enterpriseEntity.setModtime(date);
            enterpriseEntity.setCretime(date);
            enterpriseEntity.setEnte_dbuser(enterpriseEntity.getEnte_sceo_url());
            enterpriseEntity.setEnte_dbpassword(EncryptUtil.encryptPassWord(enterpriseEntity.getEnte_dbpassword()));
            enterpriseEntity.setEnte_dbaddress(baseDbIp + ":" + baseDbPort);
            String enterpriseId = (String) ormService.insert(enterpriseEntity);
            logger.info("插入企业基本属性，返回的企业ID为：" + enterpriseId);
            // 1. 新建用户与企业的关联关系 插入
            EpeoEpeoEnteSetaEntity epeoEpeoEnteSetaEntity = new EpeoEpeoEnteSetaEntity();
            epeoEpeoEnteSetaEntity.setPid(userId);
            epeoEpeoEnteSetaEntity.setClassName("people");
            epeoEpeoEnteSetaEntity.setEpeo_ente_obj(enterpriseId);
            String peopEpeoEnteSetaId = (String) ormService.insertSelective(epeoEpeoEnteSetaEntity);
            logger.info("将用户Id和企业Id插入到关联表里面，返回的关联表ID为" + peopEpeoEnteSetaId);
            result.setData(enterpriseId);
            // TODO
            //initEnterpriseBaseInfo(userId, enterpriseEntity);

            if (StringUtil.isNullOrEmpty(enterpriseEntity.getEnte_sceo_url())) {
                result.setRetCode(Result.RECODE_ERROR);
                result.setErrMsg("企业URl不能为空");
                return result;
            }
            String dbName = StringUtils.getEnterpriseDbName(enterpriseEntity.getEnte_sceo_url());

            //创建数据库 TODO
            Map<String, String> pathMsg = new HashMap<String, String>();
            pathMsg.put("dataBase", dbName);
            pathMsg.put("name", enterpriseEntity.getEnte_sceo_url());
            pathMsg.put("dbPassword", enterpriseEntity.getEnte_dbpassword());
            Long start = System.currentTimeMillis();
            logger.info("数据库创建：pathMsg : {}", pathMsg.toString());
            Result dbPassiveRes = bizModelerService.dbPassive(Constant.DEFAULT_EDM_DB_CREATE_VERSION, pathMsg);
            logger.info("数据库创建：pathMsg : {} ; daPassiveRes : {} ", pathMsg.toString(), dbPassiveRes.toString());
            logger.info("数据库创建耗时：{}", System.currentTimeMillis() - start);
            Result predbPassiveRes = StringUtils.preHandleResult(dbPassiveRes, "调用modeler创建数据库");
            logger.info("数据库创建：predbPassiveRes : {}", predbPassiveRes.toString());

            if (!Result.RECODE_SUCCESS.equals(predbPassiveRes.getRetCode())) {
                return predbPassiveRes;
            }

            // 动态添加数据源 TODO
            DataSourceEntity dataSourceEntity = new DataSourceEntity();
            dataSourceEntity.setDbName(dbName);
            dataSourceEntity.setUsername(enterpriseEntity.getEnte_sceo_url());
            dataSourceEntity.setPassword(enterpriseEntity.getEnte_dbpassword());
            dataSourceEntity.setUrl(baseDbIp);
            dataSourceEntity.setPort(baseDbPort);
            logger.info("动态数据源添加： dataSource : {} ", JSONObject.toJSONString(dataSourceEntity));
            dataSourceService.addDynamicDataSource(dataSourceEntity);
            logger.info("动态数据源添加完成 : {}", DynamicDataSourceUtil.getDataSourceIds());

            // 初始化企业数据 TODO
            ParamsVo params = new ParamsVo();
            params.setAuthorization(auth);
            params.setClassName("enterprise");
            params.setMethodName("initEnterPriseInfo");
            Map<String, String> param = new HashMap<String, String>();
            param.put("enterpriseId", enterpriseId);
            params.setParamObj(param);
            logger.info("初始化企业信息 params : {}", params.toString());
            Result execResult = ExecUtil.exec(params);
            logger.info("初始化企业信息 result ： {}", execResult.toString());

            Result preExecResult = StringUtils.preHandleResult(execResult, "企业信息初始化");
            logger.info("企业信息初始化：preExecResult : {}", preExecResult.toString());

            if (!Result.RECODE_SUCCESS.equals(preExecResult.getRetCode())) {
                return preExecResult;
            }

        } catch (Exception e) {
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("执行addEnterPrise出错");
            logger.error("addEnterPrise方法执行错误" + e.getMessage() + e);
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result initEnterPriseInfo(String userId, String enterpriseId) {
        logger.info("exec initEnterPriseInfo method");
        Result result = new Result();
        try {
            EnterpriseEntity enterpriseEntity = ormService.load(EnterpriseEntity.class, enterpriseId);
            logger.info("userId : {} ; enterpriseEntity : {}", userId, JSONObject.toJSONString(enterpriseEntity));
            initEnterpriseBaseInfo(userId, enterpriseEntity);
        } catch (Exception e) {
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("执行initEnterPriseInfo出错");
            logger.error("initEnterPriseInfo方法执行错误" + e.getMessage(), e);
            throw new RuntimeException("initEnterPriseInfo方法执行错误" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * @param organizationCode 企业组织机构代码
     * @param enteNameCn       企业全称
     * @param enteSceoUrl      企业Sco_Url
     * @return java.lang.Boolean
     * @description 判断企业组织机构代码、企业全称、企业Sceo_Url是否已存在，如果已存则返回true，否则返回false
     * @method isOrgCodeOrFullNameOrSceoUrlExist
     */
    @DynamicDataSource(isDefaultSource = true)
    private Boolean isOrgCodeOrFullNameOrSceoUrlExist(String organizationCode, String enteNameCn, String enteSceoUrl) {
        OrmParam ormParam = new OrmParam();
        String whereExp = OrmParam.or(ormParam.getEqualXML("ente_org_code", organizationCode), ormParam.getEqualXML("ente_name_cn", enteNameCn), ormParam.getEqualXML("ente_sceo_url", enteSceoUrl));
        ormParam.setWhereExp(whereExp);
        Boolean tag = true;
        try {
            List<EnterpriseEntity> list = ormService.selectBeanList(EnterpriseEntity.class, ormParam);
            if (StringUtil.isNullOrEmpty(list) || list.size() == 0) {
                tag = false;
            }
        } catch (Exception e) {
            logger.error("isOrgCodeOrFullNameOrSceoUrlExist方法执行异常" + e.getMessage() + e);
            throw new RuntimeException(e);
        }
        return tag;
    }

    /**
     * @param userId
     * @param enterprise
     * @return void
     * @description 企业初始化信息：关联表的部门信息、员工信息、岗位信息
     * @method initEnterpriseBaseInfo
     */
    private void initEnterpriseBaseInfo(String userId, EnterpriseEntity enterprise) {
        try {
            String jobpositionEntityId = UuidCreater.uuid();
            String employeeId = UuidCreater.uuid();
            // 1.新建一个默认部门 关联公司 插入
            String deptteeID = createDepttreeEntity(userId, employeeId, enterprise, jobpositionEntityId);
            //2. 新建岗位 关联岗位 插入
            String jobpositionId = createJobpositionEntity(deptteeID, userId, employeeId, enterprise, jobpositionEntityId);
            //3. 新建员工  关联自然人 插入
            createEmployee(userId, employeeId, enterprise, jobpositionId, deptteeID);

        } catch (Exception e) {
            logger.error("方法执行异常" + e.getMessage() + e);
            throw new RuntimeException(e);
        }
    }

    private String createJobpositionEntity(String deptteeID, String userId, String employeeId, EnterpriseEntity enterprise, String jobpositionEntityId) throws Exception {
        DynamicDataSourceUtil.setDataSource(enterprise.getEnte_sceo_url());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Map<String, Object> params = new HashMap<String, Object>();
        //岗位表对象
        JobpositionEntity jobpositionEntity = new JobpositionEntity();
        //岗位历史记录表对象
        RposRposChagSetaEntity rposRposChagSetaEntity = new RposRposChagSetaEntity();
        //从职位定义类里面查询岗级、职位、职位名称、职位类别等等属性
        OrmParam ormParam = new OrmParam();
        ormParam.addWhereParam("rpof_grade", "01");
        String whereExp = "rpof_grade = #{whereParam.rpof_grade}";
        ormParam.setWhereExp(whereExp);
        List<PositiondefinitionEntity> positiondefinitionEntities = ormService.selectBeanList(PositiondefinitionEntity.class, ormParam);
        PositiondefinitionEntity positiondefinitionEntity;
        if (StringUtil.isNullOrEmpty(positiondefinitionEntities) || positiondefinitionEntities.size() == 0) {
            positiondefinitionEntity = new PositiondefinitionEntity();
            positiondefinitionEntity.setRpof_type("10");
            positiondefinitionEntity.setRpof_prop("1");
            params.put("edmnEncode", "P");
            params.put("edmnType", "3");
            Result jobpositionResult = bizModelerService.numbers(params);
            if (StringUtil.isNullOrEmpty(jobpositionResult) || !Result.RECODE_SUCCESS.equals(jobpositionResult.getRetCode())) {
                throw new RuntimeException("调用modeler获取岗位编码错误; params ; {} " + params.toString());
            }
            positiondefinitionEntity.setRpof_code(String.valueOf(jobpositionResult.getData()));
            positiondefinitionEntity.setRpof_name("董事长");
            positiondefinitionEntity.setRpof_grade("01");
            positiondefinitionEntity.setRpof_duty("公司的创始人之一，兼顾公司的决策，规划公司的发展方向和经营目标，是一个公司的最高执行人");
            positiondefinitionEntity.setCretime(date);
            positiondefinitionEntity.setCreuser(userId);
            positiondefinitionEntity.setModtime(date);
            positiondefinitionEntity.setModuser(userId);
            positiondefinitionEntity.setEdmd_ente(enterprise.getId());
            positiondefinitionEntity.setEdmd_class("positiondefinition");
            String positiondefinitionEntityId = (String) ormService.insert(positiondefinitionEntity);
            logger.info("插入 职位定义类，插入后返回的Id为：" + positiondefinitionEntityId);
        } else {
            positiondefinitionEntity = positiondefinitionEntities.get(0);
        }
        jobpositionEntity.setId(jobpositionEntityId);
        //任职人：关联员工类
        jobpositionEntity.setRpos_emp(employeeId);
        jobpositionEntity.setRpos_dept(deptteeID);
        jobpositionEntity.setRpos_name(positiondefinitionEntity.getRpof_name());
        jobpositionEntity.setRpos_duty(positiondefinitionEntity.getRpof_duty());
        jobpositionEntity.setRpos_rpof(positiondefinitionEntity.getId());
        jobpositionEntity.setRpos_beg(date);
        jobpositionEntity.setRpos_end(dateFormat.parse("9999-12-30 00:00:00"));
        //所属类
        jobpositionEntity.setEdmd_class("jobposition");
        //企业对象
        jobpositionEntity.setEdmd_ente(enterprise.getId());
        jobpositionEntity.setCretime(date);
        jobpositionEntity.setCreuser(userId);
        jobpositionEntity.setModtime(date);
        jobpositionEntity.setModuser(userId);
        jobpositionEntity.setRpos_code(positiondefinitionEntity.getRpof_code());

        //岗位记录表插入相同属性
        rposRposChagSetaEntity.setPid(jobpositionEntityId);
        rposRposChagSetaEntity.setClassName("jobposition");
        rposRposChagSetaEntity.setRpos_beg_his(date);
        rposRposChagSetaEntity.setRpos_end_his(dateFormat.parse("9999-12-30 00:00:00"));
        rposRposChagSetaEntity.setRpos_name_his(positiondefinitionEntity.getRpof_name());
        rposRposChagSetaEntity.setRpos_duty_his(positiondefinitionEntity.getRpof_duty());
        rposRposChagSetaEntity.setCretime(date);
        rposRposChagSetaEntity.setCreuser(userId);
        rposRposChagSetaEntity.setModtime(date);
        rposRposChagSetaEntity.setModuser(userId);
        //向数据库里面插入岗位信息
        String jobpositionId = (String) ormService.insertSelective(jobpositionEntity);
        //向数据库里面插入岗位记录信息
        ormService.insertSelective(rposRposChagSetaEntity);
        logger.info("插入岗位信息，插入后返回的岗位Id为：" + jobpositionId);
        return jobpositionId;
    }

    private String createDepttreeEntity(String userId, String employeeId, EnterpriseEntity enterprise, String jobpositionEntityId) throws Exception {
        DynamicDataSourceUtil.setDataSource(enterprise.getEnte_sceo_url());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Map<String, Object> params = new HashMap<String, Object>();
        DepttreeEntity depttreeEntity = new DepttreeEntity();
        depttreeEntity.setMdep_mcop(enterprise.getId());
        depttreeEntity.setMdep_name(enterprise.getEnte_name_cn() + "董事会");
        depttreeEntity.setMdep_sname(enterprise.getEnte_nickname() + "董事会");
        depttreeEntity.setMdep_lname(enterprise.getEnte_name_cn() + "董事会");
        depttreeEntity.setMdep_lvl_code("001,");
        depttreeEntity.setMdep_grade("01");
        depttreeEntity.setMdep_seq(1);
        depttreeEntity.setMdep_tl_num(0);
        depttreeEntity.setMdep_ll_num(0);
        //添加所属类
        depttreeEntity.setEdmd_class("depttree");
        //添加企业对象
        depttreeEntity.setEdmd_ente(enterprise.getId());
        //部门主责岗位：关联岗位类
        depttreeEntity.setMdep_leader_post(jobpositionEntityId);
        depttreeEntity.setMdep_leader(employeeId);
        params.put("edmnEncode", "D");
        params.put("edmnType", "2");
        Result depttreResult = bizModelerService.numbers(params);
        if (StringUtil.isNullOrEmpty(depttreResult) || !Result.RECODE_SUCCESS.equals(depttreResult.getRetCode())) {
            throw new RuntimeException("调用modeler获取部门编码错误; params ; {} " + params.toString());
        }
        depttreeEntity.setMdep_code(String.valueOf(depttreResult.getData()));
        depttreeEntity.setCretime(date);
        depttreeEntity.setCreuser(userId);
        depttreeEntity.setModtime(date);
        depttreeEntity.setModuser(userId);
        String deptteeID = (String) ormService.insertSelective(depttreeEntity);
        logger.info("插入部门信息，插入后返回的部门Id为：" + deptteeID);

        MdepMdepChagSetaEntity mdepMdepChagSetaEntity = new MdepMdepChagSetaEntity();
        mdepMdepChagSetaEntity.setId(UuidCreater.uuid());
        mdepMdepChagSetaEntity.setPid(deptteeID);
        mdepMdepChagSetaEntity.setClassName("depttree");
        mdepMdepChagSetaEntity.setMdep_name_his(depttreeEntity.getMdep_name());
        mdepMdepChagSetaEntity.setMdep_sname_his(depttreeEntity.getMdep_sname());
        mdepMdepChagSetaEntity.setMdep_grade_his("01");
        mdepMdepChagSetaEntity.setMdep_lvl_his("001,");
        mdepMdepChagSetaEntity.setMdep_seq_his(1);
        mdepMdepChagSetaEntity.setCretime(date);
        mdepMdepChagSetaEntity.setMdep_beg_his(date);
        mdepMdepChagSetaEntity.setMdep_end_his(dateFormat.parse("9999-12-30 00:00:00"));
        mdepMdepChagSetaEntity.setCreuser(userId);
        mdepMdepChagSetaEntity.setModtime(date);
        mdepMdepChagSetaEntity.setModuser(userId);
        String mdepMdepChagSetaEntityId = (String) ormService.insert(mdepMdepChagSetaEntity);
        logger.info("插入变更记录，插入后返回的Id为：" + mdepMdepChagSetaEntityId);

        MdepMdepChgrSetaEntity mdepMdepChgrSetaEntity = new MdepMdepChgrSetaEntity();
        mdepMdepChgrSetaEntity.setId(UuidCreater.uuid());
        mdepMdepChgrSetaEntity.setPid(deptteeID);
        mdepMdepChgrSetaEntity.setClassName("depttree");
        mdepMdepChgrSetaEntity.setMdep_chgr_type("1");
        mdepMdepChgrSetaEntity.setMdep_chgr_post(jobpositionEntityId);
        mdepMdepChgrSetaEntity.setMdep_chgr_emp(employeeId);
        mdepMdepChgrSetaEntity.setMdep_chgr_beg(date);
        mdepMdepChgrSetaEntity.setMdep_chgr_end(dateFormat.parse("9999-12-30 00:00:00"));
        mdepMdepChgrSetaEntity.setCretime(date);
        mdepMdepChgrSetaEntity.setCreuser(userId);
        mdepMdepChgrSetaEntity.setModtime(date);
        mdepMdepChgrSetaEntity.setModuser(userId);
        String mdepMdepChgrSetaEntityId = (String) ormService.insert(mdepMdepChgrSetaEntity);
        logger.info("插入负责人集合，插入后返回的Id为：" + mdepMdepChgrSetaEntityId);
        return deptteeID;
    }

    @DynamicDataSource(isDefaultSource = true)
    private String createEmployee(String userId, String employeeId, EnterpriseEntity enterprise, String jobpositionId, String deptteeID) throws Exception {
        Date date = new Date();
        EmployeeEntity employeeEntity = new EmployeeEntity();
        //自然人对象
        employeeEntity.setRemp_epeo_obj(userId);
        //根据userId去查询当前自然人信息
        PeopleEntity peopleEntity = ormService.load(PeopleEntity.class, userId);
        DynamicDataSourceUtil.setDataSource(enterprise.getEnte_sceo_url());
        //添加员工的id
        employeeEntity.setId(employeeId);
        //添加工号
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("edmnEncode", "");
        params.put("edmnType", "4");
        Result rempNoResult = bizModelerService.numbers(params);
        if (StringUtil.isNullOrEmpty(rempNoResult) || !Result.RECODE_SUCCESS.equals(rempNoResult.getRetCode())) {
            throw new RuntimeException("调用modeler获取工号错误; params ; {} " + params.toString());
        }
        employeeEntity.setRemp_no(String.valueOf(rempNoResult.getData()));
        //拼接姓名字符串
        String employeeName = peopleEntity.getEpeo_fist_name() + peopleEntity.getEpeo_last_name();
        //添加姓名
        employeeEntity.setRemp_name(employeeName);
        //添加英文名
        employeeEntity.setRemp_name_en(peopleEntity.getEpeo_name_en());
        //添加姓名拼音
        employeeEntity.setRemp_name_cn(peopleEntity.getEpeo_name_cn());
        //添加法人公司
        employeeEntity.setRemp_mcop(enterprise.getId());
        //员工类型
        employeeEntity.setRemp_type("1");
        //身份账号
        employeeEntity.setRemp_card_id(peopleEntity.getEpeo_card_no());
        //添加岗位信息:关联岗位表
        employeeEntity.setRemp_post(jobpositionId);
        //添加部门信息
        employeeEntity.setRemp_dept(deptteeID);
        //所属类
        employeeEntity.setEdmd_class("employee");
        //企业对象
        employeeEntity.setEdmd_ente(enterprise.getId());

        employeeEntity.setCretime(date);
        employeeEntity.setCreuser(employeeId);
        employeeEntity.setModtime(date);
        employeeEntity.setModuser(employeeId);
        ormService.insert(employeeEntity);
        logger.info("插入员工信息，插入信息后返回的员工ID为：" + employeeId);
        return employeeId;
    }

    /**
     * @param organizationCode
     * @return boolean
     * @description 判断企业是否已注册
     * @method isOrganizationCodeExist
     */
    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Boolean isOrganizationCodeExist(String organizationCode) {
        OrmParam ormParam = new OrmParam();
        ormParam.addWhereParam("ente_org_code", organizationCode);
        String whereExp = "ente_org_code = #{whereParam.ente_org_code}";
        ormParam.setWhereExp(whereExp);
        boolean tag = true;
        try {
            List<EnterpriseEntity> list = ormService.selectBeanList(EnterpriseEntity.class, ormParam);
            if (StringUtil.isNullOrEmpty(list) || list.size() == 0) {
                tag = false;
            }
        } catch (Exception e) {
            logger.error("isOrganizationCodeExist方法执行异常异常" + e.getMessage() + e);
            throw new RuntimeException(e);
        }
        return tag;
    }

    /**
     * @param enteNameCn
     * @return boolean
     * @description 判断企业全称的是否存在
     * @method isEnterpriseFullNameExist
     */
    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Boolean isEnterpriseFullNameExist(String enteNameCn) {
        OrmParam ormParam = new OrmParam();
        ormParam.addWhereParam("ente_name_cn", enteNameCn);
        String whereExp = "ente_name_cn = #{whereParam.ente_name_cn}";
        ormParam.setWhereExp(whereExp);
        boolean tag = true;
        try {
            List<EnterpriseEntity> list = ormService.selectBeanList(EnterpriseEntity.class, ormParam);
            if (StringUtil.isNullOrEmpty(list) || list.size() == 0) {
                tag = false;
            }
        } catch (Exception e) {
            logger.error("isEnterpriseFullNameExist方法执行异常" + e.getMessage() + e);
            throw new RuntimeException(e);
        }
        return tag;
    }

    /**
     * @param enteSceoUrl
     * @return java.lang.Boolean
     * @description 判断企业Sceo_Url是否存在
     * @method isSceoUrlExist
     */
    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Boolean isSceoUrlExist(String enteSceoUrl) {
        OrmParam ormParam = new OrmParam();
        ormParam.addWhereParam("ente_sceo_url", enteSceoUrl);
        String whereExp = "ente_sceo_url = #{whereParam.ente_sceo_url}";
        ormParam.setWhereExp(whereExp);
        boolean tag = true;
        try {
            List<EnterpriseEntity> list = ormService.selectBeanList(EnterpriseEntity.class, ormParam);
            if (StringUtil.isNullOrEmpty(list) || list.size() == 0) {
                tag = false;
            }
        } catch (Exception e) {
            logger.error("isEnterpriseUrlExist方法执行异常" + e.getMessage() + e);
            throw new RuntimeException(e);
        }
        return tag;
    }
}
