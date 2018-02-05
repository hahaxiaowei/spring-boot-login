package com.huntkey.rx.sceo.login.service.impl;

import com.alibaba.fastjson.JSON;
import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.commons.utils.string.StringUtil;
import com.huntkey.rx.edm.entity.JobpositionEntity;
import com.huntkey.rx.edm.entity.MagnetEntity;
import com.huntkey.rx.edm.entity.RposRposMagnetSetaEntity;
import com.huntkey.rx.sceo.common.constant.Constant;
import com.huntkey.rx.sceo.common.constant.MagnetType;
import com.huntkey.rx.sceo.common.entity.MagnetSetaVo;
import com.huntkey.rx.sceo.common.entity.MagnetVo;
import com.huntkey.rx.sceo.common.utils.UserUtil;
import com.huntkey.rx.sceo.login.aop.DynamicDataSource;
import com.huntkey.rx.sceo.login.service.MagnetService;
import com.huntkey.rx.sceo.orm.common.model.OrmParam;
import com.huntkey.rx.sceo.orm.service.OrmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lulx on 2017/12/27 0027 上午 9:41
 */
@Service
public class MagnetServiceImpl implements MagnetService {

    private static Logger logger = LoggerFactory.getLogger(MagnetServiceImpl.class);

    @Autowired
    private OrmService ormService;

