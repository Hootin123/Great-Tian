package com.xtr.core.service.company;

import com.xtr.api.domain.company.CompanyDepsBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.service.company.CompanyDeptService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.core.persistence.reader.company.CompanyDepsReaderMapper;
import com.xtr.core.persistence.reader.company.CompanysReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomersReaderMapper;
import com.xtr.core.persistence.writer.company.CompanyDepsWriterMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Xuewu
 * @Date 2016/8/15.
 */
@Service("companyDeptService")
public class CompanyDeptServiceImpl implements CompanyDeptService {

    @Resource
    private CompanyDepsWriterMapper companyDepsWriterMapper;

    @Resource
    private CompanyDepsReaderMapper companyDepsReaderMapper;

    @Resource
    private CustomersReaderMapper customersReaderMapper;

    @Resource
    private CompanysReaderMapper companysReaderMapper;

    @Override
    public CompanyDepsBean getTree(Long companyId) {
        List<CompanyDepsBean> baseDept = companyDepsReaderMapper.selectBaseDept(companyId);
        if (baseDept != null) {
            if (baseDept.size() > 1) {
                long rootId = initDept(companysReaderMapper.selectByPrimaryKey(companyId));
                companyDepsWriterMapper.addRootDept(rootId, companyId);
                return getTree(companyId);
            } else if (baseDept.size() == 1) {
                CompanyDepsBean companyDepsBean = baseDept.get(0);
                if (!new Integer(4).equals(companyDepsBean.getDepType())) {
                    long rootId = initDept(companysReaderMapper.selectByPrimaryKey(companyId));
                    companyDepsWriterMapper.addRootDept(rootId, companyId);
                    return getTree(companyId);
                }
            } else {
                long rootId = initDept(companysReaderMapper.selectByPrimaryKey(companyId));
                companyDepsWriterMapper.addRootDept(rootId, companyId);
                return getTree(companyId);
            }
            CompanyDepsBean companyDepsBean = baseDept.get(0);
            loadTree(companyDepsBean);
            return companyDepsBean;
        }
        return null;
    }

    @Override
    public List<CompanyDepsBean> getAllDept(Long companyId, Long depId) {
        List<CompanyDepsBean> allDept = new ArrayList<>();
        List<CompanyDepsBean> baseDept = companyDepsReaderMapper.selectBaseDept(companyId);
        if (baseDept != null)
            for (CompanyDepsBean companyDepsBean : baseDept) {
                if (!companyDepsBean.getDepId().equals(depId)) {
                    allDept.add(companyDepsBean);
                    loadAllDept(allDept, companyDepsBean.getDepId(), depId);
                }
            }
        return allDept;
    }

    private void loadAllDept(List<CompanyDepsBean> allDept, Long depParentId, Long depId) {
        List<CompanyDepsBean> companyDepsBeans = companyDepsReaderMapper.selectChildren(depParentId);
        for (CompanyDepsBean companyDepsBean : companyDepsBeans) {
            if (!companyDepsBean.getDepId().equals(depId)) {
                allDept.add(companyDepsBean);
                loadAllDept(allDept, companyDepsBean.getDepId(), depId);
            }
        }

    }

