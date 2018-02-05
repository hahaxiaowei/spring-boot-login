package com.huntkey.rx.sceo.login.service.impl;

import com.github.tobato.fastdfs.domain.MateData;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.DefaultFastFileStorageClient;
import com.huntkey.rx.edm.entity.PeopleEntity;
import com.huntkey.rx.sceo.login.aop.DynamicDataSource;
import com.huntkey.rx.sceo.login.service.UploadImageService;
import com.huntkey.rx.sceo.orm.service.OrmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by caojq on 2017/11/23.
 */
@Service
public class UploadImageServiceImpl implements UploadImageService {
    private static Logger logger = LoggerFactory.getLogger(UploadImageServiceImpl.class);

    @Value("${nginxIp}")
    String nginxIp;

    @Autowired
    private DefaultFastFileStorageClient defaultFastFileStorageClient;

    @Autowired
    private OrmService ormService;

    @Override
    @DynamicDataSource(isDefaultSource = true)
    public String uploadImage(MultipartFile upLoadFile, String userId) {
        //上传图片
        StorePath path = upload(upLoadFile);
        //更新imageUrl入库
        PeopleEntity peopleEntity = new PeopleEntity();
        peopleEntity.setEpeo_photourl(path.getFullPath());
        peopleEntity.setId(userId);
        try {
            int update = ormService.updateSelective(peopleEntity);
            String abpath = path.getFullPath();
            logger.info("上传图片成功！ update：" + update + "abpath：" + abpath);
            upLoadFile.getInputStream().close();
            return abpath;
        } catch (Exception e) {
            logger.error("更新图片失败！ " + e);
            throw new RuntimeException(e);
        }

    }

    public StorePath upload(MultipartFile upLoadFile) {
        InputStream inputStream = null;
        try {
            inputStream = upLoadFile.getInputStream();
            long fileSize = inputStream.available();
            String fileExtName = "jpg";
            Set<MateData> metaDataSet = new HashSet<MateData>();
            metaDataSet.add(new MateData("width", "800"));
            metaDataSet.add(new MateData("bgcolor", "FFFFFF"));
            metaDataSet.add(new MateData("author", "FirstMateData"));
            StorePath path = defaultFastFileStorageClient.uploadImageAndCrtThumbImage(inputStream, fileSize, fileExtName, metaDataSet);
            return path;
        } catch (IOException e) {
            logger.error("上传图片失败！ " + e);
            throw new RuntimeException(e);
        }
    }

}
