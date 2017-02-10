package com.xtr.api.service.app;

import com.xtr.api.basic.AppResponse;
import com.xtr.api.domain.app.CustomerUserBean;
import com.xtr.api.dto.app.CustomerUserDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/8 17:13
 */
public interface CustomerUserService {

    /**
     * 新写入数据库记录,customer_user
     *
     * @param record
     */
    int insert(CustomerUserBean record) throws Exception;

    /**
     * 用户登录
     *
     * @param mobile
     * @param pwd
     * @param appType
     * @return
     * @throws Exception
     */
    AppResponse login(String mobile, String pwd, Integer appType) throws Exception;

    /**
     * 根据手机号查询用户信息
     *
     * @param mobile
     * @return
     */
    CustomerUserBean selectUserByMobile(String mobile);

    /**
     * 修改用户密码
     *
     * @param mobile
     * @param pwd
     * @return
     */
    int updatePwd(String mobile, String pwd) throws Exception;

    /**
     * 修改用户密码
     *
     * @param id
     * @param pwd
     * @param oldPwd
     * @return
     */
    int updatePwd(Long id, String pwd, String oldPwd) throws Exception;

    /**
     * 提现申请
     *
     * @param id
     * @param amout
     * @param cardNo
     * @return
     */
    void withDrawals(Long id, BigDecimal amout, String cardNo);

    /**
     * 查询用户详情
     *
     * @param mobile
     * @return
     */
    CustomerUserDto selectCustomerUserInfo(String mobile);

    /**
     * 修改用户头像,姓名等信息
     * @param customerUserBean
     * @return
     * @throws Exception
     */
    int updateCustomerHeader(CustomerUserBean customerUserBean) throws Exception;

    /**
     * 根据身份证或者手机号获取用户信息
     * @param customerUserBean
     * @return
     */
    List<CustomerUserBean> selectByUserCardOrMobile(CustomerUserBean customerUserBean);

    /**
     * 根据ID查询用户详细信息
     * @param id
     * @return
     */
    CustomerUserDto selectCustomerUserDetailById(Long id);

    /**
     * 根据ID查询用户信息
     * @param customerUserId
     * @return
     */
    CustomerUserBean selectByPrimaryKey(Long customerUserId);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,customer_user
     *
     * @param record
     */
    int updateByPrimaryKeySelective(CustomerUserBean record);
}
