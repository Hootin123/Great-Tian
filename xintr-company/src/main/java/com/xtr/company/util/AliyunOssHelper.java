package com.xtr.company.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * Created by ali_j2 on 2016.07.08 008.
 */
public class AliyunOssHelper {
    public static String keyId ="we7s7ph6fXs1PpjN";
    public static String keySecret ="KnNEE2iJpaVq3mw5aCd7CSrHfnmEiP";
    public static String endpoint ="http://oss-cn-hangzhou.aliyuncs.com";
    public static String bucketNameFile ="xintairuan-file";
    public static String bucketNameImg ="xintairuan-img";

    /**
     * 上传文件到阿里云附件服务器
     * @param file 上传文件
     * @param key 文件名
     * @param bucketName 目录名
     * @return
     */
    public static boolean putObject(MultipartFile file, String key, String bucketName) {

        try {
            // 初始化OSSClient
            OSSClient client = new OSSClient(endpoint,keyId, keySecret);
            // 获取指定文件的输入流
            InputStream content = file.getInputStream();
            // 创建上传Object的Metadata
            ObjectMetadata meta = new ObjectMetadata();
            // 必须设置ContentLength
            meta.setContentLength(file.getSize());
            // 上传Object.
            PutObjectResult result = client.putObject(bucketName, key, content, meta);
            String bakString=result.getETag();
            return true;
        } catch (Exception e) {

            String bakString=e.getMessage();
            return false;
        }



    }
}
