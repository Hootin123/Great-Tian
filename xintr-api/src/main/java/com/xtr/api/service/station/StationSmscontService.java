package com.xtr.api.service.station;

import com.xtr.api.domain.station.StationSmscontBean;

/**
 * Created by abiao on 2016/7/4.
 */
public interface StationSmscontService {

    /*
    * <p>根据主键进行查询</p>
    * @auther 何成彪
    * @createTime2016/7/4 14:30
    */
    StationSmscontBean selectByPrimaryKey(int primarykey);

    /**
     * 根据短信类型获取短信模板
     * @param smsType
     * @return
     */
    StationSmscontBean selectBySmsType(Integer smsType);

}
