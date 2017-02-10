package com.xtr.core.service.salary;

import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.service.salary.PayCycleService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.util.DateUtil;
import com.xtr.core.persistence.reader.salary.PayCycleReaderMapper;
import com.xtr.core.persistence.writer.salary.PayCycleWriterMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>计薪周期</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/19 9:55
 */
@Service("payCycleService")
public class PayCycleServiceImpl implements PayCycleService {

    @Resource
    private PayCycleWriterMapper payCycleWriterMapper;

    @Resource
    private PayCycleReaderMapper payCycleReaderMapper;

    /**
     * 新写入数据库记录,pay_cycle
     *
     * @param record
     */
    public int insert(PayCycleBean record) {
        return payCycleWriterMapper.insert(record);
    }

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,pay_cycle
     *
     * @param record
     */
    public int updateByPrimaryKeySelective(PayCycleBean record) {
        return payCycleWriterMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据指定主键获取一条数据库记录,pay_cycle
     *
     * @param id
     */
    public PayCycleBean selectByPrimaryKey(Long id) {
        return payCycleReaderMapper.selectByPrimaryKey(id);
    }

    /**
     * 获取未生成工资单的计薪周期
     *
     * @return
     */
    public List<PayCycleBean> getPayCycleBy() {
        return payCycleReaderMapper.selectPayCycle();
    }

    /**
     * 根据公司Id查询计薪周期表
     *
     * @param companyId
     * @return
     */
    public PayCycleBean selectByCompanyId(Long companyId) {
        return payCycleReaderMapper.selectByCompanyId(companyId);
    }

    /**
     * 初始化计薪周期
     *
     * @param yearMonth
     * @param startDay
     * @param companyId
     * @param payWay
     * @param isSocialSecurity
     * @return
     */
    public int initPayCycle(String yearMonth, String startDay, Long companyId, String payWay, String isSocialSecurity) {
        if (StringUtils.isBlank(startDay)) {
            throw new BusinessException("计薪开始日期不能为空");
        }
        if (StringUtils.isBlank(yearMonth)) {
            throw new BusinessException("计薪开始年月不能为空");
        }
        startDay = Integer.valueOf(startDay) < 10 ? "0" + startDay : startDay;
        String startDate = yearMonth + "-" + startDay;
        String endDate = DateUtil.formatDate(getCalcSalary(DateUtil.stringToDate(startDate, DateUtil.DATEYEARFORMATTER)));
        return initPayCycle(yearMonth, startDate, endDate, companyId);
    }

    /**
     * 初始化计薪周期
     *
     * @param yearMonth
     * @param startDate
     * @param endDate
     * @param companyId
     * @return
     */
    public int initPayCycle(String yearMonth, String startDate, String endDate, Long companyId) {
        if (StringUtils.isBlank(yearMonth)) {
            throw new BusinessException("开始计薪工资日期不能为空");
        }
        if (StringUtils.isBlank(startDate)) {
            throw new BusinessException("计薪开始日期不能为空");
        }
        if (StringUtils.isBlank(endDate)) {
            throw new BusinessException("计薪结束日期不能为空");
        }
        PayCycleBean payCycleBean = new PayCycleBean();
        //公司主键
        payCycleBean.setCompanyId(companyId);
        //是否生成工资单 0:未生成 1:已生成
        payCycleBean.setIsGeneratePayroll(Integer.valueOf(0));
        //开始日期
        payCycleBean.setStartDate(DateUtil.stringToDate(startDate, DateUtil.DATEYEARFORMATTER));
        //结束日期
        payCycleBean.setEndDate(DateUtil.stringToDate(endDate, DateUtil.DATEYEARFORMATTER));
        //当前计薪周期
        payCycleBean.setCurrentPayCycle(DateUtil.dateToString(payCycleBean.getStartDate(), DateUtil.DATEYEARFORMATTER) + "~" + DateUtil.dateToString(payCycleBean.getEndDate(), DateUtil.DATEYEARFORMATTER));
        //年度
        payCycleBean.setYear(yearMonth.substring(0, 4));
        //月份
        if (yearMonth.length() == 6) {
            payCycleBean.setMonth(yearMonth.substring(5, 6));
        } else {
            payCycleBean.setMonth(yearMonth.substring(5, 7));
        }
        //当月工资是否已发放 0:未发放 1:待发放  2:发放中  3:已发放
        payCycleBean.setIsPayOff(Integer.valueOf(0));
        //工资单审批状态: 0:未审批  1:审批通过
        payCycleBean.setApprovalState(Integer.valueOf(0));
        //新增计薪周期
        return payCycleWriterMapper.insertSelective(payCycleBean);
    }

    /**
     * 生成下月计薪周期
     *
     * @param payCycleBean
     */
    public int generatePayCycle(PayCycleBean payCycleBean) {
        PayCycleBean payCycleBean1 = new PayCycleBean();
        //公司主键
        payCycleBean1.setCompanyId(payCycleBean.getCompanyId());
        //是否生成工资单 0:未生成 1:已生成
        payCycleBean1.setIsGeneratePayroll(Integer.valueOf(0));
        //开始日期
        payCycleBean1.setStartDate(DateUtil.addDate(payCycleBean.getEndDate(), 1));
        //结束日期
        payCycleBean1.setEndDate(getCalcSalary(payCycleBean1.getStartDate()));
//        //当前计薪周期
        payCycleBean1.setCurrentPayCycle(DateUtil.dateToString(payCycleBean1.getStartDate(), DateUtil.DATEYEARFORMATTER) + "~" + DateUtil.dateToString(payCycleBean1.getEndDate(), DateUtil.DATEYEARFORMATTER));
//        //年度
        payCycleBean1.setYear(DateUtil.dateToString(payCycleBean1.getStartDate(), DateUtil.DATEYEARFORMATTER).substring(0, 4));
//        //月份
        payCycleBean1.setMonth(Integer.valueOf(DateUtil.dateToString(payCycleBean1.getStartDate(), DateUtil.DATEYEARFORMATTER).substring(5, 7)).toString());
        //当月工资是否已发放 0:未发放 1:待发放  2:发放中  3:已发放
        payCycleBean1.setIsPayOff(Integer.valueOf(0));
        //工资单审批状态: 0:未审批  1:审批通过
        payCycleBean1.setApprovalState(Integer.valueOf(0));
        //新增计薪周期
        return payCycleWriterMapper.insertSelective(payCycleBean1);
    }

    /**
     * 获取计薪结束日
     *
     * @param startDate
     * @return
     */
    private Date getCalcSalary(Date startDate) {
        if (startDate.getDate() == 1) {
            return DateUtil.stringToDate(DateUtil.getMonthStartAndEndDate(startDate)[1], DateUtil.DATEYEARFORMATTER);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(DateUtil.dateFormatter.parse(DateUtil.dateToString(startDate, DateUtil.DATEYEARFORMATTER), new ParsePosition(0)));
            cal.add(Calendar.MONTH, 1);
            return cal.getTime();
        }
    }

    /**
     * 获取未发工资的计薪周期
     *
     * @return
     */
    public List<PayCycleBean> getPayCycleSalary() {
        return payCycleReaderMapper.selectPayCycleSalary();
    }

    /**
     * 根据公司id获取年度
     *
     * @param companyId
     * @return
     */
    public List<PayCycleBean> selectYearByCompanyId(Long companyId) {
        return payCycleReaderMapper.selectYearByCompanyId(companyId);
    }

    /**
     * 根据公司id和年度查询工资单
     *
     * @param companyId
     * @param year
     * @return
     */
    public List<PayCycleBean> selectByCompanyIdAndYear(Long companyId, String year) {
        return payCycleReaderMapper.selectByCompanyIdAndYear(companyId, year);
    }

    /**
     * 根据公司id获取计薪周期年度
     *
     * @param companyId
     * @return
     */
    public List<String> selectYearPayroll(Long companyId) {
        return payCycleReaderMapper.selectYearPayroll(companyId);
    }

    /**
     * 根据公司Id，年度查询工资单
     *
     * @param companyId
     * @param year
     * @return
     */
    public List<PayCycleBean> selectPayrollSummary(Long companyId, String year) {
        return payCycleReaderMapper.selectPayrollSummary(companyId, year);
    }

    /**
     * 更新计薪周期工资发放状态为已发放
     *
     * @param payCycleId
     * @return
     */
    public PayCycleBean updatePayCyclePayOff(Long payCycleId) {
        //更改计薪周期审批状态
        PayCycleBean payCycleBean = payCycleReaderMapper.selectByPrimaryKey(payCycleId);

        //工资单审批状态: 0:未审批  1:审批通过
        if (payCycleBean == null) {
            throw new BusinessException("未生成计薪周期");
        }
        if (payCycleBean.getApprovalState().intValue() == 0) {
            throw new BusinessException("工资单还未审批，不能进行工资发放");
        }
        if (payCycleBean.getIsPayOff().intValue() == 1 || payCycleBean.getIsPayOff().intValue() == 2) {
            throw new BusinessException("该工资单正在发放中");
        }
        if (payCycleBean.getIsPayOff().intValue() == 3) {
            throw new BusinessException("该工资单工资已发放完毕");
        }
        //当月工资是否已发放 0:未发放 1:待发放  2:发放中  3:已发放
        payCycleBean.setIsPayOff(Integer.valueOf(1));
        int result = updateByPrimaryKeySelective(payCycleBean);
        if (result <= 0)
            throw new BusinessException("更新计薪周期工资发放状态失败");
        return payCycleBean;
    }

    public static void main(String[] args) {
        System.out.println(new PayCycleServiceImpl().getCalcSalary(DateUtil.stringToDate("2016-01-01", DateUtil.DATEYEARFORMATTER)));
    }

}
