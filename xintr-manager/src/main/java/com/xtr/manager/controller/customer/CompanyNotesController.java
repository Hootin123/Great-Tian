package com.xtr.manager.controller.customer;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyNotesBean;
import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.api.service.company.CompanyNotesService;
import com.xtr.manager.util.SessionUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by xuewu on 2016/7/28.
 * 联系小记
 */
@Controller
@RequestMapping("/companynote")
public class CompanyNotesController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CompanyNotesController.class);

    @Resource
    private CompanyNotesService companyNotesService;


    @RequestMapping("add.htm")
    public ModelAndView add() {
        return new ModelAndView("xtr/customer/company/companyNoteAdd");
    }

    @RequestMapping("save.htm")
    @ResponseBody
    public ResultResponse toCompanyManageInfoPage(HttpServletRequest request, CompanyNotesBean companyNotesBean) {
        ResultResponse rr = new ResultResponse();
        rr.setSuccess(true);

        try {
            if(companyNotesBean.getCompanyId() == null) {
                rr.setSuccess(false);
                rr.setMessage("公司不能为空");
            }else if(StringUtils.isEmpty(companyNotesBean.getNoteTitle())){
                rr.setSuccess(false);
                rr.setMessage("标题不能为空");
            }else if(StringUtils.isEmpty(companyNotesBean.getCompanyId())){
                rr.setSuccess(false);
                rr.setMessage("内容不能为空");
            }else{
                SysUserBean sysUserBean = SessionUtils.getUser(request);
                companyNotesBean.setSysUserId(sysUserBean.getId());
                companyNotesBean.setSysUserName(sysUserBean.getUserName());
                companyNotesBean.setDelFlag(0);
                companyNotesBean.setRecordTime(new Date());
                companyNotesService.save(companyNotesBean);
            }
        } catch (Exception e) {
            LOGGER.error("添加联系小记异常：" + e + " 参数：" + companyNotesBean);
            rr.setSuccess(false);
            rr.setMessage(e.getMessage());
            e.printStackTrace();
        }
        return rr;
    }


    @RequestMapping("page.htm")
    @ResponseBody
    public ResultResponse page(HttpServletRequest request, @RequestParam Long cId, CompanyNotesBean companyNotesBean) {
        ResultResponse resultResponse = companyNotesService.selectPage(cId, companyNotesBean.getPageIndex(), companyNotesBean.getPageSize());
        return resultResponse;
    }

}
