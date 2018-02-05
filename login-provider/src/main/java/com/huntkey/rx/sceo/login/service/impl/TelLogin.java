package com.huntkey.rx.sceo.login.service.impl;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.edm.entity.PeopleEntity;
import com.huntkey.rx.sceo.common.entity.UserInfo;
import com.huntkey.rx.sceo.login.service.LoginHandler;
import com.huntkey.rx.sceo.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 手机号登录
 * Created by lulx on 2018/1/8 0008 下午 17:00
 */
@Service
public class TelLogin extends LoginHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private MailLogin mailLogin;

    @Override
    public Result handler(UserInfo userInfo) {
        PeopleEntity epeoTel = null;
        if (preHandler(userInfo.getPhone())) {
            epeoTel = userService.findPeopleByKey("epeo_tel", userInfo.getPhone());
        }
        return getResult(epeoTel, userInfo, mailLogin);
    }

    @Override
    public boolean preHandler(String userName) {
        return true;
    }
}
