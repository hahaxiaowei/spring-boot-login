package com.huntkey.rx.sceo.login.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by caojq on 2017/11/22.
 */
@Service
public interface UploadImageService {


    /**
     * 上传个人照
     */
    String uploadImage(MultipartFile file,String userId);


}
