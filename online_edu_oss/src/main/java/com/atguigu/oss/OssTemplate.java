package com.atguigu.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
@Component
public class OssTemplate {
    @Value("${oss.endpoint}")
    public String endpoint;
    @Value("${oss.accessKeyId}")
    public String accessKeyId;
    @Value("${oss.accessKeySecret}")
    public String accessKeySecret;
    @Value("${oss.bucketName}")
    public String bucketName;
    //文件上传
    public String upload(String fileName,InputStream inputStream) throws FileNotFoundException {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, "pic/"+fileName, inputStream);
        // 关闭OSSClient。
        ossClient.shutdown();
        String retUrl="https://"+bucketName+"."+endpoint+"/pic/"+fileName;
        return retUrl;
    }
    //单个文件删除
    public void deleteSingleFile(String fileName) {
        //shift+f6
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, "pic/"+fileName);
        // 关闭OSSClient。
        ossClient.shutdown();
    }
    //批量删除
    public  void deleteMultiFile() {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 删除文件。key等同于ObjectName，表示删除OSS文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        List<String> keys = new ArrayList<String>();
        keys.add("web2.jpg");
        keys.add("2.jpg");
        keys.add("pic/xxx.jpg");
        DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(keys));
        List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
        // 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("ok");
    }

    public static void main(String[] args) {
        System.out.println( );
    }




}
