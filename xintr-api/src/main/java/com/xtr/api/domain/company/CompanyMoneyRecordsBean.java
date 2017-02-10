package com.xtr.api.domain.company;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.xtr.api.basic.BaseObject;

/**
 * 
 * <p>企业账户资金变动记录</p>
 *
 * @author 任齐
 * @createTime: 2016/06/29 上午10:26:12
 */
public class CompanyMoneyRecordsBean extends BaseObject implements Serializable {
    /**
     *  id,所属表字段为company_money_records.record_id
     */
    private Long recordId;

    /**
     *  企业id,所属表字段为company_money_records.record_company_id
     */
    private Long recordCompanyId;

    /**
     * 记录产生来源主键id,所属表字段为company_money_records.resource_id
     */
    private Long resourceId;

    /**
     *  企业资金变动 正为收入 负为支出,所属表字段为company_money_records.record_money
     */
    private BigDecimal recordMoney;

    /**
     *  当前剩余,所属表字段为company_money_records.record_money_now
     */
    private BigDecimal recordMoneyNow;

    /**
     *  记录描述,所属表字段为company_money_records.record_description
     */
    private String recordDescription;

    /**
     *  记录类型 1收入 2支付,所属表字段为company_money_records.record_type
     */
    private Integer recordType;

    /**
     *  记录产生来源 1充值+ 2提现-  3发工资- 4平台垫付放款+ 5购买理财- 6理财还款+ 7得到返现+,所属表字段为company_money_records.record_source
     */
    private Integer recordSource;

    /**
     *  记录产生时间,所属表字段为company_money_records.record_addtime
     */
    private Date recordAddtime;

    /**
     *  备注,所属表字段为company_money_records.record_remark
     */
    private String recordRemark;

    /**
     *  同一个业务标识(时间戳,精确到毫秒),所属表字段为company_money_records.tran_id
     */
    private Long tranId;

    /**
     *  0未冻结 1已冻结,所属表字段为company_money_records.record_freeze
     */
    private Integer recordFreeze;

    /**
     *  冻结类型 1提现冻结 2代发、垫发,所属表字段为company_money_records.record_freeze_type
     */
    private Integer recordFreezeType;

    /**
     *  冻结对象id,所属表字段为company_money_records.record_freeze_objectid
     */
    private Long recordFreezeObjectid;

    /**
     * 获取 id 字段:company_money_records.record_id
     *
     * @return company_money_records.record_id, id
     */
    public Long getRecordId() {
        return recordId;
    }

    /**
     * 设置 id 字段:company_money_records.record_id
     *
     * @param recordId company_money_records.record_id, id
     */
    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    /**
     * 获取 企业id 字段:company_money_records.record_company_id
     *
     * @return company_money_records.record_company_id, 企业id
     */
    public Long getRecordCompanyId() {
        return recordCompanyId;
    }

    /**
     * 设置 企业id 字段:company_money_records.record_company_id
     *
     * @param recordCompanyId company_money_records.record_company_id, 企业id
     */
    public void setRecordCompanyId(Long recordCompanyId) {
        this.recordCompanyId = recordCompanyId;
    }

    /**
     * 获取 企业资金变动 正为收入 负为支出 字段:company_money_records.record_money
     *
     * @return company_money_records.record_money, 企业资金变动 正为收入 负为支出
     */
    public BigDecimal getRecordMoney() {
        return recordMoney;
    }

    /**
     * 设置 企业资金变动 正为收入 负为支出 字段:company_money_records.record_money
     *
     * @param recordMoney company_money_records.record_money, 企业资金变动 正为收入 负为支出
     */
    public void setRecordMoney(BigDecimal recordMoney) {
        this.recordMoney = recordMoney;
    }

    /**
     * 获取 当前剩余 字段:company_money_records.record_money_now
     *
     * @return company_money_records.record_money_now, 当前剩余
     */
    public BigDecimal getRecordMoneyNow() {
        return recordMoneyNow;
    }

    /**
     * 设置 当前剩余 字段:company_money_records.record_money_now
     *
     * @param recordMoneyNow company_money_records.record_money_now, 当前剩余
     */
    public void setRecordMoneyNow(BigDecimal recordMoneyNow) {
        this.recordMoneyNow = recordMoneyNow;
    }

    /**
     * 获取 记录描述 字段:company_money_records.record_description
     *
     * @return company_money_records.record_description, 记录描述
     */
    public String getRecordDescription() {
        return recordDescription;
    }

    /**
     * 设置 记录描述 字段:company_money_records.record_description
     *
     * @param recordDescription company_money_records.record_description, 记录描述
     */
    public void setRecordDescription(String recordDescription) {
        this.recordDescription = recordDescription == null ? null : recordDescription.trim();
    }

