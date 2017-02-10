package com.xtr.company.controller.salary;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.BaseController;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.salary.RapidlyPayrollBean;
import com.xtr.api.domain.salary.RapidlyPayrollExcelBean;
import com.xtr.api.service.salary.PayrollAccountService;
import com.xtr.api.service.salary.RapidlyPayrollExcelService;
import com.xtr.api.service.salary.RapidlyPayrollService;
import com.xtr.api.service.validate.ValidateService;
import com.xtr.api.util.ExcelUtil;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CompanyConstant;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.company.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
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
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>急速发工资</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/10/27 17:54
 */
@Controller
@RequestMapping("rapidlyPayroll")
public class RapidlyPayrollController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RapidlyPayrollController.class);

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RapidlyPayrollExcelService rapidlyPayrollExcelService;

    @Resource
    private RapidlyPayrollService rapidlyPayrollService;

    @Resource
    private PayrollAccountService payrollAccountService;

    /**
     * 数据合法性验证
     */
    @Resource(name = "rapidlyPayrollValidate")
    private ValidateService validateService;

    /**
     * 急速发工资文件上传界面
     *
     * @param mav
     * @return
     */
    @RequestMapping("rapidlyPayrollExcel.htm")
    public ModelAndView payrollExcel(ModelAndView mav) {
        mav.addObject("BatchRapidlyTemplate",PropertyUtils.getString("company.download.rapidly.path"));
        mav.setViewName("xtr/salary/rapidlyPayrollExcel");
        return mav;
    }

    /**
     * 急速发工资预览界面
     *
     * @param mav
     * @param excelPath
     * @return
     */
    @RequestMapping("rapidlyPayroll.htm")
    public ModelAndView payroll(ModelAndView mav, @RequestParam(value = "excelPath") String excelPath) {
        ResultResponse resultResponse = new ResultResponse();
        RapidlyPayrollExcelBean rapidlyPayrollExcelBean = (RapidlyPayrollExcelBean) redisTemplate.opsForValue().get(excelPath);
        if (rapidlyPayrollExcelBean != null) {
            //创建分页
            resultResponse.setPaginator(new Paginator(0, 10, rapidlyPayrollExcelBean.getPayrollList().size()));
            resultResponse.setData(rapidlyPayrollExcelBean);
        }
        mav.addObject("resultResponse", JSON.toJSONString(resultResponse));
        mav.setViewName("xtr/salary/rapidlyPayroll");
        return mav;
    }

    /**
     * 文件上传
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @RequestMapping("upload.htm")
    @ResponseBody
    public ResultResponse upload(HttpServletRequest request, @RequestParam("file") MultipartFile multipartFile) throws Exception {
        ResultResponse resultResponse = new ResultResponse();
        LOGGER.info("文件上传开始```");
        CompanyMembersBean companyBean = SessionUtils.getUser(request);
        boolean flag = false;
        //真实的文件名
        String fileName = multipartFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String newFileName = UUID.randomUUID().toString() + "." + suffix;
        try {
            List<Map<String, String>> list = analysisExcel(multipartFile);
            //数据合法性验证
            validateService.validate(list);
            if (!list.isEmpty()) {
                //文件上传至服务器
                flag = AliOss.uploadFile(multipartFile.getInputStream(), newFileName, PropertyUtils.getString("oss.bucketName.file"));
                if (flag) {
                    RapidlyPayrollExcelBean rapidlyPayrollExcelBean = transformationData(companyBean.getMemberCompanyId(), fileName, newFileName, list);
                    //创建人
                    rapidlyPayrollExcelBean.setCreateUser(companyBean.getMemberId());
                    rapidlyPayrollExcelBean = rapidlyPayrollExcelService.insert(rapidlyPayrollExcelBean);
                    //将文件数据放入缓存 --只保存30分钟
                    redisTemplate.opsForValue().set(newFileName, rapidlyPayrollExcelBean, 60 * 60, TimeUnit.SECONDS);
                    //数据转换
                    resultResponse.setData(newFileName);
                    resultResponse.setSuccess(true);
                } else {
                    throw new BusinessException("文件上传失败");
                }
            } else {
                throw new BusinessException("文件中没有可用的数据");
            }
            resultResponse.setMessage("您的工资单已上传成功");
            resultResponse.setSuccess(true);
        } catch (Exception e) {
            LOGGER.error("文件上传异常", e);
            resultResponse.setMessage(e.getMessage());
            if (flag) {
                //删除文件
                AliOss.deleteFile(PropertyUtils.getString("oss.bucketName.file"), newFileName);
            }
        }
        return resultResponse;
    }

    /**
     * 对文档进行解析
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
        return ExcelUtil.analyzeData(list, startLine, 5);
    }

    /**
     * 数据转换
     *
     * @param fileName
     * @param newFileName
     * @param list
     * @return
     */
    private RapidlyPayrollExcelBean transformationData(Long companyId, String fileName, String newFileName, List<Map<String, String>> list) {
        RapidlyPayrollExcelBean rapidlyPayrollExcelBean = new RapidlyPayrollExcelBean();
        //工资总额
        BigDecimal totalWages = new BigDecimal(0);
        List<RapidlyPayrollBean> payrollBeanList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            RapidlyPayrollBean rapidlyPayrollBean = new RapidlyPayrollBean();
            Map<String, String> map = list.get(i);
            //姓名
            rapidlyPayrollBean.setCustomerName(map.get("A"));
            //身份证号
            rapidlyPayrollBean.setIdCard(map.get("B"));
            //工资卡开户行
            rapidlyPayrollBean.setBankAccount(map.get("C"));
            //工资卡号
            rapidlyPayrollBean.setBankNumber(map.get("D"));
            //实发金额(元)
            rapidlyPayrollBean.setRealWage(map.get("E") == null ? new BigDecimal(0) : new BigDecimal(map.get("E")));
            totalWages = totalWages.add(rapidlyPayrollBean.getRealWage());
            payrollBeanList.add(rapidlyPayrollBean);
        }
        //公司主键
        rapidlyPayrollExcelBean.setCompanyId(companyId);
        //文档名称
        rapidlyPayrollExcelBean.setExcelName(fileName);
        //文档标题
        rapidlyPayrollExcelBean.setExcelTitle(fileName.substring(0,fileName.lastIndexOf(".")));
        //excel路径
        rapidlyPayrollExcelBean.setExcelPath(newFileName);
        //文件组名称
        rapidlyPayrollExcelBean.setExcelGroupName(PropertyUtils.getString("oss.bucketName.file"));
        //工资总额
        rapidlyPayrollExcelBean.setTotalWages(totalWages);
        //发薪人数
        rapidlyPayrollExcelBean.setPayrollNumber(list.size());
        //工资发放状态 0:未发放 1:待发放  2:发放中  3:已发放
        rapidlyPayrollExcelBean.setGrantState(0);
        //创建时间
        rapidlyPayrollExcelBean.setCreateTime(new Date());
        //年度
        Calendar c = Calendar.getInstance();
        c.setTime(rapidlyPayrollExcelBean.getCreateTime());
        rapidlyPayrollExcelBean.setYear(c.get(Calendar.YEAR));
        //是否生成工资单 0:未生成 1:已生成
        rapidlyPayrollExcelBean.setIsGeneratePayroll(0);
        rapidlyPayrollExcelBean.setPayrollList(payrollBeanList);
        return rapidlyPayrollExcelBean;
    }

    /**
     * 发工资
     *
     * @return
     */
    @RequestMapping("payOff.htm")
    @ResponseBody
    public ResultResponse payOff(HttpServletRequest request, @RequestParam("excelPath") String excelPath,
                                 @RequestParam("excelTitle")String excelTitle) {
        ResultResponse resultResponse = new ResultResponse();
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        try {
            if (excelPath != null) {
                RapidlyPayrollExcelBean rapidlyPayrollExcelBean = (RapidlyPayrollExcelBean) redisTemplate.opsForValue().get(excelPath);
                //设置标题
                rapidlyPayrollExcelBean.setExcelTitle(excelTitle);
                //生成工资单并生成充值订单
                rapidlyPayrollService.payOff(rapidlyPayrollExcelBean);
                //判断当前登录用户是否有访问账户权限
                if (payrollAccountService.validateCustomerRole(companyMembersBean)) {//有访问权限
                    resultResponse.setData(CompanyConstant.COMPANYMEMBER_VISITMENU_YES);
                } else {//无访问权限
                    resultResponse.setData(CompanyConstant.COMPANYMEMBER_VISITMENU_NO);
                }
                resultResponse.setSuccess(true);
            } else {
                resultResponse.setMessage("文件路径不能为空");
            }
        } catch (BusinessException e) {
            LOGGER.error(e.getMessage(),e);
            resultResponse.setMessage(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            resultResponse.setMessage("工资发放失败,请刷新界面重试");
        }
        return resultResponse;
    }

}
