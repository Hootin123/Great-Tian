package com.xtr.core.service.sys;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.sys.SysLogBean;
import com.xtr.api.service.sys.SysLogService;
import com.xtr.core.persistence.reader.sys.SysLogReaderMapper;
import com.xtr.core.persistence.writer.sys.SysLogWriterMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/8/8 16:53
 */
@Service("sysLogService")
public class SysLogServiceImpl implements SysLogService {

    @Resource
    private SysLogReaderMapper sysLogReaderMapper;

    @Resource
    private SysLogWriterMapper sysLogWriterMapper;

    /**
     * 新写入数据库记录,sys_log
     *
     * @param record
     */
    public int insert(SysLogBean record) {
        return sysLogWriterMapper.insert(record);
    }

    /**
     * 分页查询日志信息
     *
     * @param sysLogBean
     * @return
     */
    public ResultResponse selectPageList(SysLogBean sysLogBean) {
        ResultResponse resultResponse = new ResultResponse();
        PageBounds pageBounds = new PageBounds(sysLogBean.getPageIndex(), sysLogBean.getPageSize());
        PageList<SysLogBean> list = sysLogReaderMapper.listPage(sysLogBean, pageBounds);
        resultResponse.setData(list);
        Paginator paginator = list.getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setSuccess(true);
        return resultResponse;
    }
}
