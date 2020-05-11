package com.atguigu.controller;

import com.atguigu.response.RetVal;
import com.atguigu.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/oss")
@CrossOrigin
public class OssController {
    @Autowired
    private OssService ossService;

    //1.上传文件
    @PostMapping("uploadFile")
    public RetVal uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
       String url=ossService.uploadFile(file);
       return RetVal.success().data("url",url);
    }
    //2.删除文件
    @PostMapping("deleteFile")
    public RetVal deleteFile(String fileName){
       boolean flag= ossService.deleteFile(fileName);
       return RetVal.success();
    }
}
