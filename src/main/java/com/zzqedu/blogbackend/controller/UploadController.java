package com.zzqedu.blogbackend.controller;

import com.zzqedu.blogbackend.util.QiniuUtils;
import com.zzqedu.blogbackend.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.UUID;

@RestController
public class UploadController {

    @Resource
    QiniuUtils qiniuUtils;


    @PostMapping("/upload")
    public Result upload(@RequestParam("image")MultipartFile file) {
        // 原始文件名称
        String originalFilename = file.getOriginalFilename();
        // 唯一文件名称
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfter(originalFilename, ".");

        boolean upload = qiniuUtils.upload(file, fileName);
        if(upload) {
            return Result.success(QiniuUtils.url + fileName);
        }
        return Result.fail(20001,"上传失败");
    }

}
