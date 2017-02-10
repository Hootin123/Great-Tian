package com.xtr.api.domain.company;

import com.xtr.api.basic.BaseObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BorrowInfoBean extends BaseObject implements Serializable {
    /**
     * id,所属表字段为borrow_info.id
     */
    private Long id;

    /**
     * borrow_order_number,所属表字段为borrow_info.borrow_order_number
     */
    private String borrowOrderNumber;

    /**
     * borrow_bank_name,所属表字段为borrow_info.borrow_bank_name
     */
    private String borrowBankName;

    /**
     * borrow_bank_no,所属表字段为borrow_info.borrow_bank_no
     */
    private String borrowBankNo;

    /**
     * 成员borrow_serial_number,所属表字段为borrow_info.borrow_serial_number
     */
    private String borrowSerialNumber;

    /**
     * 成员borrow_account_money,所属表字段为borrow_info.borrow_account_money
     */
    private BigDecimal borrowAccountMoney;

    /**
     * 成员borrow_remark,所属表字段为borrow_info.borrow_remark
     */
    private String borrowRemark;

    /**
     * 记录添加时间,所属表字段为borrow_info.borrow_addtime
     */
    private Date borrowAddtime;

    private String borrowAccountMoneyValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBorrowOrderNumber() {
        return borrowOrderNumber;
    }

    public void setBorrowOrderNumber(String borrowOrderNumber) {
        this.borrowOrderNumber = borrowOrderNumber;
    }

    public String getBorrowBankName() {
        return borrowBankName;
    }

    public void setBorrowBankName(String borrowBankName) {
        this.borrowBankName = borrowBankName;
    }

    public String getBorrowBankNo() {
        return borrowBankNo;
    }

    public void setBorrowBankNo(String borrowBankNo) {
        this.borrowBankNo = borrowBankNo;
    }

    public String getBorrowSerialNumber() {
        return borrowSerialNumber;
    }

    public void setBorrowSerialNumber(String borrowSerialNumber) {
        this.borrowSerialNumber = borrowSerialNumber;
    }

    public BigDecimal getBorrowAccountMoney() {
        return borrowAccountMoney;
    }

    public void setBorrowAccountMoney(BigDecimal borrowAccountMoney) {
        this.borrowAccountMoney = borrowAccountMoney;
    }

    public String getBorrowRemark() {
        return borrowRemark;
    }

    public void setBorrowRemark(String borrowRemark) {
        this.borrowRemark = borrowRemark;
    }

    public Date getBorrowAddtime() {
        return borrowAddtime;
    }

    public void setBorrowAddtime(Date borrowAddtime) {
        this.borrowAddtime = borrowAddtime;
    }

    public String getBorrowAccountMoneyValue() {
        return borrowAccountMoneyValue;
    }

    public void setBorrowAccountMoneyValue(String borrowAccountMoneyValue) {
        this.borrowAccountMoneyValue = borrowAccountMoneyValue;
    }
}