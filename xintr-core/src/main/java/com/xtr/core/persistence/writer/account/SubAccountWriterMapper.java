package com.xtr.core.persistence.writer.account;

import com.xtr.api.domain.account.SubAccountBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SubAccountWriterMapper {
    /**
     *  根据主键删除数据库的记录,sub_account
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  新写入数据库记录,sub_account
     *
     * @param record
     */
    int insert(SubAccountBean record);

    /**
     *  动态字段,写入数据库记录,sub_account
     *
     * @param record
     */
    int insertSelective(SubAccountBean record);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,sub_account
     *
     * @param record
     */
    int updateByPrimaryKeySelective(SubAccountBean record);

    /**
     *  根据主键来更新符合条件的数据库记录,sub_account
     *
     * @param record
     */
    int updateByPrimaryKey(SubAccountBean record);

    /**
     * 更新账户余额自动控制
     *
     * @param custId
     * @param property
     * @param isAutoTransfer
     * @return
     */
    int updateAutoTransfer(@Param("custId") Long custId, @Param("property") int property, @Param("isAutoTransfer") int isAutoTransfer);

    /**
     * 批量插入员工账户
     * @param list
     * @return
     */
    int insertBatch(List<SubAccountBean> list);

    /**
     * 根据custId查询账户详情
     *
     * @param custId
     * @param property
     * @return
     */
    SubAccountBean selectByCustId(@Param("custId") Long custId, @Param("property") Integer property);

    /**
     * 根据SubAccountBean查询账户信息
     *
     * @param subAccountBean
     * @return
     */
    SubAccountBean selectSubAccountBean(SubAccountBean subAccountBean);
}