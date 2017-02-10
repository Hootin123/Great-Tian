package com.xtr.api.service.h5wallet;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.dto.hongbao.NewHongbaoDto;
import com.xtr.comm.basic.BusinessException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 红包接口
 * @author  zhangshuai
 * @date 2016/8/25.
 */
public interface HongbaoService {

    /**
     * 登录
     * @param companyMembersBean
     * @return
     * @throws Exception
     */
    public ResultResponse login(CompanyMembersBean companyMembersBean) throws Exception;

    /**
     * 注册
     * @param companyMembersBean
     * @param registerFrom
     * @param companyName
     * @param phoneCode          @return
     */
    ResultResponse hongbaoRegister(CompanyMembersBean companyMembersBean, String registerFrom, String companyName, String phoneCode, String pushNumber) throws Exception;

    /**
     * 发送手机验证码
     * @param memberPhone
     * @return
     */
    ResultResponse sendPhoneMsgByH5(String memberPhone) throws IOException;

    /**
     * 生成验证码
     * @param key
     * @param response
     */
    void generateH5ValidatorImages(String key, HttpServletResponse response) throws IOException;

    /**
     * 重置密码
     * @param phoneNum
     * @param phoneCode       短信验证码
     * @param confirmPassword 确认密码
     * @param newPassword     新密码
     * @return
     */
    ResultResponse resetPassword(String phoneNum, String phoneCode, String confirmPassword, String newPassword) throws BusinessException;

    /**
     * 公司完善信息
     * @param companysBean
     * @return
     */
    ResultResponse perfectInformation(CompanysBean companysBean);

    /**
     * 新版红包注册
     * @param newHongbaoDto
     * @return
     */

    ResultResponse activityRegister(NewHongbaoDto newHongbaoDto, String registerFrom) throws Exception;

    /**
     * 新版红包登录
     * @param newHongbaoDto
     * @return
     */
    ResultResponse activityLogin(NewHongbaoDto newHongbaoDto) throws Exception;

    /**
     * 新版红包资料完善
     * @param companysBean
     * @return
     */
    ResultResponse activityApprove(CompanysBean companysBean) throws Exception;

    /**
     * 收集资料提交
     * @param newHongbaoDto
     * @return
     */
    ResultResponse infoCollectSubmit(NewHongbaoDto newHongbaoDto) throws Exception;


    /**
     * 新版红包登录 简化版
     *
     * @param newHongbaoDto
     * @return
     */
    ResultResponse activityLoginNew(NewHongbaoDto newHongbaoDto) throws Exception;

    /**
     * 新版红包注册 简化版
     * @param newHongbaoDto
     * @param registerFrom
     * @return
     * @throws Exception
     */
    ResultResponse activityRegisterNew(NewHongbaoDto newHongbaoDto, String registerFrom) throws Exception;
}