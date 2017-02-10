package com.xtr.company.controller.payday;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.BaseController;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyDepsBean;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.service.company.CompanyDepsService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.validate.ValidateService;
import com.xtr.api.util.ExcelUtil;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CommonConstants;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.HtmlUtil;
import com.xtr.comm.util.MethodUtil;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.company.util.SessionUtils;
import com.xtr.company.web.IExcelView;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/24 8:57
 */
@Controller
public class CustomersController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomersController.class);

    @Resource
    private IExcelView excelView;

    @Resource
    private CustomersService customersService;

    @Resource
    private CompanyDepsService companyDepsService;

    @Resource
    private CompanysService companysService;

    @Resource(name = "validateCustomer")
    private ValidateService validateService;

    /**
     * 新增企业员工信息
     *
     * @param mav
     * @return
     */
    @RequestMapping("addMembers.htm")
    private ModelAndView addMenbers(ModelAndView mav) {
        mav.setViewName("xtr/payday/addmembers");
        mav.addObject("member", new CustomersBean());
        return mav;
    }

    /**
     * 编辑企业员工信息
     *
     * @param mav
     * @return
     */
    @RequestMapping("editMembers.htm")
    private ModelAndView editMenbers(ModelAndView mav, @RequestParam long id) {
        mav.setViewName("xtr/payday/addmembers");
        ResultResponse resultResponse = customersService.selectCustomerInfoDetail(id);
        mav.addObject("member", resultResponse.getData());
        return mav;
    }

    /**
     * 批量导入企业员工信息
     *
     * @param mav
     * @return
     */
    @RequestMapping("batchImport.htm")
    private ModelAndView batchImport(HttpServletRequest request,ModelAndView mav) {
        //组织成员文件上传模板路径
        String customerPath = PropertyUtils.getString("company.download.customer.path");
        mav.addObject("customerPath", customerPath);
        CompanysBean companysBean = SessionUtils.getAttr(request,CommonConstants.LOGIN_COMPANY_KEY);
        mav.addObject("companyName",companysBean.getCompanyName());
        mav.setViewName("xtr/payday/batchImport");
        return mav;
    }

    /**
     * 移动到其他部门页面
     *
     * @param mav
     * @return
     */
    @RequestMapping("moveDeptPage.htm")
    private ModelAndView moveDeptPage(ModelAndView mav, HttpServletRequest request) {
        mav.setViewName("xtr/payday/movedept");
        //根据当前登录用户获取所有部门信息
        try {
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            mav.addObject("companyTree", companyDepsService.getCompanyTree(companyMembersBean.getMemberCompanyId()));
        } catch (Exception e) {
            LOGGER.info(e.getMessage(), e);
        }
        return mav;
    }

    /**
     * 新增企业员工信息
     *
     * @param customersBean
     * @return
     */
    @RequestMapping("addCustomers.htm")
    @ResponseBody
    private void addCustomers(HttpServletResponse response, HttpServletRequest request, CustomersBean customersBean) throws Exception {
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        customersBean.setCustomerCompanyId(companyMembersBean.getMemberCompanyId());
        ResultResponse rr = new ResultResponse();
        if(customersBean.getCustomerId() == null) {
            rr = customersService.insert(customersBean);
        }else{
            int result = customersService.updateByPrimaryKeySelective(customersBean);
            if(result > 0) {
                rr.setSuccess(true);
            }
        }

        HtmlUtil.writerJson(response, rr);
    }


    /**
     * 冻结企业员工账号
     *
     * @param customerIds
     * @return
     */
    @RequestMapping("frozenCustomers.htm")
    @ResponseBody
    private ResultResponse frozenCustomers(@RequestParam("customerIds") Long[] customerIds) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            resultResponse = customersService.frozenCustomers(customerIds);
        } catch (Exception e) {
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 启用企业员工账号
     *
     * @param customerIds
     * @return
     */
    @RequestMapping("enableCustomers.htm")
    @ResponseBody
    private ResultResponse enableCustomers(@RequestParam("customerIds") Long[] customerIds) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            resultResponse = customersService.enableCustomers(customerIds);
        } catch (Exception e) {
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 文件模板下载
     */
//    @RequestMapping("orgmember/download.htm")
//    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
////        String filePath = request.getSession().getServletContext().getRealPath(PropertyUtils.getString("company.download.customer.path"));
//        String filePath = PropertyUtils.getString("company.download.customer.path");
//        excelView.download(filePath, PropertyUtils.getString("company.file.customer.name"), response);
//    }

    /**
     * 文件上传
     *
     * @param mav
     * @param multipartFile
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("customers/upload.htm")
    @ResponseBody
    public ResultResponse upload(ModelAndView mav, @RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) {
        LOGGER.info("文件上传开始```");
        ResultResponse resultResponse = new ResultResponse();
        //真实的文件名
        String fileName = multipartFile.getOriginalFilename();
        try {

            //对文档进行校验
            List<Map<String, String>> list = analysisExcel(multipartFile);
            Map map = new HashMap();
            CompanysBean companysBean = SessionUtils.getAttr(request, CommonConstants.LOGIN_COMPANY_KEY);
            map.put("company", companysBean);
            map.put("excelDataList", list);
            //对文档进行合法性验证
            validateService.validate(map);

            if (!list.isEmpty()) {
                //保存组织与成员上传记录
                saveCustomers(request, list);
                resultResponse.setSuccess(true);
            } else {
                throw new BusinessException("文件中没有可用的数据");
            }
            LOGGER.info("文件上传成功···");
        } catch (BusinessException e) {
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error("文件上传异常", e);
            resultResponse.setMessage(e.getMessage());
        }
        return resultResponse;
    }


    /**
     * 保存组织与成员上传记录
     *
     * @param list
     */
    private void saveCustomers(HttpServletRequest request, List<Map<String, String>> list) throws Exception {
        List<CustomersBean> beanList = new ArrayList<CustomersBean>();
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        for (Map<String, String> map : list) {
            //验证部门是否存在
            ResultResponse resultResponse = companyDepsService.checkDeptByName(companyMembersBean.getMemberCompanyId(), map.get("C"));
            if (resultResponse.isSuccess()) {
                CompanyDepsBean companyDepsBean = (CompanyDepsBean) resultResponse.getData();
                CustomersBean customersBean = new CustomersBean();
                //企业Id
                customersBean.setCustomerCompanyId(companyMembersBean.getMemberCompanyId());
                //企业名称
                customersBean.setCustomerCompanyName(map.get("B"));
                String idCard = map.get("G");
                //部门id
                customersBean.setCustomerDepId(companyDepsBean.getDepId());
                //部门名称
                customersBean.setCustomerDepName(companyDepsBean.getDepName());
                //员工姓名
                customersBean.setCustomerTurename(map.get("D"));
                //性别
                customersBean.setCustomerSex(StringUtils.equals(map.get("E"), "男") ? 1 : 0);
                //职位
                customersBean.setCustomerPlace(map.get("F"));
                //身份证号码
                customersBean.setCustomerIdcard(idCard);
                //联系电话
                customersBean.setCustomerPhone(map.get("H"));
                //短信验证（0.未验证 1.已验证）
                customersBean.setCustomerPhoneIsauthentication(0);
                //开户行
                customersBean.setCustomerBank(map.get("I"));
                //卡号
                customersBean.setCustomerBanknumber(map.get("J"));
                //入职时间
                customersBean.setCustomerJoinTime(DateUtil.string2Date(map.get("K"), DateUtil.DATEYEARFORMATTER));
                //用户名 -- 默认手机号码
                customersBean.setCustomerLogname(customersBean.getCustomerPhone());
                //登录密码 --默认身份证后6位
                int length = idCard.length();
                String password = idCard.substring(length - 6, length);
                customersBean.setCustomerPassword(MethodUtil.MD5(password));
                //是否可用 0不可以 1可用
                customersBean.setCustomerSign(1);
                //是否可发工资
                customersBean.setCustomerIspay(1);
                //注册时间
                customersBean.setCustomerAddtime(new Date());

                beanList.add(customersBean);
            } else {
                throw new BusinessException("部门【" + map.get("C") + "】不存在");
            }
        }
        //新增企业员工账号信息
        if (!beanList.isEmpty()) {
            customersService.saveCustomers(beanList);
        }
    }

    /**
     * 根据企业id，身份证号验证该员工是否已经存在
     *
     * @param customersBean
     */
//    private void checkCustomerIdCard(CustomersBean customersBean) throws BusinessException {
//        CustomersBean bean = new CustomersBean();
//        bean.setCustomerCompanyId(customersBean.getCustomerCompanyId());
//        bean.setCustomerIdcard(customersBean.getCustomerIdcard());
//        List<CustomersBean> list = customersService.getCustomers(customersBean);
//        if (!list.isEmpty()) {
//            throw new BusinessException("身份证号【" + customersBean.getCustomerIdcard() + "】已存在");
//        }
//    }

    /**
     * 校验上传的Excel文档是否符合规范
     *
     * @param multipartFile
     * @return
     * @throws Exception
     */
    private List<Map<String, String>> analysisExcel(MultipartFile multipartFile) throws Exception {
        LOGGER.info("开始解析文件" + multipartFile.getOriginalFilename());

        /**解析Excel*/
        List<Object[]> list = ExcelUtil.getDataFromExcel(multipartFile, 1, 0);
        return ExcelUtil.analyzeData(list, 1,52);
    }


    /**
     * 修改员工所属部门
     *
     * @param customerIds
     * @param deptId
     * @return
     */
    @RequestMapping("moveDept.htm")
    @ResponseBody
    public ResultResponse moveDept(@RequestParam("customerIds") Long[] customerIds,
                                   @RequestParam("deptId") Long deptId) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            customersService.updateDeptId(customerIds, deptId);
            resultResponse.setSuccess(true);
        } catch (BusinessException e) {
            LOGGER.info(e.getMessage(), e);
            resultResponse.setMessage(e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 获取组织成员信息
     *
     * @return
     */
    @RequestMapping("getMembetInfo.htm")
    public ResultResponse getMembetInfo(@RequestParam("deptId") Long deptId) {
        ResultResponse resultResponse = new ResultResponse();
        return resultResponse;
    }

    /**
     * 上传logo页面
     *
     * @return
     */
    @RequestMapping("logo.htm")
    public ModelAndView logohtm() {
        return new ModelAndView("xtr/uploadLogo");
    }

    /**
     * 上传图片
     *
     * @return
     */
    @RequestMapping(value = "uploadLogo.htm")
    @ResponseBody
    public String uploadLogo(@RequestParam MultipartFile file, HttpServletResponse response) {
        response.setContentType("text/html; charset=utf-8");
        ResultResponse resultResponse = new ResultResponse();

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        String fileName = System.currentTimeMillis() + "." + extension;

        String[] filenames={"jpg", "png", "jpeg", "gif"};

        boolean ok = false;
        for (String filename : filenames) {
            if(filename.equalsIgnoreCase(extension)) {
                ok = true;
                break;
            }
        }
        if(!ok) {
            resultResponse.setSuccess(false);
            resultResponse.setMessage("文件后缀名错误");
            return JSON.toJSONString(resultResponse);
        }

        resultResponse.setSuccess(true);
        try {
            AliOss.uploadFile(file.getInputStream(), fileName,PropertyUtils.getString("oss.bucketName.img"));
            resultResponse.setComment(fileName);
        } catch (IOException e) {
            resultResponse.setSuccess(false);
            resultResponse.setMessage(e.getMessage());
        }

        return JSON.toJSONString(resultResponse);
    }

    /**
     * 下载图片
     *
     * @return
     */
    @RequestMapping("downLogo.htm")
    public void downLogo(@RequestParam String logoName, HttpServletResponse response) throws IOException {
        if(StringUtils.isNotEmpty(logoName)) {
            InputStream inputStream = AliOss.downloadFile(PropertyUtils.getString("oss.bucketName.img"), logoName);
            IOUtils.copy(inputStream, response.getOutputStream());
        }
//        return resultResponse;
    }

    /**
     * 下载图片
     *
     * @return
     */
    @RequestMapping("toCutLogo.htm")
    @ResponseBody
    public ResultResponse saveLogo(HttpServletRequest request, @RequestParam String logoName, @RequestParam double scale, @RequestParam double cutX, @RequestParam double cutY, @RequestParam double width, @RequestParam double height) throws IOException {
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        Long memberCompanyId = companyMembersBean.getMemberCompanyId();

        ResultResponse rr = new ResultResponse();
        rr.setSuccess(true);
        try {
            String extension = FilenameUtils.getExtension(logoName);
            InputStream inputStream = AliOss.downloadFile(PropertyUtils.getString("oss.bucketName.img"), logoName);
            BufferedImage image = ImageIO.read(inputStream);

            if(scale != 1.0) {
                int newWidth = (int) (image.getWidth() * scale);
                int newHeight = (int) (image.getHeight() * scale);
                BufferedImage image2 = new BufferedImage(newWidth, newHeight, image.getType());
                image2.getGraphics().drawImage(image, 0, 0, newWidth, newHeight, null);
                image = image2;
            }

            BufferedImage newImage = new BufferedImage((int)width, (int)height, image.getType());
            newImage.getGraphics().drawImage(image, (int)cutX, (int)cutY, null);

            ByteArrayOutputStream aos = new ByteArrayOutputStream();
            ImageIO.write(newImage, extension, aos);

            String cutLogoName = System.currentTimeMillis() + "." + extension;

            AliOss.uploadFile(new ByteArrayInputStream(aos.toByteArray()), cutLogoName,PropertyUtils.getString("oss.bucketName.img"));


            CompanysBean cb = new CompanysBean();
            cb.setCompanyId(memberCompanyId);
            cb.setCompanyLogo(cutLogoName);
            companysService.updateCompanysBeanId(cb);

            SessionUtils.getCompany(request).setCompanyLogo(cutLogoName);

            rr.setComment(cutLogoName);

        }catch (Exception e){
            rr.setSuccess(false);
            rr.setMessage(e.getMessage());
        }
        return rr;
    }
}
