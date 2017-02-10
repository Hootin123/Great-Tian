package com.xtr.core.service.company;

import com.alibaba.fastjson.JSON;
import com.xtr.BaseTest;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyBorrowBillsBean;
import com.xtr.api.service.company.CompanyBorrowBillsService;
import com.xtr.comm.basic.BusinessException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>企业借款账单服务测试</p>
 *
 * @author 任齐
 * @createTime: 2016/6/30 18:05
 */
public class CompanyBorrowBillsServiceTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyBorrowBillsServiceTest.class);

    @Autowired
    private CompanyBorrowBillsService companyBorrowBillsService;

    @Test
    public void testSelectListByOrderId() throws Exception {
        LOGGER.info("::::=> 根据企业订单id查询账单列表开始");

        List<CompanyBorrowBillsBean> companyBorrowBillsBeanList = companyBorrowBillsService.selectListByOrderId(15L);
        LOGGER.info("::::=> " + JSON.toJSONString(companyBorrowBillsBeanList));

        LOGGER.info("::::=> 根据企业订单id查询账单列表结束");
    }

    @Test
    public void testSelectPageList() throws Exception {
        LOGGER.info("::::=> 分页查询企业订单开始");

        CompanyBorrowBillsBean companyBorrowBillsBean = new CompanyBorrowBillsBean();
        companyBorrowBillsBean.setPageIndex(0);
        companyBorrowBillsBean.setPageSize(10);

        ResultResponse resultResponse = companyBorrowBillsService.selectPageList(companyBorrowBillsBean);
        LOGGER.info("::::=> " + JSON.toJSONString(resultResponse));
        LOGGER.info("::::=> 分页查询企业订单结束");
    }

    @Test
    public void testAddBorrowBills() throws Exception {
        LOGGER.info("::::=> 保存企业订单开始");
        CompanyBorrowBillsBean companyBorrowBillsBean = new CompanyBorrowBillsBean();
        companyBorrowBillsBean.setBillIndex(10);
        companyBorrowBillsBean.setBillBorrowOrderId(15L);
        companyBorrowBillsBean.setBillCompanyId(2L);
        companyBorrowBillsBean.setBillDepId(10L);
        companyBorrowBillsBean.setBillMoney(new BigDecimal(66666.66));
        companyBorrowBillsBean.setBillMoneyBase(new BigDecimal(33333.33));
        companyBorrowBillsBean.setBillMoneyInterest(new BigDecimal(123.654));
        companyBorrowBillsBean.setBillAddtime(new Date());

        try {
            int result = companyBorrowBillsService.addBorrowBills(companyBorrowBillsBean);
            LOGGER.info("::::=> result = " + result);
            LOGGER.info("::::=> 保存企业订单结束");
        } catch (BusinessException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
