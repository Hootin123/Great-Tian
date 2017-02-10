package com.xtr.comm.util;

import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.oss.AliOss;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/9/7 9:40
 */
public class UploadUtils {

    /**
     * 上传图片
     *
     * @param multipartFile
     * @param type
     * @return
     * @throws Exception
     */
    public static String upload(MultipartFile multipartFile, String type) throws Exception {
        if (multipartFile != null) {
            String fileName = multipartFile.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            String newFileName = UUID.randomUUID().toString() + "." + suffix;
            //验证上传文件类型是否符合规范,false代表不符合
            boolean checkFlag = false;
            if (!StringUtils.isStrNull(type) && "img".equals(type)) {
                String[] filenames = {"jpg", "png", "jpeg", "gif"};
                for (String filenameCheck : filenames) {
                    if (filenameCheck.equalsIgnoreCase(suffix)) {
                        checkFlag = true;
                        break;
                    }
                }
            } else if (!StringUtils.isStrNull(type) && "excel".equals(type)) {
                String[] filenames = {"xls", "xlsx"};
                for (String filenameCheck : filenames) {
                    if (filenameCheck.equalsIgnoreCase(suffix)) {
                        checkFlag = true;
                        break;
                    }
                }
            }else{
                checkFlag = true;
            }
            if (!checkFlag) {
                throw new BusinessException("上传文件类型不正确");
            }
            long size = multipartFile.getSize();
            size = size / 1024 / 1024;
            if (size >= 2) {
                throw new BusinessException("上传文件不能超过2M");
            }
            //上传图片
            boolean result = AliOss.uploadFile(multipartFile.getInputStream(), newFileName, PropertyUtils.getString("oss.bucketName.img"));
            if (result) {
                return newFileName;
            } else {
                throw new Exception("上传文件失败");
            }
        } else {
            throw new Exception("上传文件为空");
        }
    }

    /**
     * 压缩图片(太慢了,不能用)
     *
     * @param multipartFile
     * @param suffix
     * @return
     * @throws Exception
     */
    private static InputStream zipWidthHeightImageFile(MultipartFile multipartFile, String suffix) throws Exception {
        Image srcFile = ImageIO.read(multipartFile.getInputStream());
        double rate = (double) 102400 / (double) (multipartFile.getSize());
        int width = (int) (srcFile.getWidth(null) * rate);
        int height = (int) (srcFile.getHeight(null) * rate);
        BufferedImage tag = null;
        if ("png".equals(suffix)) {
            tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        } else {
            tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }
        tag.getGraphics().drawImage(srcFile.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        tag.flush();
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
        ImageIO.write(tag, suffix, imOut);
        return new ByteArrayInputStream(bs.toByteArray());
    }
}
