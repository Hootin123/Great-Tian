package com.xtr.company.controller.salary;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanyProtocolsBean;
import com.xtr.api.domain.salary.AllowanceSettingBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.dto.salary.AllowanceSettingDto;
import com.xtr.api.service.company.CompanyProtocolsService;
import com.xtr.api.service.salary.AllowanceApplyService;
import com.xtr.api.service.salary.AllowanceSettingService;
import com.xtr.api.service.salary.PayCycleService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.jd.util.StringUtils;
import com.xtr.comm.util.DateUtil;
import com.xtr.company.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>津贴设置控制器</p>
 *
 * @author 任齐
 * @createTime: 2016/8/15 14:12.
 */
@Controller
@RequestMapping("salary_setting")
public class AllowanceSettingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllowanceSettingController.class);

    @Resource
    private AllowanceSettingService allowanceSettingService;

    @Resource
    private AllowanceApplyService allowanceApplyService;

    @Resource
    private PayCycleService payCycleService;

    @Resource
    private CompanyProtocolsService companyProtocolsService;

    /**
     * 津贴设置页面
     *
     * @param mav
     * @return
     */
    @RequestMapping(value = "allowance.htm", method = RequestMethod.GET)
    public ModelAndView allowance(HttpServletRequest request, ModelAndView mav) {

        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        Long comapnyId = companyMembersBean.getMemberCompanyId();

        // 读取记薪周期
        PayCycleBean payCycleBean = payCycleService.selectByCompanyId(comapnyId);
        if(null != payCycleBean){

            Date start = payCycleBean.getStartDate();
            Date end = payCycleBean.getEndDate();

            int startDay = start.getDate();
            int endDay = end.getDate();

            String endStr = "当月";
            String endDayStr = endDay + "";

            if(start.getMonth() != end.getMonth()){
                endStr = "次月";
            }
            if(endDay == 1){
                endDayStr = "月底";
            }

            mav.addObject("startDay", startDay);
            mav.addObject("endStr", endStr);
            mav.addObject("endDayStr", endDayStr);


            //获取社保公积金协议  1代发协议 2垫发协议 3社保代缴协议
            CompanyProtocolsBean companyProtocolsBean = companyProtocolsService.selectByCorpProtocol(comapnyId,3);
            if (companyProtocolsBean == null){
                mav.addObject("isSign", "0");
            }
        }

        mav.setViewName("xtr/salary/allowance");
        return mav;
    }

    /**
     * 获取津贴设置的数据
     *
     * @return
     */
    @RequestMapping(value = "get_allowances.htm", method = RequestMethod.GET)
    @ResponseBody
    public ResultResponse getAllowances(HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();

        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        Long comapnyId = companyMembersBean.getMemberCompanyId();

        try {
            AllowanceSettingBean allowanceSettingBean = new AllowanceSettingBean();
            allowanceSettingBean.setCompanyId(comapnyId);

            List<AllowanceSettingBean> allowanceSettingBeens = allowanceSettingService.getCompanyAllowanceSettingList(allowanceSettingBean);
            resultResponse.setData(allowanceSettingBeens);
            resultResponse.setSuccess(true);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setSuccess(false);
        }
        return resultResponse;
    }

    /**
     * 保存津贴
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "save_allowance.htm", method = RequestMethod.POST)
    @ResponseBody
    public ResultResponse saveAllowance(
                                        HttpServletRequest request,
                                        @RequestParam(value = "saveData", required = false) String saveData,
                                        @RequestParam(value = "updateData", required = false) String updateData,
                                        @RequestParam(value = "delData", required = false) String delData) {

        ResultResponse resultResponse = new ResultResponse();

        try {

            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            Long comapnyId = companyMembersBean.getMemberCompanyId();

            if(StringUtils.isNotBlank(saveData)){
                // 新增
                AllowanceSettingDto allowanceSettingBeen = JSON.parseObject(saveData, AllowanceSettingDto.class);

                Long memberId = companyMembersBean.getMemberId();
                Long deptId = companyMembersBean.getMemberDepId();
                Long allowId = allowanceSettingService.saveAllowanceSetting(comapnyId, memberId, deptId, allowanceSettingBeen);
                resultResponse.setData(allowId);
            }

            if(StringUtils.isNotBlank(updateData)){
                // 更新
                List<AllowanceSettingDto> updateAllowanceSettingData = JSON.parseArray(updateData, AllowanceSettingDto.class);
                allowanceSettingService.updateAllowanceSettings(comapnyId, updateAllowanceSettingData);
            }

            if(StringUtils.isNotBlank(delData)){
                // 删除
                allowanceSettingService.deleteAllowanceSettings(comapnyId, delData);
            }

            resultResponse.setSuccess(true);
        } catch (BusinessException e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setSuccess(false);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setSuccess(false);
        }
        return resultResponse;
    }

    /**
     * 获取树形组织架构
     *
     * @return
     */
    @RequestMapping(value = "get_orgmembers.htm", method = RequestMethod.GET)
    @ResponseBody
    public ResultResponse get_orgmembers(HttpServletRequest request) {
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        Long comapnyId = companyMembersBean.getMemberCompanyId();
        return allowanceSettingService.getOrgMembers(comapnyId);
    }

    /**
     * 获取某个津贴的适用员工
     *
     * @param allowanceId
     * @return
     */
    @RequestMapping(value = "allowancemembers/{allowanceId}.htm", method = RequestMethod.GET)
    @ResponseBody
    public ResultResponse getAllowanceMembers(@PathVariable Long allowanceId) {
        return  allowanceApplyService.getAllowanceMembers(allowanceId);
    }
}
