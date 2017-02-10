package com.xtr.api.dto.hongbao;

import java.io.Serializable;

/**
 * 用于红包业务的封装类
 * @author:zhangshuai
 * @date: 2016/9/27.
 */
public class  NewHongbaoDto implements Serializable{

    //公司名称
    private String companyName;
    //用户姓名
    private String userName;
    //密码
    private String password;
    //确认密码
    private String rePassword;
    //手机号码
    private String phoneNumber;
   //短信验证码
    private String phoneCode;

    //登录的手机号码
    private String loginPhoneNumber;
    //登录的密码
    private String loginPassword;
    //图形验证码
    private String code;
    //图形验证码
    private String key;
    //公司地址
    private String companyAddress;
    //营业执照编号
    private String companyNumber;
    //logo
    private String companyLogo;
    //执照
    private String companyOrganizationImg;
    //法人代表
    private String companyCorporation;
    //联系电话
    private String companyCorporationPhone;
    //姓名
    private String companyTruename;
    //支付宝账号
    private String companyAlipayNumber;
    //是否已经收集了信息
    private Integer companyIsCollectInfo;
    //公司id
    private Long companyId;

    //注册来源 即邀请码
    private String companyContactPlace;

    public String getCompanyContactPlace() {
        return companyContactPlace;
    }

    public void setCompanyContactPlace(String companyContactPlace) {
        this.companyContactPlace = companyContactPlace;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Integer getCompanyIsCollectInfo() {
        return companyIsCollectInfo;
    }

    public void setCompanyIsCollectInfo(Integer companyIsCollectInfo) {
        this.companyIsCollectInfo = companyIsCollectInfo;
    }

    public String getCompanyTruename() {
        return companyTruename;
    }

    public void setCompanyTruename(String companyTruename) {
        this.companyTruename = companyTruename;
    }

    public String getCompanyAlipayNumber() {
        return companyAlipayNumber;
    }

    public void setCompanyAlipayNumber(String companyAlipayNumber) {
        this.companyAlipayNumber = companyAlipayNumber;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyOrganizationImg() {
        return companyOrganizationImg;
    }

    public void setCompanyOrganizationImg(String companyOrganizationImg) {
        this.companyOrganizationImg = companyOrganizationImg;
    }

    public String getCompanyCorporation() {
        return companyCorporation;
    }

    public void setCompanyCorporation(String companyCorporation) {
        this.companyCorporation = companyCorporation;
    }

    public String getCompanyCorporationPhone() {
        return companyCorporationPhone;
    }

    public void setCompanyCorporationPhone(String companyCorporationPhone) {
        this.companyCorporationPhone = companyCorporationPhone;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getLoginPhoneNumber() {
        return loginPhoneNumber;
    }

    public void setLoginPhoneNumber(String loginPhoneNumber) {
        this.loginPhoneNumber = loginPhoneNumber;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
