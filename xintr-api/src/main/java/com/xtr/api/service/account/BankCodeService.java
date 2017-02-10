package com.xtr.api.service.account;

import com.xtr.api.domain.account.BankCodeBean;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/10 14:46
 */

public interface BankCodeService {

    /**
     * 根据银行全称或者简称获取银行简码
     *
     * @param bankName
     * @return
     */
    BankCodeBean selectByBankName(String bankName);
}
