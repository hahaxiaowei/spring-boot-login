package com.huntkey.rx.sceo.login.service.impl;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.edm.entity.PeopleEntity;
import com.huntkey.rx.sceo.common.entity.UserInfo;
import com.huntkey.rx.sceo.login.service.LoginHandler;
import com.huntkey.rx.sceo.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 锐信号
 * Created by lulx on 2018/1/9 0009 上午 9:13
 */
@Service
public class RxhLogin extends LoginHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private IdCardLogin idCardLogin;

    @Override
    public Result handler(UserInfo userInfo) {
        PeopleEntity epeoRxnbr = null;
        if (preHandler(userInfo.getPhone())) {
            epeoRxnbr = userService.findPeopleByKey("epeo_rxnbr", userInfo.getPhone());
        }
        return getResult(epeoRxnbr, userInfo, idCardLogin);
    }

    @Override
    public boolean preHandler(String userName) {
        return true;
    }
}
