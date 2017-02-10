package com.xtr.api.dto.company;

import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanysBean;

import java.io.Serializable;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/25 10:42
 */
public class CompanyMembersDto extends CompanyMembersBean implements Serializable {
    //新密码
    private String newPwd;
    //新密码确认
    private String confirmNewPwd;


    //新的手机号
    private String newMemberPhone;
    //姓名
    private String  userName;

    public String getNewMemberPhone() {
        return newMemberPhone;
    }

    public void setNewMemberPhone(String newMemberPhone) {
        this.newMemberPhone = newMemberPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getConfirmNewPwd() {
        return confirmNewPwd;
    }

    public void setConfirmNewPwd(String confirmNewPwd) {
        this.confirmNewPwd = confirmNewPwd;
    }
}
