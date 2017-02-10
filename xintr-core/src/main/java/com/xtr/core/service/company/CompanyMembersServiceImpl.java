package com.xtr.core.service.company;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.*;
import com.xtr.api.dto.company.CompanyMenuDto;
import com.xtr.api.service.company.CompanyMembersService;
import com.xtr.comm.util.StringUtils;
import com.xtr.core.persistence.reader.company.CompanyMembersReaderMapper;
import com.xtr.core.persistence.writer.company.CompanyMembersWriterMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by abiao on 2016/6/22.
 */
@Service("companyMembersService")
public class CompanyMembersServiceImpl implements CompanyMembersService {

    @Resource
    private CompanyMembersReaderMapper companyMembersReaderMapper;
    @Resource
    private CompanyMembersWriterMapper companyMembersWriterMapper;

    public int insert(CompanyMembersBean record) {
        companyMembersWriterMapper.insert(record);
        return Integer.parseInt(record.getMemberId().toString());
    }

    public CompanyMembersBean findByCondition(CompanyMembersBean companyMembersBeans) {
        return companyMembersReaderMapper.selectByCondition(companyMembersBeans);
    }

    public CompanyMembersBean findByMemberLogname(String memberLogname) {
        return companyMembersReaderMapper.selectByMemberLogname(memberLogname);
    }


    public int updateByCondition(CompanyMembersBean companyMembersBeans) {
        return companyMembersWriterMapper.updateByCondition(companyMembersBeans);
    }

    public CompanyMembersBean findByCondition(Map map) {
        return null;
    }

    @Override
    public CompanyMembersBean selectCompanyMembersBean(long memberId) {
        return companyMembersReaderMapper.selectByPrimaryKey(memberId);
    }

    /**
     * 根据企业id检查企业是否已经签约
     *
     * @param companyId
     */
    public boolean checkCompanyIsSign(Long companyId) {
        if (null == companyId) {
            return false;
        }
        CompanyMembersBean companyMembersBean = new CompanyMembersBean();
        companyMembersBean.setMemberCompanyId(companyId);
        return companyMembersReaderMapper.selectCount(companyMembersBean) > 0;
    }

