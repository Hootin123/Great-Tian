package com.xtr.api.service.shebao;

import com.xtr.api.domain.company.CompanyShebaoOrderBean;
import com.xtr.api.domain.customer.CustomerShebaoBean;
import com.xtr.api.domain.customer.CustomerShebaoOrderBean;
import com.xtr.api.dto.customer.CustomerShebaoOrderDto;
import com.xtr.api.dto.shebao.CustomerShebaoSumDto;
import com.xtr.api.dto.shebao.JrOrderDto;
import com.xtr.api.dto.shebao.ResultSheBaoDto;
import com.xtr.api.dto.shebao.TJShebaoDto;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.enums.ShebaoTypeEnum;
import com.xtr.comm.sbt.api.SocialBase;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import java.util.List;
import java.util.Map;

/**
 * 员工社保订单
 * @Author Xuewu
 * @Date 2016/9/19.
 */
public interface CustomerShebaoOrderService {


    /**
     * 缴纳社保
     * @param dto
     * @return
     */
    boolean jrShebao(JrOrderDto dto, long companyId) throws BusinessException;


    /**
     * 缴纳公积金
     * @param dto
     * @param loginCompanyId
     * @return
     * @throws BusinessException
     */
    boolean jrGjj(JrOrderDto dto, Long loginCompanyId) throws BusinessException;

    /**
     * 更新社保订单汇总信息
     * @param companyOrderId
     * @param customerId
     * @param shebaoTypeEnum
     * @param orderType
     */
    void updateCustomerOrderDesc(long companyOrderId, long customerId, ShebaoTypeEnum shebaoTypeEnum, int orderType, String month);

    /**
     * 获取员工社保失败信息
     * @param customerId
     * @param companyShebaoOrderId
     * @return
     */
    Map<Integer, List> getOrderFaildMsg(long customerId, long companyShebaoOrderId);

    /**
     * 获取社保订单
     * @param memerId
     * @param orderId
     * @return
     */
    CustomerShebaoOrderDto getShebaoOrder(Long memerId, Long orderId, int requireType);

    /**
     * 补缴
     * @param list
     * @param cityCode
     * @param type
     * @param companyShebaoOrderId
     * @param customerId
     * @param sheBaoType
     */
    void bj(List<JrOrderDto> list, String cityCode, String type, long companyShebaoOrderId, long customerId, ShebaoTypeEnum sheBaoType);

    /**
     * 清除补缴
     * @param companyShebaoOrderId
     * @param customerId
     * @param sheBaoType
     */
    void cleanBj(long companyShebaoOrderId, long customerId, ShebaoTypeEnum sheBaoType);

    /**
     * 停缴
     * @param dto
     * @param stopDataParam
     * @return
     * @throws BusinessException
     */
    CompanyShebaoOrderBean stopPayment(TJShebaoDto dto,String stopDataParam) throws Exception;

    /**
     * 调基
     * @param dto
     * @param adjustAmount
     * @return
     * @throws BusinessException
     */
    CompanyShebaoOrderBean adjustBase(TJShebaoDto dto,BigDecimal adjustAmount) throws Exception;

    /**
     * 批量缴纳
     * @param dto
     * @param companyId
     * @throws BusinessException
     * @throws Exception
     */
    void batchPayment(TJShebaoDto dto,Long companyId)throws Exception;

    /**
     * 查询企业异常订单数
     * @param map
     * @return
     */
    long getErrorOrderCount(Map map);

    /**
     * 查询订单失败列表
     * @param orderId
     * @return
     */
    List<CustomerShebaoOrderDto> getShebaoErrorOrder(Long orderId);


    /**
     * 清楚当前企业订单下其他需求订单
     * @param customerId
     * @param companyOrderId
     * @param requireType
     * @param orderType
     * @return
     */
    int cleanOrderByCustomerIdAndCompanyOrderId(long customerId, long companyOrderId, int requireType, Object... orderType);

    /**
     * 创建需求订单
     * @param customerId
     * @param orderType
     * @param companyOrderId
     * @param shebaoTypeEnum
     * @param base
     * @param socialBase
     * @return
     */
    CustomerShebaoOrderBean createOrder(long customerId, int orderType, long companyOrderId, ShebaoTypeEnum shebaoTypeEnum, Date orderMonth, BigDecimal base, SocialBase socialBase);

    /**
     * 社保公积金停缴验证
     * @param dto
     * @throws BusinessException
     * @throws Exception
     */
   //void paymentValidator(TJShebaoDto dto)throws Exception;

    /**
     *公积金取消停缴验证
     * @param dto
     * @return
     * @throws BusinessException
     * @throws Exception
     */
    boolean cancelGjjValidator(TJShebaoDto dto)throws Exception;

