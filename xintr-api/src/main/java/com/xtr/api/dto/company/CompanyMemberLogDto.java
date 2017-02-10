package com.xtr.api.dto.company;

import com.xtr.api.domain.company.CompanyMemberLogsBean;

import java.io.Serializable;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/7/11 16:03
 */
public class CompanyMemberLogDto extends CompanyMemberLogsBean implements Serializable {

    private String memberLogname;
    private String userName;
    private String jobName;

    public CompanyMemberLogDto() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getMemberLogname() {
        return memberLogname;
    }

    public void setMemberLogname(String memberLogname) {
        this.memberLogname = memberLogname;
    }
}
