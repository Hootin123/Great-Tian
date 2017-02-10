package com.xtr.company.controller.validate;

import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanyProtocolsBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.service.company.CompanyProtocolsService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.validate.ValidateService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.RegexUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>工资单非空验证</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/29 15:19
 */
@Service("payrollValidate")
public class ValidatePayrollImpl implements ValidateService {

    @Resource
    private CustomersService customersService;

    @Resource
    private CompanyProtocolsService companyProtocolsService;

    /**
     * 非空验证
     *
     * @param obj
     * @throws Exception
     */
    public void validate(Object obj) throws Exception {
        if (obj != null) {
            Map map = (Map) obj;
            List<Map<String, String>> list = (List<Map<String, String>>) map.get("excelDataList");
            if (!list.isEmpty()) {
                CompanyMembersBean companyMembersBean = (CompanyMembersBean) map.get("user");
                //检查公司是否签订代发协议
                checkCompany(companyMembersBean, String.valueOf(map.get("payday")));
                for (Map<String, String> dataMap : list) {
                    if (StringUtils.isBlank(dataMap.get("B"))) {
                        throw new BusinessException("员工姓名不能为空！");
                    } else if (StringUtils.isBlank(dataMap.get("C")) && StringUtils.isBlank(dataMap.get("D"))) {
                        throw new BusinessException("身份证号码与工资卡卡号必填其一！");
                    }
//                    else if (StringUtils.isBlank(dataMap.get("D"))) {
//                        throw new BusinessException("工资卡卡号不能为空！");
//                    }
                    else if (StringUtils.isBlank(dataMap.get("C")) && StringUtils.isBlank(dataMap.get("E"))) {
                        throw new BusinessException("工资卡银行与身份证必填其一！");
                    } else if (StringUtils.isBlank(dataMap.get("F"))) {
                        throw new BusinessException("实发工资不能为空！");
                    }
                    //数据非法性检查
                    if (StringUtils.isNotBlank(dataMap.get("C")) && !RegexUtil.checkIdCard(dataMap.get("C"))) {
                        throw new BusinessException("身份证号【" + dataMap.get("C") + "】格式不正确");
                    } else if (StringUtils.isNotBlank(dataMap.get("D")) && !RegexUtil.checkBankNumber(dataMap.get("D"))) {
                        throw new BusinessException("银行卡卡号【" + dataMap.get("D") + "】格式不正确");
                    }
                    //检查金额格式是否正确
                    if (!RegexUtil.checkDecimals(dataMap.get("F"))){
                        throw new BusinessException("实发工资【" + dataMap.get("F") + "】格式不正确");
                    }

                    //检查员工信息
                    checkCustomerInfo(dataMap, companyMembersBean);

                }
            } else {
                throw new BusinessException("请添加工资单数据");
            }
        }

        //校验身份证号码，工资卡卡号、工资卡银行、在系统中是否存在
    }

    /**
     * 检查公司是否签署代发协议
     *
     * @param companyMembersBean
     * @param payday
     */
    private void checkCompany(CompanyMembersBean companyMembersBean, String payday) {
        //签约类型：1代发协议 2垫发协议',
        CompanyProtocolsBean companyProtocolsBean = companyProtocolsService.selectIsUserFulByTypeAndTime(companyMembersBean.getMemberCompanyId(), 1, DateUtil.stringToDate(payday, DateUtil.dateString));
        if (companyProtocolsBean == null) {
            throw new BusinessException("未签署代发协议，不能进行工资代发业务");
        } else {//协议状态:1待审批 2签约 3即将到期 4合约到期 5冻结
            if (companyProtocolsBean.getProtocolCurrentStatus().intValue() == 1) {
                throw new BusinessException("代发协议还未审批，不能进行工资代发业务");
            } else if (companyProtocolsBean.getProtocolCurrentStatus().intValue() == 4) {
                throw new BusinessException("代发协议合约已到期，不能进行工资代发业务");
            } else if (companyProtocolsBean.getProtocolCurrentStatus().intValue() == 5) {
                throw new BusinessException("代发协议合约已被冻结，不能进行工资代发业务");
            }
        }
    }

    /**
     * 检查员工信息
     *
     * @param dataMap
     */
    private void checkCustomerInfo(Map<String, String> dataMap, CompanyMembersBean companyMembersBean) throws BusinessException {
        //验证企业是否存在
//        CompanysBean companysBean = checkCompanyName(dataMap.get("B"));
//        //验证部门是否存在
//        checkDeptName(companysBean.getCompanyId(), dataMap.get("C"));
        //验证员工是否存在
        CustomersBean bean = new CustomersBean();
        bean.setCustomerCompanyId(companyMembersBean.getMemberCompanyId());
        List<CustomersBean> list = null;
        if (StringUtils.isNotBlank(dataMap.get("C"))) {
            bean.setCustomerIdcard(dataMap.get("C"));
            list = customersService.getCustomers(bean);
            if (list.isEmpty()) {
                throw new BusinessException("员工【" + dataMap.get("B") + "】身份证号【" + dataMap.get("C") + "】不存在");
            }
        } else {
            bean.setCustomerBanknumber(dataMap.get("D"));
            list = customersService.getCustomers(bean);
            if (list.isEmpty()) {
                throw new BusinessException("员工【" + dataMap.get("B") + "】工资卡卡号【" + dataMap.get("D") + "】不存在");
            }
        }

    }
}
