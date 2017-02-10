package com.xtr.core.service.salary;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyDepsBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.domain.customer.CustomersStationBean;
import com.xtr.api.domain.salary.AllowanceApplyBean;
import com.xtr.api.domain.salary.AllowanceSettingBean;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.salary.AllowanceApplyService;
import com.xtr.api.service.salary.AllowanceSettingService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.jd.util.StringUtils;
import com.xtr.core.persistence.reader.company.CompanyDepsReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomersReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomersStationReaderMapper;
import com.xtr.core.persistence.reader.salary.AllowanceApplyReaderMapper;
import com.xtr.core.persistence.writer.salary.AllowanceApplyWriterMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>津贴适用范围Service实现</p>
 *
 * @author 任齐
 * @createTime: 2016/8/18 15:14.
 */
@Service("allowanceApplyService")
public class AllowanceApplyServiceImpl implements AllowanceApplyService {

    @Resource
    private AllowanceApplyReaderMapper allowanceApplyReaderMapper;

    @Resource
    private AllowanceApplyWriterMapper allowanceApplyWriterMapper;

    @Resource
    private AllowanceSettingService allowanceSettingService;

    @Resource
    private CustomersReaderMapper customersReaderMapper;

    @Resource
    private CompanysService companysService;

    @Resource
    private CustomersStationReaderMapper customersStationReaderMapper;

    @Resource
    private CompanyDepsReaderMapper companyDepsReaderMapper;

    /**
     * 保存津贴适用范围
     *
     * @param companyId
     * @param data
     * @throws BusinessException
     */
    @Transactional
    @Override
    public void batchSave(Long companyId, String data) throws BusinessException {

    }

