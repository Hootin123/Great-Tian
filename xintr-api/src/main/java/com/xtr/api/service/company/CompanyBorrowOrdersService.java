package com.xtr.api.service.company;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.BorrowInfoBean;
import com.xtr.api.domain.company.CompanyBorrowOrdersBean;
import com.xtr.api.dto.company.CompanyPayWithDto;
import com.xtr.comm.basic.BusinessException;

import java.util.List;

/**
 * <p>企业借款订单</p>
 *
 * @author 任齐
 * @createTime: 2016/6/28 12:05
 */
public interface CompanyBorrowOrdersService {

    /**
     * 根据主键查询数据库记录
     * @param orderId
     * @return
     */
	CompanyBorrowOrdersBean selectByPrimaryKey(Long orderId);

    /**
     * 查询企业借款列表
     *
     * @param companyBorrowOrdersBean
     * @return
     */
    List<CompanyBorrowOrdersBean> selectList(CompanyBorrowOrdersBean companyBorrowOrdersBean);
    
    /**
     * 根据借款订单id查询借款详情
     * @param orderId 企业借款订单id
     */
    CompanyPayWithDto selectCompanyBorrowOrderDetail(Long orderId) throws BusinessException;
    
    /**
     * 分页查询
     * 
     * @param companyBorrowOrdersBean
     */
    ResultResponse selectPageList(CompanyBorrowOrdersBean companyBorrowOrdersBean);

    /**
     * 新写入数据库记录,company_borrow_orders
     *
     * @param companyBorrowOrdersBean
     */
    int addBorrowOrder(CompanyBorrowOrdersBean companyBorrowOrdersBean) throws BusinessException;

    /**
     * 分页查询
     *
     * @param companyBorrowOrdersBean
     */
    ResultResponse selectPageBorrowOrdersList(CompanyBorrowOrdersBean companyBorrowOrdersBean, int pageIndex, int pageSize);

    /**
     * 分页查询
     *
     * @param companyBorrowOrdersBean
     */
    ResultResponse selectPageListAll(CompanyBorrowOrdersBean companyBorrowOrdersBean);

    /**
     * 新写入数据库记录,borrow_info
     *
     * @param borrowInfoBean
     */
    int addBorrowInfoBean(BorrowInfoBean borrowInfoBean) throws BusinessException;

    /**
     * 查询借款信息列表
     *
     * @param borrowInfoBean
     * @return
     */
    List<BorrowInfoBean> selectBorrowInfoBeanList(BorrowInfoBean borrowInfoBean);

    /**
     * 修改垫付信息
     *
     * @param companyBorrowOrdersBean
     * @return
     */
    void updateCompanyBorrowOrdersBeanId(CompanyBorrowOrdersBean companyBorrowOrdersBean) throws BusinessException;

    /**
     * 根据订单编号查询
     * @param orderNumber
     * @return
     */
    CompanyBorrowOrdersBean selectByOrderNumber(String orderNumber);
}
