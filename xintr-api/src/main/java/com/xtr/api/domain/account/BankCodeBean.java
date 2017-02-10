package com.xtr.api.domain.account;

import java.io.Serializable;

public class BankCodeBean implements Serializable {
    /**
     *  主键,所属表字段为bank_code.id
     */
    private Long id;

    /**
     *  银行简称,所属表字段为bank_code.bank_code
     */
    private String bankCode;

    /**
     *  银行全称,所属表字段为bank_code.bank_full_name
     */
    private String bankFullName;

    /**
     *  银行简称,所属表字段为bank_code.bank_abbreviation
     */
    private String bankAbbreviation;

    /**
     * 获取 主键 字段:bank_code.id
     *
     * @return bank_code.id, 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 主键 字段:bank_code.id
     *
     * @param id bank_code.id, 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 银行简称 字段:bank_code.bank_code
     *
     * @return bank_code.bank_code, 银行简称
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * 设置 银行简称 字段:bank_code.bank_code
     *
     * @param bankCode bank_code.bank_code, 银行简称
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    /**
     * 获取 银行全称 字段:bank_code.bank_full_name
     *
     * @return bank_code.bank_full_name, 银行全称
     */
    public String getBankFullName() {
        return bankFullName;
    }

    /**
     * 设置 银行全称 字段:bank_code.bank_full_name
     *
     * @param bankFullName bank_code.bank_full_name, 银行全称
     */
    public void setBankFullName(String bankFullName) {
        this.bankFullName = bankFullName == null ? null : bankFullName.trim();
    }

    /**
     * 获取 银行简称 字段:bank_code.bank_abbreviation
     *
     * @return bank_code.bank_abbreviation, 银行简称
     */
    public String getBankAbbreviation() {
        return bankAbbreviation;
    }

    /**
     * 设置 银行简称 字段:bank_code.bank_abbreviation
     *
     * @param bankAbbreviation bank_code.bank_abbreviation, 银行简称
     */
    public void setBankAbbreviation(String bankAbbreviation) {
        this.bankAbbreviation = bankAbbreviation == null ? null : bankAbbreviation.trim();
    }
}