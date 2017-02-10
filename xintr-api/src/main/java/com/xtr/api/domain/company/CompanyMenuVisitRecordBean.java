package com.xtr.api.domain.company;

import java.io.Serializable;
import java.util.Date;

/**
 * 访问目录实体
 * @author:zhangshuai
 * @date: 2016/9/12.
 */
public class CompanyMenuVisitRecordBean implements Serializable{

    /**主键**/
    private Integer  id;
    /**公司id**/
    private Long companyId;
    /**登录账号的id**/
    private Long memberId;
    /**访问的菜单类型  1、首页 2、员工管理 3、工资核算 **/
    private Integer type;
    /**访问该目录的时间**/
    private Date visitTime;
    /**ip地址**/
    private String  ip;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
