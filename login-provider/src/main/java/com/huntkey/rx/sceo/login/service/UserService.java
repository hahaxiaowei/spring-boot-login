package com.huntkey.rx.sceo.login.service;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.edm.entity.EpeoEpeoFamSetaEntity;
import com.huntkey.rx.edm.entity.EpeoEpeoStuSetaEntity;
import com.huntkey.rx.edm.entity.EpeoEpeoWorkSetaEntity;
import com.huntkey.rx.edm.entity.PeopleEntity;
import com.huntkey.rx.sceo.common.entity.UserInfo;
import org.springframework.stereotype.Service;

/**
 * Created by lulx on 2017/11/22 0022 下午 16:29
 */
@Service
public interface UserService {
    /**
     * 查询用户信息
     *
     * @param userId 用户ID
     * @return
     */
    Result queryPeopleInfo(String userId);

    /**
     * 查询企业列表
     *
     * @param userId 用户ID
     * @return
     */
    Result queryEnterpriseList(String userId);

    /**
     * 更新用户信息
     *
     * @param peopleEntity
     * @return
     */
    Result updatePeopleInfo(PeopleEntity peopleEntity);

    /**
     * 查询用户企业岗位信息
     *
     * @param enterpriseId
     * @param userId
     * @return
     */
    Result queryEnterprise(String enterpriseId, String userId);

    /**
     * 新增用户信息
     *
     * @param peopleEntity
     * @return
     */
    Result addPeople(PeopleEntity peopleEntity, String enterpriseId);

    /**
     * 根据身份证号查询用户信息
     *
     * @param idCard
     * @return
     */
    Result queryPeopleInfoByIdCard(String idCard);

    /**
     * 入职
     * 更新用户企业圈
     *
     * @param userId       用户ID
     * @param enterpriseId 企业id
     * @return
     */
    Result addPeopleEnteSet(String userId, String enterpriseId);

    /**
     * 离职
     * 更新用户企业圈
     *
     * @param userId       用户ID
     * @param enterpriseId 企业id
     * @return
     */
    Result removePeopleEnteSet(String userId, String enterpriseId);

    /**
     * 增加用户的教育背景
     *
     * @param stuSet
     * @return
     */
    Result addStuSet(EpeoEpeoStuSetaEntity stuSet);

    /**
     * 删除用户的教育背景
     *
     * @param stuSetId
     * @return
     */
    Result delStuSet(String stuSetId);

    /**
     * 更新用户的教育背景
     *
     * @param stuSet
     * @return
     */
    Result modStuSet(EpeoEpeoStuSetaEntity stuSet);

    /**
     * 增加用户的工作经历
     *
     * @param workSet
     * @return
     */
    Result addWorkSet(EpeoEpeoWorkSetaEntity workSet);

    /**
     * 删除用户的工作经历
     *
     * @param workSetId
     * @return
     */
    Result delWorkSet(String workSetId);

    /**
     * 更新用户的工作经历
     *
     * @param workSet
     * @return
     */
    Result modWorkSet(EpeoEpeoWorkSetaEntity workSet);

    /**
     * 根据传入的key值重新people
     *
     * @param key
     * @param val
     * @return
     */
    PeopleEntity findPeopleByKey(String key, String val);

    /**
     * 删除用户的家庭成员
     *
     * @param famSetId
     * @return
     */
    Result delFamSet(String famSetId);

    /**
     * 获取用户的家庭成员
     *
     * @param famSetId
     * @return
     */
    Result getFamSet(String famSetId);

    /**
     * 更新用户的家庭成员
     *
     * @param famSet
     * @return
     */
    Result modFamSet(EpeoEpeoFamSetaEntity famSet);

    /**
     * 增加用户的家庭成员
     *
     * @param famSet
     * @return
     */
    Result addFamSet(EpeoEpeoFamSetaEntity famSet);

    /**
     * 获取家庭成员列表
     *
     * @return
     */
    Result getFamSetList();

    /**
     * 修改密码
     *
     * @param userInfo
     * @return
     */
    Result modPassWord(UserInfo userInfo);

    /**
     * 修改手机号
     *
     * @param userInfo
     * @return
     */
    Result modTel(UserInfo userInfo);

    /**
     * 获取教育背景列表
     *
     * @return
     */
    Result getStuSetList();

    /**
     * 获取教育背景
     *
     * @return
     */
    Result getWorkSetList();

    /**
     * 发送邮箱验证码
     * @param mail
     * @return
     */
    Result mailCode(String mail);

    /**
     * 修改邮箱
     * @param userInfo
     * @return
     */
    Result modMail(UserInfo userInfo);
}
