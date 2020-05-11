package com.atguigu.service.impl;

import com.atguigu.oss.OssTemplate;
import com.atguigu.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Autowired
    private OssTemplate ossTemplate;
    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        //文件的原始名称 2.jpg
        String originalFilename = file.getOriginalFilename();
        //b0e877b9-9cf2-4a30-b605-cb10669943482.jpg
        String fileName=UUID.randomUUID().toString().replaceAll("-","")+originalFilename;
        //需要返回一个url
        String retVal = ossTemplate.upload(fileName, inputStream);
        return retVal;
    }

    @Override
    public boolean deleteFile(String fileName) {
        ossTemplate.deleteSingleFile(fileName);
        return true;
    }
}
