package com.huntkey.rx.sceo.login.service.impl;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.edm.entity.PeopleEntity;
import com.huntkey.rx.sceo.common.entity.UserInfo;
import com.huntkey.rx.sceo.login.service.LoginHandler;
import com.huntkey.rx.sceo.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 邮箱登录
 * <p>
 * Created by lulx on 2018/1/8 0008 下午 18:01
 */
@Service
public class MailLogin extends LoginHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private RxhLogin rxhLogin;

    @Override
    public Result handler(UserInfo userInfo) {
        PeopleEntity epeoMail = null;
        if (preHandler(userInfo.getPhone())) {
            epeoMail = userService.findPeopleByKey("epeo_mail", userInfo.getPhone());
        }
        return getResult(epeoMail, userInfo, rxhLogin);
    }

    @Override
    public boolean preHandler(String userName) {
        return userName.contains("@");
    }
}
