package com.xtr.manager.controller.customer;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.BaseController;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyDepositBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.api.dto.company.CompanyDepositDto;
import com.xtr.api.service.company.CompanyDepositService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CompanyDepositConstants;
import com.xtr.comm.constant.CompanyProtocolConstants;
import com.xtr.manager.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/8/5 18:40
 */
@Controller
@RequestMapping("companyDeposit")
public class CompanyDepositController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDepositController.class);

    @Resource
    private CompanyDepositService companyDepositService;

    /**
     * 跳转提现账户管理页面
     * @param mav
     * @return
     */
    @RequestMapping("toCompanyDepositMainPage.htm")
    public ModelAndView toCompanyDepositMainPage(ModelAndView mav) {
        mav.addObject("protocolTypeMap", CompanyProtocolConstants.PROTOCOLTYPEMAPHAVE);
        mav.setViewName("xtr/customer/companyDeposit/companyDepositMain");
        return mav;
    }

    /**
     * 跳转提现账户添加页面
     * @param mav
     * @return
     */
    @RequestMapping("toCompanyDepositCreatePage.htm")
    public ModelAndView toCompanyDepositCreatePage(ModelAndView mav,Long memberId,Long companyId) {
        mav.addObject("memberId",memberId);
        mav.addObject("companyId",companyId);
        mav.setViewName("xtr/customer/companyDeposit/companyDepositCreate");
        return mav;
    }

    /**
     * 跳转提现账户修改页面
     * @param mav
     * @return
     */
    @RequestMapping("toCompanyDepositEditPage.htm")
    public ModelAndView toCompanyDepositEditPage(ModelAndView mav,Long depositId) {
        CompanyDepositBean companyDepositBean=companyDepositService.selectByPrimaryKey(depositId);
        mav.addObject("companyDepositBean",companyDepositBean);
        mav.setViewName("xtr/customer/companyDeposit/companyDepositEdit");
        return mav;
    }

    /**
     * 根据过滤条件获取所有用户提现账户
     * @param companyDepositDto
     * @return
     */
    @RequestMapping("dataList.htm")
    @ResponseBody
    public ResultResponse dataList(CompanyDepositDto companyDepositDto) {
        ResultResponse resultResponse=companyDepositService.selectByCondition(companyDepositDto);
        LOGGER.info("根据过滤条件获取所有用户提现账户,返回结果："+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 新增用户提现账户
     * @param companyDepositBean
     * @return
     */
    @RequestMapping("createCompanyDeposit.htm")
    @ResponseBody
    public ResultResponse createCompanyDeposit(HttpServletRequest request,CompanyDepositBean companyDepositBean) {
        LOGGER.info("新增用户提现账户,传递参数："+ JSON.toJSONString(companyDepositBean));
        ResultResponse resultResponse=new ResultResponse();
        if(companyDepositBean!=null){
            Date date=new Date();
            SysUserBean sysUserBean= SessionUtils.getUser(request);
            companyDepositBean.setDepositCreateUser(sysUserBean.getUserName());
            companyDepositBean.setDepositCreateTime(date);
            companyDepositBean.setDepositUpdateUser(sysUserBean.getUserName());
            companyDepositBean.setDepositUpdateTime(date);
            companyDepositBean.setDepositState(CompanyDepositConstants.DEPOSIT_STATE_YES);
            int count=companyDepositService.insertSelective(companyDepositBean);
            if(count<=0){
                resultResponse.setMessage("新增用户提现账户失败");
            }else{
                resultResponse.setSuccess(true);
            }
        }else{
            resultResponse.setMessage("新增用户提现账户,传递参数不能为空");
        }
        LOGGER.info("新增用户提现账户,返回结果："+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 修改用户提现账户
     * @param companyDepositBean
     * @return
     */
    @RequestMapping("editCompanyDeposit.htm")
    @ResponseBody
    public ResultResponse editCompanyDeposit(HttpServletRequest request,CompanyDepositBean companyDepositBean) {
        LOGGER.info("修改用户提现账户,传递参数："+ JSON.toJSONString(companyDepositBean));
        ResultResponse resultResponse=new ResultResponse();
        try{
            if(companyDepositBean!=null){
                companyDepositBean.setDepositUpdateUser(SessionUtils.getUser(request).getUserName());
                companyDepositBean.setDepositUpdateTime(new Date());
                int count=companyDepositService.updateByPrimaryKeySelective(companyDepositBean);
                if(count<=0){
                    resultResponse.setMessage("修改用户提现账户失败");
                }else{
                    resultResponse.setSuccess(true);
                }
            }else{
                resultResponse.setMessage("修改用户提现账户,传递参数不能为空");
            }
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("修改用户提现账户"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("修改用户提现账户失败");
            LOGGER.error("修改用户提现账户",e);
        }

        LOGGER.info("修改用户提现账户,返回结果："+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }
}
