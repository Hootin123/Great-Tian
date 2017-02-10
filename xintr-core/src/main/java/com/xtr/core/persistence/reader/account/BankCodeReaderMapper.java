package com.xtr.core.persistence.reader.account;

import com.xtr.api.domain.account.BankCodeBean;

import java.util.List;

public interface BankCodeReaderMapper {

    /**
     * 根据指定主键获取一条数据库记录,bank_code
     *
     * @param id
     */
    BankCodeBean selectByPrimaryKey(Long id);

    /**
     * 根据银行全称或者简称获取银行简码
     *
     * @param bankName
     * @return
     */
    BankCodeBean selectByBankName(String bankName);

    /**
     * 获取所有的银行名称
     * @return
     */
    List<BankCodeBean> selectAll();
}