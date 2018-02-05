package com.huntkey.rx.sceo.login.service.impl;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.edm.entity.PeopleEntity;
import com.huntkey.rx.sceo.common.entity.UserInfo;
import com.huntkey.rx.sceo.login.service.LoginHandler;
import com.huntkey.rx.sceo.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 身份证登录
 * Created by lulx on 2018/1/9 0009 上午 9:16
 */
@Service
public class IdCardLogin extends LoginHandler {

    @Autowired
    private UserService userService;

    @Override
    public Result handler(UserInfo userInfo) {
        PeopleEntity epeoCardNo = null;
        if (preHandler(userInfo.getPhone())) {
            epeoCardNo = userService.findPeopleByKey("epeo_card_no", userInfo.getPhone());
        }
        return getResult(epeoCardNo, userInfo, null);
    }

    @Override
    public boolean preHandler(String userName) {
        return true;
    }
}
