package com.xtr.company.controller.social;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.RedEnvelopeBean;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanyRechargesBean;
import com.xtr.api.domain.company.CompanySocialOrdersBean;
import com.xtr.api.service.account.RedEnvelopeService;
import com.xtr.api.service.company.CompanyRechargesService;
import com.xtr.api.service.company.CompanySocialOrdersService;
import com.xtr.comm.constant.CompanyRechargeConstant;
import com.xtr.comm.constant.RedActivityConstant;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.MathUtils;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.comm.util.RandomNumber;
import com.xtr.company.util.AliyunOssHelper;
import com.xtr.company.util.SessionUtils;
import com.xtr.company.web.IExcelView;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

/**
 * Created by ali_j2 on 2016.07.08 008.
 */
@Controller
public class CompanySocialOrdersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanySocialOrdersController.class);

    @Resource
    private CompanySocialOrdersService companySocialOrdersService;
    @Resource
    private RedEnvelopeService redEnvelopeService;
    @Resource
    private CompanyRechargesService companyRechargesService;

    @Resource
    private IExcelView excelView;


    /**
     * 上传excel
     * @param multipartFile
     * @param orderType
     * @param orderId
     * @return
     */
    @RequestMapping("social/uploadExcel.htm")
    @ResponseBody
    public ResultResponse uploadExcel(@RequestParam("file") MultipartFile multipartFile, @RequestParam(value="orderType",defaultValue="0") int orderType, @RequestParam(value="orderId",defaultValue = "0") long orderId,HttpServletRequest request){

        ResultResponse resultResponse = new ResultResponse();

        try {
            if (null == multipartFile) {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("请上传文件");
                return resultResponse;
            }

            if (orderType < 1) {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("参数错误(type)");
                return resultResponse;
            }


            CompanyMembersBean member = SessionUtils.getUser(request);

            String fileName = multipartFile.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            suffix = suffix.toLowerCase();

            if (!suffix.equals("xls") && !suffix.equals("xlsx")) {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("上传文件类型错误");
                return resultResponse;
            }

            Date now = new Date();
            Random r = new Random(System.currentTimeMillis());

            String front = "SU";
            String upfilefont = "excel_cussocial_";
            if (orderType == 1) {
                front = "SU";
                upfilefont = "excel_cussocial_";
            } else if (orderType == 2) {
                front = "SP";
                upfilefont = "excel_cussocialpay_";
            } else if (orderType == 3) {
                front = "SS";
                upfilefont = "excel_cussocialpaysup_";
            } else if (orderType == 4) {
                front = "ST";
                upfilefont = "excel_cussocialstop_";
            } else if (orderType == 5) {
                front = "SE";
                upfilefont = "excel_cussocialedit_";
            }

            //文件名
            String key = upfilefont + DateUtil.date2String(now, "yyyyMMddHHmmss") + "_" + RandomNumber.getRandomNumberByLength(r, 4) + "." + suffix;

            //上传文件到阿里云附件服务器
            boolean upflag = AliyunOssHelper.putObject(multipartFile, key, AliyunOssHelper.bucketNameFile);
            if (!upflag) {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("文件上传失败");
                return resultResponse;
            }

            //文件访问全路径
            String filepath = "http://" + AliyunOssHelper.bucketNameFile + ".oss-cn-hangzhou.aliyuncs.com/" + key;

            String ordernumber = front + DateUtil.date2String(now, "yyyyMMddHHmmss") + RandomNumber.getRandomNumberByLength(r, 4);

            CompanySocialOrdersBean socialOrdersBean = new CompanySocialOrdersBean();
            socialOrdersBean.setOrderCompanyId(member.getMemberCompanyId());
            socialOrdersBean.setOrderDepId(member.getMemberDepId());
            socialOrdersBean.setOrderCompanyMemberId(member.getMemberId());
            /*
            socialOrdersBean.setOrderCompanyId(3l);
            socialOrdersBean.setOrderDepId(3l);
            socialOrdersBean.setOrderCompanyMemberId(3l);
            */
            socialOrdersBean.setOrderUpExcel(filepath);


            resultResponse.setData(orderId);
            if (orderId < 1) {
                socialOrdersBean.setOrderAddtime(now);
                socialOrdersBean.setOrderNumber(ordernumber);
                socialOrdersBean.setOrderType(orderType);
                socialOrdersBean.setOrderStatus(1);
                long oid=companySocialOrdersService.insert(socialOrdersBean);

                resultResponse.setData(oid);
            } else {
                socialOrdersBean.setOrderId(orderId);
                companySocialOrdersService.update(socialOrdersBean);
            }

        }catch (Exception e){
            LOGGER.error("文件上传异常", e);
            resultResponse.setMessage(e.getCause().getMessage());
            resultResponse.setSuccess(false);
            return resultResponse;
        }

        resultResponse.setSuccess(true);
        resultResponse.setMessage("上传文件成功");

        return resultResponse;
    }

    /**
     * 上传excel文件
     * @param multipartFile
     * @param orderType
     * @param request
     * @return
     */
    @RequestMapping("social/uploadExcelFile.htm")
    @ResponseBody
    public ResultResponse uploadExcelFile(@RequestParam("file") MultipartFile multipartFile, @RequestParam(value="orderType",defaultValue="0") int orderType,HttpServletRequest request){

        ResultResponse resultResponse = new ResultResponse();

        try {
            if (null == multipartFile) {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("请上传文件");
                return resultResponse;
            }

            if (orderType < 1) {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("参数错误(type)");
                return resultResponse;
            }



            String fileName = multipartFile.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            suffix = suffix.toLowerCase();

            if (!suffix.equals("xls") && !suffix.equals("xlsx")) {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("上传文件类型错误");
                return resultResponse;
            }


            Date now = new Date();
            Random r = new Random(System.currentTimeMillis());


            String upfilefont = "excel_cussocial_";
            if (orderType == 1) {

                upfilefont = "excel_cussocial_";
            } else if (orderType == 2) {

                upfilefont = "excel_cussocialpay_";
            } else if (orderType == 3) {

                upfilefont = "excel_cussocialpaysup_";
            } else if (orderType == 4) {

                upfilefont = "excel_cussocialstop_";
            } else if (orderType == 5) {

                upfilefont = "excel_cussocialedit_";
            }

            //文件名
            String key = upfilefont + DateUtil.date2String(now, "yyyyMMddHHmmss") + "_" + RandomNumber.getRandomNumberByLength(r, 4) + "." + suffix;

            //上传文件到阿里云附件服务器
            boolean upflag = AliyunOssHelper.putObject(multipartFile, key, AliyunOssHelper.bucketNameFile);
            if (!upflag) {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("文件上传失败");
                return resultResponse;
            }

            //文件访问全路径
            String filepath = "http://" + AliyunOssHelper.bucketNameFile + ".oss-cn-hangzhou.aliyuncs.com/" + key;

            resultResponse.setData(filepath);


        }catch (Exception e){
            LOGGER.error("文件上传异常", e);
            resultResponse.setMessage(e.getCause().getMessage());
            resultResponse.setSuccess(false);
            return resultResponse;
        }

        resultResponse.setSuccess(true);
        resultResponse.setMessage("上传文件成功");

        return resultResponse;
    }

    /**
     * 创建，社保订单
     * @param filepath
     * @param orderType
     * @param request
     * @return
     */
    @RequestMapping("social/creatSocialOrder.htm")
    @ResponseBody
    public ResultResponse creatSocialOrder(@RequestParam(value="filepath",defaultValue = "") String filepath, @RequestParam(value="orderType",defaultValue="0") int orderType,@RequestParam(value="orderBeginDate",defaultValue = "") String orderBeginDate,@RequestParam(value="orderEndDate",defaultValue = "") String orderEndDate,@RequestParam(value="orderState",defaultValue="") String orderState, HttpServletRequest request){

        ResultResponse resultResponse = new ResultResponse();

        try {
            if (filepath.length()<1) {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("请上传文件");
                return resultResponse;
            }

            if (orderType < 1) {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("参数错误(type)");
                return resultResponse;
            }

            CompanyMembersBean member = SessionUtils.getUser(request);


            Date now = new Date();
            Random r = new Random(System.currentTimeMillis());

            String front = "SU";

            if (orderType == 1) {
                front = "SU";
            } else if (orderType == 2) {
                front = "SP";
            } else if (orderType == 3) {
                front = "SS";
            } else if (orderType == 4) {
                front = "ST";
            } else if (orderType == 5) {
                front = "SE";
            }



            String ordernumber = front + DateUtil.date2String(now, "yyyyMMddHHmmss") + RandomNumber.getRandomNumberByLength(r, 4);

            CompanySocialOrdersBean socialOrdersBean = new CompanySocialOrdersBean();
            socialOrdersBean.setOrderCompanyId(member.getMemberCompanyId());
            socialOrdersBean.setOrderDepId(member.getMemberDepId());
            socialOrdersBean.setOrderCompanyMemberId(member.getMemberId());
            /*
            socialOrdersBean.setOrderCompanyId(3l);
            socialOrdersBean.setOrderDepId(3l);
            socialOrdersBean.setOrderCompanyMemberId(3l);
            */
            socialOrdersBean.setOrderUpExcel(filepath);
            socialOrdersBean.setOrderAddtime(now);
            socialOrdersBean.setOrderNumber(ordernumber);
            socialOrdersBean.setOrderType(orderType);
            socialOrdersBean.setOrderStatus(1);

            if(orderBeginDate.length()>0) {
                socialOrdersBean.setOrderBeginDate(DateUtil.string2Date(orderBeginDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }else{
                if(orderType==2) {
                    String dstr=DateUtil.date2String(now,"yyyy-MM");
                    socialOrdersBean.setOrderBeginDate(DateUtil.string2Date(dstr + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
                }
            }

            if(orderEndDate.length()>0) {
                socialOrdersBean.setOrderEndDate(DateUtil.string2Date(orderBeginDate + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
            }else{
                if(orderType==2) {
                    String dstr=DateUtil.date2String(now,"yyyy-MM");
                    socialOrdersBean.setOrderEndDate(DateUtil.string2Date(dstr + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
                }
            }

            if(orderState.length()>0) {
                socialOrdersBean.setOrderState(orderState);
            }else{
                if(orderType==2)
                    socialOrdersBean.setOrderState("1,2");
            }

            companySocialOrdersService.insert(socialOrdersBean);


        }catch (Exception e){
            LOGGER.error("提交异常", e);
            resultResponse.setMessage(e.getCause().getMessage());
            resultResponse.setSuccess(false);
            return resultResponse;
        }

        resultResponse.setSuccess(true);
        resultResponse.setMessage("提交成功");

        return resultResponse;
    }

    /**
     * 上传图片压缩包
     * @param multipartFile
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping("social/uploadImg.htm")
    @ResponseBody
    public ResultResponse uploadImg(@RequestParam("file") MultipartFile multipartFile, @RequestParam(value="orderId",defaultValue = "0") long orderId,HttpServletRequest request){

        ResultResponse resultResponse = new ResultResponse();

        try {
            if (null == multipartFile) {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("请上传文件");
                return resultResponse;
            }

            CompanyMembersBean member = SessionUtils.getUser(request);

            String fileName = multipartFile.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            suffix = suffix.toLowerCase();

            if (!suffix.equals("zip") && !suffix.equals("rar") && !suffix.equals("7z")) {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("上传文件类型错误");
                return resultResponse;
            }


            Date now = new Date();
            Random r = new Random(System.currentTimeMillis());

            String front = "SU";
            String upfilefont = "img_cussocial_";


            //文件名
            String key = upfilefont + DateUtil.date2String(now, "yyyyMMddHHmmss") + "_" + RandomNumber.getRandomNumberByLength(r, 4) + "." + suffix;

            //上传文件到阿里云附件服务器
            boolean upflag = AliyunOssHelper.putObject(multipartFile, key, AliyunOssHelper.bucketNameImg);
            if (!upflag) {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("文件上传失败");
                return resultResponse;
            }

            //文件访问全路径
            String filepath = "http://" + AliyunOssHelper.bucketNameImg + ".oss-cn-hangzhou.aliyuncs.com/" + key;

            String ordernumber = front + DateUtil.date2String(now, "yyyyMMddHHmmss") + RandomNumber.getRandomNumberByLength(r, 4);

            CompanySocialOrdersBean socialOrdersBean = new CompanySocialOrdersBean();
            socialOrdersBean.setOrderCompanyId(member.getMemberCompanyId());
            socialOrdersBean.setOrderDepId(member.getMemberDepId());
            socialOrdersBean.setOrderCompanyMemberId(member.getMemberId());
            /*
            socialOrdersBean.setOrderCompanyId(3l);
            socialOrdersBean.setOrderDepId(3l);
            socialOrdersBean.setOrderCompanyMemberId(3l);
            */
            socialOrdersBean.setOrderUpImg(filepath);

            if (orderId < 1) {
                socialOrdersBean.setOrderAddtime(now);
                socialOrdersBean.setOrderNumber(ordernumber);
                socialOrdersBean.setOrderType(1);
                socialOrdersBean.setOrderStatus(1);
                long oid=companySocialOrdersService.insert(socialOrdersBean);

                resultResponse.setData(oid);
            } else {
                socialOrdersBean.setOrderId(orderId);
                companySocialOrdersService.update(socialOrdersBean);
            }

        }catch (Exception e){
            LOGGER.error("文件上传异常", e);
            resultResponse.setMessage(e.getCause().getMessage());
            resultResponse.setSuccess(false);
            return resultResponse;
        }

        resultResponse.setSuccess(true);
        resultResponse.setMessage("上传文件成功");

        return resultResponse;
    }

    /**
     * 撤销撤销订单
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping("social/orderCancel.htm")
    @ResponseBody
    public ResultResponse orderCancel(@RequestParam(value="orderId",defaultValue = "0") long orderId,HttpServletRequest request){

        ResultResponse resultResponse = new ResultResponse();

        try {
            if (orderId < 1) {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("撤销参数错误");
                return resultResponse;
            }


            CompanySocialOrdersBean socialOrdersBean = new CompanySocialOrdersBean();
            socialOrdersBean.setOrderStatus(4);
            socialOrdersBean.setOrderId(orderId);
            companySocialOrdersService.update(socialOrdersBean);

        }catch (Exception e){
            LOGGER.error("撤销异常", e);
            resultResponse.setMessage(e.getCause().getMessage());
            resultResponse.setSuccess(false);
            return resultResponse;
        }

        resultResponse.setSuccess(true);
        resultResponse.setMessage("撤销成功");

        return resultResponse;
    }

    /**
     * 订单银行支付
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping("social/orderBankPay.htm")
    @ResponseBody
    public ResultResponse orderBankPay(@RequestParam(value="orderId",defaultValue = "0") long orderId, @RequestParam(value="rechargeBank",defaultValue = "") String rechargeBank
            , @RequestParam(value="rechargeBankNumber",defaultValue = "") String rechargeBankNumber, @RequestParam(value="rechargeSerialNumber",defaultValue = "") String rechargeSerialNumber
            , @RequestParam(value="rechargeMoney",defaultValue = "0") BigDecimal rechargeMoney,@RequestParam(value="rechargeBak",defaultValue = "") String rechargeBak
            ,@RequestParam(value="orderPaytype",defaultValue = "0") Integer orderPaytype, HttpServletRequest request){

        ResultResponse resultResponse = new ResultResponse();

        try{

            if(orderId<1){
                resultResponse.setSuccess(false);
                resultResponse.setMessage("撤销订单错误");
                return resultResponse;
            }

            if(orderPaytype<1){
                resultResponse.setSuccess(false);
                resultResponse.setMessage("参数错误(orderPaytype)");
                return resultResponse;
            }

            if(rechargeBank.length()<1){
                resultResponse.setSuccess(false);
                resultResponse.setMessage("请填写有效银行名称");
                return resultResponse;
            }

            if(rechargeBankNumber.length()<1){
                resultResponse.setSuccess(false);
                resultResponse.setMessage("请填写有效银行卡号");
                return resultResponse;
            }

            if(rechargeSerialNumber.length()<1){
                resultResponse.setSuccess(false);
                resultResponse.setMessage("请填写有效银行流水号");
                return resultResponse;
            }

            if(rechargeMoney.doubleValue()-0<=0){
                resultResponse.setSuccess(false);
                resultResponse.setMessage("请填写有效支付金额");
                return resultResponse;
            }

            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);

            if (null == companyMembersBean) {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("请先登录");
                return resultResponse;
            }

            Date now=new Date();
            Random r = new Random(System.currentTimeMillis());

            String rechargenumber = "RS" + DateUtil.date2String(now, "yyyyMMddHHmmss") + RandomNumber.getRandomNumberByLength(r, 4);

            CompanyRechargesBean rechargesBean=new CompanyRechargesBean();
            rechargesBean.setRechargeType(8);
            rechargesBean.setRechargeMoney(rechargeMoney);
            rechargesBean.setRechargeCompanyId(companyMembersBean.getMemberCompanyId());
            rechargesBean.setRechargeAddtime(now);
            rechargesBean.setRechargeNumber(rechargenumber);
            rechargesBean.setRechargeBak(rechargeBak);
            rechargesBean.setRechargeStation(0);
            rechargesBean.setRechargeClient(1);
            rechargesBean.setRechargeMoneyserver(new BigDecimal(0));
            rechargesBean.setRechargeBank(rechargeBank);
            rechargesBean.setRechargeBankarea("");
            rechargesBean.setRechargeBanknumber(rechargeBankNumber);
            rechargesBean.setRechargeMoneynow(rechargeMoney);
            rechargesBean.setRechargeIstoexcel(0);
            rechargesBean.setRechargeMoneybt(new BigDecimal(0));
            rechargesBean.setRechargeIstorecord(0);
            rechargesBean.setRechargeState(0);

            int ret=companyRechargesService.companySocialOrderBankPay(rechargesBean,orderId,orderPaytype);

            if(ret!=1){
                resultResponse.setSuccess(false);
                resultResponse.setMessage("提交付款信息失败");
                return resultResponse;
            }



        }catch (Exception e){
            LOGGER.error("付款异常", e);
            resultResponse.setMessage(e.getCause().getMessage());
            resultResponse.setSuccess(false);
            return resultResponse;
        }

        resultResponse.setSuccess(true);
        resultResponse.setMessage("提交付款信息成功");
        return resultResponse;
    }

    /**
     * 社保记录分页列表
     * @param mav
     * @param type
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping("socialOrders.htm")
    public ModelAndView socialOrders(ModelAndView mav,
                                          @RequestParam(value = "type", defaultValue = "0") Integer type,
                                          @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                                          @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                     HttpServletRequest request){


        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);

        CompanySocialOrdersBean socialOrdersBean=new CompanySocialOrdersBean();
        socialOrdersBean.setPageIndex(pageIndex);
        socialOrdersBean.setPageSize(pageSize);
        socialOrdersBean.setOrderCompanyId(companyMembersBean.getMemberCompanyId());
        //socialOrdersBean.setOrderCompanyId(3l);

        if(type>0){
            mav.addObject("type",type);
            socialOrdersBean.setOrderType(type);
        }

        ResultResponse resultResponse =companySocialOrdersService.selectPageList(socialOrdersBean);

        if (resultResponse.isSuccess()) {
            mav.addObject("orderDate", resultResponse.getData());
            mav.addObject("paginator", resultResponse.getPaginator());
        }

        mav.setViewName("xtr/social/socialOrders");
        return mav;
    }

    /**
     * 员工资料导入
     * @param mav
     * @param request
     * @return
     */
    @RequestMapping("socil/socialInfoUp.htm")
    public ModelAndView socialInfoUp(ModelAndView mav,
                                     HttpServletRequest request){

        mav.setViewName("xtr/social/socialInfoUp");
        return mav;
    }

    /**
     * 缴纳社保
     * @param mav
     * @param request
     * @return
     */
    @RequestMapping("socil/socialPayUp.htm")
    public ModelAndView socialPayUp(ModelAndView mav,
                                     HttpServletRequest request){

        mav.setViewName("xtr/social/socialPayUp");
        return mav;
    }

    /**
     * 补缴社保
     * @param mav
     * @param request
     * @return
     */
    @RequestMapping("socil/socialPayUpSup.htm")
    public ModelAndView socialPayUpSup(ModelAndView mav,
                                    HttpServletRequest request){

        mav.setViewName("xtr/social/socialPayUpSup");
        return mav;
    }

    /**
     * 服务费详情
     *
     * @param mav
     * @param orderId
     * @return
     */
    @RequestMapping("socil/socialInfoDetail.htm")
    public ModelAndView socialInfoDetail(ModelAndView mav,
                                       @RequestParam("orderId") Long orderId) {

        CompanySocialOrdersBean companySocialOrdersBean = companySocialOrdersService.find(orderId);
        mav.addObject("socialData", companySocialOrdersBean);

        // 上海人数
        Integer shOrderCount = (null == companySocialOrdersBean.getShOrderCount()) ? 0 : companySocialOrdersBean.getShOrderCount();
        Integer otherCount = companySocialOrdersBean.getOrderPeopleNumber() - shOrderCount;
        mav.addObject("shOrderCount", shOrderCount);
        mav.addObject("otherCount", otherCount);

        Double shServiceMoney = shOrderCount * 29.9;
        Double otherServiceMoney = otherCount * 59.9;

        mav.addObject("shServiceMoney", MathUtils.toMoney(shServiceMoney));
        mav.addObject("otherServiceMoney", MathUtils.toMoney(otherServiceMoney));
        mav.addObject("serviceMoney", MathUtils.toMoney(shServiceMoney+otherServiceMoney));

        mav.setViewName("xtr/social/socialInfoDetail");
        return mav;
    }

    /**
     * 社保停缴
     * @param mav
     * @param request
     * @return
     */
    @RequestMapping("socil/socialInfoStop.htm")
    public ModelAndView socialInfoStop(ModelAndView mav,
                                     HttpServletRequest request){

        mav.setViewName("xtr/social/socialInfoStop");
        return mav;
    }

    /**
     * 调整基数
     * @param mav
     * @param request
     * @return
     */
    @RequestMapping("socil/socialInfoEdit.htm")
    public ModelAndView socialInfoEdit(ModelAndView mav,
                                       HttpServletRequest request){

        mav.setViewName("xtr/social/socialInfoEdit");
        return mav;
    }

    /**
     * 付款页面
     * @param mav
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping("socil/orderPay.htm")
    public ModelAndView orderPay(ModelAndView mav,
                                 @RequestParam(value = "orderId", defaultValue = "0") Long orderId
                                 ,HttpServletRequest request){

        CompanyMembersBean member = SessionUtils.getUser(request);

        CompanySocialOrdersBean socialOrdersBean=companySocialOrdersService.find(orderId);
        if(socialOrdersBean!=null){
            Double orderMoney=socialOrdersBean.getOrderMoney();
            Double orderMoneyServer=socialOrdersBean.getOrderMoneyServer();

            // 订单金额 = 订单金额+薪太软服务费
            Double orderMoneyTotal = orderMoney + orderMoneyServer;

            mav.addObject("orderMoneyTotal", MathUtils.toMoney(orderMoneyTotal));
            mav.addObject("realMoney", MathUtils.toMoney(orderMoneyTotal));

            // 查询有没有代缴社保的红包
            RedEnvelopeBean redEnvelopeBean = redEnvelopeService.getByCompanyId(member.getMemberCompanyId(), CompanyRechargeConstant.SOCIAL_TYPE);
            if(null != redEnvelopeBean){

                boolean used = false;

                Integer count = socialOrdersBean.getOrderPeopleNumber();

                if(redEnvelopeBean.getActivityId().equals(RedActivityConstant.AC_22) && count >= 5){
                    used = true;
                }

                if(redEnvelopeBean.getActivityId().equals(RedActivityConstant.AC_66) && count >= 15){
                    used = true;
                }

                if(used){

                    mav.addObject("redMoney", MathUtils.toMoney(redEnvelopeBean.getRedMoney()));

                    Double realMoney = MathUtils.toMoney(orderMoneyTotal - redEnvelopeBean.getRedMoney().doubleValue());

                    mav.addObject("realMoney", realMoney);
                }

            }

        } else{
            mav.addObject("orderMoneyTotal","0.00");
        }

        mav.addObject("orderId",orderId);
        mav.setViewName("xtr/social/orderPay");
        return mav;
    }

    /**
     * 根据社保订单号下载excel
     *
     * @param request
     * @param response
     * @param type      1:失败的excel 2:上传的excel
     * @param orderId
     */
    @RequestMapping(value = "socil/download.htm")
    public void download(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("type") Integer type,
            @RequestParam("orderId") Long orderId) {

        CompanySocialOrdersBean companySocialOrdersBean = companySocialOrdersService.find(orderId);
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);

        if(null != companySocialOrdersBean){

            // 判断当前登录用户是否是该企业
            if(!companySocialOrdersBean.getOrderCompanyId().equals(companyMembersBean.getMemberCompanyId())){
                return;
            }

            try {
                String fileName = (type==1) ? FilenameUtils.getName(companySocialOrdersBean.getOrderErrExcel()) :
                        FilenameUtils.getName(companySocialOrdersBean.getOrderUpExcel());

                if(fileName.startsWith("http") && fileName.lastIndexOf(".com/") != -1){
                    fileName = fileName.substring(fileName.lastIndexOf(".com/")+5);
                }

                excelView.download(PropertyUtils.getString("oss.bucketName.file"), fileName, fileName, response);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        } else{
            LOGGER.info("###### 企业社保订单不存在，ID为【"+ orderId +"】 ######");
        }
    }

    /**
     * 订单付款成功页面
     * @param mav
     * @return
     */
    @RequestMapping(value = "socialPaySuccess.htm", method = RequestMethod.GET)
    public ModelAndView socialPaySuccess(ModelAndView mav,
                                         @RequestParam("") Long a) {
        mav.addObject("", "");
        mav.setViewName("xtr/social/socialPaySuccess");
        return mav;
    }
}
