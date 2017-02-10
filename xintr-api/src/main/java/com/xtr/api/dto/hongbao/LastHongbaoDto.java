package com.xtr.api.dto.hongbao;

import java.io.Serializable;
import java.util.Date;

/**
 * @author:zhangshuai
 * @date: 2016/10/26.
 */
public class LastHongbaoDto implements Serializable{

    private Integer  id;

    private  String  companyPhone;

    private Date createTime;

    private String maimai;

    public String getMaimai() {
        return maimai;
    }

    public void setMaimai(String maimai) {
        this.maimai = maimai;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
