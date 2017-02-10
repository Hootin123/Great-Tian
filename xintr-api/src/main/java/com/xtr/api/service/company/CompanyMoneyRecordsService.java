package com.xtr.api.service.company;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMoneyRecordsBean;
import com.xtr.comm.basic.BusinessException;

/**
 * <p>企业资金变动记录</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/17 10:07
 */
public interface CompanyMoneyRecordsService {

    /**
     * 根据主键查询数据库记录
     * @param recordId
     * @return
     */
    CompanyMoneyRecordsBean selectByPrimaryKey(Long recordId);
    
    /**
     * 分页查询
     *
     * @param companyMoneyRecordsBean
     * @return
     */
    ResultResponse selectPageList(CompanyMoneyRecordsBean companyMoneyRecordsBean);

    /**
     * 保存资金变动记录
     * @param companyMoneyRecordsBean
     * @return
     */
    int addMoneyRecord(CompanyMoneyRecordsBean companyMoneyRecordsBean) throws BusinessException;

    /**
     * 根据企业id删除企业资金变动记录
     *
     * @param companyId
     * @return
     */
    int deleteByCompanyId(Long companyId) throws BusinessException;

}
