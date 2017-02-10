package com.xtr.company.controller.customer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ObjectArrayCodec;
import com.xtr.api.basic.BaseController;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.BankCodeBean;
import com.xtr.api.domain.company.*;
import com.xtr.api.domain.customer.*;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.dto.customer.CustomerResponse;
import com.xtr.api.dto.customer.CustomersDto;
import com.xtr.api.dto.shebao.TJShebaoDto;
import com.xtr.api.service.account.BankCodeService;
import com.xtr.api.service.company.CompanyDepsService;
import com.xtr.api.service.company.CompanyMembersService;
import com.xtr.api.service.company.CompanyProtocolsService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.customer.*;
import com.xtr.api.service.salary.PayCycleService;
import com.xtr.api.service.salary.PayrollAccountService;
import com.xtr.api.service.shebao.CompanyShebaoService;
import com.xtr.api.service.shebao.CustomerShebaoOrderService;
import com.xtr.api.service.shebao.CustomerShebaoService;
import com.xtr.comm.annotation.SystemControllerLog;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CompanyProtocolConstants;
import com.xtr.comm.constant.CustomerConstants;
import com.xtr.comm.constant.ShebaoConstants;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.util.*;
import com.xtr.company.util.SessionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Decoder;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/8/12 9:38
 */
@Controller
@RequestMapping("customerManager")
public class CustomerManagerController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerManagerController.class);

    @Resource
    private CustomersService customersService;

    @Resource
    private CompanysService companysService;

    @Resource
    private CustomersStationService customersStationService;

    @Resource
    private CustomersRecordService customersRecordService;

    @Resource
    private CustomersPersonalService customersPersonalService;

    @Resource
    private CustomerUpdateSalaryService customerUpdateSalaryService;

    @Resource
    private CompanyDepsService companyDepsService;

    @Resource
    private CompanyProtocolsService companyProtocolsService;

    @Resource
    private BankCodeService bankCodeService;

    @Resource
    private PayrollAccountService payrollAccountService;

    @Resource
    private CompanyMembersService companyMembersService;

    @Resource
    private PayCycleService payCycleService;

    @Resource
    private CustomerShebaoOrderService customerOrderShebaoService;

    @Resource
    private CompanyShebaoService companyShebaoService;

    @Resource
    private CustomerShebaoService customerShebaoService;

    /**
     * 跳转到员工管理页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("toCustomerMainPage.htm")
    public ModelAndView toCustomerMainPage(HttpServletRequest request, ModelAndView mav) {
        mav.setViewName("xtr/customer/customerMain");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            String startStr = sdf.format(calendar.getTime());
            startStr = startStr.substring(0, 10);
            startStr = startStr + " 00:00:00";
            Date startDate = sdf.parse(startStr);
            mav.addObject("startTime", startDate);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            String endStr = sdf.format(calendar.getTime());
            endStr = endStr.substring(0, 10);
            endStr = endStr + " 23:59:59";
            Date endDate = sdf.parse(endStr);
            mav.addObject("endTime", endDate);
            //判断是否第一次访问
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            Map<String,Object> visitMap = new HashMap<String,Object>();
            visitMap.put("type",2);//首页的类型
            visitMap.put("memberId",companyMembersBean.getMemberId());
            CompanyMenuVisitRecordBean companyMenuVisitRecordBean = companyMembersService.selectVisitRecord(visitMap);
            //页面带一个标识
            int insertFlag = 0;
            if(null==companyMenuVisitRecordBean){
                //说明之前没登陆过
                //插入一条记录
                CompanyMenuVisitRecordBean saveVisitRecord = new CompanyMenuVisitRecordBean();
                saveVisitRecord.setIp(request.getRemoteAddr());
                saveVisitRecord.setMemberId(companyMembersBean.getMemberId());
                saveVisitRecord.setCompanyId(companyMembersBean.getMemberCompanyId());
                saveVisitRecord.setType(2);//员工管理
                saveVisitRecord.setVisitTime(new Date());
                insertFlag = companyMembersService.saveVisitRecord(saveVisitRecord);
                if(insertFlag==1){
                    //带标识去页面
                    mav.addObject("customerVisitFirst","customerVisitFirst");
                }
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return mav;
    }

    /**
     * 跳转到员工新增页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("toCustomerAddPage.htm")
    public ModelAndView toCustomerAddPage(ModelAndView mav) {
        mav.setViewName("xtr/customer/customerAdd");
        return mav;
    }

    /**
     * 跳转到员工批量导入页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("toCustomerBatchImportPage.htm")
    public ModelAndView toCustomerBatchImportPage(ModelAndView mav) {
        mav.setViewName("xtr/customer/customerBatchImport");
        return mav;
    }

    /**
     * 跳转到员工批量导入上传规则页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("toCustomerUploadRulePage.htm")
    public ModelAndView toCustomerUploadRulePage(ModelAndView mav) {
        mav.setViewName("xtr/customer/customerUploadRule");
        return mav;
    }

    /**
     * 跳转到员工编辑主页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("toCustomerEditPage.htm")
    public ModelAndView toCustomerEditPage(ModelAndView mav, Long customerId)throws Exception {
        CustomersDto customersDto = customersService.selectCustomersByCustomerId(customerId);
        Date enterTime = customersDto.getStationEnterTime();
        if (enterTime != null) {
            Integer diffDays = DateUtil.getDiffDaysOfTwoDateByNegative(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), new SimpleDateFormat("yyyy-MM-dd").format(enterTime));
            if (diffDays < 0) {
                mav.addObject("diffYear", 0);
            } else {
                BigDecimal diffYear = new BigDecimal(String.valueOf(diffDays)).divide(new BigDecimal("365"), 1, BigDecimal.ROUND_HALF_DOWN);
                mav.addObject("diffYear", diffYear);
            }
        } else {
            mav.addObject("diffYear", 0);
        }
        //获取年龄
        String birthdayTime=customersDto.getCustomerBirthdayMonth();
        if(!StringUtils.isStrNull(birthdayTime)){
            int birthdayInt=Integer.parseInt(birthdayTime.substring(0,4));
            int nowInt=Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
            mav.addObject("ageStr",(nowInt- birthdayInt+1<=0)?"":String.valueOf(nowInt- birthdayInt+1));
        }
        //获取是否签约代发工资(如签约代发工资，身份证号、工资卡卡号、工资卡开户行，是必填字段)
        CompanyProtocolsBean companyProtocolsBean=new CompanyProtocolsBean();
        companyProtocolsBean.setProtocolContractType(CompanyProtocolConstants.PROTOCOL_TYPE_DF);//代发类型
        companyProtocolsBean.setProtocolCompanyId(customersDto.getCustomerCompanyId());
        int count=companyProtocolsService.selectCountByUserfulProtocol(companyProtocolsBean);
        mav.addObject("ffCount", count<=0?0:count);//count>0代表有有效的代发协议
        List<CustomersDto> recordList = customersRecordService.selectRecordByCustomerId(customerId);
        mav.addObject("customersDto", customersDto);
        mav.addObject("customerId", customerId);
        mav.addObject("recordList", recordList);
        mav.addObject("companyCheckId", customersDto.getCustomerCompanyId());

        mav.setViewName("xtr/customer/customerEdit");
        return mav;
    }

    /**
     * 跳转到员工编辑基本信息页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("toCustomerEditBasePage.htm")
    public ModelAndView toCustomerEditBasePage(ModelAndView mav,Long customerId)throws Exception {
        CustomersDto customersDto = customersService.selectCustomersByCustomerId(customerId);
        Date enterTime = customersDto.getStationEnterTime();
        if (enterTime != null) {
            Integer diffDays = DateUtil.getDiffDaysOfTwoDateByNegative(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), new SimpleDateFormat("yyyy-MM-dd").format(enterTime));
            if (diffDays < 0) {
                mav.addObject("diffYear", 0);
            } else {
                BigDecimal diffYear = new BigDecimal(String.valueOf(diffDays)).divide(new BigDecimal("365"), 1, BigDecimal.ROUND_HALF_DOWN);
                mav.addObject("diffYear", diffYear);
            }
        } else {
            mav.addObject("diffYear", 0);
        }
        List<CustomersDto> recordList = customersRecordService.selectRecordByCustomerId(customerId);
        mav.addObject("customersDto", customersDto);
        mav.addObject("customerId", customerId);
        mav.addObject("recordList", recordList);
        mav.addObject("companyCheckId", customersDto.getCustomerCompanyId());

        mav.setViewName("xtr/customer/customerEditBase");
        return mav;
    }

    /**
     * 跳转到员工编辑个人信息页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("toCustomerEditPersonalPage.htm")
    public ModelAndView toCustomerEditPersonalPage(ModelAndView mav) {
        mav.setViewName("xtr/customer/customerEditPersonal");
        return mav;
    }

    /**
     * 跳转到员工编辑岗位信息页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("toCustomerEditStationPage.htm")
    public ModelAndView toCustomerEditStationPage(ModelAndView mav) {
        mav.setViewName("xtr/customer/customerEditStation");
        return mav;
    }

    /**
     * 跳转到筛选页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("toCustomerSelectorConditionPage.htm")
    public ModelAndView toCustomerSelectorConditionPage(ModelAndView mav) {
        mav.setViewName("xtr/customer/customerSelectorCondition");
        return mav;
    }

    /**
     * 跳转到搜索页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("toCustomerSelectorNamePage.htm")
    public ModelAndView toCustomerSelectorNamePage(ModelAndView mav) {
        mav.setViewName("xtr/customer/customerSelectorName");
        return mav;
    }

    /**
     * 跳转到入职需知页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("toCustomerEnterRequirePage.htm")
    public ModelAndView toCustomerEnterRequirePage(ModelAndView mav) {
        mav.setViewName("xtr/customer/customerEnterRequire");
        return mav;
    }

    /**
     * 过滤查询用户信息
     *
     * @param response
     * @param customersDto
     */
    @RequestMapping("queryList.htm")
    @ResponseBody
    public void queryList(HttpServletRequest request, HttpServletResponse response, CustomersDto customersDto) {
        Long startTime=new Date().getTime();
        //获取登录用户
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        //获取列表结果
        customersDto.setCustomerCompanyId(companyMembersBean.getMemberCompanyId());
//        customersDto.setStartTimeStr(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(customersDto.getStartTime()));
        customersDto.setStartTimeStr(DateUtil.date2String(customersDto.getStartTime(), DateUtil.dateTimeString));
//        customersDto.setEndTimeStr(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(customersDto.getEndTimeStr()));
        customersDto.setEndTimeStr(DateUtil.date2String(customersDto.getEndTime(), DateUtil.dateTimeString));
        try {
            ResultResponse resultResponse = customersService.selectPageListByCondition(customersDto);
            LOGGER.info("获取列表所用时间:"+((new Date().getTime())-startTime));
            CustomerResponse customerResponse = new CustomerResponse();
            customerResponse.setResultResponse(resultResponse);
            LOGGER.info("企业端用户分页查询,返回结果:" + JSON.toJSONString(customerResponse));
            HtmlUtil.writerJson(response, customerResponse);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 获取员工数量
     * @param request
     * @param response
     * @param customersDto
     */
    @RequestMapping("queryCount.htm")
    @ResponseBody
    public void queryCount(HttpServletRequest request, HttpServletResponse response, CustomersDto customersDto) {
        Long startTime=new Date().getTime();
        //获取登录用户
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        //获取入职\离职\待转正\未补全资料的人数
        CustomersDto customersCountDto = new CustomersDto();
        customersCountDto.setStartTime(customersDto.getStartTime());
        customersCountDto.setEndTime(customersDto.getEndTime());
        customersCountDto.setStartTimeStr(DateUtil.date2String(customersDto.getStartTime(), DateUtil.dateTimeString));
        customersCountDto.setEndTimeStr(DateUtil.date2String(customersDto.getEndTime(), DateUtil.dateTimeString));
        customersCountDto.setCustomerCompanyId(companyMembersBean.getMemberCompanyId());
        customersCountDto.setSelState(CustomerConstants.CUSTOMER_PERSONNUM_ALL);
        int totalCount = customersService.selectCustomerNumber(customersCountDto);
        customersCountDto.setSelState(CustomerConstants.CUSTOMER_PERSONNUM_ENTER);
        int enterCount = customersService.selectCustomerNumber(customersCountDto);
        customersCountDto.setSelState(CustomerConstants.CUSTOMER_PERSONNUM_LEAVE);
        int leaveCount = customersService.selectCustomerNumber(customersCountDto);
        customersCountDto.setSelState(CustomerConstants.CUSTOMER_PERSONNUM_WAIT);
        int willCount = customersService.selectCustomerNumber(customersCountDto);
        customersCountDto.setCustomerIsComplement(CustomerConstants.CUSTOMER_ISCOMPLEMENT_NO);
        customersCountDto.setSelState(CustomerConstants.CUSTOMER_PERSONNUM_LESSINFO);
        int lessCount = customersService.selectCustomerNumber(customersCountDto);
        LOGGER.info("获取员工数所用时间:"+((new Date().getTime())-startTime));

        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setTotalCount(totalCount);
        customerResponse.setEnterCount(enterCount);
        customerResponse.setLeaveCount(leaveCount);
        customerResponse.setWillCount(willCount);
        customerResponse.setLessCount(lessCount);
        LOGGER.info("企业端用户分页查询,返回结果:" + JSON.toJSONString(customerResponse));
        HtmlUtil.writerJson(response, customerResponse);
    }

    /***
     * 跳转到新增员工页面
     */
    @RequestMapping(value = "toCustomerAdd.htm")
    public ModelAndView jumpCustomerAdd(ModelAndView modelAndView, HttpServletRequest request) {
        modelAndView.setViewName("xtr/customer/customerAdd");
        return modelAndView;
    }


    /**
     * 新增员工
     *
     * @param request
     * @param customersDto
     * @return
     */

    @RequestMapping(value = "cusomerAdd.htm")
    @ResponseBody
    @SystemControllerLog(operation = "新增员工", modelName = "用户管理模块")
    public ResultResponse cusomerAdd(HttpServletRequest request, CustomersDto customersDto) {

        ResultResponse resultResponse = new ResultResponse();


        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);//获取企业id

        long customerCompanyId = companyMembersBean.getMemberCompanyId();//企业id
        customersDto.setCustomerCompanyId(customerCompanyId);//公司id

        int customerState = 0;
        if (!StringUtils.isEmpty(customersDto.getIsRegular()) && customersDto.getIsRegular() == 2){
            customerState = 2;//已转正
        }else{
            customerState  = 1;//入职
        }

        BigDecimal currentSalary = customersDto.getCustomerCurrentSalary();
        BigDecimal regularSalary = customersDto.getCustomerRegularSalary();

        if(currentSalary != null || regularSalary != null){
            if(1 == customerState){
                if(currentSalary == null){
                    return ResultResponse.buildFail(null).message("请输入试用期工资");
                }
            }

            if(2 == customerState){
                if(regularSalary == null){
                    return ResultResponse.buildFail(null).message("请输入转正后工资");
                }
            }
        }


        try {
            //判断入职时间是否大于服务器时间
            Date joinTime = customersDto.getCustomerJoinTime();
            if(joinTime.getTime()-System.currentTimeMillis()>0){
                resultResponse.setSuccess(false);
                resultResponse.setMessage("入职时间不能大于当前时间(以服务器时间为准)");
                return resultResponse;
            }
            if(!StringUtils.isEmpty(customersDto.getCustomerRegularTime())){
                if(joinTime.getTime()-(customersDto.getCustomerRegularTime().getTime())>0){
                    resultResponse.setSuccess(false);
                    resultResponse.setMessage("入职时间不能大于转正时间(以服务器时间为准)");
                    return resultResponse;
                }
            }


            //新增员工
            resultResponse = customersService.insertCustomer(customersDto);
            Long customerId = (Long) resultResponse.getData();

            //新增工资单
            try {
                payrollAccountService.generatePayroll(customerCompanyId,customerId);
            } catch ( Exception e ) {
                LOGGER.info("当前生成工资单接口出现错误：" + e.getMessage());
            }

            // 调用定薪接口
            try {
                CustomersBean customersBean = customerUpdateSalaryService.setCustomerSalary(customerId, customersDto.getCustomerCurrentSalary(), customersDto.getCustomerRegularSalary());
                customerUpdateSalaryService.updatePayRoll(customersBean);
            } catch (Exception e) {
                LOGGER.info("当前定薪接口出现错误：" + e.getMessage());
            }



        } catch (BusinessException e) {

            resultResponse.setMessage(e.getMessage());

        }
        return resultResponse;
    }


    /**
     * 跳转到员工转正页面
     *
     * @param modelAndView
     * @return
     */
    @RequestMapping(value = "toCustomerRegular.htm")
    public ModelAndView jumpCustomerRegularPage(ModelAndView modelAndView, HttpServletRequest request) {


        //根据customerId查询员工信息
        long customerId = Long.valueOf(request.getParameter("customerId"));
        CustomersBean customersBean = customersService.selectNameById(customerId);
        //根据customerId查询岗位表
        CustomersStationBean customersStationBean = customersStationService.selectCutomerStationByCustomerId(customerId);

        //封装页面参数
        modelAndView.addObject("customerId", customerId);//员工id
        modelAndView.addObject("trueName", customersBean.getCustomerTurename());
        modelAndView.addObject("stationEnterTime", customersStationBean.getStationEnterTime());//入职时间
        modelAndView.addObject("stationRegularTime", customersStationBean.getStationRegularTime());//转正时间
        //判断当前时间是否比转正日期大
        if (StringUtils.isEmpty(customersStationBean.getStationEnterTime())) {
            if (System.currentTimeMillis() - customersStationBean.getStationEnterTime().getTime() > 0) {
                modelAndView.addObject("isRegular", 2);
            }
        }
        modelAndView.setViewName("xtr/customer/customerRegular");
        return modelAndView;
    }

    /**
     * 员工转正
     *
     * @param request
     * @return
     */

    @RequestMapping(value = "customerRegular.htm")
    @ResponseBody
    @SystemControllerLog(operation = "员工转正", modelName = "用户管理模块")
    public ResultResponse customerRegular(HttpServletRequest request, CustomersDto customersDto) {

        ResultResponse resultResponse = new ResultResponse();

        try {
            long companyId = SessionUtils.getUser(request).getMemberCompanyId();//员工的企业id
            customersDto.setCompanyId(companyId);
            resultResponse = customersService.customerRegular(customersDto);
        } catch (BusinessException e) {
            resultResponse.setMessage(e.getMessage());

        }

        return resultResponse;
    }

    /**
     * 跳转到员工调岗页面
     *
     * @param modelAndView
     * @return
     */
    @RequestMapping(value = "toCustomerTransferPosition.htm")
    public ModelAndView jumpTransferPosition(ModelAndView modelAndView, HttpServletRequest request) {



        long customerId = Long.valueOf(request.getParameter("customerId"));
        CustomersBean customersBean = customersService.selectNameById(customerId);
        CustomersStationBean customersStationBean = customersStationService.selectCutomerStationByCustomerId(customerId);

        if(!StringUtils.isEmpty(customersStationBean.getStationDeptId())) {
            CompanyDepsBean companyDepsBean = companyDepsService.selectByPrimaryKey(customersStationBean.getStationDeptId());
            modelAndView.addObject("depName", companyDepsBean.getDepName());//部门
        }
        //封装页面参数
        modelAndView.addObject("customerId", customerId);//员工id
        modelAndView.addObject("trueUserName", customersBean.getCustomerTurename());//姓名
        modelAndView.addObject("stationName", customersStationBean.getStationStationName());//职位
        modelAndView.setViewName("xtr/customer/customerTransferPosition");
        return modelAndView;
    }

    /**
     * 员工调岗
     *
     * @param request
     * @param customersDto
     * @return
     */

    @RequestMapping(value = "customerTransferPosition.htm")
    @ResponseBody
    @SystemControllerLog(operation = "员工调岗", modelName = "用户管理模块")
    public ResultResponse customerTransferPosition(HttpServletRequest request, CustomersDto customersDto) {

        ResultResponse resultResponse = new ResultResponse();
        try {

            resultResponse = customersService.customerTransferPosition(customersDto);
        } catch (BusinessException e) {
            resultResponse.setMessage(e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 更改入职需知
     *
     * @param request
     * @param companysBean
     * @return
     */
    @RequestMapping(value = "modifyEnterRequire.htm")
    @ResponseBody
    @SystemControllerLog(operation = "更改入职需知", modelName = "用户管理模块")
    public ResultResponse modifyEnterRequire(HttpServletRequest request, CompanysBean companysBean) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            LOGGER.info("更改入职须知,传递参数:" + JSON.toJSONString(companysBean) + ",登录用户信息:" + JSON.toJSONString(companyMembersBean));
            if (companysBean != null && companyMembersBean != null && companyMembersBean.getMemberCompanyId() != null) {
                companysBean.setCompanyId(companyMembersBean.getMemberCompanyId());
                if (StringUtils.isStrNull(companysBean.getCompanyEnterRequire())) {
                    companysBean.setCompanyEnterRequire("");
                }
                int count = companysService.updateByProtocolModify(companysBean);
                if (count <= 0) {
                    LOGGER.info("更改入职须知失败");
                    resultResponse.setMessage("更改入职须知失败");
                } else {
                    resultResponse.setSuccess(true);
                }
            } else {
                LOGGER.info("更改入职须知,传递参数不能为空");
                resultResponse.setMessage("传递参数不能为空");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resultResponse.setMessage(e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 查询入职需知
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "queryEnterRequire.htm")
    @ResponseBody
    public ResultResponse queryEnterRequire(HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            CompanysBean companysBean = companysService.selectCompanyByCompanyId(companyMembersBean.getMemberCompanyId());
            if (companysBean != null) {
                resultResponse.setSuccess(true);
                resultResponse.setData(companysBean);
            } else {
                LOGGER.info("查询入职须知,获取不到企业信息,企业ID:" + companyMembersBean.getMemberCompanyId());
                resultResponse.setMessage("获取不到企业信息");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resultResponse.setMessage(e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 跳转到离职页面
     *
     * @param modelAndView
     * @return
     */

    @RequestMapping(value = "toCustomerDimission.htm")
    public ModelAndView jumpCustomerDimission(ModelAndView modelAndView, HttpServletRequest request) {

        //获取customerId
        long customerId = Long.valueOf(request.getParameter("customerId"));
        CustomersBean customersBean = customersService.selectNameById(customerId);
        //判断当前员工是否有社保需求
        CustomerShebaoBean customerShebaoBean = customerShebaoService.selectCustomerShebaoByCustomerId(customerId);
        if(customerShebaoBean!=null){
            if(!StringUtils.isEmpty(customerShebaoBean.getIsGjjKeep())&&customerShebaoBean.getIsGjjKeep()==1){
                modelAndView.addObject("gjjDemand","gjjDemand");
                modelAndView.addObject("sbDemand","sbDemand");
            }else{
                modelAndView.addObject("gjjDemand","noGjjDemand");
                if(!StringUtils.isEmpty(customerShebaoBean.getIsSbKeep())&&customerShebaoBean.getIsSbKeep()==1){
                    modelAndView.addObject("sbDemand","sbDemand");
                }else{
                     modelAndView.addObject("sbDemand","noSbDemand");
                }
            }

        }else{
            modelAndView.addObject("sbDemand","noSbDemand");
            modelAndView.addObject("gjjDemand","noGjjDemand");
        }
        //封装页面参数
        modelAndView.addObject("customerId", customerId);
        modelAndView.addObject("trueUserName", customersBean.getCustomerTurename());//姓名
        modelAndView.setViewName("xtr/customer/customerDimission");
        return modelAndView;
    }


    /**
     * 员工离职
     *
     * @param request
     * @param customersDto
     * @return
     */

    @RequestMapping(value = "customerDimission.htm")
    @ResponseBody
    @SystemControllerLog(operation = "员工离职", modelName = "用户管理模块")
    public ResultResponse customerDimission(HttpServletRequest request, CustomersDto customersDto,Long customerId,String checkSb,String checkGjj) throws ParseException {

        ResultResponse resultResponse = new ResultResponse();
        Map returnMap =null;
        try {
            //后台判断离职日期是否小于当前日期
            Date dimissDate =customersDto.getStationDimissingTime();
//            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
//            if(dimissDate.getTime()-new SimpleDateFormat("yyyy-MM-dd").parse(currentDate).getTime()<0){
//                resultResponse.setMessage("离职时间不能小于当前时间");
//                return resultResponse;
//            }
            CompanysBean companysBean =SessionUtils.getCompany(request);
            customersDto.setCompanyId(companysBean.getCompanyId());
           resultResponse = customersService.customerDimission(customersDto);
             resultResponse.setSuccess(true);
            if(resultResponse.isSuccess()){

                String dimissDateStr=new SimpleDateFormat("yyyy-MM-dd").format(dimissDate);
                 returnMap = this.getSbStopDate(request,dimissDateStr,customerId,checkSb,checkGjj);

                LOGGER.info("员工离职停缴月计算返回的map："+returnMap);
                if(StringUtils.isEmpty(returnMap.get("message"))&&returnMap.get("message").equals("noSbDemand")){
                    //没有需求
                    resultResponse.setMessage(JSON.toJSON(returnMap).toString());
                    return resultResponse;
                }
                resultResponse.setMessage(JSON.toJSON(returnMap).toString());
                String sbStopDate = StringUtils.isEmpty(returnMap.get("sbStopDateStr"))==true?null:returnMap.get("sbStopDateStr").toString();
                String gjjStopDate =StringUtils.isEmpty(returnMap.get("gjjStopDateStr"))==true?null:returnMap.get("gjjStopDateStr").toString();

                if(!StringUtils.isEmpty(gjjStopDate)){
                    //调用停缴接口
                    SimpleDateFormat sm = new SimpleDateFormat("yyyy年MM月");
                    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
                    Date date = sm.parse(gjjStopDate);
                    String dateStr = sd.format(date);
                    //公积金停缴接口
                    TJShebaoDto gjjtj = new TJShebaoDto();
                    gjjtj.setCustomerId(customerId);
                    gjjtj.setRelationType(2);//公积金
                    customerOrderShebaoService.stopPayment(gjjtj,dateStr);
                }

                if(!StringUtils.isEmpty(sbStopDate)){
                //调用停缴接口
                SimpleDateFormat sm = new SimpleDateFormat("yyyy年MM月");
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
                Date date = sm.parse(sbStopDate);
                String dateStr = sd.format(date);
                //社保停缴接口
                TJShebaoDto sbtj = new TJShebaoDto();
                sbtj.setCustomerId(customerId);
                sbtj.setRelationType(1);//社保
                customerOrderShebaoService.stopPayment(sbtj,dateStr);
                LOGGER.info("调用社保停缴接口："+sbtj +"===="+dateStr);
               }


            }
        } catch (Exception e) {
           // e.printStackTrace();
            LOGGER.error("员工离职出现异常信息："+e.getMessage());
            resultResponse.setSuccess(false);
            resultResponse.setData(JSON.toJSON(returnMap).toString());
            resultResponse.setMessage(e.getMessage());
        }

        return resultResponse;

    }


    /**
     * 算出社保与公积金的停缴月
     * @return
     */

    public Map<String,Object> getSbStopDate(HttpServletRequest request,String stationDimissingTime,Long customerId,String checkSb,String checkGjj) throws Exception{

        Map returnMap = new HashMap();
        String sbStopDate =null;//社保停缴月
        String gjjStopDate=null;//公积金停缴月

        try{
            CompanysBean companysBean = SessionUtils.getCompany(request);
            //公司Id
            Long companyId = companysBean.getCompanyId();
            LOGGER.info("获取员工停缴月参数companyId："+companyId);
            //计算社保 公积金状态是否为缴纳
            CustomerShebaoBean customerShebaoBean = customerShebaoService.selectCustomerShebaoByCustomerId(customerId);
            if(customerShebaoBean==null){
                // 不需要设置
                returnMap.put("message","noSbDemand");//没有社保需求
                return returnMap;
            }
             returnMap.put("message","");
            //获取当前离职时间
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM");
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate=sm.parse(stationDimissingTime);//离职日期
            Calendar chanCalendar = Calendar.getInstance();
            chanCalendar.setTime(currentDate);
            String str = chanCalendar.get(Calendar.YEAR)+"-"+(chanCalendar.get(Calendar.MONTH)+1)+"-01";
            currentDate =sd.parse(str);

            Calendar dimissCalendar = Calendar.getInstance();
            dimissCalendar.setTime(currentDate);//社保停缴月

            Calendar gjjDimissCalendar = Calendar.getInstance();
            gjjDimissCalendar.setTime(currentDate);

            Date currentJiaoNaMonth = customerShebaoBean.getCurrentMonth();//当前缴纳月份
            LOGGER.info("员工当前缴纳月："+currentJiaoNaMonth);


            //判断当前公司的社保公积金订单是否已提交
            String joinCityCode = customerShebaoBean.getJoinCityCode();
            Map searchMap = new HashMap();
            searchMap.put("companyId",companyId);
            searchMap.put("joinCityCode",joinCityCode);
            if(!StringUtils.isStrNull(checkGjj)&&!StringUtils.isStrNull(checkSb)){
                dimissCalendar.add(Calendar.MONTH,1);
                gjjDimissCalendar.add(Calendar.MONTH,1);//公积金的

            }
            if(!StringUtils.isStrNull(checkSb)&&StringUtils.isStrNull(checkGjj)){
                dimissCalendar.add(Calendar.MONTH,1);
            }

            LOGGER.info("社保离职停缴月："+dimissCalendar.getTime());
            LOGGER.info("公积金离职停缴月："+gjjDimissCalendar.getTime());
            CompanyShebaoOrderBean companyShebaoOrderBean=null;
            //社保已缴纳
            if(!StringUtils.isEmpty(customerShebaoBean.getIsSbKeep())&&customerShebaoBean.getIsSbKeep()==1){

                if(dimissCalendar.getTime().getTime()-currentJiaoNaMonth.getTime()<0){

                    Calendar current = Calendar.getInstance();
                    current.setTime(currentJiaoNaMonth);
                    //查询社保订单是否已提交
                    searchMap.put("orderDate",currentJiaoNaMonth);

                     companyShebaoOrderBean = companyShebaoService.selectShebaoOrderByMap(searchMap);
                    if(null!=companyShebaoOrderBean&&companyShebaoOrderBean.getStatus()!=1&&companyShebaoOrderBean.getStatus()!=0){
                        current.add(Calendar.MONTH,1);
                        returnMap.put("sbStopDateStr",current.get(Calendar.YEAR)+"年"+(current.get(Calendar.MONTH)+1)+"月");
                    }else{
                        //未提交
                        returnMap.put("sbStopDateStr",current.get(Calendar.YEAR)+"年"+(current.get(Calendar.MONTH)+1)+"月");
                    }

                }else if(dimissCalendar.getTime().getTime()-currentJiaoNaMonth.getTime()>0){

                    //社保的停缴月
                    Date sbTingjiaoDate = customerShebaoBean.getSbStopDate();
                    if(!StringUtils.isEmpty(sbTingjiaoDate)){
                        //没有设置社保停缴月
                        Calendar sbTingjiao = Calendar.getInstance();
                        sbTingjiao.setTime(sbTingjiaoDate);
                        if(sbTingjiaoDate.getTime()-dimissCalendar.getTime().getTime()>0){
                            returnMap.put("sbStopDateStr",dimissCalendar.get(Calendar.YEAR)+"年"+(dimissCalendar.get(Calendar.MONTH)+1)+"月");
                        }else {
                            returnMap.put("sbStopDateStr",sbTingjiao.get(Calendar.YEAR)+"年"+(sbTingjiao.get(Calendar.MONTH)+1)+"月");
                        }
                    }else{
                        returnMap.put("sbStopDateStr",dimissCalendar.get(Calendar.YEAR)+"年"+(dimissCalendar.get(Calendar.MONTH)+1)+"月");
                    }

                }else{
                    //等于
                    //已提交
                    //判断订单是否已提交
                    searchMap.put("orderDate",dimissCalendar.getTime());
                    companyShebaoOrderBean = companyShebaoService.selectShebaoOrderByMap(searchMap);
                    if(null!=companyShebaoOrderBean&&companyShebaoOrderBean.getStatus()!=1&&companyShebaoOrderBean.getStatus()!=0){
                        dimissCalendar.add(Calendar.MONTH,1);
                        returnMap.put("sbStopDateStr",dimissCalendar.get(Calendar.YEAR)+"年"+(dimissCalendar.get(Calendar.MONTH)+1)+"月");
                    }else{
                        //未提交
                        returnMap.put("sbStopDateStr",dimissCalendar.get(Calendar.YEAR)+"年"+(dimissCalendar.get(Calendar.MONTH)+1)+"月");
                    }

                }
            }else{
                //社保未缴纳
                //判断是否有设置的停缴月
                if(!StringUtils.isEmpty(customerShebaoBean.getSbStopDate())){
                    Calendar changeCalendar = Calendar.getInstance();
                    changeCalendar.setTime(customerShebaoBean.getSbStopDate());
                    sbStopDate=changeCalendar.get(Calendar.YEAR)+"年"+(changeCalendar.get(Calendar.MONTH)+1)+"月";
                }else{
                    sbStopDate="";
                }
                returnMap.put("sbStopDateStr",sbStopDate);
                LOGGER.info("社保未缴纳时，当前的社保停缴月为："+sbStopDate);

            }



            //公积金已缴纳
            if(!StringUtils.isEmpty(customerShebaoBean.getIsGjjKeep())&&customerShebaoBean.getIsGjjKeep()==1){

                if(gjjDimissCalendar.getTime().getTime()-currentJiaoNaMonth.getTime()<0){
                    Calendar current = Calendar.getInstance();
                    current.setTime(currentJiaoNaMonth);
                    searchMap.put("orderDate",currentJiaoNaMonth);
                    companyShebaoOrderBean = companyShebaoService.selectShebaoOrderByMap(searchMap);
                    //查询社保订单是否已提交
                    if(null!=companyShebaoOrderBean&&companyShebaoOrderBean.getStatus()!=1&&companyShebaoOrderBean.getStatus()!=0){
                        gjjDimissCalendar.add(Calendar.MONTH,1);
                        returnMap.put("gjjStopDateStr",current.get(Calendar.YEAR)+"年"+(current.get(Calendar.MONTH)+1)+"月");
                    }else{
                        //未提交
                        returnMap.put("gjjStopDateStr",current.get(Calendar.YEAR)+"年"+(current.get(Calendar.MONTH)+1)+"月");
                    }

                }else if(gjjDimissCalendar.getTime().getTime()-currentJiaoNaMonth.getTime()>0){
                    //社保的停缴月
                    Date gjjTingjiaoDate = customerShebaoBean.getGjjStopDate();
                    if(!StringUtils.isEmpty(gjjTingjiaoDate)){
                        Calendar gjjTingjiao = Calendar.getInstance();
                        gjjTingjiao.setTime(gjjTingjiaoDate);
                        if(gjjTingjiaoDate.getTime()-gjjDimissCalendar.getTime().getTime()>0){
                            returnMap.put("gjjStopDateStr",gjjDimissCalendar.get(Calendar.YEAR)+"年"+(gjjDimissCalendar.get(Calendar.MONTH)+1)+"月");
                        }else {
                            returnMap.put("gjjStopDateStr",gjjTingjiao.get(Calendar.YEAR)+"年"+(gjjTingjiao.get(Calendar.MONTH)+1)+"月");
                        }
                    }else{
                        returnMap.put("gjjStopDateStr",gjjDimissCalendar.get(Calendar.YEAR)+"年"+(gjjDimissCalendar.get(Calendar.MONTH)+1)+"月");
                    }

                }else{
                    //等于
                    //已提交
                    searchMap.put("orderDate",gjjDimissCalendar.getTime());
                    companyShebaoOrderBean=companyShebaoService.selectShebaoOrderByMap(searchMap);
                    if(null!=companyShebaoOrderBean&&companyShebaoOrderBean.getStatus()!=1&&companyShebaoOrderBean.getStatus()!=0){
                        dimissCalendar.add(Calendar.MONTH,1);
                        returnMap.put("gjjStopDateStr",gjjDimissCalendar.get(Calendar.YEAR)+"年"+(gjjDimissCalendar.get(Calendar.MONTH)+1)+"月");
                    }else{
                        //未提交
                        returnMap.put("gjjStopDateStr",gjjDimissCalendar.get(Calendar.YEAR)+"年"+(gjjDimissCalendar.get(Calendar.MONTH)+1)+"月");
                    }

                }



            }else{
                //公积金未提交
                //判断是否有设置的停缴月
                if(!StringUtils.isEmpty(customerShebaoBean.getGjjStopDate())){
                    Calendar changeCalendar = Calendar.getInstance();
                    changeCalendar.setTime(customerShebaoBean.getGjjStopDate());
                    gjjStopDate=changeCalendar.get(Calendar.YEAR)+"年"+(changeCalendar.get(Calendar.MONTH)+1)+"月";
                }else{
                    gjjStopDate="";
                }
                returnMap.put("gjjStopDateStr",gjjStopDate);
                LOGGER.info("公积金未缴纳时，当前的公积金停缴月为："+gjjStopDate);
            }


        }catch(Exception e){
            LOGGER.error("获取离职员工停缴月份异常错误："+e.getMessage(), e);
        }
        return returnMap;
    }

    /**
     * 转换时间  将date 类型转成yyyy年MM月格式
     * @param date
     * @return
     */
    private String getStr(Date date) {

        if(!StringUtils.isEmpty(date)){
            Calendar ca = Calendar.getInstance();
            ca.setTime(date);
            int year =ca.get(Calendar.YEAR);
            int month = ca.get(Calendar.MONTH)+1;
            return year+"年"+month+"月";
        }else{
            return "";
        }
    }


    /**
     * 跳转到员工删除页面
     *
     * @return
     */
    @RequestMapping(value = "toCustomerDelete.htm")
    public ModelAndView jumpCustomerDelete(ModelAndView modelAndView, HttpServletRequest request) {

        long customerId = Long.valueOf(request.getParameter("customerId"));
        CustomersBean customersBean = customersService.selectNameById(customerId);
        String phoneNumber = request.getParameter("customerPhone");
        modelAndView.addObject("customerId", customerId);
        modelAndView.addObject("customerPhone", phoneNumber);
        modelAndView.addObject("trueName",customersBean.getCustomerTurename());
        modelAndView.setViewName("xtr/customer/customerDelete");
        return modelAndView;
    }


    /**
     * 员工删除操作
     *
     * @param request
     * @param customersDto
     * @return
     */
    @RequestMapping(value = "customerDelete.htm")
    @ResponseBody
    @SystemControllerLog(operation = "员工删除操作", modelName = "用户管理模块")
    public ResultResponse customerDelete(HttpServletRequest request, CustomersDto customersDto) {


        ResultResponse resultResponse = new ResultResponse();
        try {
            resultResponse = customersService.customerDelete(customersDto);

        } catch (BusinessException e) {
            resultResponse.setMessage(e.getMessage());
        }

        return resultResponse;
    }


    /**
     * 提交上传的新增用户EXCEL文件
     *
     * @param request
     * @param fileName
     */
    @RequestMapping("commitUploadFile.htm")
    @ResponseBody
    @SystemControllerLog(operation = "提交上传的新增用户EXCEL文件", modelName = "用户管理模块")
    public ResultResponse commitUploadFile(HttpServletRequest request, String fileName) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            LOGGER.info("提交上传的新增用户EXCEL文件,获取文件名称:" + fileName);
            if (!StringUtils.isStrNull(fileName)) {
                if (fileName.toLowerCase().endsWith(CustomerConstants.CUSTOMER_UPLOADTYPE_XLS)
                        || fileName.toLowerCase().endsWith(CustomerConstants.CUSTOMER_UPLOADTYPE_XLSX)) {
                    CompanysBean companysBean = SessionUtils.getCompany(request);
                    resultResponse = customersService.insertCustomersBatch(companysBean, fileName);
                } else {
                    LOGGER.info("提交上传的新增用户EXCEL文件,上传文件类型不正确");
                    resultResponse.setMessage("上传文件类型不正确");
                }
            } else {
                LOGGER.info("提交上传的新增用户EXCEL文件,上传文件类型不正确");
                resultResponse.setMessage("上传文件名称获取失败");
            }

        } catch (Exception e) {
            LOGGER.error("提交上传的新增用户EXCEL文件," + e.getMessage());
            resultResponse.setMessage(e.getMessage());
        }
        LOGGER.info("提交上传的新增用户EXCEL文件,返回结果:" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

//    /**
//     * 将新增用户的EXCEL文件上传到服务器
//     * @param file
//     * @return
//     */
//    @RequestMapping(value = "customerUploadBatch.htm")
//    @ResponseBody
//    public Map<String, Object> customerUploadBatch(@RequestParam("file") MultipartFile file){
//        Map<String, Object> result = new HashMap<>();
//        try {
//            String oldFileName = file.getOriginalFilename();
//            String suffix = oldFileName.substring(oldFileName.lastIndexOf(".") + 1);
//            String fileName = UUID.randomUUID().toString() + "." + suffix;
//            InputStream in = file.getInputStream();
//            String url = PropertyUtils.getString("oss.download.url") + fileName;
//            LOGGER.info("新增用户,上传服务器:url:"+url+",fileName:"+fileName);
//            AliOss.uploadFile(in, fileName, PropertyUtils.getString("oss.bucketName.img"));
//            result.put("state", true);
//            result.put("url", fileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//            result.put("state", false);
//            result.put("msg", "上传失败");
//        }
//        return result;
//    }

    /**
     * 批量上传用户
     * @param fileCode
     * @param fileName
     * @return
     */
    @RequestMapping(value = "customerUploadBatch.htm")
    @ResponseBody
    public ResultResponse customerUploadBatch(String fileCode, String fileName) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            if (!StringUtils.isStrNull(fileCode)) {
                fileCode = fileCode.substring(fileCode.indexOf(";base64,") + 8);
                BASE64Decoder decoder = new BASE64Decoder();
                //base64解码
                byte[] decoderBytes = decoder.decodeBuffer(fileCode);
                InputStream is = new ByteArrayInputStream(decoderBytes);
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                String newFileName = UUID.randomUUID().toString() + "." + suffix;
                //上传文件到服务器
                AliOss.uploadFile(is, newFileName, PropertyUtils.getString("oss.bucketName.img"));
                resultResponse.setSuccess(true);
                resultResponse.setData(newFileName);
            } else {
                LOGGER.info("批量上传用户EXCEl,传递参数不能为空");
                resultResponse.setMessage("传递参数不能为空");
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            resultResponse.setMessage("上传失败");
        }
        return resultResponse;
    }

    /**
     * 用户上传,下载文件
     *
     * @param response
     * @param fileName
     */
    @RequestMapping("downloadUserFile.htm")
    public void downloadUserFile(HttpServletResponse response, String fileName) {
        //设置没有缓存
        response.reset();
        InputStream browserStream = null;
        OutputStream outputStream = null;
        try {
            LOGGER.info("用户上传,下载文件,获取文件名称:" + fileName);
            browserStream = AliOss.downloadFile(PropertyUtils.getString("oss.bucketName.img"), fileName);
            if (browserStream != null) {
                response.setHeader("content-disposition", "attachment;filename=" + fileName);
                response.setContentType("application/octet-stream");
                outputStream = response.getOutputStream();
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = browserStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }
            }
        } catch (IOException e) {
            LOGGER.error("用户上传,下载文件," + e.getMessage());
        } finally {
            try {
                if (null != outputStream) {
                    outputStream.flush();
                    outputStream.close();
                }
                if (null != browserStream) {
                    browserStream.close();
                }
            } catch (IOException e) {
                LOGGER.error("用户上传,下载文件," + e.getMessage());
            }
        }
    }

    /**
     * 更新用户基本信息
     *
     * @param customersBean
     * @return
     */
    @RequestMapping(value = "modifyCustomerBase.htm")
    @ResponseBody
    @SystemControllerLog(operation = "更新用户基本信息", modelName = "用户管理模块")
    public ResultResponse modifyCustomerBase(CustomersBean customersBean, Long companyCheckId) {
        LOGGER.info("更新用户基本信息,传递基本信息参数:" + JSON.toJSONString(customersBean));
        ResultResponse resultResponse = new ResultResponse();
        try {
            if (customersBean != null && customersBean.getCustomerId() != null) {
                //判断银行输入的是否正确
               BankCodeBean bankCodeBean= bankCodeService.selectByBankName(customersBean.getCustomerBank());
                if(bankCodeBean==null){
                    resultResponse.setMessage("工资卡开户行输入错误");
                    return resultResponse;

                }
                resultResponse = customersService.modifyCustomerBase(customersBean, companyCheckId);
            } else {
                LOGGER.info("更新用户基本信息,传递参数不能为空");
                resultResponse.setMessage("传递参数不能为空");
            }
        } catch (Exception e) {
            LOGGER.error("更新用户基本信息" + e.getMessage());
            resultResponse.setMessage(e.getMessage());
        }
        LOGGER.info("更新用户基本信息,返回结果:" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 更新用户岗位信息
     *
     * @param customersStationBean
     * @return
     */
    @RequestMapping(value = "modifyCustomerStation.htm")
    @ResponseBody
    @SystemControllerLog(operation = "更新用户岗位信息", modelName = "用户管理模块")
    public ResultResponse modifyCustomerStation(CustomersStationBean customersStationBean, Long companyCheckStationId, Integer isRegular) {
        LOGGER.info("更新用户岗位信息,传递岗位信息参数:" + JSON.toJSONString(customersStationBean));
        ResultResponse resultResponse = new ResultResponse();
        try {
            if (customersStationBean != null && customersStationBean.getStationId() != null) {
                if (isRegular == 1) {
                    customersStationBean.setStationCustomerState(CustomerConstants.CUSTOMER_PERSONSTATE_REGULAR);
                } else if (isRegular == 2) {
                    customersStationBean.setStationCustomerState(CustomerConstants.CUSTOMER_PERSONSTATE_ENTER);
                }
                resultResponse = customersStationService.modifyCustomerStation(customersStationBean, companyCheckStationId);
            } else {
                LOGGER.info("更新用户岗位信息,传递参数不能为空");
                resultResponse.setMessage("传递参数不能为空");
            }
        } catch (BusinessException e) {
            LOGGER.error("更新用户岗位信息," + e.getMessage());
            resultResponse.setMessage(e.getMessage());
        } catch (ParseException e) {
            LOGGER.error("更新用户岗位信息," + e.getMessage());
            resultResponse.setMessage(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("更新用户岗位信息," + e.getMessage());
            resultResponse.setMessage(e.getMessage());
        }
        LOGGER.info("更新用户岗位信息,返回结果:" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 更新用户个人信息
     *
     * @param customersPersonalBean
     * @return
     */
    @RequestMapping(value = "modifyCustomerPersonal.htm")
    @ResponseBody
    @SystemControllerLog(operation = "更新用户个人信息", modelName = "用户管理模块")
    public ResultResponse modifyCustomerPersonal(CustomersPersonalBean customersPersonalBean) {
        LOGGER.info("更新用户个人信息,传递个人信息参数:" + JSON.toJSONString(customersPersonalBean));
        ResultResponse resultResponse = new ResultResponse();
        try {
            if(customersPersonalBean!=null){
                if (customersPersonalBean.getPersonalId() != null) {
                    customersPersonalBean.setPersonalUpdateTime(new Date());
                    if (StringUtils.isStrNull(customersPersonalBean.getPersonalNation())) {
                        customersPersonalBean.setPersonalNation("");
                    }
                    if (StringUtils.isStrNull(customersPersonalBean.getPersonalPoliticsStatus())) {
                        customersPersonalBean.setPersonalPoliticsStatus("");
                    }
                    if (StringUtils.isStrNull(customersPersonalBean.getPersonalCurrentPlace())) {
                        customersPersonalBean.setPersonalCurrentPlace("");
                    }
                    if (StringUtils.isStrNull(customersPersonalBean.getPersonalGraduateSchool())) {
                        customersPersonalBean.setPersonalGraduateSchool("");
                    }
                    if (StringUtils.isStrNull(customersPersonalBean.getPersonalMajor())) {
                        customersPersonalBean.setPersonalMajor("");
                    }
                    if (StringUtils.isStrNull(customersPersonalBean.getPersonalResume())) {
                        customersPersonalBean.setPersonalResume("");
                    }
                    if (customersPersonalBean.getPersonalEducationHigh() == null) {
                        customersPersonalBean.setPersonalEducationHigh(0);
                    }
                    int countPersonal = customersPersonalService.updateByPrimaryKeySelective(customersPersonalBean);
                    if (countPersonal <= 0) {
                        resultResponse.setMessage("更新用户个人信息失败");
                    } else {
                        resultResponse.setSuccess(true);
                    }
                } else {
                    customersPersonalBean.setPersonalCreateTime(new Date());
                    int countPersonal = customersPersonalService.insert(customersPersonalBean);
                    if (countPersonal <= 0) {
                        resultResponse.setMessage("新增用户个人信息失败");
                    } else {
                        resultResponse.setSuccess(true);
                    }
                }
            }

        } catch (Exception e) {
            LOGGER.error("更新用户个人信息" + e.getMessage());
            resultResponse.setMessage(e.getMessage());
        }
        LOGGER.info("更新用户个人信息,返回结果:" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 更新基本信息中的身份证复印件
     *
     * @param multipartFile
     * @return
     */
    @RequestMapping("uploadBaseIdCardImg.htm")
    @ResponseBody
    public ResultResponse uploadBaseIdCardImg(@RequestParam("file") MultipartFile multipartFile,String uploadType) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            String newFilename=UploadUtils.upload(multipartFile,uploadType);
            resultResponse.setSuccess(true);
            resultResponse.setData(newFilename);
//            if (multipartFile != null) {
//                String fileName = multipartFile.getOriginalFilename();
//                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
//                String newFileName = UUID.randomUUID().toString() + "." + suffix;
//                String[] filenames={"jpg", "png", "jpeg","bmp"};
//                boolean checkFlag = false;//上传文件类型是否符合规范,false代表不符合
//                for (String filenameCheck : filenames) {
//                    if(filenameCheck.equalsIgnoreCase(suffix)) {
//                        checkFlag = true;
//                        break;
//                    }
//                }
//                if(!checkFlag) {
//                    resultResponse.setMessage("图片格式为png,jpg,jpeg,bmp");
//                    return resultResponse;
//                }
//                long size=multipartFile.getSize();
//                size=size/1024/1024;
//                if(size>2){
//                    resultResponse.setMessage("图片不能超过2M");
//                    return resultResponse;
//                }
//                boolean result = AliOss.uploadFile(multipartFile.getInputStream(), newFileName, PropertyUtils.getString("oss.bucketName.img"));
//                if (result) {
//                    resultResponse.setSuccess(true);
//                    resultResponse.setData(newFileName);
//                } else {
//                    LOGGER.error("更新员工信息,上传文件失败");
//                    resultResponse.setMessage("上传文件失败");
//                }
//            } else {
//                LOGGER.error("更新员工信息,上传文件,但没有上传文件");
//                resultResponse.setMessage("没有上传文件");
//            }
        } catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("更新员工信息，上传文件"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("上传文件失败");
            LOGGER.error("更新员工信息，上传文件",e);
        }
        LOGGER.info("更新员工信息,上传文件,返回结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 获取服务器当前时间
     * @return
     */
    @RequestMapping("queryNowTime.htm")
    @ResponseBody
    public ResultResponse queryNowTime() {
        ResultResponse resultResponse = new ResultResponse();
        try {
            resultResponse.setData(DateUtil.dateFormatter.parse(DateUtil.dateFormatter.format(new Date())));
            resultResponse.setSuccess(true);
        } catch (ParseException e) {
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        return resultResponse;
    }

    //判断两个时间的月份之差
    public static int getMonthsOfAge(Calendar calendarBirth,
                                     Calendar calendarNow) {
        return Math.abs((calendarNow.get(Calendar.YEAR) - calendarBirth
                .get(Calendar.YEAR))* 12+ calendarNow.get(Calendar.MONTH)
                - calendarBirth.get(Calendar.MONTH));
    }

    /**
     * 判断离职日期与当前缴纳月的大小
     * @param dimissTime
     * @return
     */
    @RequestMapping(value="judgeDimissTime.htm")
    @ResponseBody
    public ResultResponse judgeDimissTimeWithCurrentTime(String dimissTime,Long customerId){

     ResultResponse resultResponse =  new ResultResponse();
     try{
         //判断当前员工是否有社保需求
         CustomerShebaoBean customerShebaoBean = customerShebaoService.selectCustomerShebaoByCustomerId(customerId);
         if(customerShebaoBean==null||StringUtils.isEmpty(customerShebaoBean.getCurrentMonth())){
            //不显示
            resultResponse.setSuccess(true);//不显示
         }else{
             if(!StringUtils.isStrNull(dimissTime)){
                 dimissTime = dimissTime.substring(0,7);
                 dimissTime=dimissTime+"-01";
                 SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
                 Date dim = sm.parse(dimissTime);
                 if(dim.getTime()-customerShebaoBean.getCurrentMonth().getTime()<0){
                     //不显示
                     resultResponse.setSuccess(true);
                 }
             }
         }
     }catch(Exception e){
      LOGGER.error("判断离职日期是否小于当前的缴纳月份出现异常错误："+e.getMessage());
     }
     return resultResponse;
    }


    /**
     * 跳转到员工管理页面(new)
     *
     * @param mav
     * @return
     */
    @RequestMapping("toCustomerMainPageNew.htm")
    public ModelAndView toCustomerMainPageNew(HttpServletRequest request, ModelAndView mav) {

        mav.setViewName("xtr/customer/new/customerMain");

        //判断请求是否来自首页
        if(!StringUtils.isStrNull(request.getParameter("from"))){
             mav.addObject("from","index");
        }
        //判断是否有员工,如果没有员工,则页面展示"新增员工指示图"
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        List<CustomersBean> customers=customersService.selectCustomersForCompanyId(companyMembersBean.getMemberCompanyId());
        if(customers!=null && customers.size()>0){//不展示
            mav.addObject("isShowGuid",1);
        }else{//展示
            mav.addObject("isShowGuid",2);
        }
        return mav;
    }

    /**
     * 查询用户信息列表
     *
     * @param response
     * @param customersDto
     */
    @RequestMapping("queryListByCondition.htm")
    @ResponseBody
    public void queryListByCondition(HttpServletRequest request, HttpServletResponse response, CustomersDto customersDto, Integer type) {

        LOGGER.info("查询用户信息列表,传递参数："+ JSON.toJSONString(customersDto)+",类型:"+type);
        ResultResponse resultResponse=new ResultResponse();
        try{
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            resultResponse=customersService.queryListByCondition( type, customersDto, companyMembersBean.getMemberCompanyId());
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("查询用户信息列表"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("查询用户信息列表失败,请重试或联系管理员");
            LOGGER.error("查询用户信息列表",e);
        }
        LOGGER.info("查询用户信息列表,返回结果："+ JSON.toJSONString(resultResponse));
        HtmlUtil.writerJson(response, resultResponse);
    }

    /**
     * 查询用户数量
     *
     * @param response
     */
    @RequestMapping("queryCountByCondition.htm")
    @ResponseBody
    public void queryCountByCondition(HttpServletRequest request, HttpServletResponse response) {

        //获取登录用户
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        //获取入职\离职\待转正\未补全资料的人数
        CustomersDto customersCountDto = new CustomersDto();
        customersCountDto.setCustomerCompanyId(companyMembersBean.getMemberCompanyId());
        long startDateAssemBaseAmount = System.currentTimeMillis();
        customersCountDto.setShowIndexType(CustomerConstants.CUSTOMER_OPERATIONTYPE_ALL);
        int totalCount = customersService.queryCustomerCount(customersCountDto,CustomerConstants.CUSTOMER_OPERATIONTYPE_ALL);

        customersCountDto.setShowIndexType(CustomerConstants.CUSTOMER_OPERATIONTYPE_REGULAR);
        int enterCount = customersService.queryCustomerCount(customersCountDto,CustomerConstants.CUSTOMER_OPERATIONTYPE_REGULAR);

        customersCountDto.setShowIndexType(CustomerConstants.CUSTOMER_OPERATIONTYPE_TRY);
        int tryCount = customersService.queryCustomerCount(customersCountDto,CustomerConstants.CUSTOMER_OPERATIONTYPE_TRY);

        customersCountDto.setShowIndexType(CustomerConstants.CUSTOMER_OPERATIONTYPE_WILLLEAVE);
        int willCount = customersService.queryCustomerCount(customersCountDto,CustomerConstants.CUSTOMER_OPERATIONTYPE_WILLLEAVE);

        customersCountDto.setShowIndexType(CustomerConstants.CUSTOMER_OPERATIONTYPE_LEAVE);
        int leaveCount = customersService.queryCustomerCount(customersCountDto,CustomerConstants.CUSTOMER_OPERATIONTYPE_LEAVE);
        LOGGER.info("获取员工数量执行时间：" + (System.currentTimeMillis()-startDateAssemBaseAmount));
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setTotalCount(totalCount);
        customerResponse.setEnterCount(enterCount);
        customerResponse.setLeaveCount(leaveCount);
        customerResponse.setWillCount(willCount);
        customerResponse.setTryCount(tryCount);
        LOGGER.info("查询用户数量,返回结果:" + JSON.toJSONString(customerResponse));
        HtmlUtil.writerJson(response, customerResponse);
    }

    /**
     * 新增员工(新)
     *
     * @param request
     * @param customersDto
     * @return
     */

    @RequestMapping(value = "cusomerAddNew.htm")
    @ResponseBody
    @SystemControllerLog(operation = "新增员工", modelName = "用户管理模块")
    public void cusomerAddNew(HttpServletRequest request, HttpServletResponse response,CustomersDto customersDto,Integer operationType) {

        LOGGER.info("新增员工,传递参数："+ JSON.toJSONString(customersDto)+",类型:"+operationType);
        ResultResponse resultResponse=new ResultResponse();
        try{
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            customersDto.setCustomerCompanyId(companyMembersBean.getMemberCompanyId());
            //新增员工
            long customerId=customersService.createCustomer( customersDto, operationType);

            //获取当前计薪周期
            PayCycleBean payCycleBean = payCycleService.selectByCompanyId(customersDto.getCustomerCompanyId());
            if(payCycleBean != null){
                try{
                    //新增工资单X
                    payrollAccountService.generatePayroll(customersDto.getCustomerCompanyId(),customerId);
                    // 调用定薪接口
                    CustomersBean customersBean = customerUpdateSalaryService.setCustomerSalary(customerId, customersDto.getCustomerProbationSalary(), customersDto.getCustomerRegularSalary());

                    //员工未入职
                    if (customersBean != null){
                        customerUpdateSalaryService.updatePayRoll(customersBean);
                    }
                }catch(BusinessException e){
                    LOGGER.error("新增员工生成工资单失败"+e.getMessage());
                }catch(Exception e){
                    LOGGER.error("新增员工生成工资单失败"+e.getMessage(),e);
                }
            }
            else{
                LOGGER.info("新增员工customerId=" + customerId + ",该公司未设置计薪周期");
            }

            resultResponse.setSuccess(true);
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error("新增员工"+e.getMessage(),e);
        }catch(Exception e){
            resultResponse.setMessage("新增员工失败,请重试或联系管理员");
            LOGGER.error("新增员工"+e.getMessage(),e);
        }
        LOGGER.info("新增员工,返回结果："+ JSON.toJSONString(resultResponse));
        HtmlUtil.writerJson(response, resultResponse);
    }

    /**
     * 获取调薪初始化数据
     *
     * @param customerId
     * @return
     */

    @RequestMapping(value = "updateSalaryInit.htm")
    @ResponseBody
    public void updateSalaryInit(HttpServletResponse response, Long customerId) {
        LOGGER.info("获取调薪初始化数据,传递参数："+ customerId);
        ResultResponse resultResponse=new ResultResponse();
        try{
            CustomerResponse customerResponse=customersService.updateSalaryInit(customerId);
            resultResponse.setSuccess(true);
            resultResponse.setData(customerResponse);
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("获取调薪初始化数据"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("获取调薪初始化数据失败,请重试或联系管理员");
            LOGGER.error("获取调薪初始化数据",e);
        }
        LOGGER.info("获取调薪初始化数据,返回结果："+ JSON.toJSONString(resultResponse));
        HtmlUtil.writerJson(response, resultResponse);
    }

    /**
     * 跳转到员工信息页面(new)
     *
     * @param mav
     * @return
     */
    @RequestMapping("toCustomerEditPageNew.htm")
    public ModelAndView toCustomerEditPageNew(HttpServletRequest request, ModelAndView mav,Long customerId) {

        try{
            CustomersDto customersDto = customersService.selectCustomersByCustomerId(customerId);
            mav.addObject("customersDto", customersDto);
            mav.addObject("customerId", customerId);
            //社保公积金缴纳信息,如果未缴纳,则为空
            CustomerShebaoBean  shebaoBean=customerShebaoService.selectCustomerShebaoByCustomerId(customerId);
            mav.addObject("shebaoBean", shebaoBean);
            //是否有有效的协议
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            customersDto.setCustomerCompanyId(companyMembersBean.getMemberCompanyId());
            int count=companyProtocolsService.selectUsefulProtocolForCompany( companyMembersBean.getMemberCompanyId());
            mav.addObject("userFulCount", count);
            mav.setViewName("xtr/customer/new/customerEdit");
        }catch(Exception e){
            LOGGER.error("跳转到员工信息页面异常",e);
        }
        return mav;
    }

    /**
     * 更新用户基本信息(新)
     *
     * @param customersBean
     * @return
     */
    @RequestMapping(value = "modifyCustomerBaseNew.htm")
    @ResponseBody
    @SystemControllerLog(operation = "更新用户基本信息", modelName = "用户管理模块")
    public ResultResponse modifyCustomerBaseNew(HttpServletRequest request,CustomersBean customersBean) {
        LOGGER.info("更新用户基本信息,传递基本信息参数:" + JSON.toJSONString(customersBean));
        ResultResponse resultResponse = new ResultResponse();
        try {
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            resultResponse = customersService.modifyCustomerBaseNew(customersBean, companyMembersBean.getMemberCompanyId());
        } catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("更新用户基本信息"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("更新用户基本信息失败,请重试或联系管理员");
            LOGGER.error("更新用户基本信息",e);
        }
        LOGGER.info("更新用户基本信息,返回结果:" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 更新用户身份证信息(新)
     *
     * @param customersBean
     * @return
     */
    @RequestMapping(value = "modifyCustomerIdcardNew.htm")
    @ResponseBody
    @SystemControllerLog(operation = "更新用户身份证信息", modelName = "用户管理模块")
    public ResultResponse modifyCustomerIdcardNew(HttpServletRequest request,CustomersBean customersBean) {
        LOGGER.info("更新用户身份证信息,传递基本信息参数:" + JSON.toJSONString(customersBean));
        ResultResponse resultResponse = new ResultResponse();
        try {
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            resultResponse = customersService.modifyCustomerIdcardNew(customersBean, companyMembersBean.getMemberCompanyId());
        } catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("更新用户身份证信息"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("更新用户基本信息失败,请重试或联系管理员");
            LOGGER.error("更新用户身份证信息",e);
        }
        LOGGER.info("更新用户身份证信息,返回结果:" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 上传身份证图片
     * @param multipartFile
     * @param response
     * @return
     */
    @RequestMapping("uploadIdCardImgNew.htm")
    @ResponseBody
    public String uploadIdCardImgNew(@RequestParam("file") MultipartFile multipartFile, HttpServletResponse response) {
        LOGGER.info("上传身份证图片");
        response.setContentType("text/html; charset=utf-8");
        ResultResponse resultResponse = new ResultResponse();
        try {
            String fileName= UploadUtils.upload(multipartFile,"img");
            resultResponse.setSuccess(true);
            resultResponse.setData(fileName);
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("上传身份证图片"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("上传身份证图片失败,请重试或联系管理员");
            LOGGER.error("上传身份证图片",e);
        }
        LOGGER.info("上传身份证图片,返回结果："+ JSON.toJSONString(resultResponse));
        return JSON.toJSONString(resultResponse);
    }

    /**
     * 更新用户岗位信息(新)
     *
     * @param customersStationBean
     * @return
     */
    @RequestMapping(value = "modifyCustomerStationNew.htm")
    @ResponseBody
    @SystemControllerLog(operation = "更新用户岗位信息", modelName = "用户管理模块")
    public ResultResponse modifyCustomerStationNew(HttpServletRequest request,CustomersStationBean customersStationBean) {
        LOGGER.info("更新用户岗位信息,传递岗位信息参数:" + JSON.toJSONString(customersStationBean));
        ResultResponse resultResponse = new ResultResponse();
        try {
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            resultResponse = customersStationService.modifyCustomerStationNew(customersStationBean,companyMembersBean.getMemberCompanyId());
        } catch (BusinessException e) {
            LOGGER.error("更新用户岗位信息," + e.getMessage(),e);
            resultResponse.setMessage(e.getMessage());
        }  catch (Exception e) {
            LOGGER.error("更新用户岗位信息," + e.getMessage(),e);
            resultResponse.setMessage(e.getMessage());
        }
        LOGGER.info("更新用户岗位信息,返回结果:" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 跳转到补全员工资料页面(new)
     *
     * @param mav
     * @return
     */
    @RequestMapping("toCustomerCompletionPageNew.htm")
    public ModelAndView toCustomerCompletionPageNew(HttpServletRequest request, ModelAndView mav,Long customerId) {
        try{
//            CustomersDto customersDto = customersService.selectCustomersByCustomerId(customerId);
//            mav.addObject("customersDto", customersDto);
//            mav.addObject("customerId", customerId);
//            //社保公积金缴纳信息,如果未缴纳,则为空
//            CustomerShebaoBean  shebaoBean=customerShebaoService.selectCustomerShebaoByCustomerId(customerId);
//            mav.addObject("shebaoBean", shebaoBean);
//            //是否有有效的协议
//            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
//            customersDto.setCustomerCompanyId(companyMembersBean.getMemberCompanyId());
//            int count=companyProtocolsService.selectUsefulProtocolForCompany( companyMembersBean.getMemberCompanyId());
//            mav.addObject("userFulCount", count);
            mav.setViewName("xtr/customer/new/customerCompletion");
        }catch(Exception e){
            LOGGER.error("跳转到员工信息页面异常",e);
        }
        return mav;
    }

    /**
     * 查询未补全员工资料信息列表
     *
     * @param response
     * @param customersDto
     */
    @RequestMapping("queryInCompleteCustomerList.htm")
    @ResponseBody
    public void queryInCompleteCustomerList(HttpServletRequest request, HttpServletResponse response, CustomersDto customersDto) {

        LOGGER.info("查询未补全员工资料信息列表,传递参数："+ JSON.toJSONString(customersDto));
        ResultResponse resultResponse=new ResultResponse();
        try{
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            resultResponse=customersService.queryInCompleteCustomerList(  customersDto, companyMembersBean.getMemberCompanyId());
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("查询未补全员工资料信息列表"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("查询未补全员工资料信息列表失败,请重试或联系管理员");
            LOGGER.error("查询未补全员工资料信息列表",e);
        }
        LOGGER.info("查询未补全员工资料信息列表,返回结果："+ JSON.toJSONString(resultResponse));
        HtmlUtil.writerJson(response, resultResponse);
    }

    /**
     * 更改未补全的信息
     *
     * @param response
     * @param customersBean
     */
    @RequestMapping("uptInCompleteCustomer.htm")
    @ResponseBody
    public void uptInCompleteCustomer(HttpServletRequest request, HttpServletResponse response, CustomersBean customersBean,Integer type) {

        LOGGER.info("查询未补全员工资料信息列表,传递参数："+ JSON.toJSONString(customersBean)+"类型:"+type);
        ResultResponse resultResponse=new ResultResponse();
        try{
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            resultResponse=customersService.uptInCompleteCustomer(  customersBean, companyMembersBean.getMemberCompanyId(),type);
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("查询未补全员工资料信息列表"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("查询未补全员工资料信息列表失败,请重试或联系管理员");
            LOGGER.error("查询未补全员工资料信息列表",e);
        }
        LOGGER.info("查询未补全员工资料信息列表,返回结果："+ JSON.toJSONString(resultResponse));
        HtmlUtil.writerJson(response, resultResponse);
    }

    /**
     * 发送未补全资料员工手机短信
     *
     * @param response
     */
    @RequestMapping("sendInPlemeteMsg.htm")
    @ResponseBody
    public void sendInPlemeteMsg(HttpServletRequest request, HttpServletResponse response) {

        ResultResponse resultResponse=new ResultResponse();
        try{
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            customersService.sendInPlemeteMsg(  companyMembersBean.getMemberCompanyId());
            resultResponse.setSuccess(true);
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("发送未补全资料员工手机短信"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("发送未补全资料员工手机短信失败,请重试或联系管理员");
            LOGGER.error("发送未补全资料员工手机短信",e);
        }
        LOGGER.info("发送未补全资料员工手机短信,返回结果："+ JSON.toJSONString(resultResponse));
        HtmlUtil.writerJson(response, resultResponse);
    }

    /**
     * 获取要发送的未补全信息数量
     *
     * @param response
     */
    @RequestMapping("querySendInPlemeteMsgCount.htm")
    @ResponseBody
    public void querySendInPlemeteMsgCount(HttpServletRequest request, HttpServletResponse response) {

        ResultResponse resultResponse=new ResultResponse();
        try{

            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            int count=customersService.querySendInPlemeteMsgCount(  companyMembersBean.getMemberCompanyId());
            resultResponse.setSuccess(true);
            resultResponse.setMessage(String.valueOf(count));
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("获取要发送的未补全信息数量"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("获取要发送的未补全信息数量失败,请重试或联系管理员");
            LOGGER.error("获取要发送的未补全信息数量",e);
        }
        LOGGER.info("获取要发送的未补全信息数量,返回结果："+ JSON.toJSONString(resultResponse));
        HtmlUtil.writerJson(response, resultResponse);
    }

    /**
     * 批量上传用户(新)
     * @param file
     * @return
     */
    @RequestMapping(value = "customerUploadBatchNew.htm")
    @ResponseBody
    public ResultResponse customerUploadBatchNew(HttpServletRequest request,MultipartFile file) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            LOGGER.info("批量上传用户");
            if (!StringUtils.isStrNull(file.getOriginalFilename())) {
                if (file.getOriginalFilename().toLowerCase().endsWith(CustomerConstants.CUSTOMER_UPLOADTYPE_XLS)
                        || file.getOriginalFilename().toLowerCase().endsWith(CustomerConstants.CUSTOMER_UPLOADTYPE_XLSX)) {
                    CompanysBean companysBean = SessionUtils.getCompany(request);
                    InputStream inputStream = file.getInputStream();
                    ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                    byte[] buff = new byte[100];
                    int rc = 0;
                    while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                        swapStream.write(buff, 0, rc);
                    }
                    byte[] in2b = swapStream.toByteArray();
                    //批量上传用户
                    resultResponse = customersService.customerUploadBatchNew(companysBean, in2b);
                    if(resultResponse.isSuccess()){
                        //批量生成工资单
                        List<CustomersBean> willAddPhoneDto=resultResponse.getData()==null?null:(List<CustomersBean>)resultResponse.getData();
                        if (willAddPhoneDto != null && willAddPhoneDto.size() > 0) {
                            //获取当前计薪周期
                            PayCycleBean payCycleBean = payCycleService.selectByCompanyId(companysBean.getCompanyId());
                            if(payCycleBean != null){
                                final List<Exception> errorList = new ArrayList();
                                List<Future> rowResult = new CopyOnWriteArrayList<Future>();
                                int size = willAddPhoneDto.size();
                                ExecutorService pool = Executors.newFixedThreadPool(size > 10 ? 10 : size);
                                for (final CustomersBean insertCustomerBean2 : willAddPhoneDto) {
                                    rowResult.add(pool.submit(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                generatePayrollBatch(insertCustomerBean2);
                                            } catch (Exception e) {
                                                //添加一次信息
                                                errorList.add(e);
                                                //输出异常信息
                                                LOGGER.error("批量上传用户【" + insertCustomerBean2.getCustomerTurename() + "】出现异常···" + e.getMessage(), e);
                                            }
                                        }
                                    }));
                                }
                                //等待处理结果
                                for (Future f : rowResult) {
                                    f.get();
                                }
                                //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用
                                pool.shutdown();
                                //出现异常
                                if (!errorList.isEmpty()) {
                                    //抛出异常信息
//                                    throw new BusinessException(errorList.get(0).getMessage());
                                }
                            }
                            else{
                                LOGGER.info("批量新增员工，,该公司（companyId=" + companysBean.getCompanyId() + "）未设置计薪周期");
                            }

                        }
                        resultResponse.setMessage(file.getOriginalFilename());
                    }
                } else {
                    LOGGER.info("批量上传用户,上传文件类型不正确");
                    resultResponse.setMessage("上传文件格式错误");
                }
            } else {
                LOGGER.info("批量上传用户,上传文件类型不正确");
                resultResponse.setMessage("上传文件名称获取失败");
            }

        } catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("批量上传用户"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("批量上传用户失败,请重试或联系管理员");
            LOGGER.error("批量上传用户",e);
        }
        LOGGER.info("批量上传用户,返回结果:" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    public void generatePayrollBatch(CustomersBean insertCustomerBean2) throws Exception {
        //新增工资单
        payrollAccountService.generatePayroll(insertCustomerBean2.getCustomerCompanyId(),insertCustomerBean2.getCustomerId());
        // 调用定薪接口
        CustomersBean customersBean2 = customerUpdateSalaryService.setCustomerSalary(insertCustomerBean2.getCustomerId(), insertCustomerBean2.getCustomerProbationSalary(), insertCustomerBean2.getCustomerRegularSalary());

        if (customersBean2 != null){
            customerUpdateSalaryService.updatePayRoll(customersBean2);
        }

    }

    /**
     * 批量上传未补全用户(新)
     * @param file
     * @return
     */
    @RequestMapping(value = "inCompleteUploadBatchNew.htm")
    @ResponseBody
    public ResultResponse inCompleteUploadBatchNew(HttpServletRequest request,MultipartFile file) {
        ResultResponse resultResponse = new ResultResponse();

        try {
            LOGGER.info("批量上传未补全用户");
            if (!StringUtils.isStrNull(file.getOriginalFilename())) {
                if (file.getOriginalFilename().toLowerCase().endsWith(CustomerConstants.CUSTOMER_UPLOADTYPE_XLS)
                        || file.getOriginalFilename().toLowerCase().endsWith(CustomerConstants.CUSTOMER_UPLOADTYPE_XLSX)) {
                    CompanysBean companysBean = SessionUtils.getCompany(request);
                    InputStream inputStream = file.getInputStream();
                    ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                    byte[] buff = new byte[100];
                    int rc = 0;
                    while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                        swapStream.write(buff, 0, rc);
                    }
                    swapStream.flush();
                    swapStream.close();
                    byte[] in2b = swapStream.toByteArray();
                    resultResponse = customersService.inCompleteUploadBatchNew(companysBean, in2b);
                    if(resultResponse.isSuccess()){
                        resultResponse.setData(file.getOriginalFilename());
                    }
                } else {
                    LOGGER.info("批量上传未补全用户,上传文件类型不正确");
                    resultResponse.setMessage("上传文件格式错误");
                }
            } else {
                LOGGER.info("批量上传未补全用户,上传文件类型不正确");
                resultResponse.setMessage("上传文件名称获取失败");
            }
        } catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("批量上传未补全用户"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("批量上传未补全用户失败,请重试或联系管理员");
            LOGGER.error("批量上传未补全用户",e);
        }
        LOGGER.info("批量上传未补全用户,返回结果:" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 员工转正(新)
     * @param request
     * @param customersDto
     * @return
     */
    @RequestMapping(value = "customerRegularNew.htm")
    @ResponseBody
    @SystemControllerLog(operation = "员工转正", modelName = "用户管理模块")
    public ResultResponse customerRegularNew(HttpServletRequest request, CustomersDto customersDto,String regularTimeOld) {

        ResultResponse resultResponse = new ResultResponse();

        try {
            if(customersDto==null){
                throw new BusinessException("无员工信息");
            }
            if(customersDto.getStationRegularTime()==null){
                throw new BusinessException("请选择更改后的转正时间");
            }
            if(!StringUtils.isStrNull(regularTimeOld)){
                String regularTimeNew=DateUtil.dateFormatter.format(customersDto.getStationRegularTime());
                if(regularTimeOld.equals(regularTimeNew)){
                    throw new BusinessException("转正时间并未发生变化");
                }
            }
            long companyId = SessionUtils.getUser(request).getMemberCompanyId();//员工的企业id
            customersDto.setCompanyId(companyId);
            resultResponse = customersService.customerRegular(customersDto);
        } catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("员工转正"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("员工转正失败,请重试或联系管理员");
            LOGGER.error("员工转正",e);
        }

        return resultResponse;
    }

    /**
     * 跳转到离职页面
     *
     * @param customerId
     * @return
     */

    @RequestMapping(value = "toCustomerDimissionPageNew.htm")
    @ResponseBody
    public ResultResponse toCustomerDimissionPageNew(Long customerId,Date dimissTime) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            if(customerId==null){
                throw new BusinessException("无员工信息");
            }
            CustomerResponse customerResponse=new CustomerResponse();
            //获取用户社保公积金基本信息
            CustomerShebaoBean customerShebaoBean=customerShebaoService.selectCustomerShebaoByCustomerId(customerId);
            if(customerShebaoBean!=null){
                if(customerShebaoBean.getIsSbKeep()!=null && customerShebaoBean.getIsSbKeep().intValue()== ShebaoConstants.KEEP_YES){
                    customerResponse.setShebaoState(1);
                }else{
                    customerResponse.setShebaoState(2);
                }
                if(customerShebaoBean.getIsGjjKeep()!=null && customerShebaoBean.getIsGjjKeep().intValue()== ShebaoConstants.KEEP_YES){
                    customerResponse.setGjjState(1);
                }else{
                    customerResponse.setGjjState(2);
                }
                if(customerShebaoBean.getCurrentMonth()!=null){
                    customerResponse.setCurrentMonth(Integer.parseInt(DateUtil.dayFormatter.format(customerShebaoBean.getCurrentMonth())));
                }

                if(customerShebaoBean.getSbStopDate()!=null){
                    customerResponse.setShebaoTjMonth(Integer.parseInt(DateUtil.dayFormatter.format(customerShebaoBean.getSbStopDate())));
                }
                if(customerShebaoBean.getGjjStopDate()!=null){
                    customerResponse.setGjjTjMonth(Integer.parseInt(DateUtil.dayFormatter.format(customerShebaoBean.getGjjStopDate())));
                }
                if(customerShebaoBean.getCurrentCompanyOrderId()!=null){
                    CompanyShebaoOrderBean companyShebaoOrderBean=companyShebaoService.selectByPrimaryKey(customerShebaoBean.getCurrentCompanyOrderId());
                    if(companyShebaoOrderBean==null){
//                        customerResponse.setCompanyState(2);
                        customerResponse.setShebaoState(2);
                        customerResponse.setGjjState(2);
                    }else if(companyShebaoOrderBean.getStatus()==null || companyShebaoOrderBean.getStatus().intValue()==ShebaoConstants.COMPANY_ORDER_INIT ||companyShebaoOrderBean.getStatus().intValue()==ShebaoConstants.COMPANY_ORDER_WILLSUBMIT ){
                        //提交截止日
                        Date orderLastTime=companyShebaoOrderBean.getOrderLastTime();
                        if(orderLastTime!=null){
                            int orderLastTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(orderLastTime));
                            int nowTimeInt=Integer.parseInt(DateUtil.dateFormatterNoLine.format(new Date()));
                            if(nowTimeInt<orderLastTimeInt){
                                customerResponse.setCompanyState(2);
                            }else{
                                customerResponse.setCompanyState(1);
                            }
                        }else{
                            customerResponse.setCompanyState(2);
                        }

                    }else if(companyShebaoOrderBean.getStatus().intValue()>ShebaoConstants.COMPANY_ORDER_WILLSUBMIT){
                        customerResponse.setCompanyState(1);
                    }else{
//                        customerResponse.setCompanyState(2);
                        customerResponse.setShebaoState(2);
                        customerResponse.setGjjState(2);
                    }
                    if(companyShebaoOrderBean!=null){//显示将要停缴的月份
                        if(companyShebaoOrderBean.getStatus()!=null){//已提交
                            if(customerShebaoBean.getCurrentMonth()!=null){
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(customerShebaoBean.getCurrentMonth());
                                cal.add(cal.MONTH, 1);
                                customerResponse.setWillTjMonth(Integer.parseInt(DateUtil.dayFormatter.format(cal.getTime())));
                            }
                        }else{
                            if(customerShebaoBean.getCurrentMonth()!=null){
                                customerResponse.setWillTjMonth(customerResponse.getCurrentMonth());
                            }
                        }
                    }
                }else{
                    customerResponse.setCompanyState(2);
                }
            }else{
                customerResponse.setShebaoState(2);
                customerResponse.setGjjState(2);
            }

            int dimissTimeInt=Integer.parseInt(DateUtil.dayFormatter.format(dimissTime));
            //社保分类
            if(customerResponse.getShebaoState().intValue()==2){//未缴纳
                customerResponse.setShebaoDimissShowType(1);
            }else{//缴纳
                int currentMonth=customerResponse.getCurrentMonth()==null?0:customerResponse.getCurrentMonth().intValue();
                if(currentMonth==0){
                    customerResponse.setShebaoDimissShowType(1);
                }else{
                    if(dimissTimeInt>currentMonth) {//离职月大于当前社保月
                        if(customerResponse.getShebaoTjMonth()!=null){
                            int shebaoTjMonthInt=customerResponse.getShebaoTjMonth();
                            if(shebaoTjMonthInt<=dimissTimeInt){
                                customerResponse.setShebaoDimissShowType(5);
                            }else{
                                customerResponse.setShebaoDimissShowType(2);
                            }
                        }else{
                            customerResponse.setShebaoDimissShowType(2);
                        }
                    }else if(dimissTimeInt<currentMonth){//离职月小于当前社保月
                        customerResponse.setShebaoDimissShowType(3);
                    }else if(dimissTimeInt==currentMonth){//离职月等于当前社保月
                        if(customerResponse.getCompanyState()!=null && customerResponse.getCompanyState().intValue()==1){//账单已经提交或关闭
                            customerResponse.setShebaoDimissShowType(4);
                        }else{//账单未提交或未到截止日
                            customerResponse.setShebaoDimissShowType(2);
                        }
                    }
                }
            }
            //公积金分类
            if(customerResponse.getGjjState().intValue()==2){//未缴纳
                customerResponse.setGjjDimissShowType(1);
            }else{//缴纳
                int currentMonth=customerResponse.getCurrentMonth()==null?0:customerResponse.getCurrentMonth().intValue();
                if(currentMonth==0){
                    customerResponse.setGjjDimissShowType(1);
                }else{
                    if(dimissTimeInt>currentMonth) {//离职月大于当前社保月
                        if(customerResponse.getGjjTjMonth()!=null){
                            int gjjTjMonthInt=customerResponse.getGjjTjMonth();
                            if(gjjTjMonthInt<=dimissTimeInt){
                                customerResponse.setGjjDimissShowType(5);
                            }else{
                                customerResponse.setGjjDimissShowType(2);
                            }
                        }else{
                            customerResponse.setGjjDimissShowType(2);
                        }
                    }else if(dimissTimeInt<currentMonth){//离职月小于当前社保月
                        customerResponse.setGjjDimissShowType(3);
                    }else if(dimissTimeInt==currentMonth){//离职月等于当前社保月
                        if(customerResponse.getCompanyState()!=null && customerResponse.getCompanyState().intValue()==1){//账单已经提交或关闭
                            customerResponse.setGjjDimissShowType(4);
                        }else{//账单未提交或未到截止日
                            customerResponse.setGjjDimissShowType(2);
                        }
                    }
                }
            }
            resultResponse.setSuccess(true);
            resultResponse.setData(customerResponse);
        } catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("员工转正"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("员工转正失败,请重试或联系管理员");
            LOGGER.error("员工转正",e);
        }
        return resultResponse;
    }


    /**
     * 员工离职
     *
     * @param request
     * @param customersDto
     * @return
     */

    @RequestMapping(value = "customerDimissionNew.htm")
    @ResponseBody
    @SystemControllerLog(operation = "员工离职", modelName = "用户管理模块")
    public ResultResponse customerDimissionNew(HttpServletRequest request, CustomersDto customersDto,Integer isSbKeepState,Integer isGjjKeepState) throws ParseException {

        ResultResponse resultResponse = new ResultResponse();
        Map returnMap =null;
        try {
            //后台判断离职日期是否小于当前日期
            Date dimissDate =customersDto.getStationDimissingTime();
            CompanysBean companysBean =SessionUtils.getCompany(request);
            customersDto.setCompanyId(companysBean.getCompanyId());
            resultResponse = customersService.customerDimissionNew(customersDto);
            resultResponse.setSuccess(true);
            if(resultResponse.isSuccess()){

                String dimissDateStr=new SimpleDateFormat("yyyy-MM-dd").format(dimissDate);
                returnMap = this.getSbStopDateNew(request,dimissDateStr,customersDto.getCustomerId(),isSbKeepState,isGjjKeepState);

                LOGGER.info("员工离职停缴月计算返回的map："+returnMap);
                if(!StringUtils.isEmpty(returnMap.get("message"))&&returnMap.get("message").equals("noSbDemand")){
                    //没有需求
                    resultResponse.setMessage(JSON.toJSON(returnMap).toString());
                    return resultResponse;
                }
                resultResponse.setMessage(JSON.toJSON(returnMap).toString());
                String sbStopDate = StringUtils.isEmpty(returnMap.get("sbStopDateStr"))==true?null:returnMap.get("sbStopDateStr").toString();
                String gjjStopDate =StringUtils.isEmpty(returnMap.get("gjjStopDateStr"))==true?null:returnMap.get("gjjStopDateStr").toString();

                if(!StringUtils.isEmpty(gjjStopDate)){
                    //调用停缴接口
                    SimpleDateFormat sm = new SimpleDateFormat("yyyy年MM月");
                    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
                    Date date = sm.parse(gjjStopDate);
                    String dateStr = sd.format(date);
                    //公积金停缴接口
                    TJShebaoDto gjjtj = new TJShebaoDto();
                    gjjtj.setCustomerId(customersDto.getCustomerId());
                    gjjtj.setRelationType(2);//公积金
                    try{
                        customerOrderShebaoService.stopPayment(gjjtj,dateStr);
                    }catch(BusinessException e){
                        LOGGER.error("员工离职调用停缴接口发生异常"+e.getMessage());
                    }catch (Exception e) {
                        LOGGER.error("员工离职调用停缴接口发生异常",e);
                    }
                }

                if(!StringUtils.isEmpty(sbStopDate)){
                    //调用停缴接口
                    SimpleDateFormat sm = new SimpleDateFormat("yyyy年MM月");
                    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM");
                    Date date = sm.parse(sbStopDate);
                    String dateStr = sd.format(date);
                    //社保停缴接口
                    TJShebaoDto sbtj = new TJShebaoDto();
                    sbtj.setCustomerId(customersDto.getCustomerId());
                    sbtj.setRelationType(1);//社保
                    try{
                        customerOrderShebaoService.stopPayment(sbtj,dateStr);
                    }catch(BusinessException e){
                        LOGGER.error("员工离职调用停缴接口发生异常"+e.getMessage());
                    }catch (Exception e) {
                        LOGGER.error("员工离职调用停缴接口发生异常",e);
                    }

                    LOGGER.info("调用社保停缴接口："+sbtj +"===="+dateStr);
                }


            }
        } catch(BusinessException e){
            resultResponse.setSuccess(false);
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("员工离职"+e.getMessage());
        }catch (Exception e) {
            resultResponse.setSuccess(false);
            resultResponse.setData(JSON.toJSON(returnMap).toString());
            resultResponse.setMessage("员工离职失败,请重试或联系管理员");
            LOGGER.error("员工离职",e);
        }

        return resultResponse;

    }

    /**
     * 算出社保与公积金的停缴月
     * @return
     */

    private Map<String,Object> getSbStopDateNew(HttpServletRequest request,String stationDimissingTime,Long customerId,Integer checkSb,Integer checkGjj) throws Exception{

        Map returnMap = new HashMap();
        String sbStopDate =null;//社保停缴月
        String gjjStopDate=null;//公积金停缴月

        try{
            CompanysBean companysBean = SessionUtils.getCompany(request);
            //公司Id
            Long companyId = companysBean.getCompanyId();
            LOGGER.info("获取员工停缴月参数companyId："+companyId);
            //计算社保 公积金状态是否为缴纳
            CustomerShebaoBean customerShebaoBean = customerShebaoService.selectCustomerShebaoByCustomerId(customerId);
            if(customerShebaoBean==null){
                // 不需要设置
                returnMap.put("message","noSbDemand");//没有社保需求
                return returnMap;
            }
            returnMap.put("message","");
            //获取当前离职时间
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM");
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate=sm.parse(stationDimissingTime);//离职日期
            Calendar chanCalendar = Calendar.getInstance();
            chanCalendar.setTime(currentDate);
            String str = chanCalendar.get(Calendar.YEAR)+"-"+(chanCalendar.get(Calendar.MONTH)+1)+"-01";
            currentDate =sd.parse(str);

            Calendar dimissCalendar = Calendar.getInstance();
            dimissCalendar.setTime(currentDate);//社保停缴月

            Calendar gjjDimissCalendar = Calendar.getInstance();
            gjjDimissCalendar.setTime(currentDate);

            Date currentJiaoNaMonth = customerShebaoBean.getCurrentMonth();//当前缴纳月份
            LOGGER.info("员工当前缴纳月："+currentJiaoNaMonth);


            //判断当前公司的社保公积金订单是否已提交
            String joinCityCode = customerShebaoBean.getJoinCityCode();
            Map searchMap = new HashMap();
            searchMap.put("companyId",companyId);
            searchMap.put("joinCityCode",joinCityCode);
            if(checkSb!=null){
                if(checkSb.intValue()==1){
                    dimissCalendar.add(Calendar.MONTH,1);
                }
            }
            if(checkGjj!=null){
                if(checkGjj.intValue()==1){
                    gjjDimissCalendar.add(Calendar.MONTH,1);//公积金的
                }
            }

            LOGGER.info("社保离职停缴月："+dimissCalendar.getTime());
            LOGGER.info("公积金离职停缴月："+gjjDimissCalendar.getTime());
            CompanyShebaoOrderBean companyShebaoOrderBean=null;
            //社保已缴纳
            if(!StringUtils.isEmpty(customerShebaoBean.getIsSbKeep())&&customerShebaoBean.getIsSbKeep()==1){

                if(dimissCalendar.getTime().getTime()-currentJiaoNaMonth.getTime()<0){

                    Calendar current = Calendar.getInstance();
                    current.setTime(currentJiaoNaMonth);
                    //查询社保订单是否已提交
                    searchMap.put("orderDate",currentJiaoNaMonth);

                    companyShebaoOrderBean = companyShebaoService.selectShebaoOrderByMap(searchMap);
                    if(null!=companyShebaoOrderBean&&companyShebaoOrderBean.getStatus()!=1&&companyShebaoOrderBean.getStatus()!=0){
                        current.add(Calendar.MONTH,1);
                        returnMap.put("sbStopDateStr",current.get(Calendar.YEAR)+"年"+(current.get(Calendar.MONTH)+1)+"月");
                    }else{
                        //未提交
                        returnMap.put("sbStopDateStr",current.get(Calendar.YEAR)+"年"+(current.get(Calendar.MONTH)+1)+"月");
                    }

                }else if(dimissCalendar.getTime().getTime()-currentJiaoNaMonth.getTime()>0){

                    //社保的停缴月
                    Date sbTingjiaoDate = customerShebaoBean.getSbStopDate();
                    if(!StringUtils.isEmpty(sbTingjiaoDate)){
                        //没有设置社保停缴月
                        Calendar sbTingjiao = Calendar.getInstance();
                        sbTingjiao.setTime(sbTingjiaoDate);
                        if(sbTingjiaoDate.getTime()-dimissCalendar.getTime().getTime()>0){
                            returnMap.put("sbStopDateStr",dimissCalendar.get(Calendar.YEAR)+"年"+(dimissCalendar.get(Calendar.MONTH)+1)+"月");
                        }else {
                            returnMap.put("sbStopDateStr",sbTingjiao.get(Calendar.YEAR)+"年"+(sbTingjiao.get(Calendar.MONTH)+1)+"月");
                        }
                    }else{
                        returnMap.put("sbStopDateStr",dimissCalendar.get(Calendar.YEAR)+"年"+(dimissCalendar.get(Calendar.MONTH)+1)+"月");
                    }

                }else{
                    //等于
                    //已提交
                    //判断订单是否已提交
                    searchMap.put("orderDate",dimissCalendar.getTime());
                    companyShebaoOrderBean = companyShebaoService.selectShebaoOrderByMap(searchMap);
                    if(null!=companyShebaoOrderBean&&companyShebaoOrderBean.getStatus()!=1&&companyShebaoOrderBean.getStatus()!=0){
                        dimissCalendar.add(Calendar.MONTH,1);
                        returnMap.put("sbStopDateStr",dimissCalendar.get(Calendar.YEAR)+"年"+(dimissCalendar.get(Calendar.MONTH)+1)+"月");
                    }else{
                        //未提交
                        returnMap.put("sbStopDateStr",dimissCalendar.get(Calendar.YEAR)+"年"+(dimissCalendar.get(Calendar.MONTH)+1)+"月");
                    }

                }
            }else{
                //社保未缴纳
                //判断是否有设置的停缴月
                if(!StringUtils.isEmpty(customerShebaoBean.getSbStopDate())){
                    Calendar changeCalendar = Calendar.getInstance();
                    changeCalendar.setTime(customerShebaoBean.getSbStopDate());
                    sbStopDate=changeCalendar.get(Calendar.YEAR)+"年"+(changeCalendar.get(Calendar.MONTH)+1)+"月";
                }else{
                    sbStopDate="";
                }
                returnMap.put("sbStopDateStr",sbStopDate);
                LOGGER.info("社保未缴纳时，当前的社保停缴月为："+sbStopDate);

            }



            //公积金已缴纳
            if(!StringUtils.isEmpty(customerShebaoBean.getIsGjjKeep())&&customerShebaoBean.getIsGjjKeep()==1){

                if(gjjDimissCalendar.getTime().getTime()-currentJiaoNaMonth.getTime()<0){
                    Calendar current = Calendar.getInstance();
                    current.setTime(currentJiaoNaMonth);
                    searchMap.put("orderDate",currentJiaoNaMonth);
                    companyShebaoOrderBean = companyShebaoService.selectShebaoOrderByMap(searchMap);
                    //查询社保订单是否已提交
                    if(null!=companyShebaoOrderBean&&companyShebaoOrderBean.getStatus()!=1&&companyShebaoOrderBean.getStatus()!=0){
                        gjjDimissCalendar.add(Calendar.MONTH,1);
                        returnMap.put("gjjStopDateStr",current.get(Calendar.YEAR)+"年"+(current.get(Calendar.MONTH)+1)+"月");
                    }else{
                        //未提交
                        returnMap.put("gjjStopDateStr",current.get(Calendar.YEAR)+"年"+(current.get(Calendar.MONTH)+1)+"月");
                    }

                }else if(gjjDimissCalendar.getTime().getTime()-currentJiaoNaMonth.getTime()>0){
                    //社保的停缴月
                    Date gjjTingjiaoDate = customerShebaoBean.getGjjStopDate();
                    if(!StringUtils.isEmpty(gjjTingjiaoDate)){
                        Calendar gjjTingjiao = Calendar.getInstance();
                        gjjTingjiao.setTime(gjjTingjiaoDate);
                        if(gjjTingjiaoDate.getTime()-gjjDimissCalendar.getTime().getTime()>0){
                            returnMap.put("gjjStopDateStr",gjjDimissCalendar.get(Calendar.YEAR)+"年"+(gjjDimissCalendar.get(Calendar.MONTH)+1)+"月");
                        }else {
                            returnMap.put("gjjStopDateStr",gjjTingjiao.get(Calendar.YEAR)+"年"+(gjjTingjiao.get(Calendar.MONTH)+1)+"月");
                        }
                    }else{
                        returnMap.put("gjjStopDateStr",gjjDimissCalendar.get(Calendar.YEAR)+"年"+(gjjDimissCalendar.get(Calendar.MONTH)+1)+"月");
                    }

                }else{
                    //等于
                    //已提交
                    searchMap.put("orderDate",gjjDimissCalendar.getTime());
                    companyShebaoOrderBean=companyShebaoService.selectShebaoOrderByMap(searchMap);
                    if(null!=companyShebaoOrderBean&&companyShebaoOrderBean.getStatus()!=1&&companyShebaoOrderBean.getStatus()!=0){
                        dimissCalendar.add(Calendar.MONTH,1);
                        returnMap.put("gjjStopDateStr",gjjDimissCalendar.get(Calendar.YEAR)+"年"+(gjjDimissCalendar.get(Calendar.MONTH)+1)+"月");
                    }else{
                        //未提交
                        returnMap.put("gjjStopDateStr",gjjDimissCalendar.get(Calendar.YEAR)+"年"+(gjjDimissCalendar.get(Calendar.MONTH)+1)+"月");
                    }

                }



            }else{
                //公积金未提交
                //判断是否有设置的停缴月
                if(!StringUtils.isEmpty(customerShebaoBean.getGjjStopDate())){
                    Calendar changeCalendar = Calendar.getInstance();
                    changeCalendar.setTime(customerShebaoBean.getGjjStopDate());
                    gjjStopDate=changeCalendar.get(Calendar.YEAR)+"年"+(changeCalendar.get(Calendar.MONTH)+1)+"月";
                }else{
                    gjjStopDate="";
                }
                returnMap.put("gjjStopDateStr",gjjStopDate);
                LOGGER.info("公积金未缴纳时，当前的公积金停缴月为："+gjjStopDate);
            }


        }catch(Exception e){
            LOGGER.error("获取离职员工停缴月份异常错误："+e.getMessage(), e);
        }
        return returnMap;
    }
}
