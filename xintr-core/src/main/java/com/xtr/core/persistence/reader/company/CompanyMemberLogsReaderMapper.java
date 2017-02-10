package com.xtr.core.persistence.reader.company;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.domain.company.CompanyMemberLogsBean;
import com.xtr.api.dto.company.CompanyMemberLogDto;
import org.apache.ibatis.annotations.Param;

public interface CompanyMemberLogsReaderMapper {

    /**
     * 根据主键查询操作日志
     *
     * @param logId
     * @return
     */
    CompanyMemberLogsBean selectByPrimaryKey(Long logId);

    /**
     * 分页查询企业员工操作日志
     *
     * @param uname
     * @param date_str
     * @param pageBounds
     * @return
     */
    PageList<CompanyMemberLogDto> selectPage(@Param("memberId") Long memberId,@Param("uname") String uname,@Param("dateStr") String date_str, PageBounds pageBounds);
}