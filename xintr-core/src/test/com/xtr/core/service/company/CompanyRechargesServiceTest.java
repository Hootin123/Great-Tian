package com.xtr.core.service.company;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.BaseTest;
import com.xtr.api.basic.QueryParam;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.SubAccountBean;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanyMoneyRecordsBean;
import com.xtr.api.domain.company.CompanyRechargesBean;
import com.xtr.api.dto.company.CompanyRechargeDto;
import com.xtr.api.service.account.SubAccountService;
import com.xtr.api.service.company.*;
import com.xtr.api.service.customer.CustomerMoneyRecordsService;
import com.xtr.api.service.customer.CustomerRechargesService;
import com.xtr.api.service.customer.CustomerSalarysService;
import com.xtr.api.service.task.TaskLogService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.AccountType;
import com.xtr.comm.util.RandomDataUtil;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>测试企业充值提现</p>
 *
 * @author 任齐
 * @createTime: 2016/7/1 17:31
 */
public class CompanyRechargesServiceTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyRechargesServiceTest.class);

    @Resource
    private CompanyRechargesService companyRechargesService;

    @Resource
    private CompanyMembersService companyMembersService;

    @Resource
    private CompanyMoneyRecordsService companyMoneyRecordsService;

    @Resource
    private SubAccountService subAccountService;

    @Resource
    private CustomerSalarysService customerSalarysService;

    @Resource
    private CustomerRechargesService customerRechargesService;

    @Resource
    private CustomerMoneyRecordsService customerMoneyRecordsService;

    @Resource
    private CompanySalaryExcelService companySalaryExcelService;

    @Test
    public void testSelectByPrimaryKey() throws Exception {
        LOGGER.info("::::=> 根据主键查询企业充值提现开始");

        CompanyRechargesBean companyRechargesBean = companyRechargesService.selectByPrimaryKey(13L);

        LOGGER.info("::::=> " + JSON.toJSONString(companyRechargesBean));

        LOGGER.info("::::=> 根据主键查询企业充值提现结束");
    }

    @Test
    public void testAddRecharge() throws Exception {
        LOGGER.info("::::=> 保存企业充值提现开始");

        CompanyRechargesBean companyRechargesBean = new CompanyRechargesBean();
        companyRechargesBean.setRechargeCompanyId(RandomDataUtil.getRandomLong(5));
        companyRechargesBean.setRechargeMoney(RandomDataUtil.getRandomMoney(5000));
        companyRechargesBean.setRechargeMoneynow(RandomDataUtil.getRandomMoney(5000));
        companyRechargesBean.setRechargeType(RandomDataUtil.getRandomState(2));
        companyRechargesBean.setRechargeAddtime(new Date());
        companyRechargesBean.setRechargeNumber(RandomDataUtil.getRandomID());
        companyRechargesBean.setRechargeBak(RandomDataUtil.getRandomID());
        companyRechargesBean.setRechargeStation(RandomDataUtil.getRandomState());
        companyRechargesBean.setRechargeClient(RandomDataUtil.getRandomState(2));
        companyRechargesBean.setRechargeMoneyserver(RandomDataUtil.getRandomMoney(100000));
        companyRechargesBean.setRechargeBank(RandomDataUtil.getRandomID());
        companyRechargesBean.setRechargeBanknumber(RandomDataUtil.getRandomID());
        companyRechargesBean.setRechargeIstoexcel(0);
        companyRechargesBean.setRechargeIstorecord(0);
        companyRechargesBean.setRechargeRecallTime(RandomDataUtil.getRandomDate());
        companyRechargesBean.setRechargeRecallResult(RandomDataUtil.getRandomState(4));
        companyRechargesBean.setRechargeAuditFirstTime(RandomDataUtil.getRandomDate());
        companyRechargesBean.setRechargeAuditSecondTime(RandomDataUtil.getRandomDate());
        companyRechargesBean.setRechargeAuditFirstMember(0L);

        try {
            companyRechargesService.addRecharge(companyRechargesBean);

            LOGGER.info("::::=> 保存企业充值提现结束");
        } catch (BusinessException e) {
            LOGGER.error(e.getMessage(), e);
        }

    }


    /**
     * 清空企业资产
     * <p>
     * 充值 提现 发工资
     *
     * @throws Exception
     */
    @Test
    public void testResetAccount() throws Exception {
        LOGGER.info("::::=> 重置企业账户资产开始");

        // 查找企业id
        String loginName = "admin@qq.com";

        CompanyMembersBean companyMembersBean = companyMembersService.findByMemberLogname(loginName);
        if (null != companyMembersBean) {

            Long companyId = companyMembersBean.getMemberCompanyId();

            // 清空企业资产数据
            companyMoneyRecordsService.deleteByCompanyId(companyId);
            companyRechargesService.deleteByCompanyId(companyId);
            companySalaryExcelService.deleteByCompanyId(companyId);

            // 更新企业md5
            SubAccountBean subAccountBean = subAccountService.selectByCustId(companyId, AccountType.COMPANY);
            subAccountBean.setAmout(new BigDecimal("200000.00"));
            subAccountBean.setCashAmout(new BigDecimal("200000.00"));
            subAccountBean.setUncashAmount(new BigDecimal("0.00"));
            subAccountBean.setFreezeCashAmount(new BigDecimal("0.00"));
            subAccountBean.setFreezeUncashAmount(new BigDecimal("0.00"));
            subAccountBean.setState("00");

            subAccountService.updateAccountCheckValue(subAccountBean);

            customerSalarysService.deleteByCompanyId(companyId);
            customerRechargesService.deleteByCompanyId(companyId);
            customerMoneyRecordsService.deleteByCompanyId(companyId);

            LOGGER.info("::::=> 重置企业账户资产结束");
        } else {
            LOGGER.info("不存在的企业," + loginName);
        }
    }


    @Test
    public void testPage(){
        System.out.println(companyRechargesService.selectPageList(1L,null,null,0,1,10));
    }
}
