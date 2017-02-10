package com.xtr.comm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;


/**
 * 消息property工具类
 *
 * @author 张峰
 */
public class MsgPropertyUtil {

    private Properties properties;
    private static Logger logger = LoggerFactory.getLogger(MsgPropertyUtil.class);

    public MsgPropertyUtil(String fileName) {
        String filePath = "/msg/" + fileName;
        try {
            properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource(filePath));
        } catch (IOException e) {
            logger.error("消息文件加载异常！", e);
        }
    }

    public String getMsg(String key, Object... params) {
        String msgContent = null;
        try {
            msgContent = properties.getProperty(key);
        } catch (Exception e) {
            msgContent = "发生未知错误，请联系管理员";
            logger.error("没有对应key:!" + key, e);
        }
        String result = "";
        if (!StringUtils.isStrNull(msgContent)) {
            result = MessageFormat.format(msgContent, params);
        }
        return result;
    }
}

