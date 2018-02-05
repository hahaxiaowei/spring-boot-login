package com.huntkey.rx.sceo.login.service;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.edm.entity.EnterpriseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by lulx on 2018/1/8 0008 上午 10:08
 */
@Service
public interface EnterpriseService {

    /**
     * 更新企业信息
     *
     * @param enterprise
     * @return
     */
    Result updateEnterpriseInfo(EnterpriseEntity enterprise);

    /**
     * 查询企业列表
     *
     * @return
     */
    Result enterpriseList();
}
