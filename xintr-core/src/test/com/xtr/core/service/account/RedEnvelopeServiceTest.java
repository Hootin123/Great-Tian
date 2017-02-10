package com.xtr.core.service.account;

import com.xtr.BaseTest;
import com.xtr.api.service.account.RedEnvelopeService;
import com.xtr.comm.basic.BusinessException;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/8/23 13:34.
 */
public class RedEnvelopeServiceTest extends BaseTest {

    @Resource
    private com.xtr.api.service.account.RedEnvelopeService redEnvelopeService;
    /**
     * 测试事务
     */
    @Test
    public void testTx(){
        try {
            redEnvelopeService.sendRegisterEnvelope(1L);
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }

}
