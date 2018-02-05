package com.huntkey.rx.sceo.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.sceo.starter.util.RedisUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lulx on 2017/11/30 0030 上午 9:00
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BizLoginAuthenticationApplication.class)
public class RedisTest {

    private static Logger logger = LoggerFactory.getLogger(RedisTest.class);

    @Test
    public void jsonTest() throws InterruptedException {
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("createTime",System.currentTimeMillis());
//        map.put("verificatCode","123456");
//        boolean flag = RedisUtils.getInstance().setValue("12345678901", JSONObject.toJSONString(map), 5);
//        Thread.sleep(5000);
        //根据手机号从redis里面取数据
        String value = RedisUtils.getInstance().getValue("12345678901");
        if (!StringUtil.isNullOrEmpty(value)) {
            Map<String, Object> mapRedis = JSON.parseObject(value, Map.class);
            Long createTime = (Long) mapRedis.get("createTime");
            long laveTime = 60 - (System.currentTimeMillis() - createTime)/1000;
              if (laveTime<5L) {
                  Map<String,Object> newMapFirst = new HashMap<String, Object>();
                  int randNum = 0 + (int) (Math.random() * 1000000);
                  newMapFirst.put("createTime",System.currentTimeMillis());
                  newMapFirst.put("verificatCode",randNum);
                  RedisUtils.getInstance().setValue("手机号",JSONObject.toJSONString(newMapFirst),60);
                  return;
              }else {
                  return;
              }

        }else {
            // 重新创建验证码
            Map<String,Object> newMapSecond = new HashMap<String, Object>();
            int randNum = 0 + (int) (Math.random() * 1000000);
            newMapSecond.put("createTime",System.currentTimeMillis());
            newMapSecond.put("verificatCode",randNum);
            RedisUtils.getInstance().setValue("手机号",JSONObject.toJSONString(newMapSecond),60);
            return;
        }


//        if(laveTime < 5){
//            // 重新创建验证码
//            int randNum = 0 + (int) (Math.random() * 1000000);
//            return;
//        }
//        logger.info("laveTime : " + laveTime);
//        logger.info("verificatCode : " + mapRedis.get("verificatCode"));
//
//        Map<String, Object> map2 = new HashMap<String, Object>();
//        map.put("time","60");
//        map.put("verificatCode","123456");
    }

    public static void main(String[] args) throws InterruptedException {
        Long startTime = System.currentTimeMillis();
        Thread.sleep(5000);
        Long endTime = System.currentTimeMillis();
        long l = (endTime - startTime) / 1000;
        System.out.println(l);
    }

    @Test
    public void startTest(){
        String val = "86";
        RedisUtils.getInstance().setValue("AE86",val);
        String ae86 = RedisUtils.getInstance().getValue("AE86");
        logger.info("----------");
        logger.info(ae86);
        Assert.assertEquals(val, ae86);
        logger.info("----------");
    }
}
