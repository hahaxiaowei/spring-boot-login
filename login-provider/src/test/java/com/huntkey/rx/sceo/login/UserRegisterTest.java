package com.huntkey.rx.sceo.login;

import com.alibaba.fastjson.JSON;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.commons.utils.uuid.UuidCreater;
import com.huntkey.rx.edm.entity.PeopleEntity;
import com.huntkey.rx.sceo.login.service.RegisterService;
import com.huntkey.rx.sceo.orm.common.model.OrmParam;
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
import java.util.Map;

/**
 * Created by lulx on 2017/11/22 0022 下午 16:43
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BizLoginAuthenticationApplication.class)
public class UserRegisterTest {

    private static Logger logger = LoggerFactory.getLogger(UserRegisterTest.class);

    @Autowired
    RegisterService registerService;

    @Autowired
    OrmService ormService;

    @Test
    public void userRegister(){
        Result tag = registerService.selectPeopleByTelPhone("111111", "");
        logger.info("------------");
        logger.info(tag.toString());
        logger.info("------------");
    }
    @Test
    public void selectPeopleByTelPhone(){
        OrmParam ormParam = new OrmParam();
        ormParam.addWhereParam("epeo_tel","111");
        String whereExp = "epeo_tel = #{whereParam.epeo_tel}";
        ormParam.setWhereExp(whereExp);
        Boolean tag = true;
        try {
            if (StringUtil.isNullOrEmpty(ormService.selectBeanList(PeopleEntity.class, ormParam))) {
                tag = false;
            }
        } catch (Exception e) {
            logger.error("selectPeopleByTelPhone方法执行异常异常");
            e.printStackTrace();
        }
        logger.info("tag的值为"+tag);
    }

    @Test
    public void isVerificationCorrect(){
        Result result  = registerService.getVerificatCode("18856315599");
        Map<String, Object> mapRedis = JSON.parseObject(JSON.toJSONString(result.getData()), Map.class);
        String code = String.valueOf(mapRedis.get("verificatCode"));
        logger.info("验证码为"+ result.getData());
        try {
            Thread.sleep(60000);
            Boolean tag = registerService.isVerificatCorrect(code,"18856315599");
            logger.info("判断结果"+tag);

            Result result1 = registerService.getVerificatCode("18856315599");
            logger.info("重新获取验证码"+result1.getData());
            Map<String, Object> mapRedis2 = JSON.parseObject(JSON.toJSONString(result1.getData()), Map.class);
            String code2 = String.valueOf(mapRedis2.get("verificatCode"));
            Boolean tag2 = registerService.isVerificatCorrect(code2,"18856315599");
            logger.info("这时候判断验证码为："+tag2);
        } catch (Exception e) {
            logger.error("方法执行异常"+ e.getMessage() + e);
            throw new RuntimeException(e);
        }
    }
    @Test
    public void addUserInfo() {

        PeopleEntity peopleEntity = new PeopleEntity();
        peopleEntity.setEpeo_fist_name("王");
        peopleEntity.setId(UuidCreater.uuid());
        peopleEntity.setCreuser("王小宝");;
        Date date = new Date();
        peopleEntity.setCretime(date);
        peopleEntity.setModuser("王小宝");
        peopleEntity.setModtime(date);
        peopleEntity.setEpeo_password("88888888");
        peopleEntity.setEpeo_tel("110110110");
        peopleEntity.setEpeo_addr("安徽合肥");
        peopleEntity.setEpeo_blood("B型血");
        Result result = registerService.addUserInfo(peopleEntity);
        logger.info("---------"+result);
        System.out.println("执行成功");
    }

    @Test
    public void getVerificatCodeTest(){
        Result verificatCode = registerService.getVerificatCode("15688888888");
        logger.info("---------");
        logger.info("verificatCode : " + verificatCode.getData());
        Assert.assertEquals(Result.RECODE_SUCCESS, verificatCode.getRetCode());
        logger.info("---------");
    }
}
