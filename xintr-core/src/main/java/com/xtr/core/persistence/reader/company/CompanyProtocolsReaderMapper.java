package com.xtr.core.persistence.reader.company;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.xtr.api.domain.company.CompanyProtocolsBean;
import com.xtr.api.dto.company.CompanyProtocolsDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/11 17:12
 */
public interface CompanyProtocolsReaderMapper {

    /**
     * 根据企业ID查询协议
     * @param protocolCompanyId
     * @return
     */
    List<CompanyProtocolsBean> selectByCompanyId(Long protocolCompanyId);

    /**
     * 根据过滤条件查询企业协议(分页)
     * @param companyProtocolsDto
     * @return
     */
    List<CompanyProtocolsDto> selectProtocolByCondition(CompanyProtocolsDto companyProtocolsDto,PageBounds pageBounds);

    /**
     * 根据过滤条件查询企业协议
     * @param companyProtocolsDto
     * @return
     */
    List<CompanyProtocolsDto> selectProtocolByCondition(CompanyProtocolsDto companyProtocolsDto);

    /**
     * 查询所有的企业协议
     * @return
     */
    List<CompanyProtocolsDto> selectProtocolAll();

    /**
     *根据协议ID查询协议和企业信息
     * @param protocolId
     */
    List<CompanyProtocolsDto> selectProtocolAndCompanyById(Long protocolId);

    /**
     * 根据企业ID查询签约\即将到期\冻结的协议
     * @param companyProtocolsBean
     * @return
     */
    List<CompanyProtocolsBean> selectDfInfoByState(CompanyProtocolsBean companyProtocolsBean);

    /**
     * 根据企业ID查询协议(分页)
     * @param protocolCompanyId
     * @return
     */
    List<CompanyProtocolsBean> selectByCompanyId(Long protocolCompanyId,PageBounds pageBounds);

    /**
     * 根据协议类型及指定时间判断协议是否还有效
     * @param companyProtocolsBean
     * @return
     */
    CompanyProtocolsBean selectIsUserFulByTypeAndTime(CompanyProtocolsBean companyProtocolsBean);

    /**
     * 第二个查询列表通过过滤条件查询
     * @return
     */
    List<CompanyProtocolsDto> selectCompanyProtocolAllByCreateTime(CompanyProtocolsDto companyProtocolsDto);

    /**
     * 根据企业ID查询签约\即将到期\冻结的协议
     * @param companyProtocolsBean
     * @return
     */
    List<CompanyProtocolsBean> selectUsefulProtocolsByState(CompanyProtocolsBean companyProtocolsBean);

    /**
     * 根据企业ID查询签约\即将到期\冻结的协议
     * @param companyProtocolsBean
     * @return
     */
    List<CompanyProtocolsBean> selectUsefulProtocolsByAddTime(CompanyProtocolsBean companyProtocolsBean);

    /**
     * 根据企业ID查询协议
     * @param companyProtocolsBean
     * @return
     */
    List<CompanyProtocolsBean> selectByContractType(CompanyProtocolsBean companyProtocolsBean);

    /**
     *根据企业ID查询签约\即将到期\冻结的协议的数量
     * @param companyProtocolsBean
     * @return
     */
    int selectCountByUserfulProtocol(CompanyProtocolsBean companyProtocolsBean);

    /**
     *根据企业ID查询签约\即将到期\的协议
     * @param companyId
     * @return
     */
    CompanyProtocolsBean selectByCorpProtocol(@Param("companyId")Long companyId,@Param("protocolContractType")int protocolContractType);

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
     * 根据企业ID查询签约\即将到期\冻结的代发\垫发协议(此处的sql别人不要调用,方便后面如果要扩展,可能会更改)
     * @param protocolCompanyId
     * @return
     */
    CompanyProtocolsBean selectDJAndFFInfoByState(Long protocolCompanyId);

    /**
     * 查询有效协议数量
     * @param companyId
     * @param protocolType
     * @return
     */
    int selectUsefulProtocolCount(@Param("companyId") long companyId, @Param("protocolType") int protocolType);

    /**
     * 获取协议最新的一条数据
     * @param protocolCompanyId
     * @param protocolContractType
     * @return
     */
    CompanyProtocolsBean selectLastData(@Param("protocolCompanyId") long protocolCompanyId, @Param("protocolContractType") int protocolContractType);

    /**
     * 获取企业有效协议的数量
     * @param companyId
     * @return
     */
    int selectUsefulProtocolForCompany(@Param("companyId") long companyId);

    /**
     * 获取企业有效协议的协议
     * @param companyId
     * @return
     */
    List<CompanyProtocolsBean> selectUsefulListForCompanyId(@Param("companyId") long companyId);
}
