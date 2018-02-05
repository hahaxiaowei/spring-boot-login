package com.huntkey.rx.sceo.login.controller;

import com.huntkey.rx.commons.utils.rest.Result;
import com.huntkey.rx.sceo.login.service.UploadImageService;
import com.huntkey.rx.sceo.method.register.plugin.annotation.MethodRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by cjq on 2017/11/23.
 */
@RestController
@RequestMapping("/upload")
public class UploadImageController {

    @Autowired
    private UploadImageService uploadImageService;

    /**
     * 上传个人图片
     */
    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
    @MethodRegister(
            edmClass = "people",
            methodCate = "ID系统",
            methodDesc = "上传个人图片"
    )
    public Result uploadImg(@RequestParam("file") MultipartFile file, String userId) {
        Result result = new Result();
        String path = uploadImageService.uploadImage(file, userId);
        result.setRetCode(Result.RECODE_SUCCESS);
        result.setData(path);
        return result;
    }



}
