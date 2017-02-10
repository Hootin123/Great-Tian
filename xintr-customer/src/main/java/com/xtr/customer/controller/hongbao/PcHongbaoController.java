package com.xtr.customer.controller.hongbao;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.dto.hongbao.NewHongbaoDto;
import com.xtr.api.service.company.CompanyMembersService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.h5wallet.H5walletService;
import com.xtr.api.service.h5wallet.HongbaoService;
import com.xtr.comm.constant.CommonConstants;
import com.xtr.comm.constant.Constant;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.comm.util.StringUtils;
import com.xtr.comm.verify.VerifyCodeUtils;
import com.xtr.customer.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * pc端红包页面
 * @author:zhangshuai
 * @date: 2016/10/11.
 */
@Controller
@RequestMapping("web")
public class PcHongbaoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PcHongbaoController.class);

    @Resource
    private HongbaoService hongbaoService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CompanysService companysService;

    @Resource
    private CompanyMembersService companyMembersService;

    @Resource
    private H5walletService h5walletService;

    /**
     * 跳转至详情页面
     * @param modelAndView
     * @return
     */
    @RequestMapping(value="jumpToWebActivePage.htm")
    public ModelAndView jumpToWebActivePage(ModelAndView modelAndView,String companyContactPlace){
        modelAndView.addObject("companyContactPlace",companyContactPlace);
        modelAndView.setViewName("xtr/PChongbao/webActive");
        return modelAndView;
    }

    /**
     * 跳转至信息收集页面
     * @return
     */
    @RequestMapping(value="jumpToWebCollectionInfoPage.htm")
    public ModelAndView  jumpToWebCollectionInfoPage(ModelAndView modelAndView,Long companyId){
     modelAndView.setViewName("xtr/PChongbao/webCollectionInfo");
     modelAndView.addObject("companyId",companyId);
     return modelAndView;
    }


    /**
     * 跳转至登录页面
     * @return
     */
    @RequestMapping(value="jumpToLoginPage.htm")
    public ModelAndView jumpToLoginPage(ModelAndView modelAndView){
        modelAndView.setViewName("xtr/PChongbao/webLogin");
        return modelAndView;
    }

    /**
     * 跳转至注册页面
     * @return
     */
    @RequestMapping(value="jumpToRegisterPage.htm")
    public ModelAndView jumpToRegisterPage(ModelAndView modelAndView,String companyContactPlace){
        modelAndView.setViewName("xtr/PChongbao/webRegister");
        modelAndView.addObject("companyContactPlace",companyContactPlace);
        return modelAndView;
    }

    /**
     * 审核未通过
     * @return
     */
    @RequestMapping(value="jumpToPromptDefeat.htm")
    public ModelAndView jumpToPromptDefeat(ModelAndView modelAndView,HttpServletRequest request) throws UnsupportedEncodingException {
        modelAndView.setViewName("xtr/PChongbao/webPromptDefeat");
        String reason = request.getParameter("reason");
        String companyId = request.getParameter("companyId");
        modelAndView.addObject("reason",new String(reason.getBytes("iso8859-1"),"utf-8"));//失败原因
        modelAndView.addObject("companyId",companyId);//公司Id
       return modelAndView;
    }

    /**
     * 审核通过
     * @return
     */
    @RequestMapping(value="jumpToPromptSuccess.htm")
    public ModelAndView jumpToPromptSuccess(ModelAndView modelAndView){
        modelAndView.setViewName("xtr/PChongbao/webPromptSuccess");
        return modelAndView;
    }

    /**
     * 等待审核
     * @return
     */
    @RequestMapping(value="jumpToPromptWait.htm")
    public ModelAndView jumpToPromptWait(ModelAndView modelAndView){
        modelAndView.setViewName("xtr/PChongbao/webPromptWait");
        return modelAndView;
    }

    /**
     * 登录
     * @return
     */
    @RequestMapping(value="webLogin.htm")
    @ResponseBody
    public ResultResponse webLogin(HttpServletRequest request, NewHongbaoDto hongbaoDto ){

        ResultResponse resultResponse = new ResultResponse();
        try{
            String key = request.getSession().getId() + Constant.VCOD_EIMG;
            hongbaoDto.setKey(key);
            resultResponse = hongbaoService.activityLogin(hongbaoDto);
            if(resultResponse.isSuccess()){
                CompanyMembersBean resultBean=(CompanyMembersBean)resultResponse.getData();
                //根据企业ID获取企业信息放入session中
                CompanysBean companyBean=companysService.selectCompanyByCompanyId(resultBean.getMemberCompanyId());
                request.getSession().setAttribute(CommonConstants.LOGIN_USER_KEY, resultBean);
                request.getSession().setAttribute(CommonConstants.SESSION_USER, resultBean);
                request.getSession().setAttribute(CommonConstants.LOGIN_COMPANY_KEY, companyBean);
                // resultResponse.setData(resultBean.getMemberCompanyId());
                Map map = new HashMap();
                map.put("companyId",resultBean.getMemberCompanyId());//公司id
                //判断是否完善信息
                if(StringUtils.isStrNull(companyBean.getCompanyName())||StringUtils.isStrNull(companyBean.getCompanyAddress())
                        ||StringUtils.isStrNull(companyBean.getCompanyNumber())||StringUtils.isStrNull(companyBean.getCompanyOrganizationImg())
                        ||StringUtils.isStrNull(companyBean.getCompanyCorporation())||StringUtils.isStrNull(companyBean.getCompanyCorporationPhone())||
                        StringUtils.isEmpty(companyBean.getCompanyAuditStatus())){
                    map.put("returnStr","perfectInfo");
                }else{
                    //判断是否收集过资料
                    if(StringUtils.isEmpty(companyBean.getCompanyIsCollectInfo())||companyBean.getCompanyIsCollectInfo()!=1){
                        //收集资料页面标识
                        map.put("returnStr","collectInfo");
                    }else{
                        //判断审核状态
                        map.put("returnStr",companyBean.getCompanyAuditStatus());
                        if(!StringUtils.isEmpty(companyBean.getCompanyAuditStatus())){
                            if(companyBean.getCompanyAuditStatus()==1)
                                map.put("reason",StringUtils.isStrNull(companyBean.getCompanyDatumDismissreason())==true?"":companyBean.getCompanyDatumDismissreason());

                        }
                    }
                }
                resultResponse.setData(map);
            }
        }catch (Exception e){
            LOGGER.error("登录出现异常错误："+e.getMessage());
            resultResponse.setMessage(e.getMessage());
        }
    return resultResponse;
    }

    /**
     * 注册
     * @param request
     * @param hongbaoDto
     * @return
     */
    @RequestMapping(value="webRegister.htm")
    @ResponseBody
    public ResultResponse webRegister(HttpServletRequest request,NewHongbaoDto hongbaoDto){
        ResultResponse resultResponse = new ResultResponse();
        try{
            resultResponse = hongbaoService.activityRegister(hongbaoDto,hongbaoDto.getCompanyContactPlace());
        }catch (Exception e){
           LOGGER.error("pc端红包注册出现异常错误："+e.getMessage());
        }
        return resultResponse;
    }


    /**
     * 判断手机号是否已注册
     * @return
     */
    @RequestMapping(value="judgePhoneNumber.htm")
    @ResponseBody
    public ResultResponse judgePhoneNumber(HttpServletRequest request){
        ResultResponse resultResponse = new ResultResponse();
        try{
            String phone = request.getParameter("phoneNumber");
            LOGGER.info("判断手机号是否已被注册："+phone);
            CompanyMembersBean checkCompanyBean=new CompanyMembersBean();
            checkCompanyBean.setMemberPhone(phone);
            List<CompanyMembersBean> hasCom = companyMembersService.selectByPhoneOrLogname(checkCompanyBean);
            if(hasCom!=null&&hasCom.size()>0){
                resultResponse.setMessage("手机号已被注册");
            }else{
                //发送短信验证码
                resultResponse= hongbaoService.sendPhoneMsgByH5(phone);
            }
        }catch (Exception e){
            LOGGER.error("判断手机号是否已注册出现错误："+e.getMessage());
            resultResponse.setMessage(e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 生成图形验证码
     * @param request
     * @param response
     */
    @RequestMapping(value="getImgCode.htm")
    public void getImgCode(HttpServletRequest request, HttpServletResponse response){
        try{
            String key = request.getSession().getId()+ Constant.VCOD_EIMG;
            //生成验证码
            //h5walletService.generateH5ValidatorImages(key,response);
            int w = 100, h = 40;
            //生成验证码图片并放入response中
            String verifyCode = VerifyCodeUtils.outputVerifyImage(w,h,response.getOutputStream(),4);
            LOGGER.info("新版红包登录生成验证码:"+verifyCode);
            //将验证码存入缓存
            ValueOperations<String,Object> valueOperations=redisTemplate.opsForValue();
            valueOperations.set(key,verifyCode);
        }catch(IOException e){
            LOGGER.error(e.getMessage());
        }catch(Exception e){
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 跳转至资料审核页面
     * @param modelAndView
     * @return
     */
    @RequestMapping(value="jumpToCompanyInfoPage.htm")
    public ModelAndView jumpToActivityApprove(ModelAndView modelAndView,HttpServletRequest request){
        modelAndView.setViewName("xtr/PChongbao/webCompanyInfo");
        Long companyId = Long.valueOf(request.getParameter("companyId"));
        LOGGER.info("跳转 至审核页面参数companyId："+companyId);
        if(null==companyId){
            LOGGER.error("公司id为空值");
        }
        CompanysBean companysBean = companysService.selectCompanyByCompanyId(companyId);
        //获取修改页面的标识
        if(!StringUtils.isStrNull(request.getParameter("editFlag"))){

            modelAndView.addObject("companyAddress",companysBean.getCompanyAddress());//地址
            modelAndView.addObject("companyNumber",companysBean.getCompanyNumber());//营业执照编号
            modelAndView.addObject("companyName",companysBean.getCompanyName());
            //公司logo
            if(StringUtils.isStrNull(companysBean.getCompanyLogo())){
                modelAndView.addObject("companyLogo","");
            }else{
                modelAndView.addObject("companyLogo",companysBean.getCompanyLogo());
                modelAndView.addObject("companyLogoUrl","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+companysBean.getCompanyLogo());
            }
            if(StringUtils.isStrNull(companysBean.getCompanyOrganizationImg())){
                modelAndView.addObject("companyOrganizationImg","");
            }else{
                modelAndView.addObject("companyOrganizationImg",companysBean.getCompanyOrganizationImg());
                modelAndView.addObject("companyOrganizationImgUrl","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+companysBean.getCompanyOrganizationImg());
            }
            modelAndView.addObject("companyCorporation",companysBean.getCompanyCorporation());
            modelAndView.addObject("companyCorporationPhone",companysBean.getCompanyCorporationPhone());
            modelAndView.addObject("companyId",companyId);

            //显示
            modelAndView.addObject("companyScale",companysBean.getCompanyScale());//公司规模
            modelAndView.addObject("companyBelongIndustry",companysBean.getCompanyBelongIndustry());//公司行业
            modelAndView.addObject("companyChannel",companysBean.getCompanyChannel());
            String companyChannelOther="";
            if(!StringUtils.isEmpty(companysBean.getCompanyChannel())){
                if(companysBean.getCompanyChannel()==1){
                    companyChannelOther="百度360，等搜索引擎";
                }else if(companysBean.getCompanyChannel()==2){
                    companyChannelOther="微信，QQ等社交平台";
                }else if(companysBean.getCompanyChannel()==3){
                    companyChannelOther="推销";
                }else if(companysBean.getCompanyChannel()==4){
                    companyChannelOther="广告";
                }else if(companysBean.getCompanyChannel()==5){
                    companyChannelOther="朋友介绍";
                }else if(companysBean.getCompanyChannel()==6){
                    companyChannelOther="其他";
                }
            }

          modelAndView.addObject("companyChannelOther",companyChannelOther);
        }else{
            modelAndView.addObject("companyLogo","");
            modelAndView.addObject("companyOrganizationImg","");
            modelAndView.addObject("companyName",companysBean.getCompanyName());
            modelAndView.addObject("companyId",companyId);

        }

        return modelAndView;
    }


    /**
     * 资料审核提交
     * @param request
     * @param companysBean
     * @return
     */
    @RequestMapping(value="activityApprove.htm")
    @ResponseBody
    public ResultResponse activityApprove(HttpServletRequest request, CompanysBean companysBean){
        ResultResponse resultResponse = new ResultResponse();
        CompanysBean companys = SessionUtils.getCompany(request);
        Long companyId =null;
        if(null==companys)
            companyId = companysBean.getCompanyId();
        else
            companyId =companys.getCompanyId();

        CompanysBean com = companysService.selectCompanyByCompanyId(companyId);
        try{
            companysBean.setCompanyId(companyId);//公司id
            resultResponse = hongbaoService.activityApprove(companysBean);
            Map map = new HashMap();
            map.put("companyId",companyId);
            map.put("isCollect",com.getCompanyIsCollectInfo());
            resultResponse.setData(map);
            //提交资料判断是否已经收集过资料
        }catch (Exception e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error("资料完善出现异常错误："+e.getMessage());
        }
        return resultResponse;
    }


    /**
     * H5上传营业执照及上传公司logo
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping("uploadFile.htm")
    @ResponseBody
    public String h5UploadOrganizeImg(@RequestParam("file") MultipartFile multipartFile, HttpServletResponse response) {
        LOGGER.info("请求上传接口。。。。。");
        response.setContentType("text/html; charset=utf-8");
        ResultResponse resultResponse = new ResultResponse();
        try {
            if(multipartFile!=null){
                String fileName = multipartFile.getOriginalFilename();
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                String[] filenames={"jpg", "png", "jpeg", "bmp"};
                String newFileName = UUID.randomUUID().toString() + "." + suffix;
                boolean checkFlag = false;//上传文件类型是否符合规范,false代表不符合
                for (String filenameCheck : filenames) {
                    if(filenameCheck.equalsIgnoreCase(suffix)) {
                        checkFlag = true;
                        break;
                    }
                }
                if(!checkFlag) {
                    resultResponse.setSuccess(false);
                    resultResponse.setMessage("文件后缀名错误");
                    return JSON.toJSONString(resultResponse);
                }
                //上传文件是否大于5M
                if(multipartFile.getSize()>(5*1024*1024)){
                    resultResponse.setMessage("上传文件大小不能超过5M");
                    resultResponse.setSuccess(false);
                    return JSON.toJSONString(resultResponse);
                }

                boolean result= AliOss.uploadFile(multipartFile.getInputStream(), newFileName, PropertyUtils.getString("oss.bucketName.img"));
                if(result){
                    resultResponse.setSuccess(true);
                    resultResponse.setData(newFileName);
                }else{
                    LOGGER.error("上传营业执照图片,上传文件失败");
                    resultResponse.setMessage("上传文件失败");
                }
            }else{
                LOGGER.error("上传营业执照图片,没有上传文件");
                resultResponse.setMessage("没有上传文件");
            }
        }catch (IOException e){
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("上传营业执照图片,返回结果："+ JSON.toJSONString(resultResponse));
        return JSON.toJSONString(resultResponse);
    }

    /**
     * 信息收集提交
     * @param request
     * @return
     */
    @RequestMapping(value="infoCollectSubmit.htm")
    @ResponseBody
    public ResultResponse infoCollectSubmit(HttpServletRequest request,NewHongbaoDto newHongbaoDto){

        Long companyId = newHongbaoDto.getCompanyId();
        CompanysBean companysBean = companysService.selectCompanyByCompanyId(companyId);
        ResultResponse resultResponse = new ResultResponse();
        try{
            resultResponse = hongbaoService.infoCollectSubmit(newHongbaoDto);
            newHongbaoDto = (NewHongbaoDto) resultResponse.getData();
            Map map = new HashMap();
            map.put("newHongbaoDto",newHongbaoDto);
            if(companysBean.getCompanyAuditStatus()==null||companysBean.getCompanyAuditStatus()==0){
                //待审核
                map.put("perInfo","wait");
            }else if(companysBean.getCompanyAuditStatus()==1){
                //审核未通过
                map.put("perInfo","noPass");
                map.put("reason",companysBean.getCompanyDatumDismissreason());
                map.put("companyId",companysBean.getCompanyId());
            }else if(companysBean.getCompanyAuditStatus()==2){
                //审核通过
                map.put("perInfo","pass");
            }
            resultResponse.setData(map);
        }catch (Exception e){
            LOGGER.error("信息收集提交出现错误："+e.getMessage());
        }
        return resultResponse;
    }

}
