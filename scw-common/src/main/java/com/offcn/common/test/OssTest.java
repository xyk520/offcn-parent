package com.offcn.common.test;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class OssTest {
    public static void main(String[] args) throws FileNotFoundException {
        // Endpoint以北京为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = "LTAI4G22TuASRy4LBXtvxKNj";
        String accessKeySecret = "5bbYuwKMOAVS7ZVMh6LtxCPoXNO3a5";
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 创建OSSClient实例。
        PutObjectRequest putObjectRequest = new PutObjectRequest("20201209-xyk", "20201209.jpg", new File("D:\\xyk\\Pictures\\Saved Pictures\\300458.jpg"));

       /* OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 上传文件流。
        InputStream inputStream = new FileInputStream(new File("D:\\007.jpg"));
        ossClient.putObject("offcn20200330", "pic/008.jpg", inputStream);
        // 关闭OSSClient。
        ossClient.shutdown();*/
        ossClient.putObject(putObjectRequest);

        System.out.println("测试完成");
    }
}
