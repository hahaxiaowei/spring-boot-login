package com.huntkey.rx.sceo.login.service;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.edm.entity.PeopleEntity;
import com.huntkey.rx.sceo.common.entity.UserInfo;
import org.springframework.stereotype.Service;

/**
 * Created by lulx on 2018/1/8 0008 下午 16:53
 */
@Service
public abstract class LoginHandler {

    protected LoginHandler nextHandler;

    public LoginHandler getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(LoginHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract Result handler(UserInfo userInfo);

    public Result getUnSuccess() {
        Result result = new Result();
        result.setRetCode(Result.RECODE_VALIDATE_ERROR);
        result.setErrMsg("用户名或密码不正确！");
        return result;
    }

    public Result getSuccess(PeopleEntity people) {
        Result result = new Result();
        result.setData(people);
        return result;
    }

    public Result getResult(PeopleEntity people, UserInfo userInfo, LoginHandler handler) {
        if (StringUtil.isNullOrEmpty(people)) {
            if (StringUtil.isNullOrEmpty(handler)) {
                return getUnSuccess();
            }
            return handler.handler(userInfo);
        } else if (people.getEpeo_password().equals(userInfo.getPassword())) {
            return getSuccess(people);
        } else {
            return getUnSuccess();
        }
    }

    /**
     * 用户名格式校验 是否属于属于当前处理类
     *
     * @param userName 手机号、邮箱、身份证、锐信号
     * @return
     */
    public abstract boolean preHandler(String userName);
}
