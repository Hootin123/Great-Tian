package com.xtr.api.domain.customer;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户账户资金变动记录
 */
public class CustomerMoneyRecordsBean extends BaseObject implements Serializable {
    /**
     * id,所属表字段为customer_money_records.record_id
     */
    private Long recordId;

    /**
     * 客户id,所属表字段为customer_money_records.record_customer_id
     */
    private Long recordCustomerId;

    /**
     * 记录产生来源主键id,所属表字段为company_money_records.resource_id
     */
    private Long resourceId;

    /**
     * 企业资金变动 正为收入 负为支出,所属表字段为customer_money_records.record_money
     */
    private BigDecimal recordMoney;

    /**
     * 当前剩余,暂停使用,所属表字段为customer_money_records.record_money_now
     */
    private BigDecimal recordMoneyNow;

    /**
     * 记录描述,所属表字段为customer_money_records.record_description
     */
    private String recordDescription;

    /**
     * 记录类型 1收入 2支付,所属表字段为customer_money_records.record_type
     */
    private Integer recordType;

    /**
     * 记录产生来源 1充值+ 2提现- 3预支工资+ 4公司发工资+ 5预支工资还款- 6购买理财- 7理财还款+ 8得到返现,所属表字段为customer_money_records.record_source
     */
    private Integer recordSource;

    /**
     * 记录产生时间,所属表字段为customer_money_records.record_addtime
     */
    private Date recordAddtime;

    /**
     * 备注,所属表字段为customer_money_records.record_remark
     */
    private String recordRemark;

    /**
     * 同一个业务标识(时间戳,精确到毫秒),所属表字段为customer_money_records.tran_id
     */
    private Long tranId;

    /**
     * 1正常，0无效,所属表字段为customer_money_records.sign
     */
    private Integer sign;

    /**
     * 获取 id 字段:customer_money_records.record_id
     *
     * @return customer_money_records.record_id, id
     */
    public Long getRecordId() {
        return recordId;
    }

    /**
     * 设置 id 字段:customer_money_records.record_id
     *
     * @param recordId customer_money_records.record_id, id
     */
    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    /**
     * 获取 客户id 字段:customer_money_records.record_customer_id
     *
     * @return customer_money_records.record_customer_id, 客户id
     */
    public Long getRecordCustomerId() {
        return recordCustomerId;
    }

    /**
     * 设置 客户id 字段:customer_money_records.record_customer_id
     *
     * @param recordCustomerId customer_money_records.record_customer_id, 客户id
     */
    public void setRecordCustomerId(Long recordCustomerId) {
        this.recordCustomerId = recordCustomerId;
    }

    /**
     * 获取 企业资金变动 正为收入 负为支出 字段:customer_money_records.record_money
     *
     * @return customer_money_records.record_money, 企业资金变动 正为收入 负为支出
     */
    public BigDecimal getRecordMoney() {
        return recordMoney;
    }

    /**
     * 设置 企业资金变动 正为收入 负为支出 字段:customer_money_records.record_money
     *
     * @param recordMoney customer_money_records.record_money, 企业资金变动 正为收入 负为支出
     */
    public void setRecordMoney(BigDecimal recordMoney) {
        this.recordMoney = recordMoney;
    }

    /**
     * 获取 当前剩余,暂停使用 字段:customer_money_records.record_money_now
     *
     * @return customer_money_records.record_money_now, 当前剩余,暂停使用
     */
    public BigDecimal getRecordMoneyNow() {
        return recordMoneyNow;
    }

    /**
     * 设置 当前剩余,暂停使用 字段:customer_money_records.record_money_now
     *
     * @param recordMoneyNow customer_money_records.record_money_now, 当前剩余,暂停使用
     */
    public void setRecordMoneyNow(BigDecimal recordMoneyNow) {
        this.recordMoneyNow = recordMoneyNow;
    }

    /**
     * 获取 记录描述 字段:customer_money_records.record_description
     *
     * @return customer_money_records.record_description, 记录描述
     */
    public String getRecordDescription() {
        return recordDescription;
    }

