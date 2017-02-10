package com.xtr.api.dto.company;

import com.xtr.api.domain.company.CompanyIntentBean;

import java.io.Serializable;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/12 13:51
 */
public class CompanyIntentDto extends CompanyIntentBean implements Serializable {

    //企业名称
    private String companyName;
    //联系手机
    private String companyPhone;
    //业务经理姓名
    private String managerName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
}
