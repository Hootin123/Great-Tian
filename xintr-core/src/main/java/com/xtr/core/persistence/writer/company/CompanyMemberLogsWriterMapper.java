package com.xtr.core.persistence.writer.company;

import com.xtr.api.domain.company.CompanyMemberLogsBean;

public interface CompanyMemberLogsWriterMapper {

    /**
     * 根据主键删除
     *
     * @param logId
     * @return
     */
    int deleteByPrimaryKey(Long logId);

    /**
     * 插入到数据库
     *
     * @param record
     * @return
     */
    int insert(CompanyMemberLogsBean record);

    /**
     * 插入到数据库
     *
     * @param record
     * @return
     */
    int insertSelective(CompanyMemberLogsBean record);


    /**
     * 根据主键更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(CompanyMemberLogsBean record);
}