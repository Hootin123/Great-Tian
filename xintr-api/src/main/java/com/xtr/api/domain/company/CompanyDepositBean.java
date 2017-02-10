package com.xtr.api.domain.company;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.util.Date;

public class CompanyDepositBean extends BaseObject implements Serializable {
    /**
     *  id,所属表字段为company_deposit.deposit_id
     */
    private Long depositId;

    /**
     *  企业用户id,所属表字段为company_deposit.deposit_member_id
     */
    private Long depositMemberId;

    /**
     *  企业id,所属表字段为company_deposit.deposit_company_id
     */
    private Long depositCompanyId;

    /**
     *  提现银行名称,所属表字段为company_deposit.deposit_bank_name
     */
    private String depositBankName;

    /**
     *  提现银行账户名,所属表字段为company_deposit.deposit_bank_account_name
     */
    private String depositBankAccountName;

    /**
     *  提现银行账号,所属表字段为company_deposit.deposit_bank_number
     */
    private String depositBankNumber;

    /**
     *  开户支行名称,所属表字段为company_deposit.deposit_sub_bank_name
     */
    private String depositSubBankName;

    /**
     *  创建人,所属表字段为company_deposit.deposit_create_user
     */
    private String depositCreateUser;

    /**
     *  修改人,所属表字段为company_deposit.deposit_update_user
     */
    private String depositUpdateUser;

    /**
     *  创建时间,所属表字段为company_deposit.deposit_create_time
     */
    private Date depositCreateTime;

    /**
     *  修改时间,所属表字段为company_deposit.deposit_update_time
     */
    private Date depositUpdateTime;
    //生效状态 1有效 2无效
    private Integer depositState;

    public Integer getDepositState() {
        return depositState;
    }

    public void setDepositState(Integer depositState) {
        this.depositState = depositState;
    }

    /**
     * 获取 id 字段:company_deposit.deposit_id
     *
     * @return company_deposit.deposit_id, id
     */
    public Long getDepositId() {
        return depositId;
    }

    /**
     * 设置 id 字段:company_deposit.deposit_id
     *
     * @param depositId company_deposit.deposit_id, id
     */
    public void setDepositId(Long depositId) {
        this.depositId = depositId;
    }

    /**
     * 获取 企业用户id 字段:company_deposit.deposit_member_id
     *
     * @return company_deposit.deposit_member_id, 企业用户id
     */
    public Long getDepositMemberId() {
        return depositMemberId;
    }

    /**
     * 设置 企业用户id 字段:company_deposit.deposit_member_id
     *
     * @param depositMemberId company_deposit.deposit_member_id, 企业用户id
     */
    public void setDepositMemberId(Long depositMemberId) {
        this.depositMemberId = depositMemberId;
    }

    /**
     * 获取 企业id 字段:company_deposit.deposit_company_id
     *
     * @return company_deposit.deposit_company_id, 企业id
     */
    public Long getDepositCompanyId() {
        return depositCompanyId;
    }

    /**
     * 设置 企业id 字段:company_deposit.deposit_company_id
     *
     * @param depositCompanyId company_deposit.deposit_company_id, 企业id
     */
    public void setDepositCompanyId(Long depositCompanyId) {
        this.depositCompanyId = depositCompanyId;
    }

    /**
     * 获取 提现银行名称 字段:company_deposit.deposit_bank_name
     *
     * @return company_deposit.deposit_bank_name, 提现银行名称
     */
    public String getDepositBankName() {
        return depositBankName;
    }

    /**
     * 设置 提现银行名称 字段:company_deposit.deposit_bank_name
     *
     * @param depositBankName company_deposit.deposit_bank_name, 提现银行名称
     */
    public void setDepositBankName(String depositBankName) {
        this.depositBankName = depositBankName == null ? null : depositBankName.trim();
    }

