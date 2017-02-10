package com.xtr.core.service.company;

import com.alibaba.fastjson.JSON;
import com.xtr.BaseTest;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMoneyRecordsBean;
import com.xtr.api.service.company.CompanyMoneyRecordsService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>测试企业资金变动记录</p>
 *
 * @author 任齐
 * @createTime: 2016/7/1 17:26
 */
public class CompanyMoneyRecordsServiceTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyMoneyRecordsServiceTest.class);

    @Resource
    private CompanyMoneyRecordsService companyMoneyRecordsService;

    @Test
    public void testSelectByPrimaryKey() throws Exception {
        LOGGER.info("::::=> 根据主键查询企业账户资金变动记录开始");

        CompanyMoneyRecordsBean companyMoneyRecordsBean = companyMoneyRecordsService.selectByPrimaryKey(13L);

        LOGGER.info("::::=> " + JSON.toJSONString(companyMoneyRecordsBean));

        LOGGER.info("::::=> 根据主键查询企业账户资金变动记录结束");
    }

    @Test
    public void testSelectPageList() throws Exception {
        LOGGER.info("::::=> 分页查询企业账户资金变动记录开始");

        CompanyMoneyRecordsBean companyMoneyRecordsBean = new CompanyMoneyRecordsBean();
        companyMoneyRecordsBean.setPageIndex(0);
        companyMoneyRecordsBean.setPageSize(5);
        companyMoneyRecordsBean.setRecordType(2);

        ResultResponse resultResponse = companyMoneyRecordsService.selectPageList(companyMoneyRecordsBean);

        LOGGER.info("::::=> " + JSON.toJSONString(resultResponse));

        LOGGER.info("::::=> 分页查询企业账户资金变动记录结束");
    }

}
