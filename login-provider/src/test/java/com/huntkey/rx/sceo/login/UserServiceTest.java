package com.huntkey.rx.sceo.login;

import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.edm.entity.PeopleEntity;
import com.huntkey.rx.sceo.login.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by lulx on 2017/11/22 0022 下午 16:43
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BizLoginAuthenticationApplication.class)
public class UserServiceTest {

    private static Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @Autowired
    UserService userService;

    @Test
    public void queryUserInfoTest(){
        Result result = userService.queryPeopleInfo("32c74c78d0394bd48b54429c3cd7d007");
        logger.info("-----------");
        logger.info(JSONObject.toJSONString(result));
        Assert.assertEquals(Result.RECODE_SUCCESS, result.getRetCode());
        logger.info("-----------");
    }

    @Test
    public void queryEnterpriseListTest(){
        Result result = userService.queryEnterpriseList("bf9d3dc0f916436ca56b449d3e688e3e");
        logger.info("-----------");
        logger.info(JSONObject.toJSONString(result));
        Assert.assertEquals(Result.RECODE_SUCCESS, result.getRetCode());
        logger.info("-----------");
    }

    @Test
    public void test(){
        Result result = userService.queryPeopleInfo("32c74c78d0394bd48b54429c3cd7d007");
        logger.info("-----------");
        logger.info(JSONObject.toJSONString(result));
        Assert.assertEquals(Result.RECODE_SUCCESS, result.getRetCode());
        logger.info("-----------");

        PeopleEntity peopleEntity = (PeopleEntity) result.getData();
        peopleEntity.setEpeo_name_ni(peopleEntity.getEpeo_name_ni()+ "H");

        Result result2 = userService.updatePeopleInfo(peopleEntity);
        logger.info("-----------");
        logger.info(JSONObject.toJSONString(result2));
        Assert.assertEquals(Result.RECODE_SUCCESS, result2.getRetCode());
        logger.info("-----------");

        Result result3 = userService.queryPeopleInfo("32c74c78d0394bd48b54429c3cd7d007");
        PeopleEntity peopleEntity2 = (PeopleEntity) result.getData();

        logger.info("-----------");
        logger.info(JSONObject.toJSONString(result));
        Assert.assertEquals(peopleEntity.getEpeo_name_ni(), peopleEntity2.getEpeo_name_ni());
        logger.info("-----------");
    }


    @Test
    public void queryEnterpriseTest(){
        Result result = userService.queryEnterprise("2fd4254c2f014905a5b29bd75e5675e1", "32c74c78d0394bd48b54429c3cd7d007");
        logger.info("-----------");
        logger.info(JSONObject.toJSONString(result));
        Assert.assertEquals(Result.RECODE_SUCCESS, result.getRetCode());
        logger.info("-----------");
    }

    @Test
    public void mailCodeTest(){
        Result result = userService.mailCode("lulx@huntkey.net");
        logger.info("-----------");
        logger.info(JSONObject.toJSONString(result));
        Assert.assertEquals(Result.RECODE_SUCCESS, result.getRetCode());
        logger.info("-----------");
    }
}
