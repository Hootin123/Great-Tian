package com.xtr.core.service.station;

import com.xtr.api.domain.station.StationSmsRecordsBean;
import com.xtr.api.service.station.StationSmsRecordsService;
import com.xtr.core.persistence.reader.station.StationSmsRecordsReaderMapper;
import com.xtr.core.persistence.writer.station.StationSmsRecordsWriterMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by abiao on 2016/7/4.
 */
@Service("stationSmsRecordsService")
public class StationSmsRecordsServiceImpl implements StationSmsRecordsService {

    @Resource
    private StationSmsRecordsReaderMapper stationSmsRecordsReaderMapper;

    @Resource
    private StationSmsRecordsWriterMapper stationSmsRecordsWriterMapper;

    /*
    * <p>查询一定时间范围内注册同一手机号发送的短信条数</p>
    * @auther 何成彪
    * @createTime2016/7/4 16:55
    */
    public long getCountByCondition(String recordPhone, String payday) {
        return stationSmsRecordsReaderMapper.getCountByCondition(recordPhone,payday);
    }

    /*
    * <p>增加手机验证短息信息</p>
    * @auther 何成彪
    * @createTime2016/7/4 17:08
    */
    public int insert(StationSmsRecordsBean record) {
        return stationSmsRecordsWriterMapper.insert(record);
    }
}