    /**
     * 获取 记录类型 1收入 2支付 字段:company_money_records.record_type
     *
     * @return company_money_records.record_type, 记录类型 1收入 2支付
     */
    public Integer getRecordType() {
        return recordType;
    }

    /**
     * 设置 记录类型 1收入 2支付 字段:company_money_records.record_type
     *
     * @param recordType company_money_records.record_type, 记录类型 1收入 2支付
     */
    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    /**
     * 获取 记录产生来源 1充值+ 2提现-  3发工资- 4平台垫付放款+ 5购买理财- 6理财还款+ 7得到返现+ 字段:company_money_records.record_source
     *
     * @return company_money_records.record_source, 记录产生来源 1充值+ 2提现-  3发工资- 4平台垫付放款+ 5购买理财- 6理财还款+ 7得到返现+
     */
    public Integer getRecordSource() {
        return recordSource;
    }

    /**
     * 设置 记录产生来源 1充值+ 2提现-  3发工资- 4平台垫付放款+ 5购买理财- 6理财还款+ 7得到返现+ 字段:company_money_records.record_source
     *
     * @param recordSource company_money_records.record_source, 记录产生来源 1充值+ 2提现-  3发工资- 4平台垫付放款+ 5购买理财- 6理财还款+ 7得到返现+
     */
    public void setRecordSource(Integer recordSource) {
        this.recordSource = recordSource;
    }

    /**
     * 获取 记录产生时间 字段:company_money_records.record_addtime
     *
     * @return company_money_records.record_addtime, 记录产生时间
     */
    public Date getRecordAddtime() {
        return recordAddtime;
    }

    /**
     * 设置 记录产生时间 字段:company_money_records.record_addtime
     *
     * @param recordAddtime company_money_records.record_addtime, 记录产生时间
     */
    public void setRecordAddtime(Date recordAddtime) {
        this.recordAddtime = recordAddtime;
    }

    /**
     * 获取 备注 字段:company_money_records.record_remark
     *
     * @return company_money_records.record_remark, 备注
     */
    public String getRecordRemark() {
        return recordRemark;
    }

    /**
     * 设置 备注 字段:company_money_records.record_remark
     *
     * @param recordRemark company_money_records.record_remark, 备注
     */
    public void setRecordRemark(String recordRemark) {
        this.recordRemark = recordRemark == null ? null : recordRemark.trim();
    }

    /**
     * 获取 同一个业务标识(时间戳,精确到毫秒) 字段:company_money_records.tran_id
     *
     * @return company_money_records.tran_id, 同一个业务标识(时间戳,精确到毫秒)
     */
    public Long getTranId() {
        return tranId;
    }

    /**
     * 设置 同一个业务标识(时间戳,精确到毫秒) 字段:company_money_records.tran_id
     *
     * @param tranId company_money_records.tran_id, 同一个业务标识(时间戳,精确到毫秒)
     */
    public void setTranId(Long tranId) {
        this.tranId = tranId;
    }

    /**
     * 获取 0未冻结 1已冻结 字段:company_money_records.record_freeze
     *
     * @return company_money_records.record_freeze, 0未冻结 1已冻结
     */
    public Integer getRecordFreeze() {
        return recordFreeze;
    }

    /**
     * 设置 0未冻结 1已冻结 字段:company_money_records.record_freeze
     *
     * @param recordFreeze company_money_records.record_freeze, 0未冻结 1已冻结
     */
    public void setRecordFreeze(Integer recordFreeze) {
        this.recordFreeze = recordFreeze;
    }

    /**
     * 获取 冻结类型 1提现冻结 2代发、垫发 字段:company_money_records.record_freeze_type
     *
     * @return company_money_records.record_freeze_type, 冻结类型 1提现冻结 2代发、垫发
     */
    public Integer getRecordFreezeType() {
        return recordFreezeType;
    }

    /**
     * 设置 冻结类型 1提现冻结 2代发、垫发 字段:company_money_records.record_freeze_type
     *
     * @param recordFreezeType company_money_records.record_freeze_type, 冻结类型 1提现冻结 2代发、垫发
     */
    public void setRecordFreezeType(Integer recordFreezeType) {
        this.recordFreezeType = recordFreezeType;
    }

    /**
     * 获取 冻结对象id 字段:company_money_records.record_freeze_objectid
     *
     * @return company_money_records.record_freeze_objectid, 冻结对象id
     */
    public Long getRecordFreezeObjectid() {
        return recordFreezeObjectid;
    }

    /**
     * 设置 冻结对象id 字段:company_money_records.record_freeze_objectid
     *
     * @param recordFreezeObjectid company_money_records.record_freeze_objectid, 冻结对象id
     */
    public void setRecordFreezeObjectid(Long recordFreezeObjectid) {
        this.recordFreezeObjectid = recordFreezeObjectid;
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