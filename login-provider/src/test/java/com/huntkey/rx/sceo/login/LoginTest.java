package com.huntkey.rx.sceo.login;

import com.alibaba.fastjson.JSONObject;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.edm.entity.DepttreeEntity;
import com.huntkey.rx.edm.entity.EnterpriseEntity;
import com.huntkey.rx.edm.entity.JobpositionEntity;
import com.huntkey.rx.edm.entity.PeopleEntity;
import com.huntkey.rx.sceo.common.entity.JobpositionVo;
import com.huntkey.rx.sceo.common.entity.UserInfo;
import com.huntkey.rx.sceo.common.utils.EncryptUtil;
import com.huntkey.rx.sceo.login.service.LoginService;
import com.huntkey.rx.sceo.login.service.UserService;
import com.huntkey.rx.sceo.orm.common.model.OrmParam;
import com.huntkey.rx.sceo.orm.service.OrmService;
import jdk.nashorn.internal.scripts.JO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lulx on 2017/11/22 0022 上午 9:28
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BizLoginAuthenticationApplication.class)
public class LoginTest {

    private static Logger logger = LoggerFactory.getLogger(LoginTest.class);

    @Autowired
    LoginService loginService;

    @Test
    public void findTeset() throws Exception {
        Result result = null;
        UserInfo userInfo = new UserInfo();
        userInfo.setPhone("15805515672");
        userInfo.setPassword("12345678cC23");

        PeopleEntity dbuserInfo = loginService.findUser(userInfo.getPhone());
        if (StringUtil.isNullOrEmpty(dbuserInfo)){
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("该用户未注册！");
        }
        //验证用户密码
        String ps = EncryptUtil.encryptPassWord(userInfo.getPassword());
        if(!ps.equals(dbuserInfo.getEpeo_password())){
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("密码不正确！");
        }
        logger.info(dbuserInfo.getEpeo_name_en()+" 登录成功！");
    }

    /*@Test
    public void findEnterpriseList(){
        Result result = null;
        PeopleEntity dbuserInfo = loginService.findUser("183438483");
        if (StringUtil.isNullOrEmpty(dbuserInfo)) {
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("该用户未注册！");
        }
        //查询企业列表
        List<EnterpriseEntity> list = loginService.findEnterpriseList("1b472c0d4b7b4c2eb489187037c33732");
        Map<EnterpriseEntity,Map<DepttreeEntity, JobpositionEntity>> map1= loginService.findDepttree("1b472c0d4b7b4c2eb489187037c33732");
        Map<EnterpriseEntity,List<JobpositionVo>> map = loginService.findJobposition("1b472c0d4b7b4c2eb489187037c33732");
        logger.info("企业列表"+ list.toArray());
        for(EnterpriseEntity enterpriseEntity : list){
            System.out.println(enterpriseEntity.getEnte_name_cn());
        }
        logger.info("部门列表"+map1.toString());
        for(EnterpriseEntity enterpriseEntity:map1.keySet()) {
            for (DepttreeEntity depttreeEntity : map1.get(enterpriseEntity).keySet()){
                System.out.println(depttreeEntity.getMdep_name());
            }
        }
        logger.info("岗位列表"+map.toString());
        for(EnterpriseEntity enterpriseEntity: map.keySet()){
            List<JobpositionVo> list1 = map.get(enterpriseEntity);
            for (JobpositionVo jobpositionVo : list1){
                System.out.println(jobpositionVo.getDepttreeEntity().getMdep_name());
                System.out.println(jobpositionVo.getJobpositionEntity().getRpos_name());
            }
        }
    }*/


}
