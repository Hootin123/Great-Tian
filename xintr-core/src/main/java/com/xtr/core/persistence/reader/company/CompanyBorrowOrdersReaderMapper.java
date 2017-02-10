package com.xtr.core.persistence.reader.company;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.domain.company.BorrowInfoBean;
import com.xtr.api.domain.company.CompanyBorrowOrdersBean;
import org.apache.ibatis.annotations.Param;

public interface CompanyBorrowOrdersReaderMapper {

    /**
     *  根据指定主键获取一条数据库记录,company_borrow_orders
     *
     * @param orderId
     */
    CompanyBorrowOrdersBean selectByPrimaryKey(Long orderId);

    /**
     * 分页查询
     * 
     * @param companyBorrowOrdersBean
     */
    List<CompanyBorrowOrdersBean> selectPageList(CompanyBorrowOrdersBean companyBorrowOrdersBean);

    /**
     * 分页查询借款订单
     *
     * @param companyBorrowOrdersBean
     * @param pageBounds
     * @return
     */
    PageList<CompanyBorrowOrdersBean> selectPageBorrowOrdersList(CompanyBorrowOrdersBean companyBorrowOrdersBean, PageBounds pageBounds);

    /**
     * 分页查询
     *
     * @param companyBorrowOrdersBean
     */
    List<CompanyBorrowOrdersBean> selectPageList(CompanyBorrowOrdersBean companyBorrowOrdersBean, PageBounds pageBounds);

    /**
     * 查询借款信息表
     *
     * @param borrowInfoBean
     */
    List<BorrowInfoBean> selectBorrowInfoBeanList(BorrowInfoBean borrowInfoBean);

    /**
     * 根据订单编号查询
     * @param orderNumber
     * @return
     */
    CompanyBorrowOrdersBean selectByOrderNumber(@Param("orderNumber") String orderNumber);
}