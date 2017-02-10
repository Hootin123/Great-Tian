package com.xtr.core.persistence.writer.company;

import com.xtr.api.domain.company.CompanyProtocolsBean;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/11 17:46
 */
public interface CompanyProtocolsWriterMapper {

    /**
     * 根据企业ID更改协议
     * @param companyProtocolsBean
     * @return
     */
    int updateByCompanyAndType(CompanyProtocolsBean companyProtocolsBean);

    /**
     * 新增企业协议
     * @param companyProtocolsBean
     * @return
     */
    int insert(CompanyProtocolsBean companyProtocolsBean);

    /**
     * 根据协议ID更新协议状态
     * @param companyProtocolsBean
     * @return
     */
    int updateCurrentStateById(CompanyProtocolsBean companyProtocolsBean);

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
     * 续约
     * @param
     * @param
     * @return
     */
    int renewSureProtocol(Map  map);
}
