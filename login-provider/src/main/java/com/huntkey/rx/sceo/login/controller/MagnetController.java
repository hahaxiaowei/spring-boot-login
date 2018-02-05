package com.huntkey.rx.sceo.login.controller;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.edm.entity.MagnetEntity;
import com.huntkey.rx.edm.entity.RposRposMagnetSetaEntity;
import com.huntkey.rx.sceo.common.constant.MagnetType;
import com.huntkey.rx.sceo.common.utils.UserUtil;
import com.huntkey.rx.sceo.login.service.MagnetService;
import com.huntkey.rx.sceo.method.register.plugin.annotation.MethodRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 磁贴管理类
 * Created by lulx on 2017/12/27 0027 上午 9:09
 */
@RestController
@RequestMapping("/magnet")
public class MagnetController {

    private static Logger logger = LoggerFactory.getLogger(MagnetController.class);

    @Autowired
    private MagnetService magnetService;

    /**
     * 调整开始按钮磁贴位置
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/adjustMagnetsSort", method = RequestMethod.POST)
    @MethodRegister(
            edmClass = "magnet",
            methodCate = "ID系统",
            methodDesc = "调整开始按钮磁贴位置"
    )
    public Result adjustMagnetsSort(@RequestBody List<String> ids) {
        Result result = null;
        try {
            result = magnetService.adjustMagnetsSort(ids, MagnetType.MetroStart.getType());
        } catch (Exception e) {
            logger.error("调整开始按钮磁贴位置：" + e.getMessage(), e);
            throw new RuntimeException("调整开始按钮磁贴位置：" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 调整驾驶舱磁贴位置
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/adjustMagnets", method = RequestMethod.POST)
    @MethodRegister(
            edmClass = "magnet",
            methodCate = "ID系统",
            methodDesc = "调整驾驶舱磁贴位置"
    )
    public Result adjustMagnets(@RequestBody List<String> ids) {
        Result result = null;
        try {
            result = magnetService.adjustMagnetsSort(ids, MagnetType.Magnet.getType());
        } catch (Exception e) {
            logger.error("调整驾驶舱磁贴位置：" + e.getMessage(), e);
            throw new RuntimeException("调整驾驶舱磁贴位置：" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 调整磁贴位置: 移入开始按钮
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/moveInMetroStart/{id}", method = RequestMethod.POST)
    @MethodRegister(
            edmClass = "magnet",
            methodCate = "ID系统",
            methodDesc = "调整磁贴位置: 移入开始按钮"
    )
    public Result moveInMetroStart(@PathVariable String id) {
        Result result = null;
        try {
            result = magnetService.moveInMetroStart(id, MagnetType.MetroStart.getType());
        } catch (Exception e) {
            logger.error("调整磁贴位置: 移入开始按钮：" + e.getMessage(), e);
            throw new RuntimeException("调整磁贴位置: 移入开始按钮：" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 调整磁贴位置: 移出开始按钮
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/moveOutMetroStart/{id}", method = RequestMethod.POST)
    @MethodRegister(
            edmClass = "magnet",
            methodCate = "ID系统",
            methodDesc = "调整磁贴位置: 移出开始按钮"
    )
    public Result moveOutMetroStart(@PathVariable String id) {
        Result result = null;
        try {
            result = magnetService.moveOutMetroStart(id, MagnetType.Magnet.getType());
        } catch (Exception e) {
            logger.error("调整磁贴位置: 移出开始按钮：" + e.getMessage(), e);
            throw new RuntimeException("调整磁贴位置: 移出开始按钮：" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 新增磁贴
     *
     * @param magnetEntity
     * @return
     */
    @RequestMapping(value = "/add/magnet", method = RequestMethod.POST)
    @MethodRegister(
            edmClass = "magnet",
            methodCate = "ID系统",
            methodDesc = "新增磁贴"
    )
    public Result addMagnet(@RequestBody MagnetEntity magnetEntity) {
        Result result = null;
        try {
            result = magnetService.addMagnet(magnetEntity);
        } catch (Exception e) {
            logger.error("新增磁贴：" + e.getMessage(), e);
            throw new RuntimeException("新增磁贴：" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 删除磁贴
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/del/magnet/{id}", method = RequestMethod.DELETE)
    @MethodRegister(
            edmClass = "magnet",
            methodCate = "ID系统",
            methodDesc = "删除磁贴"
    )
    public Result delMagnet(@PathVariable String id) {
        Result result = null;
        try {
            result = magnetService.delMagnet(id);
        } catch (Exception e) {
            logger.error("删除磁贴：" + e.getMessage(), e);
            throw new RuntimeException("删除磁贴：" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 修改磁贴
     *
     * @param magnetEntity
     * @return
     */
    @RequestMapping(value = "/update/magnet", method = RequestMethod.PUT)
    @MethodRegister(
            edmClass = "magnet",
            methodCate = "ID系统",
            methodDesc = "修改磁贴"
    )
    public Result updateMagnet(@RequestBody MagnetEntity magnetEntity) {
        Result result = null;
        try {
            result = magnetService.updateMagnet(magnetEntity);
        } catch (Exception e) {
            logger.error("修改磁贴：" + e.getMessage(), e);
            throw new RuntimeException("修改磁贴：" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 查询磁贴
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/magnet/{id}", method = RequestMethod.GET)
    @MethodRegister(
            edmClass = "magnet",
            methodCate = "ID系统",
            methodDesc = "查询磁贴"
    )
    public Result getMagnet(@PathVariable String id) {
        Result result = null;
        try {
            result = magnetService.getMagnet(id);
        } catch (Exception e) {
            logger.error("查询磁贴：" + e.getMessage(), e);
            throw new RuntimeException("查询磁贴：" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 查询岗位磁贴列表
     *
     * @return
     */
    @RequestMapping(value = "/get/jobmagnetlist", method = RequestMethod.GET)
    @MethodRegister(
            edmClass = "magnet",
            methodCate = "ID系统",
            methodDesc = "查询岗位磁贴列表"
    )
    public Result jobMagnetlist() {
        Result result = null;
        try {
            result = magnetService.getJobMagnetList(UserUtil.getPosition().getId());
        } catch (Exception e) {
            logger.error("查询岗位磁贴列表：" + e.getMessage(), e);
            throw new RuntimeException("查询岗位磁贴列表：" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 查询磁贴列表
     *
     * @param name   名称
     * @param layout 布局
     * @param local  是否本地
     * @return
     */
    @RequestMapping(value = "/get/magnetlist", method = RequestMethod.GET)
    @MethodRegister(
            edmClass = "magnet",
            methodCate = "ID系统",
            methodDesc = "查询磁贴列表",
            getReqParamsNameNoPathVariable = {"name", "layout", "local"}
    )
    public Result magnetlist(@RequestParam(required = false) String name, @RequestParam(required = false) String layout
            , @RequestParam(required = false) String local) {
        Result result = null;
        try {
            result = magnetService.getMagnetList(name, layout, local);
        } catch (Exception e) {
            logger.error("查询磁贴列表：" + e.getMessage(), e);
            throw new RuntimeException("查询磁贴列表：" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 新增磁贴属性集
     *
     * @param rposMagnetSetaEntity
     * @return
     */
    @RequestMapping(value = "/add/magnetset", method = RequestMethod.POST)
    @MethodRegister(
            edmClass = "magnet",
            methodCate = "ID系统",
            methodDesc = "新增磁贴属性集"
    )
    public Result addMagnetSet(@RequestBody RposRposMagnetSetaEntity rposMagnetSetaEntity) {
        Result result = null;
        try {
            result = magnetService.addMagnetSet(rposMagnetSetaEntity);
        } catch (Exception e) {
            logger.error("新增磁贴属性集：" + e.getMessage(), e);
            throw new RuntimeException("新增磁贴属性集：" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 删除磁贴属性集
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/del/magnetset/{id}", method = RequestMethod.DELETE)
    @MethodRegister(
            edmClass = "magnet",
            methodCate = "ID系统",
            methodDesc = "删除磁贴属性集"
    )
    public Result delMagnetSet(@PathVariable String id) {
        Result result = null;
        try {
            result = magnetService.delMagnetSet(id);
        } catch (Exception e) {
            logger.error("删除磁贴属性集：" + e.getMessage(), e);
            throw new RuntimeException("删除磁贴属性集：" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 修改磁贴属性集
     *
     * @param rposMagnetSetaEntity
     * @return
     */
    @RequestMapping(value = "/update/magnetset", method = RequestMethod.PUT)
    @MethodRegister(
            edmClass = "magnet",
            methodCate = "ID系统",
            methodDesc = "修改磁贴属性集"
    )
    public Result updateMagnetSet(@RequestBody RposRposMagnetSetaEntity rposMagnetSetaEntity) {
        Result result = null;
        try {
            result = magnetService.updateMagnetSet(rposMagnetSetaEntity);
        } catch (Exception e) {
            logger.error("修改磁贴属性集：" + e.getMessage(), e);
            throw new RuntimeException("修改磁贴属性集：" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * 查询磁贴属性集
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/magnetset/{id}", method = RequestMethod.GET)
    @MethodRegister(
            edmClass = "magnet",
            methodCate = "ID系统",
            methodDesc = "查询磁贴属性集"
    )
    public Result getMagnetSet(@PathVariable String id) {
        Result result = null;
        try {
            result = magnetService.getMagnetSet(id);
        } catch (Exception e) {
            logger.error("查询磁贴属性集：" + e.getMessage(), e);
            throw new RuntimeException("查询磁贴属性集：" + e.getMessage(), e);
        }
        return result;
    }


}
