package com.huntkey.rx.sceo.login;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.common.entity.EmailVo;
import com.huntkey.rx.sceo.login.service.CommonService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lulx on 2017/12/25 0025 下午 16:03
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BizLoginAuthenticationApplication.class)
public class CommonServiceTest {
    private static Logger logger  = LoggerFactory.getLogger(CommonServiceTest.class);

    @Autowired
    CommonService commonService;

    @Test
    public void sendMsgTest(){
        Result result = commonService.sendMsg("17730212236", "我在测试"+System.currentTimeMillis());
        logger.info(result.toString());
        Assert.assertEquals(Result.RECODE_SUCCESS, result.getRetCode());
    }

    @Test
    public void sendMail(){
        EmailVo vo = new EmailVo();
        vo.setMailContent("邮件内容：我在测试"+System.currentTimeMillis());
        vo.setMailSubject("主题：测试邮件");
        //List<String> recipient = new ArrayList<String>();
        //recipient.add("lulx@huntkey.net");
        //vo.setMailRecipient(recipient);
        vo.addMailRecipient("lulx@huntkey.net");
        vo.addMailCopyRecipient("zhengbinb@huntkey.net");
        Result result = commonService.sendEmail(vo);
        logger.info(result.toString());
        Assert.assertEquals(Result.RECODE_SUCCESS, result.getRetCode());
    }
}
