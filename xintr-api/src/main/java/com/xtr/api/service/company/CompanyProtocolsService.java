package com.xtr.api.service.company;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyProtocolsBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.dto.company.CompanyProtocolsDto;
import com.xtr.comm.basic.BusinessException;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/11 17:55
 */
public interface CompanyProtocolsService {

    /**
     *根据企业ID更改协议
     * @param companyProtocolsBean
     * @return
     * @throws BusinessException
     */
    int updateByCompanyAndType(CompanyProtocolsBean companyProtocolsBean)throws BusinessException;

    /**
     *新增企业协议
     * @param companyProtocolsBean
     * @return
     */
    int insert(CompanyProtocolsBean companyProtocolsBean)throws BusinessException;

    /**
     * 根据企业ID查询协议
     * @param protocolCompanyId
     * @return
     */
    List<CompanyProtocolsBean> selectByCompanyId(Long protocolCompanyId);

    /**
     * 企业详细修改企业协议
     * @param dtype
     * @param ftype
     * @param dno
     * @param fno
     * @param companyId
     * @return
     * @throws BusinessException
     */
    ResultResponse updateCompanyProtocol(int dtype, int ftype, String dno, String fno, Long companyId)throws Exception;

    /**
     * 根据过滤条件查询企业协议
     * @param companyProtocolsDto
     * @return
     */
    ResultResponse selectProtocolByCondition(CompanyProtocolsDto companyProtocolsDto);

    /**
     *点击确认按钮,更新企业信息,新增企业协议信息
     * @param companyProtocolsBean
     * @return
     */
    ResultResponse updateProtocolAndUpdateCompany(CompanyProtocolsBean companyProtocolsBean, CompanysBean companysBean, Long userId)throws Exception;

    /**
     * 查询所有的企业协议
     * @return
     */
    List<CompanyProtocolsDto> selectProtocolAll();

    /**
     * 根据协议ID批量更新协议状态
     * @param companyProtocolsBean
     * @return
     */
    int updateCurrentStateById(CompanyProtocolsBean companyProtocolsBean);

    /**
     *根据协议ID查询协议和企业信息
     * @param protocolId
     */
    CompanyProtocolsDto selectProtocolAndCompanyById(Long protocolId);

    /**
     * 修改协议状态业务
     * @param companyProtocolsBean
     * @return
     */
    ResultResponse updateCurrentStateByIdBusiness(CompanyProtocolsBean companyProtocolsBean)throws BusinessException;

    /**
     * 根据企业ID查询签约\即将到期\冻结的协议
     * @param companyProtocolsBean
     * @return
     */
    List<CompanyProtocolsBean> selectDfInfoByState(CompanyProtocolsBean companyProtocolsBean);

    /**
     * 批量更新状态
     * @param list
     * @return
     */
    int updateCurrentStateByIdList(List<CompanyProtocolsBean> list);

    /**
     * 根据协议ID更新协议
     * @param companyProtocolsBean
     */
    int updateProtocolInfoById(CompanyProtocolsBean companyProtocolsBean);

    /**
     * 根据企业ID查询协议(分页)
     * @param companyProtocolsBean
     * @return
     */
    ResultResponse selectPageListByCompanyId(CompanyProtocolsBean companyProtocolsBean);

    /**
     * 查询征信资料
     * @param companyId
     * @return
     */
    ResultResponse queryCreditInformations(String currentPath,Long companyId)throws BusinessException,IOException;

    /**
     * 根据协议类型及指定时间判断协议是否还有效
     * @param protocolCompanyId 企业ID
     * @param protocolContractType 协议类型 1代发协议 2垫发协议
     * @param checkTime 指定时间
     * @return null 代表没有有效的协议
     */
    CompanyProtocolsBean selectIsUserFulByTypeAndTime(Long protocolCompanyId,Integer protocolContractType,Date checkTime);

    /**
     * 搜索第二个列表企业协议
     * @param companyProtocolsDto
     * @return
     */
    ResultResponse queryCompanysProtocol(CompanyProtocolsDto companyProtocolsDto);

    /**
     * 添加企业协议
     * @param companyProtocolsBean
     * @param memberId
     * @return
     * @throws Exception
     */
    ResultResponse addCompanyProtocol(CompanyProtocolsBean companyProtocolsBean, Long memberId)throws Exception;

    /**
     * 根据企业ID查询签约\即将到期\冻结的协议
     * @param companyProtocolsBean
     * @return
     */
    List<CompanyProtocolsBean> selectUsefulProtocolsByState(CompanyProtocolsBean companyProtocolsBean);

    /**
     * 是否能够签署垫发协议
     * @param companyId
     * @return
     */
    boolean isCanSignDf(long companyId);

    /**
     * 根据企业ID查询协议
     * @param companyProtocolsBean
     * @return
     */
    List<CompanyProtocolsBean> selectByContractType(CompanyProtocolsBean companyProtocolsBean);

    /**
     * 根据企业ID查询签约\即将到期\冻结的协议
     * @param companyProtocolsBean
     * @return
     */
    List<CompanyProtocolsBean> selectUsefulProtocolsByAddTime(CompanyProtocolsBean companyProtocolsBean);

    /**
     *根据企业ID查询签约\即将到期\冻结的协议的数量
     * @param companyProtocolsBean
     * @return
     */
    int selectCountByUserfulProtocol(CompanyProtocolsBean companyProtocolsBean);

    /**
     * 批量获取企业协议有效信息
     * @param list
     * @return
     */
    List<CompanyProtocolsBean> selectUsefulProtocolsBatch(List<Long> list);

    /**
     * 根据企业ID获取最新的签约记录
     * @param protocolCompanyId
     * @return
     */
    List<CompanyProtocolsDto> selectProtocolByCompanyId(Long protocolCompanyId);

    /**
     * 根据企业id，协议类型查询签约/即将到期的协议
     * @param companyId
     * @param protocolContractType
     * @return
     */
    CompanyProtocolsBean selectByCorpProtocol(Long companyId,int protocolContractType);

    /**
     *更改发工资相关状态
     * @param protocolCompanyId
     * @param protocolContractType
     * @param type 1更改是否计算社保公积金状态为是 其它的值代表更改为否
     */
    void updateSalaryState(Long protocolCompanyId,Integer protocolContractType,int type);

    /**
     * 获取协议最新的一条数据
     * @param protocolCompanyId
     * @param protocolContractType
     * @return
     */
    CompanyProtocolsBean selectLastData(long protocolCompanyId, int protocolContractType);

    /**
     * 获取企业有效协议的数量
     * @param companyId
     * @return
     */
    int selectUsefulProtocolForCompany(long companyId);

    /**
     * 获取企业有效协议的协议
     * @param companyId
     * @return
     */
    List<CompanyProtocolsBean> selectUsefulListForCompanyId( long companyId);

    /**
     * 续约
     * @param
     * @param
     * @return
     */
    int renewSureProtocol(Map map);
}
