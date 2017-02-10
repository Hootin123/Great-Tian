package com.xtr.core.persistence.reader.company;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.basic.QueryParam;
import com.xtr.api.dto.company.CompanyRechargeDto;
import com.xtr.api.domain.company.CompanyRechargesBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyRechargesReaderMapper {

    /**
     * 根据指定主键获取一条数据库记录,company_recharges
     *
     * @param rechargeId
     */
    CompanyRechargesBean selectByPrimaryKey(Long rechargeId);

    /**
     * 查询企业充值提现记录
     *
     * @param companyRechargesBean
     * @return
     */
    CompanyRechargesBean selectCompanyRecharge(CompanyRechargesBean companyRechargesBean);

    /**
     * 分页查询充值提现发工资
     *
     * @param queryParam
     * @param pageBounds
     * @return
     */
    PageList<CompanyRechargesBean> selectPageList(QueryParam queryParam, PageBounds pageBounds);

    /**
     * 分页查询企业流水
     *
     * @param companyRechargeDto
     * @param pageBounds
     * @return
     */
    PageList<CompanyRechargeDto> selectPageRecords(CompanyRechargeDto companyRechargeDto, PageBounds pageBounds);

    /**
     * 查询企业充值提现
     *
     * @param rechargeId
     * @return
     */
    CompanyRechargeDto selectCompanyRechargeDto(Long rechargeId);

    /**
     * 查询今天的订单号
     *
     * @return
     */
    int getTodayOrderNum(@Param("rechargeType") Integer rechargeType);


    CompanyRechargesBean selectByNumber(@Param("rNubmer") String outTradeNo);

    List<CompanyRechargesBean> query(QueryParam queryParam);

    /**
     * 根据订薪周期查询是否有生成的订单
     * @param companyRechargesBean
     * @return
     */
    List<CompanyRechargesBean> selectByExcelIdAndType(CompanyRechargesBean companyRechargesBean);

    /**
     * 根据订薪周期查询是否存在某个状态的订单
     * @param companyRechargesBean
     * @return
     */
    int selectCountByCondition(CompanyRechargesBean companyRechargesBean);

    /**
     *根据订薪周期查询是否存在某个状态的订单信息
     * @param companyRechargesBean
     * @return
     */
    List<CompanyRechargesBean> selectListByCondition(CompanyRechargesBean companyRechargesBean);
}