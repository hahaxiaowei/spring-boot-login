package com.huntkey.rx.sceo.login;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.commons.utils.uuid.UuidCreater;
import com.huntkey.rx.edm.entity.EnterpriseEntity;
import com.huntkey.rx.sceo.common.entity.EnterpriseVo;
import com.huntkey.rx.sceo.login.service.RegisterService;
import com.huntkey.rx.sceo.orm.service.OrmService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * Created by lulx on 2017/11/22 0022 下午 16:43
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BizLoginAuthenticationApplication.class)
public class EnterpriseRegisterTest {

    private static Logger logger = LoggerFactory.getLogger(EnterpriseRegisterTest.class);


    @Autowired
    RegisterService registerService;

    @Autowired
    OrmService ormService;


    @Test
    public void isOrganizationCodeExist() {
        Boolean tag = registerService.isOrganizationCodeExist("88888");
        logger.info("判断结果" + tag);
    }

    @Test
    public void isEnterpriseFullNameExist() {
        Boolean tag = registerService.isEnterpriseFullNameExist("中国嘉园锐信科技信息技术有限公");
        logger.info("判断结果" + tag);
    }

    @Test
    public void isSceoUrlExist() {
        Boolean tag = registerService.isSceoUrlExist("baidu33.com");
        logger.info("判断结果" + tag);
    }

    @Test
    public void addEnterPrise() {
        EnterpriseEntity enterpriseEntity = new EnterpriseEntity();
        enterpriseEntity.setId(UuidCreater.uuid());
        enterpriseEntity.setCreuser("admin");
        Date date = new Date();
        enterpriseEntity.setCretime(date);
        enterpriseEntity.setModuser("admin");
        enterpriseEntity.setModtime(date);
        enterpriseEntity.setEnte_idcode("666");
        enterpriseEntity.setEnte_name_cn("嘉源股份1018");
        enterpriseEntity.setEnte_nickname("嘉源股份1018");
        enterpriseEntity.setEnte_org_code("嘉源股份1018");
        enterpriseEntity.setEnte_dbpassword("嘉源股份1018");
        enterpriseEntity.setEnte_fict_peop("admin");
        enterpriseEntity.setEnte_addr("合肥");
        enterpriseEntity.setEnte_sceo_url("嘉源股份1018.com");
        enterpriseEntity.setEnte_fina_beg("2017");
        EnterpriseVo enterpriseVo = new EnterpriseVo();
        enterpriseVo.setEnterpriseEntity(enterpriseEntity);
        enterpriseVo.setUserId("016bec0110aa431586263abbbd595ba4");
        Result result = registerService.addEnterPrise(enterpriseVo, "");
        logger.info("执行成功" + result);
    }

    @Test
    public void initEnterPriseInfoTest() {
        // TODO
        Result result = registerService.initEnterPriseInfo("", "");
        logger.info("-------");
        logger.info(result.toString());
        logger.info("-------");
        Assert.assertEquals(Result.RECODE_SUCCESS, result.getRetCode());
    }

}
