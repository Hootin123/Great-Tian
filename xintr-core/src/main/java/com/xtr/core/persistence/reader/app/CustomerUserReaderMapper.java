package com.xtr.core.persistence.reader.app;

import com.xtr.api.domain.app.CustomerUserBean;
import com.xtr.api.dto.app.CustomerUserDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerUserReaderMapper {


    /**
     * 根据指定主键获取一条数据库记录,customer_user
     *
     * @param id
     */
    CustomerUserBean selectByPrimaryKey(Long id);

    /**
     * 根据手机号码获取用户信息
     *
     * @param mobilePhone
     * @return
     */
    CustomerUserBean selectByUserMobile(@Param("mobilePhone") String mobilePhone);

    /**
     * 查询用户详情
     *
     * @param mobile
     * @return
     */
    CustomerUserDto selectCustomerUserInfo(@Param("mobile") String mobile);

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
}