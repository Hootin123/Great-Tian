package com.xtr.core.service.customer;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.basic.SendMsgResponse;
import com.xtr.api.domain.account.BankCodeBean;
import com.xtr.api.domain.account.SubAccountBean;
import com.xtr.api.domain.company.CompanyDepsBean;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanyProtocolsBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.customer.*;
import com.xtr.api.domain.salary.AllowanceSettingBean;
import com.xtr.api.domain.salary.CustomerSalaryRecordBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.dto.customer.CustomerResponse;
import com.xtr.api.dto.customer.CustomersDto;
import com.xtr.api.dto.salary.AllowanceSettingDto;
import com.xtr.api.service.account.BankCodeService;
import com.xtr.api.service.account.SubAccountService;
import com.xtr.api.service.company.CompanyProtocolsService;
import com.xtr.api.service.customer.*;
import com.xtr.api.service.salary.AllowanceApplyService;
import com.xtr.api.service.salary.PayCycleService;
import com.xtr.api.service.salary.PayrollAccountService;
import com.xtr.api.service.send.SendMsgService;
import com.xtr.api.util.ExcelUtil;
import com.xtr.comm.annotation.SystemServiceLog;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CompanyProtocolConstants;
import com.xtr.comm.constant.CustomerConstants;
import com.xtr.comm.excel.ExcelExportException;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.util.*;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.StringUtils;
import com.xtr.core.persistence.reader.account.BankCodeReaderMapper;
import com.xtr.core.persistence.reader.account.SubAccountReaderMapper;
import com.xtr.core.persistence.reader.company.CompanyDepsReaderMapper;
import com.xtr.core.persistence.reader.customer.*;
import com.xtr.core.persistence.reader.salary.AllowanceSettingReaderMapper;
import com.xtr.core.persistence.reader.salary.CustomerSalaryRecordReaderMapper;
import com.xtr.core.persistence.reader.salary.PayCycleReaderMapper;
import com.xtr.core.persistence.writer.company.CompanyDepsWriterMapper;
import com.xtr.core.persistence.writer.customer.*;
import com.xtr.core.persistence.writer.salary.CustomerPayrollWriterMapper;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.*;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/21 10:21
 */
