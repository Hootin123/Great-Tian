package com.xtr.api.service.shebao;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.customer.CustomerShebaoBean;
import com.xtr.api.dto.customer.CustomerShebaoOrderDto;
import com.xtr.api.dto.shebao.JrOrderDto;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.enums.ShebaoTypeEnum;
import com.xtr.comm.sbt.api.Basic;
import com.xtr.comm.sbt.api.SocialBase;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 社保
 * @Author Xuewu
 * @Date 2016/9/13.
 */
public interface CustomerShebaoService {

    /**
     * update
     * @param record
     * @return
     */
    int updateByPrimaryKey(CustomerShebaoBean record);

    /**
     * 计算社保或公积金
     * @param base
     * @return
     * @throws BusinessException
     */
    Map cal(double base, SocialBase socialBase) throws BusinessException, IOException;
//
//    /**
//     * 计算社保或公积金  根据社保通计算
//     * @param base
//     * @return
//     * @throws BusinessException
//     */
//    Map calBySbt(double base, SocialBase socialBase) throws BusinessException, IOException;

    /**
     * 计算补缴
     * @param overOrders
     * @param socialBase
     * @return
     */
    List<Map> calOverDueBySbt(List<JrOrderDto> overOrders, SocialBase socialBase) throws BusinessException, IOException;

    /**
     *
     * @param bean
     * @return
     */
    ResultResponse selectPage(CustomerShebaoBean bean);


    /**
     * 分页查询员工订单
     * @param customerShebaoOrderDto
     * @return
     */
    List<CustomerShebaoOrderDto> getCustomerOrders(CustomerShebaoOrderDto customerShebaoOrderDto);

    /**
     * 根据员工ID 查询员工社保基础信息
     * @param customerId
     * @return
     */
    CustomerShebaoBean selectCustomerShebaoByCustomerId(long customerId);


    /**
     * 根据社保缴纳类型获取基本信息
     * @param shebaoTypeEnum
     * @param basic
     * @param type
     * @return
     * @throws BusinessException
     */
    SocialBase getSocialBaseByBasic(ShebaoTypeEnum shebaoTypeEnum, Basic basic, String type) throws BusinessException;


    /**
     * 获取调基月份
     * @param joinCity
     * @param shebaoTypeEnum
     * @param type
     * @return
     */
    Date getUpdateBaseDate(String joinCity, ShebaoTypeEnum shebaoTypeEnum, String type);

    /**
     * 批量更新员工公积金社保的社保通返回状态
     * @param type
     * @param status
     * @param companyShebaoOrderId
     * @param list
     * @return
     */
    int updateSbAndGjjStateBatch(int type,int status,  long companyShebaoOrderId,List<Long> list);

    /**
     * 查询当前缴纳人数
     * @param shebaoTypeEnum
     * @param memberCompanyId
     * @return
     */
    int selectKeepCustomerCount(ShebaoTypeEnum shebaoTypeEnum, Long memberCompanyId);

    /**
     * 根据公司查询缴纳地区
     * @param companyId
     * @return
     */
    List<Map> selectJoinCityByCompanyId(long companyId);

    Map selectBaseInfo(Long customerId);

    /**
     * 查询当前公司缴纳的社保和公积金
     * @param companyId
     * @return
     */
    List<Map> selectKeepSbAndGjjType(long companyId);
}
