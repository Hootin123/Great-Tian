package com.xtr.company.controller.department;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xtr.api.basic.BaseController;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyDepsBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.service.company.CompanyDeptService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.customer.CustomersStationService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.util.HtmlUtil;
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

/**
 * @Author Xuewu
 * @Date 2016/8/15.
 */
@Controller
@RequestMapping("dept")
public class CompanyDeptController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDeptController.class);

    @Resource
    private CompanyDeptService companyDeptService;

    @Resource
    private CustomersService customersService;

    @Resource
    private CustomersStationService customersStationService;

    @RequestMapping("deptIndex.htm")
    public ModelAndView index(HttpServletRequest request) {
        CompanysBean loginCompanyObj = getLoginCompanyObj(request);
        ModelAndView modelAndView = new ModelAndView("xtr/deps/index");
        modelAndView.addObject("treeData", JSONObject.toJSON(companyDeptService.getTree(loginCompanyObj.getCompanyId())));
        modelAndView.addObject("companyName", loginCompanyObj.getCompanyName());
        int customerCount = customersService.selectCountByDepId(0L, loginCompanyObj.getCompanyId());
        int unassign = customersService.selectCountByDepId(null, loginCompanyObj.getCompanyId());
        modelAndView.addObject("customerCount", customerCount);
        modelAndView.addObject("unassign", unassign);
        return modelAndView;
    }

    @RequestMapping("addDept.htm")
    public ModelAndView addDept(HttpServletRequest request, @RequestParam Long deptId, @RequestParam Boolean isEdit) {
        CompanysBean loginCompanyObj = getLoginCompanyObj(request);
        ModelAndView modelAndView = new ModelAndView("xtr/deps/addDept");
        long id = deptId;
        if (isEdit) {
            CompanyDepsBean companyDepsBean = companyDeptService.selectByPrimaryKey(deptId);

            modelAndView.addObject("name", companyDepsBean.getDepName());
            modelAndView.addObject("id", companyDepsBean.getDepId());

            deptId = companyDepsBean.getDepParentId();
        }

        modelAndView.addObject("depts", companyDeptService.getAllDept(loginCompanyObj.getCompanyId(), isEdit ? id : null));
        modelAndView.addObject("companyName", loginCompanyObj.getCompanyName());
        modelAndView.addObject("deptId", deptId);
        return modelAndView;
    }

    @RequestMapping("setLeader.htm")
    public ModelAndView setLeader(HttpServletRequest request, @RequestParam Long deptId, @RequestParam(required = false) boolean isFirst) {
        CompanysBean loginCompanyObj = getLoginCompanyObj(request);
        ModelAndView modelAndView = new ModelAndView("xtr/deps/setLeader");

        modelAndView.addObject("customers", customersService.selectUserCustomerByCompanyId(loginCompanyObj.getCompanyId()));

        CompanyDepsBean companyDepsBean = companyDeptService.selectByPrimaryKey(deptId);
        modelAndView.addObject("leaderId", companyDepsBean.getDepLeader());

        modelAndView.addObject("deptId", deptId);
        modelAndView.addObject("isFirst", isFirst);
        return modelAndView;
    }

    @RequestMapping("saveDept.htm")
    @ResponseBody
    public ResultResponse save(HttpServletRequest request, CompanyDepsBean companyDepsBean) {
        Long loginUserId = getLoginUserId(request);
        CompanysBean loginCompanyObj = getLoginCompanyObj(request);
        try {
            companyDepsBean.setCompanyId(loginCompanyObj.getCompanyId());
            companyDepsBean.setDepCompanyId(loginCompanyObj.getCompanyId());
            boolean flag = companyDeptService.saveOrUpdate(companyDepsBean, loginUserId);
            if (flag) {
                return ResultResponse.buildSuccess(null).message("操作成功");
            } else {
                return ResultResponse.buildFail(null).message("操作失败");
            }
        } catch (Exception be) {
            LOGGER.error(be.getMessage(), be);
            return ResultResponse.buildFail(null).message(be.getMessage());
        }

    }

    @RequestMapping("delDept.htm")
    @ResponseBody
    public ResultResponse delDept(HttpServletRequest request, long id) {
        Long loginUserId = getLoginUserId(request);
        CompanysBean loginCompanyObj = getLoginCompanyObj(request);
        try {
            boolean flag = companyDeptService.deleteDept(id, loginCompanyObj.getCompanyId());
            if (flag) {
                return ResultResponse.buildSuccess(null).message("操作成功");
            } else {
                return ResultResponse.buildFail(null).message("操作失败");
            }
        } catch (Exception be) {
            LOGGER.error(be.getMessage(), be);
            return ResultResponse.buildFail(null).message(be.getMessage());
        }

    }

    @RequestMapping("tree.htm")
    @ResponseBody
    public ResultResponse tree(HttpServletRequest request) {
        CompanysBean loginCompanyObj = getLoginCompanyObj(request);
        return ResultResponse.buildSuccess(companyDeptService.getTree(loginCompanyObj.getCompanyId()));
    }

    @RequestMapping("employees.htm")
    @ResponseBody
    public ResultResponse employees(HttpServletRequest request, @RequestParam(required = false) Long depId) {
        CompanysBean loginCompanyObj = getLoginCompanyObj(request);
        return ResultResponse.buildSuccess(customersService.selectByDepId(depId, loginCompanyObj.getCompanyId()));
    }

    @RequestMapping("moveEmployees.htm")
    @ResponseBody
    public ResultResponse moveEmployees(HttpServletRequest request, @RequestParam(required = false) Long depId, @RequestParam Long empId) {
        CompanysBean loginCompanyObj = getLoginCompanyObj(request);
        return ResultResponse.buildSuccess(customersStationService.moveCustomerDept(depId, empId, loginCompanyObj.getCompanyId()));
    }

    @RequestMapping("updateLeader.htm")
    @ResponseBody
    public ResultResponse updateLeader(HttpServletRequest request, @RequestParam Long depId, @RequestParam(required = false) Long leaderId) {
        return ResultResponse.buildSuccess(companyDeptService.updateLeader(depId, leaderId));
    }

    /**
     * 根据部门名称模糊查询部门信息
     *
     * @param request
     * @param response
     * @param deptName
     */
    @RequestMapping("selectByDeptName.htm")
    @ResponseBody
    public void selectByDeptName(HttpServletRequest request, HttpServletResponse response,
                                 String deptName) {
        CompanysBean loginCompanyObj = getLoginCompanyObj(request);
        HtmlUtil.writerJson(response, companyDeptService.selectByDeptName(loginCompanyObj.getCompanyId(), deptName));
    }

    /**
     * 修改部门名称界面
     * @param deptId
     * @return
     */
    @RequestMapping("toEditDeptPage.htm")
    public ModelAndView toEditDeptPage(@RequestParam Long deptId) {

        ModelAndView modelAndView = new ModelAndView("xtr/deps/editDept");
        CompanyDepsBean companyDepsBean = companyDeptService.selectByPrimaryKey(deptId);
        modelAndView.addObject("name", companyDepsBean.getDepName());
        modelAndView.addObject("id", companyDepsBean.getDepId());
        return modelAndView;
    }

    /**
     * 修改部门名称
     * @param request
     * @param companyDepsBean
     * @param newDeptName
     * @return
     */
    @RequestMapping("modifyDepts.htm")
    @ResponseBody
    public ResultResponse modifyDepts(HttpServletRequest request, CompanyDepsBean companyDepsBean,String newDeptName) {
        Long loginUserId = getLoginUserId(request);
        ResultResponse resultResponse=new ResultResponse();
        try {
            companyDeptService.modifyDepts(companyDepsBean, loginUserId,newDeptName);
            resultResponse.setSuccess(true);
        } catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("修改部门名称"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("修改部门名称失败,请重试或联系管理员");
            LOGGER.error("修改部门名称",e);
        }
        LOGGER.info("修改部门名称,返回结果:" + JSON.toJSONString(resultResponse));
        return resultResponse;

    }

    /**
     * 删除部门弹框
     * @param deptId
     * @return
     */
    @RequestMapping("toDelDeptPage.htm")
    public ModelAndView toDelDeptPage(@RequestParam Long deptId) {
        ModelAndView modelAndView = new ModelAndView("xtr/deps/deletDept");
        modelAndView.addObject("id", deptId);
        return modelAndView;
    }

    @RequestMapping("toAddDeptPage.htm")
    public ModelAndView toAddDeptPage(HttpServletRequest request, @RequestParam Long deptId) {
        CompanysBean loginCompanyObj = getLoginCompanyObj(request);
        ModelAndView modelAndView = new ModelAndView("xtr/deps/addDept");
//        long id = deptId;
//        if (isEdit) {
//            CompanyDepsBean companyDepsBean = companyDeptService.selectByPrimaryKey(deptId);
//
//            modelAndView.addObject("name", companyDepsBean.getDepName());
//            modelAndView.addObject("id", companyDepsBean.getDepId());
//
//            deptId = companyDepsBean.getDepParentId();
//        }
        CompanyDepsBean companyDepsBean = companyDeptService.selectByPrimaryKey(deptId);
//        modelAndView.addObject("depts", companyDeptService.getAllDept(loginCompanyObj.getCompanyId(), null));
//        modelAndView.addObject("companyName", loginCompanyObj.getCompanyName());
        modelAndView.addObject("depParentId", companyDepsBean.getDepId());
        modelAndView.addObject("parentDepName", companyDepsBean.getDepName());
        return modelAndView;
    }

    /**
     * 新增部门
     * @param request
     * @param companyDepsBean
     * @return
     */
    @RequestMapping("createDept.htm")
    @ResponseBody
    public ResultResponse createDept(HttpServletRequest request, CompanyDepsBean companyDepsBean) {
        Long loginUserId = getLoginUserId(request);
        ResultResponse resultResponse=new ResultResponse();
        try {
            CompanysBean loginCompanyObj = getLoginCompanyObj(request);
            companyDepsBean.setCompanyId(loginCompanyObj.getCompanyId());
            companyDepsBean.setDepCompanyId(loginCompanyObj.getCompanyId());
            companyDeptService.createDept(companyDepsBean, loginUserId);
            resultResponse.setSuccess(true);
        } catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("新增部门"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("新增部门失败,请重试或联系管理员");
            LOGGER.error("新增部门",e);
        }
        LOGGER.info("新增部门,返回结果:" + JSON.toJSONString(resultResponse));
        return resultResponse;

    }
}
