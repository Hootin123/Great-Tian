package com.xtr.core.persistence.reader.company;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.xtr.api.domain.company.CompanyShebaoOrderBean;
import com.xtr.api.dto.company.CompanyShebaoOrderDto;
import com.xtr.api.dto.customer.CustomerShebaoOrderDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CompanyShebaoOrderReaderMapper {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    CompanyShebaoOrderBean selectByPrimaryKey(Long id);

    /**
     * 查询账单数据
     * @param city  城市编码
     * @param is_current  类型, 1:当前账单 0:历史账单
     * @return
     */
    List<CompanyShebaoOrderDto> selectPayOrder(@Param("company_id") Long companyId, @Param("city") String city, @Param("isCurrent") int is_current);

    /**
     * 查询指定月份企业订单
     * @param companyId
     * @param city
     * @param month
     * @return
     */
    CompanyShebaoOrderBean selectOrderByCityAndMonth(@Param("pCompanyId") Long companyId, @Param("pCity") String city, @Param("pMonth") String month);

    /**
     * 查询当前账单个数
     * @param comapnyId
     * @param status
     * @return
     */
    int selectCompanyShebaoOrderCount(@Param("comapnyId") Long comapnyId, @Param("status") int status);

    /**
     * 查询企业账单所有地区
     * @param comapnyId
     * @return
     */
    List<CompanyShebaoOrderBean> selectOrderCities(@Param("comapnyId") Long comapnyId);

    /**
     * 查询当前账单列表
     * @param companyId
     * @param city
     * @param is_current
     * @return
     */
    List<CompanyShebaoOrderDto> selectCurrentOrderList(@Param("company_id") Long companyId, @Param("city") String city, @Param("isCurrent") int is_current,@Param("flag") String flag);

    /**
     * 查询当前账单数量
     * @param companyId
     * @return
     */
    int selectCurrentOrderCount(@Param("company_id") Long companyId);

    /**
     * 查询所有已过需求窗口期 并且 处于订单提交期的 订单
     * @return
     */
    List<CompanyShebaoOrderBean> selectOrderTimeOrder();

    /**
     * 查询所有已过社保通截止日的当前订单
     * @return
     */
    List<CompanyShebaoOrderBean> selectTimeOutOrder();

    /**
     * 查询历史账单
     * @param map
     * @return
     */
    List<CompanyShebaoOrderDto> selectHistoryBills(Map map);

    /**
     * 查询社保订单是否提交
     * @param map
     * @return
     */
    CompanyShebaoOrderBean selectShebaoOrderByMap(Map map);

    /**
     * 根据订单编号查询订单
     * @param orderNumber
     * @return
     */
    CompanyShebaoOrderBean selectByOrderNumber(String orderNumber);

    /**
     * 根据过滤条件获取所有用户财务付款的订单(分页)
     * @param companyShebaoOrderDto
     * @return
     */
    List<CompanyShebaoOrderDto> selectPayInfoByCondition(CompanyShebaoOrderDto companyShebaoOrderDto,PageBounds pageBounds);

    /**
     * 根据过滤条件获取所有用户财务未付款的订单
     * @return
     */
    List<CompanyShebaoOrderDto> selectPayInfoNoPayByCondition(PageBounds pageBounds);

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
     * 根据过滤条件获取所有用户财务付款的订单
     * @param companyShebaoOrderDto
     * @return
     */
    List<CompanyShebaoOrderDto> selectPayInfoByCondition(CompanyShebaoOrderDto companyShebaoOrderDto);

    /**
     * 根据所有财务付款的企业信息
     * @return
     */
    List<CompanyShebaoOrderDto> selectPayInfoWithCompanyInfo();

    /**
     * 查询所有的社保公积金订单(后台管理使用)
     * @param map
     * @return
     */
    List<CompanyShebaoOrderDto> selectAllOrdersByMap(Map<String, Object> map);

    /**
     * 查询当前未读的账单
     * @param companyId
     * @return
     */
    Long selectNoReadOrderCount(Long companyId);

    /**
     * 查询当前公司未付款的账单数量
     * @param companyId
     * @return
     */
    long selectUnpayOrders(long companyId);

    /**
     * 查询所有的未提交账单
     * @param companyId
     * @return
     */
    List<CompanyShebaoOrderDto> selectLastOrder(long companyId);
}