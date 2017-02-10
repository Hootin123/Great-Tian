package com.xtr.company.controller.payday;

import com.xtr.api.basic.BaseController;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyDepsBean;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.service.company.CompanyDepsService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.comm.util.HtmlUtil;
import com.xtr.company.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>企业部门</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/21 13:50
 */
@Controller
public class CompanyDepsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDepsController.class);

    @Resource
    private CompanyDepsService companyDepsService;

    @Resource
    private CustomersService customersService;

    /**
     * 新增部门页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("addDeptPage.htm")
    private ModelAndView addMenbers(HttpServletRequest request,ModelAndView mav) {
        mav.setViewName("xtr/payday/adddept");
        mav.addObject("companyName",SessionUtils.getUser(request).getCompanyName());
        return mav;
    }

    /**
     * 新建子部门
     *
     * @param companyDepsBean
     * @return
     */
    @RequestMapping("addDept.htm")
    @ResponseBody
    public void addDept(HttpServletRequest request,HttpServletResponse response, CompanyDepsBean companyDepsBean) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            //验证部门是否存在
            resultResponse = companyDepsService.checkDeptByName(companyMembersBean.getMemberCompanyId(), companyDepsBean.getDepName());
            if (resultResponse.isSuccess() && resultResponse.getData() != null) {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("该部门已存在，不能重复添加");
            } else {
                companyDepsBean.setDepCompanyId(companyMembersBean.getMemberCompanyId());
                companyDepsBean.setCompanyId(companyMembersBean.getMemberCompanyId());
                companyDepsService.insert(companyDepsBean);
                resultResponse.setSuccess(true);
            }
        } catch (Exception e) {
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        HtmlUtil.writerJson(response, resultResponse);
    }

    /**
     * 修改部门名称
     *
     * @param deptId
     * @return
     */
    @RequestMapping("updateDeptName.htm")
    @ResponseBody
    public void updateDeptName(HttpServletResponse response,
                               @RequestParam("deptId") Long deptId,
                               @RequestParam("deptName") String deptName) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            companyDepsService.updateDeptName(deptId, deptName);
            resultResponse.setSuccess(true);
        } catch (Exception e) {
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        HtmlUtil.writerJson(response, resultResponse);
    }

    /**
     * 修改部门名称
     *
     * @param deptId
     * @return
     */
    @RequestMapping("deleteDept.htm")
    @ResponseBody
    public void deleteDept(HttpServletRequest request,HttpServletResponse response, @RequestParam("deptId") Long deptId) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            //判断该部门下是否还有企业员工
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            CustomersBean customersBean = new CustomersBean();
            customersBean.setCustomerCompanyId(companyMembersBean.getMemberCompanyId());
            customersBean.setCustomerDepId(deptId);
            //获取成员数据
            resultResponse = customersService.selectPageList(customersBean);
            if (resultResponse.getData() != null && !((List<CustomersBean>) resultResponse.getData()).isEmpty()) {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("请先将该部门下的成员移至其他部门");
            } else {
                companyDepsService.deleteDept(deptId);
                resultResponse.setSuccess(true);
            }
        } catch (Exception e) {
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        HtmlUtil.writerJson(response, resultResponse);
    }


    /**
     * 获取组织树
     *
     * @return
     */
    @RequestMapping("getCompanyTree.htm")
    @ResponseBody
    public ResultResponse getCompanyTree(HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        //获取组织树
        resultResponse.setData(companyDepsService.getCompanyTree(companyMembersBean.getMemberCompanyId()));
        resultResponse.setSuccess(true);
        return resultResponse;
    }


}
