package com.huntkey.rx.sceo.login;

import com.huntkey.rx.sceo.login.service.MagnetService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by lulx on 2017/12/27 0027 下午 15:40
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BizLoginAuthenticationApplication.class)
public class MagnetServiceTest {
    private static Logger logger = LoggerFactory.getLogger(MagnetServiceTest.class);

    @Autowired
    private MagnetService magnetService;

    @Test
    public void aopTest(){
        magnetService.test();
    }
}
