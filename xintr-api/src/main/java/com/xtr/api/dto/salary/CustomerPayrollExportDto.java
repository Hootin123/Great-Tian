package com.xtr.api.dto.salary;

import java.io.Serializable;

/**
 * <p>工资单导出Dto</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/29 17:28
 */
public class CustomerPayrollExportDto implements Serializable {

    /**
     * 关键字
     */
    private String key;

    /**
     * 类型 0:基本工资 1:津贴 2:奖金
     */
    private Integer type;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
