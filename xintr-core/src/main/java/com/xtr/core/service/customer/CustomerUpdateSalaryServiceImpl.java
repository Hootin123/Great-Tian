package com.xtr.core.service.customer;

import com.alibaba.fastjson.JSONObject;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.domain.customer.CustomersRecordBean;
import com.xtr.api.domain.customer.CustomersStationBean;
import com.xtr.api.domain.customer.LegalHolidayBean;
import com.xtr.api.domain.salary.CustomerPayrollBean;
import com.xtr.api.domain.salary.CustomerSalaryRecordBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.dto.salary.CustomerPayrollDto;
import com.xtr.api.service.customer.CustomerUpdateSalaryService;
import com.xtr.api.service.salary.PayrollAccountService;
import com.xtr.api.util.ExcelUtil;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.util.BigDecimalUtil;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.verify.ValidatorUtils;
import com.xtr.core.persistence.reader.customer.CustomersReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomersStationReaderMapper;
import com.xtr.core.persistence.reader.customer.LegalHolidayReaderMapper;
import com.xtr.core.persistence.reader.salary.CustomerPayrollReaderMapper;
import com.xtr.core.persistence.reader.salary.CustomerSalaryRecordReaderMapper;
import com.xtr.core.persistence.reader.salary.PayCycleReaderMapper;
import com.xtr.core.persistence.writer.customer.CustomersRecordWriterMapper;
import com.xtr.core.persistence.writer.customer.CustomersWriterMapper;
import com.xtr.core.persistence.writer.salary.CustomerSalaryRecordWriterMapper;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Author Xuewu
 * @Date 2016/8/17.
 */
@Service("customerUpdateSalaryService")
public class CustomerUpdateSalaryServiceImpl implements CustomerUpdateSalaryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerUpdateSalaryServiceImpl.class);

    @Resource
    private CustomerSalaryRecordReaderMapper customerSalaryRecordReaderMapper;

    @Resource
    private CustomerSalaryRecordWriterMapper customerSalaryRecordWriterMapper;

    @Resource
    private CustomersReaderMapper customersReaderMapper;

    @Resource
    private CustomersWriterMapper customersWriterMapper;

    @Resource
    private CustomersStationReaderMapper customersStationReaderMapper;

    @Resource
    private LegalHolidayReaderMapper legalHolidayReaderMapper;

    @Resource
    private PayCycleReaderMapper payCycleReaderMapper;

    @Resource
    private CustomersRecordWriterMapper customersRecordWriterMapper;

    @Resource
    private PayrollAccountService payrollAccountService;

    @Resource
    private CustomerPayrollReaderMapper customerPayrollReaderMapper;

    @Override
    public boolean insertSalaryRecord(long customerId, BigDecimal salary, Date date, String reason) throws Exception {
        LOGGER.info("员工调薪：参数【" + customerId + "" + salary + " " + date + " " + reason);
        date = DateUtil.formateDateToYYYYMMDD(date);
        if (!BigDecimalUtil.isGreaterThanZero(salary)) {
            throw new BusinessException("请输入调薪后基本工资");
        }

        if(date == null){
            throw new BusinessException("请选择生效日期");
        }
        int checkTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(date));

        CustomersBean customersBean = customersReaderMapper.selectByPrimaryKey(customerId);

        //调薪生效日期必须大于最新的已审核的计薪周期结束时间
        PayCycleBean checkCycleBean=payCycleReaderMapper.selectLastApprovedInfo(customersBean.getCustomerCompanyId());
        if(checkCycleBean!=null && checkCycleBean.getEndDate()!=null){
            int cycleTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(checkCycleBean.getEndDate()));
            if(checkTimeInt<=cycleTimeInt){
                throw new BusinessException("调薪生效日期必须大于最新的已审核的计薪周期结束时间");
            }
        }

        CustomersStationBean customersStationBean = customersStationReaderMapper.selectByCustomerId(customerId);

        if (customersBean.getCustomerRegularTime() == null) {
            throw new BusinessException("员工未定薪");
        }
        if(customersStationBean!=null && customersStationBean.getStationRegularTime()!=null){
            int regularTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(customersStationBean.getStationRegularTime()));
            if(checkTimeInt<regularTimeInt){
                throw new BusinessException("生效日期不能小于转正日期");
            }
        }
        if(customersStationBean!=null && customersStationBean.getStationEnterTime()!=null){
            int enterTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(customersStationBean.getStationEnterTime()));
            if(checkTimeInt<enterTimeInt){
                throw new BusinessException("生效日期不能小于入职日期");
            }
        }

        PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(customersBean.getCustomerCompanyId());

        if (payCycleBean != null) {
            Date startDate = DateUtil.formateDateToYYYYMMDD(payCycleBean.getStartDate());

            if(date.getTime() < startDate.getTime()) {
                throw new BusinessException("生效日期不能小于计薪起始日");
//                throw new BusinessException("生效日期应在当月计薪开始日（" + DateUtil.date2String(startDate, "yyyy-MM-dd") + "）之后");
            }

        }

        if(customersStationBean.getStationEnterTime() != null) {
            if(date.getTime() < customersStationBean.getStationEnterTime().getTime()) {
                throw new BusinessException("生效日期应在入职日（" + DateUtil.date2String(customersStationBean.getStationEnterTime(), "yyyy-MM-dd") + "）之后");
            }
        }

        if (customersStationBean.getStationCustomerState() > 2)
            throw new BusinessException("员工已离职");

        boolean flag = false;
