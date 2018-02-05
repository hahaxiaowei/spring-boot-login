package com.huntkey.rx.sceo.login.controller;

import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.edm.entity.*;
import com.huntkey.rx.sceo.common.constant.Constant;
import com.huntkey.rx.sceo.common.entity.CurrentStatus;
import com.huntkey.rx.sceo.common.entity.EcosystemSession;
import com.huntkey.rx.sceo.common.entity.SessionObjectSet;
import com.huntkey.rx.sceo.common.entity.UserInfo;
import com.huntkey.rx.sceo.common.utils.EncryptUtil;
import com.huntkey.rx.sceo.common.utils.JwtUtil;
import com.huntkey.rx.sceo.common.utils.SerializeUtils;
import com.huntkey.rx.sceo.common.utils.SessionUtils;
import com.huntkey.rx.sceo.login.service.LoginService;
import com.huntkey.rx.sceo.method.register.plugin.annotation.MethodRegister;
import com.huntkey.rx.sceo.starter.util.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caojq on 2017/11/22.
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Value("${redisTimeoutSeconds}")
    String redisTimeoutSeconds;

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    /**
     * 登录
     */
    @RequestMapping(method = RequestMethod.POST)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "用户登录"
    )
    public Result login(@RequestBody UserInfo userInfo) {
        Result result = new Result();
        try {
            Result login = loginService.login(userInfo);
            if (!Result.RECODE_SUCCESS.equals(login.getRetCode())) {
                return login;
            }
            PeopleEntity dbuserInfo = (PeopleEntity) login.getData();
            //创建jwt并返回给前台
            userInfo.setEpeoCode(dbuserInfo.getEpeo_code());
            JSONObject subject = JwtUtil.generalSubject(dbuserInfo);
            String token = JwtUtil.createJWT(Constant.JWT_ID, subject, Constant.JWT_TTL);
            JSONObject jo = new JSONObject();
            jo.put("token", token);
            //更新redis中session数据
            updateEcosystemSession(dbuserInfo, token);
            result.setData(jo);
            result.setRetCode(Result.RECODE_SUCCESS);
            logger.info(dbuserInfo.getId() + " 登录成功！");
            return result;
        } catch (Exception e) {
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg(e.getMessage());
            logger.error("登录失败，请重试！" + e);
            return result;
        }
    }

    /**
     * 登出
     */
    @RequestMapping(value = "/loginOut", method = RequestMethod.GET)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "用户登出"
    )
    public Result loginOut(HttpServletRequest request) {
        String tokenKey = EncryptUtil.getTokenKey(request);
        RedisUtils.getInstance().delete(tokenKey.getBytes());
        logger.info("登出成功：" + tokenKey);
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        return result;
    }

    /**
     * 获取EcosystemSession
     */
    @RequestMapping(value = "/getEcosystemSession", method = RequestMethod.GET)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "获取EcosystemSession"
    )
    public Result getEcosystemSession(ServletRequest request) {
        Result result = new Result();
        EcosystemSession ecosystemSession = SessionUtils.getSession(request);
        if (StringUtil.isNullOrEmpty(ecosystemSession)) {
            result.setRetCode(Result.RECODE_UNLOGIN);
            logger.info("请重新登录！");
        } else {
            result.setRetCode(Result.RECODE_SUCCESS);
            result.setData(ecosystemSession);
            logger.info("成功获取ecosystemSession！");
        }
        return result;
    }

    /**
     * 清除CurrentStatus
     */
    @RequestMapping(value = "/clearCurrentStatus", method = RequestMethod.GET)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "清除CurrentStatus"
    )
    public Result clearCurrentStatus(ServletRequest request) {
        logger.info("开始清除currentStatus！");
        EcosystemSession ecosystemSession = SessionUtils.getSession(request);
        ecosystemSession.setCurrentStatus(null);
        String tokenKey = EncryptUtil.getTokenKey(request);
        logger.info("tokenKey：" + tokenKey);
        String redisTimeout[] = redisTimeoutSeconds.split(" ");
        RedisUtils.getInstance().setValue(tokenKey.getBytes(), SerializeUtils.serialize(ecosystemSession),
                Integer.parseInt(redisTimeout[0]) * Integer.parseInt(redisTimeout[1]) * Integer.parseInt(redisTimeout[2]) * Integer.parseInt(redisTimeout[3]));
        Result result = new Result();
        result.setRetCode(Result.RECODE_SUCCESS);
        result.setData(ecosystemSession);
        logger.info("清除currentStatus成功！");
        return result;
    }


    private void updateEcosystemSession(PeopleEntity dbuserInfo, String token) {

        logger.info("开始更新EcosystemSession");

        //构建EcosystemSession
        EcosystemSession ecosystemSession = new EcosystemSession();
        ecosystemSession.setSessionHost("");
        ecosystemSession.setSessionCode(SessionUtils.getSessionCode());
        ecosystemSession.setLanguage(SessionUtils.getLanguage());
        ecosystemSession.setSessionStyle(SessionUtils.getSessionStyle());

        PeopleEntity peopleEntity = new PeopleEntity();
        peopleEntity.setId(dbuserInfo.getId());
        peopleEntity.setEpeo_tel(dbuserInfo.getEpeo_tel());
        peopleEntity.setEpeo_code(dbuserInfo.getEpeo_code());
        peopleEntity.setEpeo_mail(dbuserInfo.getEpeo_mail());
        peopleEntity.setEpeo_name_en(dbuserInfo.getEpeo_name_en());
        peopleEntity.setEpeo_name_cn(dbuserInfo.getEpeo_name_cn());
        peopleEntity.setEpeo_name_ni(dbuserInfo.getEpeo_name_ni());
        peopleEntity.setEpeo_fist_name(dbuserInfo.getEpeo_fist_name());
        peopleEntity.setEpeo_last_name(dbuserInfo.getEpeo_last_name());
        peopleEntity.setEpeo_gender(dbuserInfo.getEpeo_gender());
        peopleEntity.setEpeo_card_no(dbuserInfo.getEpeo_card_no());
        CurrentStatus currentStatus = new CurrentStatus(new ArrayList<JobpositionEntity>(), new EmployeeEntity(), new JobpositionEntity(), new EnterpriseEntity(), new DepttreeEntity());
        ecosystemSession.setCurrentStatus(currentStatus);
        ecosystemSession.setPeople(peopleEntity);

        String tokenKey = EncryptUtil.encryptPassWord(token);
        Map<String, SessionObjectSet> map = new HashMap<String, SessionObjectSet>();
        SessionObjectSet sessionObjectSet = new SessionObjectSet(new HashMap<String, Object>(), new Date());
        map.put(tokenKey, sessionObjectSet);
        ecosystemSession.setSessionnObjectSet(map);
        String redisTimeout[] = redisTimeoutSeconds.split(" ");
        RedisUtils.getInstance().setValue(tokenKey.getBytes(), SerializeUtils.serialize(ecosystemSession),
                Integer.parseInt(redisTimeout[0]) * Integer.parseInt(redisTimeout[1]) * Integer.parseInt(redisTimeout[2]) * Integer.parseInt(redisTimeout[3]));
        logger.info("更新完毕EcosystemSession");

    }

    /**
     * 获取当前岗位信息
     */
    @RequestMapping(value = "/updateCurrentJobInfo/{enterpriseId}/{jobId}", method = RequestMethod.GET)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "updateCurrentJobInfo"
    )
    public Result updateCurrentJobInfo(@PathVariable String enterpriseId, @PathVariable String jobId, ServletRequest request) {
        return loginService.updateCurrentJobInfo(enterpriseId, jobId, request);
    }


}