    @Override
    @DynamicDataSource()
    public Result adjustMagnetsSort(List<String> ids, String type) {
        Result result = new Result();
        try {
            if (ids.size() == 0) {
                result.setErrMsg("无数据");
                return result;
            }
            RposRposMagnetSetaEntity rposMagnetSetaEntity = null;
            for (int i = 0; i < ids.size(); i++) {
                rposMagnetSetaEntity = ormService.load(RposRposMagnetSetaEntity.class, ids.get(i));
                rposMagnetSetaEntity.setRpos_mdsort(i);
                rposMagnetSetaEntity.setRpos_magntype(type);
                ormService.updateSelective(rposMagnetSetaEntity);
            }
        } catch (Exception e) {
            logger.error("adjustMagnetsSort error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("queryPeopleInfo error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource()
    public Result moveInMetroStart(String id, String type) {
        Result result = new Result();
        try {
            result.setData(updateRposRposMagnetSetaEntity(id, type));
        } catch (Exception e) {
            logger.error("moveInMetroStart error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("moveInMetroStart error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource()
    public Result moveOutMetroStart(String id, String type) {
        Result result = new Result();
        try {
            result.setData(updateRposRposMagnetSetaEntity(id, type));
        } catch (Exception e) {
            logger.error("moveOutMetroStart error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("moveOutMetroStart error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource()
    public Result addMagnet(MagnetEntity magnetEntity) {
        Result result = new Result();
        try {
            result.setData(ormService.insertSelective(magnetEntity));
        } catch (Exception e) {
            logger.error("addMagnet error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("addMagnet error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource()
    public Result delMagnet(String id) {
        Result result = new Result();
        try {
            result.setData(ormService.delete(MagnetEntity.class, id));
        } catch (Exception e) {
            logger.error("delMagnet error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("delMagnet error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource()
    public Result updateMagnet(MagnetEntity magnetEntity) {
        Result result = new Result();
        try {
            result.setData(ormService.updateSelective(magnetEntity));
        } catch (Exception e) {
            logger.error("updateMagnet error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("updateMagnet error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource()
    public Result getMagnet(String id) {
        Result result = new Result();
        try {
            result.setData(ormService.load(MagnetEntity.class, id));
        } catch (Exception e) {
            logger.error("getMagnet error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("getMagnet error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource()
    public Result getJobMagnetList(String jobId) {
        Result result = new Result();
        try {
            JobpositionEntity jobpositionEntity = ormService.load(JobpositionEntity.class, jobId);
            List<RposRposMagnetSetaEntity> rposRposMagnetSetaEntities = jobpositionEntity.loadRpos_magnet_set();
            MagnetSetaVo magnetSetaVo = new MagnetSetaVo();
            if (StringUtil.isNullOrEmpty(rposRposMagnetSetaEntities) || rposRposMagnetSetaEntities.size() == 0) {
                result.setData(magnetSetaVo);
            }
            MagnetVo magnetVo = null;
            for (RposRposMagnetSetaEntity entity : rposRposMagnetSetaEntities) {
                magnetVo = new MagnetVo();
                magnetVo.setMagnetSetaEntity(entity);
                MagnetEntity magnetEntity = entity.loadRpos_magnet();
                magnetVo.setMagnetEntity(magnetEntity);
                if (MagnetType.Magnet.getType().equals(entity.getRpos_magntype())) {
                    //驾驶舱
                    magnetSetaVo.addMagnetSetaDashboard(magnetVo);
                } else if (MagnetType.MetroStart.getType().equals(entity.getRpos_magntype())) {
                    // 开始启动
                    magnetSetaVo.addMagnetSetaStart(magnetVo);
                } else {
                    logger.error("磁贴错误数据 : {}", JSON.toJSONString(entity));
                }
            }
            Collections.sort(magnetSetaVo.getMagnetSetaDashboard(), new Comparator<MagnetVo>() {
                @Override
                public int compare(MagnetVo left, MagnetVo right) {
                    return left.getMagnetSetaEntity().getRpos_mdsort() - right.getMagnetSetaEntity().getRpos_mdsort();
                }
            });
            result.setData(magnetSetaVo);
        } catch (Exception e) {
            logger.error("getJobMagnetList error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("getJobMagnetList error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource()
    public Result getMagnetList(String name, String layout, String local) {
        Result result = new Result();
        try {
            OrmParam ormParam = new OrmParam();
            List<String> str = new ArrayList<String>();
            if (!StringUtil.isNullOrEmpty(name)) {
                str.add(ormParam.getMatchMiddleXML("magn_name", name));
            }
            if (!StringUtil.isNullOrEmpty(layout)) {
                str.add(ormParam.getMatchMiddleXML("magn_layout", layout));
            }
            if (!StringUtil.isNullOrEmpty(local)) {
                str.add(ormParam.getMatchMiddleXML("magn_local", local));
            }
            String whereExp = OrmParam.and(str);
            ormParam.setWhereExp(whereExp);
            List<MagnetEntity> magnetEntities = ormService.selectBeanList(MagnetEntity.class, ormParam);
            result.setData(magnetEntities);
        } catch (Exception e) {
            logger.error("getMagnetList error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("getMagnetList error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource()
    public Result addMagnetSet(RposRposMagnetSetaEntity rposMagnetSetaEntity) {
        Result result = new Result();
        try {
            rposMagnetSetaEntity.setPid(UserUtil.getPosition().getId());
            rposMagnetSetaEntity.setClassName(Constant.CLASS_NAME_JOBPOSITION);
            rposMagnetSetaEntity.setCreuser(UserUtil.getPosition().getId());
            result.setData(ormService.insert(rposMagnetSetaEntity));
        } catch (Exception e) {
            logger.error("addMagnetSet error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("addMagnetSet error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource()
    public Result delMagnetSet(String id) {
        Result result = new Result();
        try {
            result.setData(ormService.delete(RposRposMagnetSetaEntity.class, id));
        } catch (Exception e) {
            logger.error("delMagnetSet error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("delMagnetSet error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource()
    public Result updateMagnetSet(RposRposMagnetSetaEntity rposMagnetSetaEntity) {
        Result result = new Result();
        try {
            result.setData(ormService.updateSelective(rposMagnetSetaEntity));
        } catch (Exception e) {
            logger.error("updateMagnetSet error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("updateMagnetSet error :" + e.getMessage());
        }
        return result;
    }

    @Override
    @DynamicDataSource()
    public Result getMagnetSet(String id) {
        Result result = new Result();
        try {
            result.setData(ormService.load(RposRposMagnetSetaEntity.class, id));
        } catch (Exception e) {
            logger.error("getMagnetSet error : " + e.getMessage(), e);
            result.setRetCode(Result.RECODE_ERROR);
            result.setErrMsg("getMagnetSet error :" + e.getMessage());
        }
        return result;
    }

    private Serializable updateRposRposMagnetSetaEntity(String id, String type) throws Exception {
        RposRposMagnetSetaEntity rposMagnetSetaEntity = ormService.load(RposRposMagnetSetaEntity.class, id);
        rposMagnetSetaEntity.setRpos_magntype(type);
        return ormService.updateSelective(rposMagnetSetaEntity);
    }

    @Override
    @DynamicDataSource()
    public void test() {
        logger.info("-----test-------");
    }

}
