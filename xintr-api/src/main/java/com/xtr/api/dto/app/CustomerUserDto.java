package com.xtr.api.dto.app;

import com.xtr.api.domain.app.CustomerUserBean;

import java.io.Serializable;

/**
 * <p>用户信息Dto</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/12 17:46
 */
public class CustomerUserDto extends CustomerUserBean implements Serializable {

    /**
     * 企业Id
     */
    private Long companyId;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 部门Id
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 性别 0女 1男
     */
    private Integer sex;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行卡号
     */
    private String bankNumber;


    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }
}
