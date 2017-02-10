package com.xtr.core.service.h5wallet;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.basic.SendMsgResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.station.StationSmsRecordsBean;
import com.xtr.api.domain.station.StationSmscontBean;
import com.xtr.api.dto.hongbao.NewHongbaoDto;
import com.xtr.api.service.account.RedEnvelopeService;
import com.xtr.api.service.company.CompanyMembersService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.h5wallet.HongbaoService;
import com.xtr.api.service.send.SendMsgService;
import com.xtr.api.service.station.StationCollaborationService;
import com.xtr.api.service.station.StationSmsRecordsService;
import com.xtr.api.service.station.StationSmscontService;
import com.xtr.comm.basic.BusinessException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 红包接口实现
 * @auth zhangshuai
 * @date 2016/8/25.
 */
@Service("hongbaoService")
public class HongbaoServiceImpl implements HongbaoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HongbaoServiceImpl.class);

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


    @Transactional
    @Override
    public ResultResponse login(CompanyMembersBean companyMembersBean) throws Exception {
        LOGGER.info("H5用户登录,传递参数:用户信息："+JSON.toJSONString(companyMembersBean));
        ResultResponse resultResponse=new ResultResponse();
         if(!StringUtils.isEmpty(companyMembersBean)&&!StringUtils.isStrNull(companyMembersBean.getMemberPhone())&&!StringUtils.isStrNull(companyMembersBean.getMemberPassword())){
            //验证手机号是否被注册
            CompanyMembersBean checkCompanyBean=new CompanyMembersBean();
            checkCompanyBean.setMemberPhone(companyMembersBean.getMemberPhone());
            List<CompanyMembersBean> companyMemberList=companyMembersService.selectByPhoneOrLogname(checkCompanyBean);
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
                resultResponse.setMessage("用户还未注册，请先去注册");
                return resultResponse;
            }
        }else{
            LOGGER.info("H5用户登录,传递的参数不能有空值");
            resultResponse.setMessage("传递的参数不能有空值");
            return resultResponse;
        }
        return resultResponse;
    }

    @Transactional
    @Override
    public ResultResponse hongbaoRegister(CompanyMembersBean companyMembersBean, String registerFrom, String companyName, String phoneCode,String pushNumber) throws Exception {
        LOGGER.info("H5注册,传递参数:用户信息："+ JSON.toJSONString(companyMembersBean)+",企业名称："+companyName+",手机验证码："+phoneCode);
        ResultResponse resultResponse=new ResultResponse();
        if(companyMembersBean!=null && org.apache.commons.lang.StringUtils.isNotBlank(companyMembersBean.getMemberPhone()) && org.apache.commons.lang.StringUtils.isNotBlank(companyMembersBean.getMemberPassword())
                 && org.apache.commons.lang.StringUtils.isNotBlank(companyName) && org.apache.commons.lang.StringUtils.isNotBlank(phoneCode) ) {

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
            List<CompanyMembersBean> companyMemberList=companyMembersService.selectByPhoneOrLogname(checkCompanyBean);
            if (companyMemberList!=null && companyMemberList.size()>0) {
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
            companysBean.setCompanyContactPlace(registerFrom);//注册来源
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
            //需要保存用户姓名
            if(!StringUtils.isStrNull(companyMembersBean.getMemberName())) ;//姓名
            companyMembersBean.setMemberName(companyMembersBean.getMemberName());//保存姓名
            //企业注册用户地推 推荐人编号
            if(!StringUtils.isStrNull(pushNumber)){
              companyMembersBean.setMemberPushNumber(pushNumber);
            }
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

    @Transactional
    @Override
    public ResultResponse sendPhoneMsgByH5(String memberPhone) throws IOException {
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
        redisTemplate.opsForValue().set(PropertyUtils.getString("mobile.verification.code") + memberPhone, verificationCode, 600 , TimeUnit.SECONDS);
        resultResponse.setSuccess(true);
        return resultResponse;
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
     * 重置密码
     * @param phoneNum
     * @param phoneCode       短信验证码
     * @param confirmPassword 确认密码
     * @param newPassword     新密码
     * @return
     */
    @Override
    public ResultResponse resetPassword( String phoneNum, String phoneCode, String confirmPassword, String newPassword)throws BusinessException {
        ResultResponse resultResponse = new ResultResponse();
        //非空校验
        if(!StringUtils.isStrNull(phoneCode)&&!StringUtils.isStrNull(confirmPassword)&&!StringUtils.isStrNull(newPassword)){
            //判断两次密码是否一样
            if(!(confirmPassword.equals(newPassword))){
                resultResponse.setMessage("两次密码不一样");
                return resultResponse;
            }

            //校验短信验证码是否正确
            Object code = redisTemplate.opsForValue().get(PropertyUtils.getString("mobile.verification.code") +phoneNum);
            if (code == null || !org.apache.commons.lang.StringUtils.equals(phoneCode.toLowerCase(), String.valueOf(code).toLowerCase())) {
                LOGGER.info("H5注册用户,手机验证码不正确");
                resultResponse.setMessage("手机验证码不正确或手机号码输入错误");
                return resultResponse;
            }

            //判断当前手机是否存在
            CompanyMembersBean cc = new CompanyMembersBean();
            cc.setMemberPhone(phoneNum);
            List<CompanyMembersBean> list = companyMembersService.selectByPhoneOrLogname(cc);
            if(StringUtils.isEmpty(list)||list.size()<=0){
                resultResponse.setMessage("该手机号还未注册");
                return resultResponse;
            }
            //更新操作
            CompanyMembersBean companyMembersBean =  new CompanyMembersBean();
            companyMembersBean.setMemberId(list.get(0).getMemberId());
            companyMembersBean.setMemberPassword(StringUtils.getMD5two(confirmPassword));//MD5加密
            int updateResult = companyMembersService.updateByPrimaryKeySelective(companyMembersBean);
            if(updateResult!=1){
                //更新出错
                LOGGER.info("更新密码出现错误");
                resultResponse.setMessage("操作失败");
                return resultResponse;
            }

            resultResponse.setSuccess(true);
            companyMembersBean.setMemberCompanyId(list.get(0).getMemberCompanyId());
            resultResponse.setData(companyMembersBean);
            resultResponse.setMessage("操作成功");

        }else{
            throw new BusinessException("短信验证码或新密码或确认密码不能为空");
        }

        return resultResponse;
    }

    /**
     * 公司完善信息
     * @param companysBean
     * @return
     */
    @Override
    public ResultResponse perfectInformation(CompanysBean companysBean) throws BusinessException{

        if(StringUtils.isEmpty(companysBean)){
           throw   new  BusinessException("公司实体参数不能为空");
        }
        if (StringUtils.isEmpty(companysBean.getCompanyId())){
            throw  new BusinessException("公司id不能为空");
        }
        ResultResponse  resultResponse = new ResultResponse();
        //待审核
        companysBean.setCompanyAuditStatus(0);//待审核
        //设置当前的更新时间
        companysBean.setCompanyEditime(new Date());//记录修改时间
        int updateresult =companysService.updateByPrimaryKeySelective(companysBean);
        if(updateresult==1){
            resultResponse.setSuccess(true);
            resultResponse.setMessage("操作成功");
        }

        return resultResponse;
    }

    /**
     * 新版红包注册
     * @param newHongbaoDto
     * @return
     */
    @Transactional
    @Override
    public ResultResponse activityRegister(NewHongbaoDto newHongbaoDto,String registerFrom) throws Exception{
        //参数非空校验
        if(null==newHongbaoDto){
            throw new  BusinessException("注册页面参数封装对象为空");
        }
        if(StringUtils.isStrNull(newHongbaoDto.getCompanyName())){
            throw new BusinessException("请输入4-20位公司名称");
        }
        if(StringUtils.isStrNull(newHongbaoDto.getUserName())){
            throw new BusinessException("请输入您的姓名");
        }
        if(StringUtils.isStrNull(newHongbaoDto.getPassword())){
            throw new BusinessException("请设置登录密码");

        }
        if(StringUtils.isStrNull(newHongbaoDto.getRePassword())){
            throw new BusinessException("重复登录密码");
        }
        if(StringUtils.isStrNull(newHongbaoDto.getPhoneNumber())){
            throw new BusinessException("请输入手机号码");
        }
        if(StringUtils.isStrNull(newHongbaoDto.getPhoneCode())){
            throw  new BusinessException("请输入短信验证码");
        }
        if( !newHongbaoDto.getPassword().equals(newHongbaoDto.getRePassword())){
            throw new BusinessException("两次密码不一致");
        }

        ResultResponse resultResponse = new ResultResponse();
        //验证手机验证码是否正确
        Object code = redisTemplate.opsForValue().get(PropertyUtils.getString("mobile.verification.code") + newHongbaoDto.getPhoneNumber());
        if (code == null || !org.apache.commons.lang.StringUtils.equals(newHongbaoDto.getPhoneCode().toLowerCase(), String.valueOf(code).toLowerCase())) {
            LOGGER.info("新版注册用户,手机验证码不正确");
            resultResponse.setMessage("手机验证码不正确");
            return resultResponse;
        }
        //插入企业信息
        CompanysBean companysBean = new CompanysBean();
        Date date=new Date();
        companysBean.setCompanyName(newHongbaoDto.getCompanyName());
        companysBean.setCompanyContactTel(newHongbaoDto.getPhoneNumber());
        companysBean.setCompanyAddime(date);
        companysBean.setCompanyEditime(date);
        companysBean.setCompanyContactPlace(registerFrom);//注册来源
        Long companyId = companysService.insertByCompanyId(companysBean);
        if(companyId==null || companyId<=0){
            throw new BusinessException("插入企业信息失败");
        }
        //插入注册用户
        CompanyMembersBean companyMembersBean = new CompanyMembersBean();
        companysBean.setCompanyId(companyId);
        companyMembersBean.setMemberCompanyId(companyId);
        companyMembersBean.setMemberLogname(newHongbaoDto.getPhoneNumber());
        companyMembersBean.setMemberPhone(newHongbaoDto.getPhoneNumber());
        companyMembersBean.setMemberPassword(StringUtils.getMD5two(newHongbaoDto.getPassword()));
        companyMembersBean.setCompanyName(newHongbaoDto.getCompanyName());
        companyMembersBean.setMemberIsdefault(CompanyMeMemberIsdefaultEnum.IS_MANAGER.getCode());
        companyMembersBean.setRegisterTime(date);
        companyMembersBean.setMemberLoginRecentTime(date);
        companyMembersBean.setMemberSign(CompanyMeMemberSignEnum.ACTIVE.getCode());
        companyMembersBean.setMemberStatus(1);
        companyMembersBean.setMemberName(newHongbaoDto.getUserName());//保存姓名
        //企业注册用户地推 推荐人编号
//        if(!StringUtils.isStrNull(pushNumber)){
//            companyMembersBean.setMemberPushNumber(pushNumber);
//        }
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
        LOGGER.info("新版红包注册用户,发送合作意向，返回结果:"+ JSON.toJSONString(collaborationResponse));
        if(!collaborationResponse.isSuccess()){
            throw new BusinessException(collaborationResponse.getMessage());
        }


        resultResponse.setSuccess(true);
        //设置公司id
        newHongbaoDto.setCompanyId(companyId);
        resultResponse.setData(newHongbaoDto);
        return resultResponse;
    }

    /**
     * 新版红包登录
     * @param newHongbaoDto
     * @return
     */

    @Override
    public ResultResponse activityLogin(NewHongbaoDto newHongbaoDto) throws Exception{
        if(null==newHongbaoDto){
            throw new BusinessException("登录封装对象为空");
        }
        if(StringUtils.isStrNull(newHongbaoDto.getLoginPhoneNumber())){
            throw new BusinessException("请输入手机号码");
        }
        if(StringUtils.isStrNull(newHongbaoDto.getLoginPassword())){
            throw new BusinessException("请输入登录密码");
        }
        if (StringUtils.isStrNull(newHongbaoDto.getCode())){
            throw  new BusinessException("请输入图形验证码");
        }
        ResultResponse resultResponse=new ResultResponse();

            //验证验证码是否正确
            if (!checkVcodeimg(newHongbaoDto.getCode(), newHongbaoDto.getKey())) {//验证码不正确
                LOGGER.info("用户登录,验证码不正确");
                resultResponse.setMessage(ErrorCodeEnum.VERIFYCODE_ERROR.getMessage());
                return resultResponse;
            }
            //验证手机号是否可以登录
            CompanyMembersBean checkCompanyBean=new CompanyMembersBean();
            checkCompanyBean.setMemberPhone(newHongbaoDto.getLoginPhoneNumber());
            List<CompanyMembersBean> companyMemberList=companyMembersService.selectCanLoginByPhone(checkCompanyBean);
            if (companyMemberList!=null && companyMemberList.size()>0) {
                CompanyMembersBean resultBean=companyMemberList.get(0);
                if(resultBean!=null && !StringUtils.isStrNull(resultBean.getMemberPassword())
                        && (StringUtils.getMD5two(newHongbaoDto.getLoginPassword()).toLowerCase()).equals(resultBean.getMemberPassword())){
                    resultResponse.setSuccess(true);
                    resultResponse.setData(resultBean);
                }else{
                    LOGGER.info("用户登录,用户名或密码不正确");
                    resultResponse.setMessage("用户名或密码不正确");
                    return resultResponse;
                }
            }else{
                LOGGER.info("用户登录,用户名或密码不正确");
                resultResponse.setMessage("用户还未注册");
                return resultResponse;
            }

        return resultResponse;
    }

    @Transactional
    @Override
    public ResultResponse activityApprove(CompanysBean companysBean) throws Exception{
        if(null==companysBean){
          throw new BusinessException("信息完善封装对象为空");
        }
        if(StringUtils.isStrNull(companysBean.getCompanyName())){
            throw new BusinessException("公司名称不能为空");
        }
        if(StringUtils.isStrNull(companysBean.getCompanyAddress())){
            throw  new BusinessException("公司地址不能为空值");
        }
        if(StringUtils.isStrNull(companysBean.getCompanyNumber())){
          throw new BusinessException("营业执照编号不能为空");
        }
        if(StringUtils.isStrNull(companysBean.getCompanyOrganizationImg())){
            throw  new BusinessException("营业执照扫描文件不能为空");
        }

        if(StringUtils.isStrNull(companysBean.getCompanyCorporationPhone())){
            throw new BusinessException("联系电话不能为空");
        }

        ResultResponse  resultResponse = new ResultResponse();
        //待审核
        companysBean.setCompanyAuditStatus(0);//待审核
        //设置当前的更新时间
        companysBean.setCompanyEditime(new Date());//记录修改时间
        int updateresult =companysService.updateByPrimaryKeySelective(companysBean);
        if(updateresult==1){
            resultResponse.setSuccess(true);
            resultResponse.setMessage("操作成功");
        }
        return resultResponse;
    }

    /**
     * 更新公司信息收集
     * @param newHongbaoDto
     * @return
     */
    @Override
    public ResultResponse infoCollectSubmit(NewHongbaoDto newHongbaoDto) throws Exception {
        if(null==newHongbaoDto){
          throw  new BusinessException("信息采集页面封装参数为空");
        }
        if(StringUtils.isEmpty(newHongbaoDto.getCompanyId())){
            throw new BusinessException("公司Id为空");
        }
        if(StringUtils.isStrNull(newHongbaoDto.getCompanyTruename())){
            throw new BusinessException("姓名不能为空");
        }
        if(StringUtils.isStrNull(newHongbaoDto.getCompanyAlipayNumber())){
            throw new BusinessException("支付宝账号不能为空");
        }
        //更新
        ResultResponse resultResponse = new ResultResponse();
        Map map = new HashMap();
        map.put("companyTruename",newHongbaoDto.getCompanyTruename());//姓名
        map.put("companyAlipayNumber",newHongbaoDto.getCompanyAlipayNumber());//支付宝账号
        map.put("companyIsCollectInfo",1);//已完善
        map.put("companyId",newHongbaoDto.getCompanyId());//公司id
        int  result = companysService.updateCollectInfo(map);
        if(result==1){
            resultResponse.setSuccess(true);
            resultResponse.setData(newHongbaoDto);
        }
        return resultResponse;
    }

    /**
     * 新版红包登录简化版
     * @param newHongbaoDto
     * @return
     * @throws Exception
     */
    @Override
    public ResultResponse activityLoginNew(NewHongbaoDto newHongbaoDto) throws Exception {
        if(null==newHongbaoDto){
            throw new BusinessException("登录封装对象为空");
        }
        if(StringUtils.isStrNull(newHongbaoDto.getLoginPhoneNumber())){
            throw new BusinessException("请输入手机号码");
        }
        if(StringUtils.isStrNull(newHongbaoDto.getLoginPassword())){
            throw new BusinessException("请输入登录密码");
        }
        ResultResponse resultResponse=new ResultResponse();

//        //验证验证码是否正确
//        if (!checkVcodeimg(newHongbaoDto.getCode(), newHongbaoDto.getKey())) {//验证码不正确
//            LOGGER.info("用户登录,验证码不正确");
//            resultResponse.setMessage(ErrorCodeEnum.VERIFYCODE_ERROR.getMessage());
//            return resultResponse;
//        }
        //验证手机号是否可以登录
        CompanyMembersBean checkCompanyBean=new CompanyMembersBean();
        checkCompanyBean.setMemberPhone(newHongbaoDto.getLoginPhoneNumber());
        List<CompanyMembersBean> companyMemberList=companyMembersService.selectCanLoginByPhone(checkCompanyBean);
        if (companyMemberList!=null && companyMemberList.size()>0) {
            CompanyMembersBean resultBean=companyMemberList.get(0);
            if(resultBean!=null && !StringUtils.isStrNull(resultBean.getMemberPassword())
                    && (StringUtils.getMD5two(newHongbaoDto.getLoginPassword()).toLowerCase()).equals(resultBean.getMemberPassword())){
                resultResponse.setSuccess(true);
                resultResponse.setData(resultBean);
            }else{
                LOGGER.info("用户登录,用户名或密码不正确");
                resultResponse.setMessage("用户名或密码不正确");
                return resultResponse;
            }
        }else{
            LOGGER.info("用户登录,用户名或密码不正确");
            resultResponse.setMessage("用户还未注册");
            return resultResponse;
        }
       // LOGGER.info("用户登录4");
        return resultResponse;
    }

    /**
     * 新版红包注册简化版
     * @param newHongbaoDto
     * @param registerFrom
     * @return
     * @throws Exception
     */
    @Override
    public ResultResponse activityRegisterNew(NewHongbaoDto newHongbaoDto, String registerFrom) throws Exception {
        //参数非空校验
        if(null==newHongbaoDto){
            throw new  BusinessException("注册页面参数封装对象为空");
        }
        if(StringUtils.isStrNull(newHongbaoDto.getCompanyName())){
            throw new BusinessException("请输入4-20位公司名称");
        }

        if(StringUtils.isStrNull(newHongbaoDto.getPassword())){
            throw new BusinessException("请设置登录密码");

        }
        if(StringUtils.isStrNull(newHongbaoDto.getPhoneNumber())){
            throw new BusinessException("请输入手机号码");
        }
        if(StringUtils.isStrNull(newHongbaoDto.getPhoneCode())){
            throw  new BusinessException("请输入短信验证码");
        }


        ResultResponse resultResponse = new ResultResponse();
        //验证手机验证码是否正确
        Object code = redisTemplate.opsForValue().get(PropertyUtils.getString("mobile.verification.code") + newHongbaoDto.getPhoneNumber());
        if (code == null || !org.apache.commons.lang.StringUtils.equals(newHongbaoDto.getPhoneCode().toLowerCase(), String.valueOf(code).toLowerCase())) {
            LOGGER.info("新版注册用户,手机验证码不正确");
            resultResponse.setMessage("手机验证码不正确");
            return resultResponse;
        }
        //插入企业信息
        CompanysBean companysBean = new CompanysBean();
        Date date=new Date();
        companysBean.setCompanyName(newHongbaoDto.getCompanyName());
        companysBean.setCompanyContactTel(newHongbaoDto.getPhoneNumber());
        companysBean.setCompanyAddime(date);
        companysBean.setCompanyEditime(date);
        companysBean.setCompanyContactPlace(registerFrom);//注册来源
        Long companyId = companysService.insertByCompanyId(companysBean);
        if(companyId==null || companyId<=0){
            throw new BusinessException("插入企业信息失败");
        }
        //插入注册用户
        CompanyMembersBean companyMembersBean = new CompanyMembersBean();
        companysBean.setCompanyId(companyId);
        companyMembersBean.setMemberCompanyId(companyId);
        companyMembersBean.setMemberLogname(newHongbaoDto.getPhoneNumber());
        companyMembersBean.setMemberPhone(newHongbaoDto.getPhoneNumber());
        companyMembersBean.setMemberPassword(StringUtils.getMD5two(newHongbaoDto.getPassword()));
        companyMembersBean.setCompanyName(newHongbaoDto.getCompanyName());
        companyMembersBean.setMemberIsdefault(CompanyMeMemberIsdefaultEnum.IS_MANAGER.getCode());
        companyMembersBean.setRegisterTime(date);
        companyMembersBean.setMemberLoginRecentTime(date);
        companyMembersBean.setMemberSign(CompanyMeMemberSignEnum.ACTIVE.getCode());
        companyMembersBean.setMemberStatus(1);
       // companyMembersBean.setMemberName(newHongbaoDto.getUserName());//保存姓名
        //企业注册用户地推 推荐人编号
//        if(!StringUtils.isStrNull(pushNumber)){
//            companyMembersBean.setMemberPushNumber(pushNumber);
//        }
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
        LOGGER.info("新版红包注册用户,发送合作意向，返回结果:"+ JSON.toJSONString(collaborationResponse));
        if(!collaborationResponse.isSuccess()){
            throw new BusinessException(collaborationResponse.getMessage());
        }


        resultResponse.setSuccess(true);
        //设置公司id
        newHongbaoDto.setCompanyId(companyId);
        resultResponse.setData(newHongbaoDto);
        return resultResponse;
    }


}