    /**
     * 保存一条津贴员工信息
     *
     * @param allowanceId
     * @param type
     * @param userdata
     * @throws BusinessException
     */
    @Transactional
    @Override
    public void saveAllowanceApply(Long allowanceId, Byte type, String userdata) throws BusinessException {
        if(null == allowanceId){
            throw new BusinessException("津贴id参数不能为空");
        }

        if(null == type){
            throw new BusinessException("适用人员类型参数不能为空");
        }

        try {
            AllowanceApplyBean allowanceApplyBean = new AllowanceApplyBean();
            allowanceApplyBean.setCreateTime(new Date());
            allowanceApplyBean.setAllowanceId(allowanceId);
            allowanceApplyBean.setType(type);
            allowanceApplyBean.setUserData(userdata);

            allowanceApplyWriterMapper.insert(allowanceApplyBean);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 根据津贴删除适用人员
     *
     * @param allowanceId
     * @throws BusinessException
     */
    @Transactional
    @Override
    public void deleteByAllowanceId(Long allowanceId) throws BusinessException {
        if(null == allowanceId){
            throw new BusinessException("津贴id参数为空");
        }

        try {
            allowanceApplyWriterMapper.deleteByAllowanceId(allowanceId);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

    }

    /**
     * 查询某个津贴的员工列表
     *
     * @param allowanceId
     * @return
     */
    @Override
    public List<AllowanceApplyBean> getApplyByAllowanId(Long allowanceId) {
        if(null != allowanceId){
            return allowanceApplyReaderMapper.selectList(allowanceId);
        }
        return null;
    }

    /**
     * 查询公司津贴以及员工列表
     * <p>
     * <Long, Map<Long, Long>>
     * <津贴id, 员工数据>
     *
     * @param companyId
     * @return
     */
    @Override
    public Map<Long, Map<Long, Long>> getCompanyAllowanceApplyMembers(Long companyId) {
        Map<Long, Map<Long, Long>> map = new HashMap<>();
        if (null != companyId) {
            AllowanceSettingBean allowanceSettingBean = new AllowanceSettingBean();
            allowanceSettingBean.setCompanyId(companyId);
            List<AllowanceSettingBean> allowanceSettingBeanList = allowanceSettingService.getCompanyAllowanceSettingList(allowanceSettingBean);
            if (null != allowanceSettingBeanList && allowanceSettingBeanList.size() > 0) {
                for (AllowanceSettingBean item : allowanceSettingBeanList) {

                    // 津贴id
                    Long allowanceId = item.getId();
                    // 根据津贴id查询人员列表
                    Map<Long, Long> memebrs = this.getMembers(allowanceId);
                    if (null != memebrs && memebrs.size() > 0) {
                        map.put(allowanceId, memebrs);
                    }
                }
            }
        }
        return map;
    }

    /**
     * 获取某个津贴的适用员工
     *
     * @param allowanceId
     * @return
     */
    @Override
    public ResultResponse getAllowanceMembers(Long allowanceId) {
        ResultResponse resultResponse = new ResultResponse();
        if(null != allowanceId){
            List<AllowanceApplyBean> allowanceApplyBeanList = allowanceApplyReaderMapper.selectList(allowanceId);
            if(null != allowanceApplyBeanList){
                JSONObject jsonObject = new JSONObject();
                for(AllowanceApplyBean allowanceApplyBean : allowanceApplyBeanList){
                    byte type = allowanceApplyBean.getType();
                    String data = allowanceApplyBean.getUserData().trim();
                    // 全体员工
                    if(type == 1 && StringUtils.isNotBlank(data) && !data.equals("null")){
                        CompanysBean companysBean = companysService.selectCompanyByCompanyId(Long.valueOf(data));
                        if(null != companysBean){
                            JSONObject all = new JSONObject();
                            all.put("companyId", companysBean.getCompanyId());
                            all.put("companyName", companysBean.getCompanyName());
                            jsonObject.put("all", all);
                        }
                    }
                    // 部门
                    if(type == 2 && StringUtils.isNotBlank(data) && !data.equals("null")){
                        String[] ids = StringUtils.split(data, ",");
                        JSONArray depts = new JSONArray();
                        List<CompanyDepsBean> companyDepsBeanList = companyDepsReaderMapper.selectByIds(ids);
                        for(CompanyDepsBean companyDepsBean : companyDepsBeanList){
                            JSONObject dept = new JSONObject();
                            dept.put("deptId", companyDepsBean.getDepId());
                            dept.put("deptName", companyDepsBean.getDepName());
                            depts.add(dept);
                        }
                        jsonObject.put("depts", depts);
                    }
                    // 员工
                    if(type == 3 && StringUtils.isNotBlank(data) && !data.equals("null")){
                        String[] ids = StringUtils.split(data, ",");
                        JSONArray members = new JSONArray();
                        List<CustomersBean> customersBeanList = customersReaderMapper.selectByIds(ids);
                        for(CustomersBean customersBean : customersBeanList){
                            JSONObject member = new JSONObject();
                            member.put("customerId", customersBean.getCustomerId());
                            member.put("customerName", customersBean.getCustomerTurename());

                            CustomersStationBean customersStationBean = customersStationReaderMapper.selectByCustomerId(customersBean.getCustomerId());
                            if(null != customersStationBean){
                                member.put("customerMethod", customersStationBean.getStationEmployMethod());
                                member.put("customerState", customersStationBean.getStationCustomerState());
                            }

                            members.add(member);
                        }
                        jsonObject.put("members", members);
                    }
                }
                resultResponse.setData(jsonObject);
                resultResponse.setSuccess(true);
            }
        }
        return resultResponse;
    }

    private Map<Long, Long> getMembers(Long allowanceId) {
        if (null != allowanceId) {
            Map<Long, Long> map = new HashMap<>();
            List<AllowanceApplyBean> allowanceApplyBeens = allowanceApplyReaderMapper.selectList(allowanceId);
            if (null != allowanceApplyBeens && allowanceApplyBeens.size() > 0) {
                Set<Long> memberIds = new HashSet<>();

                for (AllowanceApplyBean allowanceApplyBean : allowanceApplyBeens) {
                    Byte type = allowanceApplyBean.getType();

                    String data = allowanceApplyBean.getUserData().trim();

                    List<Long> membersList = null;

                    switch (type) {
                        case 1:
                            membersList = customersReaderMapper.selectPrimaryKey(Long.valueOf(data), null, null);
                            break;
                        case 2:
                            String[] ids = StringUtils.split(allowanceApplyBean.getUserData(), ",");
                            membersList = customersReaderMapper.selectPrimaryKey(null, ids, null);
                            break;
                        case 3:
                            String[] members = StringUtils.split(allowanceApplyBean.getUserData(), ",");
                            membersList = customersReaderMapper.selectPrimaryKey(null, null, members);
                            break;
                    }

                    if (null != membersList && membersList.size() > 0) {
                        memberIds.addAll(membersList);
                    }
                }

                for(Long mId : memberIds){
                    map.put(mId, mId);
                }
            }
            return map;
        }
        return null;
    }
}
