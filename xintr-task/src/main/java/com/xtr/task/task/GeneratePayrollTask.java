package com.xtr.task.task;

import com.alibaba.fastjson.JSON;
import com.xtr.api.domain.company.CompanySalaryExcelBean;
import com.xtr.api.domain.customer.CustomerSalarysBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.service.company.CompanyRechargesService;
import com.xtr.api.service.company.CompanySalaryExcelService;
import com.xtr.api.service.customer.CustomerSalarysService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.util.ExcelJsonUtil;
import com.xtr.api.util.ExcelUtil;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.GrantStateConstant;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.comm.util.SpringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <p>生成工资单任务</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/29 13:42
 */
public class GeneratePayrollTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayDayTask.class);

    @Resource
    private CompanySalaryExcelService companySalaryExcelService;

    //    private CompanyDepsService companyDepsService;
    @Resource
    private CustomerSalarysService customerSalarysService;
    @Resource
    private CustomersService customersService;

    @Resource
    private CompanyRechargesService companyRechargesService;


    /**
     * 创建线程池
     */
    ExecutorService pool = null;

    public GeneratePayrollTask() {
        super();
        companySalaryExcelService = (CompanySalaryExcelService) SpringUtils.getBean("companySalaryExcelService");
//        companyDepsService = (CompanyDepsService) SpringUtils.getBean("companyDepsService");
        customerSalarysService = (CustomerSalarysService) SpringUtils.getBean("customerSalarysService");
        customersService = (CustomersService) SpringUtils.getBean("customersService");
        companyRechargesService = (CompanyRechargesService) SpringUtils.getBean("companyRechargesService");
    }

    /**
     * 定时任务处理
     */
    public void run() throws Exception {
        //获取待处理状态的工资单
        String sameDay = DateUtil.formatCurrentDate();
        LOGGER.info("开始执行【" + sameDay + "】工资单生成任务");
        List<CompanySalaryExcelBean> list = companySalaryExcelService.getPendingPayroll(sameDay, GrantStateConstant.GRANT_STATE_0);
        if (list != null && !list.isEmpty()) {
            LOGGER.info("【" + sameDay + "】需要生成工资单的文档数量【" + list.size() + "】");
            int size = list.size();
            pool = Executors.newFixedThreadPool(size > 10 ? 10 : size);
            for (CompanySalaryExcelBean companySalaryExcelBean : list) {
                //解析excel文档
                analysisExcel(companySalaryExcelBean);
            }
        }
        LOGGER.info("结束执行【" + sameDay + "】工资单生成任务");
    }

    /**
     * 解析excel文档
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void analysisExcel(CompanySalaryExcelBean companySalaryExcelBean) throws Exception {
        LOGGER.info("生成工资单--开始解析Excel文档,参数:" + JSON.toJSONString(companySalaryExcelBean));
        if (companySalaryExcelBean != null) {
//            String filePath = PropertyUtils.getString("xintr.file.server.url") + companySalaryExcelBean.getExcelPath();
            //文件读取开始行
            int startLine = Integer.valueOf(PropertyUtils.getString("company.readLine"));
            //文件读取页签
            int sheetNum = Integer.valueOf(PropertyUtils.getString("company.sheetNum"));
            /**解析Excel*/
            byte[] bytes = AliOss.downloadFileByte(PropertyUtils.getString("oss.bucketName.file"), companySalaryExcelBean.getExcelPath());
