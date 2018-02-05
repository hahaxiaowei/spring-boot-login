package com.huntkey.rx.sceo.login.service;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.edm.entity.PeopleEntity;
import com.huntkey.rx.sceo.common.entity.EnterpriseVo;
import org.springframework.stereotype.Service;

/**
 * Created by caojq on 2017/11/21.
 */
@Service
public interface RegisterService {

    /**
     * 添加用户信息
     *
     * @param peopleEntity
     */
    Result addUserInfo(PeopleEntity peopleEntity);


    /**
     * 验证码校验
     * @param verificationcode
     * @param epeoTel
     * @return
     */
    Boolean isVerificatCorrect(String verificationcode,String epeoTel);

    /**
     * 根据手机号或者邮箱生成6位数字验证码
     *
     * @param phoneNumOrEmail
     */
    Result getVerificatCode(String phoneNumOrEmail);

    /**
     * 判断用户的手机号是否已注册
     *
     * @param epeoTel
     * @param peopleId
     * @return
     */
    Result selectPeopleByTelPhone(String epeoTel, String peopleId);

    /**
     * 判断组织结构代码是否存在
     *
     * @param organizationCode
     * @return
     */
    Boolean isOrganizationCodeExist(String organizationCode);

    /**
     * 判断企业全称是否存在
     *
     * @param enteNameCn
     * @return
     */
    Boolean isEnterpriseFullNameExist(String enteNameCn);

    /**
     * 判断企业Sceo_Url网址是否唯一
     *
     * @param ente_sceo_url
     * @return
     */
    Boolean isSceoUrlExist(String ente_sceo_url);

    /**
     * 添加企业信息
     *
     * @param enterpriseVo
     */
    Result addEnterPrise(EnterpriseVo enterpriseVo, String auth);

    /**
     * 初始化企业信息
     * @param userId 用户ID
     * @param enterpriseId 企业ID
     * @return
     */
    Result initEnterPriseInfo(String userId, String enterpriseId);
}
