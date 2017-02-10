package com.xtr.core.service.company;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanySalaryExcelBean;
import com.xtr.api.service.company.CompanySalaryExcelService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.core.persistence.reader.company.CompanySalaryExcelReaderMapper;
import com.xtr.core.persistence.writer.company.CompanySalaryExcelWriterMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>企业上传工资文档</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/17 10:15
 */
@Service("companySalaryExcelService")
public class CompanySalaryExcelServiceImpl implements CompanySalaryExcelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanySalaryExcelServiceImpl.class);

    @Resource
    private CompanySalaryExcelWriterMapper companySalaryExcelWriterMapper;

    @Resource
    private CompanySalaryExcelReaderMapper companySalaryExcelReaderMapper;


    /**
     * 根据指定主键获取一条数据库记录,company_salary_excel
     *
     * @param excelId
     */
    public CompanySalaryExcelBean selectByPrimaryKey(Long excelId) {
        return companySalaryExcelReaderMapper.selectByPrimaryKey(excelId);
    }


    /**
     * 新写入数据库记录,company_salary_excel
     *
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Long insert(CompanySalaryExcelBean companySalaryExcelBean) throws BusinessException {
        companySalaryExcelBean.setExcelAddtime(new Date());
        int result = companySalaryExcelWriterMapper.insert(companySalaryExcelBean);
        if (result <= 0) {
            throw new BusinessException("工资单上传失败");
        }
        return companySalaryExcelBean.getExcelId();
    }

    /**
     * 分页查询
     *
     * @param companySalaryExcelBean
     * @return
     */
    public ResultResponse selectPageList(CompanySalaryExcelBean companySalaryExcelBean) {
        ResultResponse resultResponse = new ResultResponse();
        PageBounds pageBounds = new PageBounds(companySalaryExcelBean.getPageIndex(), companySalaryExcelBean.getPageSize());
        List list = companySalaryExcelReaderMapper.selectPageList(companySalaryExcelBean, pageBounds);
        resultResponse.setData(list);
        Paginator paginator = ((PageList) list).getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setSuccess(true);
        LOGGER.info("返回结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }


    /**
     * 根据主键删除数据库的记录,company_salary_excel
     *
     * @param excelId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int deleteByPrimaryKey(Long excelId) throws BusinessException {
        if (excelId != null) {
            CompanySalaryExcelBean companySalaryExcelBean = companySalaryExcelReaderMapper.selectByPrimaryKey(excelId);
            if (companySalaryExcelBean != null) {
                int result = companySalaryExcelWriterMapper.deleteByPrimaryKey(excelId);
                if (result <= 0) {
                    throw new BusinessException("工资单撤销失败");
                }
                return result;
            } else {
                throw new BusinessException("该工资单不存在");
            }
        } else {
            throw new BusinessException("参数不能为空");
        }
    }

    /**
     * 根据企业id删除
     *
     * @param companyId
     * @return
     * @throws BusinessException
     */
    @Override
    public int deleteByCompanyId(Long companyId) throws BusinessException {
        if (null == companyId) {
            throw new BusinessException("企业id为空");
        }
        try {
            return companySalaryExcelWriterMapper.deleteByCompanyId(companyId);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 获取工资单
     *
     * @return
     * @throws BusinessException
     */
    public List<CompanySalaryExcelBean> getPendingPayroll(String sameDay, Integer excelGrantState) throws BusinessException {
        if (StringUtils.isNotBlank(sameDay) && excelGrantState != null) {
            return companySalaryExcelReaderMapper.getPendingPayroll(sameDay, excelGrantState);
        } else {
            throw new BusinessException("参数不能为空");
        }
    }


    /**
     * 更新工资单状态
     *
     * @param excelId
     * @param excelGrantState
     * @param oldGrantState
     * @return
     */
    public int updateGrantState(Long excelId, Integer excelGrantState, Integer oldGrantState) {
        return companySalaryExcelWriterMapper.updateGrantState(excelId, excelGrantState, oldGrantState);
    }

    /**
     * 更新工资单实发金额  状态必须为发放中
     *
     * @param excelId
     * @param amount
     * @return
     */
    public int updateSalaryTotal(Long excelId, BigDecimal amount) {
        return companySalaryExcelWriterMapper.updateSalaryTotal(excelId, amount);
    }

    /**
     * 根据企业编号获取发工资信息
     *
     * @param companySalaryExcelBean
     * @return
     */
    public ResultResponse selectSalaryPageList(CompanySalaryExcelBean companySalaryExcelBean) {
        ResultResponse resultResponse = new ResultResponse();
        PageBounds pageBounds = new PageBounds(companySalaryExcelBean.getPageIndex(), companySalaryExcelBean.getPageSize());
        List list = companySalaryExcelReaderMapper.selectPageList(companySalaryExcelBean, pageBounds);
        resultResponse.setData(list);
        Paginator paginator = ((PageList) list).getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setSuccess(true);
        LOGGER.info("根据企业编号获取发工资信息返回结果：" + JSON.toJSONString(resultResponse));
        return resultResponse;
    }


    /**
     * 根据月份查询工资单
     *
     * @param companyId
     * @param month
     * @return
     */
    public CompanySalaryExcelBean selectSalaryByMonth(Long companyId, Integer month, Integer year) {
        return companySalaryExcelReaderMapper.selectSalaryByMonth(companyId, month, year);
    }
}
