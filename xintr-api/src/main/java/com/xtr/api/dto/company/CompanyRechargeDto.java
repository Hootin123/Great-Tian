package com.xtr.api.dto.company;

import com.xtr.api.domain.company.CompanyRechargesBean;

import java.io.Serializable;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/7/7 17:48
 */
public class CompanyRechargeDto extends CompanyRechargesBean implements Serializable {

    /**
     * 财务审核人
     */
    private String nickName;

    /**
     * 企业名称
     */
    private String companyName;
    //用户名称
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public CompanyRechargeDto() {

    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


}
