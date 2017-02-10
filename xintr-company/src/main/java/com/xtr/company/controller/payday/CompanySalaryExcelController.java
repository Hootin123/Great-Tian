package com.xtr.company.controller.payday;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.basic.SendMsgResponse;
import com.xtr.api.domain.company.*;
import com.xtr.api.service.company.CompanyMsgsService;
import com.xtr.api.service.company.CompanyRechargesService;
import com.xtr.api.service.company.CompanySalaryExcelService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.send.SendMsgService;
import com.xtr.api.service.station.StationCollaborationService;
import com.xtr.api.service.validate.ValidateService;
import com.xtr.api.util.ExcelUtil;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CompanyRechargeConstant;
import com.xtr.comm.constant.GrantStateConstant;
import com.xtr.comm.constant.StationCollaborationConstants;
import com.xtr.comm.fastdfs.FastDFSResult;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.comm.util.StringUtils;
import com.xtr.company.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.xtr.comm.util.StringUtils.isStrNull;

/**
 * <p>企业上传工资文档</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/21 13:47
 */
@Controller
public class CompanySalaryExcelController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanySalaryExcelController.class);

    @Resource
    private CompanySalaryExcelService companySalaryExcelService;

    @Resource
    private StationCollaborationService stationCollaborationService;

    @Resource
    private CompanysService companysService;

    @Resource
    private CompanyRechargesService companyRechargesService;

    /**
     * 工资单非空验证服务
     */
    @Resource(name = "payrollValidate")
    private ValidateService validateService;

    /**
     * 发短信服务
     */
    @Resource
    private SendMsgService sendMsgService;
    /**
     * 公司消息服务
     */
    @Resource
    private CompanyMsgsService companyMsgsService;
    /**
     * 上传页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("upload.htm")
    public ModelAndView updatePage(ModelAndView mav) {
        mav.setViewName("xtr/payday/upload");
        return mav;
    }


    /**
     * 文件上传
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping("salary/upload.htm")
    @ResponseBody
    public ResultResponse upload(HttpServletRequest request,
                                 @RequestParam("file") MultipartFile multipartFile,
                                 @RequestParam("payday") String payday,
                                 @RequestParam("month") String month,
                                 @RequestParam("year") String year) throws Exception {
        LOGGER.info("文件上传开始```");
        //上传工资单时,发起合作意向开始
        CompanyMembersBean collaborationCompanyBean = SessionUtils.getUser(request);
        //根据企业ID获取企业信息
        CompanysBean companysBean=companysService.selectCompanyByCompanyId(collaborationCompanyBean.getMemberCompanyId());
        if(companysBean!=null){
            companysBean.setCompanyCorporationPhone(collaborationCompanyBean.getMemberPhone());
        }
        ResultResponse collaborationResponse=stationCollaborationService.updateOpeationType(companysBean, StationCollaborationConstants.STATIONCOLLABRATION_TYPE_COMMITSARLY);
        LOGGER.info("提交工资单发起合作意向,返回结果:"+ JSON.toJSONString(collaborationResponse));
        if(!collaborationResponse.isSuccess()){
            throw new Exception(collaborationResponse.getMessage());
        }
        //上传工资单时,发起合作意向结束

        ResultResponse resultResponse = new ResultResponse();
        //真实的文件名
        String fileName = multipartFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        FastDFSResult fastDFSResult = null;
        String newFileName = UUID.randomUUID().toString() + "." + suffix;
        boolean flag = false;
        try {
            //不加这句话文件上传会报错，不知道为什么
//            multipartFile.getInputStream();
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            //检查工资单是否已经上传
            resultResponse = checkSalary(request, year, month);
            if (!resultResponse.isSuccess()) {
                return resultResponse;
            } else {
                resultResponse = new ResultResponse();
            }



            //对文档解析并校验
            List<Map<String, String>> list = analysisExcel(multipartFile);
            Map map = new HashMap();
            map.put("user",companyMembersBean);
            map.put("excelDataList",list);
            map.put("payday",payday);

            //对文档进行合法性验证
            validateService.validate(map);

            if (!list.isEmpty()) {

                //文件上传至服务器
                flag = AliOss.uploadFile(multipartFile.getInputStream(), newFileName, PropertyUtils.getString("oss.bucketName.file"));
                if (flag) {
                    //保存工资单上传记录

                    saveCompanySalaryExcelBean(request,list, newFileName, fileName, payday, month);

                    resultResponse.setSuccess(true);
                } else {
                    throw new BusinessException("文件上传失败");
                }
            } else {
                throw new BusinessException("文件中没有可用的数据");
            }
            //修改提示文案
//            resultResponse.setMessage("恭喜您上传工资单成功！" +
//                    "<br>您的" + month + "月份工资" +
//                    "<br>我们将于" + payday + "发放" +
//                    "<br>请保持在发薪日前一天账户余额充足");

            resultResponse.setMessage("您好，您要发放的" + month +"月工资单已经上传成功，" +
                    "<br>薪太软将于"+ payday +"帮您发放工资，" +
                    "<br>请保持余额充足。");
            LOGGER.info("文件上传成功···");

            //封装消息实体类
            CompanyMsgsBean companyMsgsBean= packageCompanyMsgsBean(collaborationCompanyBean,payday,month,companysBean);
            //插入发送消息
            int result = companyMsgsService.insert(companyMsgsBean);
            LOGGER.info("当前插入公司消息数据返回值:::::"+result);

            //发送短信
            if(!isStrNull(collaborationCompanyBean.getMemberPhone())) {
                boolean sendPhoneMsgFlg = sendPhoneMsg(collaborationCompanyBean.getMemberPhone(), payday, month);
                LOGGER.info("发送短信消息给" + collaborationCompanyBean.getMemberPhone() + "返回状态码::::::" + sendPhoneMsgFlg);
            }

        } catch (Exception e) {
            LOGGER.error("文件上传异常", e);
            resultResponse.setMessage(e.getMessage());
            LOGGER.info("提交工资单发起合作意向,返回结果:"+ JSON.toJSONString(resultResponse));
            if (flag) {
                //删除文件
                AliOss.deleteFile(PropertyUtils.getString("oss.bucketName.file"), newFileName);
            }
        }
        return resultResponse;
    }


    /**
     * 用于发送消息
     * @param memberPhone
     * @param payday
     * @param month
     * @return
     */
    private boolean sendPhoneMsg(String memberPhone, String payday, String month) throws Exception {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(payday);
        payday=simpleDateFormat.format(date);
        String content="【薪太软】您的"+month+"月工资单已上传成功，薪太软将于"+payday+"帮您发放工资。";
        SendMsgResponse sendMsgResponse = sendMsgService.sendMsg(memberPhone, content);
        return  sendMsgResponse.isSuccess();
    }

    /**
     * 封装消息实体
     * @param collaborationCompanyBean
     * @param payday
     * @param month @throws Exception
     * @param companysBean
     */
    private CompanyMsgsBean packageCompanyMsgsBean(CompanyMembersBean collaborationCompanyBean, String payday, String month, CompanysBean companysBean) throws Exception{
        CompanyMsgsBean companyMsgsBean = new CompanyMsgsBean();
        String msgTitle="上传工资单成功提醒";
        String msgCont ="您好，您要发放的"+ month +"月工资单已经上传成功，薪太软将于"+ payday +"帮您发放工资，请保持余额充足。";
        //set值
        companyMsgsBean.setMsgTitle(msgTitle);//标题
        companyMsgsBean.setMsgCont(msgCont);//消息内容
        companyMsgsBean.setMsgType(2);// 2为消息提醒类型
        companyMsgsBean.setMsgAddtime(new Date());//消息生成时间
        companyMsgsBean.setMsgCompanyId(collaborationCompanyBean.getMemberCompanyId());//企业id
        companyMsgsBean.setMsgCompanyName(companysBean.getCompanyName());//企业名称
        companyMsgsBean.setMsgDepId(collaborationCompanyBean.getMemberDepId());//long 类型
        companyMsgsBean.setMsgFromCompanyId(0l);//long  类型
        companyMsgsBean.setMsgSign(1);//状态  1：未读   2：已读  0：删除
        companyMsgsBean.setMsgClass(1);//1：收件箱  2：发件箱
        return companyMsgsBean;
    }

    /**
     * 根据月份检查工资单是否已经上传
     *
     * @param month
     */
    private ResultResponse checkSalary(HttpServletRequest request, String year, String month) {
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setSuccess(true);
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        CompanySalaryExcelBean companySalaryExcelBean = companySalaryExcelService.selectSalaryByMonth(companyMembersBean.getMemberCompanyId(), Integer.valueOf(month), Integer.valueOf(year));
        if (companySalaryExcelBean != null) {
            resultResponse.setSuccess(false);
            //0=待处理，1=发放中，2=已发放，3=挂起，4=仅下发工资单，5=撤销
            if (GrantStateConstant.GRANT_STATE_0.intValue() == companySalaryExcelBean.getExcelGrantState().intValue()) {
                resultResponse.setMsgCode("0000");
                resultResponse.setMessage("你选择的发薪月份已经上传过工资单，但并未实际发薪。" +
                        "重复提交将会替换原先的工资单，原工资单会被撤销。");
            } else if (GrantStateConstant.GRANT_STATE_1.intValue() == companySalaryExcelBean.getExcelGrantState().intValue()) {
                resultResponse.setMsgCode("0001");
                resultResponse.setMessage("你选择的发薪月份已经上传过工资单，工资正在发放中。" +
                        "重复提交将不能上传。");
            } else if (GrantStateConstant.GRANT_STATE_2.intValue() == companySalaryExcelBean.getExcelGrantState().intValue()) {
                resultResponse.setMsgCode("0002");
                resultResponse.setMessage("你选择发薪月份已经上传，重复提交将不能上传。");
            } else if (GrantStateConstant.GRANT_STATE_4.intValue() == companySalaryExcelBean.getExcelGrantState().intValue()) {
                resultResponse.setMsgCode("0004");
                resultResponse.setMessage("你选择的发薪月份已经上传过工资单，并已经发送工资单给员工" +
                        "重复提交将不能上传。");
            }
        }
        return resultResponse;
    }

    /**
     * 保存工资单上传记录
     *
     * @param list
     * @param newFileName
     * @param fileName
     * @param payday
     * @throws BusinessException
     */
    private void saveCompanySalaryExcelBean(HttpServletRequest request,List<Map<String, String>> list, String newFileName, String fileName, String payday, String month) throws BusinessException {
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        //工资单
        CompanySalaryExcelBean companySalaryExcelBean = new CompanySalaryExcelBean();
        //年份
        companySalaryExcelBean.setExcelYear(Integer.parseInt(payday.substring(0, 4)));
        //月份
        companySalaryExcelBean.setExcelMonth(Integer.parseInt(month));
        //总成本
        companySalaryExcelBean.setExcelCostTotal(getTotleCost(list));
        //工资发放状态 0=待处理，1=发放中，2=已发放，3=挂起，4=仅下发工资单，5=撤销
        companySalaryExcelBean.setExcelGrantState(GrantStateConstant.GRANT_STATE_0);
        //发薪人数
        companySalaryExcelBean.setExcelPayrollNumber(list.size());
        //企业Id  //
        companySalaryExcelBean.setExcelCompanyId(companyMembersBean.getMemberCompanyId());
        //文件名
        companySalaryExcelBean.setExcelName(fileName);
        //文件路径
        companySalaryExcelBean.setExcelPath(newFileName);
        //文件组名
//        companySalaryExcelBean.setExcelGroupName(fastDFSResult.group_name);
        //发薪日
        companySalaryExcelBean.setExcelPayday(DateUtil.stringToDate(payday, DateUtil.DATEYEARFORMATTER));
        //添加人
        companySalaryExcelBean.setExcelAddmember(companyMembersBean.getMemberId());
        //新增企业上传工资单记录
        Long excelId = companySalaryExcelService.insert(companySalaryExcelBean);

        // 发工资
        companyRechargesService.sendSalary(companySalaryExcelBean.getExcelCostTotal(), companyMembersBean.getMemberCompanyId(), excelId);

    }

    /**
     * 校验上传的Excel文档是否符合规范
     *
     * @param multipartFile
     * @return
     * @throws Exception
     */
    private List<Map<String, String>> analysisExcel(MultipartFile multipartFile) throws Exception {
        LOGGER.info("开始解析文件" + multipartFile.getOriginalFilename());
        //文件读取开始行
        int startLine = Integer.valueOf(PropertyUtils.getString("company.readLine"));
        //文件读取页签
        int sheetNum = Integer.valueOf(PropertyUtils.getString("company.sheetNum"));
        /**解析Excel*/
        List<Object[]> list = ExcelUtil.getDataFromExcel(multipartFile, startLine, sheetNum);
        return ExcelUtil.analyzeData(list, startLine,52);
    }


    /**
     * 获取总工资
     *
     * @param list
     */
    private BigDecimal getTotleCost(List<Map<String, String>> list) {
        if (!list.isEmpty()) {
            BigDecimal totleCost = new BigDecimal(0);
            for (Map<String, String> map : list) {
                totleCost = totleCost.add(new BigDecimal(map.get("F")));
            }
            return totleCost;
        }
        return new BigDecimal(0);
    }

    /**
     * 撤销工资单
     *
     * @param excelId
     * @return
     */
    @RequestMapping("cancelSalaryExcel.htm")
    @ResponseBody
    public ResultResponse cancelSalaryExcel(@RequestParam("excelId") Long excelId, HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            companySalaryExcelService.deleteByPrimaryKey(excelId);
            //更新收支记录工资状态
            companyRechargesService.updateRechargeState(companyMembersBean.getMemberCompanyId(), excelId, GrantStateConstant.GRANT_STATE_5);
            resultResponse.setSuccess(true);
        } catch (BusinessException e) {
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        return resultResponse;
    }
}
