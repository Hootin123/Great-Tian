package com.xtr.core.service.salary;

import com.xtr.api.domain.salary.RapidlyPayrollBean;
import com.xtr.api.domain.salary.RapidlyPayrollExcelBean;
import com.xtr.api.service.salary.RapidlyPayrollExcelService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.core.persistence.reader.salary.RapidlyPayrollExcelReaderMapper;
import com.xtr.core.persistence.writer.salary.RapidlyPayrollExcelWriterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/10/28 10:11
 */
@Service("rapidlyPayrollExcelService")
public class RapidlyPayrollExcelServiceImpl implements RapidlyPayrollExcelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RapidlyPayrollExcelServiceImpl.class);

    @Resource
    private RapidlyPayrollExcelWriterMapper rapidlyPayrollExcelWriterMapper;

    @Resource
    private RapidlyPayrollExcelReaderMapper rapidlyPayrollExcelReaderMapper;

    /**
     * 根据指定主键获取一条数据库记录,rapidly_payroll_excel
     *
     * @param id
     */
    public RapidlyPayrollExcelBean selectByPrimaryKey(Long id) {
        return rapidlyPayrollExcelReaderMapper.selectByPrimaryKey(id);
    }

    /**
     * 新写入数据库记录,rapidly_payroll_excel
     *
     * @param record
     */
    public RapidlyPayrollExcelBean insert(RapidlyPayrollExcelBean record) {
        int result = rapidlyPayrollExcelWriterMapper.insert(record);
        if (result <= 0) {
            throw new BusinessException("新增工资发放记录失败");
        }
        return record;
    }

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,rapidly_payroll_excel
     *
     * @param record
     */
    public int updateByPrimaryKeySelective(RapidlyPayrollExcelBean record) {
        int result = rapidlyPayrollExcelWriterMapper.updateByPrimaryKeySelective(record);
        if (result <= 0) {
            throw new BusinessException("更新工资发放记录失败");
        }
        return result;
    }

    /**
     * 更新急速发工资工资发放状态为已发放
     *
     * @param id
     * @return
     */
    public RapidlyPayrollExcelBean updateRapidlyPayOff(Long id) {
        RapidlyPayrollExcelBean rapidlyPayrollExcelBean = selectByPrimaryKey(id);
        //0:未发放 1:待发放  2:发放中  3:已发放
        rapidlyPayrollExcelBean.setGrantState(2);
        int result = updateByPrimaryKeySelective(rapidlyPayrollExcelBean);
        if (result <= 0)
            throw new BusinessException("更新急速发工资工资发放状态失败");
        return rapidlyPayrollExcelBean;
    }

}
