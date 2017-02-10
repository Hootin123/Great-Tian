package com.xtr.core.service.company;

import com.xtr.api.domain.company.CompanyMsgsBean;
import com.xtr.api.service.company.CompanyMsgsService;
import com.xtr.core.persistence.reader.company.CompanyMsgsReaderMapper;
import com.xtr.core.persistence.writer.company.CompanyMsgsWriterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by abiao on 2016/6/22.
 */
@Service("companyMsgsService")
public  class CompanyMsgsServiceImpl implements CompanyMsgsService {

    @Autowired
    private CompanyMsgsReaderMapper companyMsgsReaderMapper;

    @Autowired
    private CompanyMsgsWriterMapper companyMsgsWriterMapper;


    @Override
    public List<CompanyMsgsBean> findAllUnReadMsg(long companyId, long maxSize) {
        return companyMsgsReaderMapper.selectAllUnReadMsg(companyId, maxSize);
    }

    @Override
    public int insert(CompanyMsgsBean companyMsgsBean) {
        return  companyMsgsWriterMapper.insert(companyMsgsBean) ;
    }

    @Override
    public List<CompanyMsgsBean> findCompanyMsgsBeanListByCompanyId(Long msgCompanyId) {
        return companyMsgsReaderMapper.findCompanyMsgsBeanListByCompanyId(msgCompanyId);
    }

    @Override
    public CompanyMsgsBean selectCompanyMsgsBeanByMsgId(Long msgId) {
        return companyMsgsReaderMapper.selectNoDelete(msgId);
    }

    @Override
    public int deleteCompanyMsgsBeanByMsgId(Long msgId) {
        CompanyMsgsBean cop = new CompanyMsgsBean();
        cop.setMsgId(msgId);
        return  companyMsgsWriterMapper.updateComMsgById(cop);
    }

    /**
     * 查询当前公司用户的未读消息个数
     * @param memberCompanyId
     * @return
     */
    @Override
    public long getUnReadCounts(Long memberCompanyId) {
        return companyMsgsReaderMapper.getUnReadCounts(memberCompanyId);
    }

    /**
     * 更新状态为已读
     * @param msgId
     * @return
     */
    @Override
    public int updateHasRead(Long msgId) {
        return companyMsgsWriterMapper.updateHasReadById(msgId);
    }

    /**
     * 新版消息盒子 查询所有的已读 或者 未读消息
     * @param companyId
     * @param  msgSign
     * @return
     */
    @Override
    public List<CompanyMsgsBean> selectCompanyMsgs(Long companyId,Integer msgSign) throws Exception {
        return companyMsgsReaderMapper.selectCompanyMsgs(companyId,msgSign);
    }
}
