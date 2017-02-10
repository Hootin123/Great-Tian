package com.xtr.comm.util;

import com.xtr.comm.basic.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>配置文件读取类</p>
 *
 * @author 任齐
 * @createTime: 2016/9/8 15:14.
 */
public class PropUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(PropUtils.class);

    public static Config load(String location){
        if(location.startsWith("classpath:")){
            return loadClassPath(location);
        } else if (location.startsWith("file:")) {
            location = location.substring("file:".length());
            return load(new File(location));
        }
        return loadClassPath(location);
    }

    /**
     * 在classpath下查找配置文件
     * @param location
     * @return
     */
    private static Config loadClassPath(String location){
        location = location.substring("classpath:".length());
        if (location.startsWith("/")) {
            location = location.substring(1);
        }
        try {
            InputStream is = getDefault().getResourceAsStream(location);
            LOGGER.info("Load config [classpath:" + location + "]");
            return loadInputStream(is, location);
        } catch (Exception e){
            LOGGER.error("配置文件加载失败", e);
        }
        return null;
    }

    // 从 File 载入
    public static Config load(File file) {
        try {
            LOGGER.info("Load config [file:" + file.getPath() + "]");
            return loadInputStream(new FileInputStream(file), file.getName());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Config loadInputStream(InputStream is, String location) {
        if (is == null) {
            throw new IllegalStateException("InputStream not found: " + location);
        }
        location = location.toLowerCase();
        try {
            Properties config = new Properties();
            config.load(is);
            return new Config(config);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            if(null != is){
                try {
                    is.close();
                } catch (Exception e){}
            }
        }
    }

    private static ClassLoader getDefault() {
        ClassLoader loader = null;
        try {
            loader = Thread.currentThread().getContextClassLoader();
        } catch (Exception e) {
        }
        if (loader == null) {
            loader = Config.class.getClassLoader();
            if (loader == null) {
                try {
                    // getClassLoader() returning null indicates the bootstrap ClassLoader
                    loader = ClassLoader.getSystemClassLoader();
                } catch (Exception e) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return loader;
    }

}