    /**
     * 社保公积金调基验证
     * @param dto
     * @throws BusinessException
     * @throws Exception
     */
    TJShebaoDto adjustBaseValidator(TJShebaoDto dto)throws Exception;

    /**
     * 更新企业订单详情
     * @param companyShebaoOrderBean
     * @return
     */
    boolean isNeedUpdateCompanyOrderDetail(CompanyShebaoOrderBean companyShebaoOrderBean);

    /**
     * 验证当前地区是否可以提交员工订单
     * @param companyShebaoOrderBean
     * @return
     */
    boolean validateOrderDate(CompanyShebaoOrderBean companyShebaoOrderBean);
    /**
     * 批量更新员工订单的社保通状态
     * @param sbtStatus
     * @param companyShebaoOrderId
     * @param list
     * @return
     */
    int updateSbtStateBatch(int sbtStatus, long companyShebaoOrderId,List<Long> list);

    /**
     * 根据周期获取所有员工的社保公积金扣除金额
     * @param payCycleId
     * @return
     */
    Map<Long, CustomerShebaoSumDto> getCustomerShebaoBase(Long payCycleId);


    /**
     * 社保通 回调 更新员工需求状态
     * @param idCard
     * @param orderNumber
     * @param status
     * @param orderMonth
     * @param comment
     * @return
     */
    boolean updateShebaotongStatus(String idCard, String orderNumber, ShebaoTypeEnum shebaoTypeEnum, int status, Date orderMonth, String comment) throws ParseException;

    /**
     * 社保公积金停缴的基础验证
     * @param dto
     */
    ResultSheBaoDto validateStopPaymentBase(TJShebaoDto dto,String string)throws BusinessException,Exception;

    /**
     * 根据传入开始结束日期 计算公积金补缴月份
     * @param obmonth
     * @param oemonth
     * @return
     */
    SocialBase getGjjBjMonth(Date obmonth, Date oemonth, long customerId);

    /**
     *判断是否有异常
     * @param orderId
     * @return
     */
    List<CustomerShebaoOrderDto> selectShebaoErrorOrderForNew(Long orderId);

    /**
     * 付款成功后更新社保通订单的状态
     * @return
     */
    int updateSbtStateForPayment(CustomerShebaoOrderBean customerShebaoOrderBean);

    /**
     * 查询员工已有社保补缴数据
     * @param customerId
     * @param currentCompanyOrderId
     * @param shebaoTypeEnum
     * @return
     */
    Map selectExistBjData(long customerId, Long currentCompanyOrderId, ShebaoTypeEnum shebaoTypeEnum);

    /**
     * 查询补缴月份
     * @param customerId
     * @param currentCompanyOrderId
     * @param shebaoTypeEnum
     * @return
     */
    List<Date> selectBjDate(long customerId, Long currentCompanyOrderId, ShebaoTypeEnum shebaoTypeEnum);

    /**
     * 查询该公司异常订单下所有的员工id
     * @param companyShebaoOrderId
     * @return
     */
    List<Map<String,Object>> selectErrorCustomerIdList(Long companyShebaoOrderId);

    /**
     * 社保公积金停缴的基础验证
     * @param dto
     */
    ResultSheBaoDto validateStopPaymentBase(TJShebaoDto dto)throws BusinessException,Exception;

    /**
     * 重设基数
     * @param socialBase
     * @param customerShebaoBean
     * @param shebaoTypeEnum
     * @return
     */
    BigDecimal resetBase(SocialBase socialBase, CustomerShebaoBean customerShebaoBean, ShebaoTypeEnum shebaoTypeEnum);

    /**
     * 社保公积金停缴
     * @param dto
     * @return
     * @throws BusinessException
     */
    CompanyShebaoOrderBean cancelStopPayment(TJShebaoDto dto) throws BusinessException,Exception;

    /**
     * 根据企业订单ID获取社保公积金订单
     * @param companyShebaoOrderId
     * @return
     */
    List<CustomerShebaoOrderBean> selectByCompanyOrderId(Long companyShebaoOrderId);

    /**
     * 查询当前公司未读账单的数量
     * @param companyId
     * @return
     */
    public Long selectNoReadOrderCount(Long companyId) throws Exception;

    /**
     * 更新当前公司未读账单状态为已读
     * @param companyId
     */
    public void updateNoReadOrders(Long companyId) throws Exception;

    /**
     * 获取当前月
     * @param customerId
     * @param companyShebaoOrderId
     * @return
     */
    Map<Integer, List> getCurrentOrderFaildMsg(long customerId, long companyShebaoOrderId);


    /**
     * 获取当前月社保补交信息
     * @param companyShebaoOrderId
     * @return
     */
    List<Map<String,Object>> getOverdueDetail(Long companyShebaoOrderId,Long customerId,Integer requireType);
}



