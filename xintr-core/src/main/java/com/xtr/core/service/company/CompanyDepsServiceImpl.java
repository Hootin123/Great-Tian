package com.xtr.core.service.company;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyDepsBean;
import com.xtr.api.dto.company.CompanysDto;
import com.xtr.api.service.company.CompanyDepsService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.core.persistence.reader.company.CompanyDepsReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomersReaderMapper;
import com.xtr.core.persistence.writer.company.CompanyDepsWriterMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/21 11:31
 */
@Service("companyDepsService")
public class CompanyDepsServiceImpl implements CompanyDepsService {

    @Resource
    private CompanyDepsWriterMapper companyDepsWriterMapper;

    @Resource
    private CompanyDepsReaderMapper companyDepsReaderMapper;

    @Resource
    private CustomersReaderMapper customersReaderMapper;


    /**
     * 根据指定主键获取一条数据库记录,company_deps
     *
     * @param depId
     */
    public CompanyDepsBean selectByPrimaryKey(Long depId) throws BusinessException {
        if (depId != null) {
            return companyDepsReaderMapper.selectByPrimaryKey(depId);
        } else {
            throw new BusinessException("部门Id不能为空");
        }
    }

    /**
     * 新写入数据库记录,company_deps
     *
     * @param record
     */
    public void insert(CompanyDepsBean record) throws BusinessException {
        if (record != null) {
            int result = companyDepsWriterMapper.insert(record);
            if (result <= 0) {
                throw new BusinessException("新建子部门失败");
            }
        } else {
            throw new BusinessException("新建子部门参数为空");
        }
    }

    /**
     * 根据公司Id、部门名称验证该部门是否存在
     *
     * @param depParentId
     * @param deptName
     * @return
     */
    public ResultResponse checkDeptByName(Long depParentId, String deptName) throws BusinessException {
        if (depParentId == null) {
            throw new BusinessException("公司主键不能为空");
        }
        if (StringUtils.isBlank(deptName)) {
            throw new BusinessException("部门名称不能为空");
        }
        ResultResponse resultResponse = new ResultResponse();
        CompanyDepsBean companyDepsBean = companyDepsReaderMapper.checkDeptByName(depParentId, deptName);
        resultResponse.setData(companyDepsBean);
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 修改部门名称
     *
     * @param deptId
     * @return
     */
    public void updateDeptName(Long deptId, String deptName) throws BusinessException {
        if (deptId == null) {
            throw new BusinessException("主键不能为空");
        }
        if (StringUtils.isBlank(deptName)) {
            throw new BusinessException("部门名称不能为空");
        }
        CompanyDepsBean companyDepsBean = new CompanyDepsBean();
        companyDepsBean.setDepName(deptName);
        companyDepsBean.setDepId(deptId);
        int result = companyDepsWriterMapper.updateByPrimaryKeySelective(companyDepsBean);
        if (result <= 0) {
            throw new BusinessException("修改部门名称失败");
        }
    }

    /**
     * 删除部门
     *
     * @param deptId
     * @return
     */
    public void deleteDept(Long deptId) throws BusinessException {
        if (deptId == null) {
            throw new BusinessException("主键不能为空");
        }
        int result = companyDepsWriterMapper.deleteByPrimaryKey(deptId);
        if (result <= 0) {
            throw new BusinessException("部门删除失败");
        }
    }


    /**
     * 根据企业主键获取部门信息
     *
     * @param companyId
     * @return
     */
    public List<CompanysDto> getCompanyTree(Long companyId) throws BusinessException {
        if (companyId != null) {
            List<CompanysDto> list = companyDepsReaderMapper.getCompanyTree(companyId);
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    CompanysDto companysDto = list.get(i);
                    companysDto.setCustomerCount(customersReaderMapper.selectCountByDeptId(companysDto.getDepId()));
                    //获取部门成员数量
                    list.set(i, companysDto);
                }
            }
            return list;
        } else {
            throw new BusinessException("企业Id不能为空");
        }
    }


    /**
     * 根据企业社保订单号部门信息
     *
     * @param companyShebaoId
     * @return
     */
    public List<CompanysDto> getDepsTreeByCompanyShebaoId(Long companyShebaoId) throws BusinessException {
        if (companyShebaoId != null) {
            List<CompanysDto> list = companyDepsReaderMapper.getDepsTreeByCompanyShebaoId(companyShebaoId);
            return list;
        } else {
            throw new BusinessException("企业companyShebaoId不能为空");
        }
    }

}
