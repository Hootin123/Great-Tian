package com.xtr.api.service.shebao;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyShebaoOrderBean;
import com.xtr.api.dto.company.CompanyShebaoOrderDto;
import com.xtr.api.dto.customer.CustomerShebaoOrderDto;
import com.xtr.api.dto.customer.CustomersSupplementDto;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.sbt.api.City;
import com.xtr.comm.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 公司社保订单
 * @Author Xuewu
 * @Date 2016/9/18.
 */
public interface CompanyShebaoService {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    CompanyShebaoOrderBean selectByPrimaryKey(Long id);

    /**
     * 根据公司 和 地区 查询缴纳订单
     * @param companyId
     * @param joinCityCode
     * @return
     */
    CompanyShebaoOrderBean selectLastOrderByCompanyAndCity(long companyId, String joinCityCode);

    /**
     * 查询账单数据
     *
     * @param companyId
     * @param city  城市编码
     * @param is_current  类型, 1:当前账单 0:历史账单
     * @return
     */
    List<CompanyShebaoOrderDto> getPayOrder(Long companyId, String city, int is_current);


    /**
     * 根据当前城市缴纳信息创建企业订单
     * @param companyId
     * @param city
     * @param cityCode
     * @return
     */
    CompanyShebaoOrderBean createCompanyShebaoOrder(long companyId, City city, String cityCode) throws BusinessException;


    /**
     * 获取当前城市缴纳信息  没有则创建订单
     * @param companyId
     * @param city
     * @param cityCode
     * @return
     * @throws BusinessException
     */
    CompanyShebaoOrderBean getCompanyShebaoOrderByCity(long companyId, City city, String cityCode) throws BusinessException;

    /**
     * 根据主键查询社保订单
     * @param id
     * @return
     */
    CompanyShebaoOrderBean getCompanyShebaoOrderById(Long id);

    /**
     * 查询当前账单个数
     * @param comapnyId
     * @param status
     * @return
     */
    int getCompanyShebaoOrderCount(Long comapnyId, int status);

    /**
     * 提交缴纳订单
     * @param id
     * @return
     */
    ResultResponse updateSubmitOrder(Long id, String type,Long companyId)throws BusinessException,Exception ;

    /**
     * 查询企业账单所有地区
     * @param comapnyId
     * @return
     */
    List<CompanyShebaoOrderBean> getOrderCities(Long comapnyId);

    /**
     * 查询当前账单列表
     * @param companyId
     * @param city
     * @param is_current
     * @return
     */
    List<CompanyShebaoOrderDto> selectCurrentOrderList(Long companyId, String city, int is_current,String flag);

    /**
     * 查询当前账单数量
     * @param companyId
     * @return
     */
    int selectCurrentOrderCount(Long companyId);

    /**
     * 更新企业订单详情内容  人数之类
     * @param companyOrderId
     * @return
     */
    boolean updateOrderDetail(long companyOrderId);

    /**
     * 获取补收补退详细信息
     * @param supplementCompanyOrderId
     * @return
     */
    CustomersSupplementDto queryAddtionDetail(Long supplementCompanyOrderId);

    /**
     * 查询历史账单
     * @param map
     * @return
     */
    ResultResponse queryHistoryPaybills(Map map);

    /**
     * 查看差额详情,获取详细信息
     * @param supplementCompanyOrderId
     * @param supplementCustomerId
     * @return
     */
    List<CustomersSupplementDto> customerAddtionDetail(Long supplementCompanyOrderId,Long supplementCustomerId,String payMonth,Integer type);

    /**
     * companyId  joinCityCode  orderDate
     * @param map
     * @return
     */
    CompanyShebaoOrderBean selectShebaoOrderByMap(Map map);

    /**
     * 取消订单
     * @param orderStr
     * @return
     * @throws BusinessException
     * @throws Exception
     */
    ResultResponse cancelOrder(String orderStr)throws BusinessException,Exception;

    /**
     * 根据过滤条件获取所有用户财务付款的订单(分页)
     * @param companyShebaoOrderDto
     * @return
     */
    ResultResponse selectPayInfoByCondition(CompanyShebaoOrderDto companyShebaoOrderDto);

    /**
     * 根据过滤条件获取所有用户财务未付款的订单(分页)
     * @return
     */
    ResultResponse selectPayInfoNoPayByCondition(CompanyShebaoOrderDto companyShebaoOrderDto);

    /**
     * 获取所有用户财务付款的订单数量
     * @return
     */
    int selectPayInfoCount();

    /**
     * 获取所有用户财务未付款的订单数量
     * @return
     */
    int selectPayInfoNoPayCount();

    /**
     * 根据过滤条件获取所有用户财务付款的订单(不分页)
     * @param companyShebaoOrderDto
     * @return
     */
    List<CompanyShebaoOrderDto> selectPayInfoByConditionNoPage(CompanyShebaoOrderDto companyShebaoOrderDto);

    /**
     * 根据所有财务付款的企业信息
     * @return
     */
    List<CompanyShebaoOrderDto> selectPayInfoWithCompanyInfo();

    /**
     * 添加社保公积金付款信息
     * @param companyShebaoOrderDto
     * @param memberId
     */
    void addCompanyOrderPayInfo(CompanyShebaoOrderDto companyShebaoOrderDto,Long memberId,String webRoot)throws BusinessException,Exception;

    /**
     * 更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(CompanyShebaoOrderBean record);

    /**
     * 提交账单生成订单
     * @param companyId
     * @param companyOrderId
     * @param priceSumDecimal
     */
    void generateSalaryOrder(Long companyId,Long companyOrderId,BigDecimal priceSumDecimal);

    boolean updateOrderStatus(String orderNumber, int status);

    /**
     * 查询员工社保公积金信息
     * @param customerId
     * @return
     */
    ResultResponse searchInfoDetails(Long customerId) throws Exception;

    /**
     * 删除没有员工需求的企业订单
     * @param id
     * @return
     */
    boolean deleteCompanyOrderIfEmpty(Long id);

    /**
     * 后台管理用   查询所有的社保公积金订单
     * @param map
     * @return
     */
    ResultResponse selectAllOrdersByMap(Map<String, Object> map) throws BusinessException;

    /**
     * 查询该公司未付款账单数量
     * @param companyId
     * @return
     */
    long selectUnpayOrders(long companyId);

    /**
     * 查询所有当前的未提交的账单
     * @param companyId
     * @return
     */
    List<CompanyShebaoOrderDto> selectLastOrder(long companyId);
}
