package com.xtr.core.persistence.reader.customer;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.dto.customer.CustomersDto;
import com.xtr.api.dto.salary.CustomerPayrollDto;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface CustomersReaderMapper {

    /**
     * 根据指定主键获取一条数据库记录,customers
     *
     * @param customerId
     */
    CustomersBean selectByPrimaryKey(Long customerId);

    /**
     * 获取员工账号信息
     *
     * @param customersBean
     * @return
     */
    List<CustomersBean> selectCustomers(CustomersBean customersBean);

    /**
     * 根据企业Id获取企业员工信息
     *
     * @param customersBean
     * @param pageBounds
     * @return
     */
    List<CustomersBean> selectPageList(CustomersBean customersBean, PageBounds pageBounds);

    /**
     * 根据过滤条件获取员工信息
     *
     * @param customersBean
     * @param pageBounds
     * @return
     */
    List<CustomersDto> selectCustomerInfoPageList(CustomersBean customersBean, PageBounds pageBounds);

    /**
     * 根据过滤条件获取员工信息
     *
     * @param customerId
     * @return
     */
    List<CustomersDto> selectCustomerInfoDetail(long customerId);

    /**
     * 获取部门成员数量
     *
     * @param deptId
     * @return
     */
    int selectCountByDeptId(@Param("deptId") Long deptId);

    /**
     * 查询企业员工数量
     *
     * @param companyId
     * @return
     */
    int selectCompanyCustomerCount(@Param("companyId") Long companyId);

    /**
     * 根据Id获取员工姓名
     *
     * @param customerId
     * @return
     */
    CustomersBean selectNameById(Long customerId);

    /**
     * 根据身份证号查询员工
     * @param idCard
     * @return
     */
    CustomersBean selectByIdCard(@Param("idCard") String idCard);

    /**
     * 根据过滤条件查询员工信息(企业端)
     *
     * @param customersDto
     * @return
     */
    List<CustomersDto> selectCustomersByCondition(CustomersDto customersDto);

    /**
     * 根据条件查询员工数量
     *
     * @param customersDto
     * @return
     */
    int selectCustomerNumber(CustomersDto customersDto);

    /**
     * 获取未定薪员工id
     *
     * @param customersDto
     * @return
     */
    List<CustomersBean> selectCustomerList(CustomersDto customersDto, PageBounds pageBounds);

    /**
     * 根据过滤条件查询员工信息(企业端分页)
     *
     * @param customersDto
     * @return
     */
    List<CustomersDto> selectCustomersByCondition(CustomersDto customersDto, PageBounds pageBounds);

    /**
     * 根据手机号 跟 姓名查询用户是否存在
     *
     * @param customer
     * @return
     */
    CustomersBean selectComstomerByPhoneAndTrueName(CustomersBean customer);

    /**
     * 根据部门查询
     *
     * @param depId
     * @return
     */
    List<CustomersBean> selectByDepId(@Param("depId") Long depId, @Param("companyId") long companyId);

    /**
     * 根据部门查询员工数量
     *
     * @param depId
     * @param companyId
     * @return
     */
    int selectCountByDepId(@Param("depId") Long depId, @Param("companyId") long companyId);

    /**
     * 根据主键id查询 当前可用的员工账户
     *
     * @param customerId
     * @return
     */
    CustomersBean selectById(Long customerId);

    /**
     * 根据ID查询所有关联信息
     *
     * @param customerId
     * @return
     */
    CustomersDto selectCustomersByCustomerId(Long customerId);

    /**
     * 查询手机号是否有重复
     *
     * @param list
     * @return
     */
    List<CustomersDto> selectPhoneForeach(@Param("list") List<String> list, @Param("companyId") Long companyId);

    /**
     * 查询身份证号是否有重复
     *
     * @param list
     * @return
     */
    List<CustomersDto> selectidCardForeach(@Param("list") List<String> list, @Param("companyId") Long companyId);

    /**
     * 根据手机查询员工
     *
     * @return
     */
    CustomersBean selectByPhone(String phone);

    /**
     * 根据CustomersBean查询员工id列表
     *
     * @return
     */
    List<Long> selectPrimaryKey(@Param("companyId") Long companyId, @Param("depts") String[] deptIds, @Param("members") String[] memberIds);

    /**
     * 根据公司查询员工
     *
     * @return
     */
    List<CustomersBean> selectUsefulCustomerByCompanyId(Long companyId);

    /**
     * 根据公司主键获取员工id
     *
     * @param companyId 公司主键
     * @param startDate 计薪周期第一天
     * @return
     */
    List<CustomersDto> selectByCompanyId(@Param("companyId") Long companyId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 根据公司主键获取员工数量
     *
     * @param companyId 公司主键
     * @param startDate 计薪周期第一天
     * @return
     */
    int selectByCompanyIdNumber(@Param("companyId") Long companyId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 根据id查询员工信息
     *
     * @param customerId 员工id
     * @param startDate  计薪周期第一天
     * @return
     */
    CustomersDto selectByCustomerId(@Param("customerId") Long customerId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 分页查询员工信息
     *
     * @param customerPayrollDto
     * @param pageBounds
     * @return
     */
    PageList<CustomersDto> selectCustomerPageList(CustomerPayrollDto customerPayrollDto, PageBounds pageBounds);

    /**
     * 新增员工时根据手机号跟部门id判别用户是否存在
     *
     * @param customer
     * @return
     */
    List<CustomersBean> selectComstomerByPhoneAndComId(CustomersBean customer);

    /**
     * 根据手机 公司ID 查询员工
     *
     * @return
     */
    CustomersBean selectByPhoneAndCompanyId(@Param("phone") String phone, @Param("companyId") long companyId);

    /**
     * 获取未定薪员工人数
     *
     * @param customersDto
     * @return
     */
    List<CustomersDto> selectComstomerCountByNoSaraly(CustomersDto customersDto);

    /**
     * 查询公司下未离职的员工
     *
     * @param companyId
     * @return
     */
    List<CustomersBean> selectUsefulCustByCompany(long companyId);

    /**
     * 根据id列表查询员工
     *
     * @param ids
     * @return
     */
    List<CustomersBean> selectByIds(String[] ids);

    /**
     * 条件查询查询当前员工的数量
     *
     * @param customersCountDto
     * @return
     */
    long selectCustomersCount(CustomersDto customersCountDto);


    /**
     * 查询所有未生成账户的员工
     *
     * @return
     */
    List<CustomersBean> selectCustoemrSubAccount();

    /**
     * 查询最新入职的那一条员工
     *
     * @param phone
     * @return
     */
    List<CustomersDto> selectLastCustomerByPhone(String phone);

    /**
     * 查询在职员工
     *
     * @param companyId
     * @param startDate
     * @return
     */
    int selectLiveCountByCompanyId(@Param("companyId") Long companyId, @Param("cycleId") long cycleId, @Param("startDate") Date startDate);

    /**
     * 查询是否符合进入入职规范页面
     *
     * @return
     */
    int selectCountForIsRedirect(long customerId);

    /**
     * 查询未绑定的员工
     *
     * @param phone
     * @return
     */
    List<CustomersDto> selectUnbindCustomers(String phone);

    /**
     * 根据公司主键获取未定薪员工人数
     *
     * @param companyId
     * @param startDate
     * @param endDate
     * @return
     */
    int selectNoSaralyCountByCompanyId(@Param("companyId") Long companyId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 循环查询员工信息
     * @param list
     * @return
     */
    List<CustomersBean> selectCustomersForeach(List<Long> list);

    /**
     * 根据员工身份证批量获取员工信息
     * @param list
     * @return
     */
    List<CustomersBean> selectCustomersForeachByIdcard(List<String> list);

    /**
     * 查询所有社保资料未审核的员工
     * @return
     */
    List<CustomersBean> selectShebaoAdditionalCustomer();

    /**
     * 查询员工社保资料审核失败数量
     * @param memberCompanyId
     * @return
     */
    int selectShebaoFailedCount(@Param("memberCompanyId") Long memberCompanyId);

    /**
     * 查询发工资计薪周期内入职员工人数
     * @param customersDto
     * @return
     */
    int selectEnterNumberForSalary(CustomersDto customersDto);

    /**
     * 查询发工资计薪周期内离职员工人数
     * @param customersDto
     * @return
     */
    int selectLeaveNumberForSalary(CustomersDto customersDto);

    /**
     * 根据相关条件获取企业员工信息并展示在员工管理首页
     * @param customersDto
     * @return
     */
    List<CustomersDto> selectCustomersByNewCondition(CustomersDto customersDto);

    /**
     * 根据相关条件获取企业员工信息并展示在员工管理首页(分页)
     * @param customersDto
     * @return
     */
    List<CustomersDto> selectCustomersByNewCondition(CustomersDto customersDto, PageBounds pageBounds);

    /**
     * 获取待离职的员工
     * @param customersDto
     * @param pageBounds
     * @return
     */
    List<CustomersDto> selectWillLeaveByNewCondtion(CustomersDto customersDto,PageBounds pageBounds);

    /**
     * 根据相关条件获取企业员工数量
     * @param customersDto
     * @return
     */
    int selectCountByNewCondition(CustomersDto customersDto);

    /**
     * 获取待离职的员工数量
     * @param customersDto
     * @return
     */
    int selectWillLeaveCountByNewCondtion(CustomersDto customersDto);

    /**
     * 根据相关条件获取企业员工信息并展示在员工管理首页(分页)
     * @param customersDto
     * @return
     */
    List<CustomersDto> selectInCompleteListByNewCondition(CustomersDto customersDto,PageBounds pageBounds);

    /**
     * 根据相关条件获取企业员工信息并展示在员工管理首页
     * @param customersDto
     * @return
     */
    List<CustomersDto> selectInCompleteListByNewCondition(CustomersDto customersDto);

    /**
     *查询企业下的所有员工
     * @param companyId
     * @return
     */
    List<CustomersBean> selectCustomersForCompanyId(@Param("companyId") long companyId);
    List<CustomersDto> selectEveryMonthCustomerCounts(@Param("companyId") Long companyId,@Param("year") int year);



    /**
     * 批量获取员工基本工资基数
     * @param list
     * @return
     */
    List<CustomersDto> selectSalaryBaseBatch(@Param("list") List<Long> list);
}