package com.xtr.core.service.station;

import com.xtr.api.domain.station.StationSmscontBean;
import com.xtr.api.service.station.StationSmscontService;
import com.xtr.core.persistence.reader.station.StationSmscontReaderMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by abiao on 2016/7/4.
 */
@Service("stationSmscontService")
public class StationSmscontServiceImpl implements StationSmscontService {

    @Resource
    private StationSmscontReaderMapper stationSmscontReaderMapper;

    /*
    * <p>查询短信模板相关记录</p>
    * @auther 何成彪
    * @createTime2016/7/4 16:57
    */
    public StationSmscontBean selectByPrimaryKey(int primarykey) {
        return stationSmscontReaderMapper.selectByPrimaryKey(primarykey);
    }

    /**
     * 根据短信类型获取短信模板
     * @param smsType
     * @return
     */
    public StationSmscontBean selectBySmsType(Integer smsType){
        return stationSmscontReaderMapper.selectBySmsType(smsType);
    }
}
