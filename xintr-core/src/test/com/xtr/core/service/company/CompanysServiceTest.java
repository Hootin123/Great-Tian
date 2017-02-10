package com.xtr.core.service.company;

import com.xtr.BaseTest;
import com.xtr.api.domain.company.CompanyActivityBean;
import com.xtr.api.service.company.CompanysService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

public class CompanysServiceTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanysServiceTest.class);

    @Resource
    private CompanysService companysService;

    @Test
    public void testsaveCompanyActivity() throws Exception {
        CompanyActivityBean companyActivityBean = new CompanyActivityBean();
        companysService.saveCompanyActivity(companyActivityBean);
        System.out.println(companyActivityBean.getActivityId());
    }

}
