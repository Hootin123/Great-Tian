package com.xtr.core.persistence.reader.salary;

import com.xtr.api.domain.salary.AllowanceSettingBean;
import com.xtr.api.dto.salary.AllowanceSettingDto;

import java.util.List;

public interface AllowanceSettingReaderMapper {

    /**
     *  根据指定主键获取一条数据库记录,allowance_setting
     *
     * @param id
     */
    AllowanceSettingBean selectByPrimaryKey(Long id);

    /**
     * 查询列表
     *
     * @param allowanceSettingBean
     * @return
     */
    List<AllowanceSettingBean> selectList(AllowanceSettingBean allowanceSettingBean);

    /**
     * 根据企业ID获取津贴信息
     * @param companyId
     * @return
     */
    List<AllowanceSettingDto> selectByCompanyId(Long companyId);
}