//            byte[] bytes = FastDFS.downloadFile(companySalaryExcelBean.getExcelGroupName(), companySalaryExcelBean.getExcelPath());
//            InputStream inputStream = new ByteArrayInputStream(bytes);
             List<Object[]> list = ExcelUtil.getDataFromExcel(bytes, startLine, sheetNum);
            //数据转换
           List<Map<String, String>>  dataMap= ExcelUtil.analyzeData(list, startLine,52);

            //获取当前excel文件的json数据list集合
            List jsonList  = (List) ExcelJsonUtil.getJsonList(bytes);
            //List<Map<String, String>> dataMapNew = ExcelJsonUtil.translate(dataMap,jsonList);
            //遍历Excel数据
            traverseExcelData(dataMap, companySalaryExcelBean);

            //更新工资单发放状态 --已发放工资单 0=待处理，1=发放中，2=已发放，3=挂起，4=仅下发工资单，5=撤销
            companySalaryExcelService.updateGrantState(companySalaryExcelBean.getExcelId(), GrantStateConstant.GRANT_STATE_4, GrantStateConstant.GRANT_STATE_0);

            //更新发工资记录状态
            companyRechargesService.updateRechargeState(companySalaryExcelBean.getExcelCompanyId(), companySalaryExcelBean.getExcelId(), GrantStateConstant.GRANT_STATE_4);
        }
    }

    /**
     * 遍历Excel数据，并利用线程池生成工资单
     *
     * @param dataMap
     * @param companySalaryExcelBean
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void traverseExcelData(List<Map<String, String>> dataMap, final CompanySalaryExcelBean companySalaryExcelBean) throws BusinessException, InterruptedException {
        LOGGER.info("生成工资单--遍历Excel数据,参数：" + JSON.toJSONString(dataMap));
        if (!dataMap.isEmpty()) {
            try {
                final List<Exception> errorList = new ArrayList();
                final String batchNo = UUID.randomUUID().toString();
                int waitTime = 500;
                List<Future> rowResult = new CopyOnWriteArrayList<Future>();

                for (final Map<String, String> map : dataMap) {

                    rowResult.add(pool.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //生成工资单
                                generatePayroll(map, companySalaryExcelBean, batchNo);
                            } catch (Exception e) {
                                //添加一次信息
                                errorList.add(e);
                                //输出异常信息
                                LOGGER.error(e.getMessage(), e);
                            }
                        }
                    }));
                    waitTime += RandomUtils.nextInt(1000);
                }

                //请求关闭、发生超时或者当前线程中断，无论哪一个首先发生之后，都将导致阻塞，直到所有任务完成执行。
//            pool.awaitTermination(waitTime, TimeUnit.MILLISECONDS);
//            //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用
//            pool.shutdown();

                //等待处理结果
                for (Future f : rowResult) {
                    f.get();
                }
                //            //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用
                pool.shutdown();
                //出现异常
                if (!errorList.isEmpty()) {
                    //数据回滚处理

                    //抛出异常信息
                    throw new BusinessException(errorList.get(0).getMessage());
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw new BusinessException(e.getMessage());
            }
        }
    }


    /**
     * 生成工资单
     *
     * @param companySalaryExcelBean
     * @param map
     */
    @Transactional(propagation = Propagation.REQUIRED)
    private void generatePayroll(Map<String, String> map, CompanySalaryExcelBean companySalaryExcelBean, String batchNo) throws Exception {
        LOGGER.info("生成工资单,参数：" + JSON.toJSONString(map));

        //获取员工信息
        CustomersBean customersBean = getCustomer(companySalaryExcelBean.getExcelCompanyId(), map.get("C"));

        if (customersBean == null) {
            throw new BusinessException("公司主键【" + companySalaryExcelBean.getExcelCompanyId() + "】,员工身份证号【" + map.get("C") + "】对应的员工不存在");
        }
        //检查工资单是否已存在
        if (checkCustomerSalarys(customersBean, companySalaryExcelBean)) {

            //工资单实体对象
            CustomerSalarysBean customerSalarysBean = new CustomerSalarysBean();
            //企业工资文件id
            customerSalarysBean.setSalaryExcelId(companySalaryExcelBean.getExcelId());
            //企业Id
            customerSalarysBean.setSalaryCompanyId(companySalaryExcelBean.getExcelCompanyId());
            //所在部门id
            customerSalarysBean.setSalaryDepId(companySalaryExcelBean.getExcelDepId());
            //部门名称
            customerSalarysBean.setSalaryDepName(customersBean.getCustomerDepName());
            //用户Id
            customerSalarysBean.setSalaryCustomerId(customersBean.getCustomerId());
            customerSalarysBean.setSalaryYear(companySalaryExcelBean.getExcelYear());
            //年度
            //月度
            customerSalarysBean.setSalaryMonth(companySalaryExcelBean.getExcelMonth());
            //考勤天数
            customerSalarysBean.setSalaryAttendancesCount(Integer.valueOf(StringUtils.isBlank(map.get("H")) ? "0" : map.get("H")));
            //应发工资
            customerSalarysBean.setSalaryDue(new BigDecimal(StringUtils.isBlank(map.get("O")) ? "0" : map.get("O")));
            //基本工资
            customerSalarysBean.setSalaryBase(new BigDecimal(StringUtils.isBlank(map.get("G")) ? "0" : map.get("G")));
            //绩效
//        customerSalarysBean.setSalaryAchievements(new BigDecimal(StringUtils.isBlank(map.get("G")) ? "0" : map.get("G")));
            //奖金
            customerSalarysBean.setSalaryBonus(new BigDecimal(StringUtils.isBlank(map.get("N")) ? "0" : map.get("N")));
            //扣除
            customerSalarysBean.setSalaryBonus(new BigDecimal(StringUtils.isBlank(map.get("L")) ? "0" : map.get("L")));
            //个税
            customerSalarysBean.setSalaryPersonalTax(new BigDecimal(StringUtils.isBlank(map.get("T")) ? "0" : map.get("T")));
            //实发工资
            customerSalarysBean.setSalaryActual(new BigDecimal(StringUtils.isBlank(map.get("F")) ? "0" : map.get("F")));
            //创建时间
            customerSalarysBean.setSalaryAddtime(new Date());
            //是否可用 0不可用 1可用
            customerSalarysBean.setSalarySign(1);
            //事假(小时）
            customerSalarysBean.setSalaryCasualleaveTime(Double.valueOf(StringUtils.isBlank(map.get("H")) ? "0" : map.get("H")) * 24);
            //缺勤(小时)
            customerSalarysBean.setSalaryAbsenceTime(Double.valueOf(StringUtils.isBlank(map.get("J")) ? "0" : map.get("J")) * 24);
            //其他(加)
            customerSalarysBean.setSalaryOtherplus(new BigDecimal(StringUtils.isBlank(map.get("R")) ? "0" : map.get("R")));
            //批次号
            customerSalarysBean.setBatchNo(batchNo);

            //获取当前这行数据的json字符串
            customerSalarysBean.setSalaryDetail(map.get("jsonStr")==null?" ":map.get("jsonStr"));//当前excel数据行的json字符串


            //插入数据库
            customerSalarysService.insert(customerSalarysBean);
        } else {
            LOGGER.info("员工【" + customersBean.getCustomerTurename() + "】工资单是否已存在,年【" + companySalaryExcelBean.getExcelYear() + "】,月份【" + companySalaryExcelBean.getExcelMonth() + "】，自动跳过");
        }
    }

    /**
     * 检查工资单是否已存在
     *
     * @param customersBean
     * @param companySalaryExcelBean
     */
    private boolean checkCustomerSalarys(CustomersBean customersBean, CompanySalaryExcelBean companySalaryExcelBean) {
        LOGGER.info("生成工资单-----检查员工【" + customersBean.getCustomerTurename() + "】工资单是否已存在,年【" + companySalaryExcelBean.getExcelYear() + "】,月份【" + companySalaryExcelBean.getExcelMonth() + "】开始");
        //工资单实体对象
        CustomerSalarysBean customerSalarysBean = new CustomerSalarysBean();
        //员工主键
        customerSalarysBean.setSalaryCustomerId(customersBean.getCustomerId());
        //公司主键
        customerSalarysBean.setSalaryCompanyId(companySalaryExcelBean.getExcelCompanyId());
        //年度
        customerSalarysBean.setSalaryYear(companySalaryExcelBean.getExcelYear());
        //月份
        customerSalarysBean.setSalaryMonth(companySalaryExcelBean.getExcelMonth());
        List<CustomerSalarysBean> list = customerSalarysService.selectCustomerSalarys(customerSalarysBean);

//        LOGGER.info("生成工资单----检查员工【" + customersBean.getCustomerTurename() + "】工资单是否已存在,年【" + companySalaryExcelBean.getExcelYear() + "】,月份【" + companySalaryExcelBean.getExcelMonth() + "】");
        return list.isEmpty();
    }

    /**
     * 获取员工信息
     *
     * @param companyId
     * @param idCard
     * @return
     */
    private CustomersBean getCustomer(Long companyId, String idCard) {
        LOGGER.info("获取员工信息参数，公司主键【" + companyId + "】,身份证号【" + idCard + "】");
        CustomersBean bean = new CustomersBean();
        bean.setCustomerCompanyId(companyId);
        bean.setCustomerIdcard(idCard);
        List<CustomersBean> list = customersService.getCustomers(bean);
        LOGGER.info("获取员工信息返回结果：" + JSON.toJSONString(list));
        if (!list.isEmpty())
            return list.get(0);
        else return null;
    }
}
