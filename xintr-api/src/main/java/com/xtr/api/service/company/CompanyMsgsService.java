package com.xtr.api.service.company;

import com.xtr.api.domain.company.CompanyMsgsBean;

import java.util.List;

/**
 * Created by xuewu on 2016/7/26.
 * 企业信息
 */
public interface CompanyMsgsService {
    public List<CompanyMsgsBean> findAllUnReadMsg(long companyId, long maxSize);

    /**
     * 保存消息记录
     * @param companyMsgsBean
     * @return
     */
    int  insert(CompanyMsgsBean companyMsgsBean);

    /**
     * 根据用户id查询消息记录的list集合
     * @param msgCompanyId
     * @return
     */
    List<CompanyMsgsBean> findCompanyMsgsBeanListByCompanyId(Long msgCompanyId );

    /**
     * 根据消息主键查询消息
     * @param msgId
     * @return
     */
    CompanyMsgsBean selectCompanyMsgsBeanByMsgId(Long msgId);

    /**
     * 逻辑删除该条消息
     * @param msgId
     * @return
     */
    int deleteCompanyMsgsBeanByMsgId(Long msgId);

    /**
     * 查询当前公司用户的未读消息数量
     * @param memberCompanyId
     * @return
     */
    long getUnReadCounts(Long memberCompanyId);

    /**
     * 更新状态为已读
     * @param msgId
     * @return
     */
    int updateHasRead(Long msgId);

    /**
     * 新版 消息盒子 查询所有的未读 或者 已读消息
     * @param companyId   公司id
     * @param  msgSign    消息状态： 1未读  2已读  0删除
     * @return
     */
    List<CompanyMsgsBean> selectCompanyMsgs(Long companyId,Integer msgSign) throws Exception;

}
