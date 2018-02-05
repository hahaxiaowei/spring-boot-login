package com.huntkey.rx.sceo.login.controller;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.edm.entity.EpeoEpeoFamSetaEntity;
import com.huntkey.rx.edm.entity.EpeoEpeoStuSetaEntity;
import com.huntkey.rx.edm.entity.EpeoEpeoWorkSetaEntity;
import com.huntkey.rx.edm.entity.PeopleEntity;
import com.huntkey.rx.sceo.common.entity.UserInfo;
import com.huntkey.rx.sceo.common.utils.UserUtil;
import com.huntkey.rx.sceo.login.aop.LimitIpRequest;
import com.huntkey.rx.sceo.login.service.LoginService;
import com.huntkey.rx.sceo.login.service.UserService;
import com.huntkey.rx.sceo.method.register.plugin.annotation.MethodRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lulx on 2017/11/22 0022 下午 16:16
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    /**
     * 查询用户信息
     *
     * @return
     */
    @RequestMapping(value = "/queryPeopleInfo", method = RequestMethod.GET)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "查询用户信息"
    )
    public Result queryPeopleInfo() {
        Result result = null;
        try {
            result = userService.queryPeopleInfo(UserUtil.getUserId());
        } catch (Exception e) {
            logger.error("queryPeopleInfo error : " + e.getMessage(), e);
            throw new RuntimeException("queryPeopleInfo error : " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 查询用户信息
     *
     * @return
     */
    @RequestMapping(value = "/queryPeopleInfoById/{userId}", method = RequestMethod.GET)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "根据userId查询用户信息"
    )
    public Result queryPeopleInfoById(@PathVariable String userId) {
        Result result = null;
        try {
            result = userService.queryPeopleInfo(userId);
        } catch (Exception e) {
            logger.error("queryPeopleInfoById error : " + e.getMessage(), e);
            throw new RuntimeException("queryPeopleInfoById error : " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 查询企业列表
     *
     * @return
     */
    @RequestMapping(value = "/queryEnterpriseList", method = RequestMethod.GET)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "查询用户企业列表"
    )
    public Result queryEnterpriseList() {
        Result result = null;
        try {
            result = userService.queryEnterpriseList(UserUtil.getUserId());
        } catch (Exception e) {
            logger.error("queryEnterpriseList error : " + e.getMessage(), e);
            throw new RuntimeException("queryEnterpriseList error : " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 更新用户信息
     *
     * @param peopleEntity
     * @return
     */
    @RequestMapping(value = "/updatePeopleInfo", method = RequestMethod.POST)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "更新用户信息"
    )
    public Result updatePeopleInfo(@RequestBody PeopleEntity peopleEntity) {
        Result result = null;
        try {
            peopleEntity.setId(UserUtil.getUserId());
            result = userService.updatePeopleInfo(peopleEntity);
        } catch (Exception e) {
            logger.error("queryPeopleInfo error : " + e.getMessage(), e);
            throw new RuntimeException("queryPeopleInfo error : " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 查询用户企业岗位信息
     *
     * @param enterpriseId 企业id
     * @return
     */
    @RequestMapping(value = "/queryEnterprise/{enterpriseId}", method = RequestMethod.GET)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "查询用户企业岗位信息"
    )
    public Result queryEnterprise(@PathVariable String enterpriseId, HttpServletRequest request) {
        Result result = null;
        try {
            result = userService.queryEnterprise(enterpriseId, UserUtil.getUserId());
            //更新当前用户状态信息
            loginService.updateCurrentEnterprise(result, request);
        } catch (Exception e) {
            logger.error("queryEnterprise error : " + e.getMessage(), e);
            throw new RuntimeException("queryEnterprise error : " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 新增用户信息
     *
     * @param peopleEntity
     * @param enterpriseId 企业id
     * @return
     */
    @RequestMapping(value = "/addPeople/{enterpriseId}", method = RequestMethod.POST)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "新增用户信息"
    )
    public Result addPeople(@RequestBody PeopleEntity peopleEntity, @PathVariable String enterpriseId) {
        Result result = null;
        try {
            peopleEntity.setId(UserUtil.getUserId());
            result = userService.addPeople(peopleEntity, enterpriseId);
        } catch (Exception e) {
            logger.error("addPeople error : " + e.getMessage(), e);
            throw new RuntimeException("addPeople error : " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 根据身份证号查询用户信息
     *
     * @param idCard 身份证号
     * @return
     */
    @RequestMapping(value = "/queryPeopleInfoByIdCard", method = RequestMethod.GET)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "根据身份证号查询用户信息",
            getReqParamsNameNoPathVariable = {"idCard"}
    )
    public Result queryPeopleInfoByIdCard(@RequestParam String idCard) {
        Result result = null;
        try {
            result = userService.queryPeopleInfoByIdCard(idCard);
        } catch (Exception e) {
            logger.error("queryPeopleInfoByTel error : " + e.getMessage(), e);
            throw new RuntimeException("queryPeopleInfoByTel error : " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 入职
     * 更新用户企业圈
     *
     * @param enterpriseId 企业id
     * @param peopleId     自然人id
     * @return
     */
    @RequestMapping(value = "/removePeopleEnteSet/{enterpriseId}/{peopleId}", method = RequestMethod.POST)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "离职更新用户企业圈"
    )
    public Result removePeopleEnteSet(@PathVariable String enterpriseId, @PathVariable String peopleId) {
        Result result = null;
        try {
            result = userService.removePeopleEnteSet(peopleId, enterpriseId);
        } catch (Exception e) {
            logger.error("removePeopleEnteSet error : " + e.getMessage(), e);
            throw new RuntimeException("removePeopleEnteSet error : " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 离职
     * 更新用户企业圈
     *
     * @param enterpriseId 企业id
     * @param peopleId     自然人id
     * @return
     */
    @RequestMapping(value = "/addPeopleEnteSet/{enterpriseId}/{peopleId}", method = RequestMethod.POST)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "入职更新用户企业圈"
    )
    public Result addPeopleEnteSet(@PathVariable String enterpriseId, @PathVariable String peopleId) {
        Result result = null;
        try {
            result = userService.addPeopleEnteSet(peopleId, enterpriseId);
        } catch (Exception e) {
            logger.error("addPeopleEnteSet error : " + e.getMessage(), e);
            throw new RuntimeException("addPeopleEnteSet error : " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 增加用户的教育背景
     *
     * @param stuSet
     * @return
     */
    @RequestMapping(value = "/addStuSet", method = RequestMethod.POST)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "增加教育背景"
    )
    public Result addStuSet(@RequestBody EpeoEpeoStuSetaEntity stuSet) {
        Result result = null;
        try {
            result = userService.addStuSet(stuSet);
        } catch (Exception e) {
            logger.error("addStuSet error : " + e.getMessage(), e);
            throw new RuntimeException("addStuSet error : " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 删除用户的教育背景
     *
     * @param stuSetId
     * @return
     */
    @RequestMapping(value = "/delStuSet/{stuSetId}", method = RequestMethod.DELETE)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "删除教育背景"
    )
    public Result delStuSet(@PathVariable String stuSetId) {
        Result result = null;
        try {
            result = userService.delStuSet(stuSetId);
        } catch (Exception e) {
            logger.error("delStuSet error : " + e.getMessage(), e);
            throw new RuntimeException("delStuSet error : " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 更新用户的教育背景
     *
     * @param stuSet
     * @return
     */
    @RequestMapping(value = "/modStuSet", method = RequestMethod.PUT)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "更新教育背景"
    )
    public Result modStuSet(@RequestBody EpeoEpeoStuSetaEntity stuSet) {
        Result result = null;
        try {
            result = userService.modStuSet(stuSet);
        } catch (Exception e) {
            logger.error("modStuSet error : " + e.getMessage(), e);
            throw new RuntimeException("modStuSet error : " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 增加用户的工作经历
     *
     * @param workSet
     * @return
     */
    @RequestMapping(value = "/addWorkSet", method = RequestMethod.POST)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "增加工作经历"
    )
    public Result addWorkSet(@RequestBody EpeoEpeoWorkSetaEntity workSet) {
        Result result = null;
        try {
            result = userService.addWorkSet(workSet);
        } catch (Exception e) {
            logger.error("addWorkSet error : " + e.getMessage(), e);
            throw new RuntimeException("addWorkSet error : " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 删除用户的工作经历
     *
     * @param workSetId
     * @return
     */
    @RequestMapping(value = "/delWorkSet/{workSetId}", method = RequestMethod.DELETE)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "删除工作经历"
    )
    public Result delWorkSet(@PathVariable String workSetId) {
        Result result = null;
        try {
            result = userService.delWorkSet(workSetId);
        } catch (Exception e) {
            logger.error("delWorkSet error : " + e.getMessage(), e);
            throw new RuntimeException("delWorkSet error : " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 更新用户的工作经历
     *
     * @param workSet
     * @return
     */
    @RequestMapping(value = "/modWorkSet", method = RequestMethod.PUT)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "更新工作经历"
    )
    public Result modWorkSet(@RequestBody EpeoEpeoWorkSetaEntity workSet) {
        Result result = null;
        try {
            result = userService.modWorkSet(workSet);
        } catch (Exception e) {
            logger.error("modWorkSet error : " + e.getMessage(), e);
            throw new RuntimeException("modWorkSet error : " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 增加用户的家庭成员
     *
     * @param famSet
     * @return
     */
    @RequestMapping(value = "/addFamSet", method = RequestMethod.POST)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "增加家庭成员"
    )
    public Result addFamSet(@RequestBody EpeoEpeoFamSetaEntity famSet) {
        Result result = null;
        try {
            result = userService.addFamSet(famSet);
        } catch (Exception e) {
            logger.error("addFamSet error : " + e.getMessage(), e);
            throw new RuntimeException("addFamSet error : " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 删除用户的家庭成员
     *
     * @param famSetId
     * @return
     */
    @RequestMapping(value = "/delFamSet/{famSetId}", method = RequestMethod.DELETE)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "删除家庭成员"
    )
    public Result delFamSet(@PathVariable String famSetId) {
        Result result = null;
        try {
            result = userService.delFamSet(famSetId);
        } catch (Exception e) {
            logger.error("delFamSet error : " + e.getMessage(), e);
            throw new RuntimeException("delFamSet error : " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 获取用户的家庭成员
     *
     * @param famSetId
     * @return
     */
    @RequestMapping(value = "/getFamSet/{famSetId}", method = RequestMethod.GET)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "获取家庭成员"
    )
    public Result getFamSet(@PathVariable String famSetId) {
        Result result = null;
        try {
            result = userService.getFamSet(famSetId);
        } catch (Exception e) {
            logger.error("getFamSet error : " + e.getMessage(), e);
            throw new RuntimeException("getFamSet error : " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 更新用户的家庭成员
     *
     * @param famSet
     * @return
     */
    @RequestMapping(value = "/modFamSet", method = RequestMethod.PUT)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "更新家庭成员"
    )
    public Result modFamSet(@RequestBody EpeoEpeoFamSetaEntity famSet) {
        Result result = null;
        try {
            result = userService.modFamSet(famSet);
        } catch (Exception e) {
            logger.error("modFamSet error : " + e.getMessage(), e);
            throw new RuntimeException("modFamSet error : " + e.getMessage(), e);
        }
        return result;
    }

    @RequestMapping(value = "/getFamSetList", method = RequestMethod.PUT)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "获取家庭成员列表"
    )
    public Result getFamSetList() {
        Result result = null;
        try {
            result = userService.getFamSetList();
        } catch (Exception e) {
            logger.error("getFamSetList error : " + e.getMessage(), e);
            throw new RuntimeException("getFamSetList error : " + e.getMessage(), e);
        }
        return result;
    }

    @RequestMapping(value = "/getStuSetList", method = RequestMethod.PUT)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "获取教育经历列表"
    )
    public Result getStuSetList() {
        Result result = null;
        try {
            result = userService.getStuSetList();
        } catch (Exception e) {
            logger.error("getStuSetList error : " + e.getMessage(), e);
            throw new RuntimeException("getStuSetList error : " + e.getMessage(), e);
        }
        return result;
    }

    @RequestMapping(value = "/getWorkSetList", method = RequestMethod.PUT)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "获取工作经历列表"
    )
    public Result getWorkSetList() {
        Result result = null;
        try {
            result = userService.getWorkSetList();
        } catch (Exception e) {
            logger.error("getWorkSetList error : " + e.getMessage(), e);
            throw new RuntimeException("getWorkSetList error : " + e.getMessage(), e);
        }
        return result;
    }

    @RequestMapping(value = "/modPassWord", method = RequestMethod.PUT)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "修改密码"
    )
    public Result modPassWord(@RequestBody UserInfo userInfo) {
        Result result = null;
        try {
            result = userService.modPassWord(userInfo);
        } catch (Exception e) {
            logger.error("modPassWord error : " + e.getMessage(), e);
            throw new RuntimeException("modPassWord error : " + e.getMessage(), e);
        }
        return result;
    }

    @RequestMapping(value = "/modTel", method = RequestMethod.PUT)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "修改手机号"
    )
    public Result modTel(@RequestBody UserInfo userInfo) {
        Result result = null;
        try {
            result = userService.modTel(userInfo);
        } catch (Exception e) {
            logger.error("modTel error : " + e.getMessage(), e);
            throw new RuntimeException("modTel error : " + e.getMessage(), e);
        }
        return result;
    }

    @RequestMapping(value = "/mailCode", method = RequestMethod.GET)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "发送邮箱验证码",
            getReqParamsNameNoPathVariable = {"mail"}
    )
    @LimitIpRequest(
            methodUrl = "/user/mailCode",
            limitCount = 5
    )
    public Result mailCode(@RequestParam String mail) {
        Result result = null;
        try {
            result = userService.mailCode(mail);
        } catch (Exception e) {
            logger.error("mailCode error : " + e.getMessage(), e);
            throw new RuntimeException("mailCode error : " + e.getMessage(), e);
        }
        return result;
    }


    @RequestMapping(value = "/modMail", method = RequestMethod.PUT)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "修改邮箱"
    )
    public Result modMail(@RequestBody UserInfo userInfo) {
        Result result = null;
        try {
            result = userService.modMail(userInfo);
        } catch (Exception e) {
            logger.error("modMail error : " + e.getMessage(), e);
            throw new RuntimeException("modMail error : " + e.getMessage(), e);
        }
        return result;
    }
}
