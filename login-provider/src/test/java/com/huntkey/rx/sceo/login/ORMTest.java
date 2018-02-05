package com.huntkey.rx.sceo.login;

import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.edm.entity.PeopleEntity;
import com.huntkey.rx.sceo.orm.common.model.OrmParam;
import com.huntkey.rx.sceo.orm.service.OrmService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lulx on 2017/11/22 0022 上午 9:28
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BizLoginAuthenticationApplication.class)
public class ORMTest {

    private static Logger logger = LoggerFactory.getLogger(ORMTest.class);

    @Autowired
    private OrmService ormService;

    @Test
    public void findTeset() throws Exception {
        OrmParam ormParam = new OrmParam();
        ormParam.addWhereParam("epeo_tel", "1111");
        String whereExp = "epeo_tel = #{whereParam.epeo_tel}";
        ormParam.setWhereExp(whereExp);
        List<PeopleEntity> peopleEntities = ormService.selectBeanList(PeopleEntity.class, ormParam);
        logger.info("----------");
        logger.info(JSONObject.toJSONString(peopleEntities));
        logger.info("----------");
    }

    @Test
    public void loadTest() throws Exception {
        PeopleEntity people = ormService.load(PeopleEntity.class, "32c74c78d0394bd48b54429c3cd7d007");
        logger.info("----------");
        logger.info(JSONObject.toJSONString(people));
        logger.info("----------");
    }

    @Test
    public void insertTest() throws Exception {
        PeopleEntity peopleEntity = new PeopleEntity();
        peopleEntity.setEpeo_fist_name("test");
        peopleEntity.setId("222");
        peopleEntity.setCreuser("admin");
        peopleEntity.setCretime(new Date());
        peopleEntity.setModuser("223");
        peopleEntity.setModtime(new Date());
        Serializable insert = ormService.insert(peopleEntity);
        logger.info("----------");
//        logger.info(insert + "");
        logger.info("----------");
    }

    @Test
    public void deleteTest() throws Exception {
        PeopleEntity peopleEntity = new PeopleEntity();
        peopleEntity.setEpeo_fist_name("test");
        int delete = ormService.delete(PeopleEntity.class, "id");
        logger.info(delete + "");
    }

    @Test
    public void updateTest() throws Exception {
        PeopleEntity peopleEntity = new PeopleEntity();
        peopleEntity.setEpeo_fist_name("test");
        peopleEntity.setId("f3af90a846d94199a545515e65b24b83");
        int update = ormService.update(peopleEntity);
        logger.info(update + "");
    }


    @Test
    public void updateSelectiveTest() throws Exception {
        PeopleEntity peopleEntity = new PeopleEntity();
        peopleEntity.setId("f3af90a846d94199a545515e65b24b83");
        peopleEntity.setEpeo_tel("15812345678");
        int update = ormService.updateSelective(peopleEntity);
        logger.info(update + "");
    }
}
