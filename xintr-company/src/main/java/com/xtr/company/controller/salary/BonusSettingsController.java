package com.xtr.company.controller.salary;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.salary.BonusSettingsBean;
import com.xtr.api.service.salary.BonusSettingsService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.company.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>奖金设置控制器</p>
 *
 * @author 任齐
 * @createTime: 2016/8/15 13:13.
 */
@Controller
@RequestMapping("salary_setting")
public class BonusSettingsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BonusSettingsController.class);

    @Resource
    private BonusSettingsService bonusSettingsService;

    /**
     * 奖金设置数据
     *
     * @return
     */
    @RequestMapping(value = "bonus_settings.htm", method = RequestMethod.GET)
    @ResponseBody
    public ResultResponse bonusSettings(HttpServletRequest request) {

        ResultResponse resultResponse = new ResultResponse();
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        Long comapnyId = companyMembersBean.getMemberCompanyId();

        BonusSettingsBean bonusSettingsBean = new BonusSettingsBean();
        bonusSettingsBean.setCompanyId(comapnyId);

        try {
            List<BonusSettingsBean> bonusSettingsBeanList = bonusSettingsService.getCompanyBonusList(bonusSettingsBean);
            resultResponse.setData(bonusSettingsBeanList);
            resultResponse.setSuccess(true);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setSuccess(false);
        }
        return resultResponse;
    }

    /**
     * 保存奖金
     *
     * @param mav
     * @param request
     * @param bonusNames
     * @return
     */
    @RequestMapping(value = "save_bonus_settings.htm", method = RequestMethod.POST)
    @ResponseBody
    public ResultResponse saveBonusSettings(ModelAndView mav,
                                   HttpServletRequest request,
                                    @RequestParam(value="bonusNames") String bonusNames,
                                            @RequestParam(value="delIds") String delIds) {

        ResultResponse resultResponse = new ResultResponse();
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        Long comapnyId = companyMembersBean.getMemberCompanyId();
        Long memberId = companyMembersBean.getMemberId();
        Long deptId = companyMembersBean.getMemberDepId();

        try {

            bonusSettingsService.saveBonusSettings(comapnyId, memberId, deptId, bonusNames);
            bonusSettingsService.deleteBonusSettings(comapnyId, delIds);
            resultResponse.setSuccess(true);
        } catch (BusinessException e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage("奖金类型保存失败");
            resultResponse.setSuccess(false);
        }

        return resultResponse;
    }

}
