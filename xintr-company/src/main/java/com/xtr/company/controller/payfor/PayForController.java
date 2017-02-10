package com.xtr.company.controller.payfor;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.google.gson.Gson;
import com.xtr.api.basic.BaseController;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.BankCodeBean;
import com.xtr.api.domain.account.SubAccountBean;
import com.xtr.api.domain.company.*;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.domain.station.StationCollaborationBean;
import com.xtr.api.domain.station.StationSmsRecordsBean;
import com.xtr.api.domain.station.StationSmscontBean;
import com.xtr.api.dto.company.CompanyProtocolsDto;
import com.xtr.api.dto.gateway.base.BusinessType;
import com.xtr.api.dto.gateway.request.DefrayPayRequest;
import com.xtr.api.service.account.BankCodeService;
import com.xtr.api.service.company.*;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.gateway.JdPayService;
import com.xtr.api.service.order.IdGeneratorService;
import com.xtr.api.service.send.SendMsgService;
import javax.servlet.http.HttpServletRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.xtr.api.service.station.StationCollaborationService;
import com.xtr.api.util.ExcelUtil;
import com.xtr.comm.annotation.SystemControllerLog;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CommonConstants;
import com.xtr.comm.constant.Constant;
import com.xtr.comm.constant.StationCollaborationConstants;
import com.xtr.comm.enums.*;
import com.xtr.comm.fastdfs.FastDFS;
import com.xtr.comm.fastdfs.FastDFSResult;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.util.*;

import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

import com.xtr.company.util.SessionUtils;
import com.xtr.company.web.IExcelView;
import java.text.DecimalFormat;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
//import com.xtr.core.service.order.IdGeneratorService;
import java.io.UnsupportedEncodingException;
import java.io.*;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * Created by 李志超 on 2016/7/12.
 */
