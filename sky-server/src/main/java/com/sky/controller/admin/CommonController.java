package com.sky.controller.admin;/*
 *  @author 阮艳瑞
 *  @version 2.0
    通用接口
 */

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@SuppressWarnings("all")
@RestController
@Api(tags = "通用接口")
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传,{}",file);




        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            ///截取原始文件名的后缀dfdfdf. png
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名称
            String ObjectName = UUID.randomUUID().toString()+ extension;
            //文件的请求路径
            String filepath = aliOssUtil.upload(file.getBytes(), ObjectName);
            return Result.success(filepath);
        } catch (IOException e) {
           log.info("文件上传失败:{}",e);
        }


        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
