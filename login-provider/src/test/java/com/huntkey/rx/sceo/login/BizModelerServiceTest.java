package com.huntkey.rx.sceo.login;

import com.alibaba.fastjson.JSON;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.edm.entity.DepttreeEntity;
import com.huntkey.rx.sceo.login.feign.BizModelerService;
import com.huntkey.rx.sceo.orm.common.model.DataSourceEntity;
import com.huntkey.rx.sceo.orm.config.DynamicDataSourceContextHolder;
import com.huntkey.rx.sceo.orm.service.DataSourceService;
import com.huntkey.rx.sceo.orm.service.OrmService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Created by lulx on 2017/12/7 0007 下午 15:57
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BizLoginAuthenticationApplication.class)
public class BizModelerServiceTest {
    private static Logger logger  = LoggerFactory.getLogger(BizModelerServiceTest.class);

    @Autowired
    BizModelerService bizModelerService;

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    OrmService ormService;

    @Test
    public void numbersTest(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("edmnEncode", "");
        params.put("edmnType", "4");
        Result numbers = bizModelerService.numbers(params);
        logger.info("-------");
        logger.info(numbers.getData() + "");
        logger.info("-------");
        Assert.assertEquals(Result.RECODE_SUCCESS, numbers.getRetCode());
    }

    @Test
    public void dbPassiveTest() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        final String url = "10.3.99.45", dbname = "testdb", username = "root", pw = "123qwe", port = "3306";
        params.put("url", url + ":" + port);
        params.put("name", username);
        params.put("password", pw);
        params.put("userDataBase", dbname);
        long start = System.currentTimeMillis();
        Result numbers = bizModelerService.dbPassive("V1.0", params);
        logger.info("-------" + (System.currentTimeMillis() - start));
        logger.info("-------");
        logger.info(numbers.toString());
        logger.info("-------");

        DataSourceEntity dataSourceEntity = new DataSourceEntity();
        dataSourceEntity.setDbName(dbname);
        dataSourceEntity.setPassword(pw);
        dataSourceEntity.setUrl(url);
        dataSourceEntity.setPort(port);
        dataSourceEntity.setUsername(username);
        dataSourceService.addDynamicDataSource(dataSourceEntity);

        boolean containsDataSource = DynamicDataSourceContextHolder.containsDataSource(dbname);
        logger.info("containsDataSource : " + containsDataSource);
        List<String> dataSourceIds = DynamicDataSourceContextHolder.dataSourceIds;
        logger.info(JSON.toJSONString("dataSourceIds : " + dataSourceIds));

        Assert.assertEquals(Result.RECODE_SUCCESS, numbers.getRetCode());
    }

}