@Service("customersService")
public class CustomersServiceImpl implements CustomersService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomersServiceImpl.class);

    @Resource
    private CustomersWriterMapper customersWriterMapper;

    @Resource
    private CustomersReaderMapper customersReaderMapper;

    @Resource
    private CompanyDepsReaderMapper companyDepsReaderMapper;

    @Resource
    private SubAccountService subAccountService;

    @Resource
    private CustomersPersonalReaderMapper customersPersonalReaderMapper;

    @Resource
    private CustomersRecordReaderMapper customersRecordReaderMapper;

    @Resource
    private CustomersStationReaderMapper customersStationReaderMapper;
    @Resource
    private CustomersPersonalWriterMapper customersPersonalWriterMapper;

    @Resource
    private CustomersRecordWriterMapper customersRecordWriterMapper;

    @Resource
    private CustomersStationWriterMapper customersStationWriterMapper;

    @Resource
    private CustomersStationService customersStationService;

    @Resource
    private CustomersRecordService customersRecordService;

    @Resource
    private CompanyDepsWriterMapper companyDepsWriterMapper;

    @Resource
    private CustomersPersonalService customersPersonalService;

    @Resource
    private CustomerUpdateSalaryService customerUpdateSalaryService;

    @Resource
    private CustomerWechatReaderMapper customerWechatReaderMapper;

    @Resource
    private CustomerPayrollWriterMapper customerPayrollWriterMapper;

    @Resource
    private PayCycleService payCycleService;

    @Resource
    private AllowanceApplyService allowanceApplyService;

    @Resource
    private AllowanceSettingReaderMapper allowanceSettingReaderMapper;

    @Resource
    private PayrollAccountService payrollAccountService;

    @Resource
    private PayCycleReaderMapper payCycleReaderMapper;

    @Resource
    private CustomerSalaryRecordReaderMapper customerSalaryRecordReaderMapper;

    @Resource
    private BankCodeService bankCodeService;

    @Resource
    private SendMsgService sendMsgService;

    @Resource
    private CompanyProtocolsService companyProtocolsService;

    @Resource
    private BankCodeReaderMapper bankCodeReaderMapper;

    @Resource
    private SubAccountReaderMapper subAccountReaderMapper;

    /**
     * 新写入数据库记录,customers
     *
     * @param record
     */
    public ResultResponse insert(CustomersBean record) throws Exception {
        ResultResponse resultResponse = new ResultResponse();
        //0不可以 1可用
        record.setCustomerSign(1);

        //报销
        record.setCustomerIsExpense(0);

        //社保通状态
        record.setCustomerSocialApproveState(CustomerConstants.CUSTOMER_SOCIAL_APPROVESTATE_NO);

        LOGGER.info("新增员工参数：" + JSON.toJSONString(record));
        int result = customersWriterMapper.insert(record);
        //新增客户子账户
        addSubAccount(record);
        if (result > 0) {
            resultResponse.setSuccess(true);
        }
        return resultResponse;
    }

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,customers
     *
     * @param record
     */
    public int updateByPrimaryKeySelective(CustomersBean record) {
        return customersWriterMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 新增客户子账户
     */
    public void addSubAccount(CustomersBean record) throws Exception {
        SubAccountBean subAccountBean = new SubAccountBean();
        //客户编号
        subAccountBean.setCustId(record.getCustomerId());
        //账户性质 1-个人 2-企业
        subAccountBean.setProperty(1);
        subAccountService.insert(subAccountBean);
    }

    /**
     * 新增企业员工账号
     *
     * @param list
     * @return
     */
    public ResultResponse saveCustomers(List<CustomersBean> list) throws Exception {
        ResultResponse resultResponse = new ResultResponse();
        if (!list.isEmpty()) {
            for (CustomersBean customersBean : list) {
                resultResponse = insert(customersBean);
            }
        }
        return resultResponse;
    }

    @Override
    public ResultResponse editCustomers(CustomersBean customersBean) throws Exception {
        ResultResponse resultResponse = new ResultResponse();
        CustomersBean customersBeanOld = customersReaderMapper.selectByPrimaryKey(customersBean.getCustomerId());
        customersBeanOld.setCustomerTurename(customersBean.getCustomerTurename());
        customersBeanOld.setCustomerSex(customersBean.getCustomerSex());
        customersBeanOld.setCustomerPhone(customersBean.getCustomerPhone());
        customersBeanOld.setCustomerPlace(customersBean.getCustomerPlace());
        customersBeanOld.setCustomerBanknumber(customersBean.getCustomerBanknumber());
        customersBeanOld.setCustomerBank(customersBean.getCustomerBank());
        customersBeanOld.setCustomerIdcard(customersBean.getCustomerIdcard());
        customersBeanOld.setCustomerJoinTime(customersBean.getCustomerJoinTime());
        int count = customersWriterMapper.updateByPrimaryKey(customersBeanOld);
        if (count == 1) {
            resultResponse.setSuccess(true);
        }
        return resultResponse;
    }


    /**
     * 冻结企业员工账号
     *
     * @param customerIds
     * @return
     */
    public ResultResponse frozenCustomers(Long[] customerIds) throws BusinessException {
        ResultResponse resultResponse = new ResultResponse();
        if (customerIds != null) {
            for (Long customerId : customerIds) {
                CustomersBean customersBean = customersReaderMapper.selectByPrimaryKey(customerId);
                if (customersBean != null) {
                    int result = customersWriterMapper.frozenCustomers(customerId);
                    //冻结子账户
                    SubAccountBean subAccountBean = subAccountService.selectByCustId(customerId, 1);
                    //状态 00-生效,01-冻结,02-注销
                    subAccountBean.setState("01");
                    subAccountService.updateByPrimaryKeySelective(subAccountBean);
                } else {
                    throw new BusinessException("该账户不存在【" + customerId + "】，请确认");
                }
            }
            resultResponse.setSuccess(true);
        }
        return resultResponse;
    }

    /**
     * 启用企业员工账号
     *
     * @param customerIds
     * @return
     */
    public ResultResponse enableCustomers(Long[] customerIds) throws BusinessException {
        ResultResponse resultResponse = new ResultResponse();
        if (customerIds != null) {
            for (Long customerId : customerIds) {
                CustomersBean customersBean = customersReaderMapper.selectByPrimaryKey(customerId);
                if (customersBean != null) {
                    int result = customersWriterMapper.enableCustomers(customerId);
                    if (result > 0) {
                        //解冻子账户
                        SubAccountBean subAccountBean = subAccountService.selectByCustId(customerId, 1);
                        //状态 00-生效,01-冻结,02-注销
                        subAccountBean.setState("00");
                        subAccountService.updateByPrimaryKeySelective(subAccountBean);
                    }
                } else {
                    throw new BusinessException("该账户不存在【" + customerId + "】，请确认");
                }
            }
            resultResponse.setSuccess(true);
        }
        return resultResponse;
    }


    /**
     * 获取员工账号信息
     *
     * @param customersBean
     * @return
     */
    public List<CustomersBean> getCustomers(CustomersBean customersBean) {
        return customersReaderMapper.selectCustomers(customersBean);
    }

    /**
     * 根据企业Id获取企业员工信息
     *
     * @param customersBean
     * @return
     */
    public ResultResponse selectPageList(CustomersBean customersBean) {
        ResultResponse resultResponse = new ResultResponse();
        if (customersBean != null) {
            PageBounds pageBounds = new PageBounds(customersBean.getPageIndex(), customersBean.getPageSize());
            List list = customersReaderMapper.selectPageList(customersBean, pageBounds);
            resultResponse.setData(list);
            Paginator paginator = ((PageList) list).getPaginator();
            resultResponse.setPaginator(paginator);
            resultResponse.setSuccess(true);
        } else {
            resultResponse.setMessage("参数不能为空");
        }
        return resultResponse;
    }


    /**
     * 修改员工的所属部门
     *
     * @param customerIds
     * @param deptId
     * @throws BusinessException
     */
    public void updateDeptId(Long[] customerIds, Long deptId) throws BusinessException {
        if (deptId == null) {
            throw new BusinessException("部门Id不能为空");
        } else if (customerIds == null || customerIds.length <= 0) {
            throw new BusinessException("员工Id不能为空");
        }
        CompanyDepsBean companyDepsBean = companyDepsReaderMapper.selectByPrimaryKey(deptId);
        if (companyDepsBean == null) {
            throw new BusinessException("部门不存在");
        }
        for (Long customerId : customerIds) {
            customersWriterMapper.updateDeptId(customerId, deptId, companyDepsBean.getDepName());
        }
    }

    /**
     * 根据过滤条件查询员工信息
     *
     * @param customersBean
     * @return
     */
    public ResultResponse selectCustomerInfoPageList(CustomersBean customersBean) {
        LOGGER.info("获取员工信息列表,传递参数:" + JSON.toJSONString(customersBean));
        ResultResponse resultResponse = new ResultResponse();
        if (customersBean != null) {
            PageBounds pageBounds = new PageBounds(customersBean.getPageIndex(), customersBean.getPageSize());
            List list = customersReaderMapper.selectCustomerInfoPageList(customersBean, pageBounds);
            resultResponse.setData(list);
            Paginator paginator = ((PageList) list).getPaginator();
            resultResponse.setPaginator(paginator);
            resultResponse.setSuccess(true);
        } else {
            resultResponse.setMessage("参数不能为空");
        }
        return resultResponse;
    }

//    public ResultResponse selectCustomerInfoList(CustomersBean customersBean) {
//        LOGGER.info("获取员工详细信息,传递参数:"+JSON.toJSONString(customersBean));
//        ResultResponse resultResponse = new ResultResponse();
//        if (customersBean != null) {
//            PageBounds pageBounds = new PageBounds(customersBean.getPageIndex(), customersBean.getPageSize());
//            List list = customersReaderMapper.selectCustomerInfoPageList(customersBean, pageBounds);
//            resultResponse.setData(list);
//            Paginator paginator = ((PageList) list).getPaginator();
//            resultResponse.setPaginator(paginator);
//            resultResponse.setSuccess(true);
//        } else {
//            resultResponse.setMessage("参数不能为空");
//        }
//        LOGGER.info("获取查询数据结果："+JSON.toJSONString(resultResponse));
//        return resultResponse;
//    }

    /**
     * 根据员工编码获取员工详细信息
     *
     * @param customerId
     * @return
     */
    public ResultResponse selectCustomerInfoDetail(long customerId) {
        LOGGER.info("获取员工详细信息,传递员工编号参数:" + customerId);
        ResultResponse resultResponse = new ResultResponse();
        List list = customersReaderMapper.selectCustomerInfoDetail(customerId);
        if (list != null && list.size() > 0) {
            resultResponse.setData(list.get(0));
            resultResponse.setSuccess(true);
        } else {
            resultResponse.setMessage("获取不到信息");
        }
        return resultResponse;
    }

    /**
     * 查询企业的员工数量
     *
     * @param companyId
     * @return
     */
    @Override
    public int getCompanyCustomerCount(Long companyId) {
        if (null == companyId) {
            return 0;
        }
        return customersReaderMapper.selectCompanyCustomerCount(companyId);
    }

    /**
     * 根据Id获取员工姓名
     *
     * @param customerId
     * @return
     */
    public CustomersBean selectNameById(Long customerId) {
        return customersReaderMapper.selectNameById(customerId);
    }


    /**
     * 员工管理 新增员工
     *
     * @param customersDto
     * @return
     * @throws BusinessException
     */
    @SystemServiceLog(operation = "新增员工", modelName = "员工管理")
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultResponse insertCustomer(CustomersDto customersDto) throws BusinessException {

        ResultResponse resultResponse = new ResultResponse();
        if (StringUtils.isStrNull(customersDto.getCustomerTurename())) {
            throw new BusinessException("姓名不能为空");
        }
        if (StringUtils.isStrNull(customersDto.getCustomerPhone())) {
            throw new BusinessException("手机号不能为空");
        }
        if (StringUtils.isEmpty(customersDto.getCustomerJoinTime())) {
            throw new BusinessException("入职日期不能为空");
        }
        if (StringUtils.isEmpty(customersDto.getStationEmployMethod())) {
            throw new BusinessException("聘用形式不能为空");
        }
        if (StringUtils.isStrNull(customersDto.getCustomerDepName())) {
            throw new BusinessException("部门名称不能为空");
        }
        if (StringUtils.isStrNull(customersDto.getStationStationName())) {
            throw new BusinessException("岗位名称不能为空");
        }
        if (StringUtils.isEmpty(customersDto.getIsRegular()) && StringUtils.isEmpty(customersDto.getStationRegularTime())) {
            throw new BusinessException("转正时间不能为空");
        }

        if (null == customersDto.getCustomerCompanyId() || "".equals(customersDto.getCustomerCompanyId())) {
            LOGGER.info("页面companyId没有获取到!");
            throw new BusinessException("未知错误");
        }
//        if (null == customersDto.getCustomerDepId() || "".equals(customersDto.getCustomerDepId())) {
//            LOGGER.info("页面departmentId没有获取到!");
//            throw new BusinessException("未知错误");
//        }


        //判断当前手机号是否存在
        CustomersBean customer = new CustomersBean();
        customer.setCustomerPhone(customersDto.getCustomerPhone());//手机号
        customer.setCustomerCompanyId(customersDto.getCustomerCompanyId());//企业id
        List<CustomersBean> customersBeanList = customersReaderMapper.selectComstomerByPhoneAndComId(customer);

        if (null == customersBeanList || customersBeanList.size() <= 0) {
            //员工customers表插入信息
            CustomersBean customersBean = new CustomersBean();
            customersBean.setCustomerCompanyId(customersDto.getCustomerCompanyId());//企业id
            customersBean.setCustomerDepId(customersDto.getCustomerDepId());//员工部门id
            customersBean.setCustomerTurename(customersDto.getCustomerTurename());//员工姓名
            customersBean.setCustomerJoinTime(customersDto.getCustomerJoinTime());//员工入职时间
            customersBean.setCustomerDepName(customersDto.getCustomerDepName());//员工部门名称
            customersBean.setCustomerPlace(customersDto.getStationStationName());//职位
            customersBean.setCustomerSign(1);//可用
            customersBean.setCustomerPhone(customersDto.getCustomerPhone());
            //报销
            customersBean.setCustomerIsExpense(0);
            //社保通状态
            customersBean.setCustomerSocialApproveState(CustomerConstants.CUSTOMER_SOCIAL_APPROVESTATE_NO);
            customersBean.setCustomerCurrentExpense(BigDecimal.ZERO);
//            customersBean.setCustomerProbationSalary(customersDto.getCustomerCurrentSalary());//试用期工资
//            customersBean.setCustomerRegularSalary(customersDto.getCustomerRegularSalary());//入职工资基数
            long resultCustomerId = customersWriterMapper.insert(customersBean);//返回插入数据的主键
            resultResponse.setData(customersBean.getCustomerId());
            if (resultCustomerId == 0 || resultCustomerId < 1) {
                LOGGER.info("插入员工表出现错误！");
                throw new BusinessException("未知错误");
            }

            //新增一条岗位信息
            addCustomersStation(customersBean, customersDto);
            //新增个人信息
            addCustomersPersonal(customersBean, customersDto);
            addCustomersRecord(customersBean, customersDto);

            //创建账号
            try {
                this.addSubAccount(customersBean);
            } catch (Exception e) {
                LOGGER.info("新增员工创建账户出现错误！");
            }


        } else {
            CustomersBean cu = customersBeanList.get(0);
            //判断员工岗位表是否存在
            CustomersStationBean hasCustomersStationBean = customersStationReaderMapper.selectByCustomerId(cu.getCustomerId());
            if (null != hasCustomersStationBean) {
                if (hasCustomersStationBean.getStationCustomerState() == 2 || hasCustomersStationBean.getStationCustomerState() == 1) {
                    throw new BusinessException("该员工已存在");
                }
            }
            //可用 直接更新customers表
            CustomersBean customersBean = new CustomersBean();
            customersBean.setCustomerId(cu.getCustomerId());//id
            customersBean.setCustomerCompanyId(customersDto.getCustomerCompanyId());//企业id
            customersBean.setCustomerDepId(customersDto.getCustomerDepId());//员工部门id
            customersBean.setCustomerTurename(customersDto.getCustomerTurename());//员工姓名
            customersBean.setCustomerJoinTime(customersDto.getCustomerJoinTime());//员工入职时间
            customersBean.setCustomerDepName(customersDto.getCustomerDepName());//员工部门名称
            customersBean.setCustomerPhone(customersDto.getCustomerPhone());//手机号码
            customersBean.setCustomerPlace(customersDto.getStationStationName());//职务名称
            //报销
            customersBean.setCustomerIsExpense(0);
            //社保通状态
            customersBean.setCustomerSocialApproveState(CustomerConstants.CUSTOMER_SOCIAL_APPROVESTATE_NO);
            customersBean.setCustomerCurrentExpense(BigDecimal.ZERO);
//                customersBean.setCustomerProbationSalary(customersDto.getCustomerCurrentSalary());//试用工资
//                customersBean.setCustomerRegularSalary(customersDto.getCustomerRegularSalary());//转正工资
            int updateCustomerResult = customersWriterMapper.updateByPrimaryKeySelective(customersBean);
            resultResponse.setData(customersBean.getCustomerId());
            if (updateCustomerResult != 1 || updateCustomerResult < 0) {
                LOGGER.info("当前更新customers表出现未知错误！");
                throw new BusinessException("未知错误");
            }


            operateCustomersStationBean(hasCustomersStationBean, customersDto, cu);
            //判断员个人信息是否存在
            CustomersPersonalBean hasCustomersPersonalBean = customersPersonalReaderMapper.selectByCustomerId(cu.getCustomerId());
            operateCustomersPersonalBean(hasCustomersPersonalBean, customersDto, cu);
            //插入一条员工记录信息  当前为新增操作
            operateCustomersRecordBean(customersDto, cu);

            //创建账号
            try {
                this.addSubAccount(cu);
            } catch (Exception e) {
                LOGGER.info("新增员工创建账户出现错误！");
            }

            //新增工资单


        }

        resultResponse.setSuccess(true);
        resultResponse.setMessage("操作成功");

        return resultResponse;
    }

    //新增
    private void addCustomersRecord(CustomersBean customersBean, CustomersDto customersDto) {
        CustomersRecordBean customersRecordBean = null;
        int resultCustomerRecord = 0;
        if (StringUtils.isEmpty(customersDto.getIsRegular()) || customersDto.getIsRegular() != 2) {
            //说明不是直接转正
            customersRecordBean = new CustomersRecordBean();
            customersRecordBean.setRecordCustomerId(customersBean.getCustomerId());//员工id
            customersRecordBean.setRecordOperationType(1);//入职
            customersRecordBean.setRecordOperationTime(new Date());//操作时间
            customersRecordBean.setRecordCreateTime(new Date());//创建时间
            resultCustomerRecord = customersRecordWriterMapper.insert(customersRecordBean);
            if (resultCustomerRecord == 0 || resultCustomerRecord < 1) {
                LOGGER.info("customers表中有数据，插入员工记录表数据");
                throw new BusinessException("未知错误");
            }
        } else {
            //插入2条记录  一条入职  一条转正
            customersRecordBean = new CustomersRecordBean();
            customersRecordBean.setRecordCustomerId(customersBean.getCustomerId());//员工id
            customersRecordBean.setRecordOperationType(1);//转正
            customersRecordBean.setRecordOperationTime(new Date());//操作时间
            customersRecordBean.setRecordCreateTime(new Date());//创建时间
            resultCustomerRecord = customersRecordWriterMapper.insert(customersRecordBean);
            if (resultCustomerRecord == 0 || resultCustomerRecord < 1) {
                LOGGER.info("customers表中有数据，插入员工记录表数据");
                throw new BusinessException("未知错误");
            }

            //再一条转正记录
            customersRecordBean = new CustomersRecordBean();
            customersRecordBean.setRecordCustomerId(customersBean.getCustomerId());//员工id
            customersRecordBean.setRecordOperationType(2);//转正
            customersRecordBean.setRecordOperationTime(new Date());//操作时间
            customersRecordBean.setRecordCreateTime(new Date());//创建时间
            resultCustomerRecord = customersRecordWriterMapper.insert(customersRecordBean);
            if (resultCustomerRecord == 0 || resultCustomerRecord < 1) {
                LOGGER.info("customers表中有数据，插入员工记录表数据");
                throw new BusinessException("未知错误");
            }
        }
    }

    //新增
    private void addCustomersPersonal(CustomersBean customersBean, CustomersDto customersDto) {
        CustomersPersonalBean cus = new CustomersPersonalBean();
        cus.setPersonalCreateTime(new Date());
        cus.setPersonalCustomerId(customersBean.getCustomerId());
        int a = customersPersonalWriterMapper.insert(cus);
        if (a < 0 || a != 1) {
            LOGGER.info("customers表中存在用户信息，customers_personal表中不存在用户信息插入出现错误");
            throw new BusinessException("未知错误");

        }
    }

    //新增
    private void addCustomersStation(CustomersBean customersBean, CustomersDto customersDto) {
        CustomersStationBean customersStationBean = new CustomersStationBean();
        customersStationBean.setStationCustomerId(customersBean.getCustomerId());//员工id
        customersStationBean.setStationEnterTime(customersDto.getCustomerJoinTime());//入职时间
        customersStationBean.setStationEmployMethod(customersDto.getStationEmployMethod());//聘用形式：1正式 2劳务
        customersStationBean.setStationRegularTime(StringUtils.isEmpty(customersDto.getStationRegularTime()) == true ? null : customersDto.getStationRegularTime());//转正时间
        if (!StringUtils.isEmpty(customersDto.getIsRegular()) && customersDto.getIsRegular() == 2)
            customersStationBean.setStationCustomerState(2);//已转正
        else
            customersStationBean.setStationCustomerState(1);//入职

        customersStationBean.setStationDeptId(customersDto.getCustomerDepId());//部门id
        customersStationBean.setStationStationName(customersDto.getStationStationName());//岗位名称
        customersStationBean.setStationCreateTime(new Date());//创建时间
        int resultCustomerStation = customersStationWriterMapper.insert(customersStationBean);
        if (resultCustomerStation == 0 || resultCustomerStation != 1) {
            LOGGER.info("customers表中存在用户信息，customers_station表中不存在用户信息插入出现错误");
            throw new BusinessException("未知错误");
        }
    }


    //新增员工时记录表新增一条数据  如果选择直接转正则插入2条记录信息：一条入职 一条转正信息
    private void operateCustomersRecordBean(CustomersDto customersDto, CustomersBean cu) {

        CustomersRecordBean customersRecordBean = null;
        int resultCustomerRecord = 0;
        if (StringUtils.isEmpty(customersDto.getIsRegular()) || customersDto.getIsRegular() != 2) {
            //说明不是直接转正
            customersRecordBean = new CustomersRecordBean();
            customersRecordBean.setRecordCustomerId(cu.getCustomerId());//员工id
            customersRecordBean.setRecordOperationType(1);//入职
            customersRecordBean.setRecordOperationTime(new Date());//操作时间
            customersRecordBean.setRecordCreateTime(new Date());//创建时间
            resultCustomerRecord = customersRecordWriterMapper.insert(customersRecordBean);
            if (resultCustomerRecord == 0 || resultCustomerRecord < 1) {
                LOGGER.info("customers表中有数据，插入员工记录表数据");
                throw new BusinessException("未知错误");
            }
        } else {
            //插入2条记录  一条入职  一条转正
            customersRecordBean = new CustomersRecordBean();
            customersRecordBean.setRecordCustomerId(cu.getCustomerId());//员工id
            customersRecordBean.setRecordOperationType(1);//转正
            customersRecordBean.setRecordOperationTime(new Date());//操作时间
            customersRecordBean.setRecordCreateTime(new Date());//创建时间
            resultCustomerRecord = customersRecordWriterMapper.insert(customersRecordBean);
            if (resultCustomerRecord == 0 || resultCustomerRecord < 1) {
                LOGGER.info("customers表中有数据，插入员工记录表数据");
                throw new BusinessException("未知错误");
            }

            //再一条转正记录
            customersRecordBean = new CustomersRecordBean();
            customersRecordBean.setRecordCustomerId(cu.getCustomerId());//员工id
            customersRecordBean.setRecordOperationType(2);//转正
            customersRecordBean.setRecordOperationTime(new Date());//操作时间
            customersRecordBean.setRecordCreateTime(new Date());//创建时间
            resultCustomerRecord = customersRecordWriterMapper.insert(customersRecordBean);
            if (resultCustomerRecord == 0 || resultCustomerRecord < 1) {
                LOGGER.info("customers表中有数据，插入员工记录表数据");
                throw new BusinessException("未知错误");
            }
        }
    }

    //判断当前员工个人信息表中是否有记录  有即更新 没有即新增数据
    private void operateCustomersPersonalBean(CustomersPersonalBean hasCustomersPersonalBean, CustomersDto customersDto, CustomersBean cu) {
        if (null == hasCustomersPersonalBean) {
            //为空 新增
            CustomersPersonalBean cus = new CustomersPersonalBean();
            cus.setPersonalCreateTime(new Date());
            cus.setPersonalCustomerId(cu.getCustomerId());
            int a = customersPersonalWriterMapper.insert(cus);
            if (a < 0 || a != 1) {
                LOGGER.info("customers表中存在用户信息，customers_personal表中不存在用户信息插入出现错误");
                throw new BusinessException("新增员工个人信息失败");

            }
        }
        //没啥字段更新的 所以无操作

    }

    //判断当前员工岗位表中是否有记录  有即更新 没有即新增数据
    private void operateCustomersStationBean(CustomersStationBean hasCustomersStationBean, CustomersDto customersDto, CustomersBean cu) {
        if (null == hasCustomersStationBean) {
            //不存在  新增一条数据
            CustomersStationBean customersStationBean = new CustomersStationBean();
            customersStationBean.setStationCustomerId(cu.getCustomerId());//员工id
            customersStationBean.setStationEnterTime(customersDto.getCustomerJoinTime());//入职时间
            customersStationBean.setStationEmployMethod(customersDto.getStationEmployMethod());//聘用形式：1正式 2劳务
            customersStationBean.setStationRegularTime(StringUtils.isEmpty(customersDto.getStationRegularTime()) == true ? null : customersDto.getStationRegularTime());//转正时间
            if (!StringUtils.isEmpty(customersDto.getIsRegular()) && customersDto.getIsRegular() == 2)
                customersStationBean.setStationCustomerState(2);//已转正
            else
                customersStationBean.setStationCustomerState(1);//入职

            customersStationBean.setStationDeptId(customersDto.getCustomerDepId());//部门id
            customersStationBean.setStationStationName(customersDto.getStationStationName());//岗位名称
            customersStationBean.setStationCreateTime(new Date());//创建时间
            int resultCustomerStation = customersStationWriterMapper.insert(customersStationBean);
            if (resultCustomerStation == 0 || resultCustomerStation != 1) {
                LOGGER.info("customers表中存在用户信息，customers_station表中不存在用户信息插入出现错误");
                throw new BusinessException("未知错误");
            }
        } else {
            //更新
            CustomersStationBean customersStationBean = new CustomersStationBean();
            customersStationBean.setStationCustomerId(cu.getCustomerId());//员工id
            customersStationBean.setStationEnterTime(customersDto.getCustomerJoinTime());//入职时间
            customersStationBean.setStationEmployMethod(customersDto.getStationEmployMethod());//聘用形式：1正式 2劳务
            customersStationBean.setStationRegularTime(StringUtils.isEmpty(customersDto.getStationRegularTime()) == true ? null : customersDto.getStationRegularTime());//转正时间
            if (!StringUtils.isEmpty(customersDto.getIsRegular()) && customersDto.getIsRegular() == 2)
                customersStationBean.setStationCustomerState(2);//已转正
            else
                customersStationBean.setStationCustomerState(1);//入职

            customersStationBean.setStationDeptId(customersDto.getCustomerDepId());//部门id
            customersStationBean.setStationStationName(customersDto.getStationStationName());//岗位名称
            customersStationBean.setStationUpdateTime(new Date());//创建时间
            customersStationBean.setStationDimissingTime(null);//离职日期 清空
            int resultCustomerStation = customersStationWriterMapper.updateByCustomerId(customersStationBean);
            if (resultCustomerStation == 0 || resultCustomerStation != 1) {
                LOGGER.info("customers表中存在用户信息，customers_station表中不存在用户信息更新出现错误");
                throw new BusinessException("未知错误");
            }
        }

    }

    /**
     * 员工转正
     *
     * @param customersDto
     * @return
     * @throws BusinessException
     */
    @SystemServiceLog(operation = "员工转正", modelName = "员工管理")
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultResponse customerRegular(CustomersDto customersDto) throws BusinessException {
        //非空参数校验
        if (StringUtils.isEmpty(customersDto.getCustomerId())) {
            throw new BusinessException("员工id参数为空");
        }
        //查询是否有改员工
        CustomersStationBean customersStationBean = customersStationReaderMapper.selectByCustomerId(customersDto.getCustomerId());
        if (null == customersStationBean) {
            LOGGER.info("customers_station中没有该条数据");
            throw new BusinessException("未知错误");
        }
        //判断是否已转正
        if ((customersStationBean.getStationCustomerState() == 2)) {
            throw new BusinessException("员工已转正,不需要再操作！");
        }


        //更新操作
        CustomersStationBean cus = new CustomersStationBean();
        cus.setStationCustomerId(customersDto.getCustomerId());
        cus.setStationRegularTime(customersDto.getStationRegularTime());//转正时间
        cus.setStationRegularEvaluate(customersDto.getStationRegularEvaluate());//转正评价
        cus.setStationUpdateTime(new Date());//更新时间
        //判断当前时间转正时间是否大于当前时间 大于的话不更新转正标识
        if (customersDto.getStationRegularTime().getTime() - new Date().getTime() <= 0) {
            cus.setStationCustomerState(2);//转正标识
        }
        int resultCustomerStation = customersStationWriterMapper.updateByCustomerId(cus);
        if (resultCustomerStation != 1 || resultCustomerStation < 0) {
            LOGGER.info("更新customers_station错误");
        }
        //只有在当前时间大于转正时间才生效
        if (customersDto.getStationRegularTime().getTime() - new Date().getTime() <= 0) {
            //新增一条员工转正记录
            CustomersRecordBean cust = new CustomersRecordBean();
            cust.setRecordCustomerId(customersDto.getCustomerId());
            cust.setRecordOperationType(2);//'操作类型 1入职 2转正 3离职 4删除 5调岗 6调薪'
            cust.setRecordCreateTime(new Date());//创建时间
            cust.setRecordOperationTime(new Date());//操作时间
            int resultCustomerRecord = customersRecordWriterMapper.insert(cust);
            if (resultCustomerRecord != 1 || resultCustomerRecord < 0) {
                LOGGER.info("保存员工记录表出现错误");
                throw new BusinessException("未知错误");
            }
        }
        PayCycleBean payCycleBean = payCycleService.selectByCompanyId(customersDto.getCompanyId());
        if(payCycleBean != null){
            //刷新转正后的工资
            try {
                customerUpdateSalaryService.calculateSalary(customersDto.getCustomerId());
            } catch (Exception e) {
                LOGGER.info("当前转正调用定薪接口出现错误 ：" + e.getMessage());
            }
        }
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 员工调岗
     *
     * @param customersDto
     * @return
     */
    @SystemServiceLog(operation = "员工调岗", modelName = "员工管理")
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultResponse customerTransferPosition(CustomersDto customersDto) {

        ResultResponse resultResponse = new ResultResponse();

        if (null == customersDto) {
            LOGGER.info("封装类为空");
            throw new BusinessException("未知错误");
        }
        if (StringUtils.isEmpty(customersDto.getCustomerId())) {
            LOGGER.info("用户id传参错误");
            throw new BusinessException("未知错误");
        }
        //查询员工信息是否存在
        CustomersBean customersBean = customersReaderMapper.selectById(customersDto.getCustomerId());
        if (customersBean == null) {
            throw new BusinessException("当前员工信息不存在");
        }
        //查询该员工岗位信息是否存在
        CustomersStationBean customersStationBean = customersStationReaderMapper.selectByCustomerId(customersDto.getCustomerId());
        if (customersStationBean == null) {
            resultResponse.setSuccess(false);
            resultResponse.setMessage("员工信息不存在！");
        } else {
            //调岗时间不能早于入职时间
            if (customersStationBean.getStationEnterTime().getTime() - customersDto.getRecordArriveTime().getTime() > 0) {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("调岗时间不能早于入职时间");
                return resultResponse;
            }
//           if(customersStationBean.getStationEnterTime().getTime()-new Date().getTime()>0){
//               resultResponse.setSuccess(false);
//               resultResponse.setMessage("调岗时间不能晚于当前时间");
//               return resultResponse;
//           }


            //判断是否调岗
            Date date = customersDto.getRecordArriveTime();//到岗时间不能超出今天以后的一个月
            if (((date.getTime() - System.currentTimeMillis()) / (60 * 60 * 24 * 1000)) > 30) {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("只能处理一个月以内的调岗！");
                return resultResponse;
            }

            Map map1 = new HashMap();
            SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
            String currentTimeStr = sf1.format(new Date()) + " 00:00:00";//时间
            boolean ccflag = false;
            //当前时间
            map1.put("customerId", customersDto.getCustomerId());
            map1.put("currentTimeStr", currentTimeStr);
            List<CustomersRecordBean> list1 = customersRecordReaderMapper.selectFutureTranferPosition(map1);

            //与当前系统时间比较
            boolean flag = compaireTime(customersDto.getRecordArriveTime());
            //数据库时间小于当前时间
            if (!flag) {
                //时间小于当前的
                //更新操作
                CustomersBean customer = new CustomersBean();
                customer.setCustomerId(customersDto.getCustomerId());
                customer.setCustomerDepId(customersDto.getCustomerDepId());//部门id
                customer.setCustomerPlace(customersDto.getCustomerPlace());//岗位
                customer.setCustomerDepName(customersDto.getCustomerDepName());//部门名称

                int updateCustomer = customersWriterMapper.updateByCustomerId(customer);
                if (updateCustomer == 0 || updateCustomer < 1) {
                    LOGGER.info("更新customers表失败");
                    throw new BusinessException("未知错误");
                }

                //更新岗位表
                CustomersStationBean cu = new CustomersStationBean();
                cu.setStationCustomerId(customersDto.getCustomerId());
                cu.setStationDeptId(customersDto.getCustomerDepId());//部门id
                cu.setStationStationName(customersDto.getCustomerPlace());//职位
                cu.setStationUpdateTime(new Date());//更新时间
                int curesult = customersStationWriterMapper.updateByCustomerId(cu);
                if (curesult != 1 || curesult < 0) {
                    throw new BusinessException("未知错误");
                }
                //判断数据库的调岗时间
                if (null == list1 || list1.size() < 1) {
                    //记录表插入一条数据    //调岗前的部门  调岗前的岗位  调岗后的岗位   调薪前的工资  调薪前的的工资
                    CustomersRecordBean cust = new CustomersRecordBean();
                    cust.setRecordCustomerId(customersDto.getCustomerId());//id
                    cust.setRecordUpdateTime(new Date());//更新时间
                    cust.setRecordOperationType(5);//调岗
                    cust.setRecordOperationTime(new Date());//操作时间
                    cust.setRecordDeptId(customersDto.getCustomerDepId());//部门id
                    cust.setRecordDeptName(customersDto.getCustomerDepName());//部门名称
                    cust.setRecordArriveTime(customersDto.getRecordArriveTime());//到岗时间
                    cust.setRecordStationReason(customersDto.getRecordStationReason());//调岗原因
                    cust.setRecordUpdateTime(new Date());//更新时间
                    //调岗前部门id
                    cust.setRecordDeptIdBefore(customersBean.getCustomerDepId());
                    //调岗前岗位
                    cust.setRecordStationNameBefore(customersBean.getCustomerPlace());
                    //调岗后岗位
                    cust.setRecordStationNameAfter(customersDto.getStationStationName());
                    int custresult = customersRecordWriterMapper.insert(cust);
                    if (custresult == 0 || custresult < 1) {
                        LOGGER.info("保存customers_record失败");
                        throw new BusinessException("未知错误");
                    }
                    resultResponse.setSuccess(true);
                    resultResponse.setMessage("操作成功");
                } else {
                    //如果记录表中有条当前时间的记录则更新
                    //更新操作
                    CustomersRecordBean customersRecordBean = list1.get(0);
                    CustomersRecordBean cb = new CustomersRecordBean();
                    cb.setRecordId(customersRecordBean.getRecordId());
                    cb.setRecordCustomerId(customersRecordBean.getRecordCustomerId());
                    cb.setRecordUpdateTime(new Date());//更新时间
                    cb.setRecordOperationTime(new Date());//操作时间
                    cb.setRecordDeptId(customersDto.getCustomerDepId());//部门id
                    cb.setRecordDeptName(customersDto.getCustomerDepName());//部门名称
                    cb.setRecordArriveTime(customersDto.getRecordArriveTime());//到岗时间
                    cb.setRecordStationReason(customersDto.getRecordStationReason());//调岗原因
                    cb.setRecordOperationType(5);//调岗
                    cb.setRecordUpdateTime(new Date());//更新时间
                    cb.setRecordArriveTime(customersDto.getRecordArriveTime());
                    //调岗前部门id
                    cb.setRecordDeptIdBefore(customersBean.getCustomerDepId());
                    //调岗前岗位
                    cb.setRecordStationNameBefore(customersBean.getCustomerPlace());
                    //调岗后岗位
                    cb.setRecordStationNameAfter(customersDto.getCustomerPlace());
                    int updateCb = customersRecordWriterMapper.updateByPrimaryKey(cb);
                    if (updateCb == 1) {
                        resultResponse.setSuccess(true);
                        resultResponse.setMessage("操作成功");
                    } else {
                        LOGGER.info("当前更新记录表出现错误");
                        throw new BusinessException("操作失败");
                    }


                }
            } else {

                if (null != list1 && list1.size() > 0) {
                    //更新操作
                    CustomersRecordBean customersRecordBean = list1.get(0);
                    CustomersRecordBean cb = new CustomersRecordBean();
                    cb.setRecordId(customersRecordBean.getRecordId());
                    cb.setRecordCustomerId(customersRecordBean.getRecordCustomerId());
                    cb.setRecordUpdateTime(new Date());//更新时间
                    cb.setRecordOperationTime(new Date());//操作时间
                    cb.setRecordDeptId(customersDto.getCustomerDepId());//部门id
                    cb.setRecordDeptName(customersDto.getCustomerDepName());//部门名称
                    cb.setRecordArriveTime(customersDto.getRecordArriveTime());//到岗时间
                    cb.setRecordStationReason(customersDto.getRecordStationReason());//调岗原因
                    cb.setRecordOperationType(5);//调岗
                    cb.setRecordUpdateTime(new Date());//更新时间
                    cb.setRecordArriveTime(customersDto.getRecordArriveTime());
                    //调岗前部门id
                    cb.setRecordDeptIdBefore(customersBean.getCustomerDepId());
                    //调岗前岗位
                    cb.setRecordStationNameBefore(customersBean.getCustomerPlace());
                    //调岗后岗位
                    cb.setRecordStationNameAfter(customersDto.getCustomerPlace());
                    int updateCb = customersRecordWriterMapper.updateByPrimaryKey(cb);
                    if (updateCb == 1) {
                        resultResponse.setSuccess(true);
                        resultResponse.setMessage("操作成功");
                    } else {
                        LOGGER.info("当前更新记录表出现错误");
                        throw new BusinessException("操作失败");
                    }

                } else {
                    //记录表插入一条数据    //调岗前的部门  调岗前的岗位  调岗后的岗位   调薪前的工资  调薪前的的工资
                    CustomersRecordBean cust = new CustomersRecordBean();
                    cust.setRecordCustomerId(customersDto.getCustomerId());//id
                    cust.setRecordUpdateTime(new Date());//更新时间
                    cust.setRecordOperationType(5);//调岗
                    cust.setRecordOperationTime(new Date());//操作时间
                    cust.setRecordDeptId(customersDto.getCustomerDepId());//部门id
                    cust.setRecordDeptName(customersDto.getCustomerDepName());//部门名称
                    cust.setRecordArriveTime(customersDto.getRecordArriveTime());//到岗时间
                    cust.setRecordStationReason(customersDto.getRecordStationReason());//调岗原因
                    cust.setRecordUpdateTime(new Date());//更新时间
                    //调岗前部门id
                    cust.setRecordDeptIdBefore(customersBean.getCustomerDepId());
                    //调岗前岗位
                    cust.setRecordStationNameBefore(customersBean.getCustomerPlace());
                    //调岗后岗位
                    cust.setRecordStationNameAfter(customersDto.getStationStationName());
                    int custresult = customersRecordWriterMapper.insert(cust);
                    if (custresult == 0 || custresult < 1) {
                        LOGGER.info("保存customers_record失败");
                        throw new BusinessException("未知错误");
                    }
                    resultResponse.setSuccess(true);
                    resultResponse.setMessage("操作成功");

                }

            }


        }

        return resultResponse;
    }

    /**
     * 与系统时间进行比较
     *
     * @param time
     * @return true  大于当前时间    false 小于当前时间
     */
    private boolean compaireTime(Date time) {
//        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
//        String str = sim.format(new Date()) + " 00:00:00";
        boolean flag = false;
        flag = time.getTime() - new Date().getTime() > 0;
        return flag;
    }

    /**
     * 离职判断时间
     *
     * @param time
     * @return
     */
    private boolean compaireDimssTime(Date time) {
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        String str = sim.format(new Date()) + " 00:00:00";
        boolean flag = false;
        try {
            flag = time.getTime() - sim.parse(str).getTime() >= 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<CustomersBean> selectByDepId(Long depId, long companyId) {
        return customersReaderMapper.selectByDepId(depId, companyId);
    }

    @Override
    public int selectCountByDepId(Long depId, long companyId) {
        return customersReaderMapper.selectCountByDepId(depId, companyId);
    }

    /**
     * 员工离职
     *
     * @param customersDto
     * @return
     */
    @SystemServiceLog(operation = "员工离职", modelName = "员工管理")
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultResponse customerDimission(CustomersDto customersDto) {

        if (StringUtils.isEmpty(customersDto.getCustomerId())) {
            LOGGER.info("customerId参数为空");
            throw new BusinessException("未知错误");
        }
        if (StringUtils.isEmpty(customersDto.getStationDimissingTime())) {
            throw new BusinessException("离职日期不能为空");

        }

        ResultResponse resultResponse = new ResultResponse();
        CustomersStationBean customersStationBean = customersStationReaderMapper.selectByCustomerId(customersDto.getCustomerId());
        if (customersStationBean == null) {
            throw new BusinessException("未知错误");
        }

        //判断离职时间  与入职时间的比较
        if (customersDto.getStationDimissingTime().getTime() - customersStationBean.getStationEnterTime().getTime() < 0) {
            throw new BusinessException("离职时间不能早于入职时间");
        }



        //离职可以选择大于当前时间    定时任务跑批
//        if(customersDto.getStationDimissingTime().getTime()-(new Date().getTime()) >0){
//            throw  new BusinessException("离职时间不能大于当前时间");
//        }


        boolean flag = compaireDimssTime(customersDto.getStationDimissingTime());
        if (!flag) {
            //删除部门领导信息
            companyDepsWriterMapper.clearLeader(customersDto.getCustomerId());

            //更新员工岗位表操作
            CustomersStationBean cu = new CustomersStationBean();
            cu.setStationCustomerId(customersDto.getCustomerId());
            cu.setStationCustomerState(3);//离职
            cu.setStationDimissingTime(customersDto.getStationDimissingTime());//离职日期
            cu.setStationUpdateTime(new Date());//修改日期
            int resultCu = customersStationWriterMapper.updateByCustomerId(cu);//更新
            if (resultCu < 0 || resultCu != 1) {
                LOGGER.info("更新员工岗位表出现错误");
                throw new BusinessException("未知错误");
            }
        }


        //插入员工记录表
        CustomersRecordBean cur = new CustomersRecordBean();
        cur.setRecordCustomerId(customersDto.getCustomerId());//员工id
        cur.setRecordOperationType(3);//离职
        cur.setRecordDimissingType(customersDto.getRecordDimissingType());//主动离职 还是被动离职
        cur.setRecordDimissingTime(customersDto.getStationDimissingTime());//离职日期
        cur.setRecordCompensation(customersDto.getRecordCompensation());//补偿金
        cur.setRecordDimissingReason(customersDto.getDimissingReason());//离职原因
        cur.setRecordOperationTime(new Date());//操作时间
        int resultCur = customersRecordWriterMapper.insert(cur);
        if (resultCur != 1 || resultCur < 0) {
            LOGGER.info("保存员工记录表出现错误");
            throw new BusinessException("未知错误");
        }
        //查询员工最新计薪周期开始日期
        PayCycleBean  payCycleBean = payCycleService.selectByCompanyId(customersDto.getCompanyId());
        if(payCycleBean==null){
            LOGGER.info("没有查询到计薪周期");
        }else{
            //判断离职日期
            if(customersDto.getStationDimissingTime().getTime()-payCycleBean.getStartDate().getTime()<0){
                //删除工资单
                customerPayrollWriterMapper.deleteByCustomerId(customersDto.getCompanyId(),customersDto.getCustomerId(),payCycleBean.getId());
            }
        }

        //所有的操作都正确
        resultResponse.setMessage("操作成功");
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 员工删除 （逻辑删除）
     *
     * @param customersDto
     * @return
     */
    @SystemServiceLog(operation = "员工删除", modelName = "员工管理")
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultResponse customerDelete(CustomersDto customersDto) {
        if (customersDto == null) {
            LOGGER.info("customerDto为空");
            throw new BusinessException("未知错误");
        }
        if (StringUtils.isEmpty(customersDto.getCustomerId())) {
            LOGGER.info("用户id为空");
            throw new BusinessException("未知错误");
        }
        //根据customerId查询customers表 判断是否有该员工的数据 有的话变成不可用
        CustomersBean customersBean = customersReaderMapper.selectById(customersDto.getCustomerId());
        if (customersBean == null) {
            throw new BusinessException("当前员工信息不存在");
        }
        //判断  员工岗位表
        CustomersStationBean customersStationBean = customersStationReaderMapper.selectByCustomerId(customersDto.getCustomerId());//不是离职的状态
        if (customersStationBean == null) {
            throw new BusinessException("当前员工没有岗位信息");

        }
        if (customersStationBean.getStationCustomerState() == 2 || customersStationBean.getStationCustomerState() == 1) {
            throw new BusinessException("未离职员工不能删除");
        }


        if (!(new Date().getTime() - (customersStationBean.getStationDimissingTime().getTime()) > 0)) {
            //离职日期还未过
            throw new BusinessException("当前员工离职时间还未到");
        }


        //删除部门领导信息
        companyDepsWriterMapper.clearLeader(customersDto.getCustomerId());

        //更新操作
        //更新员工信息表
        int resultCustomer = customersWriterMapper.frozenCustomers(customersDto.getCustomerId());//更新员工账号不可用
        if (resultCustomer < 0 || resultCustomer != 1) {
            LOGGER.info("更新员工信息出现错误");
            throw new BusinessException("未知错误");
        }
        //更新员工岗位表
        CustomersStationBean custo = new CustomersStationBean();
        custo.setStationCustomerId(customersDto.getCustomerId());
        custo.setStationUpdateTime(new Date());
        int resultCustomerStation = customersStationWriterMapper.updateCustomerStateByCustomerId(custo);//根据员工id更新岗位表的员工状态
        if (resultCustomerStation != 1 || resultCustomerStation < 0) {
            LOGGER.info("更新员工岗位表出现错误");
            throw new BusinessException("未知错误");
        }
        //插入员工记录表
        CustomersRecordBean cur = new CustomersRecordBean();
        cur.setRecordCustomerId(customersDto.getCustomerId());
        cur.setRecordOperationType(4);//删除
        cur.setRecordOperationTime(new Date());//操作时间
        int resultCustomerRecord = customersRecordWriterMapper.insert(cur);//根据员工id更新员工状态
        if (resultCustomerRecord != 1 || resultCustomerRecord < 0) {
            LOGGER.info("保存员工记录表出现错误");
            throw new BusinessException("未知错误");
        }
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setSuccess(true);
        return resultResponse;
    }


    /**
     * 根据过滤条件查询员工信息(企业端)
     *
     * @param customersDto
     * @return
     */
    public List<CustomersDto> selectCustomersByCondition(CustomersDto customersDto) {
        return customersReaderMapper.selectCustomersByCondition(customersDto);
    }

    /**
     * 企业端用户分页查询
     *
     * @param customersDto
     * @return
     */
    public ResultResponse selectPageListByCondition(CustomersDto customersDto) throws Exception {
        LOGGER.info("企业端用户分页查询,传递参数：" + JSON.toJSONString(customersDto));
        ResultResponse resultResponse = new ResultResponse();
//        if(customersDto!=null && customersDto.getCustomerCompanyId()!=null){
        PageBounds pageBounds = new PageBounds(customersDto.getPageIndex(), customersDto.getPageSize());
        List<CustomersDto> list = customersReaderMapper.selectCustomersByCondition(customersDto, pageBounds);
        //拼装是否待离职
        if (list != null && list.size() > 0) {
            List<Long> customerIdList = new ArrayList<>();
            for (CustomersDto dto : list) {
                customerIdList.add(dto.getCustomerId());
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            String startStr = sdf.format(calendar.getTime());
            startStr = startStr.substring(0, 10);
            startStr = startStr + " 00:00:00";
            Date startDate = sdf.parse(startStr);
            List<CustomersRecordBean> recordBeanList = customersRecordService.selectWillLeaveBatch(customerIdList, startDate);
            if (recordBeanList != null && recordBeanList.size() > 0) {
                for (CustomersDto dto : list) {
                    for (CustomersRecordBean record : recordBeanList) {
                        if (dto.getCustomerId().longValue() == record.getRecordCustomerId().longValue()) {
                            dto.setWillLeaveState(1);//待离职
                        }
                    }
                }

            }
        }
        resultResponse.setData(list);
        Paginator paginator = ((PageList) list).getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setSuccess(true);
//        }else{
//            resultResponse.setMessage("传递参数不能为空");
//            LOGGER.info("企业端用户分页查询,传递参数不能为空");
//        }
        return resultResponse;
    }


    @Override
    public CustomersBean selectByPrimaryKey(Long customerId) {
        return customersReaderMapper.selectByPrimaryKey(customerId);
    }

    /**
     * 插入用户,返回ID
     *
     * @param record
     * @return
     * @throws Exception
     */
    public Long insertCustomerForId(CustomersBean record) throws BusinessException {
        LOGGER.info("新增员工参数：" + JSON.toJSONString(record));
        int result = customersWriterMapper.insert(record);
        if (result <= 0) {
            LOGGER.info("新增员工失败,参数:" + JSON.toJSONString(record));
            throw new BusinessException("新增员工失败");
        }
        return record.getCustomerId();
    }

    /**
     * 根据ID查询所有关联信息
     *
     * @param customerId
     * @return
     */
    public CustomersDto selectCustomersByCustomerId(Long customerId) throws Exception {
        CustomersDto customerDto = customersReaderMapper.selectCustomersByCustomerId(customerId);
        //添加是否待离职状态
        List<Long> customerIdList = new ArrayList<>();
        customerIdList.add(customerId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String startStr = sdf.format(calendar.getTime());
        startStr = startStr.substring(0, 10);
        startStr = startStr + " 00:00:00";
        Date startDate = sdf.parse(startStr);
        List<CustomersRecordBean> recordBeanList = customersRecordService.selectWillLeaveBatch(customerIdList, startDate);
        if (recordBeanList != null && recordBeanList.size() > 0) {
            for (CustomersRecordBean record : recordBeanList) {
                if (customerId.longValue() == record.getRecordCustomerId().longValue()) {
                    customerDto.setWillLeaveState(1);//待离职
                }
            }
        }
        //如果不是入职,获取调薪后的工资基数
        if(customerDto!=null && customerDto.getStationCustomerState()!=null && customerDto.getStationCustomerState().intValue()!=CustomerConstants.CUSTOMER_PERSONSTATE_ENTER){
            Date lastDate=  new Date();
            BigDecimal currentSalary=customerUpdateSalaryService.getLastSalary(customerId, lastDate);
            customerDto.setCurrentSalary(currentSalary);
        }
        return customerDto;
    }

    /**
     * 插入上传的员工信息
     *
     * @param companysBean
     * @param fileName
     * @return
     * @throws Exception
     */
    public ResultResponse insertCustomersBatch(CompanysBean companysBean, String fileName) throws Exception {
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setSuccess(false);
        return resultResponse;
//        byte[] browserStreamArray = AliOss.downloadFileByte(PropertyUtils.getString("oss.bucketName.img"), fileName);
//        if (browserStreamArray != null && browserStreamArray.length > 0) {
//            //文件读取开始行
//            int startLine = CustomerConstants.CUSTOMER_UPLOADLINE_START;
//            //文件读取页签
//            int sheetNum = CustomerConstants.CUSTOMER_UPLOADSHEET_START;
//            //每行最大列数
//            int sheetLastNum = CustomerConstants.CUSTOMER_UPLOADLINDE_LASTNUM;
//            //解析Excel
//            List<Object[]> list = getDataFromExcel(browserStreamArray, startLine, sheetNum, sheetLastNum);
//            LOGGER.info("提交上传的新增用户EXCEL文件:解析EXCEL,返回列表:" + JSON.toJSONString(list));
//            //验证EXCEL
//            List<CustomersBean> customerBeanList = new ArrayList<>();
//            List<CustomersStationBean> customersStationBeanList = new ArrayList<>();
//            Date compareDate = CustomerConstants.SDF_YEAR_MONTH_DAY_LINE.parse(CustomerConstants.SDF_YEAR_MONTH_DAY_LINE.format(new Date()));
//            if (list != null && list.size() > 0) {
//                for (int i = 0; i < list.size(); i++) {
//                    Object[] objArray = list.get(i);
//                    if (objArray[0] != null && !StringUtils.isStrNull(String.valueOf(objArray[0]))
//                            && objArray[1] != null && !StringUtils.isStrNull(String.valueOf(objArray[1]))
//                            && objArray[2] != null && !StringUtils.isStrNull(String.valueOf(objArray[2]))
//                            && objArray[3] != null && !StringUtils.isStrNull(String.valueOf(objArray[3]))
//                            && objArray[4] != null && !StringUtils.isStrNull(String.valueOf(objArray[4]))) {//有正常数据
//                        //手机
//                        String customerPhone = String.valueOf(objArray[0]);
//                        if (StringUtils.isStrNull(customerPhone)) {
//                            resultResponse.setMessage("第" + (i + startLine + 1) + "行1列,手机不能为空值");
//                            return resultResponse;
//                        }
//                        String regexPhone = "^1[3|4|5|7|8]\\d{9}$";
//                        if (!Pattern.matches(regexPhone, customerPhone)) {
//                            resultResponse.setMessage("第" + (i + startLine + 1) + "行1列,手机格式不正确");
//                            return resultResponse;
//                        }
//                        //姓名
//                        String customerTurename = String.valueOf(objArray[1]);
//                        if (StringUtils.isStrNull(customerTurename)) {
//                            resultResponse.setMessage("第" + (i + startLine + 1) + "行2列,姓名不能为空值");
//                            return resultResponse;
//                        }
//                        if (customerTurename.length() > 20 || customerTurename.length() < 2) {
//                            resultResponse.setMessage("第" + (i + startLine + 1) + "行2列,姓名只能输入2~20个字符");
//                            return resultResponse;
//                        }
//                        //入职日期
//                        String stationEnterTimeStr = String.valueOf(objArray[2]);
//                        if (StringUtils.isStrNull(stationEnterTimeStr)) {
//                            resultResponse.setMessage("第" + (i + startLine + 1) + "行3列,入职日期不能为空值");
//                            return resultResponse;
//                        }
//                        Date stationEnterTime = null;
//                        ResultResponse validatorResponse = validatorDateByExcel(stationEnterTimeStr, i, startLine, 3);
//                        if (validatorResponse.isSuccess()) {
//                            if (validatorResponse.getData() != null) {
//                                stationEnterTime = (Date) validatorResponse.getData();
//                            }
//                        } else {
//                            return validatorResponse;
//                        }
//                        //员工状态
//                        String stationCustomerStateStr = String.valueOf(objArray[3]);
//                        if (StringUtils.isStrNull(stationCustomerStateStr)) {
//                            resultResponse.setMessage("第" + (i + startLine + 1) + "行4列,员工状态不能为空值");
//                            return resultResponse;
//                        }
//                        int stationCustomerState = 0;
//                        if (CustomerConstants.CUSTOMER_CUSTOMERSTATE_REGULAR.equals(stationCustomerStateStr)) {//转正
//                            stationCustomerState = CustomerConstants.CUSTOMER_PERSONSTATE_REGULAR;
//                        } else if (CustomerConstants.CUSTOMER_CUSTOMERSTATE_ENTRY.equals(stationCustomerStateStr)
//                                || CustomerConstants.CUSTOMER_CUSTOMERSTATE_ENTRY2.equals(stationCustomerStateStr)) {//入职
//                            stationCustomerState = CustomerConstants.CUSTOMER_PERSONSTATE_ENTER;
//                        } else {
//                            resultResponse.setMessage("第" + (i + startLine + 1) + "行4列,员工状态请填入转正或者试用期");
//                            return resultResponse;
//                        }
//                        //聘用形式
//                        String stationEmployMethodStr = String.valueOf(objArray[4]);
//                        if (StringUtils.isStrNull(stationEmployMethodStr)) {
//                            resultResponse.setMessage("第" + (i + startLine + 1) + "行5列,聘用形式不能为空值");
//                            return resultResponse;
//                        }
//                        int stationEmployMethod = 0;
//                        if (CustomerConstants.CUSTOMER_EMPLOYETYPE_OFFICAL.equals(stationEmployMethodStr)) {//正式
//                            stationEmployMethod = CustomerConstants.CUSTOMER_EMPLOYMETHOD_OFFICAL;
//                        } else if (CustomerConstants.CUSTOMER_EMPLOYETYPE_TEMPORARY.equals(stationEmployMethodStr)) {//劳务
//                            stationEmployMethod = CustomerConstants.CUSTOMER_EMPLOYMETHOD_TEMPORARY;
//                        } else {
//                            resultResponse.setMessage("第" + (i + startLine + 1) + "行5列,聘用形式请填入正式或者劳务");
//                            return resultResponse;
//                        }
//                        //转正日期
//                        Date stationRegularTime = null;
//                        if (objArray[5] != null) {
//                            String stationRegularTimeStr = String.valueOf(objArray[5]);
//                            ResultResponse validatorStationResponse = validatorDateByExcel(stationRegularTimeStr, i, startLine, 6);
//                            if (validatorStationResponse.isSuccess()) {
//                                if (validatorStationResponse.getData() != null) {
//                                    stationRegularTime = (Date) validatorStationResponse.getData();
//                                }
//                            } else {
//                                return validatorStationResponse;
//                            }
//                        }
//
//                        //工号
//                        String stationCustomerNumber = null;
//                        if (objArray[6] != null) {
//                            stationCustomerNumber = String.valueOf(objArray[6]);
//                            if (!StringUtils.isStrNull(stationCustomerNumber)) {
//                                if (stationCustomerNumber.length() > 20 || stationCustomerNumber.length() < 2) {
//                                    resultResponse.setMessage("第" + (i + startLine + 1) + "行7列,工号只能输入2~20个字符");
//                                    return resultResponse;
//                                }
//                            }
//                        }
//
//                        //身份证号
//                        String customerIdcard = null;
//                        if (objArray[7] != null) {
//                            customerIdcard = String.valueOf(objArray[7]);
//                            if (!StringUtils.isStrNull(customerIdcard)) {
//                                String regexIdcard = "^([0-9]{17}([0-9]|X))|([0-9]{15})$";
//                                if (!Pattern.matches(regexIdcard, customerIdcard)) {
//                                    resultResponse.setMessage("第" + (i + startLine + 1) + "行8列,身份证号不正确");
//                                    return resultResponse;
//                                }
//                            }
//                        }
//
//                        //银行卡号
//                        String customerBanknumber = null;
//                        if (objArray[8] != null) {
//                            customerBanknumber = String.valueOf(objArray[8]);
//                            if (!StringUtils.isStrNull(customerBanknumber)) {
//                                String regexBanknumber = "^[0-9]{15,20}$";
//                                if (!Pattern.matches(regexBanknumber, customerBanknumber)) {
//                                    resultResponse.setMessage("第" + (i + startLine + 1) + "行9列,银行卡号不正确");
//                                    return resultResponse;
//                                }
//                            }
//                        }
//
//                        //开户行
//                        String customerBank = null;
//                        if (objArray[9] != null) {
//                            customerBank = String.valueOf(objArray[9]);
//                            if (!StringUtils.isStrNull(customerBank)) {
//                                if (customerBank.length() > 30 || customerBank.length() < 2) {
//                                    resultResponse.setMessage("第" + (i + startLine + 1) + "行10列,开户行只能输入2~30个字符");
//                                    return resultResponse;
//                                }
//                            }
//                        }
//                        if (stationRegularTime != null
//                                && !stationRegularTime.after(compareDate)
//                                && CustomerConstants.CUSTOMER_CUSTOMERSTATE_ENTRY.equals(stationCustomerStateStr)) {
//                            resultResponse.setMessage("第" + (i + startLine + 1) + "行,转正日期在当前日期之前,员工状态必须为转正");
//                            return resultResponse;
//                        }
//                        //如果入职,转正日期必填
//                        //如果转正,转正日期可填可不填,并且转正日期只能小于或等于当前时间,入职日期只能小于转正日期和当前时间
//                        if (CustomerConstants.CUSTOMER_CUSTOMERSTATE_REGULAR.equals(stationCustomerStateStr)) {//转正
//                            if (stationRegularTime != null) {
//                                if (!stationRegularTime.before(compareDate)) {
//                                    resultResponse.setMessage("第" + (i + startLine + 1) + "行,员工转正,转正日期必须在当前日期之前");
//                                    return resultResponse;
//                                }
//                                if (!stationEnterTime.before(stationRegularTime)) {
//                                    resultResponse.setMessage("第" + (i + startLine + 1) + "行,员工转正,入职日期必须在转正日期之前");
//                                    return resultResponse;
//                                }
//                            } else {
//                                if (!stationEnterTime.before(compareDate)) {
//                                    resultResponse.setMessage("第" + (i + startLine + 1) + "行,员工转正,入职日期必须在当前日期之前");
//                                    return resultResponse;
//                                }
//                            }
//                        } else if (CustomerConstants.CUSTOMER_CUSTOMERSTATE_ENTRY.equals(stationCustomerStateStr)) {//入职
//                            if (stationRegularTime == null) {
//                                resultResponse.setMessage("第" + (i + startLine + 1) + "行,员工入职未转正,请输入转正日期");
//                                return resultResponse;
//                            }
//                            if (!stationEnterTime.before(stationRegularTime)) {
//                                resultResponse.setMessage("第" + (i + startLine + 1) + "行,员工入职未转正,入职日期必须在转正日期之前");
//                                return resultResponse;
//                            }
//                        }
//                        //拼装要新增的数据,用户信息
//                        CustomersBean customersBean = new CustomersBean();
//                        customersBean.setCustomerPhone(customerPhone);
//                        customersBean.setCustomerTurename(customerTurename);
//                        if (!StringUtils.isStrNull(customerIdcard)) {
//                            customersBean.setCustomerIdcard(customerIdcard);
//                        }
//                        if (!StringUtils.isStrNull(customerBanknumber)) {
//                            customersBean.setCustomerBanknumber(customerBanknumber);
//                        }
//                        if (!StringUtils.isStrNull(customerBank)) {
//                            customersBean.setCustomerBank(customerBank);
//                        }
//                        customerBeanList.add(customersBean);
//                        //用户岗位信息
//                        CustomersStationBean customersStationBean = new CustomersStationBean();
//                        customersStationBean.setStationEnterTime(stationEnterTime);
//                        customersStationBean.setStationCustomerState(stationCustomerState);
//                        customersStationBean.setStationEmployMethod(stationEmployMethod);
//                        if (stationRegularTime != null) {
//                            customersStationBean.setStationRegularTime(stationRegularTime);
//                        }
//                        if (!StringUtils.isStrNull(stationCustomerNumber)) {
//                            customersStationBean.setStationCustomerNumber(stationCustomerNumber);
//                        }
//                        customersStationBeanList.add(customersStationBean);
//                    } else if ((objArray[0] == null || StringUtils.isStrNull(String.valueOf(objArray[0])))
//                            && (objArray[1] == null || StringUtils.isStrNull(String.valueOf(objArray[1])))
//                            && (objArray[2] == null || StringUtils.isStrNull(String.valueOf(objArray[2])))
//                            && (objArray[3] == null || StringUtils.isStrNull(String.valueOf(objArray[3])))
//                            && (objArray[4] == null || StringUtils.isStrNull(String.valueOf(objArray[4])))) {//该行无数据
//
//                    } else {
//                        boolean isNullFlag1 = false;//是否为空,true代表为空
//                        boolean isNullFlag2 = false;
//                        boolean isNullFlag3 = false;
//                        boolean isNullFlag4 = false;
//                        boolean isNullFlag5 = false;
//                        if (objArray[0] != null) {
//                            isNullFlag1 = StringUtils.isStrNull(String.valueOf(objArray[0]));
//                        } else {
//                            isNullFlag1 = true;
//                        }
//                        if (objArray[1] != null) {
//                            isNullFlag2 = StringUtils.isStrNull(String.valueOf(objArray[1]));
//                        } else {
//                            isNullFlag2 = true;
//                        }
//                        if (objArray[2] != null) {
//                            isNullFlag3 = StringUtils.isStrNull(String.valueOf(objArray[2]));
//                        } else {
//                            isNullFlag3 = true;
//                        }
//                        if (objArray[3] != null) {
//                            isNullFlag4 = StringUtils.isStrNull(String.valueOf(objArray[3]));
//                        } else {
//                            isNullFlag4 = true;
//                        }
//                        if (objArray[4] != null) {
//                            isNullFlag5 = StringUtils.isStrNull(String.valueOf(objArray[4]));
//                        } else {
//                            isNullFlag5 = true;
//                        }
//                        if (isNullFlag1 && isNullFlag2 && isNullFlag3 && isNullFlag4 && isNullFlag5) {
//
//                        } else {
//                            LOGGER.info("提交上传的新增用户EXCEL文件,手机,姓名,入职日期,员工状态,聘用形式不能有空值");
//                            resultResponse.setMessage("手机姓名入职日期员工状态聘用形式不能有空值");
//                            return resultResponse;
//                        }
//                    }
//                }
//            } else {
//                LOGGER.info("提交上传的新增用户EXCEL文件,上传的excel中没有数据");
//                resultResponse.setMessage("上传的excel中没有数据");
//            }
//            List<String> phoneList = new ArrayList<>();
//            List<String> numberList = new ArrayList<>();
//            List<String> cardList = new ArrayList<>();
//            if (customerBeanList != null && customerBeanList.size() > 0) {//插入之前的验证:验证Excel中手机号\身份证号不能重复
//                for (CustomersBean checkCustomerBean : customerBeanList) {
//                    if (phoneList.contains(checkCustomerBean.getCustomerPhone())) {
//                        LOGGER.info("提交上传的新增用户EXCEL文件,手机号在excel中有重复");
//                        resultResponse.setMessage("手机号在excel中有重复");
//                        return resultResponse;
//                    } else {
//                        phoneList.add(checkCustomerBean.getCustomerPhone());
//                    }
//                    if (!StringUtils.isStrNull(checkCustomerBean.getCustomerIdcard())) {
//                        if (numberList.contains(checkCustomerBean.getCustomerIdcard())) {
//                            LOGGER.info("提交上传的新增用户EXCEL文件,身份证号在excel中有重复");
//                            resultResponse.setMessage("身份证号在excel中有重复");
//                            return resultResponse;
//                        } else {
//                            cardList.add(checkCustomerBean.getCustomerIdcard());
//                        }
//                    }
//                }
//            }
//            if (customersStationBeanList != null && customersStationBeanList.size() > 0) {//插入之前的验证:验证Excel中工号不能重复
//                for (CustomersStationBean checkStationBean : customersStationBeanList) {
//                    if (!StringUtils.isStrNull(checkStationBean.getStationCustomerNumber())) {
//                        if (numberList.contains(checkStationBean.getStationCustomerNumber())) {
//                            LOGGER.info("提交上传的新增用户EXCEL文件,手机号在excel中有重复");
//                            resultResponse.setMessage("手机号在excel中有重复");
//                            return resultResponse;
//                        } else {
//                            numberList.add(checkStationBean.getStationCustomerNumber());
//                        }
//                    }
//                }
//            }
//
//            //验证身份证号是否是唯一的
//            if (cardList != null && cardList.size() > 0) {
//                Map<Integer, List<CustomersDto>> cardMap = selectidCardForeach(cardList, companysBean.getCompanyId(), CustomerConstants.CUSTOMER_WILLOPERATION_ADD, -1);
//                if (cardMap != null && cardMap.size() > 0 && cardMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY) != null && cardMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY).size() > 0) {
//                    String showErrorStr = "";
//                    List<CustomersDto> customersList = cardMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY);
//                    for (CustomersDto checkDto : customersList) {
//                        showErrorStr += checkDto.getCustomerIdcard() + ",";
//                    }
//                    resultResponse.setMessage("身份证号:" + showErrorStr + "已经存在");
//                    return resultResponse;
//                }
//            }
//            //验证工号是否是唯一的
//            if (numberList != null && numberList.size() > 0) {
//                Map<Integer, List<CustomersStationBean>> cardMap = customersStationService.selectStatonNumberForeach(numberList, companysBean.getCompanyId(), CustomerConstants.CUSTOMER_WILLOPERATION_ADD, -1);
//                if (cardMap != null && cardMap.size() > 0 && cardMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY) != null && cardMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY).size() > 0) {
//                    String showErrorStr = "";
//                    List<CustomersStationBean> customersList = cardMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY);
//                    for (CustomersStationBean checkDto : customersList) {
//                        showErrorStr += checkDto.getStationCustomerNumber() + ",";
//                    }
//                    resultResponse.setMessage("工号:" + showErrorStr + "已经存在");
//                    return resultResponse;
//                }
//            }
//            //验证手机号是否是唯一的
//            //要编辑的手机号
//            List<CustomersBean> willEditPhoneDto = new ArrayList<>();
//            List<CustomersStationBean> willEditPhoneStation = new ArrayList<>();
//            //要新增的手机号
//            List<CustomersBean> willAddPhoneDto = new ArrayList<>();
//            List<CustomersStationBean> willAddPhoneStation = new ArrayList<>();
//            if (phoneList != null && phoneList.size() > 0) {
//                Map<Integer, List<CustomersDto>> phoneMap = selectPhoneForeach(phoneList, companysBean.getCompanyId(), CustomerConstants.CUSTOMER_WILLOPERATION_ADD, -1);
//                if (phoneMap != null && phoneMap.size() > 0 && phoneMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY) != null && phoneMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY).size() > 0) {
//                    String showErrorStr = "";
//                    List<CustomersDto> customersList = phoneMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY);
//                    for (CustomersDto checkDto : customersList) {
//                        showErrorStr += checkDto.getCustomerPhone() + ",";
//                    }
//                    resultResponse.setMessage("手机号:" + showErrorStr + "已经存在");
//                    return resultResponse;
//                }
//                if (customerBeanList != null && customerBeanList.size() > 0) {
//                    for (int i = 0; i < customerBeanList.size(); i++) {
//                        CustomersBean editCustomerBean = customerBeanList.get(i);
//                        boolean phoneCheckFlag = false;//是否有符合编辑的customer,false代表没有符合编辑的customer
//                        //是否是符合编辑的customerbean
//                        if (phoneMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_EDIT) != null && phoneMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_EDIT).size() > 0) {
//                            for (CustomersDto willEditDto : phoneMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_EDIT)) {
//                                if (editCustomerBean.getCustomerPhone().equals(willEditDto.getCustomerPhone())) {
//                                    editCustomerBean.setCustomerId(willEditDto.getCustomerId());
//                                    willEditPhoneDto.add(editCustomerBean);
//                                    willEditPhoneStation.add(customersStationBeanList.get(i));
//                                    phoneCheckFlag = true;
//                                    break;
//                                }
//                            }
//                        }
//                        if (!phoneCheckFlag) {
//                            willAddPhoneDto.add(editCustomerBean);
//                            willAddPhoneStation.add(customersStationBeanList.get(i));
//                        }
//
//                    }
//                }
//            }
//
//            //插入数据
//            Date newDate = new Date();
//            //员工记录信息列表(方便批量插入数据)
//            List<CustomersRecordBean> customersRecordBeanList = new ArrayList<>();
//            //员工账户列表(方便批量插入数据)
//            List<SubAccountBean> subAccountBeanList = new ArrayList<>();
//            BigDecimal commDecimal = new BigDecimal("0.00");
//            if (willAddPhoneDto != null && willAddPhoneDto.size() > 0) {
//                for (int i = 0; i < willAddPhoneDto.size(); i++) {
//                    CustomersBean insertCustomerBean = willAddPhoneDto.get(i);
//                    insertCustomerBean.setCustomerCompanyId(companysBean.getCompanyId());//企业ID
//                    insertCustomerBean.setCustomerCompanyName(companysBean.getCompanyName());//企业名称
//                    insertCustomerBean.setCustomerSign(1);//可使用
//                    insertCustomerBean.setCustomerIspay(1);//可发工资
//                    insertCustomerBean.setCustomerAddtime(newDate);//添加时间
//                    insertCustomerBean.setCustomerIsExpense(0);
//                    //社保通状态
//                    insertCustomerBean.setCustomerSocialApproveState(CustomerConstants.CUSTOMER_SOCIAL_APPROVESTATE_NO);
//                    insertCustomerBean.setCustomerCurrentExpense(BigDecimal.ZERO);
//                    //插入员工信息
//                    Long customerId = insertCustomerForId(insertCustomerBean);
//                    if (customerId == null || customerId <= 0L) {
//                        LOGGER.info("提交上传的新增用户EXCEL文件,插入员工信息失败");
//                        resultResponse.setMessage("插入员工信息失败");
//                        return resultResponse;
//                    }
//                    //拼装岗位信息
//                    willAddPhoneStation.get(i).setStationCustomerId(customerId);
//                    willAddPhoneStation.get(i).setStationCreateTime(newDate);
//                    willAddPhoneStation.get(i).setStationUpdateTime(newDate);
//                    //拼装员工记录信息
//                    CustomersRecordBean customersRecordBean = new CustomersRecordBean();
//                    customersRecordBean.setRecordCustomerId(customerId);
//                    customersRecordBean.setRecordOperationTime(newDate);
//                    customersRecordBean.setRecordCreateTime(newDate);
//                    customersRecordBean.setRecordUpdateTime(newDate);
//                    if (willAddPhoneStation.get(i).getStationCustomerState() == CustomerConstants.CUSTOMER_PERSONSTATE_ENTER) {//入职
//                        customersRecordBean.setRecordOperationType(CustomerConstants.CUSTOMER_RECORDTYPE_ENTER);
//                        customersRecordBeanList.add(customersRecordBean);
//                    } else if (willAddPhoneStation.get(i).getStationCustomerState() == CustomerConstants.CUSTOMER_PERSONSTATE_REGULAR) {//转正
//                        customersRecordBean.setRecordOperationType(CustomerConstants.CUSTOMER_RECORDTYPE_ENTER);
//                        customersRecordBeanList.add(customersRecordBean);
//                        customersRecordBean = new CustomersRecordBean();
//                        customersRecordBean.setRecordCustomerId(customerId);
//                        customersRecordBean.setRecordOperationTime(newDate);
//                        customersRecordBean.setRecordCreateTime(newDate);
//                        customersRecordBean.setRecordUpdateTime(newDate);
//                        customersRecordBean.setRecordOperationType(CustomerConstants.CUSTOMER_RECORDTYPE_REGULAR);
//                        customersRecordBeanList.add(customersRecordBean);
//                    }
//                    //拼装员工账户信息
//                    SubAccountBean subAccountBean = new SubAccountBean();
//                    subAccountBean.setCustId(customerId);
//                    subAccountBean.setProperty(CompanyProtocolConstants.SUBACCOUNT_PROPERTY_CUSTOMER);//账户性质 1-个人 2-企业
//                    subAccountBean.setAmout(commDecimal);
//                    subAccountBean.setCashAmout(commDecimal);
//                    subAccountBean.setUncashAmount(commDecimal);
//                    subAccountBean.setFreezeCashAmount(commDecimal);
//                    subAccountBean.setFreezeUncashAmount(commDecimal);
//                    subAccountBean.setHash(MethodUtil.MD5(UUID.randomUUID().toString()));
//                    subAccountBean.setCheckValue(subAccountService.getMd5Code(subAccountBean));
//                    subAccountBean.setState("00");//状态 00-生效,01-冻结,02-注销
//                    subAccountBean.setIsAutoTransfer(1);//余额是否自动转入 0:非自动 1;自动
//                    subAccountBean.setCreateTime(newDate);
//                    subAccountBean.setUpdateTime(newDate);
//                    subAccountBeanList.add(subAccountBean);
//                }
//                //批量插入岗位信息
//                if (willAddPhoneStation != null && willAddPhoneStation.size() > 0) {
//                    int stationCount = customersStationService.insertBatch(willAddPhoneStation);
//                    if (stationCount != willAddPhoneStation.size()) {
//                        LOGGER.info("提交上传的新增用户EXCEL文件,插入员工岗位信息失败");
//                        resultResponse.setMessage("插入员工岗位信息失败");
//                        return resultResponse;
//                    }
//                }
//                //批量插入
//                if (subAccountBeanList != null && subAccountBeanList.size() > 0) {
//                    int accountCount = subAccountService.insertBatch(subAccountBeanList);
//                    if (accountCount != subAccountBeanList.size()) {
//                        LOGGER.info("提交上传的新增用户EXCEL文件,批量插入员工账户信息失败");
//                        resultResponse.setMessage("批量插入员工账户信息失败");
//                        return resultResponse;
//                    }
//                }
//
//            }
//            //如果存在手机号相等但是离职状态的则编辑(这个数据量很小,大多数没有)
//            if (willEditPhoneDto != null && willEditPhoneDto.size() > 0) {
//                for (int i = 0; i < willEditPhoneDto.size(); i++) {
//                    CustomersBean editCustomerBean = willEditPhoneDto.get(i);
//                    editCustomerBean.setCustomerCompanyId(companysBean.getCompanyId());//企业ID
//                    editCustomerBean.setCustomerCompanyName(companysBean.getCompanyName());//企业名称
//                    editCustomerBean.setCustomerSign(1);//可使用
//                    editCustomerBean.setCustomerIspay(1);//可发工资
//                    editCustomerBean.setCustomerAddtime(newDate);//添加时间
//                    editCustomerBean.setCustomerIsExpense(0);
//                    //社保通状态
//                    editCustomerBean.setCustomerSocialApproveState(CustomerConstants.CUSTOMER_SOCIAL_APPROVESTATE_NO);
//                    editCustomerBean.setCustomerCurrentExpense(BigDecimal.ZERO);
//                    if (StringUtils.isStrNull(editCustomerBean.getCustomerIdcard())) {
//                        editCustomerBean.setCustomerIdcard("");
//                    }
//                    if (StringUtils.isStrNull(editCustomerBean.getCustomerBank())) {
//                        editCustomerBean.setCustomerBank("");
//                    }
//                    if (StringUtils.isStrNull(editCustomerBean.getCustomerBanknumber())) {
//                        editCustomerBean.setCustomerBanknumber("");
//                    }
//                    //更新员工信息
//                    int count = updateByPrimaryKeySelective(editCustomerBean);
//                    if (count <= 0) {
//                        LOGGER.info("提交上传的新增用户EXCEL文件,编辑员工基本信息失败");
//                        resultResponse.setMessage("编辑员工基本信息失败");
//                        return resultResponse;
//                    }
//                    //拼装岗位信息
//                    willEditPhoneStation.get(i).setStationCustomerId(editCustomerBean.getCustomerId());
//                    willEditPhoneStation.get(i).setStationUpdateTime(newDate);
//                    if (willEditPhoneStation.get(i).getStationRegularTime() == null) {
//                        //特殊验证,将转正时间更新为空
//                        willEditPhoneStation.get(i).setCheckRegularTime(1);
//                    }
//                    if (StringUtils.isStrNull(willEditPhoneStation.get(i).getStationCustomerNumber())) {
//                        willEditPhoneStation.get(i).setStationCustomerNumber("");
//                    }
//                    //更新岗位信息
//                    int stationCount = customersStationService.updateByCustomerId(willEditPhoneStation.get(i));
//                    if (stationCount <= 0) {
//                        willEditPhoneStation.get(i).setStationCreateTime(newDate);
//                        int stationInsertCount = customersStationService.insert(willEditPhoneStation.get(i));
//                        if (stationInsertCount <= 0) {
//                            LOGGER.info("提交上传的新增用户EXCEL文件,编辑完后插入员工岗位信息失败");
//                            resultResponse.setMessage("插入员工岗位信息失败");
//                            return resultResponse;
//                        }
//                    }
//                    //拼装员工记录信息
//                    CustomersRecordBean customersRecordBean = new CustomersRecordBean();
//                    customersRecordBean.setRecordCustomerId(editCustomerBean.getCustomerId());
//                    customersRecordBean.setRecordOperationTime(newDate);
//                    customersRecordBean.setRecordCreateTime(newDate);
//                    customersRecordBean.setRecordUpdateTime(newDate);
//                    if (willEditPhoneStation.get(i).getStationCustomerState() == CustomerConstants.CUSTOMER_PERSONSTATE_ENTER) {//入职
//                        customersRecordBean.setRecordOperationType(CustomerConstants.CUSTOMER_RECORDTYPE_ENTER);
//                        customersRecordBeanList.add(customersRecordBean);
//                    } else if (willEditPhoneStation.get(i).getStationCustomerState() == CustomerConstants.CUSTOMER_PERSONSTATE_REGULAR) {//转正
//                        customersRecordBean.setRecordOperationType(CustomerConstants.CUSTOMER_RECORDTYPE_ENTER);
//                        customersRecordBeanList.add(customersRecordBean);
//                        customersRecordBean = new CustomersRecordBean();
//                        customersRecordBean.setRecordCustomerId(editCustomerBean.getCustomerId());
//                        customersRecordBean.setRecordOperationTime(newDate);
//                        customersRecordBean.setRecordCreateTime(newDate);
//                        customersRecordBean.setRecordUpdateTime(newDate);
//                        customersRecordBean.setRecordOperationType(CustomerConstants.CUSTOMER_RECORDTYPE_REGULAR);
//                        customersRecordBeanList.add(customersRecordBean);
//                    }
//                }
//            }
//
//            //批量插入员工记录
//            if (customersRecordBeanList != null && customersRecordBeanList.size() > 0) {
//                int recordCount = customersRecordService.insertBatch(customersRecordBeanList);
//                if (recordCount != customersRecordBeanList.size()) {
//                    LOGGER.info("提交上传的新增用户EXCEL文件,插入员工记录信息失败");
//                    resultResponse.setMessage("插入员工记录信息失败");
//                    return resultResponse;
//                }
//            }
//            resultResponse.setSuccess(true);
//        } else {
//            LOGGER.info("提交上传的新增用户EXCEL文件,文件不存在,上传失败");
//            resultResponse.setMessage("文件不存在,上传失败");
//        }
//        return resultResponse;
    }

    @Override
    public List<CustomersBean> selectUserCustomerByCompanyId(Long companyId) {
        return customersReaderMapper.selectUsefulCustomerByCompanyId(companyId);
    }

    /**
     * 查询手机号是否有重复
     *
     * @param list
     * @return
     */
    public Map<Integer, List<CustomersDto>> selectPhoneForeach(List<String> list, Long companyId, int type, long customerId) {
//        return customersReaderMapper.selectPhoneForeach(list,companyId);
        //重复的LIST
        List<CustomersDto> copyList = new ArrayList<>();
        //新增的LIST
        List<CustomersDto> addList = new ArrayList<>();
        //编辑的LIST
        List<CustomersDto> editList = new ArrayList<>();
        Map<Integer, List<CustomersDto>> map = new HashMap<>();
        List<CustomersDto> dtoList = customersReaderMapper.selectPhoneForeach(list, companyId);
        if (dtoList != null && dtoList.size() > 0) {
            for (CustomersDto customersDto : dtoList) {
                if (customersDto != null && customersDto.getStationCustomerState() != null) {
                    if (type == CustomerConstants.CUSTOMER_WILLOPERATION_ADD) {
                        if (customersDto.getStationCustomerState().intValue() == CustomerConstants.CUSTOMER_PERSONSTATE_ENTER
                                || customersDto.getStationCustomerState().intValue() == CustomerConstants.CUSTOMER_PERSONSTATE_REGULAR) {
                            copyList.add(customersDto);
                            map.put(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY, copyList);

                        } else if (customersDto.getStationCustomerState().intValue() == CustomerConstants.CUSTOMER_PERSONSTATE_LEAVE) {
                            editList.add(customersDto);
                            map.put(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_EDIT, editList);
                        } else if (customersDto.getStationCustomerState().intValue() == CustomerConstants.CUSTOMER_PERSONSTATE_DELETE) {
                            addList.add(customersDto);
                            map.put(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_ADD, addList);
                        }
                    } else if (type == CustomerConstants.CUSTOMER_WILLOPERATION_EDIT) {
                        if (customersDto.getCustomerId() != null && customersDto.getCustomerId().intValue() != customerId) {
                            if (customersDto.getStationCustomerState().intValue() == CustomerConstants.CUSTOMER_PERSONSTATE_ENTER
                                    || customersDto.getStationCustomerState().intValue() == CustomerConstants.CUSTOMER_PERSONSTATE_REGULAR
                                    || customersDto.getStationCustomerState().intValue() == CustomerConstants.CUSTOMER_PERSONSTATE_LEAVE) {
                                copyList.add(customersDto);
                                map.put(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY, copyList);
                            }
                        }

                    }
                } else {
                    if (type == CustomerConstants.CUSTOMER_WILLOPERATION_ADD) {
                        copyList.add(customersDto);
                        map.put(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY, copyList);
                    } else if (type == CustomerConstants.CUSTOMER_WILLOPERATION_EDIT) {
                        if (customersDto.getCustomerId() != null && customersDto.getCustomerId().intValue() != customerId) {
                            copyList.add(customersDto);
                            map.put(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY, copyList);
                        }
                    }
                }
            }
        }
        return map;
    }

    /**
     * 查询身份证号是否有重复
     *
     * @param list type 1新增 2修改
     * @return
     */
    public Map<Integer, List<CustomersDto>> selectidCardForeach(List<String> list, Long companyId, int type, long customerId) {
        //重复的LIST
        List<CustomersDto> copyList = new ArrayList<>();
//        //新增的LIST
//        List<CustomersDto> addList=new ArrayList<>();
//        //编辑的LIST
//        List<CustomersDto> editList=new ArrayList<>();
        Map<Integer, List<CustomersDto>> map = new HashMap<>();
        List<CustomersDto> dtoList = customersReaderMapper.selectidCardForeach(list, companyId);
        if (dtoList != null && dtoList.size() > 0) {
            for (CustomersDto customersDto : dtoList) {
                if (customersDto != null && customersDto.getStationCustomerState() != null) {
                    if (type == CustomerConstants.CUSTOMER_WILLOPERATION_ADD) {
                        if (customersDto.getStationCustomerState().intValue() == CustomerConstants.CUSTOMER_PERSONSTATE_ENTER
                                || customersDto.getStationCustomerState().intValue() == CustomerConstants.CUSTOMER_PERSONSTATE_REGULAR) {
                            copyList.add(customersDto);
                            map.put(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY, copyList);
                        }
//                        else if(customersDto.getStationCustomerState().intValue()==CustomerConstants.CUSTOMER_PERSONSTATE_LEAVE){
//                            editList.add(customersDto);
//                            map.put(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_EDIT,editList);
//                        }else if(customersDto.getStationCustomerState().intValue()==CustomerConstants.CUSTOMER_PERSONSTATE_DELETE){
//                            addList.add(customersDto);
//                            map.put(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_ADD,addList);
//                        }
                    } else if (type == CustomerConstants.CUSTOMER_WILLOPERATION_EDIT) {
                        if (customersDto.getCustomerId() != null && customersDto.getCustomerId().intValue() != customerId) {
                            if (customersDto.getStationCustomerState().intValue() == CustomerConstants.CUSTOMER_PERSONSTATE_ENTER
                                    || customersDto.getStationCustomerState().intValue() == CustomerConstants.CUSTOMER_PERSONSTATE_REGULAR
                                    || customersDto.getStationCustomerState().intValue() == CustomerConstants.CUSTOMER_PERSONSTATE_LEAVE) {
                                copyList.add(customersDto);
                                map.put(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY, copyList);
                            }
//                            else if(customersDto.getStationCustomerState().intValue()==CustomerConstants.CUSTOMER_PERSONSTATE_DELETE){
//                                editList.add(customersDto);
//                                map.put(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_EDIT,editList);
//                            }
                        }

                    }
                }
//                else if(customersDto!=null && customersDto.getStationCustomerState()==null){
//                    if(type==CustomerConstants.CUSTOMER_WILLOPERATION_ADD){
//                        addList.add(customersDto);
//                            map.put(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_ADD,addList);
//                    }else if(type==CustomerConstants.CUSTOMER_WILLOPERATION_EDIT){
//                        if(customersDto.getCustomerId()!=null && customersDto.getCustomerId().intValue()!=customerId){
//                            editList.add(customersDto);
//                            map.put(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_EDIT,editList);
//                        }
//
//                    }
//                }
            }
        }
        return map;
    }

    private static List<Object[]> getDataFromExcel(byte[] bytes, int startline, int sheetNum, int sheetLastNum) throws Exception {
        ArrayList<Object[]> arraylist = new ArrayList<Object[]>();
        try {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            // 先用excel2003的方法 读取数据
            arraylist = (ArrayList<Object[]>) getDataFromExcel03(inputStream, startline, sheetNum, sheetLastNum);
        } catch (Exception e) {
            InputStream inputStream = new ByteArrayInputStream(bytes);
            // 如果出现异常， 则用excel2007的方法 读取数据
            // 暂时无法实现，容易出现错误数据
            arraylist = (ArrayList<Object[]>) getDataFromExcel07(inputStream, startline, sheetNum, sheetLastNum);
        }
        return arraylist;
    }

    /**
     * 2003excel解析
     *
     * @param inputstream
     * @param startline
     * @param sheetNum
     * @return
     * @throws Exception
     */
    private static List<Object[]> getDataFromExcel03(InputStream inputstream, int startline, int sheetNum, int sheetLastNum) throws Exception {
        ArrayList<Object[]> arraylist = new ArrayList<Object[]>();
        HSSFWorkbook hssfworkbook = new HSSFWorkbook(inputstream);
        HSSFSheet hssfsheet = hssfworkbook.getSheetAt(sheetNum);
        //总行数
        int trLength = hssfsheet.getPhysicalNumberOfRows();
        for (int j = startline; j < trLength; j++) {
            HSSFRow hssfrow = hssfsheet.getRow(j);
            if (hssfrow == null || ExcelUtil.isBlankRow(hssfrow)) {
                break;
            }
            int word0 = sheetLastNum;
            if (word0 >= 0) {
                Object[] aobj = new Object[word0];
                for (int k = 0; k < word0; k++) {
                    HSSFCell hssfcell = hssfrow.getCell((short) k);
                    if (hssfcell != null) {
                        aobj[k] = ExcelUtil.getValue(hssfcell);
                    } else {
                        aobj[k] = "";
                    }
                }
                arraylist.add(aobj);
            }
        }
        return arraylist;
    }

    /**
     * 2007Excel解析
     *
     * @param inputstream
     * @param startline
     * @param sheetNum
     * @return
     * @throws Exception
     */
    public static List<Object[]> getDataFromExcel07(InputStream inputstream, int startline, int sheetNum, int sheetLastNum) throws Exception {
        ArrayList<Object[]> arraylist = new ArrayList<Object[]>();
        Workbook wb = WorkbookFactory.create(inputstream);

        //3.得到Excel工作表对象
        Sheet sheet = wb.getSheetAt(sheetNum);
        //总行数
        int trLength = sheet.getLastRowNum() + 1;
        //4.得到Excel工作表的行
//        Row row = sheet.getRow(0);
        //总列数
//        int tdLength = row.getLastCellNum();
        for (int i = startline; i < trLength; i++) {
            //得到Excel工作表的行
            Row row1 = sheet.getRow(i);
            int tdLength = sheetLastNum;
            if (row1 != null && tdLength > 0) {
                Object[] aobj = new Object[tdLength];
                for (int j = 0; j < tdLength; j++) {
                    //得到Excel工作表指定行的单元格
                    Cell cell1 = row1.getCell(j);
                    if (cell1 == null) {
                        aobj[j] = "";
                    } else {
                        aobj[j] = ExcelUtil.getValue(cell1);
                    }
                }
                arraylist.add(aobj);
            }
        }
        return arraylist;
    }

    private ResultResponse validatorDateByExcel(String stationRegularTimeStr, int i, int startLine, int j,List<String> errorList) throws Exception {
        ResultResponse resultResponse = new ResultResponse();
        if (!StringUtils.isStrNull(stationRegularTimeStr)) {
            String regexRegularTime = "^[0-9]{8}$|^[0-9]{4}[.]{1}[0-9]{1,2}[.]{1}[0-9]{1,2}$|^([0-9]{4}[-]{1}([0-9]){1,2}[-]{1}([0-9]){1,2})$|^([0-9]{4}[/]{1}([0-9]{1,2})[/]{1}([0-9]{1,2}))$";
            if (!Pattern.matches(regexRegularTime, stationRegularTimeStr)) {
                resultResponse.setMessage("第" + (i + startLine + 1) + "行" + j + "列,日期格式不正确");
                errorList.add("第" + (i + startLine + 1) + "行" + j + "列,日期格式不正确");
//                return resultResponse;
            }
            if(errorList==null || errorList.size()<=0){
                Date stationRegularTime = null;
                String[] stationRegularTimeArray = null;
                if (stationRegularTimeStr.contains(".")) {
                    stationRegularTimeArray = stationRegularTimeStr.split("\\.");
                } else if (stationRegularTimeStr.contains("/")) {
                    stationRegularTimeArray = stationRegularTimeStr.split("/");
                } else if (stationRegularTimeStr.contains("-")) {
                    stationRegularTimeArray = stationRegularTimeStr.split("-");
                }
                if (stationRegularTimeArray != null && stationRegularTimeArray.length > 0) {
                    int year = Integer.parseInt(stationRegularTimeArray[0]);
                    if (year < 1900) {
                        resultResponse.setMessage("第" + (i + startLine + 1) + "行" + j + "列,日期不能小于1900年");
                        errorList.add("第" + (i + startLine + 1) + "行" + j + "列,日期不能小于1900年");
//                    return resultResponse;
                    }
                    int month = Integer.parseInt(stationRegularTimeArray[1]);
                    if (month > 12 || month <= 0) {
                        resultResponse.setMessage("第" + (i + startLine + 1) + "行" + j + "列,日期月份不正确");
                        errorList.add("第" + (i + startLine + 1) + "行" + j + "列,日期月份不正确");
//                    return resultResponse;
                    }
                    int day = Integer.parseInt(stationRegularTimeArray[2]);
                    //获取该月最大号
                    Calendar calendar = Calendar.getInstance();
                    String yearMonth = stationRegularTimeArray[0] + "-" + stationRegularTimeArray[1];
                    calendar.setTime(CustomerConstants.SDF_YEAR_MONTH.parse(yearMonth));
                    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                    int maxDay = Integer.parseInt(CustomerConstants.SDF_DAY.format(calendar.getTime()));
                    if (day > maxDay || day <= 0) {
                        resultResponse.setMessage("第" + (i + startLine + 1) + "行" + j + "列,日期日不正确");
                        errorList.add("第" + (i + startLine + 1) + "行" + j + "列,日期日不正确");
//                    return resultResponse;
                    }
                } else {
                    int year = Integer.parseInt(stationRegularTimeStr.substring(0, 4));
                    if (year < 1900) {
                        resultResponse.setMessage("第" + (i + startLine + 1) + "行" + j + "列,日期不能小于1900年");
                        errorList.add("第" + (i + startLine + 1) + "行" + j + "列,日期不能小于1900年");
//                    return resultResponse;
                    }
                    int month = Integer.parseInt(stationRegularTimeStr.substring(4, 6));
                    if (month > 12 || month <= 0) {
                        resultResponse.setMessage("第" + (i + startLine + 1) + "行" + j + "列,日期月份不正确");
                        errorList.add("第" + (i + startLine + 1) + "行" + j + "列,日期月份不正确");
//                    return resultResponse;
                    }
                    int day = Integer.parseInt(stationRegularTimeStr.substring(6, 8));
                    //获取该月最大号
                    Calendar calendar = Calendar.getInstance();
                    String yearMonth = year + "-" + month;
                    calendar.setTime(CustomerConstants.SDF_YEAR_MONTH.parse(yearMonth));
                    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                    int maxDay = Integer.parseInt(CustomerConstants.SDF_DAY.format(calendar.getTime()));
                    if (day > maxDay || day <= 0) {
                        resultResponse.setMessage("第" + (i + startLine + 1) + "行" + j + "列,日期日不正确");
                        errorList.add("第" + (i + startLine + 1) + "行" + j + "列,日期日不正确");
//                    return resultResponse;
                    }
                }
                if (stationRegularTimeStr.contains(".")) {
                    stationRegularTime = CustomerConstants.SDF_YEAR_MONTH_DAY_POINT.parse(stationRegularTimeStr);
                } else if (stationRegularTimeStr.contains("/")) {
                    stationRegularTime = CustomerConstants.SDF_YEAR_MONTH_DAY_XX.parse(stationRegularTimeStr);
                } else if (stationRegularTimeStr.contains("-")) {
                    stationRegularTime = CustomerConstants.SDF_YEAR_MONTH_DAY_LINE.parse(stationRegularTimeStr);
                } else {
                    stationRegularTime = CustomerConstants.SDF_YEAR_MONTH_DAY.parse(stationRegularTimeStr);
                }
                resultResponse.setSuccess(true);
                resultResponse.setData(stationRegularTime);
            }
        } else {
            resultResponse.setSuccess(true);
        }
        return resultResponse;
    }

    /**
     * 更新用户信息
     *
     * @param customersBean
     * @param customersStationBean
     * @param customersPersonalBean
     * @return
     * @throws BusinessException
     */
    @Transactional
    public ResultResponse modifyCustomers(CustomersBean customersBean, CustomersStationBean customersStationBean, CustomersPersonalBean customersPersonalBean) throws BusinessException {
        ResultResponse resultResponse = new ResultResponse();
        int count = updateByPrimaryKeySelective(customersBean);
        if (count <= 0) {
            throw new BusinessException("更新用户基本信息失败");
        }
        Date nowDate = new Date();
        customersStationBean.setStationUpdateTime(nowDate);
        int countStation = customersStationService.updateByPrimaryKeySelective(customersStationBean);
        if (countStation <= 0) {
            throw new BusinessException("更新用户岗位信息失败");
        }
        if (customersPersonalBean != null && customersPersonalBean.getPersonalId() != null) {
            customersPersonalBean.setPersonalUpdateTime(nowDate);
            int countPersonal = customersPersonalService.updateByPrimaryKeySelective(customersPersonalBean);
            if (countPersonal <= 0) {
                resultResponse.setMessage("更新用户个人信息失败");
            }
        } else {
            int countPersonal = customersPersonalService.insert(customersPersonalBean);
            if (countPersonal <= 0) {
                resultResponse.setMessage("新增用户个人信息失败");
            }
        }

        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 更新用户基本信息
     *
     * @param customersBean
     * @return
     * @throws BusinessException
     */
    public ResultResponse modifyCustomerBase(CustomersBean customersBean, Long companyId) {
        ResultResponse resultResponse = new ResultResponse();
        //验证
        if (StringUtils.isStrNull(customersBean.getCustomerTurename())) {
            resultResponse.setMessage("请输入姓名");
            return resultResponse;
        }
        if (customersBean.getCustomerSex() == null) {
            resultResponse.setMessage("请输入性别");
            return resultResponse;
        }
        if (StringUtils.isStrNull(customersBean.getCustomerPhone())) {
            resultResponse.setMessage("请输入手机号");
            return resultResponse;
        }
        if (StringUtils.isStrNull(customersBean.getCustomerIdcard())) {
            resultResponse.setMessage("请输入身份证号");
            return resultResponse;
        }
        if (StringUtils.isStrNull(customersBean.getCustomerIdcardImgFront())) {
            resultResponse.setMessage("请上传身份证正面照");
            return resultResponse;
        }
        if (StringUtils.isStrNull(customersBean.getCustomerIdcardImgBack())) {
            resultResponse.setMessage("请上传身份证反而照");
            return resultResponse;
        }
        //验证身份证号是否是唯一的
        if (companyId != null) {
            List<String> cardStrList = new ArrayList<>();
            cardStrList.add(customersBean.getCustomerIdcard());
            Map<Integer, List<CustomersDto>> cardMap = selectidCardForeach(cardStrList, companyId, CustomerConstants.CUSTOMER_WILLOPERATION_EDIT, customersBean.getCustomerId());
            if (cardMap != null && cardMap.size() > 0 && cardMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY) != null && cardMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY).size() > 0) {
                resultResponse.setMessage("身份证号已经存在");
                return resultResponse;
            }
        }
        //验证手机号是否是唯一的
        if (companyId != null) {
            List<String> phoneStrList = new ArrayList<>();
            phoneStrList.add(customersBean.getCustomerPhone());
            Map<Integer, List<CustomersDto>> phoneMap = selectPhoneForeach(phoneStrList, companyId, CustomerConstants.CUSTOMER_WILLOPERATION_EDIT, customersBean.getCustomerId());
            if (phoneMap != null && phoneMap.size() > 0 && phoneMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY) != null && phoneMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY).size() > 0) {
                resultResponse.setMessage("手机号已经存在");
                return resultResponse;
            }
        }
        if (customersBean.getCustomerAge() == null) {
            customersBean.setCustomerAge(0);
        }
        if (StringUtils.isStrNull(customersBean.getCustomerMarriage())) {
            customersBean.setCustomerMarriage("");
        }
        //如果姓名、性别、手机号、身份证号和照片有变更，则社保通状态更新为未审核
        ResultResponse checkResponse = selectCustomerInfoDetail(customersBean.getCustomerId());
        if (checkResponse != null && checkResponse.getData() != null) {
            CustomersDto dto = (CustomersDto) checkResponse.getData();
            if (dto != null) {
                if (!customersBean.getCustomerTurename().equals(dto.getCustomerTurename())
                        || customersBean.getCustomerSex().intValue() != (dto.getCustomerSex() != null ? dto.getCustomerSex().intValue() : 0)
                        || !customersBean.getCustomerPhone().equals(dto.getCustomerPhone())
                        || !customersBean.getCustomerIdcard().equals(dto.getCustomerIdcard())
                        || !customersBean.getCustomerIdcardImgFront().equals(dto.getCustomerIdcardImgFront())
                        || !customersBean.getCustomerIdcardImgBack().equals(dto.getCustomerIdcardImgBack())) {
                    customersBean.setCustomerSocialApproveState(CustomerConstants.CUSTOMER_SOCIAL_APPROVESTATE_NO);
                }
            } else {
                resultResponse.setMessage("获取不到原用户基本信息");
                return resultResponse;
            }
        } else {
            resultResponse.setMessage("获取不到原用户基本信息");
            return resultResponse;
        }
        int count = updateByPrimaryKeySelective(customersBean);
        if (count <= 0) {
            LOGGER.info("更新用户基本信息失败");
            resultResponse.setMessage("更新用户基本信息失败");
        } else {
            resultResponse.setSuccess(true);
        }
        return resultResponse;
    }

    /**
     * 薪资获取员工数量
     *
     * @param startTimeStr
     * @param endTimeStr
     * @param companyId
     * @return
     * @throws ParseException
     */
    public Map<String, Integer> querySaralyInfo(String startTimeStr, String endTimeStr, Long companyId) throws ParseException {
        LOGGER.info("薪资获取员工数量,传递参数:开始时间:" + startTimeStr + ",结束时间:" + endTimeStr + ",企业ID:" + companyId);
        Map<String, Integer> resultMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startStr = startTimeStr + " 00:00:00";
        Date startDate = sdf.parse(startStr);
        String endStr = endTimeStr + " 23:59:59";
        Date endDate = sdf.parse(endStr);

        //获取入职\离职\待转正\未补全资料的人数
        CustomersDto customersCountDto = new CustomersDto();
        customersCountDto.setStartTime(startDate);
        customersCountDto.setEndTime(endDate);
        customersCountDto.setStartTimeStr(com.xtr.comm.util.DateUtil.date2String(startDate, com.xtr.comm.util.DateUtil.dateTimeString));
        customersCountDto.setEndTimeStr(com.xtr.comm.util.DateUtil.date2String(endDate, com.xtr.comm.util.DateUtil.dateTimeString));
        customersCountDto.setCustomerCompanyId(companyId);
        customersCountDto.setSelState(CustomerConstants.CUSTOMER_PERSONNUM_ALL);
//        List<CustomersDto> totalList = selectCustomersByCondition(customersCountDto);
        //总人数
        int totalNumber = customersReaderMapper.selectByCompanyIdNumber(companyId, startDate, endDate);
        customersCountDto.setSelState(CustomerConstants.CUSTOMER_PERSONNUM_ENTER);
        //入职人数
        int enterNumber = customersReaderMapper.selectEnterNumberForSalary(customersCountDto);
        customersCountDto.setSelState(CustomerConstants.CUSTOMER_PERSONNUM_LEAVE);
        //离职人数
        int leaveNumber = customersReaderMapper.selectLeaveNumberForSalary(customersCountDto);
        customersCountDto.setSelState(CustomerConstants.CUSTOMER_PERSONNUM_NO_SARALY);
        //获取未定薪人数(取当前时间还未定薪的员工,删除和离职的不算)
//        int noSaralyNumber = customersReaderMapper.selectCustomerNumber(customersCountDto);
        int noSaralyNumber = customersReaderMapper.selectNoSaralyCountByCompanyId(companyId, startDate, endDate);
        //获取调薪人数(取时间段内的调薪员工,删除和离职的也算上去)
        List<CustomersDto> saralyList = customersRecordService.selectRecordCountBySaraly(customersCountDto);
        //获取列表结果
        resultMap.put("totalCount", totalNumber);
        resultMap.put("enterCount", enterNumber);
        resultMap.put("leaveCount", leaveNumber);
        resultMap.put("noSaralyCount", noSaralyNumber);
        resultMap.put("saralyCount", saralyList != null && saralyList.size() > 0 ? saralyList.size() : 0);
        LOGGER.info("企业端用户分页查询,返回结果:" + JSON.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 获取未定薪员工人数
     *
     * @param customersDto
     * @return
     */
    public List<CustomersDto> selectComstomerCountByNoSaraly(CustomersDto customersDto) {
        return customersReaderMapper.selectComstomerCountByNoSaraly(customersDto);
    }

    /**
     * 查询当期企业员工数量(首页使用)
     *
     * @param customersCountDto
     * @return
     */
    @Override
    public long selectCustomersCountByCondition(CustomersDto customersCountDto) {
        return customersReaderMapper.selectCustomersCount(customersCountDto);
    }

    /**
     * 根据customerId更新
     *
     * @param customersBean
     * @return
     */
    @Override
    public int updateCustomersByCustomerId(CustomersBean customersBean) {
        return customersWriterMapper.updateByCustomerId(customersBean);
    }

    /**
     * 查询所有未生成账户的员工
     *
     * @return
     */
    public List<CustomersBean> selectCustoemrSubAccount() {
        return customersReaderMapper.selectCustoemrSubAccount();
    }

    /**
     * 根据条件查询员工数量
     *
     * @param customersDto
     * @return
     */
    public int selectCustomerNumber(CustomersDto customersDto) {
        return customersReaderMapper.selectCustomerNumber(customersDto);
    }

    @Override
    public int selectLiveCountByCompanyId(Long memberCompanyId, long cycleId, Date startDate) {
        return customersReaderMapper.selectLiveCountByCompanyId(memberCompanyId, cycleId, startDate);
    }


    /**
     * 根据微信号openId查询员工绑定信息
     *
     * @param openId
     * @return
     */
    @Override
    public CustomerWechatBean selectCustomerWechatByOpenId(String openId) {
        return customerWechatReaderMapper.selectCustomerWechatByOpenId(openId);
    }

    /**
     * 查询最新入职的员工
     *
     * @param phone
     * @return
     */
    @Override
    public  List<CustomersDto> selectLastCustomerByPhone(String phone) {
        return customersReaderMapper.selectLastCustomerByPhone(phone);
    }

    /**
     * 更新员工头像
     *
     * @param map
     * @return
     */
    @Override
    public ResultResponse updateCustomerImgByCustomerId(Map map) throws BusinessException {
        if (StringUtils.isEmpty(map)) {
            throw new BusinessException("参数错误");
        }
        ResultResponse resultResponse = new ResultResponse();
        long result = customersWriterMapper.updateCustomerImgByCustomerId(map);
        if (result == 1) {
            resultResponse.setSuccess(true);
            resultResponse.setMessage("操作成功");
        } else {
            resultResponse.setMessage("操作失败");
            LOGGER.info("更新员工头像操作失败");
        }
        return resultResponse;
    }

    /**
     * 查询是否符合进入入职规范页面
     *
     * @return
     */
    public int selectCountForIsRedirect(long customerId) {
        return customersReaderMapper.selectCountForIsRedirect(customerId);
    }

    /**
     * 更改是否跳转到入职规范状态
     *
     * @param customersBean
     * @return
     */
    public int updateIsRedirectState(CustomersBean customersBean) {
        return customersWriterMapper.updateIsRedirectState(customersBean);
    }

    /**
     * 更改是否补全资料状态
     *
     * @param customersBean
     * @return
     */
    public int updateIsComplementState(CustomersBean customersBean) {
        return customersWriterMapper.updateIsComplementState(customersBean);
    }

    /**
     * 根据手机号查询未绑定的员工
     *
     * @param phone
     * @return
     */
    @Override
    public List<CustomersDto> selectUnbindCustomers(String phone) {
        return customersReaderMapper.selectUnbindCustomers(phone);
    }

    /**
     * 循环查询员工信息
     * @param list
     * @return
     */
    public List<CustomersBean> selectCustomersForeach(List<Long> list){
        return customersReaderMapper.selectCustomersForeach(list);
    }

    @Override
    public int updateShebaoStatusByIdcard(String idCard, Integer approveState, String comment) {
        return customersWriterMapper.updateShebaoStatusByIdcard(idCard, approveState, comment);
    }

    @Override
    public int selectShebaoFailedCount(Long memberCompanyId) {
        return customersReaderMapper.selectShebaoFailedCount(memberCompanyId);
    }

    /**
     * 查询该公司该年度每个月的在职员工数量
     * @param companyId
     * @param year
     * @return
     */
    @Override
    public List<CustomersDto> selectEveryMonthCustomerCounts(Long companyId, int year) {
        return customersReaderMapper.selectEveryMonthCustomerCounts(companyId,year);
    }

    /**
     * 查询员工列表
     * @param type
     * @param customersDto
     * @param customerCompanyId
     * @return
     * @throws BusinessException
     * @throws Exception
     */
    public ResultResponse queryListByCondition(Integer type, CustomersDto customersDto, Long customerCompanyId)throws BusinessException,Exception{
        if(customerCompanyId==null || customersDto==null || type==null ||
                (CustomerConstants.CUSTOMER_OPERATIONTYPE_ALL!=type.intValue() && type.intValue()==CustomerConstants.CUSTOMER_OPERATIONTYPE_REGULAR
                &&type.intValue()==CustomerConstants.CUSTOMER_OPERATIONTYPE_TRY && type.intValue()==CustomerConstants.CUSTOMER_OPERATIONTYPE_WILLLEAVE
                &&type.intValue()==CustomerConstants.CUSTOMER_OPERATIONTYPE_LEAVE)){
            throw new BusinessException("获取员工信息失败,请重试或联系管理员");
        }
        ResultResponse resultResponse=new ResultResponse();
        customersDto.setCustomerCompanyId(customerCompanyId);
        customersDto.setShowIndexType(type);
        PageBounds pageBounds = new PageBounds(customersDto.getPageIndex(), customersDto.getPageSize());
        List<CustomersDto> customerDtos=null;

        long startDateQueryAll = System.currentTimeMillis();
        if(type.intValue()==CustomerConstants.CUSTOMER_OPERATIONTYPE_WILLLEAVE){//待离职
            customerDtos=customersReaderMapper.selectWillLeaveByNewCondtion(customersDto,pageBounds);
        }else{
            customerDtos=customersReaderMapper.selectCustomersByNewCondition(customersDto,pageBounds);
        }
        LOGGER.info("获取数据列表执行时间：" + (System.currentTimeMillis()-startDateQueryAll));

        long startDateAssemAllowance = System.currentTimeMillis();
        //根据企业ID获取津贴信息
        List<AllowanceSettingDto> settingDtoList=allowanceSettingReaderMapper.selectByCompanyId(customerCompanyId);
        if(settingDtoList!=null && settingDtoList.size()>0 && customerDtos!=null && customerDtos.size()>0){
            for(CustomersDto dto:customerDtos){
                for(AllowanceSettingDto settingDto:settingDtoList){
                    if(settingDto.getType()!=null){
                        if(settingDto.getType().intValue()==1){//全体员工
                            if(StringUtils.isStrNull(dto.getAllowanceWay())){
                                dto.setAllowanceWay(settingDto.getAllowanceName());
                            }else{
                                dto.setAllowanceWay(dto.getAllowanceWay()+","+settingDto.getAllowanceName());
                            }
                        }else if(settingDto.getType().intValue()==2){//部门
                            if(!StringUtils.isStrNull(settingDto.getUserData())){
                                String[] userDateArray=settingDto.getUserData().split(",");
                                if(userDateArray!=null && userDateArray.length>0){
                                    for(String userDateStr:userDateArray){
                                        if(dto.getStationDeptId()!=null && dto.getStationDeptId().toString().equals(userDateStr)){
                                            if(StringUtils.isStrNull(dto.getAllowanceWay())){
                                                dto.setAllowanceWay(settingDto.getAllowanceName());
                                            }else{
                                                dto.setAllowanceWay(dto.getAllowanceWay()+","+settingDto.getAllowanceName());
                                            }
                                        }
                                    }
                                }
                            }
                        }else if(settingDto.getType().intValue()==3){//员工
                            if(!StringUtils.isStrNull(settingDto.getUserData())){
                                String[] userDateArray=settingDto.getUserData().split(",");
                                if(userDateArray!=null && userDateArray.length>0){
                                    for(String userDateStr:userDateArray){
                                        if(dto.getCustomerId()!=null && dto.getCustomerId().toString().equals(userDateStr)){
                                            if(StringUtils.isStrNull(dto.getAllowanceWay())){
                                                dto.setAllowanceWay(settingDto.getAllowanceName());
                                            }else{
                                                dto.setAllowanceWay(dto.getAllowanceWay()+","+settingDto.getAllowanceName());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        LOGGER.info("拼装津贴信息执行时间：" + (System.currentTimeMillis()-startDateAssemAllowance));

        long startDateAssemBaseAmount = System.currentTimeMillis();
        List<Long> customerIdList=new ArrayList<>();
        if(customerDtos!=null && customerDtos.size()>0){
            for(CustomersDto dto:customerDtos){
                customerIdList.add(dto.getCustomerId());
            }
        }
        if(customerIdList!=null && customerIdList.size()>0){
            //批量获取员工基本工资基数
            List<CustomersDto> salaryList=customersReaderMapper.selectSalaryBaseBatch(customerIdList);
            //批量获取员工最新工资基数
            Date currentDate=DateUtil.formateDateToYYYYMMDD(new Date());
            List<CustomerSalaryRecordBean> salaryRecordList=customerSalaryRecordReaderMapper.selectNewSalaryBatch(customerIdList,currentDate );
            if(customerDtos!=null && customerDtos.size()>0){
                for(CustomersDto dto:customerDtos){
                    //拼装基本工资
                    if(salaryRecordList!=null && salaryRecordList.size()>0){
                        for(CustomerSalaryRecordBean salaryRecordBean:salaryRecordList){
                            if(salaryRecordBean.getCustomerId()!=null && dto.getCustomerId()!=null && salaryRecordBean.getCustomerId().longValue()==dto.getCustomerId().longValue()){
                                dto.setCurrentSalary(salaryRecordBean.getNewSalary());//取最新基本工资
                                break;
                            }
                        }
                    }
                    if(dto.getCurrentSalary()==null && salaryList!=null && salaryList.size()>0){
                        for(CustomersDto salary:salaryList){
                            if(salary.getCustomerId()!=null && dto.getCustomerId()!=null && salary.getCustomerId().longValue()==dto.getCustomerId().longValue()){
                                if (salary.getStationRegularTime() != null) {
                                    Date regularDate = DateUtil.formateDateToYYYYMMDD(salary.getStationRegularTime());
                                    if (regularDate.getTime() <= currentDate.getTime()) {//员工已转正
                                        dto.setCurrentSalary(salary.getCustomerRegularSalary());//取转正工资
                                        break;
                                    }else{
                                        dto.setCurrentSalary(salary.getCustomerProbationSalary());//取试用期工资
                                        break;
                                    }
                                }else{
                                    dto.setCurrentSalary(salary.getCustomerProbationSalary());//取试用期工资
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        LOGGER.info("拼装基本工资执行时间：" + (System.currentTimeMillis()-startDateAssemBaseAmount));
        Paginator paginator = ((PageList) customerDtos).getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setData(customerDtos);
        resultResponse.setSuccess(true);

        return resultResponse;
    }

    /**
     * 根据条件查询员工数量
     *
     * @param customersDto
     * @return
     */
    public int queryCustomerCount(CustomersDto customersDto,int type) {
        if(type==CustomerConstants.CUSTOMER_OPERATIONTYPE_WILLLEAVE){//待离职
            return customersReaderMapper.selectWillLeaveCountByNewCondtion(customersDto);
        }else{
            return customersReaderMapper.selectCountByNewCondition(customersDto);
        }
    }

    /**
     * 新增员工业务
     * @param customersDto
     * @param type
     * @throws BusinessException
     * @throws Exception
     */
    @Transactional
    public Long createCustomer(CustomersDto customersDto,int type)throws BusinessException,Exception{
        //基础验证
        createCustomerNewValidator( customersDto, type);
        //新增员工
        return createCustomerDetail( customersDto, type);
    }

    /**
     * 新增员工基本验证
     * @param customersDto
     * @param type
     */
    private void createCustomerNewValidator(CustomersDto customersDto,int type){

        //非空验证
        if(type!=CustomerConstants.CUSTOMER_ADDCUSTOMER_ENTER && type!=CustomerConstants.CUSTOMER_ADDCUSTOMER_REGULAR){
            throw new BusinessException("新增员工失败,请重试或联系管理员");
        }
        if (null == customersDto.getCustomerCompanyId()) {
            throw new BusinessException("新增员工失败,请重试或联系管理员");
        }
        if (StringUtils.isStrNull(customersDto.getCustomerTurename())) {
            throw new BusinessException("请输入正确的姓名");
        }
        if (StringUtils.isStrNull(customersDto.getCustomerPhone())) {
            throw new BusinessException("请输入正确的手机号");
        }
        if (StringUtils.isStrNull(customersDto.getCustomerDepName())) {
            throw new BusinessException("请选择部门");
        }
        if (StringUtils.isStrNull(customersDto.getStationStationName())) {
            throw new BusinessException("请输入岗位");
        }
        if (customersDto.getStationEnterTime()==null) {
            throw new BusinessException("请选择员工入职时间");
        }
        if (customersDto.getCustomerRegularSalary()==null) {
            throw new BusinessException("请输入转正后基本工资金额");
        }
        if(type==CustomerConstants.CUSTOMER_ADDCUSTOMER_ENTER){//入职
            if (customersDto.getStationRegularTime()==null) {
                throw new BusinessException("请选择员工转正时间");
            }
            if (customersDto.getCustomerProbationSalary()==null) {
                throw new BusinessException("请输入试用期基本工资金额");
            }
            int stationEnterTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(customersDto.getStationEnterTime()));
            int regularEnterTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(customersDto.getStationRegularTime()));
            if(regularEnterTimeInt<stationEnterTimeInt){
                throw new BusinessException("转正日期不可小于入职日期");
            }
            if(stationEnterTimeInt==regularEnterTimeInt && customersDto.getCustomerProbationSalary().compareTo(customersDto.getCustomerRegularSalary())!=0){
                throw new BusinessException("试用期工资应与转正后工资相同");
            }
            if(customersDto.getCustomerProbationSalary().compareTo(CustomerConstants.INIT_BIGDECIMAL_COMPARE)<0){
                throw new BusinessException("基本工资不可为负数");
            }
        }
        //其它验证
        if(customersDto.getCustomerTurename().length()<1 || customersDto.getCustomerTurename().length()>20){
            throw new BusinessException("请输入正确的姓名");
        }
        String regexPhone = "^[0-9]{11}$";
        if(!Pattern.matches(regexPhone, customersDto.getCustomerPhone())){
            throw new BusinessException("请输入正确的手机号");
        }
        int currentTime=Integer.parseInt(com.xtr.comm.util.DateUtil.dateFormatterNoLine.format(new Date()));
        int joinTime=Integer.parseInt(com.xtr.comm.util.DateUtil.dateFormatterNoLine.format(customersDto.getStationEnterTime()));
        if(joinTime>currentTime){
            throw new BusinessException("入职时间不能大于当前时间");
        }
        if(customersDto.getCustomerRegularSalary().compareTo(CustomerConstants.INIT_BIGDECIMAL_COMPARE)<0){
            throw new BusinessException("基本工资不可为负数");
        }
    }

    /**
     * 新增员工信息
     * @param customersDto
     * @param type
     * @throws BusinessException
     * @throws Exception
     */
    private Long createCustomerDetail(CustomersDto customersDto,int type)throws BusinessException,Exception{
        Long customerId=0L;
        //判断当前手机号是否存在
        CustomersBean customer = new CustomersBean();
        customer.setCustomerPhone(customersDto.getCustomerPhone());//手机号
        customer.setCustomerCompanyId(customersDto.getCustomerCompanyId());//企业id
        List<CustomersBean> customersBeanList = customersReaderMapper.selectComstomerByPhoneAndComId(customer);
        if (null == customersBeanList || customersBeanList.size() <= 0) {//手机号不存在,则新增用户
            //新增员工基本信息
            CustomersBean customerBean=createCustomersBaseNew( customersDto,1,null);
            customerId=customerBean.getCustomerId();
            //新增一条岗位信息
            createCustomersStationNew( customerBean,  customersDto, type);
            //新增个人信息
            createCustomersPersonalNew( customerBean);
            //新增员工记录
            createCustomersRecordNew( customerBean, type,customersDto.getStationRegularTime());
            //创建账号,如果已经存在账号,则不创建
            SubAccountBean propSubAccountBean=subAccountReaderMapper.selectByCustId(customerId,1);
            if(propSubAccountBean==null){
                this.addSubAccount(customerBean);
            }
        } else {//手机号存在,验证该员工是否离职,如果离职的话,则更新员工信息
            CustomersBean cu = customersBeanList.get(0);
            //判断员工岗位表是否存在
            CustomersStationBean hasCustomersStationBean = customersStationReaderMapper.selectByCustomerId(cu.getCustomerId());
            if (null != hasCustomersStationBean) {
                if (hasCustomersStationBean.getStationCustomerState()!=null &&
                        (hasCustomersStationBean.getStationCustomerState().intValue() == CustomerConstants.CUSTOMER_PERSONSTATE_ENTER || hasCustomersStationBean.getStationCustomerState().intValue() == CustomerConstants.CUSTOMER_PERSONSTATE_REGULAR)) {
                    throw new BusinessException("此手机号已被绑定");
                }
            }
            //更新员工信息
            CustomersBean customerBean=createCustomersBaseNew( customersDto,2,cu.getCustomerId());
            customerId=customerBean.getCustomerId();
            //更新岗位信息
            updateCustomersStationBeanNew( hasCustomersStationBean,  customersDto,  cu, type);
            //判断员个人信息是否存在
            CustomersPersonalBean hasCustomersPersonalBean = customersPersonalReaderMapper.selectByCustomerId(cu.getCustomerId());
            //更新员工个人信息
            operateCustomersPersonalBean(hasCustomersPersonalBean, customersDto, cu);
            //新增员工记录
            createCustomersRecordNew( cu, type,customersDto.getStationRegularTime());
//            插入一条员工记录信息  当前为新增操作
//            operateCustomersRecordBean(customersDto, cu);
            //创建账号,如果已经存在账号,则不创建
            SubAccountBean propSubAccountBean=subAccountReaderMapper.selectByCustId(customerId,1);
            if(propSubAccountBean==null){
                this.addSubAccount(customerBean);
            }
        }
        return customerId;
    }

    /**
     * 新增或更新员工基本信息
     * @param customersDto
     * @param operationType 1新增 2更新
     * @return
     */
    private CustomersBean createCustomersBaseNew(CustomersDto customersDto,int operationType,Long customerId){
        CustomersBean customersBean = new CustomersBean();
        customersBean.setCustomerCompanyId(customersDto.getCustomerCompanyId());//企业id
        customersBean.setCustomerDepId(customersDto.getCustomerDepId());//员工部门id
        customersBean.setCustomerTurename(customersDto.getCustomerTurename());//员工姓名
        customersBean.setCustomerJoinTime(customersDto.getStationEnterTime());//员工入职时间
        customersBean.setCustomerDepName(customersDto.getCustomerDepName());//员工部门名称
        customersBean.setCustomerPlace(customersDto.getStationStationName());//职位
        customersBean.setCustomerPhone(customersDto.getCustomerPhone());//员工手机号
        customersBean.setCustomerIsExpense(0);//未定额
        customersBean.setCustomerSocialApproveState(CustomerConstants.CUSTOMER_SOCIAL_APPROVESTATE_NO);//社保通资料审核状态
        customersBean.setCustomerCurrentExpense(BigDecimal.ZERO);//当前报销总额
        customersBean.setCustomerProbationSalary(customersDto.getCustomerProbationSalary());
        customersBean.setCustomerRegularSalary(customersDto.getCustomerRegularSalary());
        if(operationType==1){
            Date newDate = new Date();
            customersBean.setCustomerSign(1);//可用
            customersBean.setCustomerAddtime(newDate);//添加时间
            customersBean.setCustomerRegularTime(newDate);//定薪时间
            if(customersDto.getCustomerProbationSalary()==null){
                customersDto.setCustomerProbationSalary(customersDto.getCustomerRegularSalary());
            }
            int count = customersWriterMapper.insert(customersBean);//返回插入数据的主键
            if (count<=0) {
                throw new BusinessException("新增员工基本信息失败");
            }
        }else if(operationType==2){
            customersBean.setCustomerId(customerId);//id
            customersBean.setCustomerRegularTime(new Date());//定薪时间
            int count = customersWriterMapper.updateByPrimaryKeySelective(customersBean);
            if (count<=0) {
                throw new BusinessException("更新员工基本信息失败");
            }
        }
        return customersBean;
    }

    /**
     * 新增员工岗位信息
     * @param customersBean
     * @param customersDto
     * @param type
     */
    private void createCustomersStationNew(CustomersBean customersBean, CustomersDto customersDto,int type) {
        CustomersStationBean customersStationBean = new CustomersStationBean();
        customersStationBean.setStationCustomerId(customersBean.getCustomerId());//员工id
        customersStationBean.setStationEnterTime(customersDto.getStationEnterTime());//入职时间
        customersStationBean.setStationEmployMethod(CustomerConstants.CUSTOMER_EMPLOYMETHOD_OFFICAL);//默认都是正式工
        if(type==CustomerConstants.CUSTOMER_ADDCUSTOMER_ENTER){//入职
            customersStationBean.setStationRegularTime(customersDto.getStationRegularTime());//转正时间
            int stationCurrentTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(new Date()));
            int regularEnterTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(customersDto.getStationRegularTime()));
            if(stationCurrentTimeInt>=regularEnterTimeInt){//转正时间等于当天时间
                customersStationBean.setStationCustomerState(2);//转正
            }else{
                customersStationBean.setStationCustomerState(1);//入职
            }
        }else if(type==CustomerConstants.CUSTOMER_ADDCUSTOMER_REGULAR){
            customersStationBean.setStationCustomerState(2);//已转正
        }
        customersStationBean.setStationDeptId(customersDto.getCustomerDepId());//部门id
        customersStationBean.setStationStationName(customersDto.getStationStationName());//岗位名称
        customersStationBean.setStationCreateTime(new Date());//创建时间
        int count = customersStationWriterMapper.insert(customersStationBean);
        if (count<= 0) {
            throw new BusinessException("新增员工岗位信息失败");
        }
    }

    /**
     * 新增员工个人信息
     * @param customersBean
     */
    private void createCustomersPersonalNew(CustomersBean customersBean) {
        CustomersPersonalBean cus = new CustomersPersonalBean();
        cus.setPersonalCreateTime(new Date());
        cus.setPersonalCustomerId(customersBean.getCustomerId());
        int count = customersPersonalWriterMapper.insert(cus);
        if (count<=0) {
            throw new BusinessException("新增员工个人信息失败");
        }
    }

    /**
     * 新增员工记录
     * @param customersBean
     * @param type
     */
    private void createCustomersRecordNew(CustomersBean customersBean,int type,Date regularTime) {
        CustomersRecordBean customersRecordBean = null;
        if (type==CustomerConstants.CUSTOMER_ADDCUSTOMER_ENTER) {//入职

            int stationCurrentTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(new Date()));
            int regularEnterTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(regularTime));
            if(stationCurrentTimeInt>=regularEnterTimeInt){//转正时间等于当天时间
                //插入2条记录  一条入职  一条转正
                customersRecordBean = new CustomersRecordBean();
                customersRecordBean.setRecordCustomerId(customersBean.getCustomerId());//员工id
                customersRecordBean.setRecordOperationType(CustomerConstants.CUSTOMER_RECORDTYPE_ENTER);//转正
                customersRecordBean.setRecordOperationTime(new Date());//操作时间
                customersRecordBean.setRecordCreateTime(new Date());//创建时间
                int countEnter = customersRecordWriterMapper.insert(customersRecordBean);
                if (countEnter<=0) {
                    throw new BusinessException("新增员工记录失败");
                }

                //再一条转正记录
                customersRecordBean = new CustomersRecordBean();
                customersRecordBean.setRecordCustomerId(customersBean.getCustomerId());//员工id
                customersRecordBean.setRecordOperationType(CustomerConstants.CUSTOMER_RECORDTYPE_REGULAR);//转正
                customersRecordBean.setRecordOperationTime(new Date());//操作时间
                customersRecordBean.setRecordCreateTime(new Date());//创建时间
                int countRegular = customersRecordWriterMapper.insert(customersRecordBean);
                if (countRegular<=0) {
                    throw new BusinessException("新增员工记录失败");
                }
            }else{
                customersRecordBean = new CustomersRecordBean();
                customersRecordBean.setRecordCustomerId(customersBean.getCustomerId());//员工id
                customersRecordBean.setRecordOperationType(CustomerConstants.CUSTOMER_RECORDTYPE_ENTER);//入职
                customersRecordBean.setRecordOperationTime(new Date());//操作时间
                customersRecordBean.setRecordCreateTime(new Date());//创建时间
                int count = customersRecordWriterMapper.insert(customersRecordBean);
                if (count<=0) {
                    throw new BusinessException("新增员工记录失败");
                }
            }
        } else {//转正
            //插入2条记录  一条入职  一条转正
            customersRecordBean = new CustomersRecordBean();
            customersRecordBean.setRecordCustomerId(customersBean.getCustomerId());//员工id
            customersRecordBean.setRecordOperationType(CustomerConstants.CUSTOMER_RECORDTYPE_ENTER);//转正
            customersRecordBean.setRecordOperationTime(new Date());//操作时间
            customersRecordBean.setRecordCreateTime(new Date());//创建时间
            int countEnter = customersRecordWriterMapper.insert(customersRecordBean);
            if (countEnter<=0) {
                throw new BusinessException("新增员工记录失败");
            }

            //再一条转正记录
            customersRecordBean = new CustomersRecordBean();
            customersRecordBean.setRecordCustomerId(customersBean.getCustomerId());//员工id
            customersRecordBean.setRecordOperationType(CustomerConstants.CUSTOMER_RECORDTYPE_REGULAR);//转正
            customersRecordBean.setRecordOperationTime(new Date());//操作时间
            customersRecordBean.setRecordCreateTime(new Date());//创建时间
            int countRegular = customersRecordWriterMapper.insert(customersRecordBean);
            if (countRegular<=0) {
                throw new BusinessException("新增员工记录失败");
            }
        }
    }

    /**
     * 更新岗位信息
     * @param hasCustomersStationBean
     * @param customersDto
     * @param cu
     * @param type
     */
    private void updateCustomersStationBeanNew(CustomersStationBean hasCustomersStationBean, CustomersDto customersDto, CustomersBean cu,int type) {
        if (null == hasCustomersStationBean) {//新增
            CustomersBean customersBean=new CustomersBean();
            customersBean.setCustomerId(cu.getCustomerId());
            createCustomersStationNew( customersBean,  customersDto, type);
        } else {//更新
            CustomersStationBean customersStationBean = new CustomersStationBean();
            customersStationBean.setStationCustomerId(cu.getCustomerId());//员工id
            customersStationBean.setStationEnterTime(customersDto.getStationEnterTime());//入职时间
            customersStationBean.setStationEmployMethod(CustomerConstants.CUSTOMER_EMPLOYMETHOD_OFFICAL);//默认都是正式工
            if(type==CustomerConstants.CUSTOMER_ADDCUSTOMER_ENTER){//入职
                customersStationBean.setStationRegularTime(customersDto.getStationRegularTime());//转正时间
//                customersStationBean.setStationCustomerState(1);//入职
                int stationCurrentTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(new Date()));
                int regularEnterTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(customersDto.getStationRegularTime()));
                if(stationCurrentTimeInt>=regularEnterTimeInt){//转正时间等于当天时间
                    customersStationBean.setStationCustomerState(2);//转正
                }else{
                    customersStationBean.setStationCustomerState(1);//入职
                }
            }else if(type==CustomerConstants.CUSTOMER_ADDCUSTOMER_REGULAR){
                customersStationBean.setStationCustomerState(2);//已转正
            }
            customersStationBean.setStationDeptId(customersDto.getCustomerDepId());//部门id
            customersStationBean.setStationStationName(customersDto.getStationStationName());//岗位名称
            customersStationBean.setStationUpdateTime(new Date());//创建时间
            customersStationBean.setStationDimissingTime(null);//离职日期 清空
            int count = customersStationWriterMapper.updateByCustomerId(customersStationBean);
            if (count<=0) {
                throw new BusinessException("更新员工岗位信息失败");
            }
        }

    }

    /**
     *获取调薪初始化数据
     * @param customerId
     * @return
     * @throws BusinessException
     * @throws Exception
     */
    public CustomerResponse updateSalaryInit(Long customerId)throws BusinessException,Exception{
        //基础验证
        if(customerId==null){
           throw new BusinessException("员工信息异常");
        }
        CustomerResponse customerResponse=new CustomerResponse();

        //获取员工信息
        CustomersBean customersBean = selectByPrimaryKey(customerId);
        //获取员工岗位信息
        CustomersStationBean station = customersStationService.selectCutomerStationByCustomerId(customerId);
        if(station.getStationCustomerState() ==null || station.getStationCustomerState().intValue() > 2){
            throw new BusinessException("员工信息异常");
        }
//        //获取当前基本工资
//        PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(customersBean.getCustomerCompanyId());
//        if(payCycleBean==null || payCycleBean.getEndDate()==null){
//            throw new BusinessException("无计薪周期");
//        }
        Date lastDate=  new Date();
        BigDecimal currentSalary=customerUpdateSalaryService.getLastSalary(customerId, lastDate);
        //获取之前调薪的基本工资与生效日期
        Date nowDate= DateUtil.dateFormatter.parse(DateUtil.dateFormatter.format(new Date()));
        CustomerSalaryRecordBean nextRecord = customerSalaryRecordReaderMapper.selectFirstRecordByDate(customerId, nowDate);
        if (nextRecord != null) {//有调薪记录
            BigDecimal futureSalary=nextRecord.getNewSalary();
            String futureDate=DateUtil.dateFormatter.format(nextRecord.getEffectiveDate());
            customerResponse.setFutureSalary(futureSalary);
            customerResponse.setFutureDate(futureDate);
        }
        customerResponse.setCustomersBean(customersBean);
        customerResponse.setCustomerStation(station);
//        customerResponse.setPayrollId(payCycleBean.getId());
        customerResponse.setCurrentSalary(currentSalary);
        return customerResponse;
    }

    /**
     * 更新用户基本信息
     *
     * @param customersBean
     * @return
     * @throws BusinessException
     */
    public ResultResponse modifyCustomerBaseNew(CustomersBean customersBean, Long companyId) {
        ResultResponse resultResponse = new ResultResponse();
        //验证
        if (StringUtils.isStrNull(customersBean.getCustomerTurename())) {
            resultResponse.setMessage("请输入员工姓名");
            return resultResponse;
        }
        if (StringUtils.isStrNull(customersBean.getCustomerPhone())) {
            resultResponse.setMessage("请输入手机号");
            return resultResponse;
        }
        String regexPhone = "^[0-9]{11}$";
        if(!Pattern.matches(regexPhone, customersBean.getCustomerPhone())){
            throw new BusinessException("请填写正确的手机号");
        }

        //验证手机号是否是唯一的
        if (companyId != null) {
            List<String> phoneStrList = new ArrayList<>();
            phoneStrList.add(customersBean.getCustomerPhone());
            Map<Integer, List<CustomersDto>> phoneMap = selectPhoneForeach(phoneStrList, companyId, CustomerConstants.CUSTOMER_WILLOPERATION_EDIT, customersBean.getCustomerId());
            if (phoneMap != null && phoneMap.size() > 0 && phoneMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY) != null && phoneMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY).size() > 0) {
                resultResponse.setMessage("此手机号已被绑定");
                return resultResponse;
            }
        }
        //如果姓名、性别、手机号、身份证号和照片有变更，则社保通状态更新为未审核
        CustomersBean checkBean = selectByPrimaryKey(customersBean.getCustomerId());
        if (checkBean != null) {
            if (!customersBean.getCustomerTurename().equals(checkBean.getCustomerTurename())
                    || (customersBean.getCustomerSex()!=null && customersBean.getCustomerSex().intValue() != (checkBean.getCustomerSex() != null ? checkBean.getCustomerSex().intValue() : 0))
                    || !customersBean.getCustomerPhone().equals(checkBean.getCustomerPhone())) {
                customersBean.setCustomerSocialApproveState(CustomerConstants.CUSTOMER_SOCIAL_APPROVESTATE_NO);
            }
        } else {
            resultResponse.setMessage("获取不到原用户基本信息");
            return resultResponse;
        }
        //生日如果为空,则更新为空
        if(StringUtils.isStrNull(customersBean.getCustomerBirthdayMonth())){
            customersBean.setCustomerBirthdayMonth("");
        }

        int count = updateByPrimaryKeySelective(customersBean);
        if (count <= 0) {
            LOGGER.info("更新用户基本信息失败");
            resultResponse.setMessage("更新用户基本信息失败");
        } else {
            resultResponse.setSuccess(true);
        }
        return resultResponse;
    }

    /**
     * 更新用户身份证信息
     *
     * @param customersBean
     * @return
     * @throws BusinessException
     */
    public ResultResponse modifyCustomerIdcardNew(CustomersBean customersBean, Long companyId) {
        ResultResponse resultResponse = new ResultResponse();
        //验证
        if (StringUtils.isStrNull(customersBean.getCustomerIdcard())) {
            resultResponse.setMessage("请输入身份证号");
            return resultResponse;
        }
        if (StringUtils.isStrNull(customersBean.getCustomerIdcardImgFront())) {
            resultResponse.setMessage("请添加身份证照");
            return resultResponse;
        }
        if (StringUtils.isStrNull(customersBean.getCustomerIdcardImgBack())) {
            resultResponse.setMessage("请添加身份证照");
            return resultResponse;
        }
        if (StringUtils.isStrNull(customersBean.getCustomerBanknumber())) {
            resultResponse.setMessage("请输入工资卡号");
            return resultResponse;
        }
        if (StringUtils.isStrNull(customersBean.getCustomerBank())) {
            resultResponse.setMessage("请输入工资卡开户行");
            return resultResponse;
        }

        String regexIdCard = "^[0-9]{15}$|^[0-9]{18}$|^[a-zA-Z\\d]{18}$|^[a-zA-Z\\d]{15}$";
        if(!Pattern.matches(regexIdCard, customersBean.getCustomerIdcard())){
            throw new BusinessException("请输入正确的身份证号码");
        }
        String regexNumber = "^[0-9]{13,23}$";
        if(!Pattern.matches(regexNumber, customersBean.getCustomerBanknumber())){
            throw new BusinessException("请输入正确的工资卡号");
        }

        //判断银行输入的是否正确
        BankCodeBean bankCodeBean= bankCodeService.selectByBankName(customersBean.getCustomerBank());
        if(bankCodeBean==null){
            resultResponse.setMessage("请填写正确的工资卡开户行");
            return resultResponse;

        }

        //验证身份证号是否是唯一的
        if (companyId != null) {
            List<String> cardStrList = new ArrayList<>();
            cardStrList.add(customersBean.getCustomerIdcard());
            Map<Integer, List<CustomersDto>> cardMap = selectidCardForeach(cardStrList, companyId, CustomerConstants.CUSTOMER_WILLOPERATION_EDIT, customersBean.getCustomerId());
            if (cardMap != null && cardMap.size() > 0 && cardMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY) != null && cardMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY).size() > 0) {
                resultResponse.setMessage("此身份证号已经存在");
                return resultResponse;
            }
        }

        //如果姓名、性别、手机号、身份证号和照片有变更，则社保通状态更新为未审核
        CustomersBean checkBean = selectByPrimaryKey(customersBean.getCustomerId());
        if (checkBean != null) {
            if (!customersBean.getCustomerIdcard().equals(checkBean.getCustomerIdcard())
                    || !customersBean.getCustomerIdcardImgFront().equals(checkBean.getCustomerIdcardImgFront())
                    || !customersBean.getCustomerIdcardImgBack().equals(checkBean.getCustomerIdcardImgBack())) {
                customersBean.setCustomerSocialApproveState(CustomerConstants.CUSTOMER_SOCIAL_APPROVESTATE_NO);
            }
        } else {
            resultResponse.setMessage("获取不到原用户基本信息");
            return resultResponse;
        }

        int count = updateByPrimaryKeySelective(customersBean);
        if (count <= 0) {
            LOGGER.info("更新用户身份证信息失败");
            resultResponse.setMessage("更新用户身份证信息失败");
        } else {
            resultResponse.setSuccess(true);
        }
        return resultResponse;



    }

    /**
     * 查询未补全员工资料信息列表
     * @param customersDto
     * @param customerCompanyId
     * @return
     * @throws BusinessException
     * @throws Exception
     */
    public ResultResponse queryInCompleteCustomerList( CustomersDto customersDto, Long customerCompanyId)throws BusinessException,Exception{

        if(customerCompanyId==null || customersDto==null){
            throw new BusinessException("查询未补全员工资料信息列表失败,请重试或联系管理员");
        }
        ResultResponse resultResponse=new ResultResponse();
        customersDto.setCustomerCompanyId(customerCompanyId);
        PageBounds pageBounds = new PageBounds(customersDto.getPageIndex(), customersDto.getPageSize());
        List<CustomersDto> customerDtos=customersReaderMapper.selectInCompleteListByNewCondition( customersDto,pageBounds);
        Paginator paginator = ((PageList) customerDtos).getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setData(customerDtos);
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 更改未补全的信息
     * @param customersBean
     * @param companyId
     * @param type
     * @return
     */
    public ResultResponse uptInCompleteCustomer(CustomersBean customersBean,Long companyId,Integer type){

        if(companyId==null || type==null
                ||(type.intValue()!=CustomerConstants.CUSTOMER_INCOMPLETE_BANKNUMBER && type.intValue()!=CustomerConstants.CUSTOMER_INCOMPLETE_BANKNAME && type.intValue()!=CustomerConstants.CUSTOMER_INCOMPLETE_IDCARD
        &&type.intValue()!=CustomerConstants.CUSTOMER_INCOMPLETE_IMGPRE && type.intValue()!=CustomerConstants.CUSTOMER_INCOMPLETE_IMGBACK)){
            throw new BusinessException("无企业信息");
        }
        //如果姓名、性别、手机号、身份证号和照片有变更，则社保通状态更新为未审核
        CustomersBean checkBean = selectByPrimaryKey(customersBean.getCustomerId());
        if(checkBean==null){
            throw new BusinessException("无员工信息");
        }

        ResultResponse resultResponse=new ResultResponse();
        String errorMsg="";
        if(type==CustomerConstants.CUSTOMER_INCOMPLETE_BANKNUMBER){
            if (!StringUtils.isStrNull(customersBean.getCustomerBanknumber())) {
                String regexNumber = "^[0-9]{13,23}$";
                if(!Pattern.matches(regexNumber, customersBean.getCustomerBanknumber())){
                    throw new BusinessException("请输入正确的工资卡号");
                }
            }else{
                customersBean.setCustomerBanknumber("");
            }
            errorMsg="更新工资卡号失败";
        }else if(type==CustomerConstants.CUSTOMER_INCOMPLETE_BANKNAME){
            if (!StringUtils.isStrNull(customersBean.getCustomerBank())) {
                //判断银行输入的是否正确
                BankCodeBean bankCodeBean= bankCodeService.selectByBankName(customersBean.getCustomerBank());
                if(bankCodeBean==null){
                    resultResponse.setMessage("请填写正确的工资卡开户行");
                    return resultResponse;
                }
            }else{
                customersBean.setCustomerBank("");
            }
        }else if(type==CustomerConstants.CUSTOMER_INCOMPLETE_IDCARD){
            if (!StringUtils.isStrNull(customersBean.getCustomerIdcard())) {
                String regexIdCard = "^[0-9]{15}$|^[0-9]{18}$|^[a-zA-Z\\d]{18}$|^[a-zA-Z\\d]{15}$";
                if(!Pattern.matches(regexIdCard, customersBean.getCustomerIdcard())){
                    throw new BusinessException("请输入正确的身份证号码");
                }

                //验证身份证号是否是唯一的
                if (companyId != null) {
                    List<String> cardStrList = new ArrayList<>();
                    cardStrList.add(customersBean.getCustomerIdcard());
                    Map<Integer, List<CustomersDto>> cardMap = selectidCardForeach(cardStrList, companyId, CustomerConstants.CUSTOMER_WILLOPERATION_EDIT, customersBean.getCustomerId());
                    if (cardMap != null && cardMap.size() > 0 && cardMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY) != null && cardMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY).size() > 0) {
                        resultResponse.setMessage("此身份证号已经存在");
                        return resultResponse;
                    }
                }
                if(StringUtils.isStrNull(checkBean.getCustomerIdcard())){//原来数据为空
                    //社保通状态更新为未审核
                    customersBean.setCustomerSocialApproveState(CustomerConstants.CUSTOMER_SOCIAL_APPROVESTATE_NO);
                }else{
                    if (!customersBean.getCustomerIdcard().equals(checkBean.getCustomerIdcard())){//两个都不为空,但不相等
                        customersBean.setCustomerSocialApproveState(CustomerConstants.CUSTOMER_SOCIAL_APPROVESTATE_NO);
                    }
                }

            }else{
                customersBean.setCustomerIdcard("");
                if(!StringUtils.isStrNull(checkBean.getCustomerIdcard())){//原来数据为空
                    //社保通状态更新为未审核
                    customersBean.setCustomerSocialApproveState(CustomerConstants.CUSTOMER_SOCIAL_APPROVESTATE_NO);
                }
            }
        }else if(type==CustomerConstants.CUSTOMER_INCOMPLETE_IMGPRE){
            if (StringUtils.isStrNull(customersBean.getCustomerIdcardImgFront())) {
                resultResponse.setMessage("请添加身份证照");
                return resultResponse;
            }
            if(StringUtils.isStrNull(checkBean.getCustomerIdcardImgFront())){//原来数据为空
                //社保通状态更新为未审核
                customersBean.setCustomerSocialApproveState(CustomerConstants.CUSTOMER_SOCIAL_APPROVESTATE_NO);
            }else{
                if (!customersBean.getCustomerIdcardImgFront().equals(checkBean.getCustomerIdcardImgFront())){//两个都不为空,但不相等
                    customersBean.setCustomerSocialApproveState(CustomerConstants.CUSTOMER_SOCIAL_APPROVESTATE_NO);
                }
            }
        }else if(type==CustomerConstants.CUSTOMER_INCOMPLETE_IMGBACK){
            if (StringUtils.isStrNull(customersBean.getCustomerIdcardImgBack())) {
                resultResponse.setMessage("请添加身份证照");
                return resultResponse;
            }

            if(StringUtils.isStrNull(checkBean.getCustomerIdcardImgBack())){//原来数据为空
                //社保通状态更新为未审核
                customersBean.setCustomerSocialApproveState(CustomerConstants.CUSTOMER_SOCIAL_APPROVESTATE_NO);
            }else{
                if (!customersBean.getCustomerIdcardImgBack().equals(checkBean.getCustomerIdcardImgBack())){//两个都不为空,但不相等
                    customersBean.setCustomerSocialApproveState(CustomerConstants.CUSTOMER_SOCIAL_APPROVESTATE_NO);
                }
            }
        }

        int count = updateByPrimaryKeySelective(customersBean);
        if (count <= 0) {
            LOGGER.info(errorMsg);
            resultResponse.setMessage(errorMsg);
        } else {
            resultResponse.setSuccess(true);
        }
        return resultResponse;
    }

    /**
     * 发送未补全资料员工手机短信
     * @param companyId
     * @throws BusinessException
     * @throws Exception
     */
    public void sendInPlemeteMsg(Long companyId)throws BusinessException,Exception{
        if(companyId==null){
            throw new BusinessException("无企业信息");
        }
        CustomersDto customersDto=new CustomersDto();
        customersDto.setCustomerCompanyId(companyId);
        List<CustomersDto> customerDtos=customersReaderMapper.selectInCompleteListByNewCondition( customersDto);
        List<Long> customerIdList=new ArrayList<>();
        List<CustomersDto> dtoList=new ArrayList<>();
        Date nowDate=new Date();
        if(customerDtos!=null && customerDtos.size()>0){
            for(CustomersDto dto:customerDtos){
                Date sendMsgTime=dto.getCustomerSendmsgTime();
                if(sendMsgTime==null || (sendMsgTime!=null && !(DateUtil.dateFormatterNoLine.format(sendMsgTime).equals(DateUtil.dateFormatterNoLine.format(nowDate))))){
                    if(StringUtils.isStrNull(dto.getCustomerPhone())){
                        throw new BusinessException(dto.getCustomerTurename()+"员工无手机号码,不能发送短信");
                    }
                    customerIdList.add(dto.getCustomerId());
                    dtoList.add(dto);
                }
            }
        }
        if(customerIdList!=null && customerIdList.size()>0){
            int count=customersWriterMapper.updateSendMsgTimeBatch(customerIdList);
            if(count<=0 || count!=customerIdList.size()){
                throw new BusinessException("更新发送信息失败");
            }
        }
        List<CompanyProtocolsBean> protocolsList=companyProtocolsService.selectUsefulListForCompanyId(companyId);
        String detailMsgCont="";

        if(protocolsList!=null && protocolsList.size()>0){
            int protocolType=0;//0 无代发和无社保协议 1代发 2社保 3有代发和有社保
            for(CompanyProtocolsBean protocol:protocolsList){
                if(protocol.getProtocolContractType()!=null && protocol.getProtocolContractType().intValue()==CompanyProtocolConstants.PROTOCOL_ADDPROTOCOL_NO){//有代发
                    if(protocolType==2){
                        protocolType=3;
                    }else{
                        protocolType=1;//有社保协议
                    }
                    protocolType=1;//有代发协议
                }
                if(protocol.getProtocolContractType()!=null && protocol.getProtocolContractType().intValue()==CompanyProtocolConstants.PROTOCOL_TYPE_DJ){//有社保
                    if(protocolType==1){
                        protocolType=3;
                    }else{
                        protocolType=2;//有社保协议
                    }

                }
            }
            if(protocolType==1){
                detailMsgCont="公司通过服务商给您发工资。请加微信号“xintairuan0203”，点击“个人信息”补全相关资料。否则会影响您的工资发放。";
            }else if(protocolType==2){
                detailMsgCont="公司通过服务商给您缴纳社保。请加微信号“xintairuan0203”，点击“个人信息”补全相关资料。否则会影响您的社保缴纳。";
            }else if(protocolType==3){
                detailMsgCont="公司通过服务商给您发工资（缴纳社保）。请加微信号“xintairuan0203”，点击“个人信息”补全相关资料。否则会影响您的工资发放（社保缴纳）。";
            }
        }else{
            detailMsgCont="公司通过服务商给您发工资（缴纳社保）。请加微信号“xintairuan0203”，点击“个人信息”补全相关资料。否则会影响您的工资发放（社保缴纳）。";
        }
        if(dtoList!=null && dtoList.size()>0){
            for(CustomersDto dto:dtoList){
                String msgCont=dto.getCustomerTurename() + "你好,"+detailMsgCont;  //+",薪太软公司";
                sendMsgService.sendMsg(dto.getCustomerPhone(), msgCont);
            }
        }
    }

    /**
     * 获取要发送的未补全信息数量
     * @param companyId
     * @throws BusinessException
     * @throws Exception
     */
    public Integer querySendInPlemeteMsgCount(Long companyId)throws BusinessException,Exception{
        if(companyId==null){
            throw new BusinessException("无企业信息");
        }
        CustomersDto customersDto=new CustomersDto();
        customersDto.setCustomerCompanyId(companyId);
        List<CustomersDto> customerDtos=customersReaderMapper.selectInCompleteListByNewCondition( customersDto);
        List<Long> customerIdList=new ArrayList<>();
        Date nowDate=new Date();
        if(customerDtos!=null && customerDtos.size()>0){
            for(CustomersDto dto:customerDtos){
                Date sendMsgTime=dto.getCustomerSendmsgTime();
                if(sendMsgTime==null || (sendMsgTime!=null && !(DateUtil.dateFormatterNoLine.format(sendMsgTime).equals(DateUtil.dateFormatterNoLine.format(nowDate))))){
                    if(StringUtils.isStrNull(dto.getCustomerPhone())){
                        throw new BusinessException(dto.getCustomerTurename()+"员工无手机号码,不能发送短信");
                    }
                    customerIdList.add(dto.getCustomerId());
                }
            }
        }
        return customerIdList.size();
    }

    /**
     * 插入上传的员工信息
     *
     * @param companysBean
     * @return
     * @throws Exception
     */
    public ResultResponse customerUploadBatchNew(CompanysBean companysBean,byte[] in2b) throws BusinessException, Exception {

        ResultResponse resultResponse = new ResultResponse();
        if(companysBean==null || companysBean.getCompanyId()==null){
            throw new BusinessException("无企业信息");
        }
//        byte[] browserStreamArray =assemByteForFile(file);
        byte[] browserStreamArray =in2b;
        List<String> errorList=new ArrayList<>();
        if (browserStreamArray != null && browserStreamArray.length > 0) {
            //文件读取开始行
            int startLine = CustomerConstants.CUSTOMER_UPLOADLINE_START;
            //文件读取页签
            int sheetNum = CustomerConstants.CUSTOMER_UPLOADSHEET_START;
            //每行最大列数
            int sheetLastNum = CustomerConstants.CUSTOMER_UPLOADLINDE_NEWLASTNUM;
            //解析Excel
            List<Object[]> list = getDataFromExcel(browserStreamArray, startLine, sheetNum, sheetLastNum);
            LOGGER.info("提交上传的新增用户EXCEL文件:解析EXCEL,返回列表:" + JSON.toJSONString(list));
            //验证EXCEL
            List<CustomersBean> customerBeanList = new ArrayList<>();
            List<CustomersStationBean> customersStationBeanList = new ArrayList<>();
            validatorUploadCustomers( list, startLine, companysBean.getCompanyId(), customerBeanList, customersStationBeanList,errorList);
            if(errorList!=null && errorList.size()>0){
                resultResponse.setSuccess(false);
                resultResponse.setData(errorList);
                return resultResponse;
            }

            List<String> phoneList = new ArrayList<>();
            if (customerBeanList != null && customerBeanList.size() > 0) {//插入之前的验证:验证Excel中手机号\身份证号不能重复
                for (int i=0,len=customerBeanList.size();i<len;i++) {
                    CustomersBean checkCustomerBean=customerBeanList.get(i);
                    if (phoneList.contains(checkCustomerBean.getCustomerPhone())) {
                        LOGGER.info("提交上传的新增用户EXCEL文件,手机号在excel中有重复");
                        resultResponse.setMessage((phoneList.indexOf(checkCustomerBean.getCustomerPhone())+startLine + 1)+"行," + (i + startLine + 1) + "行手机号码重复");
                        errorList.add((phoneList.indexOf(checkCustomerBean.getCustomerPhone())+startLine + 1)+"行," + (i + startLine + 1) + "行手机号码重复");
//                        return resultResponse;
                    } else {
                        phoneList.add(checkCustomerBean.getCustomerPhone());
                    }
                }
            }

            if(errorList!=null && errorList.size()>0){
                resultResponse.setSuccess(false);
                resultResponse.setData(errorList);
                return resultResponse;
            }
            //验证手机号是否是唯一的
            //要编辑的手机号
            List<CustomersBean> willEditPhoneDto = new ArrayList<>();
            List<CustomersStationBean> willEditPhoneStation = new ArrayList<>();
            //要新增的手机号
            List<CustomersBean> willAddPhoneDto = new ArrayList<>();
            List<CustomersStationBean> willAddPhoneStation = new ArrayList<>();
            if (phoneList != null && phoneList.size() > 0) {
                Map<Integer, List<CustomersDto>> phoneMap = selectPhoneForeach(phoneList, companysBean.getCompanyId(), CustomerConstants.CUSTOMER_WILLOPERATION_ADD, -1);
                if (phoneMap != null && phoneMap.size() > 0 && phoneMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY) != null && phoneMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY).size() > 0) {
                    String showErrorStr = "";
                    List<CustomersDto> customersList = phoneMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_COPY);
                    for (CustomersDto checkDto : customersList) {
                        showErrorStr += checkDto.getCustomerPhone() + ",";
                    }
                    resultResponse.setMessage("手机号:" + showErrorStr + "已经存在");
                    errorList.add("手机号:" + showErrorStr + "已经存在");
//                    return resultResponse;
                }
                if(errorList!=null && errorList.size()>0){
                    resultResponse.setSuccess(false);
                    resultResponse.setData(errorList);
                    return resultResponse;
                }
                if (customerBeanList != null && customerBeanList.size() > 0) {
                    for (int i = 0; i < customerBeanList.size(); i++) {
                        CustomersBean editCustomerBean = customerBeanList.get(i);
                        boolean phoneCheckFlag = false;//是否有符合编辑的customer,false代表没有符合编辑的customer
                        //是否是符合编辑的customerbean
                        if (phoneMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_EDIT) != null && phoneMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_EDIT).size() > 0) {
                            for (CustomersDto willEditDto : phoneMap.get(CustomerConstants.CUSTOMER_WILLOPERATIONSTATE_EDIT)) {
                                if (editCustomerBean.getCustomerPhone().equals(willEditDto.getCustomerPhone())) {
                                    editCustomerBean.setCustomerId(willEditDto.getCustomerId());
                                    willEditPhoneDto.add(editCustomerBean);
                                    willEditPhoneStation.add(customersStationBeanList.get(i));
                                    phoneCheckFlag = true;
                                    break;
                                }
                            }
                        }
                        if (!phoneCheckFlag) {
                            willAddPhoneDto.add(editCustomerBean);
                            willAddPhoneStation.add(customersStationBeanList.get(i));
                        }

                    }
                }
            }

            //插入数据
            Date newDate = new Date();
            //员工记录信息列表(方便批量插入数据)
            List<CustomersRecordBean> customersRecordBeanList = new ArrayList<>();
            //员工账户列表(方便批量插入数据)
            List<SubAccountBean> subAccountBeanList = new ArrayList<>();
            BigDecimal commDecimal = new BigDecimal("0.00");
            if (willAddPhoneDto != null && willAddPhoneDto.size() > 0) {
                for (int i = 0; i < willAddPhoneDto.size(); i++) {
                    CustomersBean insertCustomerBean = willAddPhoneDto.get(i);
                    insertCustomerBean.setCustomerCompanyId(companysBean.getCompanyId());//企业ID
                    insertCustomerBean.setCustomerCompanyName(companysBean.getCompanyName());//企业名称
                    insertCustomerBean.setCustomerSign(1);//可使用
                    insertCustomerBean.setCustomerIspay(1);//可发工资
                    insertCustomerBean.setCustomerAddtime(newDate);//添加时间
                    insertCustomerBean.setCustomerRegularTime(newDate);//定薪时间
                    insertCustomerBean.setCustomerIsExpense(0);
                    insertCustomerBean.setCustomerSocialApproveState(CustomerConstants.CUSTOMER_SOCIAL_APPROVESTATE_NO);//社保通资料审核状态
                    insertCustomerBean.setCustomerCurrentExpense(BigDecimal.ZERO);//当前报销总额
                    //插入员工信息
                    Long customerId = insertCustomerForId(insertCustomerBean);
                    if (customerId == null || customerId <= 0L) {
                        LOGGER.info("提交上传的新增用户EXCEL文件,插入员工信息失败");
                        resultResponse.setMessage("插入员工信息失败");
                        errorList.add("插入员工信息失败");
                        throw new BusinessException("插入员工信息失败");
//                        return resultResponse;
                    }
                    willAddPhoneDto.get(i).setCustomerId(customerId);
                    //拼装岗位信息
                    willAddPhoneStation.get(i).setStationCustomerId(customerId);
                    willAddPhoneStation.get(i).setStationCreateTime(newDate);
                    willAddPhoneStation.get(i).setStationUpdateTime(newDate);
                    //拼装员工记录信息
                    CustomersRecordBean customersRecordBean = new CustomersRecordBean();
                    customersRecordBean.setRecordCustomerId(customerId);
                    customersRecordBean.setRecordOperationTime(newDate);
                    customersRecordBean.setRecordCreateTime(newDate);
                    customersRecordBean.setRecordUpdateTime(newDate);
                    if (willAddPhoneStation.get(i).getStationCustomerState() == CustomerConstants.CUSTOMER_PERSONSTATE_ENTER) {//入职
                        customersRecordBean.setRecordOperationType(CustomerConstants.CUSTOMER_RECORDTYPE_ENTER);
                        customersRecordBeanList.add(customersRecordBean);
                    } else if (willAddPhoneStation.get(i).getStationCustomerState() == CustomerConstants.CUSTOMER_PERSONSTATE_REGULAR) {//转正
                        customersRecordBean.setRecordOperationType(CustomerConstants.CUSTOMER_RECORDTYPE_ENTER);
                        customersRecordBeanList.add(customersRecordBean);
                        customersRecordBean = new CustomersRecordBean();
                        customersRecordBean.setRecordCustomerId(customerId);
                        customersRecordBean.setRecordOperationTime(newDate);
                        customersRecordBean.setRecordCreateTime(newDate);
                        customersRecordBean.setRecordUpdateTime(newDate);
                        customersRecordBean.setRecordOperationType(CustomerConstants.CUSTOMER_RECORDTYPE_REGULAR);
                        customersRecordBeanList.add(customersRecordBean);
                    }
                    //拼装员工账户信息
                    SubAccountBean subAccountBean = new SubAccountBean();
                    subAccountBean.setCustId(customerId);
                    subAccountBean.setProperty(CompanyProtocolConstants.SUBACCOUNT_PROPERTY_CUSTOMER);//账户性质 1-个人 2-企业
                    subAccountBean.setAmout(commDecimal);
                    subAccountBean.setCashAmout(commDecimal);
                    subAccountBean.setUncashAmount(commDecimal);
                    subAccountBean.setFreezeCashAmount(commDecimal);
                    subAccountBean.setFreezeUncashAmount(commDecimal);
                    subAccountBean.setHash(MethodUtil.MD5(UUID.randomUUID().toString()));
                    subAccountBean.setCheckValue(subAccountService.getMd5Code(subAccountBean));
                    subAccountBean.setState("00");//状态 00-生效,01-冻结,02-注销
                    subAccountBean.setIsAutoTransfer(1);//余额是否自动转入 0:非自动 1;自动
                    subAccountBean.setCreateTime(newDate);
                    subAccountBean.setUpdateTime(newDate);
                    subAccountBeanList.add(subAccountBean);


                }
                //批量插入岗位信息
                if (willAddPhoneStation != null && willAddPhoneStation.size() > 0) {
                    int stationCount = customersStationService.insertBatch(willAddPhoneStation);
                    if (stationCount != willAddPhoneStation.size()) {
                        LOGGER.info("提交上传的新增用户EXCEL文件,插入员工岗位信息失败");
                        resultResponse.setMessage("插入员工岗位信息失败");
                        errorList.add("插入员工岗位信息失败");
                        throw new BusinessException("插入员工岗位信息失败");
//                        return resultResponse;
                    }
                }
                //批量插入
                if (subAccountBeanList != null && subAccountBeanList.size() > 0) {
                    int accountCount = subAccountService.insertBatch(subAccountBeanList);
                    if (accountCount != subAccountBeanList.size()) {
                        LOGGER.info("提交上传的新增用户EXCEL文件,批量插入员工账户信息失败");
                        resultResponse.setMessage("批量插入员工账户信息失败");
                        errorList.add("批量插入员工账户信息失败");
                        throw new BusinessException("批量插入员工账户信息失败");
//                        return resultResponse;
                    }
                }
            }
            //如果存在手机号相等但是离职状态的则编辑(这个数据量很小,大多数没有)
            if (willEditPhoneDto != null && willEditPhoneDto.size() > 0) {
                for (int i = 0; i < willEditPhoneDto.size(); i++) {
                    CustomersBean editCustomerBean = willEditPhoneDto.get(i);
                    editCustomerBean.setCustomerCompanyId(companysBean.getCompanyId());//企业ID
                    editCustomerBean.setCustomerCompanyName(companysBean.getCompanyName());//企业名称
                    editCustomerBean.setCustomerSign(1);//可使用
                    editCustomerBean.setCustomerIspay(1);//可发工资
                    editCustomerBean.setCustomerAddtime(newDate);//添加时间
                    editCustomerBean.setCustomerIsExpense(0);
                    //社保通状态
                    editCustomerBean.setCustomerSocialApproveState(CustomerConstants.CUSTOMER_SOCIAL_APPROVESTATE_NO);
                    editCustomerBean.setCustomerCurrentExpense(BigDecimal.ZERO);
                    editCustomerBean.setCustomerRegularTime(newDate);//定薪时间
//                    if (StringUtils.isStrNull(editCustomerBean.getCustomerIdcard())) {
//                        editCustomerBean.setCustomerIdcard("");
//                    }
//                    if (StringUtils.isStrNull(editCustomerBean.getCustomerBank())) {
//                        editCustomerBean.setCustomerBank("");
//                    }
//                    if (StringUtils.isStrNull(editCustomerBean.getCustomerBanknumber())) {
//                        editCustomerBean.setCustomerBanknumber("");
//                    }
                    //更新员工信息
                    int count = updateByPrimaryKeySelective(editCustomerBean);
                    if (count <= 0) {
                        LOGGER.info("提交上传的新增用户EXCEL文件,编辑员工基本信息失败");
                        resultResponse.setMessage("编辑员工基本信息失败");
                        errorList.add("编辑员工基本信息失败");
                        throw new BusinessException("编辑员工基本信息失败");
//                        return resultResponse;
                    }
                    //拼装岗位信息
                    willEditPhoneStation.get(i).setStationCustomerId(editCustomerBean.getCustomerId());
                    willEditPhoneStation.get(i).setStationUpdateTime(newDate);
                    if (willEditPhoneStation.get(i).getStationRegularTime() == null) {
                        //特殊验证,将转正时间更新为空
                        willEditPhoneStation.get(i).setCheckRegularTime(1);
                    }
//                    if (StringUtils.isStrNull(willEditPhoneStation.get(i).getStationCustomerNumber())) {
//                        willEditPhoneStation.get(i).setStationCustomerNumber("");
//                    }
                    //更新岗位信息
                    int stationCount = customersStationService.updateByCustomerId(willEditPhoneStation.get(i));
                    if (stationCount <= 0) {
                        willEditPhoneStation.get(i).setStationCreateTime(newDate);
                        int stationInsertCount = customersStationService.insert(willEditPhoneStation.get(i));
                        if (stationInsertCount <= 0) {
                            LOGGER.info("提交上传的新增用户EXCEL文件,编辑完后插入员工岗位信息失败");
                            resultResponse.setMessage("插入员工岗位信息失败");
                            errorList.add("插入员工岗位信息失败");
                            throw new BusinessException("插入员工岗位信息失败");
//                            return resultResponse;
                        }
                    }
                    //拼装员工记录信息
                    CustomersRecordBean customersRecordBean = new CustomersRecordBean();
                    customersRecordBean.setRecordCustomerId(editCustomerBean.getCustomerId());
                    customersRecordBean.setRecordOperationTime(newDate);
                    customersRecordBean.setRecordCreateTime(newDate);
                    customersRecordBean.setRecordUpdateTime(newDate);
                    if (willEditPhoneStation.get(i).getStationCustomerState() == CustomerConstants.CUSTOMER_PERSONSTATE_ENTER) {//入职
                        customersRecordBean.setRecordOperationType(CustomerConstants.CUSTOMER_RECORDTYPE_ENTER);
                        customersRecordBeanList.add(customersRecordBean);
                    } else if (willEditPhoneStation.get(i).getStationCustomerState() == CustomerConstants.CUSTOMER_PERSONSTATE_REGULAR) {//转正
                        customersRecordBean.setRecordOperationType(CustomerConstants.CUSTOMER_RECORDTYPE_ENTER);
                        customersRecordBeanList.add(customersRecordBean);
                        customersRecordBean = new CustomersRecordBean();
                        customersRecordBean.setRecordCustomerId(editCustomerBean.getCustomerId());
                        customersRecordBean.setRecordOperationTime(newDate);
                        customersRecordBean.setRecordCreateTime(newDate);
                        customersRecordBean.setRecordUpdateTime(newDate);
                        customersRecordBean.setRecordOperationType(CustomerConstants.CUSTOMER_RECORDTYPE_REGULAR);
                        customersRecordBeanList.add(customersRecordBean);
                    }
                }
            }

            //批量插入员工记录
            if (customersRecordBeanList != null && customersRecordBeanList.size() > 0) {
                int recordCount = customersRecordService.insertBatch(customersRecordBeanList);
                if (recordCount != customersRecordBeanList.size()) {
                    LOGGER.info("提交上传的新增用户EXCEL文件,插入员工记录信息失败");
                    resultResponse.setMessage("插入员工记录信息失败");
                    errorList.add("插入员工记录信息失败");
                    throw new BusinessException("插入员工记录信息失败");
//                    return resultResponse;
                }
            }
            List<CustomersBean> allDto=new ArrayList<>();
            if(willAddPhoneDto!=null && willAddPhoneDto.size()>0){
                allDto.addAll(willAddPhoneDto);
            }
            if(willEditPhoneDto!=null && willEditPhoneDto.size()>0){
                allDto.addAll(willEditPhoneDto);
            }
            resultResponse.setData(allDto);
            resultResponse.setSuccess(true);
        } else {
            LOGGER.info("提交上传的新增用户EXCEL文件,文件不存在,上传失败");
            resultResponse.setMessage("文件不存在,上传失败");
            throw new BusinessException("文件不存在,上传失败");
        }

        return resultResponse;
    }

    /**
     * 验证并拼装EXCEL
     * @param list
     * @param startLine
     * @param companyId
     * @param customerBeanList
     * @param customersStationBeanList
     * @return
     * @throws BusinessException
     * @throws Exception
     */
    private ResultResponse validatorUploadCustomers(List<Object[]> list,int startLine,long companyId,List<CustomersBean> customerBeanList,List<CustomersStationBean> customersStationBeanList,List<String> errorList)throws BusinessException,Exception{
        ResultResponse resultResponse=new ResultResponse();
        Date nowDate=new Date();
        if (list != null && list.size() > 0) {
            //获取当前基本工资
            PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(companyId);
            Date startDate=null;
            if(payCycleBean!=null && payCycleBean.getStartDate()!=null){
                startDate=  payCycleBean.getStartDate();
            }
            for (int i = 0; i < list.size(); i++) {
                Object[] objArray = list.get(i);
//                if (objArray[0] != null && !StringUtils.isStrNull(String.valueOf(objArray[0]))
//                        && objArray[1] != null && !StringUtils.isStrNull(String.valueOf(objArray[1]))
//                        && objArray[2] != null && !StringUtils.isStrNull(String.valueOf(objArray[2]))
//                        && objArray[5] != null && !StringUtils.isStrNull(String.valueOf(objArray[5]))) {//有正常数据
                    //姓名
                    String customerTurename = String.valueOf(objArray[0]);
                    if (StringUtils.isStrNull(customerTurename)) {
                        resultResponse.setMessage("第" + (i + startLine + 1) + "行,请填写姓名");
                        errorList.add("第" + (i + startLine + 1) + "行,请填写姓名");
//                        return resultResponse;
                    }else{
                        if (customerTurename.length() > 20 || customerTurename.length() < 2) {
                            resultResponse.setMessage("第" + (i + startLine + 1) + "行,请填写正确的姓名");
                            errorList.add("第" + (i + startLine + 1) + "行,请填写正确的姓名");
//                        return resultResponse;
                        }
                    }
                    //手机
                    String customerPhone = String.valueOf(objArray[1]);
                    if (StringUtils.isStrNull(customerPhone)) {
                        resultResponse.setMessage("第" + (i + startLine + 1) + "行,请填手机号");
                        errorList.add("第" + (i + startLine + 1) + "行,请填手机号");
//                        return resultResponse;
                    }else{
                        String regexPhone = "^1[3|4|5|7|8]\\d{9}$";
                        if (!Pattern.matches(regexPhone, customerPhone)) {
                            resultResponse.setMessage("第" + (i + startLine + 1) + "行,请填写正确的手机号");
                            errorList.add("第" + (i + startLine + 1) + "行,请填写正确的手机号");
//                        return resultResponse;
                        }
                    }

                    //入职日期
                    String stationEnterTimeStr = String.valueOf(objArray[2]);
                    Date stationEnterTime = null;
                    if (StringUtils.isStrNull(stationEnterTimeStr)) {
                        resultResponse.setMessage("第" + (i + startLine + 1) + "行,请填写入职时间");
                        errorList.add("第" + (i + startLine + 1) + "行,请填写入职时间");
//                        return resultResponse;
                    }else{
                        int oldSize=errorList.size();
                        ResultResponse validatorResponse = validatorDateByExcel(stationEnterTimeStr, i, startLine, 3,errorList);
                        if (errorList.size()==oldSize) {
                            if (validatorResponse.getData() != null) {
                                stationEnterTime = (Date) validatorResponse.getData();
                            }
                        }
                    }


                    //试用期基本工资
                    String enterAmountStr="";
                    BigDecimal enterAmountDecimal=null;
                    if(objArray[3]!=null && !StringUtils.isStrNull(String.valueOf(objArray[3]))){
                        enterAmountStr = String.valueOf(objArray[3]);
                        String regexEnterAmount = "^(?!0+(?:\\.0+)?$)(?:[1-9]\\d*|0)(?:\\.\\d{1,2})?$";
                        if (!Pattern.matches(regexEnterAmount, enterAmountStr)) {
                            resultResponse.setMessage("第" + (i + startLine + 1) + "行,请填写正确的试用期基本工资");
                            errorList.add("第" + (i + startLine + 1) + "行,请填写正确的试用期基本工资");
//                            return resultResponse;
                        }
                        if(errorList==null || errorList.size()<=0){
                            enterAmountDecimal=new BigDecimal(enterAmountStr);//试用期工资
                        }
                    }


                    //转正日期
                    Date stationRegularTime = null;
                    if(objArray[4]!=null && !StringUtils.isStrNull(String.valueOf(objArray[4]))){
                        String stationRegularTimeStr = String.valueOf(objArray[4]);
                        int oldSize=errorList.size();
                        ResultResponse validatorStationResponse = validatorDateByExcel(stationRegularTimeStr, i, startLine, 6,errorList);
                        if (errorList.size()==oldSize) {
                            if (validatorStationResponse.getData() != null) {
                                stationRegularTime = (Date) validatorStationResponse.getData();
                            }
                        }
                    }

                    //转正后基本工资
                    BigDecimal regularAmountDecimal=null;
                    String regularAmountStr = String.valueOf(objArray[5]);
                    if (StringUtils.isStrNull(regularAmountStr)) {
                        resultResponse.setMessage("第" + (i + startLine + 1) + "行,请填写转正后基本工资");
                        errorList.add("第" + (i + startLine + 1) + "行,请填写转正后基本工资");
//                        return resultResponse;
                    }else{
                        String regexRegularAmount = "^(?!0+(?:\\.0+)?$)(?:[1-9]\\d*|0)(?:\\.\\d{1,2})?$";
                        if (!Pattern.matches(regexRegularAmount, regularAmountStr)) {
                            resultResponse.setMessage("第" + (i + startLine + 1) + "行,请填写正确的转正后基本工资");
                            errorList.add("第" + (i + startLine + 1) + "行,请填写正确的转正后基本工资");
//                        return resultResponse;
                        }
                    }
                    if(errorList==null || errorList.size()<=0){
                        regularAmountDecimal=new BigDecimal(regularAmountStr);//转正工资
                    }
                    if(errorList==null || errorList.size()<=0){
                        if(stationEnterTime!=null){
                            int stationEnterTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(stationEnterTime));//入职时间
                            int nowTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(nowDate));
                            if(stationEnterTimeInt>nowTimeInt){
                                resultResponse.setMessage("第" + (i + startLine + 1) + "行,入职时间不能大于当前时间");
                                errorList.add("第" + (i + startLine + 1) + "行,入职时间不能大于当前时间");
                            }
                        }

                        if(stationRegularTime!=null && !StringUtils.isStrNull(enterAmountStr)){
                            int stationRegularTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(stationRegularTime));//转正时间
                            int stationEnterTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(stationEnterTime));//入职时间
                            if(stationRegularTimeInt<stationEnterTimeInt){
                                resultResponse.setMessage("第" + (i + startLine + 1) + "行,转正时间不能小于入职时间");
                                errorList.add("第" + (i + startLine + 1) + "行,转正时间不能小于入职时间");
//                            return resultResponse;
                            }
                            if(stationRegularTimeInt==stationEnterTimeInt && enterAmountDecimal.compareTo(regularAmountDecimal)!=0){
                                resultResponse.setMessage("第" + (i + startLine + 1) + "行,转正时间有误");
                                errorList.add("第" + (i + startLine + 1) + "行,转正时间有误");
//                            return resultResponse;
                            }
                            if(regularAmountDecimal.compareTo(enterAmountDecimal)<0){
                                resultResponse.setMessage("第" + (i + startLine + 1) + "行,转正后基本工资不能小于试用期基本工资");
                                errorList.add("第" + (i + startLine + 1) + "行,转正后基本工资不能小于试用期基本工资");
//                            return resultResponse;
                            }
                        }

                        if(stationRegularTime==null && !StringUtils.isStrNull(enterAmountStr)){
                            if(regularAmountDecimal.compareTo(enterAmountDecimal)<0){
                                resultResponse.setMessage("第" + (i + startLine + 1) + "行,转正后基本工资不能小于试用期基本工资");
                                errorList.add("第" + (i + startLine + 1) + "行,转正后基本工资不能小于试用期基本工资");
//                            return resultResponse;
                            }
                        }

                        if(stationRegularTime!=null && StringUtils.isStrNull(enterAmountStr)){
                            int stationRegularTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(stationRegularTime));//转正时间
                            int stationEnterTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(stationEnterTime));//入职时间
                            int nowTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(nowDate));

                            if(stationRegularTimeInt>=nowTimeInt){
                                resultResponse.setMessage("第" + (i + startLine + 1) + "行,请填试用期基本工资");
                                errorList.add("第" + (i + startLine + 1) + "行,请填试用期基本工资");
//                            return resultResponse;
                            }
                            if(stationRegularTimeInt<nowTimeInt){
                                if(startDate!=null){
                                    int startTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(startDate));
                                    if(stationRegularTimeInt>=startTimeInt){
                                        resultResponse.setMessage("第" + (i + startLine + 1) + "行,请填试用期基本工资");
                                        errorList.add("第" + (i + startLine + 1) + "行,请填试用期基本工资");
//                                    return resultResponse;
                                    }
                                }
                            }
                            if(stationRegularTimeInt<stationEnterTimeInt){
                                resultResponse.setMessage("第" + (i + startLine + 1) + "行,转正时间不能小于入职时间");
                                errorList.add("第" + (i + startLine + 1) + "行,转正时间不能小于入职时间");
//                            return resultResponse;
                            }
                            if(stationRegularTimeInt==stationEnterTimeInt && enterAmountDecimal.compareTo(regularAmountDecimal)!=0){
                                resultResponse.setMessage("第" + (i + startLine + 1) + "行,转正时间有误");
                                errorList.add("第" + (i + startLine + 1) + "行,转正时间有误");
//                            return resultResponse;
                            }
                        }

                        //拼装要新增的数据,用户信息
                        CustomersBean customersBean = new CustomersBean();
                        customersBean.setCustomerPhone(customerPhone);
                        customersBean.setCustomerTurename(customerTurename);
                        customersBean.setCustomerRegularSalary(regularAmountDecimal);
                        customersBean.setCustomerCurrentSalary(CustomerConstants.INIT_BIGDECIMAL_COMPARE);
                        if(enterAmountDecimal!=null){
                            customersBean.setCustomerProbationSalary(enterAmountDecimal);
                        }else{
                            customersBean.setCustomerProbationSalary(regularAmountDecimal);
                        }
                        customerBeanList.add(customersBean);
                        //用户岗位信息
                        CustomersStationBean customersStationBean = new CustomersStationBean();
                        customersStationBean.setStationEnterTime(stationEnterTime);

                        //转正时间为空或者转时间等于当前时间,则为转正,其它的为试用期
                        if (stationRegularTime != null) {
                            customersStationBean.setStationRegularTime(stationRegularTime);
                            int stationRegularTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(stationRegularTime));//转正时间
                            int nowTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(nowDate));//当前时间
                            if(stationRegularTimeInt<=nowTimeInt){
                                customersStationBean.setStationCustomerState(CustomerConstants.CUSTOMER_PERSONSTATE_REGULAR);
                            }else{
                                customersStationBean.setStationCustomerState(CustomerConstants.CUSTOMER_PERSONSTATE_ENTER);
                            }
                        }else{
                            customersStationBean.setStationCustomerState(CustomerConstants.CUSTOMER_PERSONSTATE_REGULAR);
                        }
                        customersStationBean.setStationEmployMethod(CustomerConstants.CUSTOMER_EMPLOYMETHOD_OFFICAL);//默认都是正式工
                        customersStationBeanList.add(customersStationBean);
                    }

