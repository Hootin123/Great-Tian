package com.xtr.api.service.customer;


import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.customer.CustomerWechatBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.domain.customer.CustomersPersonalBean;
import com.xtr.api.domain.customer.CustomersStationBean;
import com.xtr.api.dto.customer.CustomerResponse;
import com.xtr.api.dto.customer.CustomersDto;
import com.xtr.comm.annotation.SystemServiceLog;
import com.xtr.comm.basic.BusinessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>用户信息(员工)</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/21 10:19
 */

public interface CustomersService {

    /**
     * 新写入数据库记录,customers
     *
     * @param record
     */
    ResultResponse insert(CustomersBean record) throws Exception;

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,customers
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CustomersBean record);

    /**
     * 新增客户子账户
     */
    void addSubAccount(CustomersBean record) throws Exception;

    /**
     * 新增企业员工账号
     *
     * @param list
     * @return
     */
    ResultResponse saveCustomers(List<CustomersBean> list) throws Exception;

    /**
     * 新增企业员工账号
     *
     * @param list
     * @return
     */
    ResultResponse editCustomers(CustomersBean list) throws Exception;

    /**
     * 冻结企业员工账号
     *
     * @param customerIds
     * @return
     */
    ResultResponse frozenCustomers(Long[] customerIds) throws BusinessException;

    /**
     * 启用企业员工账号
     *
     * @param customerIds
     * @return
     */
    ResultResponse enableCustomers(Long[] customerIds) throws BusinessException;

    /**
     * 获取员工账号信息
     *
     * @param companyId
     * @return
     */
    List<CustomersBean> getCustomers(CustomersBean companyId);

    /**
     * 根据企业Id获取企业员工信息
     *
     * @param customersBean
     * @return
     */
    ResultResponse selectPageList(CustomersBean customersBean);

    /**
     * 修改员工的所属部门
     *
     * @param membersIds
     * @param deptId
     * @throws BusinessException
     */
    void updateDeptId(Long[] membersIds, Long deptId) throws BusinessException;

    /**
     * 根据过滤条件查询员工信息
     *
     * @param customersBean
     * @return
     */
    ResultResponse selectCustomerInfoPageList(CustomersBean customersBean);

    /**
     * 根据员工编码获取员工详细信息
     *
     * @param customerId
     * @return
     */
    ResultResponse selectCustomerInfoDetail(long customerId);

    /**
     * 查询企业的员工数量
     *
     * @param companyId
     * @return
     */
    int getCompanyCustomerCount(Long companyId);

    /**
     * 根据Id获取员工姓名
     *
     * @param customerId
     * @return
     */
    CustomersBean selectNameById(Long customerId);

    /**
     * 新增员工
     *
     * @param customersDto
     * @return
     * @throws BusinessException
     */
    ResultResponse insertCustomer(CustomersDto customersDto) throws BusinessException;

    /**
     * 员工转正
     *
     * @param customersDto
     * @return
     * @throws BusinessException
     */
    ResultResponse customerRegular(CustomersDto customersDto) throws BusinessException;

    /**
     * 员工调岗
     *
     * @param customersDto
     * @return
     */
    ResultResponse customerTransferPosition(CustomersDto customersDto);

    /**
     * 根据部门查询员工
     *
     * @param depId
     * @return
     */
    List<CustomersBean> selectByDepId(Long depId, long companyId);

    /**
     * 根据部门查询员工数量
     *
     * @param depId
     * @return
     */
    int selectCountByDepId(Long depId, long companyId);


    /**
     * 员工离职
     *
     * @param customersDto
     * @return
     */
    ResultResponse customerDimission(CustomersDto customersDto);

    /**
     * 员工删除
     *
     * @param customersDto
     * @return
     */
    ResultResponse customerDelete(CustomersDto customersDto);

    /**
     * 根据过滤条件查询员工信息(企业端)
     *
     * @param customersDto
     * @return
     */
    List<CustomersDto> selectCustomersByCondition(CustomersDto customersDto);

    /**
     * 企业端用户分页查询
     *
     * @param customersDto
     * @return
     */
    ResultResponse selectPageListByCondition(CustomersDto customersDto) throws Exception;


    /**
     * 根据指定主键获取一条数据库记录,customers
     *
     * @param customerId
     */
    CustomersBean selectByPrimaryKey(Long customerId);


    /**
     * 插入用户,返回ID
     *
     * @param record
     * @return
     * @throws Exception
     */
    Long insertCustomerForId(CustomersBean record) throws BusinessException;

    /**
     * 根据ID查询所有关联信息
     *
     * @param customerId
     * @return
     */
    CustomersDto selectCustomersByCustomerId(Long customerId) throws Exception;

    /**
     * 查询手机号是否有重复
     * @param list
     * @return
     */
