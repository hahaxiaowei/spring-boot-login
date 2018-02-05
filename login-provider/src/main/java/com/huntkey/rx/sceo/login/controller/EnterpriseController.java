package com.huntkey.rx.sceo.login.controller;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.edm.entity.EnterpriseEntity;
import com.huntkey.rx.sceo.login.service.EnterpriseService;
import com.huntkey.rx.sceo.method.register.plugin.annotation.MethodRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lulx on 2018/1/8 0008 上午 10:01
 */
@RestController
@RequestMapping("/enterprise")
public class EnterpriseController {

    private static Logger logger = LoggerFactory.getLogger(EnterpriseController.class);

    @Autowired
    private EnterpriseService enterpriseService;

    @RequestMapping(value = "/updateEnterpriseInfo", method = RequestMethod.PUT)
    @MethodRegister(
            edmClass = "enterprise",
            methodCate = "ID系统",
            methodDesc = "更新企业信息"
    )
    public Result updateEnterpriseInfo(@RequestBody EnterpriseEntity enterprise) {
        Result result = null;
        try {
            result = enterpriseService.updateEnterpriseInfo(enterprise);
        } catch (Exception e) {
            logger.error("updateEnterpriseInfo error : " + e.getMessage(), e);
            throw new RuntimeException("updateEnterpriseInfo error : " + e.getMessage(), e);
        }
        return result;
    }

    @RequestMapping(value = "/enterpriseList", method = RequestMethod.GET)
    @MethodRegister(
            edmClass = "enterprise",
            methodCate = "ID系统",
            methodDesc = "获取企业信息列表"
    )
    public Result enterpriseList() {
        Result result = null;
        try {
            result = enterpriseService.enterpriseList();
        } catch (Exception e) {
            logger.error("enterpriseList error : " + e.getMessage(), e);
            throw new RuntimeException("enterpriseList error : " + e.getMessage(), e);
        }
        return result;
    }
}
