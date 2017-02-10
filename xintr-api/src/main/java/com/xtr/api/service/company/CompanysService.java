package com.xtr.api.service.company;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyActivityBean;
import com.xtr.api.domain.company.FileResourcesBean;
import com.xtr.api.domain.company.CompanySalaryExcelBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.dto.company.CompanyRechargeDto;
import com.xtr.api.dto.company.CompanysDto;
import com.xtr.api.dto.gateway.response.NotifyResponse;
import com.xtr.api.dto.hongbao.LastHongbaoDto;
import com.xtr.comm.basic.BusinessException;

import java.util.List;
import java.util.Map;

/**
 * Created by abiao on 2016/6/22.
 */
public interface CompanysService {

    /**
     * 根据公司名称验证企业是否存在
     *
     * @param companyName
     * @return
     */
    ResultResponse checkCompanyByName(String companyName) throws BusinessException;

    /**
     * 根据过滤条件获取企业信息
     * @param companysDto
     * @return
     */
    ResultResponse selectCompanyInfoPageList(CompanysDto companysDto);

    /**
     * 根据企业编号获取企业详细信息
     * @param companyId
     * @return
     */
    ResultResponse selectCompanyInfoDetail(long companyId);

    /**
     * 根据企业编号获取企业充值或提现记录信息
     * @param companyRechargeDto
     * @return
     */
    ResultResponse selectCompanyRechargeList(CompanyRechargeDto companyRechargeDto);

    /**
     * 根据企业编号获取发工资信息
     * @param companySalaryExcelBean
     * @return
     */
    ResultResponse selectCompanySalaryList(CompanySalaryExcelBean companySalaryExcelBean);

    /**
     * 根据企业编号查询企业信息
     *
     * @param companyId
     * @return
     */
    CompanysBean selectCompanyByCompanyId(Long companyId);

    /**
     * 新写入数据库记录,companys
     *
     * @param companysBean
     */
    int addCompanys(CompanysBean companysBean) throws BusinessException;

    /**
     * 修改垫付信息
     *
     * @param companysBean
     * @return
     */
    void updateCompanysBeanId(CompanysBean companysBean) throws BusinessException;

    /**
     * 查询
     * @param param
     * @return
     */
    Map<String,Object> find(Map<String, Object> param);

    /**
     * 增加新闻点击次数
     * @param nId
     * @return
     */
    boolean stationNewsClicked(long nId);

    /**
     * 分页
     * @param param
     * @return
     */
    List<Map<String,Object>> findListPage(Map<String, Object> param);

    /**
     * 总数
     * @param param
     * @return
     */
    int findListPageCount(Map<String, Object> param);

    int insert(CompanysBean companysBean);

    ResultResponse selectCompanyByCompanyNumber(String companyNumber);


    /**
     * 查询
     * @param map
     * @return
     */
    Map<String,Object> stationCollaborationFind(Map<String, Object> map);

    /**
     * 新增
     * @param map
     * @return
     */
    int stationCollaborationAdd(Map<String, Object> map);

    /**
     * 更新
     * @param map
     * @return
     */
    int stationCollaborationUpdate(Map<String, Object> map);

    /**
     * 企业协议管理时更改企业相关信息
     * @param companysBean
     * @return
     */
    int updateByProtocolModify(CompanysBean companysBean)throws BusinessException;

    /**
     * 新写入数据库记录,file_resources
     *
     * @param fileResourcesBean
     */
    int addFileResources(FileResourcesBean fileResourcesBean) throws BusinessException;

    /**
     * 查询资源文件
     * @param map
     * @return
     */
    List<Map<String,Object>> getfileResources(Map<String, Object> map);

    /**
     * 删除资源文件
     *
     * @param id
     * @return
     */
    void deletefileResources(Long id) throws BusinessException;

    /**
     * 插入企业信息返回企业ID
     * @param companysBean
     * @return
     */
    Long insertByCompanyId(CompanysBean companysBean);

    /**
     * 查询资源文件类型
     * @param map
     * @return
     */
    List<Map<String,Object>> getfileResourcesByFileType(Map<String, Object> map);

    /**
     * 更改企业信息
     * @param companysBean
     * @return
     * @throws BusinessException
     */
    int  updateByPrimaryKeySelective(CompanysBean companysBean);


    /**
     * 查询垫付列表
     * @param companysBean
     * @param kw
     * @param kw
     * @return
     */
    ResultResponse selectPrepaidPage(CompanysBean companysBean, String kw);

    /**
     * 审核垫付
     * @param cId
     * @param cause
     * @param b
     * @param memberId
     * @return
     */
    boolean auditPrepaid(long cId, String cause, boolean b, Long memberId);

    /**
     * 处理京东回调
     * @param response
     * @return
     */
    boolean doJdResponse(NotifyResponse response);

    /**
     * 查询当前领取红包的hr人数
     * @return
     */
    long selectActivityRedCounts();

    /**
     * 查询领取完善信息红包的人数
     * @return
     */
    long selectCountsOfPerfectInfo();

    /**
     * 查询当前登录账号已领取的红包金额
     * @param memberId
     * @return
     */
    long selectHasReceiveAccount(Long memberId);

    /**
     * 查询当前用户是否领取过5元注册红包或10元完善信息红包
     * @param params
     * @return
     */
    CompanyActivityBean selectReds(Map<String, Object> params);

    /**
     * 保存已发送红包信息记录
     * @param activity
     * @return
     */
    CompanyActivityBean saveCompanyActivity(CompanyActivityBean activity);

    /**
     * 更新红包
     * @param updateMap
     * @return
     */
    int updateActivity(Map updateMap);

    /**
     * 获取企业总数
     * @param companysDto
     * @return
     */
    Integer selectCountForCompanyList(CompanysDto companysDto);

    /***
     * 查询入职须知
     * @param wechatCompanyId
     * @return
     */
    CompanysBean selectCompanyEnterRequireById(Long wechatCompanyId);

    /**
     * 新版红包更新姓名 支付宝账号
     * @param map
     * @return
     */
    int updateCollectInfo(Map map);

    /**
     * 简化版红包注册之前保存手机号
     * @param lastHongbaoDto
     * @return
     */
    ResultResponse saveCompanyPhone(LastHongbaoDto lastHongbaoDto) throws Exception;
}