@Controller
public class PayForController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayForController.class);


    @Resource
    private IExcelView excelView;

    @Resource
    private CompanySalaryExcelService companySalaryExcelService;

    @Resource
    private CompanyDepsService companyDepsService;

    @Resource
    private CustomersService customersService;

    @Resource
    private CompanysService companysService;

    @Resource
    private SendMsgService sendMsgService;

    @Resource
    private CompanyMembersService companyMembersService;

    @Resource
    private CompanyBorrowOrdersService companyBorrowOrdersService;

    @Resource
    private IdGeneratorService idGeneratorService;

    @Resource
    private CompanyProtocolsService companyProtocolsService;

    @Resource
    private CompanyLogService companyLogService;

    @Resource
    private JdPayService jdPayService;

    @Resource
    private BankCodeService bankCodeService;

    @Resource
    private StationCollaborationService stationCollaborationService;

    /**
     * 垫付工资页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("payfor.htm")
    public ModelAndView payroll(ModelAndView mav, HttpServletRequest request, @RequestParam("paytype") String paytype) {
        //LOGGER.info("１```");
        CompanysBean obj = getLoginCompanyObj(request);
        obj=companysService.selectCompanyByCompanyId(obj.getCompanyId());
        CompanyProtocolsBean companyProtocolsBean=new CompanyProtocolsBean();
        companyProtocolsBean.setProtocolCompanyId(obj.getCompanyId());
        companyProtocolsBean.setProtocolContractType(2);
        List list=companyProtocolsService.selectByContractType(companyProtocolsBean);
        mav.addObject("companyBean",obj);
        mav.addObject("isShow", true);
        if(list.size()>0){
            mav.addObject("sum","1");
            mav.setViewName("xtr/payday/paysecurity");
        }else{
            mav.addObject("paytype",paytype);
            mav.addObject("companyId",obj.getCompanyId());
            //LOGGER.info("２```");
            if(obj.getCompanyIsauth()==null || obj.getCompanyIsauth()!=1){
                //LOGGER.info("３```");
                if(obj.getCompanyAuditStatus()==null || obj.getCompanyAuditStatus()==1 || obj.getCompanyAuditStatus()==0){
                    mav.addObject("companyName",obj.getCompanyName());
                    if(obj.getCompanyLogo()==null){
                        mav.addObject("companyLogo","");
                    }else{
                        mav.addObject("companyLogo",obj.getCompanyLogo());
                    }
                    mav.addObject("companyLogoUrl","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+obj.getCompanyLogo());
                    mav.addObject("companyAddress",obj.getCompanyAddress());
                    mav.addObject("companyNumber",obj.getCompanyNumber());
                    if(obj.getCompanyOrganizationImg()==null){
                        mav.addObject("companyOrganizationImg","");
                    }else{
                        mav.addObject("companyOrganizationImg",obj.getCompanyOrganizationImg());
                    }
                    mav.addObject("companyOrganizationImgUrl","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+obj.getCompanyOrganizationImg());
                    mav.addObject("companyCorporation",obj.getCompanyCorporation());
                    mav.addObject("companyContactTel",obj.getCompanyContactTel());
                    mav.addObject("companyScale",obj.getCompanyScale());
                    mav.addObject("companyBelongIndustry",obj.getCompanyBelongIndustry());
                    mav.addObject("companyChannel",obj.getCompanyChannel());
                    mav.addObject("auditStatus",obj.getCompanyAuditStatus());
                    mav.addObject("companyAuditRemark",obj.getCompanyAuditRemark());
                    mav.setViewName("xtr/payday/payfor");
                }else{
                    if(obj.getCompanyCertificationAmount()==null || "0".equals(String.valueOf(obj.getCompanyIsauth()))){
                        mav.addObject("companyName",obj.getCompanyName());
                        mav.addObject("companyNumber",obj.getCompanyNumber());
                        mav.addObject("companyContactPlace",obj.getCompanyContactPlace());
                        mav.addObject("companyAddress",obj.getCompanyAddress());
                        mav.addObject("companyCorporation",obj.getCompanyCorporation());
                        mav.addObject("companyCorporationIdcard",obj.getCompanyCorporationIdcard());
                        mav.addObject("companyBank",obj.getCompanyBank());
                        mav.addObject("companyBankaddress",obj.getCompanyBankaddress());
                        mav.addObject("companyDepositBankNo",obj.getCompanyDepositBankNo());
                        mav.addObject("companyCorporationPhone",obj.getCompanyCorporationPhone());
                        mav.setViewName("xtr/payday/payforamount");
                    }else{
                        //LOGGER.info("５```");
                        mav.addObject("companyQuotaValue",obj.getCompanyCertificationAmount());
                        mav.addObject("companyBank",obj.getCompanyDepositBank());
                        // LOGGER.info("６```");
                        if (obj.getCompanyDepositBankNo() == null || obj.getCompanyDepositBankNo().length() < 4){
                            mav.addObject("companyBanknumber"," ");
                        }
                        else{
                            mav.addObject("companyBanknumber",obj.getCompanyDepositBankNo().substring(obj.getCompanyDepositBankNo().length()-4,obj.getCompanyDepositBankNo().length()));
                        }

                        // LOGGER.info("７```");
                        mav.addObject("companyName",obj.getCompanyDepositBankAccountName());
                        if(obj.getAmountNumber()!=null){
                            mav.addObject("amountNumber",obj.getAmountNumber());
                        }else{
                            mav.addObject("amountNumber",3);
                        }
                        if(obj.getAmountNumber()!=null && obj.getAmountNumber()==0){
                            // LOGGER.info("８```");
                            mav.setViewName("xtr/payday/bankfailure");
                        }else{
                            //LOGGER.info("９```");
                            mav.setViewName("xtr/payday/bankcertification");
                        }
                    }
                }
            }else {
                //LOGGER.info("１０```");
                if("1".equals(paytype)){
                    //LOGGER.info("１１```");
                    if("1".equals(obj.getCompanyDatumStatus())){
                        mav.setViewName("xtr/payday/noaudit");
                    }else{
                        mav.setViewName("xtr/payday/literaturereview");
                    }
                }else{
                    //LOGGER.info("１２```");
                    mav.setViewName("xtr/payday/paycomplete");
                }
            }
        }
        return mav;
    }

    /**
     * 主页面
     *
     * @param mav
     * @return
     */
    @RequestMapping({"payhome.htm"})
    public ModelAndView home(ModelAndView mav,HttpServletRequest request) {
        CompanyMembersBean loginUserObj = getLoginUserObj(request);
        mav.addObject("memberLogname",loginUserObj.getMemberLogname());
        mav.addObject("companyIsAuth",1);
        mav.setViewName("xtr/home");
        return mav;
    }

    /**
     * 资料审核页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("literaturereview.htm")
    public ModelAndView leviewshow(ModelAndView mav, @RequestParam("companyId") Long companyId, @RequestParam("paytype") String paytype, @RequestParam("companyBanknumber") String companyBanknumber, @RequestParam("companyName") String companyName, @RequestParam("amount") String amount) {
        if (companyId == null) {
            throw new BusinessException("主键不能为空");
        }

        CompanysBean companysBean = new CompanysBean();
        companysBean.setCompanyIsauth(1);
        companysBean.setCompanyDatumStatus(0);
        companysBean.setCompanyId(companyId);

        companysService.updateCompanysBeanId(companysBean);
        mav.addObject("companyId",companyId);
        if("1".equals(paytype)){
            mav.setViewName("xtr/payday/literaturereview");
        }else{
            mav.setViewName("xtr/payday/paycomplete");
        }
        return mav;
    }

    /**
     * 跳转资料审核页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("literaturereviewquery.htm")
    public ModelAndView leviewshowquery(ModelAndView mav) {
        mav.setViewName("xtr/payday/literaturereview");
        return mav;
    }

    /**
     * 权限管理
     * @param request
     * @return
     */
    @RequestMapping(value="queryPowersManager.htm")
    @ResponseBody
    public ResultResponse queryPowersManager(HttpServletRequest request){

       LOGGER.info("请求权限管理。。。。。。");
       ResultResponse  resultResponse = new ResultResponse();
       try {
           CompanysBean obj = getLoginCompanyObj(request);
           obj=companysService.selectCompanyByCompanyId(obj.getCompanyId());
           List<CompanyRoleBean> listRole=companyMembersService.selectCompanyRoleBeanByState(0);
           CompanyMembersBean cmb=new CompanyMembersBean();
           cmb.setMemberCompanyId(obj.getCompanyId());
           //cmb.setMemberIsdefault(0);
           cmb.setMemberStatus(3);
           List<CompanyMembersBean> listCmember=companyMembersService.selectByCompanyMembersBean(cmb);
           for(int i=0;i<listCmember.size();i++){
               Map<String,String> map=companyMembersService.selectCompanyMenuBeanByUserId(listCmember.get(i).getMemberId());
               if(map!=null){
                   listCmember.get(i).setRoleName(map.get("roleName"));
                   listCmember.get(i).setIdValue(map.get("ids"));
               }else{
                   listCmember.get(i).setRoleName("");
               }
           }
             String listRoleStr ="";
             for(int i=0;i<listRole.size();i++){
                 if(i<listRole.size()-1) {
                     listRoleStr += listRole.get(i).getRoleName() + ",";
                 }else{
                     listRoleStr+=listRole.get(i).getRoleName()+"";
                 }
             }
              Map map = new HashMap();
              if(listRole!=null&&listRole.size()>0){
                  map.put("listRole",listRole);
              }
              map.put("listRoleStr",listRoleStr);
              map.put("listCmember",listCmember);
              resultResponse.setSuccess(true);
              resultResponse.setData(map);

       }catch ( Exception  e){
          LOGGER.error("当前查询权限出现异常错误："+e.getMessage());
       }
      return resultResponse;
    }




    /**
     * 添加用户权限 显示该用户有哪些权限
     * @return
     */
  @RequestMapping(value="showHasPowers.htm")
  @ResponseBody
  public ResultResponse showHasPower(HttpServletRequest request){

      ResultResponse resultResponse = new ResultResponse();
      try{
          CompanysBean obj = getLoginCompanyObj(request);
          obj=companysService.selectCompanyByCompanyId(obj.getCompanyId());
          List<CompanyRoleBean> listRole=companyMembersService.selectCompanyRoleBeanByState(0);
          resultResponse.setData(listRole);
          resultResponse.setSuccess(true);
      }catch ( Exception e ){
          resultResponse.setMessage(e.getMessage());
          LOGGER.error("点击添加操作员出现异常错误："+e.getMessage());
      }

        return resultResponse;
  }



    /**
     * 操作日志的分页查询
     * @return
     */
    @RequestMapping(value="logsManagers.htm")
    @ResponseBody
    public  ResultResponse logsManagers(HttpServletRequest request,String userName,
                                        @RequestParam(required = false, defaultValue = "1") int pageIndex,
                                        @RequestParam(required = false, defaultValue = "8") int pageSize){
         ResultResponse resultResponse =null;
         try{
             LOGGER.info("请求操作日志分页>>>>>>"+pageIndex);
             CompanysBean obj = getLoginCompanyObj(request);
             obj=companysService.selectCompanyByCompanyId(obj.getCompanyId());
             CompanyMembersBean loginUserObj = getLoginUserObj(request);
             CompanyLogBean sysLogBean=new CompanyLogBean();
             if (!com.xtr.comm.jd.util.StringUtils.isBlank(userName)){
                     sysLogBean.setUserName(userName);
             }
                 sysLogBean.setMemberCompanyId(obj.getCompanyId());
                 sysLogBean.setPageIndex(pageIndex);
                 sysLogBean.setPageSize(pageSize);
                 resultResponse = companyLogService.selectPageList(sysLogBean);
                 if (resultResponse != null && resultResponse.isSuccess()) {

                     PageList<CompanyLogBean> list = (PageList<CompanyLogBean>) resultResponse.getData();
                     List<CompanyLogBean> logList = new ArrayList<CompanyLogBean>();
                     String dateStr="";
                     String typeStr="";
                     for (CompanyLogBean companyLogBean:list){
                          if(companyLogBean.getType()==0){
                              typeStr="成功";
                          }else{
                              typeStr="失败";
                          }
                          if(!com.xtr.comm.util.StringUtils.isEmpty(companyLogBean.getCreateTime())){
                              dateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(companyLogBean.getCreateTime());
                          }
                         companyLogBean.setDateStr(dateStr);
                         companyLogBean.setTypeStr(typeStr);
                         logList.add(companyLogBean);
                      }
                     Map map = new HashMap();
                     //整理参数
                     map.put("ordersData",logList);
                     map.put("paginator",resultResponse.getPaginator());
                     resultResponse.setData(map);
                 }

         }catch ( Exception e ){
            LOGGER.error("当前点击操作日志出现异常错误："+e.getMessage());
         }
        return  resultResponse;
    }



    /**
     * 跳转完善信息页面
     * @param mav
     * @return
     */
    @RequestMapping("payforInfo.htm")
    public ModelAndView payforInfo(ModelAndView mav, HttpServletRequest request, @RequestParam("type") String type,
                                   @RequestParam(value = "userName", required = false) String userName) {

        CompanysBean obj = getLoginCompanyObj(request);
        obj=companysService.selectCompanyByCompanyId(obj.getCompanyId());
        CompanyMembersBean loginUserObj = getLoginUserObj(request);
        List<CompanyRoleBean> listRole=companyMembersService.selectCompanyRoleBeanByState(0);
        mav.addObject("listRole",listRole);
        mav.addObject("companyName",obj.getCompanyName());
        if(obj.getCompanyLogo()==null){
            mav.addObject("companyLogo","");
        }else{
            mav.addObject("companyLogo",obj.getCompanyLogo());
        }
        mav.addObject("companyLogoUrl","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+obj.getCompanyLogo());
        mav.addObject("companyAddress",obj.getCompanyAddress());
        mav.addObject("companyNumber",obj.getCompanyNumber());
        if(obj.getCompanyOrganizationImg()==null){
            mav.addObject("companyOrganizationImg","");
        }else{
            mav.addObject("companyOrganizationImg",obj.getCompanyOrganizationImg());
        }
        mav.addObject("companyOrganizationImgUrl","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+obj.getCompanyOrganizationImg());
        mav.addObject("companyCorporation",obj.getCompanyCorporation());
        mav.addObject("companyContactTel",obj.getCompanyContactTel());
        mav.addObject("companyScale",obj.getCompanyScale());
        mav.addObject("companyBelongIndustry",obj.getCompanyBelongIndustry());
        mav.addObject("companyChannel",obj.getCompanyChannel());
        mav.addObject("typeValue",type);
        mav.addObject("auditStatus",obj.getCompanyAuditStatus());
        mav.addObject("companyAuditRemark",obj.getCompanyAuditRemark());
        mav.addObject("isShow", false);
        //获取签约记录页签信息
//        List<CompanyProtocolsDto> protocloList=companyProtocolsService.selectProtocolByCompanyId(obj.getCompanyId());
//        mav.addObject("protocloList", protocloList);
        mav.setViewName("xtr/payday/payforInfo");
        return mav;
    }

    /**
     * 垫付工资社保页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("paysecurity.htm")
    public ModelAndView paysecurity(ModelAndView mav, HttpServletRequest request,
                                    @RequestParam(required = false, defaultValue = "0") int pageIndex,
                                    @RequestParam(required = false, defaultValue = "8") int pageSize,
                                    @RequestParam(required = false) String sort) {
        CompanysBean obj = getLoginCompanyObj(request);
        obj=companysService.selectCompanyByCompanyId(obj.getCompanyId());
        CompanyProtocolsBean companyProtocolsBean=new CompanyProtocolsBean();
        companyProtocolsBean.setProtocolCompanyId(obj.getCompanyId());
        List<CompanyProtocolsBean> list=companyProtocolsService.selectUsefulProtocolsByAddTime(companyProtocolsBean);
        if(list.size()>0){
            mav.addObject("companyProtocolsSum", "1");
            mav.addObject("protocol_serve_rate", list.get(0).getProtocolServeRate());
            mav.addObject("protocol_rate", list.get(0).getProtocolRate());
        }
        //获取登录用户
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);

        //获取页面数据
        CompanyBorrowOrdersBean companyBorrowOrdersBean = new CompanyBorrowOrdersBean();
        companyBorrowOrdersBean.setPageIndex(pageIndex);
        companyBorrowOrdersBean.setPageSize(pageSize);
        companyBorrowOrdersBean.setOrderCompanyId(companyMembersBean.getMemberCompanyId());
        ResultResponse resultResponse = companyBorrowOrdersService.selectPageListAll(companyBorrowOrdersBean);

        if (resultResponse != null && resultResponse.isSuccess()) {
            mav.addObject("ordersData", resultResponse.getData());
            mav.addObject("paginator", resultResponse.getPaginator());
        }
        mav.setViewName("xtr/payday/paysecurity");
        return mav;
    }

    /**
     * 申请垫付工资社保页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("applypayfor.htm")
    public ModelAndView applypayfor(ModelAndView mav, HttpServletRequest request) {
        CompanysBean obj = getLoginCompanyObj(request);
        obj=companysService.selectCompanyByCompanyId(obj.getCompanyId());

        CompanyProtocolsBean companyProtocolsBean=new CompanyProtocolsBean();
        companyProtocolsBean.setProtocolCompanyId(obj.getCompanyId());
        companyProtocolsBean.setProtocolContractType(2);
        List<CompanyProtocolsBean> list=companyProtocolsService.selectByContractType(companyProtocolsBean);
        if(list.size()>0){
            try{
                mav.addObject("protocol_serve_rate", list.get(0).getProtocolServeRate());
                mav.addObject("protocol_rate", list.get(0).getProtocolRate());
            }catch (Exception e){
            }
        }
        mav.setViewName("xtr/payday/applypayfor");
        return mav;
    }


    /**
     * 完成授信页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("completecredit.htm")
    public ModelAndView completecredit(ModelAndView mav, @RequestParam("typeId") Long typeId) {
        mav.addObject("typeId",typeId);
        mav.setViewName("xtr/payday/completecredit");
        return mav;
    }

    /**
     * 认证失败跳转页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("bankfailure.htm")
    public ModelAndView bankfailure(ModelAndView mav, HttpServletRequest request, @RequestParam("companyBank") String companyBank, @RequestParam("companyBanknumber") String companyBanknumber, @RequestParam("companyName") String companyName) {
        try {
            companyName=new String(companyName.getBytes("ISO-8859-1"),"utf-8");
            companyBank=new String(companyBank.getBytes("ISO-8859-1"),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mav.addObject("companyBank",companyBank);
        mav.addObject("companyBanknumber",companyBanknumber);
        mav.addObject("companyName",companyName);
        mav.setViewName("xtr/payday/bankfailure");
        return mav;
    }

    /*
   * <p>修改垫付金额错误次数</p>
   * @auther xulong
   * @createTime2016/7/14 11:57
   */
    @RequestMapping("amountnumber.htm")
    @ResponseBody
    public ResultResponse amountNumber(ModelAndView mav, HttpServletRequest request,@RequestParam("amountNumber") int amountNumber) throws IOException {
        ResultResponse resultResponse = new ResultResponse();
        CompanysBean obj = getLoginCompanyObj(request);
        obj.setAmountNumber(amountNumber);
        companysService.updateCompanysBeanId(obj);
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /*
   * <p>修改垫付执照信息</p>
   * @auther xulong
   * @createTime2016/7/14 11:57
   */
    @RequestMapping("confirmLicense.htm")
    @ResponseBody
    public ResultResponse confirmLicense(ModelAndView mav,@RequestParam("companyId") Long companyId,@RequestParam("companyOrganizationImg") String companyOrganizationImg) throws IOException {
        ResultResponse resultResponse = new ResultResponse();
        if (companyId == null) {
            throw new BusinessException("主键不能为空");
        }
        CompanysBean companysBean = new CompanysBean();
        companysBean.setCompanyOrganizationImg(companyOrganizationImg);
        companysBean.setCompanyId(companyId);
        companysService.updateCompanysBeanId(companysBean);
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /*
  * <p>获取资源文件信息数量</p>
  * @auther xulong
  * @createTime2016/7/27 11:57
  */
    @RequestMapping("fileresourcesCount.htm")
    @ResponseBody
    public ResultResponse getfileResourcesCount(HttpServletRequest request) throws IOException {
        ResultResponse resultResponse = new ResultResponse();
        CompanysBean loginCompanyObj = getLoginCompanyObj(request);
        CompanyMembersBean loginUserObj = getLoginUserObj(request);
        Map<String,Object> par=new HashMap<String,Object>();
        par.put("companyId", loginCompanyObj.getCompanyId());
        par.put("memberId", loginUserObj.getMemberId());
        List<Map<String,Object>> mapfile=companysService.getfileResourcesByFileType(par);
        resultResponse.setSuccess(true);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping().create();
        resultResponse.setData(gson.toJson(mapfile));
        return resultResponse;
    }

    /*
   * <p>获取资源文件信息</p>
   * @auther xulong
   * @createTime2016/7/27 11:57
   */
    @RequestMapping("fileresources.htm")
    @ResponseBody
    public ResultResponse getfileResources(HttpServletRequest request,@RequestParam("fileType") String fileType) throws IOException {
        ResultResponse resultResponse = new ResultResponse();
        CompanysBean loginCompanyObj = getLoginCompanyObj(request);
        CompanyMembersBean loginUserObj = getLoginUserObj(request);
        Map<String,Object> par=new HashMap<String,Object>();
        par.put("fileType", Integer.parseInt(fileType));
        par.put("companyId", loginCompanyObj.getCompanyId());
        par.put("memberId", loginUserObj.getMemberId());
        List<Map<String,Object>> mapfile=companysService.getfileResources(par);
        resultResponse.setSuccess(true);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping().create();
        resultResponse.setData(gson.toJson(mapfile));
        return resultResponse;
    }

    /*
   * <p>删除资源文件信息</p>
   * @auther xulong
   * @createTime2016/7/27 11:57
   */
    @RequestMapping("filedelete.htm")
    @ResponseBody
    public ResultResponse fileDelete(HttpServletRequest request,@RequestParam("fileUrl") String fileUrl,@RequestParam("id") Long id) throws IOException {
        ResultResponse resultResponse = new ResultResponse();
        try{
            companysService.deletefileResources(id);
            AliOss.deleteFile(PropertyUtils.getString("oss.bucketName.img"),fileUrl);
            resultResponse.setSuccess(true);
        }catch (Exception e){
            resultResponse.setSuccess(false);
        }
        return resultResponse;
    }

    /*
   * <p>完善信息手机验证码短息发送</p>
   * @auther xulong
   * @createTime2016/7/14 11:57
   */
    @RequestMapping("sendpayformessage.htm")
    @ResponseBody
    public ResultResponse sendPayForMessage(ModelAndView mav,CompanysBean companysBean) throws IOException {
        ResultResponse resultResponse = new ResultResponse();
        String memberPhone =  companysBean.getCompanyCorporationPhone();
        //生成随机数
        Random random=new Random(System.currentTimeMillis());
        String smscode=RandomNumber.getRandomNumberByLength(random, 6);
        String paraSmsCode = PropertyUtils.getString("environ.istest");    //短信验证码生成环境，如果是test测试环境，永远是111111
        if ("1".equals(paraSmsCode)){
            smscode= "111111";
        }
        LOGGER.info("smscode:>>>>>>" + smscode);

        resultResponse.setMessage(smscode);
        smscode="您的验证码是："+smscode+"。请不要把验证码泄露给其他人。如非本人操作，可不用理会！";
        System.out.println(smscode);
        sendMsgService.sendMsg(memberPhone,smscode);
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 文件上传
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping("payfor/upload.htm")
    @ResponseBody
    public ResultResponse upload(@RequestParam("file") MultipartFile multipartFile,HttpServletRequest request, @RequestParam("type") String type, @RequestParam("fileType") String fileType) throws Exception {
        LOGGER.info("文件上传开始```");
        ResultResponse resultResponse = new ResultResponse();
        //真实的文件名
        String fileName = multipartFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String newFileName = UUID.randomUUID().toString() + "." + suffix;
        boolean flag = false;
        try {
            //文件上传至服务器
            flag = AliOss.uploadFile(multipartFile.getInputStream(), newFileName,PropertyUtils.getString("oss.bucketName.img"));
            if(flag){
                if("1".equals(type)){
                    CompanysBean loginCompanyObj = getLoginCompanyObj(request);
                    CompanyMembersBean loginUserObj = getLoginUserObj(request);
                    FileResourcesBean fileResourcesBean=new FileResourcesBean();
                    fileResourcesBean.setFileType(Integer.parseInt(fileType));
                    fileResourcesBean.setFileUrl(newFileName);
                    fileResourcesBean.setCompanyId(loginCompanyObj.getCompanyId());
                    fileResourcesBean.setMemberId(loginUserObj.getMemberId());
                    fileResourcesBean.setFileAddime(new Date());
                    int result=companysService.addFileResources(fileResourcesBean);
                    fileResourcesBean.setId(Long.parseLong(String.valueOf(result)));
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping().create();
                    resultResponse.setMessage(gson.toJson(fileResourcesBean));
                }else{
                    resultResponse.setMessage(newFileName);
                }
            }
            resultResponse.setSuccess(flag);
            LOGGER.info("文件上传成功···");
        } catch (Exception e) {
            LOGGER.error("文件上传异常", e);
            resultResponse.setMessage(e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 垫付工资保存
     * @param companysBean
     * @return
     */
    @RequestMapping(value = "payforsave.htm", method = RequestMethod.POST)
    @ResponseBody
    @SystemControllerLog(operation = "完善公司信息", modelName = "垫付工资社保")
    public ResultResponse addRecharge(CompanysBean companysBean, HttpServletRequest request, @RequestParam("paytype") Long paytype) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            CompanysBean obj = getLoginCompanyObj(request);
            obj=companysService.selectCompanyByCompanyId(obj.getCompanyId());
            obj.setCompanyName(companysBean.getCompanyName());
            obj.setCompanyLogo(companysBean.getCompanyLogo());
            obj.setCompanyAddress(companysBean.getCompanyAddress());
            obj.setCompanyNumber(companysBean.getCompanyNumber());
            obj.setCompanyOrganizationImg(companysBean.getCompanyOrganizationImg());
            obj.setCompanyCorporation(companysBean.getCompanyCorporation());
            obj.setCompanyContactTel(companysBean.getCompanyContactTel());
            obj.setCompanyScale(companysBean.getCompanyScale());
            obj.setCompanyBelongIndustry(companysBean.getCompanyBelongIndustry());
            obj.setCompanyChannel(companysBean.getCompanyChannel());
            obj.setCompanyAuditStatus(0);
            companysService.updateCompanysBeanId(obj);
            request.getSession().setAttribute(CommonConstants.LOGIN_COMPANY_KEY, obj);
            resultResponse.setSuccess(true);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setSuccess(false);
        }
        return resultResponse;
    }

    /**
     * 垫付工资账户修改
     * @param mav
     * @param companysBean
     * @return
     */
    @RequestMapping(value = "payforamount.htm", method = RequestMethod.POST)
    @ResponseBody
    @SystemControllerLog(operation = "实名认证", modelName = "垫付工资社保")
    public ModelAndView updateRecharge(ModelAndView mav, CompanysBean companysBean, HttpServletRequest request, @RequestParam("paytype") Long paytype) {
        try {
            CompanysBean obj = getLoginCompanyObj(request);
            String orderId = idGeneratorService.getOrderId(BusinessEnum.COMPANY_ADVACE_BORROW);
            obj=companysService.selectCompanyByCompanyId(obj.getCompanyId());
            mav.addObject("paytype",paytype);
            obj.setCompanyDepositBankAccountName(companysBean.getCompanyDepositBankAccountName());
            obj.setCompanyDepositBank(companysBean.getCompanyDepositBank());
            obj.setCompanyDepositBankNo(companysBean.getCompanyDepositBankNo());
            obj.setCompanyDepositBankaddress(companysBean.getCompanyDepositBankaddress());
            obj.setCompanyAssociatedCode(companysBean.getCompanyAssociatedCode());
            obj.setAmountNumber(3);
            obj.setCompanyPayStatus(0);
            obj.setCompanyPayNumber(orderId);
            DecimalFormat dcmFmt = new DecimalFormat("0.00");
            Random rand = new Random();
            float f = rand.nextFloat();
            System.out.println(">>>><<<<<<<<<>>>> " +dcmFmt.format(f));
            mav.addObject("companyQuotaValue",dcmFmt.format(f));
            obj.setCompanyCertificationAmount(new BigDecimal(dcmFmt.format(f)));
            obj.setCompanyIsauth(2);
            mav.addObject("companyBean",obj);
            mav.addObject("companyBank",companysBean.getCompanyDepositBank());
            mav.addObject("companyBanknumber",companysBean.getCompanyDepositBankNo().substring(companysBean.getCompanyDepositBankNo().length()-4,companysBean.getCompanyDepositBankNo().length()));
            mav.addObject("companyName",companysBean.getCompanyDepositBankAccountName());
            mav.addObject("amountNumber",obj.getAmountNumber());
            companysService.updateCompanysBeanId(obj);
            mav.addObject("companyId",obj.getCompanyId());

            try {
                DefrayPayRequest dr = new DefrayPayRequest(BusinessType.VALIDATE_CUSTOMER);
                BankCodeBean bankCodeBean = bankCodeService.selectByBankName(companysBean.getCompanyDepositBank());
                if (bankCodeBean != null){
                    //收款银行编码
                    dr.setPayeeBankCode(bankCodeBean.getBankCode());

                    dr.setPayeeBankAssociatedCode(companysBean.getCompanyAssociatedCode());//*联行号，对公 必填
                    dr.setPayeeAccountType("C");//*收款帐户类型  对私户=P；对公户=C 默认P
                    dr.setPayeeCardType("DE"); //收款卡种  借记卡=DE；信用卡=CR 默认DE
                    dr.setPayeeAccountNo(companysBean.getCompanyDepositBankNo()); //*收款帐户号 必填
                    dr.setPayeeAccountName(companysBean.getCompanyDepositBankAccountName());//*收款帐户名称 必填
                    dr.setTradeSubject("实名认证"); //订单摘要  商品描述，订单标题，关键描述信息 必填
                    //dr.setTradeAmount(dcmFmt.format(f));//*订单交易金额  单位：分，大于0。必填
                    dr.setTradeAmount(String.valueOf((int)(f*100)));
                    dr.setOutTradeNo(orderId);//订单ID 唯一 必填
                    jdPayService.defrayPay(dr);
                }
                else{
                    LOGGER.info("收款银行获取失败：" + companysBean.getCompanyDepositBank());
                    obj.setCompanyIsauth(0);
                    companysService.updateCompanysBeanId(obj);
                    mav.addObject("companyDepositBankAccountName", companysBean.getCompanyDepositBankAccountName());
                    mav.addObject("companyDepositBank", companysBean.getCompanyDepositBank());
                    mav.addObject("companyDepositBankNo", companysBean.getCompanyDepositBankNo());
                    mav.addObject("companyDepositBankaddress", companysBean.getCompanyDepositBankaddress());
                    mav.addObject("companyAssociatedCode", companysBean.getCompanyAssociatedCode());
                    mav.addObject("error","暂不支持该收款银行");
                    mav.setViewName("xtr/payday/payforamount");
                    return mav;
                }



            }catch (Exception e) {
                LOGGER.error("京东发送异常：" + e.getMessage(), e);

                obj.setCompanyIsauth(0);
                companysService.updateCompanysBeanId(obj);
                mav.addObject("companyDepositBankAccountName", companysBean.getCompanyDepositBankAccountName());
                mav.addObject("companyDepositBank", companysBean.getCompanyDepositBank());
                mav.addObject("companyDepositBankNo", companysBean.getCompanyDepositBankNo());
                mav.addObject("companyDepositBankaddress", companysBean.getCompanyDepositBankaddress());
                mav.addObject("companyAssociatedCode", companysBean.getCompanyAssociatedCode());
                mav.addObject("error", e.getMessage());
                mav.setViewName("xtr/payday/payforamount");
                return mav;
            }


        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            mav.addObject("companyDepositBankAccountName", companysBean.getCompanyDepositBankAccountName());
            mav.addObject("companyDepositBank", companysBean.getCompanyDepositBank());
            mav.addObject("companyDepositBankNo", companysBean.getCompanyDepositBankNo());
            mav.addObject("companyDepositBankaddress", companysBean.getCompanyDepositBankaddress());
            mav.addObject("companyAssociatedCode", companysBean.getCompanyAssociatedCode());
            mav.addObject("error", e.getMessage());
            mav.setViewName("xtr/payday/payforamount");
            return mav;
        }
        mav.setViewName("xtr/payday/bankcertification");
        return mav;
    }

    /**
     * 垫付工资社保增加
     * @param mav
     * @param companyBorrowOrdersBean
     * @return
     */
    @RequestMapping(value = "addapplypayfor.htm", method = RequestMethod.POST)
    @ResponseBody
    //@SystemControllerLog(operation = "申请垫付", modelName = "垫付工资社保")
    public ModelAndView addRecharge(ModelAndView mav, CompanyBorrowOrdersBean companyBorrowOrdersBean, HttpServletRequest request) {
        String orderId = idGeneratorService.getOrderId(BusinessEnum.COMPANY_ADVACE_BORROW);
        CompanysBean obj = getLoginCompanyObj(request);
        companyBorrowOrdersBean.setOrderCompanyId(obj.getCompanyId());
        companyBorrowOrdersBean.setOrderDepId(10L);
        companyBorrowOrdersBean.setOrderMoney(new BigDecimal(companyBorrowOrdersBean.getOrderMoneyValue()));
        companyBorrowOrdersBean.setOrderActualServer(new BigDecimal(companyBorrowOrdersBean.getOrderActualServerValue()));
        companyBorrowOrdersBean.setOrderAddtime(new Date());
        companyBorrowOrdersBean.setOrderNumber(orderId);
        companyBorrowOrdersBean.setOrderState(0);
        //companyBorrowOrdersBean.setOrderCanceltime(new Date());
        companyBorrowOrdersBean.setOrderSign(10);
        companyBorrowOrdersBean.setOrderInterestType(companyBorrowOrdersBean.getOrderInterestType());
        companyBorrowOrdersBean.setOrderInterestCycle(companyBorrowOrdersBean.getOrderInterestCycle());
        companyBorrowOrdersBean.setOrderRepayType(companyBorrowOrdersBean.getOrderRepayType());
        companyBorrowOrdersBean.setOrderExpectDay(companyBorrowOrdersBean.getOrderExpectDay());
        companyBorrowOrdersBean.setOrderFinanceResion(companyBorrowOrdersBean.getOrderFinanceResion());
        //companyBorrowOrdersBean.setOrderRepayStarttime(new Date());
        //companyBorrowOrdersBean.setOrderRepayEndtime(new Date());
        companyBorrowOrdersBean.setOrderCycle(6);
        companyBorrowOrdersBean.setOrderCycleUnit(2);
        companyBorrowOrdersBean.setOrderRate(new BigDecimal(companyBorrowOrdersBean.getOrderRateValue()));
        companyBorrowOrdersBean.setOrderPropId(10);
        companyBorrowOrdersBean.setOrderPropCouponType(0);
        companyBorrowOrdersBean.setOrderMoneyArrival(new BigDecimal(companyBorrowOrdersBean.getOrderMoneyValue()));
        companyBorrowOrdersBean.setOrderActualMoney(new BigDecimal(companyBorrowOrdersBean.getOrderMoneyValue()));

        try {
            int result = companyBorrowOrdersService.addBorrowOrder(companyBorrowOrdersBean);
            mav.addObject("orderMoneyValue",companyBorrowOrdersBean.getOrderMoney());
            mav.addObject("orderActualServer",companyBorrowOrdersBean.getOrderActualServer());
            mav.addObject("orderInterestType",companyBorrowOrdersBean.getOrderInterestType());
            mav.addObject("orderInterestCycle",companyBorrowOrdersBean.getOrderInterestCycle());
            mav.addObject("orderRepayType",companyBorrowOrdersBean.getOrderRepayType());
            mav.addObject("orderRate",companyBorrowOrdersBean.getOrderRate());
            mav.addObject("orderExpectDay",companyBorrowOrdersBean.getOrderExpectDay());
            LOGGER.info("::::=> result = " + result);
            LOGGER.info("::::=> 保存企业借款订单结束");
        } catch (BusinessException e) {
            LOGGER.error(e.getMessage(), e);
            mav.addObject("error", e.getMessage());
            mav.setViewName("xtr/account/recharge");
            return mav;
        }
        mav.setViewName("xtr/payday/applywin");
        return mav;
    }

    /**
     * 借款信息保存
     * @param orderState
     * @return
     */
    @RequestMapping(value = "borrowInfoupdate.htm", method = RequestMethod.POST)
    @ResponseBody
    @SystemControllerLog(operation = "撤销垫付", modelName = "垫付工资社保")
    public ResultResponse updateRecharge(Long orderId,Integer orderState) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            CompanyBorrowOrdersBean companyBorrowOrdersBean=new CompanyBorrowOrdersBean();
            companyBorrowOrdersBean.setOrderId(orderId);
            companyBorrowOrdersBean.setOrderState(orderState);
            companyBorrowOrdersService.updateCompanyBorrowOrdersBeanId(companyBorrowOrdersBean);
            resultResponse.setSuccess(true);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setSuccess(false);
        }
        return resultResponse;
    }

    /**
     * 添加操作员
     * @param companyMembersBean
     * @return
     */
    @RequestMapping(value = "companyRoleAdd.htm", method = RequestMethod.POST)
    @ResponseBody
    @SystemControllerLog(operation = "添加操作员", modelName = "公司设置")
    public ResultResponse companyRoleAdd(CompanyMembersBean companyMembersBean, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            CompanyMembersBean loginUserObj = getLoginUserObj(request);
            CompanyMembersBean addUserObj = new CompanyMembersBean();
            addUserObj.setMemberCompanyId(loginUserObj.getMemberCompanyId());
            boolean memberIsExist = true;
            String memberLogname = companyMembersBean.getMemberPhone();

            List<CompanyMembersBean>  hasCom = companyMembersService.selectByPhoneOrLogname(companyMembersBean);
            memberIsExist = null != hasCom && hasCom.size() > 0;

            if (!memberIsExist){
                Random random=new Random(System.currentTimeMillis());
                String smscode=RandomNumber.getRandomNumberByLength(random, 6);
                String processMemberPassword = com.xtr.comm.util.StringUtils.getMD5two(smscode);
                addUserObj.setMemberPassword(processMemberPassword);
                smscode="您的操作员密码是："+smscode+"。请不要把操作员密码泄露给其他人。如非本人操作，可不用理会！";


                sendMsgService.sendMsg(companyMembersBean.getMemberPhone(),smscode);
                addUserObj.setMemberSign(CompanyMeMemberSignEnum.UNACTIVE.getCode());
                addUserObj.setMemberIsdefault(CompanyMeMemberIsdefaultEnum.NO_MANAGER.getCode());
                addUserObj.setRegisterTime(new Date());
                addUserObj.setMemberName(companyMembersBean.getMemberName());
                addUserObj.setMemberLogname(companyMembersBean.getMemberPhone());
                addUserObj.setMemberPhone(companyMembersBean.getMemberPhone());
                addUserObj.setMemberSign(CompanyMeMemberSignEnum.ACTIVE.getCode());
                addUserObj.setMemberStatus(1);
                addUserObj.setMemberId(null);
                int result = companyMembersService.insert(addUserObj);
                if(result>0){
                    if (!StringUtils.isBlank(companyMembersBean.getMemberPowerIds())){
                        String[] ary = companyMembersBean.getMemberPowerIds().split(",");
                        for(String item: ary){
                            CompanyRoleRelBean companyRoleRelBean=new CompanyRoleRelBean();
                            companyRoleRelBean.setObjId(Long.parseLong(String.valueOf(result)));
                            companyRoleRelBean.setRelType(1);
                            companyRoleRelBean.setRoleId(Long.parseLong(item));
                            companyMembersService.companyRoleRelBeanAdd(companyRoleRelBean);
                        }
                    }
                    resultResponse.setSuccess(true);
                }else{
                    resultResponse.setMessage("添加操作员失败");
                    resultResponse.setSuccess(false);
                }
            }
            else{
                resultResponse.setMessage("手机号码已经被注册");
                resultResponse.setSuccess(false);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setMessage("添加操作员失败");
            resultResponse.setSuccess(false);
        }
        return resultResponse;
    }

    /**
     * 修改用户角色
     * @param companyMembersBean
     * @return
     */
    @RequestMapping(value = "companyMemberEdit.htm", method = RequestMethod.POST)
    @ResponseBody
    @SystemControllerLog(operation = "修改操作员", modelName = "公司设置")
    public ResultResponse companyMemberEdit(CompanyMembersBean companyMembersBean, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            CompanyRoleRelBean companyRoleRelBean=new CompanyRoleRelBean();
            companyRoleRelBean.setRelType(1);
            companyRoleRelBean.setObjId(companyMembersBean.getMemberId());
            companyMembersService.deleteRoleRelBeanById(companyRoleRelBean);

            if (!StringUtils.isBlank(companyMembersBean.getMemberPowerIds())){
                String[] ary = companyMembersBean.getMemberPowerIds().split(",");
                for(String item: ary){
                    companyRoleRelBean.setRoleId(Long.parseLong(item));
                    companyMembersService.companyRoleRelBeanAdd(companyRoleRelBean);
                }
            }
            resultResponse.setSuccess(true);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setSuccess(false);
        }
        return resultResponse;
    }

    /**
     * 删除用户
     * @param companyMembersBean
     * @return
     */
    @RequestMapping(value = "companyMemberDel.htm", method = RequestMethod.POST)
    @ResponseBody
    //@SystemControllerLog(operation = "删除操作员", modelName = "公司设置")
    public ResultResponse companyMemberDel(CompanyMembersBean companyMembersBean, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            int result = companyMembersService.updateByPrimaryKeySelective(companyMembersBean);
            if(result > 0) {
                resultResponse.setSuccess(true);
                //插入日志
                //*========数据库日志=========*//
                CompanyLogBean sysLogBean = new CompanyLogBean();
                //所属模块
                sysLogBean.setModelName("完善信息模块");
                //用户所做的操作
                if(companyMembersBean.getMemberStatus()==1){
                    sysLogBean.setOperation("开启操作员");
                }else if(companyMembersBean.getMemberStatus()==2){
                    sysLogBean.setOperation("关闭操作员");
                }else if(companyMembersBean.getMemberStatus()==3){
                    sysLogBean.setOperation("删除操作员");
                }
                //日志类型  0：操作日志 1：异常日志
                sysLogBean.setType(Integer.valueOf(0));
                //请求Ip
                sysLogBean.setRequestIp(request.getRemoteAddr());
                //服务器名称
                sysLogBean.setServerName(InetAddress.getLocalHost().getHostName());
                //操作人
                sysLogBean.setUserId(companyMembersBean.getMemberId());
                //创建时间
                sysLogBean.setCreateTime(new Date());
                companyLogService.insert(sysLogBean);
            }else{
                resultResponse.setSuccess(false);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            resultResponse.setSuccess(false);
        }
        return resultResponse;
    }


    /**
     * 跳转签约页面
     * @return
     */
    @RequestMapping(value="toSignApplyPage.htm")
    public  ModelAndView jumpToSignApplyPage(ModelAndView modelAndView,HttpServletRequest request){
        modelAndView.setViewName("xtr/payday/signApply");
      CompanysBean companysBean = SessionUtils.getCompany(request);
      //获取协议类型
      Integer type = Integer.valueOf(request.getParameter("type"));
        String typeStr =null;
      if(type==1){
          typeStr="代发协议";
      } else if(type==2){
          typeStr="垫发协议";
      } else if(type==3){
          typeStr="社保代缴协议";
      }else{
          typeStr="报销管理";
      }
      modelAndView.addObject("typeStr",typeStr);
      modelAndView.addObject("type",type);
      //公司名称
      modelAndView.addObject("companyName",companysBean.getCompanyName());
      modelAndView.addObject("companyId",companysBean.getCompanyId());
      return  modelAndView;
    }

    /**
     * 新版签约
     * @param request
     * @return
     */
    @RequestMapping(value="toApplySignNew.htm")
    @ResponseBody
    public ResultResponse applySignNew(HttpServletRequest request){

        ResultResponse resultResponse = new ResultResponse();
        try{
            CompanysBean companysBean = SessionUtils.getCompany(request);
            Long companyId = companysBean.getCompanyId();
            String companyName = companysBean.getCompanyName();
            //获取协议类型
            Integer type = Integer.valueOf(request.getParameter("type"));
            String typeStr =null;
            if(type==1){
                typeStr="代发协议";
            } else if(type==2){
                typeStr="垫发协议";
            } else if(type==3){
                typeStr="社保代缴协议";
            }else{
                typeStr="报销管理";
            }
          Map<String,Object> map = new HashedMap();
          map.put("companyId",companyId);
          map.put("typeStr",typeStr);
          map.put("companyName",companyName);
          map.put("type",type);

         resultResponse.setSuccess(true);
         resultResponse.setData(map);

      }catch (Exception e){
        LOGGER.error("跳转签约弹框出现错误："+e.getMessage(),e);
      }
      return resultResponse;
    }





    /**
     * 获取签约记录
     * @param request
     * @return
     */
    @RequestMapping(value="querySignRecord.htm")
    @ResponseBody
    public Map querySignRecord(HttpServletRequest request){
          Map map = new HashMap();
        try{
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            List<CompanyProtocolsDto> protocloList=companyProtocolsService.selectProtocolByCompanyId(companyMembersBean.getMemberCompanyId());
            //循环
            List list = new ArrayList();
            //状态
            String stateStr =null;
            //协议名称
            String protocloTypeName = null;
            String  applyTimeStr =null;
            String isNeedApply=null;
            String produceComment=null;//签约产品说明
            if(protocloList!=null&&protocloList.size()>0){
                for (CompanyProtocolsDto co :protocloList){
                     if(!com.xtr.comm.util.StringUtils.isEmpty(co.getApplyState())){
                         if(co.getApplyState()==1){
                             stateStr = "已签约";
                             isNeedApply="";
                         }else if(co.getApplyState()==2){
                             stateStr="未签约";
                             isNeedApply="申请签约";
                         }else if(co.getApplyState()==3){
                             stateStr ="已提交签约申请";
                             isNeedApply="撤销申请";
                         }else if(co.getApplyState()==4){
                             stateStr ="已过期";
                             isNeedApply="续约";
                         }
                     }
                    //判断协议名称
                    if(co.getProtocolContractType()==1){
                        protocloTypeName="代发协议";
                        produceComment="薪资智能发放，计算、发放、查询一体化。";
                    }else if(co.getProtocolContractType()==2){
                        protocloTypeName="垫发协议";
                        produceComment="提供垫付工资、社保等服务，优化企业资金配置。";
                    }else if(co.getProtocolContractType()==3){
                        protocloTypeName="社保代缴协议";
                        produceComment="增员、汇缴、补缴、减员在线操作，缴纳区域覆盖全国。";
                    }else {
                        protocloTypeName="报销管理";
                        produceComment="在线财务报销，同时提供个税申报服务";
                    }
                    //判断时间
                    if(!com.xtr.comm.util.StringUtils.isEmpty(co.getProtocolContractTime())&& !com.xtr.comm.util.StringUtils.isEmpty(co.getProtocolExpireTime())){
                        //签约时间  跟 到期时间
                        String contractTime = new SimpleDateFormat("yyyyMMdd").format(co.getProtocolContractTime());
                        String expireTime  =  new  SimpleDateFormat("yyyyMMdd").format(co.getProtocolExpireTime());
                        applyTimeStr =contractTime+"--"+expireTime;
                    }else{
                        applyTimeStr="--";
                        if(!com.xtr.comm.util.StringUtils.isEmpty(co.getProtocolContractTime())){
                            String contractTime = new SimpleDateFormat("yyyyMMdd").format(co.getProtocolContractTime());
                            applyTimeStr =contractTime+"--";
                        }
                        if(!com.xtr.comm.util.StringUtils.isEmpty(co.getProtocolExpireTime())){
                            String expireTime  =  new  SimpleDateFormat("yyyyMMdd").format(co.getProtocolExpireTime());
                            applyTimeStr ="--"+expireTime;
                        }

                    }
                    co.setIsNeedApply(isNeedApply);
                    co.setApplyStateStr(stateStr);
                    co.setApplyTimeStr(applyTimeStr);
                    co.setProtocolContractTypeStr(protocloTypeName);
                    co.setProduceComment(produceComment);
                    list.add(co);

                }

            }
            map.put("success",true);
            map.put("msessage","查询成功");
            map.put("list",list);
            LOGGER.info(JSON.toJSONString(list));
        } catch ( Exception e ){
            LOGGER.error("获取签约记录出现异常错误："+e.getMessage());
        }
        return map;
    }

    /**
     * 新增明确期望签约意向申请
     * @param stationCollaborationBean
     * @return
     */
    @RequestMapping(value = "addSureProtocol.htm", method = RequestMethod.POST)
    @ResponseBody
    @SystemControllerLog(operation = "新增明确期望签约意向申请", modelName = "完善信息模块")
    public ResultResponse addSureProtocol(StationCollaborationBean stationCollaborationBean) {
        LOGGER.info("新增明确期望签约意向申请,传递参数:"+JSON.toJSONString(stationCollaborationBean));
        ResultResponse resultResponse = new ResultResponse();
        try {
            if(stationCollaborationBean!=null && stationCollaborationBean.getCollaborationCompanyId()!=null){
                //获取企业信息
                CompanysBean companysBean=companysService.selectCompanyByCompanyId(stationCollaborationBean.getCollaborationCompanyId());
                if(companysBean!=null && !com.xtr.comm.util.StringUtils.isStrNull(companysBean.getCompanyContactTel())){
                    stationCollaborationBean.setItemMobile(companysBean.getCompanyContactTel());
                }
                if(companysBean!=null && !com.xtr.comm.util.StringUtils.isStrNull(companysBean.getCompanyName())){
                    stationCollaborationBean.setItemName(companysBean.getCompanyName());
                }
                stationCollaborationBean.setItemType(StationCollaborationConstants.STATIONCOLLABRATION_TYPE_SURE);
                stationCollaborationBean.setAdddate(new Date());
                stationCollaborationBean.setSign(StationCollaborationConstants.STATIONCOLLABRATION_STATE_WILL);
                stationCollaborationBean.setItemTimes(1);
                stationCollaborationBean.setCollaborationCloseItems(1);
                int count=stationCollaborationService.insertSelective(stationCollaborationBean);
                if(count<=0){
                    throw new BusinessException("新增明确期望签约意向申请失败");
                }else{
                    resultResponse.setSuccess(true);
                }
            }else{
                throw new BusinessException("新增明确期望签约意向申请,请传递参数");
            }
        } catch(BusinessException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("新增明确期望签约意向申请"+e.getMessage());
        }catch(Exception e){
            resultResponse.setMessage("新增明确期望签约意向申请失败");
            LOGGER.error("新增明确期望签约意向申请",e);
        }
        LOGGER.info("新增明确期望签约意向申请,返回结果:"+JSON.toJSONString(resultResponse));
        return resultResponse;
    }

    /**
     * 物理删除签约意向申请
     * @param request
     * @return
     */
    @RequestMapping(value="deleteSureProtocol.htm")
    @ResponseBody
    public ResultResponse deleteSureProtocol(HttpServletRequest request,Integer contractType ){

        ResultResponse  resultResponse = new ResultResponse();

        try{
         CompanysBean companysBean = SessionUtils.getCompany(request);
         Long  companyId = companysBean.getCompanyId();
            Map map =  new HashMap();
            map.put("companyId",companyId);
            map.put("contractType",contractType);
          int  deleteFlag =  stationCollaborationService.deleteSureProtocol(map);
          if(deleteFlag==1){
              resultResponse.setSuccess(true);
              resultResponse.setMessage("成功撤销申请");
          }

        }catch (Exception e){
              LOGGER.error("撤销签约申请出现异常错误：");
        }
        return resultResponse;
    }

    /**
     * 续约申请
     * @param request
     * @return
     */
    @RequestMapping(value="renewSureProtocol")
    @ResponseBody
    public ResultResponse  renewSureProtocol(HttpServletRequest request,Integer contractType ){

       ResultResponse resultResponse = new ResultResponse();

       try{
        CompanysBean companysBean = SessionUtils.getCompany(request);
        Long companyId = companysBean.getCompanyId();
        Map map =  new HashMap();
        map.put("companyId",companyId);
        map.put("contractType",contractType);
        int  updateFlag = companyProtocolsService.renewSureProtocol(map);
        if(updateFlag==1){
            resultResponse.setSuccess(true);
            resultResponse.setMessage("续约成功");
        }

       }catch (Exception e){
        LOGGER.error("续约申请出现异常错误："+e.getMessage(),e);
       }
      return resultResponse;
    }

}


