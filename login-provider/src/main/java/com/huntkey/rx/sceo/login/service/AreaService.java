package com.huntkey.rx.sceo.login.service;

import com.huntkey.rx.edm.entity.GlobalareaEntity;
import com.huntkey.rx.sceo.common.entity.AreaVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by caojq on 2017/12/1.
 */
@Service
public interface AreaService {

    /**
     * 获取所有省份
     */
    List<GlobalareaEntity> getProvinces();

    /**
     * 根据省份获取地市
     */
    List<AreaVo> getCityByProvince(String pid);
}
