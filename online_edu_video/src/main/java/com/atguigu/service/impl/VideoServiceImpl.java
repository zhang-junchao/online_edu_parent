package com.atguigu.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.service.VideoService;
import com.atguigu.utils.VideoUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class VideoServiceImpl implements VideoService {
    @Value("${aliyun.video.ak}")
    private String accessKeyId;
    @Value("${aliyun.video.sk}")
    private String accessKeySecret;

    @Override
    public String uploadAliyunVideo(MultipartFile file) {
        //11.mp4
        String fileName = file.getOriginalFilename();
        //11
        String title = fileName.substring(0, fileName.indexOf("."));
        String videoId = null;
        try {
            InputStream inputStream = file.getInputStream();
            UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            if (response.isSuccess()) {
                videoId = response.getVideoId();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return videoId;
    }

    @Override
    public void deleteSingleVideo(String videoId) {
        DeleteVideoRequest request = new DeleteVideoRequest();
        //支持传入多个视频ID，多个用逗号分隔
        request.setVideoIds(videoId);
        DefaultAcsClient client = null;
        try {
            client = VideoUtils.initVodClient(accessKeyId, accessKeySecret);
            client.getAcsResponse(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getVideoPlayAuth(String videoId) {
        try {
            DefaultAcsClient client = VideoUtils.initVodClient(accessKeyId, accessKeySecret);
            GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(videoId);
            response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return playAuth;
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        return null;
    }
}
