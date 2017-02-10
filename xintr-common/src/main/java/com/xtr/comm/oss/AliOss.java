package com.xtr.comm.oss;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.xtr.comm.util.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * <p>OSS文件上传下载</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/14 11:08
 */
public class AliOss {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliOss.class);

    private static OSSClient client;

    /**
     * 文件上传
     *
     * @return
     */
    public static boolean uploadFile(InputStream inputStream, String fileName,String fileType) {
        try {

            // 创建上传Object的Metadata
            ObjectMetadata meta = new ObjectMetadata();
            // 必须设置ContentLength
            meta.setContentLength(inputStream.available());
            // 上传Object.
            PutObjectResult result = getClient().putObject(fileType, fileName, inputStream, meta);
//            String bakString = result.getETag();
            LOGGER.info(JSON.toJSONString(result));
            return true;
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;

    }

    /**
     * 文件下载
     *
     * @param fileName
     * @param fileType xintairuan-file,xintairuan-img
     * @return
     */
    public static InputStream downloadFile(String fileType, String fileName) {
        OSSObject ossObject = getClient().getObject(fileType, fileName);
        return ossObject.getObjectContent();
    }

    /**
     * 文件下载
     *
     * @param fileName
     * @param fileType xintairuan-file,xintairuan-img
     * @return
     */
    public static byte[] downloadFileByte(String fileType, String fileName) throws IOException {
        OSSObject ossObject = getClient().getObject(fileType, fileName);
        InputStream inputStream = ossObject.getObjectContent();
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inputStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    /**
     * 删除文件
     *
     * @param fileType
     * @param fileName
     */
    public static void deleteFile(String fileType, String fileName) {
        getClient().deleteObject(fileType, fileName);
    }

    /**
     * 获取
     *
     * @return
     */
    public static OSSClient getClient() {
        try {
            if (client == null) {
                // 初始化OSSClient
                client = new OSSClient(PropertyUtils.getString("oss.endpoint"), PropertyUtils.getString("oss.keyId"), PropertyUtils.getString("oss.keySecret"));
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage(), e);
        }
        return client;
    }


    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("C:\\Users\\Administrator\\Desktop\\接口问题.docx");
        // 获取指定文件的输入流
        InputStream inputStream = new FileInputStream(file);
        AliOss.uploadFile(inputStream, "测试.docx","");
    }
}
