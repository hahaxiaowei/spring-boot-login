package com.huntkey.rx.sceo.login;

import com.huntkey.rx.edm.entity.AreaEntity;
import com.huntkey.rx.edm.entity.GlobalareaEntity;
import com.huntkey.rx.sceo.common.entity.AreaVo;
import com.huntkey.rx.sceo.login.service.AreaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by chenfei on 2017/12/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BizLoginAuthenticationApplication.class)
public class AreaTest {

    private static Logger logger = LoggerFactory.getLogger(AreaTest.class);

    @Autowired
    AreaService areaService;

    /**
     * 获取所有省份
     */
    @Test
    public void testGetProvinces() {
        List<GlobalareaEntity> list = areaService.getProvinces();
        for (GlobalareaEntity area : list) {
            logger.info("------------" + area.getGare_desc());
        }
    }


    /**
     * 根据省份获取地市
     */
    @Test
    public void testGetCityByProvince() {
        List<AreaVo> list = areaService.getCityByProvince("00000000000000000000000000130000");
        for (AreaVo area : list) {
            logger.info("------------" + area.getGare_name());
        }
    }


}
