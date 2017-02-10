package com.xtr.company.controller.validate;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyDepsBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.service.company.CompanyDepsService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.validate.ValidateService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.RegexUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>员工信息非空校验</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/29 15:45
 */
@Service("validateCustomer")
public class ValidateCustomerImpl implements ValidateService {

    @Resource
    private CustomersService customersService;

    @Resource
    private CompanysService companysService;

    @Resource
    private CompanyDepsService companyDepsService;

    /**
     * 非空验证
     *
     * @param obj
     * @throws Exception
     */
    public void validate(Object obj) throws Exception {
        if (obj != null) {
            List<Map<String, String>> list = (List<Map<String, String>>) ((Map) obj).get("excelDataList");
            CompanysBean companysBean = (CompanysBean) ((Map) obj).get("company");
//            List<Map<String, String>> list = (List<Map<String, String>>) obj;
            if (!list.isEmpty()) {
                for (Map<String, String> dataMap : list) {
                    if (StringUtils.isBlank(dataMap.get("B"))) {
                        throw new BusinessException("公司名称不能为空！");
                    } else if (StringUtils.isBlank(dataMap.get("C"))) {
                        throw new BusinessException("部门名称不能为空！");
                    } else if (StringUtils.isBlank(dataMap.get("D"))) {
                        throw new BusinessException("员工姓名不能为空！");
                    } else if (StringUtils.isBlank(dataMap.get("E"))) {
                        throw new BusinessException("性别不能为空！");
                    } else if (StringUtils.isBlank(dataMap.get("F"))) {
                        throw new BusinessException("职位不能为空！");
                    } else if (StringUtils.isBlank(dataMap.get("G"))) {
                        throw new BusinessException("身份证号码为空！");
                    } else if (StringUtils.isBlank(dataMap.get("H"))) {
                        throw new BusinessException("本人联系电话为空！");
                    } else if (StringUtils.isBlank(dataMap.get("I"))) {
                        throw new BusinessException("开户行为空！");
                    } else if (StringUtils.isBlank(dataMap.get("J"))) {
                        throw new BusinessException("银行卡号为空！");
                    }
                    //数据非法性检查
                    if (!RegexUtil.checkIdCard(dataMap.get("G"))) {
                        throw new BusinessException("身份证号【" + dataMap.get("G") + "】格式不正确");
                    } else if (!RegexUtil.checkBankNumber(dataMap.get("J"))) {
                        throw new BusinessException("银行卡【" + dataMap.get("J") + "】格式不正确");
                    }
                    try {
                        //检查日期格式是否正确
                        DateUtil.string2Date(dataMap.get("K"), DateUtil.DATEYEARFORMATTER);
                    } catch (Exception e) {
                        throw new BusinessException("入职时间【" + dataMap.get("K") + "】格式不正确");
                    }
                    //对公司、部门、员工信息合法性进行校验
                    checkCustomerInfo(companysBean, dataMap);
                }
            } else {
                throw new BusinessException("请添加员工信息");
            }
        }
    }


    /**
     * 检查员工信息
     *
     * @param companysBean
     * @param dataMap
     */
    private void checkCustomerInfo(CompanysBean companysBean, Map<String, String> dataMap) throws BusinessException {
        //验证企业是否存在
//        CompanysBean companysBean = checkCompanyName(dataMap.get("B"));
        if (StringUtils.equals(companysBean.getCompanyName(), dataMap.get("B"))) {
            //验证部门是否存在
            checkDeptName(companysBean.getCompanyId(), dataMap.get("C"));
            //验证员工是否存在
            CustomersBean bean = new CustomersBean();
            bean.setCustomerCompanyId(companysBean.getCompanyId());
            bean.setCustomerIdcard(dataMap.get("G"));
            List<CustomersBean> list = customersService.getCustomers(bean);
            if (!list.isEmpty()) {
                throw new BusinessException("员工【" + dataMap.get("D") + "】与身份证号【" + dataMap.get("G") + "】已存在");
            }
        } else {
            throw new BusinessException("公司【" + dataMap.get("B") + "】与当前登录用户所属公司不匹配");
        }
    }

    /**
     * 验证部门是否存在
     *
     * @param companyId
     * @param deptName
     */
    private CompanyDepsBean checkDeptName(Long companyId, String deptName) throws BusinessException {
        //验证部门是否存在
        ResultResponse resultResponse = companyDepsService.checkDeptByName(companyId, deptName);
        if (resultResponse.isSuccess()) {
            CompanyDepsBean companyDepsBean = (CompanyDepsBean) resultResponse.getData();
            if (companyDepsBean == null) {
                throw new BusinessException("部门【" + deptName + "】不存在");
            }
            return companyDepsBean;
        }
        return null;
    }
}