    @Override
    public boolean saveOrUpdate(CompanyDepsBean companyDepsBean, Long deptEditUserId) throws BusinessException {
        if (companyDepsBean == null)
            throw new BusinessException("参数为空");
        if (StringUtils.isBlank(companyDepsBean.getDepName()))
            throw new BusinessException("部门名称为空");
        if (companyDepsBean.getDepId() == null) {//save
            Long depParentId = companyDepsBean.getDepParentId();
            if (depParentId != null) {
                //判断部门名称是否重复
                CompanyDepsBean checkDepsBean=new CompanyDepsBean();
                checkDepsBean.setDepParentId(depParentId);
                checkDepsBean.setDepName(companyDepsBean.getDepName());
                checkDepsBean.setDepCompanyId(companyDepsBean.getDepCompanyId());
                int count=companyDepsReaderMapper.checkRepeatDeptByName(checkDepsBean);
                if(count>0){
                    throw new BusinessException("同部门下的子名称不可以重复");
                }
                CompanyDepsBean parent = companyDepsReaderMapper.selectByPrimaryKey(depParentId);
                companyDepsBean.setDepLevel(parent.getDepLevel() == null ? 1 : parent.getDepLevel() + 1);
            } else {
                //判断部门名称是否重复
                CompanyDepsBean checkDepsBean=new CompanyDepsBean();
                checkDepsBean.setDepName(companyDepsBean.getDepName());
                checkDepsBean.setDepCompanyId(companyDepsBean.getDepCompanyId());
                int count=companyDepsReaderMapper.checkRepeatDeptByName(checkDepsBean);
                if(count>0){
                    throw new BusinessException("同部门下的子名称不可以重复");
                }
                companyDepsBean.setDepLevel(1);
            }

            companyDepsBean.setDepEditMember(deptEditUserId);
            companyDepsBean.setDepEditTime(new Date());
            companyDepsBean.setDelflag(0);
            companyDepsBean.setDepType(2);

            int flag = companyDepsWriterMapper.insert(companyDepsBean);
            return flag > 0;
        } else {//update
            CompanyDepsBean old = selectByPrimaryKey(companyDepsBean.getDepId());
            old.setDepName(companyDepsBean.getDepName());
            if (companyDepsBean.getDepParentId() == null) {
                old.setDepParentId(null);
                old.setDepLevel(1);
            } else {
                CompanyDepsBean parent = companyDepsReaderMapper.selectByPrimaryKey(companyDepsBean.getDepParentId());
                old.setDepLevel(parent.getDepLevel() == null ? 1 : parent.getDepLevel() + 1);
                old.setDepParentId(parent.getDepId());
            }
            old.setDepEditMember(deptEditUserId);
            old.setDepEditTime(new Date());
            int flag = companyDepsWriterMapper.updateByPrimaryKey(old);
            return flag > 0;
        }

    }

    @Override
    public CompanyDepsBean selectByPrimaryKey(Long depId) {
        return companyDepsReaderMapper.selectByPrimaryKey(depId);
    }

    @Override
    public boolean deleteDept(long id, Long companyId) throws BusinessException {
        List<CompanyDepsBean> companyDepsBeans = companyDepsReaderMapper.selectChildren(id);
        if (companyDepsBeans != null && companyDepsBeans.size() > 0)
            throw new BusinessException("该部门还有子节点无法直接删除");
        if (customersReaderMapper.selectCountByDepId(id, companyId) > 0)
            throw new BusinessException("该部门下还有员工无法直接删除");
        return companyDepsWriterMapper.deleteDeps(id) > 0;
    }

    @Override
    public boolean updateLeader(long depId, Long leaderId) throws BusinessException {
        return companyDepsWriterMapper.updateLeader(depId, leaderId) > 0;
    }

    @Override
    public long initDept(CompanysBean companysBean) {
        CompanyDepsBean companyDepsBean = new CompanyDepsBean();
        companyDepsBean.setCompanyId(companysBean.getCompanyId());
        companyDepsBean.setDepCompanyId(companysBean.getCompanyId());
        companyDepsBean.setDepName(companysBean.getCompanyName());
        companyDepsBean.setDelflag(0);
        companyDepsBean.setDepLevel(1);
        companyDepsBean.setDepType(4);//根节点
        companyDepsBean.setDepEditTime(new Date());
        companyDepsWriterMapper.insert(companyDepsBean);
        return companyDepsBean.getDepId();
    }

    @Override
    public int updateRootDeptName(long companyId, String name) {
        return companyDepsWriterMapper.updateRootDeptName(companyId, name);
    }

