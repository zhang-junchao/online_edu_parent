package com.atguigu;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.*;

import java.util.List;

public class Test01 {
    public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) throws ClientException {
        String regionId = "cn-shanghai";  // 点播服务接入区域
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

    // 请求示例
    public static void testUpload(String[] argv) throws Exception {
        DefaultAcsClient client = initVodClient("LTAI4FxsK9JSReknyhkHTKSV", "CVKyxxf57WPy49XSxOeCum2B4DiCyd");
        CreateUploadVideoResponse response = new CreateUploadVideoResponse();
        try {

            CreateUploadVideoRequest request = new CreateUploadVideoRequest();
            request.setTitle("上传案例");
            request.setFileName("C:\\work\\online.mp4");
            response = client.getAcsResponse(request);

            System.out.print("VideoId = " + response.getVideoId() + "\n");
            System.out.print("UploadAddress = " + response.getUploadAddress() + "\n");
            System.out.print("UploadAuth = " + response.getUploadAuth() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }


    /*获取播放地址函数*/
    public static void getPlayUrl(String[] argv) throws Exception {
        DefaultAcsClient client = initVodClient("LTAI4FxsK9JSReknyhkHTKSV", "CVKyxxf57WPy49XSxOeCum2B4DiCyd");
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        try {
            GetPlayInfoRequest request = new GetPlayInfoRequest();
            request.setVideoId("337b28c42d6648c8a9b9d11c19d0f44a");
            response = client.getAcsResponse(request);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            //播放地址
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
            }
            //Base信息
            System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }


    /*获取播放凭证函数getVideoPlayAuth*/
    public static void main(String[] argv) throws Exception {
        DefaultAcsClient client = initVodClient("LTAI4FxsK9JSReknyhkHTKSV", "CVKyxxf57WPy49XSxOeCum2B4DiCyd");
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        try {
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId("337b28c42d6648c8a9b9d11c19d0f44a");
            response = client.getAcsResponse(request);
            //播放凭证
            System.out.print("PlayAuth = " + response.getPlayAuth() + "\n");
            //VideoMeta信息
            System.out.print("VideoMeta.Title = " + response.getVideoMeta().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }
}
