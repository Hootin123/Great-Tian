package com.xtr.core.persistence.writer.company;

import com.xtr.api.domain.company.CompanyActivityBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.company.FileResourcesBean;
import com.xtr.api.dto.hongbao.LastHongbaoDto;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface CompanysWriterMapper {
    /**
     * 根据主键删除数据库的记录,companys
     *
     * @param companyId
     */
    int deleteByPrimaryKey(Long companyId);

    /**
     * 新写入数据库记录,companys
     *
     * @param record
     */
    int insert(CompanysBean record);

    /**
     * 动态字段,写入数据库记录,companys
     *
     * @param record
     */
    int insertSelective(CompanysBean record);

    /**
     * 动态字段获取主键,写入数据库记录,companys
     *
     * @param record
     */
    int insertSelectiveById(CompanysBean record);

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,companys
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CompanysBean record);

    /**
     * 根据主键来更新符合条件的数据库记录,companys
     *
     * @param record
     */
    int updateByPrimaryKey(CompanysBean record);



    /**
     * 新增
     * @param map
     * @return
     */
    int stationCollaborationAdd(Map<String, Object> map);

    /**
     * 更新
     * @param map
     * @return
     */
    int stationCollaborationUpdate(Map<String, Object> map);

    /**
     * 企业协议管理时更改企业相关信息
     * @param companysBean
     * @return
     */
    int updateByProtocolModify(CompanysBean companysBean);

    /**
     * 动态字段获取主键,写入数据库记录,companys
     *
     * @param record
     */
    int insertFileResourcesById(FileResourcesBean record);

    /**
     * 删除资源文件
     *
     * @param id
     */
    int deletefileByPrimaryKey(Long id);

    /**
     * 垫付审核
     * @param cId
     * @param cause
     * @param b
     * @param memberId
     */
    int updatePrepaidStatus(@Param("cId") long cId, @Param("cCause") String cause, @Param("cStatus") boolean b, @Param("memberId") Long memberId);

    /**
     * 更新京东支付状态
     * @param payNumber
     * @param payStatus
     * @param payReason
     * @return
     */
    int updatePayStatus(@Param("payNumber") String payNumber, @Param("payStatus") int payStatus, @Param("payReason") String payReason);

    /**
     * 保存红包领取记录信息
     * @param activity
     * @return
     */
    int saveCompanyActivity(CompanyActivityBean activity);

    /**
     * 更新红包
     * @param updateMap
     * @return
     */
    int updateActivity(Map updateMap);

    /**
     * 新版红包 更新公司的支付宝 姓名等信息
     * @param map
     * @return
     */
    int updateCollectInfo(Map map);

    /**
     * 简化版红包注册页面之前保存手机号
     * @param lastHongbaoDto
     * @return
     */
    int saveCompanyPhone(LastHongbaoDto lastHongbaoDto);
}