//    List<CustomersDto> selectPhoneForeach(List<String> list,Long companyId);

    /**
     * 查询身份证号是否有重复
     * @param list
     * @return
     */
//    List<CustomersDto> selectidCardForeach(List<String> list,Long companyId);

    /**
     * 插入上传的员工信息
     *
     * @param companysBean
     * @param fileName
     * @return
     * @throws Exception
     */
    ResultResponse insertCustomersBatch(CompanysBean companysBean, String fileName) throws Exception;

    /**
     * 根据企业查询有效员工
     *
     * @param companyId
     * @return
     */
    List<CustomersBean> selectUserCustomerByCompanyId(Long companyId);

    /**
     * 更新用户信息
     *
     * @param customersBean
     * @param customersStationBean
     * @param customersPersonalBean
     * @return
     * @throws BusinessException
     */
    ResultResponse modifyCustomers(CustomersBean customersBean, CustomersStationBean customersStationBean, CustomersPersonalBean customersPersonalBean) throws BusinessException;

    /**
     * 更新用户基本信息
     *
     * @param customersBean
     * @return
     * @throws BusinessException
     */
    ResultResponse modifyCustomerBase(CustomersBean customersBean, Long companyId);

    /**
     * 查询身份证号是否有重复
     *
     * @param list type 1新增 2修改
     * @return
     */
    Map<Integer, List<CustomersDto>> selectidCardForeach(List<String> list, Long companyId, int type, long customerId);

    /**
     * 查询手机号是否有重复
     *
     * @param list
     * @return
     */
    Map<Integer, List<CustomersDto>> selectPhoneForeach(List<String> list, Long companyId, int type, long customerId);

    /**
     * 获取未定薪员工人数
     *
     * @param customersDto
     * @return
     */
    List<CustomersDto> selectComstomerCountByNoSaraly(CustomersDto customersDto);

    /**
     * 薪资获取员工数量
     *
     * @param startTimeStr
     * @param endTimeStr
     * @param companyId
     * @return
     * @throws ParseException
     */
    Map<String, Integer> querySaralyInfo(String startTimeStr, String endTimeStr, Long companyId) throws ParseException;


    /**
     * 条件查询当前员工的数量
     *
     * @param customersCountDto
     */
    long selectCustomersCountByCondition(CustomersDto customersCountDto);


    int updateCustomersByCustomerId(CustomersBean customersBean);

    /**
     * 查询所有未生成账户的员工
     *
     * @return
     */
    List<CustomersBean> selectCustoemrSubAccount();

    /**
     * 根据条件查询员工数量
     *
     * @param customersDto
     * @return
     */
    int selectCustomerNumber(CustomersDto customersDto);

    /**
     * 根据微信号唯一的openId查询员工绑定信息
     *
     * @param openId
     * @return
     */
    CustomerWechatBean selectCustomerWechatByOpenId(String openId);

    /**
     * 查询最新入职的员工
     *
     * @param phone
     * @return
     */
    List<CustomersDto> selectLastCustomerByPhone(String phone);

    /**
     * 更新员工头像
     *
     * @param map
     * @return
     */
    ResultResponse updateCustomerImgByCustomerId(Map map) throws BusinessException;

    /**
     * 查询是否符合进入入职规范页面
     *
     * @return
     */
    int selectCountForIsRedirect(long customerId);

    /**
     * 更改是否跳转到入职规范状态
     *
     * @param customersBean
     * @return
     */
    int updateIsRedirectState(CustomersBean customersBean);

    /**
     * 更改是否补全资料状态
     *
     * @param customersBean
     * @return
     */
    int updateIsComplementState(CustomersBean customersBean);

    /**
     * 查询在职员工数量
     *
     * @param memberCompanyId
     * @param startDate
     * @return
     */
    int selectLiveCountByCompanyId(Long memberCompanyId, long cycleId, Date startDate);

    /**
     * 查询未绑定员工
     *
     * @param phone
     * @return
     */
    List<CustomersDto> selectUnbindCustomers(String phone);

    /**
     * 循环查询员工信息
     * @param list
     * @return
     */
    List<CustomersBean> selectCustomersForeach(List<Long> list);

    /**
     * 更新员工社保资料审核状态
     * @param idCard
     * @param approveState
     * @param comment
     * @return
     */
    int updateShebaoStatusByIdcard(String idCard, Integer approveState, String comment);

    /**
     * 查询所有社保审核失败的数量
     * @param memberCompanyId
     * @return
     */
    int selectShebaoFailedCount(Long memberCompanyId);

    /**
     * 查询该公司每个月在职员工的数量
     * @param companyId
     * @param year
     * @return
     */
    List<CustomersDto> selectEveryMonthCustomerCounts(Long companyId, int year);
    /**
     * 查询员工列表
     * @param type
     * @param customersDto
     * @param customerCompanyId
     * @return
     * @throws BusinessException
     * @throws Exception
     */
    ResultResponse queryListByCondition(Integer type, CustomersDto customersDto, Long customerCompanyId)throws BusinessException,Exception;

    /**
     * 根据条件查询员工数量
     *
     * @param customersDto
     * @return
     */
    int queryCustomerCount(CustomersDto customersDto,int type);

    /**
     * 新增员工业务
     * @param customersDto
     * @param type
     * @throws BusinessException
     * @throws Exception
     */
    Long createCustomer(CustomersDto customersDto,int type)throws BusinessException,Exception;

    /**
     *获取调薪初始化数据
     * @param customerId
     * @return
     * @throws BusinessException
     * @throws Exception
     */
    CustomerResponse updateSalaryInit(Long customerId)throws BusinessException,Exception;

    /**
     * 更新用户基本信息
     *
     * @param customersBean
     * @return
     * @throws BusinessException
     */
    ResultResponse modifyCustomerBaseNew(CustomersBean customersBean, Long companyId);

    /**
     * 更新用户身份证信息
     *
     * @param customersBean
     * @return
     * @throws BusinessException
     */
    ResultResponse modifyCustomerIdcardNew(CustomersBean customersBean, Long companyId);

    /**
     * 查询未补全员工资料信息列表
     * @param customersDto
     * @param customerCompanyId
     * @return
     * @throws BusinessException
     * @throws Exception
     */
    ResultResponse queryInCompleteCustomerList(CustomersDto customersDto, Long customerCompanyId)throws BusinessException,Exception;

    /**
     * 更改未补全的信息
     * @param customersBean
     * @param companyId
     * @param type
     * @return
     */
    ResultResponse uptInCompleteCustomer(CustomersBean customersBean,Long companyId,Integer type);

    /**
     * 发送未补全资料员工手机短信
     * @param companyId
     * @throws BusinessException
     * @throws Exception
     */
    void sendInPlemeteMsg(Long companyId)throws BusinessException,Exception;

    /**
     * 获取要发送的未补全信息数量
     * @param companyId
     * @throws BusinessException
     * @throws Exception
     */
    Integer querySendInPlemeteMsgCount(Long companyId)throws BusinessException,Exception;

    /**
     * 插入上传的员工信息
     *
     * @param companysBean
     * @return
     * @throws Exception
     */
    ResultResponse customerUploadBatchNew(CompanysBean companysBean,byte[] in2b) throws BusinessException, Exception;

    /**
     * 插入未补全的员工信息
     *
     * @param companysBean
     * @return
     * @throws Exception
     */
    ResultResponse inCompleteUploadBatchNew(CompanysBean companysBean,byte[] in2b) throws BusinessException, Exception;

    /**
     * 员工离职
     *
     * @param customersDto
     * @return
     */
    ResultResponse customerDimissionNew(CustomersDto customersDto);

    /**
     *查询企业下的所有员工
     * @param companyId
     * @return
     */
    List<CustomersBean> selectCustomersForCompanyId(long companyId);

    /**
     * 新版员工微信 更新员工资料
     * @param customer
     * @return
     */
    int updateCustomerInfoByCustomerId(CustomersBean customer);

    /**
     *查询某个员工在日期范围内的工资调整情况
     * @param companyId
     * @return
     */
    public List<Map<String,String>> selectSalaryRecords(long companyId,Date startDay,Date endDay);
}