    /**
     * 设置 记录描述 字段:customer_money_records.record_description
     *
     * @param recordDescription customer_money_records.record_description, 记录描述
     */
    public void setRecordDescription(String recordDescription) {
        this.recordDescription = recordDescription == null ? null : recordDescription.trim();
    }

    /**
     * 获取 记录类型 1收入 2支付 字段:customer_money_records.record_type
     *
     * @return customer_money_records.record_type, 记录类型 1收入 2支付
     */
    public Integer getRecordType() {
        return recordType;
    }

    /**
     * 设置 记录类型 1收入 2支付 字段:customer_money_records.record_type
     *
     * @param recordType customer_money_records.record_type, 记录类型 1收入 2支付
     */
    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    /**
     * 获取 记录产生来源 1充值+ 2提现- 3预支工资+ 4公司发工资+ 5预支工资还款- 6购买理财- 7理财还款+ 8得到返现 字段:customer_money_records.record_source
     *
     * @return customer_money_records.record_source, 记录产生来源 1充值+ 2提现- 3预支工资+ 4公司发工资+ 5预支工资还款- 6购买理财- 7理财还款+ 8得到返现
     */
    public Integer getRecordSource() {
        return recordSource;
    }

    /**
     * 设置 记录产生来源 1充值+ 2提现- 3预支工资+ 4公司发工资+ 5预支工资还款- 6购买理财- 7理财还款+ 8得到返现 字段:customer_money_records.record_source
     *
     * @param recordSource customer_money_records.record_source, 记录产生来源 1充值+ 2提现- 3预支工资+ 4公司发工资+ 5预支工资还款- 6购买理财- 7理财还款+ 8得到返现
     */
    public void setRecordSource(Integer recordSource) {
        this.recordSource = recordSource;
    }

    /**
     * 获取 记录产生时间 字段:customer_money_records.record_addtime
     *
     * @return customer_money_records.record_addtime, 记录产生时间
     */
    public Date getRecordAddtime() {
        return recordAddtime;
    }

    /**
     * 设置 记录产生时间 字段:customer_money_records.record_addtime
     *
     * @param recordAddtime customer_money_records.record_addtime, 记录产生时间
     */
    public void setRecordAddtime(Date recordAddtime) {
        this.recordAddtime = recordAddtime;
    }

    /**
     * 获取 备注 字段:customer_money_records.record_remark
     *
     * @return customer_money_records.record_remark, 备注
     */
    public String getRecordRemark() {
        return recordRemark;
    }

    /**
     * 设置 备注 字段:customer_money_records.record_remark
     *
     * @param recordRemark customer_money_records.record_remark, 备注
     */
    public void setRecordRemark(String recordRemark) {
        this.recordRemark = recordRemark == null ? null : recordRemark.trim();
    }

    /**
     * 获取 同一个业务标识(时间戳,精确到毫秒) 字段:customer_money_records.tran_id
     *
     * @return customer_money_records.tran_id, 同一个业务标识(时间戳,精确到毫秒)
     */
    public Long getTranId() {
        return tranId;
    }

    /**
     * 设置 同一个业务标识(时间戳,精确到毫秒) 字段:customer_money_records.tran_id
     *
     * @param tranId customer_money_records.tran_id, 同一个业务标识(时间戳,精确到毫秒)
     */
    public void setTranId(Long tranId) {
        this.tranId = tranId;
    }

    /**
     * 获取 1正常，0无效 字段:customer_money_records.sign
     *
     * @return customer_money_records.sign, 1正常，0无效
     */
    public Integer getSign() {
        return sign;
    }

    /**
     * 设置 1正常，0无效 字段:customer_money_records.sign
     *
     * @param sign customer_money_records.sign, 1正常，0无效
     */
    public void setSign(Integer sign) {
        this.sign = sign;
    }

    /**
     * 获取 记录产生来源主键id,字段:company_money_records.resource_id
     *
     * @return
     */
    public Long getResourceId() {
        return resourceId;
    }

    /**
     * 设置 记录产生来源主键id,字段:company_money_records.resource_id
     *
     * @param resourceId
     */
    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }
}