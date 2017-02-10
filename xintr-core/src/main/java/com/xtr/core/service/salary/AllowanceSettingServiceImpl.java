package com.xtr.core.service.salary;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.domain.customer.CustomersStationBean;
import com.xtr.api.domain.salary.AllowanceSettingBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.dto.company.CompanysDto;
import com.xtr.api.dto.salary.AllowanceSettingDto;
import com.xtr.api.service.company.CompanyDepsService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.salary.AllowanceApplyService;
import com.xtr.api.service.salary.AllowanceSettingService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.util.DateUtil;
import com.xtr.core.persistence.reader.customer.CustomersStationReaderMapper;
import com.xtr.core.persistence.reader.salary.AllowanceSettingReaderMapper;
import com.xtr.core.persistence.reader.salary.PayCycleReaderMapper;
import com.xtr.core.persistence.writer.salary.AllowanceSettingWriterMapper;
import com.xtr.core.persistence.writer.salary.CustomerPayrollWriterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>津贴设置Service实现</p>
 *
 * @author 任齐
 * @createTime: 2016/8/15 13:56.
 */
@Service("allowanceSettingService")
public class AllowanceSettingServiceImpl implements AllowanceSettingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllowanceSettingService.class);

    @Resource
    private AllowanceSettingWriterMapper allowanceSettingWriterMapper;

    @Resource
    private AllowanceSettingReaderMapper allowanceSettingReaderMapper;

    @Resource
    private AllowanceApplyService allowanceApplyService;

    @Resource
    private CompanysService companysService;

    @Resource
    private CompanyDepsService companyDepsService;

    @Resource
    private CustomersService customersService;

    @Resource
    private CustomerPayrollWriterMapper customerPayrollWriterMapper;

    @Resource
    private PayCycleReaderMapper payCycleReaderMapper;

    @Resource
    private CustomersStationReaderMapper customersStationReaderMapper;

    @Resource
    private PayrollAccountServiceImpl payrollAccountService;

    /**
     * 保存津贴设置
     *
     * @param comapnyId
     * @param memberId
     * @param deptId
     * @throws BusinessException
     */
    @Transactional
    @Override
    public Long saveAllowanceSetting(Long comapnyId, Long memberId, Long deptId, AllowanceSettingDto allowanceSettingBean) throws BusinessException {
        try {

            if (null == comapnyId) {
                throw new BusinessException("企业id不能为空");
            }

            if (null == memberId) {
                throw new BusinessException("员工id不能为空");
            }

            if (null == allowanceSettingBean) {
                throw new BusinessException("津贴参数为空");
            }

            LOGGER.info("saveAllowanceSetting 接受参数：" + JSON.toJSONString(allowanceSettingBean));

            Date cur = new Date();
            allowanceSettingBean.setCompanyId(comapnyId);
            allowanceSettingBean.setDeptId(deptId);
            allowanceSettingBean.setCompanyMenberId(memberId);
            allowanceSettingBean.setCreateTime(cur);

            allowanceSettingWriterMapper.insert(allowanceSettingBean);

            Long allowanceId = allowanceSettingBean.getId();

            JSONObject members = allowanceSettingBean.getMembers();
            if (null != members) {

                allowanceApplyService.deleteByAllowanceId(allowanceId);

                JSONArray members_ = members.getJSONArray("members");
                JSONArray depts_ = members.getJSONArray("depts");
                Boolean all = members.getBoolean("all");
                if (null != all && all) {
                    allowanceApplyService.saveAllowanceApply(allowanceId, (byte) 1, comapnyId + "");
                }
                if (depts_.size() > 0) {
                    allowanceApplyService.saveAllowanceApply(allowanceId, (byte) 2, com.xtr.comm.util.StringUtils.toString(depts_));
                }
                if (members_.size() > 0) {
                    allowanceApplyService.saveAllowanceApply(allowanceId, (byte) 3, com.xtr.comm.util.StringUtils.toString(members_));
                }
            }

            // 更新工资单
            updateCustomerUpdate(comapnyId);

            return allowanceSettingBean.getId();
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 修改津贴设置
     *
     * @param comapnyId
     * @param allowanceSettingBeans
     * @throws BusinessException
     */
    @Transactional
    @Override
    public void updateAllowanceSettings(Long comapnyId, List<AllowanceSettingDto> allowanceSettingBeans) throws BusinessException {

        if (null == comapnyId) {
            throw new BusinessException("企业id不能为空");
        }

        LOGGER.info("updateAllowanceSettings 接受参数：" + JSON.toJSONString(allowanceSettingBeans));
        try {
            if (null != allowanceSettingBeans && allowanceSettingBeans.size() > 0) {
                for (AllowanceSettingDto allowanceSettingBean : allowanceSettingBeans) {

                    Long allowanceId = allowanceSettingBean.getId();

                    allowanceSettingWriterMapper.updateByPrimaryKeySelective(allowanceSettingBean);

                    JSONObject members = allowanceSettingBean.getMembers();
                    if (null != members) {
                        JSONArray members_ = members.getJSONArray("members");
                        JSONArray depts_ = members.getJSONArray("depts");
                        Boolean all = members.getBoolean("all");

                        allowanceApplyService.deleteByAllowanceId(allowanceId);

                        if (null != all && all) {
                            allowanceApplyService.saveAllowanceApply(allowanceId, (byte) 1, comapnyId + "");
                        }
                        if (depts_.size() > 0) {
                            allowanceApplyService.saveAllowanceApply(allowanceId, (byte) 2, com.xtr.comm.util.StringUtils.toString(depts_));
                        }
                        if (members_.size() > 0) {
                            allowanceApplyService.saveAllowanceApply(allowanceId, (byte) 3, com.xtr.comm.util.StringUtils.toString(members_));
                        }
                    }
                }

                // 更新工资单
                updateCustomerUpdate(comapnyId);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }

    }

    /**
     * 查询津贴列表
     *
     * @param allowanceSettingBean
     * @return
     */
    @Override
    public List<AllowanceSettingBean> getCompanyAllowanceSettingList(AllowanceSettingBean allowanceSettingBean) {
        if (null != allowanceSettingBean) {
            return allowanceSettingReaderMapper.selectList(allowanceSettingBean);
        }
        return null;
    }

    /**
     * 删除津贴
     *
     * @param ids
     * @throws BusinessException
     */
    @Transactional
    @Override
    public void deleteAllowanceSettings(Long comapnyId, String ids) throws BusinessException {
        try {
            LOGGER.info("deleteAllowanceSettings 接受参数：" + ids);
            JSONArray array = JSON.parseArray(ids);
            if (array.size() > 0) {
                for (int i = 0, len = array.size(); i < len; i++) {
                    Long id = array.getLong(i);
                    allowanceSettingWriterMapper.deleteByPrimaryKey(id);
                    allowanceApplyService.deleteByAllowanceId(id);
                }
                // 更新工资单
                updateCustomerUpdate(comapnyId);
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 获取公司的组织架构及成员
     *
     * @param comapnyId
     * @return
     */
    @Override
    public ResultResponse getOrgMembers(Long comapnyId) {
        ResultResponse resultResponse = new ResultResponse();
        if (null != comapnyId) {
            CompanysBean companysBean = companysService.selectCompanyByCompanyId(comapnyId);
            JSONObject result = new JSONObject();
            result.put("companyId", comapnyId);
            result.put("companyName", companysBean.getCompanyName());

            JSONArray depts = new JSONArray();
            JSONArray members = new JSONArray();
            // 查询部门列表以及人员
            List<CompanysDto> companysDtos = companyDepsService.getCompanyTree(comapnyId);
            if (null != companysDtos && !companysDtos.isEmpty()) {
                for (CompanysDto companysDto : companysDtos) {

                    if (null != companysDto.getDepParentId()) {
                        JSONObject dept = new JSONObject();
                        dept.put("depId", companysDto.getDepId());
                        dept.put("depName", companysDto.getDepName());
                        dept.put("members", getDepMembers(companysDto.getDepId(), comapnyId));
                        depts.add(dept);
                        members.addAll(getDepMembers(companysDto.getDepId(), comapnyId));
                    }

                }
            }
            result.put("depts", depts);
            resultResponse.setSuccess(true);
            resultResponse.setData(result);
        }
        return resultResponse;
    }

    /**
     * 查询某个部门下的员工列表
     *
     * @param depId
     * @return
     */
    private JSONArray getDepMembers(Long depId, Long companyId) {
        JSONArray members = new JSONArray();
        if (null != depId) {
            List<CustomersBean> customersBeens = customersService.selectByDepId(depId, companyId);
            for (CustomersBean customersBean : customersBeens) {
                JSONObject member = new JSONObject();
                member.put("customerId", customersBean.getCustomerId());
                member.put("customerName", customersBean.getCustomerTurename());

                CustomersStationBean customersStationBean = customersStationReaderMapper.selectByCustomerId(customersBean.getCustomerId());
                if (null != customersStationBean) {
                    member.put("customerMethod", customersStationBean.getStationEmployMethod());
                    member.put("customerState", customersStationBean.getStationCustomerState());
                }
                members.add(member);
            }
        }
        return members;
    }

    private void updateCustomerUpdate(Long comapnyId) throws BusinessException {
        // 更新工资单
        try {
            final PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(comapnyId);
            if (null != payCycleBean) {
                customerPayrollWriterMapper.updateAllow(payCycleBean.getId(), 1);

                Thread t = new Thread() {
                    public void run() {
                        try {
                            //异步刷新工资单
                            Long startDate = System.currentTimeMillis();
                            payrollAccountService.generatePayroll(payCycleBean);
                            Long endDate = System.currentTimeMillis();
                            LOGGER.info("刷新工资单执行时间：" + DateUtil.dateDiff(startDate, endDate));
                        } catch (Exception e) {
                            LOGGER.error("刷新工资单失败", e);
                        }

                    }
                };
                t.start();
            }
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }
}