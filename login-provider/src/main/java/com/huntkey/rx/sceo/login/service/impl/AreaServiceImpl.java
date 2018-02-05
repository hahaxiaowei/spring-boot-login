package com.huntkey.rx.sceo.login.service.impl;

import com.huntkey.rx.edm.entity.GlobalareaEntity;
import com.huntkey.rx.sceo.common.constant.Constant;
import com.huntkey.rx.sceo.common.entity.AreaVo;
import com.huntkey.rx.sceo.login.aop.DynamicDataSource;
import com.huntkey.rx.sceo.login.controller.LoginController;
import com.huntkey.rx.sceo.login.service.AreaService;
import com.huntkey.rx.sceo.orm.common.model.OrmParam;
import com.huntkey.rx.sceo.orm.service.OrmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojq on 2017/12/1.
 */
@Service
public class AreaServiceImpl implements AreaService {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private OrmService ormService;

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public List<GlobalareaEntity> getProvinces() {
        OrmParam ormParam = new OrmParam();
        ormParam.addWhereParam("gare_parent_area", Constant.CONTTRY_CODE);
        String whereExp = "gare_parent_area = #{whereParam.gare_parent_area}";
        ormParam.setWhereExp(whereExp);
        try {
            List<GlobalareaEntity> list = ormService.selectBeanList(GlobalareaEntity.class, ormParam);
            return list;
        } catch (Exception e) {
            logger.error("查找省份错误！" + e);
        }
        return null;
    }

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public List<AreaVo> getCityByProvince(String pid) {
        OrmParam ormParam = new OrmParam();
        ormParam.addWhereParam("gare_parent_area", pid);
        String whereExp = "gare_parent_area = #{whereParam.gare_parent_area}";
        ormParam.setWhereExp(whereExp);
        List<AreaVo> arealist = new ArrayList<AreaVo>();
        try {
            List<GlobalareaEntity> entitylist = ormService.selectBeanList(GlobalareaEntity.class, ormParam);
            for (int i =0;i<entitylist.size();i++){
                AreaVo av = new AreaVo();
                GlobalareaEntity areaEntity = entitylist.get(i);
                av.setId(areaEntity.getId());
                av.setGare_name(areaEntity.getGare_name());
                av.setParentId(areaEntity.getGare_parent_area());
                //如果id不在pid中，则为子节点，否则为父节点
                OrmParam areaormParam = new OrmParam();
                areaormParam.addWhereParam("gare_parent_area", areaEntity.getId());
                String areawhereExp = "gare_parent_area = #{whereParam.gare_parent_area}";
                areaormParam.setWhereExp(areawhereExp);
                List<GlobalareaEntity> list = ormService.selectBeanList(GlobalareaEntity.class, areaormParam);
                if(list.size()>0){
                    av.setChildren(new String [0]);
                }else{
                    av.setChildren(null);
                }
                arealist.add(av);
            }
            return arealist;
        } catch (Exception e) {
            logger.error("查找地市错误！" + e);
        }
        return null;
    }
}
