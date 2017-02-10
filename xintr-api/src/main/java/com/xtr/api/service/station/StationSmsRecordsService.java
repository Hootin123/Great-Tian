package com.xtr.api.service.station;

import com.xtr.api.domain.station.StationSmsRecordsBean;

import java.util.Date;

/**
 * Created by abiao on 2016/7/4.
 */
public interface StationSmsRecordsService {
    /*
    * <p>查询指定手机号，规定时间内发送的短信验证码条数</p>
    * @auther 何成彪
    * @createTime2016/7/4 15:33
    */
    long getCountByCondition(String recordPhone, String payday);

    /*
    * <p>增加手机验证短息信息</p>
    * @auther 何成彪
    * @createTime2016/7/4 17:05
    */
    int insert(StationSmsRecordsBean record);

}
