package com.xtr.api.service.company;


import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanySocialOrdersBean;
import com.xtr.api.dto.company.CompanySocialOrdersDto;

/**
 * <p>企业上传工资文档</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/17 10:09
 */
public interface CompanySocialOrdersService {

    /**
     * 插入记录
     * @param companySocialOrdersBean
     * @return
     */
    long insert(CompanySocialOrdersBean companySocialOrdersBean);

    /**
     * 分页查询
     * @param companySocialOrdersBean
     * @return
     */
    ResultResponse selectPageList(CompanySocialOrdersBean companySocialOrdersBean);

    /**
     * 更新记录
     * @param companySocialOrdersBean
     * @return
     */
    int update(CompanySocialOrdersBean companySocialOrdersBean);

    /**
     * 根据主键查询
     * @param orderId
     * @return
     */
    CompanySocialOrdersBean find(long orderId);

    /**
     * 根据主键查询
     * @param orderId
     * @return
     */
    CompanySocialOrdersDto getByOrderId(Long orderId);

    /**
     * 根据订单编号查询
     *
     * @param orderRechagrId
     * @return
     */
    CompanySocialOrdersDto getByOrderRechagrId(Long orderRechagrId);

}
