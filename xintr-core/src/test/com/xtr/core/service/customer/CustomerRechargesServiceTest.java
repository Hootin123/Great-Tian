package com.xtr.core.service.customer;

import com.xtr.BaseTest;
import com.xtr.api.domain.account.SubAccountBean;
import com.xtr.api.service.account.SubAccountService;
import com.xtr.comm.constant.AccountType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.math.BigDecimal;

public class CustomerRechargesServiceTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRechargesServiceTest.class);

    @Resource
    private SubAccountService subAccountService;

    @Test
    public void testResetAccount() throws Exception {
        LOGGER.info("::::=> 重置个人账户资产开始");

        Long memberId = 218L;

        SubAccountBean subAccountBean = subAccountService.selectByCustId(memberId, AccountType.PEOPLE);
        subAccountBean.setAmout(new BigDecimal("0.00"));
        subAccountBean.setCashAmout(new BigDecimal("0.00"));
        subAccountBean.setUncashAmount(new BigDecimal("0.00"));
        subAccountBean.setFreezeCashAmount(new BigDecimal("0.00"));
        subAccountBean.setFreezeUncashAmount(new BigDecimal("0.00"));
        subAccountBean.setState("00");

        subAccountService.updateAccountCheckValue(subAccountBean);

        LOGGER.info("::::=> 重置个人账户资产结束");

    }
}