    /**
     * 获取 提现银行账户名 字段:company_deposit.deposit_bank_account_name
     *
     * @return company_deposit.deposit_bank_account_name, 提现银行账户名
     */
    public String getDepositBankAccountName() {
        return depositBankAccountName;
    }

    /**
     * 设置 提现银行账户名 字段:company_deposit.deposit_bank_account_name
     *
     * @param depositBankAccountName company_deposit.deposit_bank_account_name, 提现银行账户名
     */
    public void setDepositBankAccountName(String depositBankAccountName) {
        this.depositBankAccountName = depositBankAccountName == null ? null : depositBankAccountName.trim();
    }

    /**
     * 获取 提现银行账号 字段:company_deposit.deposit_bank_number
     *
     * @return company_deposit.deposit_bank_number, 提现银行账号
     */
    public String getDepositBankNumber() {
        return depositBankNumber;
    }

    /**
     * 设置 提现银行账号 字段:company_deposit.deposit_bank_number
     *
     * @param depositBankNumber company_deposit.deposit_bank_number, 提现银行账号
     */
    public void setDepositBankNumber(String depositBankNumber) {
        this.depositBankNumber = depositBankNumber == null ? null : depositBankNumber.trim();
    }

    /**
     * 获取 开户支行名称 字段:company_deposit.deposit_sub_bank_name
     *
     * @return company_deposit.deposit_sub_bank_name, 开户支行名称
     */
    public String getDepositSubBankName() {
        return depositSubBankName;
    }

    /**
     * 设置 开户支行名称 字段:company_deposit.deposit_sub_bank_name
     *
     * @param depositSubBankName company_deposit.deposit_sub_bank_name, 开户支行名称
     */
    public void setDepositSubBankName(String depositSubBankName) {
        this.depositSubBankName = depositSubBankName == null ? null : depositSubBankName.trim();
    }

    /**
     * 获取 创建人 字段:company_deposit.deposit_create_user
     *
     * @return company_deposit.deposit_create_user, 创建人
     */
    public String getDepositCreateUser() {
        return depositCreateUser;
    }

    /**
     * 设置 创建人 字段:company_deposit.deposit_create_user
     *
     * @param depositCreateUser company_deposit.deposit_create_user, 创建人
     */
    public void setDepositCreateUser(String depositCreateUser) {
        this.depositCreateUser = depositCreateUser == null ? null : depositCreateUser.trim();
    }

    /**
     * 获取 修改人 字段:company_deposit.deposit_update_user
     *
     * @return company_deposit.deposit_update_user, 修改人
     */
    public String getDepositUpdateUser() {
        return depositUpdateUser;
    }

    /**
     * 设置 修改人 字段:company_deposit.deposit_update_user
     *
     * @param depositUpdateUser company_deposit.deposit_update_user, 修改人
     */
    public void setDepositUpdateUser(String depositUpdateUser) {
        this.depositUpdateUser = depositUpdateUser == null ? null : depositUpdateUser.trim();
    }

    /**
     * 获取 创建时间 字段:company_deposit.deposit_create_time
     *
     * @return company_deposit.deposit_create_time, 创建时间
     */
    public Date getDepositCreateTime() {
        return depositCreateTime;
    }

    /**
     * 设置 创建时间 字段:company_deposit.deposit_create_time
     *
     * @param depositCreateTime company_deposit.deposit_create_time, 创建时间
     */
    public void setDepositCreateTime(Date depositCreateTime) {
        this.depositCreateTime = depositCreateTime;
    }

    /**
     * 获取 修改时间 字段:company_deposit.deposit_update_time
     *
     * @return company_deposit.deposit_update_time, 修改时间
     */
    public Date getDepositUpdateTime() {
        return depositUpdateTime;
    }

    /**
     * 设置 修改时间 字段:company_deposit.deposit_update_time
     *
     * @param depositUpdateTime company_deposit.deposit_update_time, 修改时间
     */
    public void setDepositUpdateTime(Date depositUpdateTime) {
        this.depositUpdateTime = depositUpdateTime;
    }
}