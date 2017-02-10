package com.xtr.api.domain.salary;

import java.io.Serializable;
import java.math.BigDecimal;

public class CustomerPayrollDetailBean implements Serializable {
    /**
     * 主键,所属表字段为customer_payroll_detail.id
     */
    private Long id;

    /**
     * 工资单主键,所属表字段为customer_payroll_detail.customer_payroll_id
     */
    private Long customerPayrollId;

    /**
     * 明细项名称,所属表字段为customer_payroll_detail.detail_name
     */
    private String detailName;

    /**
     * 明细项值,所属表字段为customer_payroll_detail.detail_value
     */
    private BigDecimal detailValue;

    /**
     * 明细项类型 0:津贴  1:奖金,所属表字段为customer_payroll_detail.detail_type
     */
    private Integer detailType;

    /**
     * 来源id  针对奖金设置id
     */
    private Long resourceId;

    /**
     * 获取 主键 字段:customer_payroll_detail.id
     *
     * @return customer_payroll_detail.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 主键 字段:customer_payroll_detail.id
     *
     * @param id customer_payroll_detail.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 工资单主键 字段:customer_payroll_detail.customer_payroll_id
     *
     * @return customer_payroll_detail.customer_payroll_id, 工资单主键
     */
    public Long getCustomerPayrollId() {
        return customerPayrollId;
    }

    /**
     * 设置 工资单主键 字段:customer_payroll_detail.customer_payroll_id
     *
     * @param customerPayrollId customer_payroll_detail.customer_payroll_id, 工资单主键
     */
    public void setCustomerPayrollId(Long customerPayrollId) {
        this.customerPayrollId = customerPayrollId;
    }

    /**
     * 获取 明细项名称 字段:customer_payroll_detail.detail_name
     *
     * @return customer_payroll_detail.detail_name, 明细项名称
     */
    public String getDetailName() {
        return detailName;
    }

    /**
     * 设置 明细项名称 字段:customer_payroll_detail.detail_name
     *
     * @param detailName customer_payroll_detail.detail_name, 明细项名称
     */
    public void setDetailName(String detailName) {
        this.detailName = detailName == null ? null : detailName.trim();
    }

    /**
     * 获取 明细项值 字段:customer_payroll_detail.detail_value
     *
     * @return customer_payroll_detail.detail_value, 明细项值
     */
    public BigDecimal getDetailValue() {
        return detailValue;
    }

    /**
     * 设置 明细项值 字段:customer_payroll_detail.detail_value
     *
     * @param detailValue customer_payroll_detail.detail_value, 明细项值
     */
    public void setDetailValue(BigDecimal detailValue) {
        this.detailValue = detailValue;
    }

    /**
     * 获取 明细项类型 0:津贴  1:奖金 字段:customer_payroll_detail.detail_type
     *
     * @return customer_payroll_detail.detail_type, 明细项类型 0:津贴  1:奖金
     */
    public Integer getDetailType() {
        return detailType;
    }

    /**
     * 设置 明细项类型 0:津贴  1:奖金 字段:customer_payroll_detail.detail_type
     *
     * @param detailType customer_payroll_detail.detail_type, 明细项类型 0:津贴  1:奖金
     */
    public void setDetailType(Integer detailType) {
        this.detailType = detailType;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }
}