//                }
//                else{
//                    resultResponse.setMessage("第" + (i + startLine + 1) + "行,数据不完整");
//                    errorList.add("第" + (i + startLine + 1) + "行,数据不完整");
//                }
            }
        } else {
            LOGGER.info("提交上传的新增用户EXCEL文件,上传的excel中没有数据");
            resultResponse.setMessage("上传的excel中没有数据");
            errorList.add("上传的excel中没有数据");
        }
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 插入未补全的员工信息
     *
     * @param companysBean
     * @return
     * @throws Exception
     */
    public ResultResponse inCompleteUploadBatchNew(CompanysBean companysBean,byte[] in2b) throws BusinessException, Exception {

        ResultResponse resultResponse = new ResultResponse();
        if(companysBean==null || companysBean.getCompanyId()==null){
            throw new BusinessException("无企业信息");
        }
        byte[] browserStreamArray =in2b;
        List<String> errorList=new ArrayList<>();
        if (browserStreamArray != null && browserStreamArray.length > 0) {
            //文件读取开始行
            int startLine = CustomerConstants.CUSTOMER_UPLOADLINE_START;
            //文件读取页签
            int sheetNum = CustomerConstants.CUSTOMER_UPLOADSHEET_START;
            //每行最大列数
            int sheetLastNum = CustomerConstants.CUSTOMER_UPLOADLINDE_INCOMPLETENUM;
            //解析Excel
            List<Object[]> list = getDataFromExcel(browserStreamArray, startLine, sheetNum, sheetLastNum);
            LOGGER.info("提交上传的新增用户EXCEL文件:解析EXCEL,返回列表:" + JSON.toJSONString(list));
            //验证EXCEL
            List<CustomersBean> customerBeanList = new ArrayList<>();
            validatorInCompleteCustomers( list, startLine, companysBean.getCompanyId(), customerBeanList,errorList);
            if(errorList!=null && errorList.size()>0){
                resultResponse.setSuccess(false);
                resultResponse.setData(errorList);
                return resultResponse;
            }

            List<String> phoneList = new ArrayList<>();
            if (customerBeanList != null && customerBeanList.size() > 0) {//插入之前的验证:验证Excel中手机号\身份证号不能重复
                for (int i=0,len=customerBeanList.size();i<len;i++) {
                    CustomersBean checkCustomerBean=customerBeanList.get(i);
                    if (phoneList.contains(checkCustomerBean.getCustomerPhone())) {
                        LOGGER.info("提交上传的新增用户EXCEL文件,手机号在excel中有重复");
                        errorList.add((phoneList.indexOf(checkCustomerBean.getCustomerPhone())+startLine + 1)+"行," + (i + startLine + 1) + "行手机号码重复");
                    } else {
                        phoneList.add(checkCustomerBean.getCustomerPhone());
                    }
                }
            }

            List<String> idCardList = new ArrayList<>();
            if (customerBeanList != null && customerBeanList.size() > 0) {//插入之前的验证:验证Excel中手机号\身份证号不能重复
                for (int i=0,len=customerBeanList.size();i<len;i++) {
                    CustomersBean checkCustomerBean=customerBeanList.get(i);
                    if (idCardList.contains(checkCustomerBean.getCustomerIdcard())) {
                        LOGGER.info("提交上传的新增用户EXCEL文件,身份证号在excel中有重复");
                        errorList.add((idCardList.indexOf(checkCustomerBean.getCustomerIdcard())+startLine + 1)+"行," + (i + startLine + 1) + "行身份证号重复");
                    } else {
                        idCardList.add(checkCustomerBean.getCustomerIdcard());
                    }
                }
            }

            List<String> bankNumberList = new ArrayList<>();
            if (customerBeanList != null && customerBeanList.size() > 0) {//插入之前的验证:验证Excel中手机号\身份证号不能重复
                for (int i=0,len=customerBeanList.size();i<len;i++) {
                    CustomersBean checkCustomerBean=customerBeanList.get(i);
                    if (bankNumberList.contains(checkCustomerBean.getCustomerBanknumber())) {
                        LOGGER.info("提交上传的新增用户EXCEL文件,工资卡号在excel中有重复");
                        errorList.add((bankNumberList.indexOf(checkCustomerBean.getCustomerBanknumber())+startLine + 1)+"行," + (i + startLine + 1) + "行工资卡号重复");
                    } else {
                        bankNumberList.add(checkCustomerBean.getCustomerBanknumber());
                    }
                }
            }
            if(errorList!=null && errorList.size()>0){
                resultResponse.setSuccess(false);
                resultResponse.setData(errorList);
                return resultResponse;
            }

            //验证身份证号是否是唯一的
            if (idCardList != null && idCardList.size()>0) {
                List<CustomersDto> dtoList = customersReaderMapper.selectidCardForeach(idCardList, companysBean.getCompanyId());
                if(dtoList!=null && dtoList.size()>0){
                    for(int i=0;i<idCardList.size();i++){
                        String idCardStr=idCardList.get(i);
                        for(CustomersDto customersDto:dtoList){
                            String customerPhone=phoneList.get(i);
                            if(!StringUtils.isStrNull(customersDto.getCustomerPhone()) && !customersDto.getCustomerPhone().equals(customerPhone)
                                    && customersDto.getCustomerIdcard().equals(idCardStr)
                                    && (customersDto.getStationCustomerState().intValue() == CustomerConstants.CUSTOMER_PERSONSTATE_ENTER
                                    || customersDto.getStationCustomerState().intValue() == CustomerConstants.CUSTOMER_PERSONSTATE_REGULAR
                                    || customersDto.getStationCustomerState().intValue() == CustomerConstants.CUSTOMER_PERSONSTATE_LEAVE)){
                                errorList.add((i + startLine + 1) + "行身份证号重复");
                            }
                        }
                    }
                }
            }

            //查询企业下的所有员工
            List<CustomersBean> customersList=customersReaderMapper.selectCustomersForCompanyId(companysBean.getCompanyId());
            if(customerBeanList!=null && customerBeanList.size()>0 && customersList!=null && customersList.size()>0){
                for(int i=0;i<customerBeanList.size();i++){
                    CustomersBean resultBean=customerBeanList.get(i);
                    for(CustomersBean allBean:customersList){
                        if(!StringUtils.isStrNull(allBean.getCustomerPhone()) && allBean.getCustomerPhone().equals(resultBean.getCustomerPhone())){
                            if(!StringUtils.isStrNull(allBean.getCustomerIdcard()) && !allBean.getCustomerIdcard().equals(resultBean.getCustomerIdcard())){
                                LOGGER.info((i + startLine + 1)+"行，与已有身份证号冲突，请核查");
                                errorList.add((i + startLine + 1)+"行，与已有身份证号冲突，请核查");
                            }
                            if(!StringUtils.isStrNull(allBean.getCustomerBanknumber()) && !allBean.getCustomerBanknumber().equals(resultBean.getCustomerBanknumber())){
                                LOGGER.info((i + startLine + 1)+"行，与已有工资卡号冲突，请核查");
                                errorList.add((i + startLine + 1)+"行，与已有工资卡号冲突，请核查");
                            }
                            if(!StringUtils.isStrNull(allBean.getCustomerBank()) && !allBean.getCustomerBank().equals(resultBean.getCustomerBank())){
                                LOGGER.info((i + startLine + 1)+"行，与已有工资卡开户行冲突，请核查");
                                errorList.add((i + startLine + 1)+"行，与已有工资卡开户行冲突，请核查");
                            }
                            if(!StringUtils.isStrNull(allBean.getCustomerTurename()) && !allBean.getCustomerTurename().equals(resultBean.getCustomerTurename())){
                                LOGGER.info((i + startLine + 1)+"行，姓名与系统不符，请核查");
                                errorList.add((i + startLine + 1)+"行，姓名与系统不符，请核查");
                            }
                            //如果姓名、性别、手机号、身份证号和照片有变更，则社保通状态更新为未审核
                            if(StringUtils.isStrNull(allBean.getCustomerIdcard())){
                                resultBean.setCustomerSocialApproveState(CustomerConstants.CUSTOMER_SOCIAL_APPROVESTATE_NO);
                            }
                        }
                    }
                }
            }
            if(errorList!=null && errorList.size()>0){
                resultResponse.setSuccess(false);
                resultResponse.setData(errorList);
                return resultResponse;
            }
            //批量更新数据
            if(customerBeanList!=null && customerBeanList.size()>0){
                int count= customersWriterMapper.updateInCompleteCustomersBatch(customerBeanList);
                if(count<=0){
                    LOGGER.error("提交上传的新增用户EXCEL文件,更新个数:"+count+",待更新个数:"+customerBeanList.size());
                    resultResponse.setMessage("批量更新未补全员工信息失败");
                    throw new BusinessException("批量更新未补全员工信息失败");
                }
            }

            resultResponse.setSuccess(true);
        } else {
            LOGGER.info("提交上传的新增用户EXCEL文件,文件不存在,上传失败");
            resultResponse.setMessage("文件不存在,上传失败");
        }
        return resultResponse;
    }

    /**
     * 验证并拼装未补全的EXCEL
     * @param list
     * @param startLine
     * @param companyId
     * @param customerBeanList
     * @return
     * @throws BusinessException
     * @throws Exception
     */
    private void validatorInCompleteCustomers(List<Object[]> list,int startLine,long companyId,List<CustomersBean> customerBeanList,List<String> errorList)throws BusinessException,Exception{
        if (list != null && list.size() > 0) {
            List<BankCodeBean> bankCodeBeenList=bankCodeReaderMapper.selectAll();
            for (int i = 0; i < list.size(); i++) {
                Object[] objArray = list.get(i);
//                if (objArray[0] != null && !StringUtils.isStrNull(String.valueOf(objArray[0]))
//                        && objArray[1] != null && !StringUtils.isStrNull(String.valueOf(objArray[1]))
//                        && objArray[2] != null && !StringUtils.isStrNull(String.valueOf(objArray[2]))
//                        && objArray[3] != null && !StringUtils.isStrNull(String.valueOf(objArray[3]))
//                        && objArray[4] != null && !StringUtils.isStrNull(String.valueOf(objArray[4]))) {//有正常数据
                    //姓名
                    String customerTurename = String.valueOf(objArray[0]);
                    if (StringUtils.isStrNull(customerTurename)) {
                        errorList.add("第" + (i + startLine + 1) + "行,请填写姓名");
                    }
                    if (customerTurename.length() > 20 || customerTurename.length() < 2) {
                        errorList.add("第" + (i + startLine + 1) + "行,请填写正确的姓名");
                    }
                    //手机
                    String customerPhone = String.valueOf(objArray[1]);
                    if (StringUtils.isStrNull(customerPhone)) {
                        errorList.add("第" + (i + startLine + 1) + "行,请填手机号");
                    }
                    String regexPhone = "^1[3|4|5|7|8]\\d{9}$";
                    if (!Pattern.matches(regexPhone, customerPhone)) {
                        errorList.add("第" + (i + startLine + 1) + "行,请填写正确的手机号");
                    }
                    //身份证号
                    String customerIdcard = String.valueOf(objArray[2]);
                    if (StringUtils.isStrNull(customerIdcard)) {
                        errorList.add("第" + (i + startLine + 1) + "行,请填写身份证号");
                    }
                    String regexIdCard = "^[0-9]{15}$|^[0-9]{18}$|^[a-zA-Z\\d]{18}$|^[a-zA-Z\\d]{15}$";
                    if(!Pattern.matches(regexIdCard, customerIdcard)){
                        errorList.add("第" + (i + startLine + 1) + "行,请填写正确的身份证号");
                    }
                    //工资卡开户行
                    String customerBankName = String.valueOf(objArray[3]);
                    if (StringUtils.isStrNull(customerBankName)) {
                        errorList.add("第" + (i + startLine + 1) + "行,请填写工资卡开户行");
                    }
                    //判断银行输入的是否正确
                    if(bankCodeBeenList!=null && bankCodeBeenList.size()>0){
                        boolean checkBankCodeFlag=false;//判断是否是正确的工资卡开户行,false代表不正确
                        for(BankCodeBean bankCodeBean:bankCodeBeenList){
                            if((!StringUtils.isStrNull(bankCodeBean.getBankFullName()) && customerBankName.equals(bankCodeBean.getBankFullName()))
                                    ||(!StringUtils.isStrNull(bankCodeBean.getBankAbbreviation()) && customerBankName.equals(bankCodeBean.getBankAbbreviation()))){
                                checkBankCodeFlag=true;
                                break;
                            }
                        }
                        if(!checkBankCodeFlag){
                            errorList.add("第" + (i + startLine + 1) + "行,请填写正确的工资卡开户行");
                        }
                    }
//                    BankCodeBean bankCodeBean= bankCodeService.selectByBankName(customerBankName);
//                    if(bankCodeBean==null){
//
//
//                    }

                    //工资卡号
                    String customerBankNumber = String.valueOf(objArray[4]);
                    if (StringUtils.isStrNull(customerBankNumber)) {
                        errorList.add("第" + (i + startLine + 1) + "行,请填写工资卡号");
                    }
                    String regexNumber = "^[0-9]{13,23}$";
                    if(!Pattern.matches(regexNumber, customerBankNumber)){
                        errorList.add("第" + (i + startLine + 1) + "行,请填写正确的工资卡号");
                    }

                    //拼装要新增的数据,用户信息
                    CustomersBean customersBean = new CustomersBean();
                    customersBean.setCustomerPhone(customerPhone);
                    customersBean.setCustomerTurename(customerTurename);
                    customersBean.setCustomerIdcard(customerIdcard);
                    customersBean.setCustomerBank(customerBankName);
                    customersBean.setCustomerBanknumber(customerBankNumber);
                    customersBean.setCustomerCompanyId(companyId);
                    customerBeanList.add(customersBean);
//                }
            }
        } else {
            LOGGER.info("提交上传的新增用户EXCEL文件,上传的excel中没有数据");
            errorList.add("上传的excel中没有数据");
        }
    }

    /**
     * 员工离职
     *
     * @param customersDto
     * @return
     */
    @SystemServiceLog(operation = "员工离职", modelName = "员工管理")
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResultResponse customerDimissionNew(CustomersDto customersDto) {

        if (StringUtils.isEmpty(customersDto.getCustomerId())) {
            LOGGER.info("customerId参数为空");
            throw new BusinessException("未知错误");
        }
        if (StringUtils.isEmpty(customersDto.getStationDimissingTime())) {
            throw new BusinessException("离职日期不能为空");

        }

        ResultResponse resultResponse = new ResultResponse();
        CustomersStationBean customersStationBean = customersStationReaderMapper.selectByCustomerId(customersDto.getCustomerId());
        if (customersStationBean == null) {
            throw new BusinessException("未知错误");
        }

        //判断离职时间  与入职时间的比较
        if (customersDto.getStationDimissingTime().getTime() - customersStationBean.getStationEnterTime().getTime() < 0) {
            throw new BusinessException("离职时间不能早于入职时间");
        }



        //离职可以选择大于当前时间    定时任务跑批
//        if(customersDto.getStationDimissingTime().getTime()-(new Date().getTime()) >0){
//            throw  new BusinessException("离职时间不能大于当前时间");
//        }


        boolean flag = compaireDimssTime(customersDto.getStationDimissingTime());
        if (!flag) {
            //删除部门领导信息
            companyDepsWriterMapper.clearLeader(customersDto.getCustomerId());

            //更新员工岗位表操作
            CustomersStationBean cu = new CustomersStationBean();
            cu.setStationCustomerId(customersDto.getCustomerId());
            cu.setStationCustomerState(3);//离职
            cu.setStationDimissingTime(customersDto.getStationDimissingTime());//离职日期
            cu.setStationUpdateTime(new Date());//修改日期
            int resultCu = customersStationWriterMapper.updateByCustomerId(cu);//更新
            if (resultCu < 0 || resultCu != 1) {
                LOGGER.info("更新员工岗位表出现错误");
                throw new BusinessException("未知错误");
            }
        }


        //插入员工记录表
        CustomersRecordBean cur = new CustomersRecordBean();
        cur.setRecordCustomerId(customersDto.getCustomerId());//员工id
        cur.setRecordOperationType(3);//离职
        cur.setRecordDimissingType(1);//主动离职 还是被动离职
        cur.setRecordDimissingTime(customersDto.getStationDimissingTime());//离职日期
        cur.setRecordOperationTime(new Date());//操作时间
        int resultCur = customersRecordWriterMapper.insert(cur);
        if (resultCur != 1 || resultCur < 0) {
            LOGGER.info("保存员工记录表出现错误");
            throw new BusinessException("未知错误");
        }
        //查询员工最新计薪周期开始日期
        PayCycleBean  payCycleBean = payCycleService.selectByCompanyId(customersDto.getCompanyId());
        if(payCycleBean==null){
            LOGGER.info("没有查询到计薪周期");
        }else{
            //判断离职日期
            if(customersDto.getStationDimissingTime().getTime()-payCycleBean.getStartDate().getTime()<0){
                //删除工资单
                customerPayrollWriterMapper.deleteByCustomerId(customersDto.getCompanyId(),customersDto.getCustomerId(),payCycleBean.getId());
            }
        }

        //所有的操作都正确
        resultResponse.setMessage("操作成功");
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     *查询企业下的所有员工
     * @param companyId
     * @return
     */
    public List<CustomersBean> selectCustomersForCompanyId(long companyId){
        return customersReaderMapper.selectCustomersForCompanyId(companyId);
    }

    /**
     * 更新员工信息   新版员工微信补全资料
     * @param customer
     * @return
     */
    @Override
    public int updateCustomerInfoByCustomerId(CustomersBean customer) {
        return customersWriterMapper.updateCustomerInfoByCustomerId(customer);
    }


    /**
     *查询某个员工在日期范围内的工资调整情况
     * @param customerId
     * @return
     */
    @Override
    public List<Map<String,String>> selectSalaryRecords(long customerId,Date startDate,Date endDate){
        List<Map<String,String>> salaryRecordList = new ArrayList<Map<String,String>>();
        try {
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(DateUtil.formateDateToYYYYMMDD(new Date()));

            CustomersBean customersBean = customersReaderMapper.selectByPrimaryKey(customerId);
            if(customersBean == null){
                LOGGER.error("员工信息不存在customerId=" + customerId);
                return null;
            }

            CustomersStationBean customersStationBean = customersStationReaderMapper.selectByCustomerId(customerId);
            if(customersStationBean == null){
                LOGGER.error("员工信息2不存在customerId=" + customerId);
                return null;
            }

            Date enterDate = DateUtil.formateDateToYYYYMMDD(customersStationBean.getStationEnterTime());

            if(enterDate.getTime() > endDate.getTime()){
                LOGGER.error("员工未入职customerId=" + customerId);
                return null;
            }

            TreeMap<Date, BigDecimal> changeList = new TreeMap<>();

            Date regularTime = customersStationBean.getStationRegularTime();
            if (regularTime != null) {//转正是否在当期区间，是的话就加进去
                regularTime = DateUtil.formateDateToYYYYMMDD(regularTime);
                if (regularTime.getTime() >= startDate.getTime() && regularTime.getTime() <= endDate.getTime()) {
                    changeList.put(regularTime, customersBean.getCustomerRegularSalary());
                }
            }

            List<CustomerSalaryRecordBean> customerSalaryRecordBeans = customerSalaryRecordReaderMapper.selectByDateArea(customerId, startDate, endDate);
            if (customerSalaryRecordBeans != null) {//查询当前区间调薪记录
                for (CustomerSalaryRecordBean customerSalaryRecordBean : customerSalaryRecordBeans) {
                    changeList.put(customerSalaryRecordBean.getEffectiveDate(), customerSalaryRecordBean.getNewSalary());
                }
            }

            BigDecimal currentSalary = null;
            //获取当前周期开始日最新工资
            if(enterDate.getTime() >= startDate.getTime()){
                currentSalary = customerUpdateSalaryService.getLastSalary(customerId, enterDate);
                startDate = enterDate;
            }else{
                currentSalary = customerUpdateSalaryService.getLastSalary(customerId, startDate);
            }

            //当前计薪周期第一天
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            String startDay = sdf2.format(startDate);

            BigDecimal sum = new BigDecimal(0d);

            if (changeList.size() > 0) {

                int lastCount = 0;
                Calendar startCalendar = Calendar.getInstance();

                for (startCalendar.setTime(startDate); startCalendar.getTime().getTime() <= endDate.getTime(); startCalendar.add(Calendar.DATE, 1)) {
                    Date date = startCalendar.getTime();
                    BigDecimal nextSalary = changeList.get(date);
                    if (nextSalary != null && nextSalary.compareTo(currentSalary) != 0) {
                        if (lastCount != 0) {
                            Map<String, String> salaryRecordMap = new HashMap<String, String>();
                            salaryRecordMap.put("salary",String.valueOf(currentSalary));
                            salaryRecordMap.put("startDay",startDay);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            salaryRecordMap.put("endDay",sdf.format(date));
                            salaryRecordList.add(salaryRecordMap);

                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.DAY_OF_MONTH, 1);
                            cal.setTime(date);
                            cal.add(Calendar.DATE, 1);
                            startDay = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

                            lastCount = 0;
                        }
                        currentSalary = changeList.get(date);
                    }
                    lastCount++;
                }

                Map<String, String> salaryRecordMap = new HashMap<String, String>();
                salaryRecordMap.put("salary",String.valueOf(currentSalary));
                salaryRecordMap.put("startDay",startDay);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                salaryRecordMap.put("endDay",sdf.format(endDate));
                salaryRecordList.add(salaryRecordMap);

            } else {//没有薪资变动记录，直接用上次工资

                Map<String, String> salaryRecordMap = new HashMap<String, String>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                salaryRecordMap.put("salary",String.valueOf(currentSalary));
                salaryRecordMap.put("startDay",sdf.format(startDate));
                salaryRecordMap.put("endDay",sdf.format(endDate));
                salaryRecordList.add(salaryRecordMap);
            }
        }catch (Exception e) {
            salaryRecordList = null;
            LOGGER.error("查询某个员工在日期范围内的工资调整情况失败", e, e.getMessage());
        }




        return salaryRecordList;
    }

//        public static void main(String[] args){
//            try
//            {
//                Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2016-11-09");
//                Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse("2016-12-09");
//                System.out.println(selectSalaryRecords(16282,date,date2).toString());
//            }
//            catch (Exception e) {
//                LOGGER.error("", e, e.getMessage());
//            }
//
//    }
}
