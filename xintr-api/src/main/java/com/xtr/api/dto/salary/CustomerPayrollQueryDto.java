package com.xtr.api.dto.salary;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;

/**
 * <p>工资单查询dto</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/29 13:46
 */
public class CustomerPayrollQueryDto extends BaseObject implements Serializable {

    /**
     * 公司id
     */
    private Long companyId;

    /**
     * 计薪周期id
     */
    private Long payCycleId;

    /**
     * 聘用形式 1正式 2劳务
     */
    private Integer stationEmployMethod;

    /**
     * 员工状态 1入职 2转正 3离职 4删除
     */
    private Integer stationCustomerState;

    /**
     * 部门
     */
    private Long deptId;

    /**
     * 津贴
     */
    private Long allowanceId;

    /**
     * 员工姓名
     */
    private String userName;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getPayCycleId() {
        return payCycleId;
    }

    public void setPayCycleId(Long payCycleId) {
        this.payCycleId = payCycleId;
    }

    public Integer getStationEmployMethod() {
        return stationEmployMethod;
    }

    public void setStationEmployMethod(Integer stationEmployMethod) {
        this.stationEmployMethod = stationEmployMethod;
    }

    public Integer getStationCustomerState() {
        return stationCustomerState;
    }

    public void setStationCustomerState(Integer stationCustomerState) {
        this.stationCustomerState = stationCustomerState;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getAllowanceId() {
        return allowanceId;
    }

    public void setAllowanceId(Long allowanceId) {
        this.allowanceId = allowanceId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
