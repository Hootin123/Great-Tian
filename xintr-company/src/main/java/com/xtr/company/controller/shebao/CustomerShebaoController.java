package com.xtr.company.controller.shebao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.xtr.api.basic.BaseController;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanyShebaoOrderBean;
import com.xtr.api.domain.customer.CustomerShebaoBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.dto.customer.CustomersDto;
import com.xtr.api.dto.shebao.JrOrderDto;
import com.xtr.api.dto.shebao.ResultSheBaoDto;
import com.xtr.api.dto.shebao.TJShebaoDto;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.sbt.SbtService;
import com.xtr.api.service.shebao.CompanyShebaoService;
import com.xtr.api.service.shebao.CustomerShebaoOrderService;
import com.xtr.api.service.shebao.CustomerShebaoService;
import com.xtr.comm.annotation.SystemControllerLog;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CustomerConstants;
import com.xtr.comm.constant.ShebaoConstants;
import com.xtr.comm.enums.ShebaoTypeEnum;
import com.xtr.comm.sbt.api.Basic;
import com.xtr.comm.sbt.api.City;
import com.xtr.comm.sbt.api.SocialBase;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.StringUtils;
import com.xtr.company.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * @Author Xuewu
 * @Date 2016/9/13.
 */
@Controller
@RequestMapping("shebao")
public class CustomerShebaoController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerShebaoController.class);

    @Resource
    private CustomerShebaoService customerShebaoService;

    @Resource
    private CustomersService customersService;

    @Resource
    private SbtService sbtService;

    @Resource
    private CustomerShebaoOrderService customerShebaoOrderService;

    @Resource
    private CompanyShebaoService companyShebaoService;

    /**
     * 加载社保通数据为数据库的
     * @return
     */
    @RequestMapping("/loadData.htm")
    @ResponseBody
    public String loadDataFromDB(){
        try {
            sbtService.loadDataFromDB();
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
        return "success";
    }

    /**
     * 清空社保通所有数据缓存
     * @return
     */
    @RequestMapping("/cleanCache.htm")
    @ResponseBody
    public String cleanChace(){
        try {
            sbtService.cleanChace();
        }catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
        return "success";
    }

    @RequestMapping("/calBySbt.htm")
    @ResponseBody
    public ResultResponse calBySbt(double base, int requireType, String type, String joinCity){
        try {
            Basic basic = sbtService.getBasic(joinCity);
            SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(ShebaoTypeEnum.valueOf(requireType), basic, type);
            return ResultResponse.buildSuccess(customerShebaoService.cal(base, socialBase));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultResponse.buildFail(null).message("计算失败");
        }
    }



    @RequestMapping("/calOverBySbt.htm")
    @ResponseBody
    public ResultResponse calOverBySbt(@RequestBody JrOrderDto dto){
        try {
            Basic basic = sbtService.getBasic(dto.getCityCode());
            SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(ShebaoTypeEnum.valueOf(dto.getShebaoType()), basic, dto.getType());
            return ResultResponse.buildSuccess(customerShebaoService.calOverDueBySbt(dto.getOverOrders(), socialBase));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultResponse.buildFail(null).message("计算失败");
        }
    }

    //设置调基通知为已读
    @RequestMapping("/setShowBchg.htm")
    @ResponseBody
    public void setShowBchg(HttpServletRequest request){
        try {
            CompanyMembersBean loginUserObj = getLoginUserObj(request);
            loginUserObj.setShowBchg(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 列表
     * @return
     */
    @RequestMapping("/shebaoIndex.htm")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("xtr/shebao/socialHome");
        CompanyMembersBean loginUserObj = getLoginUserObj(request);

        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.HOUR, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        int minDayOfMonth = instance.getActualMinimum(Calendar.DAY_OF_MONTH);
        instance.set(Calendar.DAY_OF_MONTH, minDayOfMonth);
        Date startDate = instance.getTime();

        int maxDayOfMonth = instance.getActualMaximum(Calendar.DAY_OF_MONTH);
        instance.set(Calendar.DAY_OF_MONTH, maxDayOfMonth);
        Date endDate = instance.getTime();

        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        //获取入职\离职\待转正\未补全资料的人数
        CustomersDto customersCountDto = new CustomersDto();
        customersCountDto.setStartTime(startDate);
        customersCountDto.setEndTime(endDate);
        customersCountDto.setStartTimeStr(DateUtil.date2String(startDate, DateUtil.dateTimeString));
        customersCountDto.setEndTimeStr(DateUtil.date2String(endDate, DateUtil.dateTimeString));
        customersCountDto.setCustomerCompanyId(companyMembersBean.getMemberCompanyId());
        customersCountDto.setSelState(CustomerConstants.CUSTOMER_PERSONNUM_ALL);
        int totalCount = customersService.selectCustomerNumber(customersCountDto);
        customersCountDto.setSelState(CustomerConstants.CUSTOMER_PERSONNUM_ENTER);
        int enterCount = customersService.selectCustomerNumber(customersCountDto);

        modelAndView.addObject("totalCount", totalCount);
        modelAndView.addObject("enterCount", enterCount);
        modelAndView.addObject("sbCount", customerShebaoService.selectKeepCustomerCount(ShebaoTypeEnum.SHEBAO, companyMembersBean.getMemberCompanyId()));
        modelAndView.addObject("gjjCount", customerShebaoService.selectKeepCustomerCount(ShebaoTypeEnum.GJJ, companyMembersBean.getMemberCompanyId()));
        modelAndView.addObject("failedCount", customersService.selectShebaoFailedCount(companyMembersBean.getMemberCompanyId()));
        modelAndView.addObject("joinCites", customerShebaoService.selectJoinCityByCompanyId(companyMembersBean.getMemberCompanyId()));


        List<Map> keepSbAndGjjType = customerShebaoService.selectKeepSbAndGjjType(companyMembersBean.getMemberCompanyId());
        Set<String> bchgCities = Sets.newHashSet();
        if(!loginUserObj.isShowBchg()) {
            loginUserObj.setShowBchg(true);
            try {
                if (keepSbAndGjjType != null) {
                    for (Map map : keepSbAndGjjType) {
                        String joinCityCode = String.valueOf(map.get("joinCityCode"));
                        String type = String.valueOf(map.get("type"));
                        int requireType = Integer.valueOf(map.get("requireType") + "");
                        City city = sbtService.getCityByCode(joinCityCode);
                        Basic basic = sbtService.getBasic(joinCityCode);
                        SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(ShebaoTypeEnum.valueOf(requireType), basic, type);
                        String bchgmonth = socialBase.getBchgmonth();
                        String month = city.getMonth();
                        if (org.apache.commons.lang.StringUtils.isNotBlank(bchgmonth) && org.apache.commons.lang.StringUtils.isNotBlank(month)) {
                            if (bchgmonth.replace("-", "").equalsIgnoreCase(month)) {
                                bchgCities.add(city.getCname());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.info("可调基地区计算失败");
            }
        }

        //查询当前公司未读账单
        Long companyId = SessionUtils.getCompany(request).getCompanyId();
        try {
            long orderCount = customerShebaoOrderService.selectNoReadOrderCount(companyId);
            LOGGER.info("未读账单数量为："+orderCount);
            modelAndView.addObject("orderCount",orderCount);
        }catch (Exception e){
            LOGGER.error("查询当前公司未读账单出现错误："+e.getMessage(),e);
        }

        modelAndView.addObject("bchgCities", bchgCities);
        return modelAndView;
    }

    /**
     * 社保缴纳页
     * @return
     */
    @RequestMapping("/setShebao.htm")
    public ModelAndView setSehbao(@RequestParam long customerId, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("xtr/shebao/socialSet");
        CustomersBean customersBean = customersService.selectByPrimaryKey(customerId);
        List<City> cities = sbtService.getCities();
        modelAndView.addObject("customer", customersBean);
        modelAndView.addObject("cities", cities);

        try {
            CustomerShebaoBean customerShebaoBean = customerShebaoService.selectCustomerShebaoByCustomerId(customerId);
            if(customerShebaoBean != null && new Integer(0).equals(customerShebaoBean.getIsSbKeep()) && new Integer(2).equals(customerShebaoBean.getSbShebaotongStatus())) {//直接创建缴纳订单

                if(customerShebaoBean.getCurrentCompanyOrderId() == null) {
                    return new ModelAndView("redirect:" + request.getContextPath() + "/shebao/shebaoIndex.htm");
                }
                CompanyShebaoOrderBean companyShebaoOrderBean = companyShebaoService.selectByPrimaryKey(customerShebaoBean.getCurrentCompanyOrderId());
                customerShebaoOrderService.validateOrderDate(companyShebaoOrderBean);

                Basic basic = sbtService.getBasic(customerShebaoBean.getJoinCityCode());
                SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(ShebaoTypeEnum.SHEBAO, basic, customerShebaoBean.getSbType());

                int orderType = 2;
                BigDecimal base = customerShebaoBean.getSbBase();
                //验证基数是否超出范围，
                BigDecimal newBase = customerShebaoOrderService.resetBase(socialBase, customerShebaoBean, ShebaoTypeEnum.SHEBAO);
                if(newBase != null) {
                    orderType = 3;
                    base = newBase;
                }

                customerShebaoOrderService.cleanOrderByCustomerIdAndCompanyOrderId(customerId, customerShebaoBean.getCurrentCompanyOrderId(), 1, 1, 2, 3, 4, 5);
                customerShebaoOrderService.createOrder(customerId, orderType, customerShebaoBean.getCurrentCompanyOrderId(), ShebaoTypeEnum.SHEBAO, customerShebaoBean.getCurrentMonth(), base, socialBase);
                customerShebaoOrderService.updateCustomerOrderDesc(customerShebaoBean.getCurrentCompanyOrderId(), customerId, ShebaoTypeEnum.SHEBAO, orderType, DateUtil.sbtSimpleDateFormat.format(customerShebaoBean.getCurrentMonth()));

                customerShebaoOrderService.isNeedUpdateCompanyOrderDetail(companyShebaoOrderBean);

                customerShebaoBean.setSbStopDate(null);
                customerShebaoBean.setIsSbKeep(1);
                customerShebaoService.updateByPrimaryKey(customerShebaoBean);
                return new ModelAndView("redirect:" + request.getContextPath() + "/shebao/shebaoIndex.htm");
            }
        }catch (Exception e){
            e.printStackTrace();
//            return new ModelAndView("redirect:" + request.getContextPath() + "/shebao/shebaoIndex.htm");
        }


        return modelAndView;
    }

    /**
     * 社保补缴页
     * @return
     */
    @RequestMapping("/toBj.htm")
    public ModelAndView toBj(@RequestParam long customerId, @RequestParam int shebaoType) throws ParseException {
        ModelAndView modelAndView = new ModelAndView("xtr/shebao/socialBj");

        ShebaoTypeEnum shebaoTypeEnum = ShebaoTypeEnum.valueOf(shebaoType);

        CustomerShebaoBean customerShebaoBean = customerShebaoService.selectCustomerShebaoByCustomerId(customerId);
        Basic basic = sbtService.getBasic(customerShebaoBean.getJoinCityCode());

        SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(shebaoTypeEnum, basic, (shebaoTypeEnum == ShebaoTypeEnum.GJJ ? customerShebaoBean.getGjjType() : customerShebaoBean.getSbType()));

        JSONArray jsonArray = new JSONArray();
        if(shebaoTypeEnum == ShebaoTypeEnum.GJJ) {
            if(org.apache.commons.lang.StringUtils.isNotBlank(socialBase.getObmonth()) && org.apache.commons.lang.StringUtils.isNotBlank(socialBase.getOemonth())) {
                SocialBase gjjBjMonth = customerShebaoOrderService.getGjjBjMonth(DateUtil.sbtSimpleDateFormat.parse(socialBase.getObmonth().replace("-", "")), DateUtil.sbtSimpleDateFormat.parse(socialBase.getOemonth().replace("-", "")), customerId);
                socialBase.setObmonth(gjjBjMonth.getObmonth());
                socialBase.setOemonth(gjjBjMonth.getOemonth());
            }else{
                socialBase.setObmonth(null);
                socialBase.setOemonth(null);
            }
        }else{
            List<Date> list = customerShebaoOrderService.selectBjDate(customerId, customerShebaoBean.getCurrentCompanyOrderId(), ShebaoTypeEnum.GJJ);
            if(list != null) {
                for (Date date : list) {
                    jsonArray.add(DateUtil.sbtSimpleDateFormat.format(date));
                }
            }
        }
        modelAndView.addObject("needContains", jsonArray.toJSONString());
        modelAndView.addObject("customerShebaoBean", customerShebaoBean);
        modelAndView.addObject("socialBase", socialBase);
        modelAndView.addObject("shebaoType", shebaoType);

        Map map = customerShebaoOrderService.selectExistBjData(customerId, customerShebaoBean.getCurrentCompanyOrderId(), shebaoTypeEnum);
        if(map != null) {
            modelAndView.addObject("isEdit", true);
            modelAndView.addObject("bjData", map);
        }

        return modelAndView;
    }

    /**
     * 批量补缴页
     * @return
     */
    @RequestMapping("/toBatchBj.htm")
    public ModelAndView toBatchBj(@RequestParam int shebaoType){
        ModelAndView modelAndView = new ModelAndView("xtr/shebao/bathRepPayFund");
        return modelAndView;
    }


    /**
     * 社保缴纳页
     * @return
     */
    @RequestMapping("/setGjj.htm")
    public ModelAndView setGjj(@RequestParam long customerId, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("xtr/shebao/socialSet_gjj");
        CustomersBean customersBean = customersService.selectByPrimaryKey(customerId);
        modelAndView.addObject("customer", customersBean);
        CustomerShebaoBean customerShebao = customerShebaoService.selectCustomerShebaoByCustomerId(customerId);
        modelAndView.addObject("customerShebao", customerShebao);

        try {
            CustomerShebaoBean customerShebaoBean = customerShebaoService.selectCustomerShebaoByCustomerId(customerId);
            if(customerShebaoBean != null && new Integer(0).equals(customerShebaoBean.getIsGjjKeep()) && new Integer(2).equals(customerShebaoBean.getGjjShebaotongStatus())) {//直接创建缴纳订单

                if(customerShebaoBean.getCurrentCompanyOrderId() == null) {
                    return new ModelAndView("redirect:" + request.getContextPath() + "/shebao/shebaoIndex.htm");
                }
                CompanyShebaoOrderBean companyShebaoOrderBean = companyShebaoService.selectByPrimaryKey(customerShebaoBean.getCurrentCompanyOrderId());
                customerShebaoOrderService.validateOrderDate(companyShebaoOrderBean);

                Basic basic = sbtService.getBasic(customerShebaoBean.getJoinCityCode());
                SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(ShebaoTypeEnum.GJJ, basic, customerShebaoBean.getGjjType());

                int orderType = 2;//订单类型
                BigDecimal base = customerShebaoBean.getGjjBase();//订单基数
                BigDecimal newBase = customerShebaoOrderService.resetBase(socialBase, customerShebaoBean, ShebaoTypeEnum.GJJ);
                if(newBase != null) {//基数有变动  订单类型为调基、基数为新基数
                    orderType = 3;
                    base = newBase;
                }
                //清除已有订单
                customerShebaoOrderService.cleanOrderByCustomerIdAndCompanyOrderId(customerId, customerShebaoBean.getCurrentCompanyOrderId(), 2, 1, 2, 3, 4, 5);
                //创建新订单
                customerShebaoOrderService.createOrder(customerId, orderType, customerShebaoBean.getCurrentCompanyOrderId(), ShebaoTypeEnum.GJJ, customerShebaoBean.getCurrentMonth(), base, socialBase);
                //更新汇总信息
                customerShebaoOrderService.updateCustomerOrderDesc(customerShebaoBean.getCurrentCompanyOrderId(), customerId, ShebaoTypeEnum.GJJ, orderType, DateUtil.sbtSimpleDateFormat.format(customerShebaoBean.getCurrentMonth()));
                //更新企业订单
                customerShebaoOrderService.isNeedUpdateCompanyOrderDetail(companyShebaoOrderBean);

                customerShebaoBean.setGjjStopDate(null);
                customerShebaoBean.setIsGjjKeep(1);
                customerShebaoService.updateByPrimaryKey(customerShebaoBean);
                return new ModelAndView("redirect:" + request.getContextPath() + "/shebao/shebaoIndex.htm");
            }
        }catch (Exception e){
            e.printStackTrace();
//            return new ModelAndView("redirect:" + request.getContextPath() + "/shebao/shebaoIndex.htm");
        }

        return modelAndView;
    }

    /**
     * 列表页 分页接口
     * @param bean
     * @param request
     * @return
     */
    @RequestMapping("/dataList.htm")
    @ResponseBody
    public String dataList(CustomerShebaoBean bean, HttpServletRequest request) {
        Long loginCompanyId = getLoginCompanyId(request);
        bean.setCompanyId(loginCompanyId);
        return JSONObject.toJSONString(customerShebaoService.selectPage(bean));
    }

    /**
     * 获取所有可缴纳城市信息
     * @return
     */
    @RequestMapping("/getCities.htm")
    @ResponseBody
    public ResultResponse getCities() {
        return ResultResponse.buildSuccess(sbtService.getCities());
    }

    /**
     * 获取城市基本信息
     * @param cityCode
     * @return
     */
    @RequestMapping("/cityBasic.htm")
    @ResponseBody
    public Basic cityBasic(@RequestParam String cityCode) {
        return sbtService.getBasic(cityCode);
    }


    /**
     * 获取城市历史基础信息
     * @param cityCode
     * @param startMonth
     * @param endMonth
     * @return
     */
    @RequestMapping("/getHistoryBasic.htm")
    @ResponseBody
    public ResultResponse getHistoryBasic(String cityCode, String startMonth, String endMonth){
        List<Basic> supplementaryPay = sbtService.getSupplementaryPay(cityCode, startMonth, endMonth);
        return ResultResponse.buildSuccess(supplementaryPay);
    }

    /**
     * 缴纳社保
     * @param dto
     * @return
     */
    @RequestMapping("/jrsb.htm")
    @ResponseBody
    @SystemControllerLog(operation = "缴纳社保", modelName = "社保模块")
    public ResultResponse jrShebao(@RequestBody JrOrderDto dto, HttpServletRequest request) {
        Long loginCompanyId = getLoginCompanyId(request);
        try {
            boolean jr = customerShebaoOrderService.jrShebao(dto, loginCompanyId);
            if(jr) {
                CustomerShebaoBean customerShebaoBean = customerShebaoService.selectCustomerShebaoByCustomerId(dto.getCustomerId());
                CompanyShebaoOrderBean companyShebaoOrderBean = companyShebaoService.selectByPrimaryKey(customerShebaoBean.getCurrentCompanyOrderId());
                customerShebaoOrderService.isNeedUpdateCompanyOrderDetail(companyShebaoOrderBean);
                return ResultResponse.buildSuccess(null).message("操作成功");
            }
        }catch (BusinessException be) {
            return ResultResponse.buildFail(null).message(be.getMessage());
        }catch (Exception e) {
            return ResultResponse.buildFail(null).message("操作失败");
        }

        return ResultResponse.buildFail(null).message("操作失败");
    }


    /**
     * 缴纳公积金
     * @param dto
     * @return
     */
    @RequestMapping("/jrgjj.htm")
    @ResponseBody
    @SystemControllerLog(operation = "缴纳公积金", modelName = "社保模块")
    public ResultResponse jrGjj(@RequestBody JrOrderDto dto, HttpServletRequest request) {
        Long loginCompanyId = getLoginCompanyId(request);
        try {
            boolean jr = customerShebaoOrderService.jrGjj(dto, loginCompanyId);
            if(jr) {
                CustomerShebaoBean customerShebaoBean = customerShebaoService.selectCustomerShebaoByCustomerId(dto.getCustomerId());
                CompanyShebaoOrderBean companyShebaoOrderBean = companyShebaoService.selectByPrimaryKey(customerShebaoBean.getCurrentCompanyOrderId());
                customerShebaoOrderService.isNeedUpdateCompanyOrderDetail(companyShebaoOrderBean);
                return ResultResponse.buildSuccess(null).message("操作成功");
            }
        }catch (BusinessException be) {
            be.printStackTrace();
            return ResultResponse.buildFail(null).message(be.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return ResultResponse.buildFail(null).message("操作失败");
        }

        return ResultResponse.buildFail(null).message("操作失败");
    }

    /**
     * 补缴社保
     * @param dto
     * @return
     */
    @RequestMapping("/bj.htm")
    @ResponseBody
    @SystemControllerLog(operation = "社保补缴", modelName = "社保模块")
    public ResultResponse bjShebao(@RequestBody JrOrderDto dto, HttpServletRequest request) {
        try {
            CustomerShebaoBean customerShebaoBean = customerShebaoService.selectCustomerShebaoByCustomerId(dto.getCustomerId());
            ShebaoTypeEnum shebaoTypeEnum = ShebaoTypeEnum.valueOf(dto.getShebaoType());
            if(shebaoTypeEnum == null){
                return ResultResponse.buildSuccess(null).message("操作成功");
            }
            CompanyShebaoOrderBean companyShebaoOrderBean = companyShebaoService.selectByPrimaryKey(customerShebaoBean.getCurrentCompanyOrderId());
            customerShebaoOrderService.validateOrderDate(companyShebaoOrderBean);
            if(shebaoTypeEnum == ShebaoTypeEnum.SHEBAO) {
                customerShebaoOrderService.bj(dto.getOverOrders(), customerShebaoBean.getJoinCityCode(), customerShebaoBean.getSbType(), customerShebaoBean.getCurrentCompanyOrderId(), dto.getCustomerId(), shebaoTypeEnum);
            }else{
                customerShebaoOrderService.bj(dto.getOverOrders(), customerShebaoBean.getJoinCityCode(), customerShebaoBean.getGjjType(), customerShebaoBean.getCurrentCompanyOrderId(), dto.getCustomerId(), shebaoTypeEnum);
            }
            customerShebaoOrderService.isNeedUpdateCompanyOrderDetail(companyShebaoOrderBean);
            return ResultResponse.buildSuccess(null).message("操作成功");
        }catch (BusinessException be) {
            return ResultResponse.buildFail(null).message(be.getMessage());
        }catch (Exception e) {
            return ResultResponse.buildFail(null).message("操作失败");
        }
    }

    /**
     * 清除补缴社保
     * @param dto
     * @return
     */
    @RequestMapping("/cleanBj.htm")
    @ResponseBody
    public ResultResponse cleanBj(@RequestBody JrOrderDto dto, HttpServletRequest request) {
        try {
            CustomerShebaoBean customerShebaoBean = customerShebaoService.selectCustomerShebaoByCustomerId(dto.getCustomerId());
            ShebaoTypeEnum shebaoTypeEnum = ShebaoTypeEnum.valueOf(dto.getShebaoType());
            if(shebaoTypeEnum == null){
                return ResultResponse.buildSuccess(null).message("操作失败");
            }
            CompanyShebaoOrderBean companyShebaoOrderBean = companyShebaoService.selectByPrimaryKey(customerShebaoBean.getCurrentCompanyOrderId());
            customerShebaoOrderService.validateOrderDate(companyShebaoOrderBean);
            customerShebaoOrderService.cleanBj(companyShebaoOrderBean.getId(), dto.getCustomerId(), shebaoTypeEnum);
            customerShebaoOrderService.isNeedUpdateCompanyOrderDetail(companyShebaoOrderBean);
            return ResultResponse.buildSuccess(null).message("操作成功");
        }catch (BusinessException be) {
            return ResultResponse.buildFail(null).message(be.getMessage());
        }catch (Exception e) {
            return ResultResponse.buildFail(null).message("操作失败");
        }
    }

    /**
     * 社保公积金停缴验证
     * @param dto
     * @return
     */
    @RequestMapping("/paymentValidator.htm")
    @ResponseBody
    public ResultResponse paymentValidator(TJShebaoDto dto) {
        ResultResponse resultResponse=new ResultResponse();
        LOGGER.info("社保公积金停缴验证,传递参数:"+ JSON.toJSONString(dto));
        try {
            ResultSheBaoDto resultSheBaoDto=customerShebaoOrderService.validateStopPaymentBase(dto);
            resultResponse.setData(resultSheBaoDto);
            if(!StringUtils.isStrNull(resultSheBaoDto.getAlreadyStopInfo()) && "sureMsg".equals(resultSheBaoDto.getAlreadyStopInfo())){
                throw new BusinessException("sureMsg");
            }
            resultResponse.setSuccess(true);
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("社保公积金停缴验证,"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("社保公积金停缴验证失败");
            LOGGER.error("社保公积金停缴验证",e);
        }
        LOGGER.info("社保公积金停缴验证,返回结果:"+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 公积金取消停缴验证
     * @param dto
     * @return
     */
    @RequestMapping("/cancelGjjValidator.htm")
    @ResponseBody
    public ResultResponse cancelGjjValidator(TJShebaoDto dto) {
        ResultResponse resultResponse=new ResultResponse();
        LOGGER.info("公积金取消停缴验证,传递参数:"+ JSON.toJSONString(dto));
        try {
            boolean result=customerShebaoOrderService.cancelGjjValidator(dto);
            resultResponse.setSuccess(true);
            resultResponse.setData(result);
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("公积金取消停缴验证,"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("公积金取消停缴验证失败");
            LOGGER.error("公积金取消停缴验证",e);
        }
        LOGGER.info("公积金取消停缴验证,返回结果:"+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }
    /**
     * 社保公积金停缴
     * @param dto
     * @return
     */
    @RequestMapping("/stopPayment.htm")
    @ResponseBody
    @SystemControllerLog(operation = "社保公积金停缴", modelName = "社保模块")
    public ResultResponse stopPayment(TJShebaoDto dto, String stopDataParam) {
        ResultResponse resultResponse=new ResultResponse();
        LOGGER.info("社保公积金停缴,传递参数:"+ JSON.toJSONString(dto));
        try {
            CompanyShebaoOrderBean companyShebaoOrderBean=customerShebaoOrderService.stopPayment(dto, stopDataParam);
            //更新企业订单信息(由于事务的关系,在这儿更新企业订单信息)
            Date compareDate=new Date();
            if(companyShebaoOrderBean.getRequireLastTime()!=null && companyShebaoOrderBean.getOrderLastTime()!=null
                    && compareDate.after(companyShebaoOrderBean.getRequireLastTime()) && !compareDate.after(companyShebaoOrderBean.getOrderLastTime())){
                if(companyShebaoOrderBean.getStatus()==null
                ||(companyShebaoOrderBean.getStatus()!=null && (companyShebaoOrderBean.getStatus().intValue()== ShebaoConstants.COMPANY_ORDER_INIT || companyShebaoOrderBean.getStatus().intValue()== ShebaoConstants.COMPANY_ORDER_WILLSUBMIT ))){
                    boolean deleteResult = companyShebaoService.deleteCompanyOrderIfEmpty(companyShebaoOrderBean.getId());
                    if(!deleteResult) {
                        companyShebaoService.updateOrderDetail(companyShebaoOrderBean.getId());
                    }
                }
            }
            resultResponse.setSuccess(true);
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("社保公积金停缴,"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("社保公积金停缴失败");
            LOGGER.error("社保公积金停缴",e);
        }
        LOGGER.info("社保公积金停缴,返回结果:"+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 社保公积金调基验证
     * @param dto
     * @return
     */
    @RequestMapping("/adjustBaseValidator.htm")
    @ResponseBody
    public ResultResponse adjustBaseValidator(TJShebaoDto dto) {
        ResultResponse resultResponse=new ResultResponse();
        LOGGER.info("社保公积金调基验证,传递参数:"+ JSON.toJSONString(dto));
        try {
            TJShebaoDto tjDto=customerShebaoOrderService.adjustBaseValidator(dto);
            resultResponse.setSuccess(true);
            resultResponse.setData(tjDto);
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("社保公积金调基验证,"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("社保公积金调基验证失败");
            LOGGER.error("社保公积金调基验证",e);
        }
        LOGGER.info("社保公积金调基验证,返回结果:"+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 社保公积金调基
     * @param dto
     * @return
     */
    @RequestMapping("/adjustBase.htm")
    @ResponseBody
    @SystemControllerLog(operation = "社保公积金调基", modelName = "社保模块")
    public ResultResponse adjustBase(TJShebaoDto dto,BigDecimal adjustAmount) {
        ResultResponse resultResponse=new ResultResponse();
        LOGGER.info("社保公积金调基,传递参数:"+ JSON.toJSONString(dto));
        try {
            CompanyShebaoOrderBean companyShebaoOrderBean=customerShebaoOrderService.adjustBase(dto, adjustAmount);
            //更新企业订单信息(由于事务的关系,在这儿更新企业订单信息)
            Date compareDate=new Date();
            if(companyShebaoOrderBean.getRequireLastTime()!=null && companyShebaoOrderBean.getOrderLastTime()!=null
                    && compareDate.after(companyShebaoOrderBean.getRequireLastTime()) && !compareDate.after(companyShebaoOrderBean.getOrderLastTime())){
                companyShebaoService.updateOrderDetail(companyShebaoOrderBean.getId());
            }
            resultResponse.setSuccess(true);
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("社保公积金调基,"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("社保公积金调基失败");
            LOGGER.error("社保公积金调基",e);
        }
        LOGGER.info("社保公积金调基,返回结果:"+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 批量缴纳社保公积金
     * @param dto
     * @param companyId
     * @return
     */
    @RequestMapping("/batchPayment.htm")
    @ResponseBody
    public ResultResponse batchPayment(TJShebaoDto dto,Long companyId) {
        ResultResponse resultResponse=new ResultResponse();
        LOGGER.info("批量缴纳社保公积金,传递参数:"+ JSON.toJSONString(dto)+",企业ID:"+companyId);
        try {
            customerShebaoOrderService.batchPayment(dto, companyId);
            resultResponse.setSuccess(true);
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("批量缴纳社保公积金,"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("批量缴纳社保公积金失败");
            LOGGER.error("批量缴纳社保公积金",e);
        }
        LOGGER.info("批量缴纳社保公积金,返回结果:"+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }


    /**
     * 获取公积金可补缴月份
     * @param obmonth
     * @param oemonth
     * @param customerId
     * @return
     */
    @RequestMapping("/getGjjBjMonth.htm")
    @ResponseBody
    public ResultResponse getGjjBjMonth(Date obmonth, Date oemonth, long customerId){

        return ResultResponse.buildSuccess(customerShebaoOrderService.getGjjBjMonth(obmonth, oemonth, customerId));

    }

    /**
     * 社保公积金取消停缴
     * @param dto
     * @return
     */
    @RequestMapping("/cancelStopPayment.htm")
    @ResponseBody
    @SystemControllerLog(operation = "社保公积金取消停缴", modelName = "社保模块")
    public ResultResponse cancelStopPayment(TJShebaoDto dto) {
        ResultResponse resultResponse=new ResultResponse();
        LOGGER.info("社保公积金取消停缴,传递参数:"+ JSON.toJSONString(dto));
        try {
            CompanyShebaoOrderBean companyShebaoOrderBean=customerShebaoOrderService.cancelStopPayment(dto);
            //更新企业订单信息(由于事务的关系,在这儿更新企业订单信息)
            Date compareDate=new Date();
            if(companyShebaoOrderBean.getRequireLastTime()!=null && companyShebaoOrderBean.getOrderLastTime()!=null
                    && compareDate.after(companyShebaoOrderBean.getRequireLastTime()) && !compareDate.after(companyShebaoOrderBean.getOrderLastTime())){
                        companyShebaoService.updateOrderDetail(companyShebaoOrderBean.getId());
            }
            resultResponse.setSuccess(true);
        }catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("社保公积金取消停缴,"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("社保公积金取消停缴失败");
            LOGGER.error("社保公积金取消停缴",e);
        }
        LOGGER.info("社保公积金取消停缴,返回结果:"+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }


    /**
     * 获取当前月社保补交信息
     * @param
     * @return
     */
    @RequestMapping("/getSbOverDetail.htm")
    @ResponseBody
    public ResultResponse getSbOverDetail(Long companyShebaoOrderId,Long customerId ) {
        ResultResponse resultResponse=new ResultResponse();
        LOGGER.info("获取当前月社保补交信息:companyShebaoOrderId="+ companyShebaoOrderId + ",customerId=" + customerId);
        try {
            List<Map<String,Object>> overdueDetails = customerShebaoOrderService.getOverdueDetail(companyShebaoOrderId,customerId,1);
            if (overdueDetails == null || overdueDetails.size() <= 0){
                overdueDetails = null;

                resultResponse.setSuccess(false);
                resultResponse.setData(overdueDetails);
                return resultResponse;
            }
            //resultResponse.buildSuccess(overdueDetails);
            resultResponse.setSuccess(true);
            resultResponse.setData(overdueDetails);
        }catch(Exception e){
            resultResponse.setSuccess(false);
            resultResponse.setMessage("获取当前月社保补交信息失败");
            LOGGER.error("获取当前月社保补交信息失败",e);
        }
        LOGGER.info("获取当前月社保补交信息,返回结果:"+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 获取当前月公积金补交信息
     * @param
     * @return
     */
    @RequestMapping("/getGjjOverDetail.htm")
    @ResponseBody
    public ResultResponse getGjjOverDetail(Long companyShebaoOrderId,Long customerId ) {
        ResultResponse resultResponse=new ResultResponse();
        LOGGER.info("获取当前月社保补交信息:companyShebaoOrderId="+ companyShebaoOrderId + ",customerId=" + customerId);
        try {
            List<Map<String,Object>> overdueDetails = customerShebaoOrderService.getOverdueDetail(companyShebaoOrderId,customerId,2);
            if (overdueDetails == null || overdueDetails.size() <= 0){
                overdueDetails = null;

                resultResponse.setSuccess(false);
                resultResponse.setData(overdueDetails);
                return resultResponse;
            }
            //resultResponse.buildSuccess(overdueDetails);
            resultResponse.setSuccess(true);
            resultResponse.setData(overdueDetails);
        }catch(Exception e){
            resultResponse.setSuccess(false);
            resultResponse.setMessage("获取当前月公积金补交信息失败");
            LOGGER.error("获取当前月公积金补交信息失败",e);
        }
        LOGGER.info("获取当前月公积金补交信息,返回结果:"+ JSON.toJSONString(resultResponse));
        return resultResponse;
    }
}
