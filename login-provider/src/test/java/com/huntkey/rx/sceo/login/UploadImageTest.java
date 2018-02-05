package com.huntkey.rx.sceo.login;

import com.github.tobato.fastdfs.domain.MateData;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.DefaultFastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chenfei on 2017/5/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BizLoginAuthenticationApplication.class)
public class UploadImageTest {

    @Autowired
    private DefaultFastFileStorageClient defaultFastFileStorageClient;
    @javax.annotation.Resource
    private ThumbImageConfig thumbImageConfig;

    /**
     * 测试上传文件
     */
    @Test
    public void testUpload() {

        try {
            Resource resource = new ClassPathResource("images/cat1.jpg");
            File file = resource.getFile();

            InputStream inputStream = new FileInputStream(file);
            long fileSize = inputStream.available();
            String fileExtName = "jpg";
            Set<MateData> metaDataSet = new HashSet<MateData>();

            metaDataSet.add(new MateData("width", "800"));
            metaDataSet.add(new MateData("bgcolor", "FFFFFF"));
            metaDataSet.add(new MateData("author", "FirstMateData"));

            StorePath path = defaultFastFileStorageClient.uploadFile(inputStream, fileSize, fileExtName, metaDataSet);
            StorePath.praseFromUrl(path.getFullPath());
            System.out.println(path.getFullPath());//10.3.98.153:22122/group1/M00/00/00/CgNimVoWGb2ASb4WAADJbd1CckA938.jpg
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试图片上传并生成缩略图
     */
    @Test
    public void testUploadImageAndCrtThumbImage() {
        try {
            Resource resource = new ClassPathResource("images/cat.jpg");
            File file = resource.getFile();

            InputStream inputStream = new FileInputStream(file);
            long fileSize = inputStream.available();
            String fileExtName = "jpg";
            Set<MateData> metaDataSet = new HashSet<MateData>();

            metaDataSet.add(new MateData("width", "800"));
            metaDataSet.add(new MateData("bgcolor", "FFFFFF"));
            metaDataSet.add(new MateData("author", "FirstMateData"));

            StorePath path = defaultFastFileStorageClient.uploadImageAndCrtThumbImage(inputStream, fileSize, fileExtName, metaDataSet);
            // 缩略图文件路径
            String thumbPath = thumbImageConfig.getThumbImagePath(path.getPath());
            System.out.println(path.getFullPath());
            System.out.println(thumbPath);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试删除文件
     */
    @Test
    public void testDeleteFile() {

        try {
            Resource resource = new ClassPathResource("images/cat.jpg");
            File file = resource.getFile();

            InputStream inputStream = new FileInputStream(file);
            long fileSize = inputStream.available();
            String fileExtName = "jpg";
            Set<MateData> metaDataSet = new HashSet<MateData>();

            metaDataSet.add(new MateData("width", "800"));
            metaDataSet.add(new MateData("bgcolor", "FFFFFF"));
            metaDataSet.add(new MateData("author", "FirstMateData"));

            StorePath path = defaultFastFileStorageClient.uploadFile(inputStream, fileSize, fileExtName, metaDataSet);
            inputStream.close();
            System.out.println(path.getFullPath());
            defaultFastFileStorageClient.deleteFile(path.getFullPath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
