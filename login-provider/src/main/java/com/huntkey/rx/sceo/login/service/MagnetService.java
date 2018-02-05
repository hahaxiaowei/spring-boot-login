package com.huntkey.rx.sceo.login.service;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.edm.entity.MagnetEntity;
import com.huntkey.rx.edm.entity.RposRposMagnetSetaEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lulx on 2017/12/27 0027 上午 9:41
 */
@Service
public interface MagnetService {

    /**
     * 调整磁贴顺序
     *
     * @param ids
     * @param type
     * @return
     */
    Result adjustMagnetsSort(List<String> ids, String type);

    /**
     * 调整磁贴位置: 移入开始按钮
     *
     * @param id
     * @return
     */
    Result moveInMetroStart(String id, String type);

    /**
     * 调整磁贴位置: 移出开始按钮
     *
     * @param id
     * @return
     */
    Result moveOutMetroStart(String id, String type);

    /**
     * 新增磁贴
     *
     * @param magnetEntity
     * @return
     */
    Result addMagnet(MagnetEntity magnetEntity);

    /**
     * 删除磁贴
     *
     * @param id
     * @return
     */
    Result delMagnet(String id);

    /**
     * 修改磁贴
     *
     * @param magnetEntity
     * @return
     */
    Result updateMagnet(MagnetEntity magnetEntity);

    /**
     * 查询磁贴
     *
     * @param id
     * @return
     */
    Result getMagnet(String id);

    /**
     * 新增磁贴属性集
     *
     * @param rposMagnetSetaEntity
     * @return
     */
    Result addMagnetSet(RposRposMagnetSetaEntity rposMagnetSetaEntity);

    /**
     * 删除磁贴属性集
     *
     * @param id
     * @return
     */
    Result delMagnetSet(String id);

    /**
     * 修改磁贴属性集
     *
     * @param rposMagnetSetaEntity
     * @return
     */
    Result updateMagnetSet(RposRposMagnetSetaEntity rposMagnetSetaEntity);

    /**
     * 查询磁贴属性集
     *
     * @param id
     * @return
     */
    Result getMagnetSet(String id);

    /**
     * 数据源切换测试
     *
     * @deprecated
     */
    void test();

    /**
     * 查询岗位磁贴列表
     *
     * @param jobId
     * @return
     */
    Result getJobMagnetList(String jobId);

    /**
     * 查询磁贴列表
     *
     * @param name   名称
     * @param layout 布局
     * @param local  是否本地
     * @return
     */
    Result getMagnetList(String name, String layout, String local);
}
