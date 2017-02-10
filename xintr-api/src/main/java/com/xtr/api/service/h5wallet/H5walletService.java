package com.xtr.api.service.h5wallet;

import com.xtr.api.basic.AppResponse;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.comm.basic.BusinessException;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/8/9 13:11
 */
public interface H5walletService {

    /**
     * H5注册
     * @param companyMembersBean
     * @param companyName
     * @return
     * @throws BusinessException
     */
    ResultResponse register(CompanyMembersBean companyMembersBean, String h5Code, String companyName, String phoneCode, String key,String registerCampaignCode)throws Exception;

    /**
     * 生成H5验证码图片
     * @param key
     * @param response
     * @throws IOException
     */
    void generateH5ValidatorImages(String key,HttpServletResponse response)throws IOException;

    /**
     * H5发送注册手机验证码
     * @param memberPhone
     * @return
     * @throws IOException
     */
    ResultResponse sendPhoneMsgByH5(String memberPhone)throws IOException;

    /**
     * H5审核提交
     * @return
     * @throws FileNotFoundException
     */
    ResultResponse h5Approve( CompanysBean companysBean);

    /**
     * H5审核判断
     * @param companysBean
     * @return
     */
    CompanysBean h5CheckApprove(CompanysBean companysBean);

    /**
     * H5用户登录
     * @param companyMembersBean
     * @param h5Code
     * @param key
     * @return
     */
    ResultResponse login(CompanyMembersBean companyMembersBean, String h5Code, String key);
}
