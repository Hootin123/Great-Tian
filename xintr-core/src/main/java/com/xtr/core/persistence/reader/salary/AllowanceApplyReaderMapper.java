package com.xtr.core.persistence.reader.salary;

import com.xtr.api.domain.salary.AllowanceApplyBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AllowanceApplyReaderMapper {

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    AllowanceApplyBean selectByPrimaryKey(Long id);

    /**
     * 根据津贴id查询津贴适用人员
     *
     * @param allowanceId
     * @return
     */
    List<AllowanceApplyBean> selectList(@Param("allowanceId") Long allowanceId);

}