//        CustomerSalaryRecordBean exists = customerSalaryRecordReaderMapper.selectByCustomerIdAndEffDate(customerId, date);
//        if (exists != null) {//已经存在调薪记录 覆盖
//            exists.setNewSalary(salary);
//            exists.setReason(reason);
//            flag = customerSalaryRecordWriterMapper.updateByPrimaryKey(exists) > 0;
//        } else {
//            exists = new CustomerSalaryRecordBean();
//            exists.setCreateTime(new Date());
//            exists.setCustomerId(customerId);
//            exists.setEffectiveDate(date);
//            exists.setNewSalary(salary);
//            exists.setOldSalary(getLastSalary(customerId, date));
//            exists.setReason(reason);
//            flag = customerSalaryRecordWriterMapper.insert(exists) > 0;
//        }
        //先删除当前时间之后的调薪记录,再新增调薪记录
        Date nowDate=DateUtil.dateFormatter.parse(DateUtil.dateFormatter.format(new Date()));
        customerSalaryRecordWriterMapper.deleteByUpdateSalary(customerId,  nowDate);
        //添加调薪记录
        CustomerSalaryRecordBean exists = new CustomerSalaryRecordBean();
        exists.setCreateTime(new Date());
        exists.setCustomerId(customerId);
        exists.setEffectiveDate(date);
        exists.setNewSalary(salary);
        exists.setOldSalary(getLastSalary(customerId, date));
        exists.setReason(reason);
        flag = customerSalaryRecordWriterMapper.insert(exists) > 0;
        if (flag) {

//            //修改下一条记录 的oldSalary
//            CustomerSalaryRecordBean nextRecord = customerSalaryRecordReaderMapper.selectFirstRecordByDate(customerId, date);
//            if (nextRecord != null) {
//                nextRecord.setOldSalary(salary);
//                customerSalaryRecordWriterMapper.updateByPrimaryKey(nextRecord);
//            }

            //调薪是否在当前周期，是的话就更新员工的基本薪资
            if (payCycleBean != null) {
                Date startDate = DateUtil.formateDateToYYYYMMDD(payCycleBean.getStartDate());
                Date endDate = DateUtil.formateDateToYYYYMMDD(payCycleBean.getEndDate());
                if (date.getTime() >= startDate.getTime() && date.getTime() <= endDate.getTime()) {
                    //计算工资
                    calculateSalary(customersBean, payCycleBean, null);
                    //更新工资单
                    updatePayRoll(customersBean);
                }
            }

            //增加历史记录
            CustomersRecordBean customersRecordBean = new CustomersRecordBean();
            customersRecordBean.setRecordCustomerId(customerId);
            customersRecordBean.setRecordOperationTime(new Date());
            customersRecordBean.setRecordOperationType(6);
            customersRecordBean.setRecordCreateTime(date);
            customersRecordBean.setRecordSalaryBefore(exists.getOldSalary());
            customersRecordBean.setRecordSalaryAfter(exists.getNewSalary());
            customersRecordWriterMapper.insert(customersRecordBean);
        }
        return flag;
    }

    public BigDecimal getLastSalary(long customerId, Date date) {

        date = DateUtil.formateDateToYYYYMMDD(date);

        CustomersBean customersBean = customersReaderMapper.selectByPrimaryKey(customerId);
        CustomersStationBean customersStationBean = customersStationReaderMapper.selectByCustomerId(customerId);

        if(customersBean.getCustomerRegularTime() == null) {//员工未定薪
            return new BigDecimal(0);
        }

        TreeMap<Date, BigDecimal> orderMap = new TreeMap<>();
        //todo 如果试用期工资没有？
        orderMap.put(customersStationBean.getStationEnterTime(), customersBean.getCustomerProbationSalary());

        if (customersStationBean.getStationRegularTime() != null) {
            Date regularDate = DateUtil.formateDateToYYYYMMDD(customersStationBean.getStationRegularTime());
            if (regularDate.getTime() <= date.getTime()) {//员工已转正
                orderMap.put(regularDate, customersBean.getCustomerRegularSalary());
            }
        }
        try{
//            Date nowDate=DateUtil.dateFormatter.parse(DateUtil.dateFormatter.format(new Date()));
//            CustomerSalaryRecordBean lastRecord = customerSalaryRecordReaderMapper.selectLastRecordByDate(customerId, nowDate);
//            if (lastRecord != null) {//有调薪记录
//                orderMap.put(lastRecord.getEffectiveDate(), lastRecord.getNewSalary());
//            }
                    CustomerSalaryRecordBean lastRecord = customerSalaryRecordReaderMapper.selectLastRecordByDate(customerId, date);
        if (lastRecord != null) {//有调薪记录
            orderMap.put(lastRecord.getEffectiveDate(), lastRecord.getNewSalary());
        }
        }catch(Exception e){
            LOGGER.error("获取调薪基础数据失败",e);
        }
//        CustomerSalaryRecordBean lastRecord = customerSalaryRecordReaderMapper.selectLastRecordByDate(customerId, date);
//        if (lastRecord != null) {//有调薪记录
//            orderMap.put(lastRecord.getEffectiveDate(), lastRecord.getNewSalary());
//        }

        return orderMap.lastEntry().getValue();
    }


    public void calulateSalaryByCompanyId(long companyId) throws BusinessException{
        List<CustomersBean> customersBeans = customersReaderMapper.selectUsefulCustByCompany(companyId);
        if(customersBeans != null) {
            PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(companyId);
            List<Date> workDays = calculateWorkDay(payCycleBean.getStartDate(), payCycleBean.getEndDate());
            for (CustomersBean bean : customersBeans) {
                try {
                    calculateSalary(bean, payCycleBean, workDays);
                } catch (Exception e) {
                    LOGGER.error("批量计算公司员工失败，员工：" + JSONObject.toJSONString(bean), e, e.getMessage());
                }
            }
        }

    }


    public CustomersBean calculateSalary(Long customerId) throws BusinessException{
        CustomersBean customersBean = customersReaderMapper.selectByPrimaryKey(customerId);
        return calculateSalary(customersBean, null, null);
    }


    public CustomersBean calculateSalary(CustomersBean customersBean, PayCycleBean payCycleBean, List<Date> workDays) throws BusinessException {
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(DateUtil.formateDateToYYYYMMDD(new Date()));


        Long customerId = customersBean.getCustomerId();

        CustomersStationBean customersStationBean = customersStationReaderMapper.selectByCustomerId(customerId);

        if(customersBean.getCustomerRegularTime() == null){//员工未定薪
            //更新当前月工资
            return null;
        }

        if(payCycleBean == null)
            payCycleBean = payCycleReaderMapper.selectByCompanyId(customersBean.getCustomerCompanyId());
        if(payCycleBean == null)
            throw new BusinessException("没有最新周期数据");

        Date startDate = DateUtil.formateDateToYYYYMMDD(payCycleBean.getStartDate());
        Date endDate = DateUtil.formateDateToYYYYMMDD(payCycleBean.getEndDate());
        Date enterDate = DateUtil.formateDateToYYYYMMDD(customersStationBean.getStationEnterTime());

        if(enterDate.getTime() > endDate.getTime()){
            //员工未入职
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

        if(workDays == null)
            workDays = calculateWorkDay(startDate, endDate);

        BigDecimal currentSalary = null;
        //获取当前周期开始日最新工资
        if(enterDate.getTime() >= startDate.getTime()){
            currentSalary = getLastSalary(customerId, enterDate);
        }else{
            currentSalary = getLastSalary(customerId, startDate);
        }

        BigDecimal sum = new BigDecimal(0d);

        if (changeList.size() > 0) {

            int lastCount = 0;
            int workDayCount = workDays.size();
            Calendar startCalendar = Calendar.getInstance();

            for (startCalendar.setTime(startDate); startCalendar.getTime().getTime() <= endDate.getTime(); startCalendar.add(Calendar.DATE, 1)) {
                Date date = startCalendar.getTime();
                BigDecimal nextSalary = changeList.get(date);
                if (nextSalary != null && nextSalary.compareTo(currentSalary) != 0) {
                    if (lastCount != 0) {


                        if (workDayCount == lastCount){
                            sum = sum.add(currentSalary);
                        }
                        else{
                            BigDecimal result = (currentSalary.divide(new BigDecimal(workDayCount), 2,BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(lastCount));
                            sum = sum.add(result);
                        }

                        lastCount = 0;
                    }
                    currentSalary = changeList.get(date);
                }
                if (workDays.contains(date)) {
                    lastCount++;
                }
            }

            if (workDayCount == lastCount) {
                sum = currentSalary;
//                LOGGER.error(currentSalary+",,"+enterDate+",");

            } else {
//                LOGGER.error(currentSalary+",,"+enterDate+",");

                BigDecimal result = (currentSalary.divide(new BigDecimal(workDayCount), 2,BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(lastCount));
                sum = sum.add(result);
            }
        } else {//没有薪资变动记录，直接用上次工资
            sum = currentSalary;
        }

        //更新当前月工资
        CustomersBean updateBean = new CustomersBean();
        updateBean.setCustomerId(customerId);
        updateBean.setCustomerCurrentSalary(sum);
        customersWriterMapper.updateByPrimaryKeySelective(updateBean);
        customersBean.setCustomerCurrentSalary(sum);
        return customersBean;
    }



    public List<Date> calculateWorkDay(Date startDate, Date endDate) throws BusinessException {
        startDate = DateUtil.formateDateToYYYYMMDD(startDate);
        endDate = DateUtil.formateDateToYYYYMMDD(endDate);

        Map<String, LegalHolidayBean> cachedMonthHoliday = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar startCalendar = Calendar.getInstance();

        List<Date> workDay = new ArrayList<>();

        for (startCalendar.setTime(startDate); startCalendar.getTime().getTime() <= endDate.getTime(); startCalendar.add(Calendar.DATE, 1)) {
            int year = startCalendar.get(Calendar.YEAR);
            int month = startCalendar.get(Calendar.MONTH) + 1;

            String cacheKey = year + "-" + month;

            LegalHolidayBean legalHolidayBean = cachedMonthHoliday.get(cacheKey);
            if (legalHolidayBean == null) {
                legalHolidayBean = legalHolidayReaderMapper.selectByYearAndMonth(year, month);
                cachedMonthHoliday.put(cacheKey, legalHolidayBean);
            }
            if (legalHolidayBean == null)
                throw new BusinessException("节假日信息为空，工作日无法计算");

            String[] holidays = legalHolidayBean.getLegalHoliday().split(",");

            if (!ArrayUtils.contains(holidays, startCalendar.get(Calendar.DAY_OF_MONTH) + "")) {
                Date time = startCalendar.getTime();
                workDay.add(time);
            }

        }

        return workDay;
    }

    /**
     * 根据计薪周期计算应出勤天数
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws BusinessException
     */
    public int calculateWorkNumberDay(Date startDate, Date endDate) throws BusinessException {
        return calculateWorkDay(startDate, endDate).size();
    }


    @Override
    @Transactional
    public CustomersBean setCustomerSalary(long customerId, BigDecimal probationSalary, BigDecimal regularSalary) throws Exception {
        LOGGER.info("员工定薪：参数【" + customerId + "" + probationSalary + " " + regularSalary);
        CustomersBean customersBean = customersReaderMapper.selectByPrimaryKey(customerId);

        if (customersBean == null) {
            throw new BusinessException("员工信息不存在");
        }

//        if (customersBean.getCustomerRegularTime() != null) {
//            throw new BusinessException("员工已定薪");
//        }

        CustomersStationBean customersStationBean = customersStationReaderMapper.selectByCustomerId(customerId);
        Integer stationCustomerState = customersStationBean.getStationCustomerState();
        BigDecimal currentSalary = new BigDecimal(0);

        if (stationCustomerState == 1) {//试用
            if(probationSalary == null){
                throw new BusinessException("请输入试用期工资");
            }else if (!BigDecimalUtil.isGreaterThanZero(probationSalary)) {
                throw new BusinessException("试用期工资输入有误");
            }

            if (regularSalary == null) {
                //f
                regularSalary = probationSalary;
            } else {
                if (!BigDecimalUtil.isGreaterThanZero(regularSalary)) {
                    throw new BusinessException("转正后工资输入有误");
                }
            }


        } else if (stationCustomerState == 2) {//正式
            if(regularSalary == null){
                throw new BusinessException("请输入转正后工资");
            } else if (!BigDecimalUtil.isGreaterThanZero(regularSalary)) {
                throw new BusinessException("转正后工资输入有误");
            }
            if (probationSalary != null) {
                if (!BigDecimalUtil.isGreaterThanZero(probationSalary)) {
                    throw new BusinessException("试用期工资输入有误");
                }
            } else {
                probationSalary = regularSalary;
            }


        } else if (stationCustomerState == 3 || stationCustomerState == 4) {
            throw new BusinessException("员工已离职");
        }

        if (customersWriterMapper.setCustomersSalary(customerId, currentSalary, probationSalary, regularSalary) <= 0) {
            throw new BusinessException("操作失败");
        }

        customersBean.setCustomerCurrentSalary(currentSalary);
        customersBean.setCustomerProbationSalary(probationSalary);
        customersBean.setCustomerRegularSalary(regularSalary);
        customersBean.setCustomerRegularTime(new Date());

        //计算基本工资
        customersBean = calculateSalary(customersBean, null, null);

        //更新工资单
//        updatePayRoll(customersBean);

        return customersBean;
    }

    @Override
    public Map<String, String> setSalaryBatch(List<Object[]> dataFromExcel, boolean doImport, long companyId) {
        Map<String, String> msg = new LinkedHashMap<>();
        List<Object[]> userfulData = new ArrayList<>();
        for (int row = 0; row < dataFromExcel.size(); row++) {
            Object[] objects = dataFromExcel.get(row);
            if (objects[0] == null && objects[1] == null) {
                continue;
            }

            if(ExcelUtil.isBlankRow(objects)) {
                continue;
            }

            boolean phoneFlag, currentSalaryFlag, regularSalaryFlag;
            phoneFlag = currentSalaryFlag = regularSalaryFlag = false;

            BigDecimal currentSalary = null, regularSalary = null;
            CustomersStationBean customersStationBean = null;

            //手机格式检查 必填
            String phone = String.valueOf(objects[0]);
            if ("null".equals(phone)|| StringUtils.isBlank(phone)) {
                msg.put(row + ",0", "手机不能为空");
            } else {
                String regexPhone = "^1[3|4|5|7|8]\\d{9}$";
                if (!Pattern.matches(regexPhone, phone)) {
                    msg.put(row + ",0", "手机号格式错误");
                } else {
                    phoneFlag = true;
                }
            }


            //本月基本工资格式检查  必填
            String currentSalaryStr = String.valueOf(objects[1]);
            if("null".equals(currentSalaryStr) || StringUtils.isBlank(currentSalaryStr)){
                msg.put(row + ",1", "请输入本月基本工资");
            }else if (!BigDecimalUtil.isMoney(currentSalaryStr)) {
                msg.put(row + ",1", "本月基本工资输入有误");
            } else {
                currentSalaryFlag = true;
                currentSalary = new BigDecimal(currentSalaryStr).setScale(2);
            }

            //转正月基本工资格式检查 非必填
            String regularSalaryStr = String.valueOf(objects[2]);
            if("null".equals(regularSalaryStr) || StringUtils.isBlank(regularSalaryStr)){
                regularSalaryFlag = true;
                regularSalary = currentSalary;
            }else if (!BigDecimalUtil.isMoney(regularSalaryStr)) {
                msg.put(row + ",2", "转正后基本工资输入有误");
            } else {
                regularSalaryFlag = true;
                regularSalary = new BigDecimal(regularSalaryStr).setScale(2);
            }


            //验证员工状态
            if (phoneFlag) {
                CustomersBean customersBean = customersReaderMapper.selectByPhoneAndCompanyId(phone, companyId);
                if (customersBean == null) {
                    phoneFlag = false;
                    msg.put(row + ",0", "手机号不存在");
                } else if (customersBean.getCustomerRegularTime() != null) {
                    phoneFlag = false;
                    msg.put(row + ",0", "员工已定薪");
                } else {
                    customersStationBean = customersStationReaderMapper.selectByCustomerId(customersBean.getCustomerId());
                    if (customersStationBean.getStationCustomerState() > 2) {
                        phoneFlag = false;
                        msg.put(row + ",0", "员工已离职");
                    }
                }
            }

            //员工已转正 验证转正后工资和当前工资
            if (phoneFlag && currentSalaryFlag && regularSalaryFlag && regularSalary != null && customersStationBean.getStationCustomerState() == 2) {
                if (!regularSalary.equals(currentSalary)) {
                    regularSalaryFlag = false;
                    msg.put(row + ",2", "员工已转正，转正后工资为当月基本工资");
                }
            }

            if (phoneFlag && currentSalaryFlag && regularSalaryFlag && doImport) {
                userfulData.add(new Object[]{customersStationBean, currentSalary, regularSalary, row});
            }

        }

        if (msg.size() == 0 && doImport) {
            for (Object[] objects : userfulData) {
                CustomersStationBean customersStationBean = (CustomersStationBean) objects[0];
                BigDecimal currentSalary = (BigDecimal) objects[1];
                BigDecimal regularSalary = (BigDecimal) objects[2];
                int row = (int) objects[3];

                LOGGER.info("批量定薪 导入数据 row:" + row + " customersStationBean：" + customersStationBean);
                try {
                    CustomersBean customersBean = null;
                    if (customersStationBean.getStationCustomerState() == 1) {
                        customersBean = setCustomerSalary(customersStationBean.getStationCustomerId(), currentSalary, regularSalary);
                    } else {
                        customersBean = setCustomerSalary(customersStationBean.getStationCustomerId(), regularSalary, currentSalary);
                    }
                    if(customersBean != null)
                        updatePayRoll(customersBean);

                } catch (BusinessException e) {
                    LOGGER.error("批量定薪导入失败", e.getMessage(), e);
                    msg.put(row + ",0", e.getMessage());
                } catch (Exception e) {
                    LOGGER.error("批量定薪导入失败", e.getMessage(), e);
                    msg.put(row + ",0", "定薪失败");
                }

            }
        }


        LOGGER.info("批量定薪 验证消息：" + JSONObject.toJSONString(msg));
        return msg;
    }

    /**
     * 批量调薪
     * @param dataFromExcel
     * @param doImplort
     * @param companyId
     * @return
     */
    @Override
    public Map<String, String> updateSalaryBatch(List<Object[]> dataFromExcel, boolean doImplort, long companyId) {
        Map<String, String> msg = new LinkedHashMap<>();
        List<Object[]> usefulData = new ArrayList<>();

        for (int row = 0; row < dataFromExcel.size(); row++) {
            Object[] objects = dataFromExcel.get(row);
            if (objects[0] == null && objects[1] == null && objects[2] == null) {
                continue;
            }

            if(ExcelUtil.isBlankRow(objects)) {
                continue;
            }

            boolean phoneFlag, effDateFlag, salaryFlag, reasonFlag;
            phoneFlag = effDateFlag = salaryFlag = reasonFlag = false;

            BigDecimal salary = null;
            CustomersStationBean customersStationBean = null;
            CustomersBean customersBean = null;

            //手机格式检查 必填
            String phone = String.valueOf(objects[0]);
            if ("null".equals(phone)|| StringUtils.isBlank(phone)) {
                msg.put(row + ",0", "手机不能为空");
            } else {
                String regexPhone = "^1[3|4|5|7|8]\\d{9}$";
                String str = phone.trim();
                if (!Pattern.matches(regexPhone, phone)) {
                    msg.put(row + ",0", "手机号格式错误");
                } else {
                    phoneFlag = true;
                }
            }


            //生效日期 必填
            Date effDate = DateUtil.formatAllStyle(String.valueOf(objects[1]));
            if (effDate == null) {
                msg.put(row + ",1", "生效日期不能为空");
            } else {
                effDateFlag = true;
            }

            //调薪后工资 必填
            String currentStr = String.valueOf(objects[2]);
            if (!BigDecimalUtil.isMoney(currentStr)) {
                msg.put(row + ",2", "调薪后工资输入有误");
            } else {
                salaryFlag = true;
                salary = new BigDecimal(currentStr).setScale(2);
            }

            String reason =  String.valueOf(objects[3]);
            if("null".equals(reason) || StringUtils.isBlank(reason)){
                reasonFlag = true;
                reason = "";
            }else if (reason != null && reason.length() > 20) {
                msg.put(row + ",3", "调薪原因过长，不能超过20个字符");
            } else {
                reasonFlag = true;
            }


            //验证员工状态
            if (phoneFlag) {
                customersBean = customersReaderMapper.selectByPhoneAndCompanyId(phone, companyId);
                if (customersBean == null) {
                    phoneFlag = false;
                    msg.put(row + ",0", "手机号不存在");
                } else if (customersBean.getCustomerRegularTime() == null) {
                    phoneFlag = false;
                    msg.put(row + ",0", "员工未定薪");
                } else {
                    customersStationBean = customersStationReaderMapper.selectByCustomerId(customersBean.getCustomerId());
                    if (customersStationBean.getStationCustomerState() > 2) {
                        phoneFlag = false;
                        msg.put(row + ",0", "员工已离职");
                    }
                }
            }

            if(phoneFlag && effDateFlag) {
                PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(customersBean.getCustomerCompanyId());

                if (payCycleBean != null) {
                    Date startDate = DateUtil.formateDateToYYYYMMDD(payCycleBean.getStartDate());

                    if(effDate.getTime() < startDate.getTime()) {
                        msg.put(row + ",1", "生效日期应在当月计薪开始日（" + DateUtil.date2String(startDate, "yyyy-MM-dd") + "）之后");
                        effDateFlag = false;
                    }

                }

                if(customersStationBean.getStationEnterTime() != null) {
                    if(effDate.getTime() < customersStationBean.getStationEnterTime().getTime()) {
                        msg.put(row + ",1", "生效日期应在入职日（" + DateUtil.date2String(customersStationBean.getStationEnterTime(), "yyyy-MM-dd") + "）之后");
                        effDateFlag = false;
                    }
                }
            }

            if (phoneFlag && effDateFlag && salaryFlag && reasonFlag && doImplort) {
                usefulData.add(new Object[]{customersStationBean, salary, effDate, reason, row});
            }
        }

        if (msg.size() == 0 && doImplort) {
            for (Object[] objects : usefulData) {
                CustomersStationBean customersStationBean = (CustomersStationBean) objects[0];
                BigDecimal salary = (BigDecimal) objects[1];
                Date effDate = (Date) objects[2];
                String reason = (String) objects[3];
                int row = (int) objects[4];
                try {
                    insertSalaryRecord(customersStationBean.getStationCustomerId(), salary, effDate, reason);
                }catch (BusinessException e){
                    LOGGER.error("批量调薪异常", e.getMessage(), e);
                    msg.put(row + ",0", e.getMessage());
                } catch (Exception e) {
                    LOGGER.error("批量调薪异常", e.getMessage(), e);
                    msg.put(row + ",0", "调薪失败");
                }
            }
        }

        LOGGER.info("批量调薪薪 验证消息：" + JSONObject.toJSONString(msg));
        return msg;
    }

    /**
     * 批量设置 工资明细
     * @param dataFromExcel
     * @param doImport
     * @param companyId
     * @return
     */
    @Override
    public Map<String, String> updatePreTaxAfterTaxBatch(List<Object[]> dataFromExcel, boolean doImport, long companyId) {
        Map<String, String> msg = new LinkedHashMap<>();
        List<Object[]> usefulData = new ArrayList<>();

        for (int row = 0; row < dataFromExcel.size(); row++) {
            Object[] objects = dataFromExcel.get(row);
            if (objects[0] == null) {
                continue;
            }

            if(ExcelUtil.isBlankRow(objects)) {
                continue;
            }

            boolean phoneFlag, preTaxFlag, afterTaxFlag, sbFlag, gjjFlag;
            phoneFlag = preTaxFlag = afterTaxFlag = sbFlag = gjjFlag = false;

            BigDecimal preTax = new BigDecimal(0), afterTax = new BigDecimal(0), sb = new BigDecimal(0), gjj = new BigDecimal(0);

            //手机格式检查
            String phone = String.valueOf(objects[0]);
            if ("null".equals(phone) || StringUtils.isBlank(phone)) {
                msg.put(row + ",0", "手机不能为空");
            } else {
                String regexPhone = "^1[3|4|5|7|8]\\d{9}$";
                String str = phone.trim();
                if (!Pattern.matches(regexPhone, phone)) {
                    msg.put(row + ",0", "手机号格式错误");
                } else {
                    phoneFlag = true;
                }
            }


            //税前补发/扣款
            String preTaxStr = String.valueOf(objects[1]);

            if("null".equals(preTaxStr) || StringUtils.isBlank(preTaxStr)){
                preTaxFlag = true;
            }else if (!ValidatorUtils.isNumeric(preTaxStr)) {
                msg.put(row + ",1", "税前补发/扣款 输入有误");
            } else {
                preTaxFlag = true;
                preTax = new BigDecimal(preTaxStr).setScale(2);
            }

            //税前补发/扣款
            String afterTaxStr = String.valueOf(objects[2]);
            if("null".equals(afterTaxStr) || StringUtils.isBlank(afterTaxStr)){
                afterTaxFlag = true;
            }else if (!ValidatorUtils.isNumeric(afterTaxStr)) {
                msg.put(row + ",2", "税后补发/扣款 输入有误");
            } else {
                afterTaxFlag = true;
                afterTax = new BigDecimal(afterTaxStr).setScale(2);
            }

            //社保
            String sbStr = String.valueOf(objects[3]);
            if("null".equals(sbStr) || StringUtils.isBlank(sbStr)){
                sbFlag = true;
            }else if (!ValidatorUtils.isNumeric(sbStr)) {
                msg.put(row + ",3", "社保 输入有误");
            } else {
                sbFlag = true;
                sb = new BigDecimal(sbStr).setScale(2);
            }

            //公积金
            String gjjStr = String.valueOf(objects[4]);
            if("null".equals(gjjStr) || StringUtils.isBlank(gjjStr)){
                gjjFlag = true;
            }else if (!ValidatorUtils.isNumeric(gjjStr)) {
                msg.put(row + ",4", "公积金 输入有误");
            } else {
                gjjFlag = true;
                gjj = new BigDecimal(gjjStr).setScale(2);
            }


            CustomersBean customersBean = null;
            //验证员工状态
            if (phoneFlag) {
//                todo 员工状态
                customersBean = customersReaderMapper.selectByPhoneAndCompanyId(phone, companyId);
                if (customersBean == null) {
                    phoneFlag = false;
                    msg.put(row + ",0", "手机号不存在");
                }
            }

            if (phoneFlag && preTaxFlag && afterTaxFlag && sbFlag && gjjFlag && doImport) {
                CustomersStationBean customersStationBean = customersStationReaderMapper.selectByCustomerId(customersBean.getCustomerId());
                usefulData.add(new Object[]{customersStationBean, preTax, afterTax, sb, gjj, row});
            }
        }

        if (msg.size() == 0 && doImport) {
            CustomerPayrollDto dto = new CustomerPayrollDto();
            for (Object[] objects : usefulData) {
                CustomersStationBean customersStationBean = (CustomersStationBean) objects[0];
                BigDecimal preTax = (BigDecimal) objects[1];
                BigDecimal afterTax = (BigDecimal) objects[2];
                BigDecimal sb = (BigDecimal) objects[3];
                BigDecimal gjj = (BigDecimal) objects[4];
                int row = (int) objects[5];
                try {
                    updateTax(customersStationBean.getStationCustomerId(), preTax, afterTax, sb, gjj, dto);
                }catch (BusinessException e){
                    LOGGER.error("批量设置工资明细", e.getMessage(), e);
                    msg.put(row + ",0", e.getMessage());
                }catch (Exception e){
                    LOGGER.error("批量设置工资明细异常", e.getMessage(), e);
                    msg.put(row + ",0", "设置工资明细失败");
                }
            }
        }

        LOGGER.info("批量补充工资明细 验证消息：" + JSONObject.toJSONString(msg));
        return msg;
    }

    /**
     * 设置考勤  dto需要设置 考勤天数
     * @param customerId
     * @param dto
     */
    private void updateAbsenceDay(long customerId, CustomerPayrollDto dto) throws Exception {
        CustomersBean customersBean = customersReaderMapper.selectByPrimaryKey(customerId);
        PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(customersBean.getCustomerCompanyId());
        if(payCycleBean == null)
            throw new BusinessException("没有最新周期数据");
        CustomerPayrollBean customerPayrollBean = customerPayrollReaderMapper.selectByCustomerId(customerId, payCycleBean.getId());
        dto.setId(customerPayrollBean.getId());
        payrollAccountService.updateBasePay(dto, 0);
    }

    /**
     * 批量设置考勤
     * @param dataFromExcel
     * @param doImport
     * @param companyId
     * @return
     */
    @Override
    public Map<String, String> updateAbsenceDayBatch(List<Object[]> dataFromExcel, boolean doImport, long companyId) {
        Map<String, String> msg = new LinkedHashMap<>();
        List<Object[]> usefulData = new ArrayList<>();

        for (int row = 0; row < dataFromExcel.size(); row++) {
            Object[] objects = dataFromExcel.get(row);
            if (objects[0] == null && objects[1] == null) {
                continue;
            }

            if(ExcelUtil.isBlankRow(objects)) {
                continue;
            }

            boolean phoneFlag = false, daysFlag = false;

            BigDecimal days = new BigDecimal(0);


            //手机格式检查
            String phone = String.valueOf(objects[0]);
            if ("null".equals(phone) || StringUtils.isBlank(phone)) {
                msg.put(row + ",0", "手机不能为空");
            } else {
                String regexPhone = "^1[3|4|5|7|8]\\d{9}$";
                String str = phone.trim();
                if (!Pattern.matches(regexPhone, phone)) {
                    msg.put(row + ",0", "手机号格式错误");
                } else {
                    phoneFlag = true;
                }
            }

            //缺勤天数
            String dayStr = String.valueOf(objects[1]);
            if("null".equals(dayStr) || StringUtils.isBlank(dayStr)){
                msg.put(row + ",1", "缺勤天数不能为空");
            }else if(NumberUtils.isNumber(dayStr) && Double.valueOf(dayStr) >= 0D && Double.valueOf(dayStr) <= 100D){
                daysFlag = true;
                days = new BigDecimal(dayStr).setScale(1);
            }else{
                msg.put(row + ",1", "请输入0-100之间的数字，支持一位小数");
            }


            //验证员工状态
            if (phoneFlag) {
                CustomersBean customersBean = customersReaderMapper.selectByPhoneAndCompanyId(phone, companyId);
                if (customersBean == null) {
                    phoneFlag = false;
                    msg.put(row + ",0", "手机号不存在");
                }
            }

            if(daysFlag) {
                if(days.doubleValue() > 100 || days.doubleValue() < 0) {
                    daysFlag = false;
                    msg.put(row + ",1", "数值应在0~100之间");
                }
            }

            if (phoneFlag && daysFlag && doImport) {
                CustomersBean customersBean = customersReaderMapper.selectByPhoneAndCompanyId(phone, companyId);
                CustomersStationBean customersStationBean = customersStationReaderMapper.selectByCustomerId(customersBean.getCustomerId());
                usefulData.add(new Object[]{customersStationBean, days, row});
            }
        }

        if (msg.size() == 0 && doImport) {
            CustomerPayrollDto customerPayrollDto = new CustomerPayrollDto();
            for (Object[] objects : usefulData) {
                CustomersStationBean customersStationBean = (CustomersStationBean) objects[0];
                BigDecimal days = (BigDecimal) objects[1];
                int row = (int) objects[2];
                try {
                    customerPayrollDto.setAbsenceDayNumber(days);
                    updateAbsenceDay(customersStationBean.getStationCustomerId(), customerPayrollDto);
                }catch (BusinessException e){
                    LOGGER.error("批量设置考勤异常", e.getMessage(), e);
                    msg.put(row + ",0", e.getMessage());
                }catch (Exception e){
                    LOGGER.error("批量设置考勤异常", e.getMessage(), e);
                    msg.put(row + ",0", "设置考勤失败");
                }
            }
        }

        LOGGER.info("批量补充工资明细 验证消息：" + JSONObject.toJSONString(msg));
        return msg;
    }

    /**
     * 更新工资单明细
     * @param customerId
     * @param preTax
     * @param afterTax
     * @param sb
     * @param gjj
     * @param dto
     */
    private void updateTax(long customerId, BigDecimal preTax, BigDecimal afterTax, BigDecimal sb, BigDecimal gjj, CustomerPayrollDto dto){
        CustomersBean customersBean = customersReaderMapper.selectByPrimaryKey(customerId);
        PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(customersBean.getCustomerCompanyId());
        if(payCycleBean == null)
            throw new BusinessException("没有最新周期数据");
        CustomerPayrollBean customerPayrollBean = customerPayrollReaderMapper.selectByCustomerId(customerId, payCycleBean.getId());

        dto.setId(customerPayrollBean.getId());
        dto.setPretax(preTax);
        dto.setAfterTax(afterTax);
        dto.setSocialSecurity(sb);
        dto.setFund(gjj);

        payrollAccountService.updateBasePay(dto);
    }

    /**
     * 更新员工最后一条工资单
     * @param customerId
     */
    public void updatePayRoll(long customerId) throws Exception {
        CustomersBean customersBean = new CustomersBean();
        customersBean.setCustomerId(customerId);
        updatePayRoll(customersBean);
    }


    /**
     * 更新员工最后一条工资单
     * @param customersBean
     */
    public void updatePayRoll(CustomersBean customersBean) throws Exception {
        PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(customersBean.getCustomerCompanyId());
        if(payCycleBean == null)
            return;
        CustomerPayrollBean customerPayrollBean = customerPayrollReaderMapper.selectByCustomerId(customersBean.getCustomerId(), payCycleBean.getId());
        if(customerPayrollBean == null)
            return;
        CustomerPayrollDto dto = new CustomerPayrollDto();
        dto.setId(customerPayrollBean.getId());
        payrollAccountService.updateBasePay(dto, 100);
    }


//    public static void main(String[] args){
//        System.out.println(new BigDecimal(5000).divide(new BigDecimal(21.75),5,BigDecimal.ROUND_HALF_UP));
//    }
}
