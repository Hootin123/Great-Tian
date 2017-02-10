package com.xtr.core.persistence.reader.salary;

import com.xtr.api.domain.salary.BonusSettingsBean;

import java.util.List;

public interface BonusSettingsReaderMapper {

    /**
     *  根据指定主键获取一条数据库记录,bonus_settings
     *
     * @param id
     */
    BonusSettingsBean selectByPrimaryKey(Long id);

    /**
     * 查询列表
     *
     * @param bonusSettingsBean
     * @return
     */
    List<BonusSettingsBean> selectList(BonusSettingsBean bonusSettingsBean);

}