package com.huntkey.rx.sceo.login.service.impl;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.edm.entity.EnterpriseEntity;
import com.huntkey.rx.sceo.common.utils.UserUtil;
import com.huntkey.rx.sceo.login.aop.DynamicDataSource;
import com.huntkey.rx.sceo.login.service.EnterpriseService;
import com.huntkey.rx.sceo.orm.common.model.OrmParam;
import com.huntkey.rx.sceo.orm.service.OrmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lulx on 2018/1/8 0008 上午 10:09
 */
@Service
public class EnterpriseServiceImpl implements EnterpriseService {

    private static Logger logger = LoggerFactory.getLogger(EnterpriseServiceImpl.class);

    @Autowired
    private OrmService ormService;

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result updateEnterpriseInfo(EnterpriseEntity enterprise) {
        Result result = new Result();
        try {
            enterprise.setEnte_dbpassword("");
            enterprise.setModuser(UserUtil.getUserId());
            result.setData(ormService.updateSelective(enterprise));
        } catch (Exception e) {
            logger.error("updateEnterpriseInfo error: " + e.getLocalizedMessage(), e);
            throw new RuntimeException("updateEnterpriseInfo error: " + e.getLocalizedMessage(), e);
        }
        return result;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public Result enterpriseList() {
        Result result = new Result();
        try {
            // TODO
            OrmParam ormParam = new OrmParam();
            result.setData(ormService.selectBeanList(EnterpriseEntity.class, ormParam));
        } catch (Exception e) {
            logger.error("enterpriseList error: " + e.getLocalizedMessage(), e);
            throw new RuntimeException("enterpriseList error: " + e.getLocalizedMessage(), e);
        }
        return result;
    }
}
