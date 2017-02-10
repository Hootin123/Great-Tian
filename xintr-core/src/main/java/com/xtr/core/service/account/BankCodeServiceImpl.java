package com.xtr.core.service.account;

import com.xtr.api.domain.account.BankCodeBean;
import com.xtr.api.service.account.BankCodeService;
import com.xtr.core.persistence.reader.account.BankCodeReaderMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/10 14:50
 */
@Service("bankCodeService")
public class BankCodeServiceImpl implements BankCodeService {

    @Resource
    private BankCodeReaderMapper bankCodeReaderMapper;


    /**
     * 根据银行全称或者简称获取银行简码
     *
     * @param bankName
     * @return
     */
    public BankCodeBean selectByBankName(String bankName) {
        return bankCodeReaderMapper.selectByBankName(bankName);
    }
}
