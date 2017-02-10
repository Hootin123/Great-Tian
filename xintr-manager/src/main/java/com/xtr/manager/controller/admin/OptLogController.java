package com.xtr.manager.controller.admin;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.service.company.CompanyMemberLogsService;
import com.xtr.manager.controller.finance.CompanyRechargeController;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>企业用户操作日志</p>
 *
 * @author 任齐
 * @createTime: 2016/7/11 11:53
 */
@Controller("optLogController")
@RequestMapping("admin/logs")
public class OptLogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OptLogController.class);

    @Resource
    private CompanyMemberLogsService companyMemberLogsService;

    /**
     * 操作日志页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("index.htm")
    public ModelAndView index(ModelAndView mav) {
        mav.setViewName("xtr/admin/logs/index");
        return mav;
    }

    @RequestMapping("dataList.htm")
    @ResponseBody
    public ResultResponse dataList(@RequestParam(value = "date_str", required = false) String date_str,
                                   @RequestParam(value = "uname", required = false) String uname,
                                   @RequestParam(value = "memberId", required = false) Long memberId,
                                   @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                                   @RequestParam(value = "pageSize", defaultValue = "6") int pageSize) {

        return companyMemberLogsService.selectPageList(memberId, uname, date_str, pageIndex, pageSize);
    }

}
