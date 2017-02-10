package com.xtr.core.persistence.writer.salary;

import com.xtr.api.domain.salary.AllowanceApplyBean;
import org.apache.ibatis.annotations.Param;

public interface AllowanceApplyWriterMapper {
    /**
     * 根据主键删除
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 插入一条数据
     *
     * @param record
     * @return
     */
    int insert(AllowanceApplyBean record);

    /**
     * 插入一条数据
     *
     * @param record
     * @return
     */
    int insertSelective(AllowanceApplyBean record);

    /**
     * 根据津贴id删除
     *
     * @param allowanceId
     */
    int deleteByAllowanceId(@Param("allowanceId") Long allowanceId);
}