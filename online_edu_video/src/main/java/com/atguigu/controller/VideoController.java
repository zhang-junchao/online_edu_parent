package com.atguigu.controller;

import com.atguigu.response.RetVal;
import com.atguigu.service.VideoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/aliyun")
@CrossOrigin
public class VideoController {
    @Autowired
    private VideoService videoService;
    //1.视频上传
    @PostMapping("uploadAliyunVideo")
    public RetVal uploadAliyunVideo(MultipartFile file){
        String videoId=videoService.uploadAliyunVideo(file);
        return RetVal.success().data("videoId",videoId);
    }
    //2.删除单个视频
    @DeleteMapping("{videoId}")
    public RetVal deleteSingleVideo(@PathVariable("videoId") String videoId){
        videoService.deleteSingleVideo(videoId);
        return RetVal.success();
    }
    //3.删除多个视频
    @DeleteMapping("deleteMultiVideo")
    public RetVal deleteMultiVideo(@RequestParam("videoIdList") List<String> videoIdList){
        String videoList = StringUtils.join(videoIdList, ",");
        videoService.deleteSingleVideo(videoList);
        return RetVal.success();
    }
    //4.获取播放凭证
    @GetMapping("getVideoPlayAuth/{videoId}")
    public RetVal getVideoPlayAuth(@PathVariable String videoId){
        String playAuth=videoService.getVideoPlayAuth(videoId);
        return RetVal.success().data("playAuth",playAuth);
    }
}
