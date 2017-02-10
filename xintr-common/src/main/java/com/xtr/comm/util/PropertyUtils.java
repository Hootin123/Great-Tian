package com.xtr.comm.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.*;


/**
 * 属性工具类
 *
 * @author 张峰
 */
public class PropertyUtils extends PropertyPlaceholderConfigurer {

    private static Map<String, String> ctxPropertiesMap;

    @Override
    protected void processProperties(
            ConfigurableListableBeanFactory beanFactoryToProcess,
            Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        ctxPropertiesMap = new HashMap<String, String>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            ctxPropertiesMap.put(keyStr, value);
        }
    }

    public static String getString(String key) {
        if (ctxPropertiesMap == null) {
            ctxPropertiesMap = new HashMap<String, String>();
        }
        return ctxPropertiesMap.get(key);
    }

    public static boolean getBool(String key) {
        String val = getString(key);
        if(null != val && !val.equals("")){
            return Boolean.parseBoolean(val);
        }
        return false;
    }

    /**
     * 根据键值获取Property文件中的值
     *
     * @param key          键值
     * @param defaultValue 默认值
     * @return 根据键值取得的结构
     * @author 张峰
     */
    public static String getString(String key, String defaultValue) {
        if (ctxPropertiesMap == null) {
            ctxPropertiesMap = new HashMap<String, String>();
        }

        String value = ctxPropertiesMap.get(key);

        return (value == null) ? defaultValue : value;
    }

    public static int getIntValue(String key, int defaultValue) {
        try {
            int r = Integer.parseInt(getString(key));
            return r;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static double getDoubleValue(String key, double defaultValue) {
        try {
            double r = Double.valueOf(getString(key));
            return r;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 取出复合表达式的keys
     *
     * @param express
     * @return
     */
    public static Set<String> getkeys(String express) {
        Set<String> selectedkey = new HashSet<String>();
        Set<String> keys = ctxPropertiesMap.keySet();
        for (String item : keys) {
            if (item.matches(express)) {
                selectedkey.add(item);
            }
        }
        return selectedkey;
    }


}

