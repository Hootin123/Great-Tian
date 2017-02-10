package com.xtr.api.dto.salary;

import com.xtr.api.domain.salary.PayCycleBean;

import java.io.Serializable;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/11/3 17:13
 */
public class PayCycleDto extends PayCycleBean implements Serializable {

    /**
     * 工资单标题
     */
    private String excelTitle;

    /**
     * 类型  0：薪资核算  1：急速发工资
     */
    private String type;

    public String getExcelTitle() {
        return excelTitle;
    }

    public void setExcelTitle(String excelTitle) {
        this.excelTitle = excelTitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
