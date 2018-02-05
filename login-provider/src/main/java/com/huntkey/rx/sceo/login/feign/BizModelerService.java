package com.huntkey.rx.sceo.login.feign;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.login.feign.hystrix.BizModelerServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * , url = "10.3.99.16:2001"
 * Created by lulx on 2017/10/14 0014 上午 10:38
 */
@FeignClient(value = "modeler-provider", fallback = BizModelerServiceHystrix.class)
@Component
public interface BizModelerService {
    /**
     * 获取企业相关信息的编码
     *
     * @param params "edmnEncode":"P",
     *               "edmnType":"3"
     * @return
     */
    @RequestMapping(value = "/numbers", method = RequestMethod.POST)
    Result numbers(@RequestBody Map params);

    /**
     * 创建数据库-初始化表结构
     *
     * @param version 版本 V1.0
     * @param pathMap 数据库信息
     *                "name":"root", 用户名
     *                "dbPassword":"123qwe",
     *                "dataBase":"testAB2C" 数据库名
     * @return
     */
    @RequestMapping(value = "/dbPassive", method = RequestMethod.POST)
    Result dbPassive(@RequestParam(value = "version") String version, @RequestBody Map<String, String> pathMap);
}
