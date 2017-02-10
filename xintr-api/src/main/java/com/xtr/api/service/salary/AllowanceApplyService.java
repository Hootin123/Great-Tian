package com.xtr.api.service.salary;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.salary.AllowanceApplyBean;
import com.xtr.comm.basic.BusinessException;

import java.util.List;
import java.util.Map;

/**
 * <p>津贴适用范围Service</p>
 *
 * @author 任齐
 * @createTime: 2016/8/18 14:55.
 */
public interface AllowanceApplyService {

    /**
     * 保存津贴适用范围
     *
     * @param companyId
     * @param data
     * @throws BusinessException
     */
    void batchSave(Long companyId, String data) throws BusinessException;

    /**
     * 保存一条津贴员工信息
     * @param allowanceId
     * @param type
     * @param userdata
     * @throws BusinessException
     */
    void saveAllowanceApply(Long allowanceId, Byte type, String userdata) throws BusinessException;

    /**
     * 根据津贴删除适用人员
     *
     * @param allowanceId
     * @throws BusinessException
     */
    void deleteByAllowanceId(Long allowanceId) throws BusinessException;

    /**
     * 查询某个津贴的员工列表
     *
     * @param allowanceId
     * @return
     */
    List<AllowanceApplyBean> getApplyByAllowanId(Long allowanceId);

    /**
     * 查询公司津贴以及员工列表
     *
     * <Long, Map<Long, Long>>
     * <津贴id, 员工数据>
     *
     * @param companyId
     * @return
     */
    Map<Long, Map<Long, Long>> getCompanyAllowanceApplyMembers(Long companyId);

    /**
     * 获取某个津贴的适用员工
     *
     * @param allowanceId
     * @return
     */
    ResultResponse getAllowanceMembers(Long allowanceId);
}