    private void loadTree(CompanyDepsBean companyDepsBean) {
        if (companyDepsBean != null) {
            List<CompanyDepsBean> children = companyDepsReaderMapper.selectChildren(companyDepsBean.getDepId());
            companyDepsBean.setDepCustCount(customersReaderMapper.selectCountByDepId(companyDepsBean.getDepId(), companyDepsBean.getCompanyId()));
            if (companyDepsBean.getDepLeader() != null) {
                CustomersBean customersBean = customersReaderMapper.selectByPrimaryKey(companyDepsBean.getDepLeader());
                if (customersBean != null)
                    companyDepsBean.setLeaderName(customersBean.getCustomerTurename());
            }

            companyDepsBean.setChildren(children);
            if (children != null)
                for (CompanyDepsBean child : children) {
                    loadTree(child);
                }
        }
    }


    /**
     * 根据部门名称模糊查询部门信息
     *
     * @param companyId
     * @param deptName
     * @return
     */
    public List<CompanyDepsBean> selectByDeptName(Long companyId, String deptName) {
        return companyDepsReaderMapper.selectByDeptName(companyId, deptName);
    }


    /**
     * 修改部门名称
     * @param companyDepsBean
     * @param deptEditUserId
     * @param newDeptName
     * @throws BusinessException
     */
    public void modifyDepts(CompanyDepsBean companyDepsBean, Long deptEditUserId,String newDeptName) throws BusinessException {
        if (companyDepsBean == null || companyDepsBean.getDepId()==null) {
            throw new BusinessException("参数为空");
        }
        if (StringUtils.isBlank(newDeptName)) {
            throw new BusinessException("请输入部门新名称");
        }
        CompanyDepsBean old = selectByPrimaryKey(companyDepsBean.getDepId());
        old.setDepName(newDeptName);
        old.setDepEditMember(deptEditUserId);
        old.setDepEditTime(new Date());
        int flag = companyDepsWriterMapper.updateByPrimaryKey(old);
        if(flag<=0){
            throw new BusinessException("变更失败");
        }
    }

    /**
     * 新增部门
     * @param companyDepsBean
     * @param deptEditUserId
     * @throws BusinessException
     */
    public void createDept(CompanyDepsBean companyDepsBean, Long deptEditUserId) throws BusinessException {
        if (companyDepsBean == null ) {
            throw new BusinessException("参数为空");
        }
        if (StringUtils.isBlank(companyDepsBean.getDepName())) {
            throw new BusinessException("请输入添加部门的名称");
        }
        Long depParentId = companyDepsBean.getDepParentId();
        if (depParentId != null) {
            //判断部门名称是否重复
            CompanyDepsBean checkDepsBean=new CompanyDepsBean();
            checkDepsBean.setDepParentId(depParentId);
            checkDepsBean.setDepName(companyDepsBean.getDepName());
            checkDepsBean.setDepCompanyId(companyDepsBean.getDepCompanyId());
            int count=companyDepsReaderMapper.checkRepeatDeptByName(checkDepsBean);
            if(count>0){
                throw new BusinessException("同部门下的子名称不可以重复");
            }
            CompanyDepsBean parent = companyDepsReaderMapper.selectByPrimaryKey(depParentId);
            companyDepsBean.setDepLevel(parent.getDepLevel() == null ? 1 : parent.getDepLevel() + 1);
        } else {
            //判断部门名称是否重复
            CompanyDepsBean checkDepsBean=new CompanyDepsBean();
            checkDepsBean.setDepName(companyDepsBean.getDepName());
            checkDepsBean.setDepCompanyId(companyDepsBean.getDepCompanyId());
            int count=companyDepsReaderMapper.checkRepeatDeptByName(checkDepsBean);
            if(count>0){
                throw new BusinessException("同部门下的子名称不可以重复");
            }
            companyDepsBean.setDepLevel(1);
        }

        companyDepsBean.setDepEditMember(deptEditUserId);
        companyDepsBean.setDepEditTime(new Date());
        companyDepsBean.setDelflag(0);
        companyDepsBean.setDepType(2);

        int flag = companyDepsWriterMapper.insert(companyDepsBean);
        if(flag<=0){
            throw new BusinessException("新增部门失败");
        }
    }
}
