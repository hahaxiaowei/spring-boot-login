package com.huntkey.rx.sceo.login.controller;

import com.huntkey.rx.commons.utils.eco.EcoSystemUtil;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.edm.entity.PeopleEntity;
import com.huntkey.rx.sceo.common.entity.EnterpriseVo;
import com.huntkey.rx.sceo.login.aop.LimitIpRequest;
import com.huntkey.rx.sceo.login.service.RegisterService;
import com.huntkey.rx.sceo.method.register.plugin.annotation.MethodRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by caojq on 2017/11/21.
 */
@RestController
@RequestMapping("/register")
public class RegisterController {

    private static Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private RegisterService registerService;

    /**
     * @param epeo_tel
     * @return com.huntkey.rx.commons.utils.rest.Result
     * @description 如果手机号存在返回0 不存在返回1
     * @method isPhoneNumberExist
     */

    @RequestMapping(value = "/isPhoneNumberExist", method = RequestMethod.GET)
    @MethodRegister(edmClass = "people",
            methodCate = "ID系统",
            getReqParamsNameNoPathVariable = {"epeo_tel", "peopleId"},
            methodDesc = "验证手机号是否注册过，如果手机号存在返回true 不存在返回false")
    public Result isPhoneNumberExist(@RequestParam String epeo_tel, @RequestParam String peopleId) {
        Result result = new Result();
        try {
            result = registerService.selectPeopleByTelPhone(epeo_tel, peopleId);
        } catch (Exception e) {
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("执行方法出错");
            logger.error("isPhoneNumberExist方法执行出错", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * @param peopleEntity
     * @return com.huntkey.rx.commons.utils.rest.Result
     * @description 自然人注册：添加自然人信息
     * @method addUserInfo
     */
    @RequestMapping(value = "/addUserInfo", method = RequestMethod.POST)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "自然人注册：添加用户信息"
    )
    public Result addUserInfo(@RequestBody PeopleEntity peopleEntity) {
        Result result = new Result();
        try {
            result = registerService.addUserInfo(peopleEntity);
        } catch (Exception e) {
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("执行方法出错");
            logger.info("addUserInfo方法执行出错", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * @param verificationcode
     * @param epeo_tel
     * @return com.huntkey.rx.commons.utils.rest.Result
     * @description 校验验证码
     * @method isVerificatCorrect
     */
    @RequestMapping(value = "/isVerificatCorrect", method = RequestMethod.GET)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            getReqParamsNameNoPathVariable = {"epeo_tel", "verificationcode"},
            methodDesc = "验证码校验"
    )
    public Result isVerificatCorrect(@RequestParam String epeo_tel, @RequestParam String verificationcode) {

        Result result = new Result();
        try {
            if (registerService.isVerificatCorrect(verificationcode, epeo_tel)) {
                result.setRetCode(Result.RECODE_SUCCESS);
                return result;
            }
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("验证码输入错误或输入已超时");
        } catch (Exception e) {
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("执行方法出错");
            logger.error("isVerificatCorrect方法执行出错", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 获取手机验证码
     *
     * @param phoneNumOrEmail
     * @return
     */
    @RequestMapping(value = "/getVerificatCode/{phoneNumOrEmail}", method = RequestMethod.GET)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "获取手机验证码"
    )
    @LimitIpRequest(
            methodUrl = "register/getVerificatCode/{phoneNumOrEmail}",
            limitCount = 5
    )
    public Result getVerificatCode(@PathVariable String phoneNumOrEmail) {
        Result result = new Result();
        try {
            result = registerService.getVerificatCode(phoneNumOrEmail);
            logger.info("获取验证码");
        } catch (Exception e) {
            result.setErrMsg("获取验证码失败");
            logger.error("获取验证码失败");
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * @param enterpriseVo
     * @return com.huntkey.rx.commons.utils.rest.Result
     * @description 企业注册：添加企业信息
     * @method addEnterPriseInfo
     */
    @RequestMapping(value = "/addEnterPriseInfo", method = RequestMethod.POST)
    @MethodRegister(
            edmClass = "enterprise",
            methodCate = "ID系统",
            methodDesc = "企业注册：添加企业信息"
    )
    public Result addEnterPriseInfo(@RequestBody EnterpriseVo enterpriseVo, HttpServletRequest httpRequest) {
        Result result = null;
        try {
            String auth = httpRequest.getHeader("Authorization");
            if (StringUtil.isNullOrEmpty(enterpriseVo.getUserId())) {
                Map<String, String> usermap = EcoSystemUtil.ifAuthUsable(auth);
                String userId = usermap.get("id");
                enterpriseVo.setUserId(userId);
            }
            result = registerService.addEnterPrise(enterpriseVo, auth);
        } catch (Exception e) {
            logger.error("addEnterPriseInfo方法执行出错" + e.getMessage(), e);
            throw new RuntimeException("addEnterPriseInfo方法执行出错" + e.getMessage(), e);
        }
        return result;
    }

    @RequestMapping(value = "/initEnterPriseInfo/{enterpriseId}", method = RequestMethod.POST)
    @MethodRegister(
            edmClass = "enterprise",
            methodCate = "ID系统",
            methodDesc = "初始化企业信息"
    )
    public Result initEnterPriseInfo(@PathVariable String enterpriseId, HttpServletRequest httpRequest) {
        Result result = null;
        try {
            String auth = httpRequest.getHeader("Authorization");
            Map<String, String> usermap = EcoSystemUtil.ifAuthUsable(auth);
            String userId = usermap.get("id");
            result = registerService.initEnterPriseInfo(userId, enterpriseId);
        } catch (Exception e) {
            logger.error("addEnterPriseInfo方法执行出错: " + e.getMessage(), e);
            throw new RuntimeException("addEnterPriseInfo方法执行出错: " + e.getMessage(), e);
        }
        return result;
    }
}
