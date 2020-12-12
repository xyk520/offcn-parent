package com.offcn.common.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OSSTemplate {

    private String endpoint; // "http://oss-cn-hangzhou.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    private String accessKeyId; // "<yourAccessKeyId>";
    private String accessKeySecret; // "<yourAccessKeySecret>";
    private String bucketName;
    private String bucketDomain;

    /**
     * @Author dsy
     * @Date 2020/12/8 11:05
     * @Description 上传文件
     * @Param [inputStream 文件流, fileName 文件名]
     * @Return java.lang.String 文件在url路径
     * @Since version-1.0
     */
    public String upload(InputStream inputStream,String fileName){
        // 由于未来项目中文件比较多，管理不方便，因此每天创建一个目录，存放当天的文件
        DateFormat dateFormar = new SimpleDateFormat("yyyy-MM-dd");
        String dirName = dateFormar.format(new Date());
        // 避免文件名重复，需要在文件名前加前缀 UUID
        fileName = UUID.randomUUID().toString().replace("-","") + "_" + fileName;
        // oss的客户端
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 上传
         ossClient.putObject(bucketName, "pic/" + dirName + "/" + fileName, inputStream);
         // 上传之后文件的路径
        String url =  bucketDomain + "/pic/" + dirName + "/" + fileName;
        ossClient.shutdown();
        System.out.println("文件存储的路径是:" + url);
        return url;

    }

}
