package com.huntkey.rx.sceo.login.controller;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.edm.entity.GlobalareaEntity;
import com.huntkey.rx.sceo.common.entity.AreaVo;
import com.huntkey.rx.sceo.login.service.AreaService;
import com.huntkey.rx.sceo.method.register.plugin.annotation.MethodRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by caojq on 2017/12/1.
 */
@RestController
@RequestMapping("/area")
public class AreaController {

    private static Logger logger = LoggerFactory.getLogger(AreaController.class);

    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/getProvinces", method = RequestMethod.GET)
    @MethodRegister(
            edmClass = "area",
            methodCate = "ID系统",
            methodDesc = "获取所有省份"
    )
    public Result getProvinces() {
        List<GlobalareaEntity> list = areaService.getProvinces();
        Result result = new Result();
        result.setData(list);
        result.setRetCode(Result.RECODE_SUCCESS);
        return result;
    }

    @RequestMapping(value = "/getCitiesByProvince/{pid}", method = RequestMethod.GET)
    @MethodRegister(
            edmClass = "area",
            methodCate = "ID系统",
            methodDesc = "根据省份获取地市"
    )
    public Result getCityByProvince(@PathVariable String pid) {
        List<AreaVo> list = areaService.getCityByProvince(pid);
        Result result = new Result();
        result.setData(list);
        result.setRetCode(Result.RECODE_SUCCESS);
        return result;
    }


}
