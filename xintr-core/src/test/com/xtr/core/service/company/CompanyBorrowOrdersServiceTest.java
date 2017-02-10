package com.xtr.core.service.company;

import com.alibaba.fastjson.JSON;
import com.xtr.BaseTest;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyBorrowOrdersBean;
import com.xtr.api.dto.company.CompanyPayWithDto;
import com.xtr.api.service.company.CompanyBorrowOrdersService;
import com.xtr.comm.basic.BusinessException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>企业借款订单服务测试</p>
 *
 * @author 任齐
 * @createTime: 2016/6/30 18:31
 */
public class CompanyBorrowOrdersServiceTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyBorrowOrdersServiceTest.class);

    @Autowired
    private CompanyBorrowOrdersService companyBorrowOrdersService;

    @Test
    public void testSelectByPrimaryKey() throws Exception {
        LOGGER.info("::::=> 根据主键查询企业订单开始");

        CompanyBorrowOrdersBean companyBorrowOrdersBean = companyBorrowOrdersService.selectByPrimaryKey(15L);
        LOGGER.info("::::=> " + JSON.toJSONString(companyBorrowOrdersBean));

        LOGGER.info("::::=> 根据主键查询企业订单结束");
    }

    @Test
    public void testSelectCompanyBorrowOrderDetail() throws Exception {
        LOGGER.info("::::=> 根据借款订单id查询借款详情开始");

        CompanyPayWithDto companyPayWithDto = companyBorrowOrdersService.selectCompanyBorrowOrderDetail(15L);
        LOGGER.info("::::=> " + JSON.toJSONString(companyPayWithDto));

        LOGGER.info("::::=> 根据借款订单id查询借款详情结束");
    }

    @Test
    public void testSelectPageList() throws Exception {
        LOGGER.info("::::=> 分页查询订单开始");
        CompanyBorrowOrdersBean companyBorrowOrdersBean = new CompanyBorrowOrdersBean();
        companyBorrowOrdersBean.setPageIndex(0);
        companyBorrowOrdersBean.setPageSize(5);

        ResultResponse resultResponse = companyBorrowOrdersService.selectPageList(companyBorrowOrdersBean);
        LOGGER.info("::::=> " + JSON.toJSONString(resultResponse));

        LOGGER.info("::::=> 分页查询订单结束");
    }

    @Test
    public void testAddBorrowOrder() throws Exception {
        LOGGER.info("::::=> 保存企业借款订单开始");
        CompanyBorrowOrdersBean companyBorrowOrdersBean = new CompanyBorrowOrdersBean();
        companyBorrowOrdersBean.setOrderCompanyId(3L);
        companyBorrowOrdersBean.setOrderDepId(10L);
        companyBorrowOrdersBean.setOrderMoney(new BigDecimal(909090.9090));
        companyBorrowOrdersBean.setOrderAddtime(new Date());
        companyBorrowOrdersBean.setOrderNumber("NNNNNNNNNNNNb");
        companyBorrowOrdersBean.setOrderState(2);
        companyBorrowOrdersBean.setOrderCanceltime(new Date());
        companyBorrowOrdersBean.setOrderSign(10);
        companyBorrowOrdersBean.setOrderInterestType(4);
        companyBorrowOrdersBean.setOrderRepayType(3);
        companyBorrowOrdersBean.setOrderRepayStarttime(new Date());
        companyBorrowOrdersBean.setOrderRepayEndtime(new Date());
        companyBorrowOrdersBean.setOrderCycle(6);
        companyBorrowOrdersBean.setOrderCycleUnit(2);
        companyBorrowOrdersBean.setOrderRate(new BigDecimal(2.12));
        companyBorrowOrdersBean.setOrderPropId(10);
        companyBorrowOrdersBean.setOrderPropCouponType(0);
        companyBorrowOrdersBean.setOrderMoneyArrival(new BigDecimal(2902902.11));
        companyBorrowOrdersBean.setOrderActualMoney(new BigDecimal(12323.88));

        try {
            int result = companyBorrowOrdersService.addBorrowOrder(companyBorrowOrdersBean);
            LOGGER.info("::::=> result = " + result);

            LOGGER.info("::::=> 保存企业借款订单结束");
        } catch (BusinessException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
