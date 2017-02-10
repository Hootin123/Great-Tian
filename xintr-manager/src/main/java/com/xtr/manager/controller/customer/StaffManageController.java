package com.xtr.manager.controller.customer;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.basic.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/7 18:43
 */
@Controller
@RequestMapping("customerStaff")
public class StaffManageController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StaffManageController.class);

    @Resource
    private CustomersService customersService;

    /**
     * 员工信息管理页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("staffManageInfo.htm")
    public ModelAndView toStaffManageInfoPage(ModelAndView mav) {
        mav.setViewName("xtr/customer/staff/staffmanageinfo");
        return mav;
    }

    /**
     * 员工详细信息页面
     *
     * @param mav
     * @param customerId
     * @return
     */
    @RequestMapping("staffManageDetail.htm")
    public ModelAndView toStaffManageInfoPage(ModelAndView mav, Long customerId) {
        ResultResponse resultResponse = customersService.selectCustomerInfoDetail(customerId);
        LOGGER.info("获取员工详细信息结果："+ JSON.toJSONString(resultResponse));
        mav.addObject("staffDetail", resultResponse.getData());
        mav.setViewName("xtr/customer/staff/staffManageDetail");
        return mav;
    }

    /**
     * 点击搜索按钮事件
     *
     * @param customersBean
     * @return
     */
    @RequestMapping("dataList.htm")
    @ResponseBody
    public ResultResponse staffManageSearch(CustomersBean customersBean) {
        ResultResponse resultResponse=customersService.selectCustomerInfoPageList(customersBean);
        LOGGER.info("根据过滤条件查询员工信息获取查询数据结果："+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }

}
