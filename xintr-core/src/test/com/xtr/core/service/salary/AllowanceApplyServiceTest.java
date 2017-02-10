package com.xtr.core.service.salary;

import com.xtr.BaseTest;
import com.xtr.api.domain.salary.AllowanceSettingBean;
import com.xtr.api.service.salary.AllowanceApplyService;
import com.xtr.api.service.salary.AllowanceSettingService;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/8/19 13:31.
 */
public class AllowanceApplyServiceTest extends BaseTest {

    @Resource
    private AllowanceApplyService allowanceApplyService;

    @Resource
    private AllowanceSettingService allowanceSettingService;

    @Test
    public void testMembers(){

        Map<Long, Map<Long, Long>> map = allowanceApplyService.getCompanyAllowanceApplyMembers(302L);
        System.out.println(map);
    }

    @Test
    public void testGetCompanyOrg(){
        System.out.println(allowanceSettingService.getOrgMembers(302L));
    }
}