    /**
     * 根据ID更新用户信息
     *
     * @param record
     */
    public int updateByPrimaryKeySelective(CompanyMembersBean record) {
        return companyMembersWriterMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据登录名查询激活的用户信息
     *
     * @param companyMembersBean
     * @return
     */
    public CompanyMembersBean selectByLoginNameActive(CompanyMembersBean companyMembersBean) {
        return companyMembersReaderMapper.selectByLoginNameActive(companyMembersBean);
    }

    /**
     * 根据手机号用户信息
     *
     * @param companyMembersBean
     * @return
     */
    public List<CompanyMembersBean> selectByPhone(CompanyMembersBean companyMembersBean) {
        return companyMembersReaderMapper.selectByPhone(companyMembersBean);
    }

    /**
     * 根据手机号用户信息
     *
     * @param companyMembersBean
     * @return
     */
    public List<CompanyMembersBean> selectCanLoginByPhone(CompanyMembersBean companyMembersBean) {
        return companyMembersReaderMapper.selectCanLoginByPhone(companyMembersBean);
    }

    /**
     * 通过手机号更改用户信息
     *
     * @param companyMembersBean
     * @return
     */
    public int updateByPhone(CompanyMembersBean companyMembersBean) {
        return companyMembersWriterMapper.updateByPhone(companyMembersBean);
    }

    @Override
    public List<CompanyMembersBean> findByMemberPhone(String memberLogname) {
        return companyMembersReaderMapper.findByMemberPhone(memberLogname);
    }

    /**
     * 根据手机号 或者 登录名 查询用户信息
     *
     * @param companyMembersBean
     * @return
     */
    @Override
    public List<CompanyMembersBean> selectByPhoneOrLogname(CompanyMembersBean companyMembersBean) {
        return companyMembersReaderMapper.selectByPhoneOrLogname(companyMembersBean);
    }

    /**
     * 插入用户返回longId
     *
     * @param companyMembersBean
     * @return
     */
    @Transactional
    public Long insertForReturnId(CompanyMembersBean companyMembersBean) {
        return companyMembersWriterMapper.insertForReturnId(companyMembersBean);
    }

    @Override
    public List<CompanyMenuBean> selectMenuByUserId(Long userId) {
        List<CompanyMenuBean> list = companyMembersReaderMapper.selectMenuByUserId(userId);
        return list;
    }

    @Override
    public Map<String, String> selectCompanyMenuBeanByUserId(Long userId) {
        return companyMembersReaderMapper.selectCompanyMenuBeanByUserId(userId);
    }

    @Override
    public List<CompanyRoleBean> selectCompanyRoleBeanByState(Integer state) {
        return companyMembersReaderMapper.selectCompanyRoleBeanByState(state);
    }

    @Override
    public int companyRoleRelBeanAdd(CompanyRoleRelBean companyRoleRelBean) {
        return companyMembersWriterMapper.companyRoleRelBeanAdd(companyRoleRelBean);
    }

    @Override
    public List<CompanyMembersBean> selectByCompanyMembersBean(CompanyMembersBean companyMembersBean) {
        return companyMembersReaderMapper.selectByCompanyMembersBean(companyMembersBean);
    }

    @Override
    public int deleteRoleRelBeanById(CompanyRoleRelBean companyRoleRelBean) {
        return companyMembersWriterMapper.deleteRoleRelBeanById(companyRoleRelBean);
    }

    /**
     * 获取企业管理员
     *
     * @param companyId
     * @return
     */
    @Override
    public CompanyMembersBean getCompanyManager(Long companyId) {
        if (null != companyId) {
            return companyMembersReaderMapper.selectCompanyManager(companyId);
        }
        return null;
    }

    /**
     * 查询企业用户的待办事项
     *
     * @param memberId
     * @return
     */
    @Override
    public List<TodoMaterBean> selectAllTodomaterByMemberId(Long memberId) {
        return companyMembersReaderMapper.selectAllTodomaterByMemberId(memberId);
    }

    /**
     * 新增待办事项
     *
     * @param materBean
     * @return
     */

    @Override
    public TodoMaterBean addCompanyMemberMater(TodoMaterBean materBean) {
        companyMembersWriterMapper.addTodoMater(materBean);
        return materBean;
    }

    @Transactional
    @Override
    public int updateMaterById(TodoMaterBean todoMaterBean) {
        return companyMembersWriterMapper.updateMaterById(todoMaterBean);
    }

    /**
     * 逻辑删除待办事项
     *
     * @param materId
     * @return
     */
    @Override
    public int deleteMaterById(long materId) {
        return companyMembersWriterMapper.deleteMaterById(materId);
    }

    /**
     * 根据员工ID获取菜单的访问权限
     * @param memberId
     * @param menuId
     * @return
     */
    public int selectCountForMemberVisitMenu(Long memberId,Long menuId){
        return companyMembersReaderMapper.selectCountForMemberVisitMenu(memberId,menuId);
    }

    /**
     * 根据菜单名称获取菜单ID
     * @param menuName
     * @return
     */
    public long selectMenuIdByMenuName(String menuName){
        return companyMembersReaderMapper.selectMenuIdByMenuName(menuName);
    }

    /**
     * 判断是否第一次访问
     * @param map
     * @return
     */
    @Override
    public CompanyMenuVisitRecordBean selectVisitRecord(Map<String, Object> map) {
        return companyMembersReaderMapper.selectVisitRecord(map);
    }

    /**
     * 保存访问记录
     * @param saveVisitRecord
     * @return
     */
    @Transactional
    @Override
    public int saveVisitRecord(CompanyMenuVisitRecordBean saveVisitRecord) {
        return companyMembersWriterMapper.saveVisitRecord(saveVisitRecord);
    }

    /**
     * 新版home页面查询所有的菜单
     * @param userId
     * @return
     */
    @Override
    public List<CompanyMenuDto> selectMenusByUserId(Long userId) {


        //需要辨别哪些一级菜单下有二级菜单
        //查询1级菜单
        List<CompanyMenuDto> list = companyMembersReaderMapper.selectMenusByUserId(userId);
        if(null!=list&&list.size()>0){
            int size = list.size();
            for (int i = 0; i < size; i++) {
                CompanyMenuDto companyMenuDto = list.get(i);
                //查询2级菜单
                List<CompanyMenuBean> menuBeanList = companyMembersReaderMapper.getChildMenuByUser(companyMenuDto.getId(), userId);
                if(null==menuBeanList||menuBeanList.size()<=0)
                    companyMenuDto.setHasSecondMenu(0);//没有二级菜单的标识
                 else
                    companyMenuDto.setHasSecondMenu(1);//有二级菜单

                companyMenuDto.setChildrenMenu(menuBeanList);
                list.set(i, companyMenuDto);
            }
        }
        return list;
    }
}
