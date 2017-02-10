package com.xtr.core.service.h5wallet;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.basic.SendMsgResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.station.StationSmsRecordsBean;
import com.xtr.api.domain.station.StationSmscontBean;
import com.xtr.api.service.account.RedEnvelopeService;
import com.xtr.api.service.company.CompanyMembersService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.h5wallet.H5walletService;
import com.xtr.api.service.send.SendMsgService;
import com.xtr.api.service.station.StationCollaborationService;
import com.xtr.api.service.station.StationSmsRecordsService;
import com.xtr.api.service.station.StationSmscontService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CompanyConstant;
import com.xtr.comm.constant.Constant;
import com.xtr.comm.constant.StationCollaborationConstants;
import com.xtr.comm.enums.CompanyMeMemberIsdefaultEnum;
import com.xtr.comm.enums.CompanyMeMemberSignEnum;
import com.xtr.comm.enums.ErrorCodeEnum;
import com.xtr.comm.enums.StationSmsRecordsTypeEnum;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.comm.util.RandomDataUtil;
import com.xtr.comm.util.StringUtils;
import com.xtr.comm.verify.VerifyCodeUtils;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/8/8 15:53
 */
@Service("h5walletService")
public class H5walletServiceImpl implements H5walletService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(H5walletServiceImpl.class);

    @Resource
    private CompanyMembersService companyMembersService;

    @Resource
    private CompanysService companysService;

    @Resource
    private RedEnvelopeService redEnvelopeService;

    @Resource
    private StationCollaborationService stationCollaborationService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StationSmscontService stationSmscontService;

    @Resource
    private StationSmsRecordsService stationSmsRecordsService;

    @Resource
    private SendMsgService sendMsgService;

    /**
     * H5注册
     * @param companyMembersBean
     * @param companyName
     * @return
     * @throws BusinessException
     */
    @Transactional
    public ResultResponse register(CompanyMembersBean companyMembersBean, String h5Code, String companyName, String phoneCode, String key,String registerCampaignCode)throws Exception{
        LOGGER.info("H5注册,传递参数:用户信息："+JSON.toJSONString(companyMembersBean)+",验证码："+h5Code+",企业名称："+companyName+",手机验证码："+phoneCode+",验证码对应的KEY："+key + "，活动码：" + registerCampaignCode);
        ResultResponse resultResponse=new ResultResponse();
        if(companyMembersBean!=null && org.apache.commons.lang.StringUtils.isNotBlank(companyMembersBean.getMemberPhone()) && org.apache.commons.lang.StringUtils.isNotBlank(companyMembersBean.getMemberPassword())
                && org.apache.commons.lang.StringUtils.isNotBlank(h5Code) && org.apache.commons.lang.StringUtils.isNotBlank(companyName) && org.apache.commons.lang.StringUtils.isNotBlank(phoneCode) ) {
            //验证验证码是否正确
            if (!checkVcodeimg(h5Code, key)) {//验证码不正确
                LOGGER.info("H5注册用户,验证码不正确");
                resultResponse.setMessage(ErrorCodeEnum.VERIFYCODE_ERROR.getMessage());
                return resultResponse;
            }
            //验证手机验证码是否正确
            Object code = redisTemplate.opsForValue().get(PropertyUtils.getString("mobile.verification.code") + companyMembersBean.getMemberPhone());
            if (code == null || !org.apache.commons.lang.StringUtils.equals(phoneCode.toLowerCase(), String.valueOf(code).toLowerCase())) {
                LOGGER.info("H5注册用户,手机验证码不正确");
                resultResponse.setMessage("手机验证码不正确");
                return resultResponse;
            }
            //验证手机号是否被注册
            CompanyMembersBean checkCompanyBean=new CompanyMembersBean();
            checkCompanyBean.setMemberPhone(companyMembersBean.getMemberPhone());

            List<CompanyMembersBean>  hasCom = companyMembersService.selectByPhoneOrLogname(checkCompanyBean);
            if(null!=hasCom && hasCom.size()>0){
                LOGGER.info("H5注册用户,手机号已经被注册");
                resultResponse.setMessage("手机号已经被注册");
                return resultResponse;
            }

            //插入企业信息
            CompanysBean companysBean = new CompanysBean();
            Date date=new Date();
            companysBean.setCompanyName(companyName);
            companysBean.setCompanyContactTel(companyMembersBean.getMemberPhone());
            companysBean.setCompanyAddime(date);
            companysBean.setCompanyEditime(date);
            if(!StringUtils.isStrNull(registerCampaignCode)){
                companysBean.setCompanyContactPlace(registerCampaignCode);
            }
            Long companyId = companysService.insertByCompanyId(companysBean);
            if(companyId==null || companyId<=0){
                throw new BusinessException("插入企业信息失败");
            }
            //插入注册用户
            companysBean.setCompanyId(companyId);
            companyMembersBean.setMemberCompanyId(companyId);
            companyMembersBean.setMemberLogname(companyMembersBean.getMemberPhone());
            companyMembersBean.setMemberPassword(StringUtils.getMD5two(companyMembersBean.getMemberPassword()));
            companyMembersBean.setCompanyName(companyName);
            companyMembersBean.setMemberIsdefault(CompanyMeMemberIsdefaultEnum.IS_MANAGER.getCode());
            companyMembersBean.setRegisterTime(date);
            companyMembersBean.setMemberLoginRecentTime(date);
            companyMembersBean.setMemberSign(CompanyMeMemberSignEnum.ACTIVE.getCode());
            companyMembersBean.setMemberStatus(1);
            Long memberId = companyMembersService.insertForReturnId(companyMembersBean);
            if(memberId==null || memberId<=0){
                throw new BusinessException("插入注册用户失败");
            }
            // 注册成功发红包
            redEnvelopeService.sendRegisterEnvelope(companyId);
            //新注册用户时,发起合作意向开始
            if(companysBean!=null){
                companysBean.setCompanyCorporationPhone(companyMembersBean.getMemberPhone());
            }
            ResultResponse collaborationResponse=stationCollaborationService.updateOpeationType(companysBean, StationCollaborationConstants.STATIONCOLLABRATION_TYPE_NEWUSER);
            LOGGER.info("H5注册用户,发送合作意向，返回结果:"+ JSON.toJSONString(collaborationResponse));
            if(!collaborationResponse.isSuccess()){
                throw new BusinessException(collaborationResponse.getMessage());
            }

            resultResponse.setSuccess(true);
            resultResponse.setData(companyMembersBean);
        }else{
            LOGGER.info("H5注册用户,传递的参数不能有空值");
            resultResponse.setMessage("传递的参数不能有空值");
            return resultResponse;
        }
        return resultResponse;
    }

    /**
     * 验证验证码详细
     * @param codeParam
     * @param key
     * @return
     */
    private boolean checkVcodeimg(String codeParam,String key){
        ValueOperations<String,Object> fetchCacheOperations=redisTemplate.opsForValue();
        String value =(String)fetchCacheOperations.get(key);
        return codeParam.toLowerCase().equals(value.toLowerCase());
    }

    /**
     * 生成H5验证码图片
     * @param key
     * @param response
     * @throws IOException
     */
    public void generateH5ValidatorImages(String key,HttpServletResponse response)throws IOException {
        int w = 160, h = 34;
        //生成验证码图片并放入response中
        String verifyCode = VerifyCodeUtils.outputVerifyImage(w,h,response.getOutputStream(),4);
        LOGGER.info("H5注册生成验证码:"+verifyCode);
        //将验证码存入缓存
        ValueOperations<String,Object> valueOperations=redisTemplate.opsForValue();
        valueOperations.set(key,verifyCode);
    }

    /**
     * H5发送注册手机验证码
     * @param memberPhone
     * @return
     * @throws IOException
     */
    @Transactional
    public ResultResponse sendPhoneMsgByH5(String memberPhone)throws IOException{
        LOGGER.info("H5发送注册手机验证码,传递参数:"+memberPhone);
        ResultResponse resultResponse=new ResultResponse();
        if (StringUtils.isStrNull(memberPhone)) {
            LOGGER.info("H5发送注册手机验证码,手机号码不能为空");
            resultResponse.setMessage("手机号码不能为空");
            return resultResponse;
        }
//        验证手机号是否是注册用户,是否合法
//        CompanyMembersBean checkCompanyBean=new CompanyMembersBean();
//        checkCompanyBean.setMemberPhone(memberPhone);
//        List<CompanyMembersBean> companyMemberList=companyMembersService.selectByPhone(checkCompanyBean);
//        if (companyMemberList==null || companyMemberList.size()<=0) {
//            LOGGER.info("H5发送注册手机验证码,手机号不正确");
//            resultResponse.setMessage("手机号不正确");
//            return resultResponse;
//        }
        //短信模板
        StationSmscontBean stationSmscontBean = stationSmscontService.selectBySmsType(StationCollaborationConstants.STATIONSMSCONT_TYPE_FIRST);
        if (stationSmscontBean == null) {
            LOGGER.info("H5发送注册手机验证码,获取不到短信模板");
            resultResponse.setMessage(ErrorCodeEnum.SMS_TEMPLATE_ERROR.getMessage());
            return resultResponse;
        }
        //该手机号当日已发验证短信数
        long smsNum = stationSmsRecordsService.getCountByCondition(memberPhone, DateUtil.formatCurrentDate());

        if (smsNum + 1 > Constant.SMS_LIMIT) {
            LOGGER.info("H5发送注册手机验证码,超过当天发信息上限");
            resultResponse.setMessage(ErrorCodeEnum.SMS_OVER_LIMIT.getMessage());
            return resultResponse;
        }
        //生成手机验证码
        String verificationCode = RandomDataUtil.getRandomID(6);
        String paraSmsCode = PropertyUtils.getString("environ.istest");    //短信验证码生成环境，如果是test测试环境，永远是111111
        if ("1".equals(paraSmsCode)){
            verificationCode= "111111";
        }

        LOGGER.info("H5发送注册手机验证码：" + verificationCode);
        //短信内容,用随机数替代验证码
        String smsCont = stationSmscontBean.getSmsCont() == null ? "" : stationSmscontBean.getSmsCont();
        smsCont = smsCont.replace("[变量1]", verificationCode);
        SendMsgResponse sendMsgResponse = sendMsgService.sendMsg(memberPhone, smsCont);
        LOGGER.info("H5发送注册手机验证码,返回结果:"+ JSON.toJSONString(sendMsgResponse));
        if (!sendMsgResponse.isSuccess()) {
            LOGGER.info("H5发送注册手机验证码,"+sendMsgResponse.getError());
            resultResponse.setMessage(sendMsgResponse.getError());
            return resultResponse;
        }
        // 写入短信记录
        StationSmsRecordsBean stationSmsRecordsBean = new StationSmsRecordsBean();
        stationSmsRecordsBean.setRecordPhone(memberPhone);
        stationSmsRecordsBean.setRecordType(StationSmsRecordsTypeEnum.MANAGER.getCode());
        stationSmsRecordsBean.setRecordCont(smsCont);
        stationSmsRecordsBean.setRecordAddtime(new Date());
        stationSmsRecordsBean.setRecordMbId(stationSmscontBean.getSmsId());
//        stationSmsRecordsBean.setRecordUserId(companyMemberList.get(0).getMemberId());
        int count=stationSmsRecordsService.insert(stationSmsRecordsBean);
        if(count<=0){
            throw new BusinessException("H5发送注册手机验证码，新增短信记录失败");
        }
        //将验证码放入缓存服务器 --只保留一分钟
        redisTemplate.opsForValue().set(PropertyUtils.getString("mobile.verification.code") + memberPhone, verificationCode, 600, TimeUnit.SECONDS);
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * H5审核提交
     * @return
     * @throws FileNotFoundException
     */
    @Transactional
    public ResultResponse h5Approve(CompanysBean companysBean){
        LOGGER.info("H5审核提交,传递参数:"+JSON.toJSONString(companysBean));
        ResultResponse resultResponse=new ResultResponse();
        if(companysBean!=null && !StringUtils.isStrNull(companysBean.getCompanyName())
                &&!StringUtils.isStrNull(companysBean.getCompanyAddress())
                &&!StringUtils.isStrNull(companysBean.getCompanyContactTel())
                &&!StringUtils.isStrNull(companysBean.getCompanyNumber())
                &&companysBean.getCompanyId()!=null){
                //企业信息更新
                companysBean.setCompanyEditime(new Date());
                companysBean.setCompanyAuditStatus(CompanyConstant.COMPANYMEMBER_AUDITSTATUS_ING);
                int count=companysService.updateByPrimaryKeySelective(companysBean);
                if(count<=0){
                    throw new BusinessException("H5审核提交,企业信息审核失败");
                }
                resultResponse.setSuccess(true);
                resultResponse.setData(companysBean.getCompanyId());
        }else{
            LOGGER.info("H5审核提交,传递参数不能为空");
            resultResponse.setMessage("传递参数不能为空");
        }
        return resultResponse;
    }

    /**
     * H5审核判断
     * @param companysBean
     * @return
     */
    public CompanysBean h5CheckApprove(CompanysBean companysBean){
        LOGGER.info("H5审核判断,传递参数:"+JSON.toJSONString(companysBean));
        ResultResponse resultResponse=new ResultResponse();
        if(companysBean!=null && companysBean.getCompanyId()!=null){
            CompanysBean resultBean=companysService.selectCompanyByCompanyId(companysBean.getCompanyId());
            if(resultBean!=null){
                return resultBean;
//                if(resultBean.getCompanyAuditStatus()!=null){
//                    return resultBean.getCompanyAuditStatus();
//                }else{
//                    return -1;//未审核
//                }
            }else{
                throw new BusinessException("H5审核判断,获取不到企业信息");
            }
        }else{
            throw new BusinessException("H5审核判断,传递参数不能为空");
        }
    }

    /**
     * H5用户登录
     * @param companyMembersBean
     * @param h5Code
     * @param key
     * @return
     */
    public ResultResponse login(CompanyMembersBean companyMembersBean, String h5Code, String key){
        LOGGER.info("H5用户登录,传递参数:用户信息："+JSON.toJSONString(companyMembersBean)+",验证码："+h5Code+",验证码对应的KEY："+key);
        ResultResponse resultResponse=new ResultResponse();
        if(companyMembersBean!=null && org.apache.commons.lang.StringUtils.isNotBlank(companyMembersBean.getMemberPhone()) && org.apache.commons.lang.StringUtils.isNotBlank(companyMembersBean.getMemberPassword())
                && org.apache.commons.lang.StringUtils.isNotBlank(h5Code)) {
            //验证验证码是否正确
            if (!checkVcodeimg(h5Code, key)) {//验证码不正确
                LOGGER.info("H5用户登录,验证码不正确");
                resultResponse.setMessage(ErrorCodeEnum.VERIFYCODE_ERROR.getMessage());
                return resultResponse;
            }
            //验证手机号是否可以登录
            CompanyMembersBean checkCompanyBean=new CompanyMembersBean();
            checkCompanyBean.setMemberPhone(companyMembersBean.getMemberPhone());
            List<CompanyMembersBean> companyMemberList=companyMembersService.selectCanLoginByPhone(checkCompanyBean);
            if (companyMemberList!=null && companyMemberList.size()>0) {
                CompanyMembersBean resultBean=companyMemberList.get(0);
                if(resultBean!=null && !StringUtils.isStrNull(resultBean.getMemberPassword())
                        && (StringUtils.getMD5two(companyMembersBean.getMemberPassword()).toLowerCase()).equals(resultBean.getMemberPassword())){
                    resultResponse.setSuccess(true);
                    resultResponse.setData(resultBean);
                }else{
                    LOGGER.info("H5用户登录,用户名或密码不正确");
                    resultResponse.setMessage("用户名或密码不正确");
                    return resultResponse;
                }
            }else{
                LOGGER.info("H5用户登录,用户名或密码不正确");
                resultResponse.setMessage("用户还未注册或已被关闭");
                return resultResponse;
            }
        }else{
            LOGGER.info("H5用户登录,传递的参数不能有空值");
            resultResponse.setMessage("传递的参数不能有空值");
            return resultResponse;
        }
        return resultResponse;
    }
}
