package com.xtr.comm.basic;

import com.xtr.comm.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <p>配置文件类</p>
 *
 * @author 任齐
 * @createTime: 2016/9/8 15:15.
 */
public class Config {

    private Map<String, String> config = new HashMap<>(32);

    public Config(Map<String, String> config){
        this.config = config;
    }

    public Config(Properties props){
        for (String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            this.config.put(key, value);
        }
    }

    public Map<String, String> asMap(){
        return this.config;
    }

    public String get(String key){
        return this.config.get(key);
    }

    public String get(String key, String defaultValue){
        String value = this.get(key);
        if(null == value && null != defaultValue){
            return defaultValue;
        }
        return value;
    }

    public Integer getInt(String key){
        String value = this.get(key);
        if(null != value && StringUtils.isNumeric(value)){
            return Integer.parseInt(value);
        }
        return null;
    }

    public int getInt(String key, int defaultValue){
        Integer value = this.getInt(key);
        if(null == value){
            return defaultValue;
        }
        return value;
    }

    public Long getLong(String key){
        String value = this.get(key);
        if(null != value && StringUtils.isNumeric(value)){
            return Long.parseLong(value);
        }
        return null;
    }

    public long getLong(String key, long defaultValue){
        Long value = this.getLong(key);
        if(null == value){
            return defaultValue;
        }
        return value;
    }

    public Double getDouble(String key){
        String value = this.get(key);
        if(null != value){
            return Double.parseDouble(value);
        }
        return null;
    }

    public double getDouble(String key, double defaultValue){
        Double value = this.getDouble(key);
        if(null == value){
            return defaultValue;
        }
        return value;
    }

    public Boolean getBoolean(String key){
        String value = this.get(key);
        if(null != value){
            return Boolean.parseBoolean(value);
        }
        return null;
    }

    public boolean getBoolean(String key, boolean defaultValue){
        Boolean value = this.getBoolean(key);
        if(null == value){
            return defaultValue;
        }
        return value;
    }

}
