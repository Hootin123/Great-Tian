package com.xtr.core.service.app;

import com.xtr.api.basic.AppResponse;
import com.xtr.api.domain.app.CustomerUserBean;
import com.xtr.api.domain.customer.CustomerRechargesBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.dto.app.CustomerUserDto;
import com.xtr.api.service.app.CustomerUserService;
import com.xtr.api.service.customer.CustomerRechargesService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.util.MethodUtil;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.comm.util.RandomDataUtil;
import com.xtr.comm.util.RegexUtil;
import com.xtr.core.persistence.reader.app.CustomerUserReaderMapper;
import com.xtr.core.persistence.writer.app.CustomerUserWriterMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/8 17:14
 */
@Service("customerUserService")
public class CustomerUserServiceImpl implements CustomerUserService {

    @Resource
    private CustomerUserWriterMapper customerUserWriterMapper;

    @Resource
    private CustomerUserReaderMapper customerUserReaderMapper;

    @Resource
    private CustomersService customersService;

    @Resource
    private CustomerRechargesService customerRechargesService;

    /**
     * 新写入数据库记录,customer_user
     *
     * @param record
     */
    public int insert(CustomerUserBean record) throws Exception {
        //用戶信息检查
        checkUserInfo(record);
        //状态 0=可用,1=禁用
        record.setState("0");
        //删除状态 0=未删除,1=已删除
        record.setIsDelete(0);
        //创建时间
        record.setCreateTime(new Date());
        //密码加密
        record.setSalt(RandomDataUtil.getRandomID());
        //密码
        record.setPwd(MethodUtil.MD5(record.getPwd() + record.getSalt()));
        //根据真实姓名、身份证号码匹配员工信息
//        CustomersBean customersBean = getCustomers(record);
        record.setIsMatching(0);
//        if (customersBean != null) {
//            //是否关联企业员工 0-未关联，1-已关联
//            record.setIsMatching(1);
//            //企业员工主键
//            record.setCustomerId(customersBean.getCustomerId());
//        }
        //存储头像
        return customerUserWriterMapper.insert(record);
    }

    /**
     * 根据手机号查询用户信息
     *
     * @param mobile
     * @return
     */
    public CustomerUserBean selectUserByMobile(String mobile) {
        if (StringUtils.isNotBlank(mobile)) {
            return customerUserReaderMapper.selectByUserMobile(mobile);
        } else {
            return null;
        }
    }

    /**
     * 登录
     *
     * @param mobile
     * @param pwd
     * @param appType
     * @return
     * @throws Exception
     */
    public AppResponse login(String mobile, String pwd, Integer appType) throws Exception {
        AppResponse appResponse = new AppResponse();
        CustomerUserBean customerUserBean = customerUserReaderMapper.selectByUserMobile(mobile);
        if (customerUserBean == null
                || !MethodUtil.ecompareMD5(pwd + customerUserBean.getSalt(), customerUserBean.getPwd())) {
            throw new BusinessException("请输入正确的用户名或密码");
        }
//        CustomerUserBean customerUserBean1 = selectCustomerUserInfo(mobile);
//        String ossUrl = PropertyUtils.getString("oss.download.url");
//        ossUrl = ossUrl.replace("bucketName", PropertyUtils.getString("oss.bucketName.img"));
//        InputStream inputStream = AliOss.downloadFile(PropertyUtils.getString("oss.bucketName.img"),customerUserBean1.getHeadUrl());

//        customerUserBean1.setHeadUrl(ossUrl + customerUserBean1.getHeadUrl());
        appResponse.setData(selectCustomerUserInfo(mobile));
        appResponse.setSuccess(true);
        return appResponse;
    }


    /**
     * 根据真实姓名、身份证号码匹配员工信息
     *
     * @param record
     * @return
     */
    private CustomersBean getCustomers(CustomerUserBean record) {
        CustomersBean customersBean = new CustomersBean();
        customersBean.setCustomerTurename(record.getFullName());
        customersBean.setCustomerIdcard(record.getIdCard());
        List<CustomersBean> list = customersService.getCustomers(customersBean);
        if (list.isEmpty())
            return null;
        return list.get(0);
    }


    /**
     * 检查用户注册信息
     *
     * @param record
     */
    private void checkUserInfo(CustomerUserBean record) {
        if (record != null) {

            CustomerUserBean customerUserBean = customerUserReaderMapper.selectByUserMobile(record.getMobilePhone());
            if (customerUserBean != null) {
                throw new BusinessException("该手机号已被注册");
            }

            if (StringUtils.isBlank(record.getPwd())) {
                throw new BusinessException("密码不能为空");
            } else if (StringUtils.isBlank(record.getFullName())) {
                throw new BusinessException("真实姓名不能为空");
            } else if (StringUtils.isBlank(record.getIdCard())) {
                throw new BusinessException("身份证号码不能为空");
            }

            if (!RegexUtil.checkIdCard(record.getIdCard())) {
                throw new BusinessException("身份证号码输入非法");
            }

            if (!RegexUtil.checkMobile(record.getMobilePhone())) {
                throw new BusinessException("请输入正确的手机号码");
            }
        } else {
            throw new BusinessException("参数不能为空");
        }
    }

    /**
     * 根据手机号修改密码
     *
     * @param mobile
     * @param pwd
     * @return
     * @throws Exception
     */
    public int updatePwd(String mobile, String pwd) throws Exception {
//        CustomerUserBean customerUserBean = customerUserReaderMapper.selectByUserMobile(mobile);
        CustomerUserBean customerUserBean = customerUserReaderMapper.selectByUserMobile(mobile);
        if (customerUserBean != null) {
            //更新时间
            customerUserBean.setUpdateTime(new Date());
            //密码加密
            customerUserBean.setSalt(RandomDataUtil.getRandomID());
            //密码
            customerUserBean.setPwd(MethodUtil.MD5(pwd + customerUserBean.getSalt()));
            return customerUserWriterMapper.updateByPrimaryKeySelective(customerUserBean);
        } else {
            throw new BusinessException("账户不存在");
        }
    }

    /**
     * 修改用户密码
     *
     * @param id
     * @param pwd
     * @param oldPwd
     * @return
     */
    public int updatePwd(Long id, String pwd, String oldPwd) throws Exception {
        //验证账户密码是否正确
        CustomerUserBean customerUserBean = selectByPrimaryKey(id);
        if (!MethodUtil.ecompareMD5(oldPwd + customerUserBean.getSalt(), customerUserBean.getPwd())) {
            throw new BusinessException("原密码错误");
        }
        return updatePwd(id, pwd);
    }

    /**
     * 根据ID修改密码
     *
     * @param id
     * @param pwd
     * @return
     * @throws Exception
     */
    private int updatePwd(Long id, String pwd) throws Exception {
//        CustomerUserBean customerUserBean = customerUserReaderMapper.selectByUserMobile(mobile);
        CustomerUserBean customerUserBean = customerUserReaderMapper.selectByPrimaryKey(id);
        if (customerUserBean != null) {
            //更新时间
            customerUserBean.setUpdateTime(new Date());
            //密码加密
            customerUserBean.setSalt(RandomDataUtil.getRandomID());
            //密码
            customerUserBean.setPwd(MethodUtil.MD5(pwd + customerUserBean.getSalt()));
            return customerUserWriterMapper.updateByPrimaryKeySelective(customerUserBean);
        } else {
            throw new BusinessException("账户不存在");
        }
    }

    /**
     * 提现申请
     *
     * @param id
     * @param cardNo
     * @return
     */
    public void withDrawals(Long id, BigDecimal amout, String cardNo) {
        CustomersBean customersBean = getCustomers(id, cardNo);
        if (customersBean != null) {
            //提现
//        customerRechargesService.customerWithdrawals();
            CustomerRechargesBean customerRechargesBean = new CustomerRechargesBean();
            //用户id
            customerRechargesBean.setRechargeCustomerId(customersBean.getCustomerId());
            //操作类型 类型 1充值 2提现
            customerRechargesBean.setRechargeType(2);
            //注备
            customerRechargesBean.setRechargeBak("App提现");
            //提现金额
            customerRechargesBean.setRechargeMoney(amout);
            //发起提现申请
            customerRechargesService.customerWithdrawals(customerRechargesBean);
        } else {
            throw new BusinessException("您不是企业员工，不可提现");
        }
    }


    /**
     * 获取账号绑定的员工信息
     *
     * @param id
     * @param cardNo
     * @return
     */
    private CustomersBean getCustomers(Long id, String cardNo) {
        //根据手机号获取用户信息
        CustomerUserBean customerUserBean = selectByPrimaryKey(id);
        if (customerUserBean == null) {
            throw new BusinessException("用户不存在");
        } else if (customerUserBean.getCustomerId() == null) {
            throw new BusinessException("非企业员工不能体现");
        }
        CustomersBean customersBean = new CustomersBean();
        customersBean.setCustomerId(customerUserBean.getCustomerId());
        // TODO: 2016/7/12 因为固定工资单，所以提现银行卡从数据库读取,银行卡不做匹配
        List list = customersService.getCustomers(customersBean);
        if (list.isEmpty()) {
            throw new BusinessException("对应员工不存在");
        }
        return customersBean;
    }


    /**
     * 查询用户详情
     *
     * @param mobile
     * @return
     */
    public CustomerUserDto selectCustomerUserInfo(String mobile) {
        CustomerUserDto customerUserDto = customerUserReaderMapper.selectCustomerUserInfo(mobile);
        customerUserDto.setHeadUrl(PropertyUtils.getString("oss.download.url") + customerUserDto.getHeadUrl());
        return customerUserDto;
    }

    /**
     * 修改用户头像,姓名等信息
     *
     * @param customerUserBean
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateCustomerHeader(CustomerUserBean customerUserBean) throws Exception {
//        CustomerUserBean customerUserBean = customerUserReaderMapper.selectByUserMobile(mobile);
        //传递的用户信息不为空才会更新信息
        if (customerUserBean != null && customerUserBean.getId() != null) {
            CustomerUserBean newCustomerUserBean = new CustomerUserBean();
            if (StringUtils.isNotBlank(customerUserBean.getMobilePhone())) {
                //更改的手机号必须是唯一的
                CustomerUserBean checkCustomerUserBean = new CustomerUserBean();
                checkCustomerUserBean.setMobilePhone(customerUserBean.getMobilePhone());
                List<CustomerUserBean> countList = selectByUserCardOrMobile(checkCustomerUserBean);
                if (countList != null && countList.size() > 1) {
                    throw new BusinessException("手机号已经被注册");
                }
                newCustomerUserBean.setMobilePhone(customerUserBean.getMobilePhone());
            }
            if (StringUtils.isNotBlank(customerUserBean.getIdCard())) {
                //更改的身份证号必须是唯一的
                CustomerUserBean checkCustomerUserBean = new CustomerUserBean();
                checkCustomerUserBean.setIdCard(customerUserBean.getIdCard());
                List<CustomerUserBean> countList = selectByUserCardOrMobile(checkCustomerUserBean);
                if (countList != null && countList.size() > 1) {
                    throw new BusinessException("身份证号已经被注册");
                }
                newCustomerUserBean.setIdCard(customerUserBean.getIdCard());
            }
            if (StringUtils.isNotBlank(customerUserBean.getFullName())) {
                newCustomerUserBean.setFullName(customerUserBean.getFullName());
            }
            //将头像图片的字符串进行base64解码,并重新命名上传服务器并将文件名称保存在数据库中
            if (StringUtils.isNotBlank(customerUserBean.getHeadUrl())) {
                BASE64Decoder decoder = new BASE64Decoder();
                //base64解码
                byte[] decoderBytes = decoder.decodeBuffer(customerUserBean.getHeadUrl());
                InputStream is = new ByteArrayInputStream(decoderBytes);
                String imgName = UUID.randomUUID().toString() + ".png";
                //上传文件到服务器
                AliOss.uploadFile(is, imgName, PropertyUtils.getString("oss.bucketName.img"));
                //存储文件名称
                newCustomerUserBean.setHeadUrl(imgName);
            }
            //更新时间
            newCustomerUserBean.setUpdateTime(new Date());
            newCustomerUserBean.setId(customerUserBean.getId());
            //更新用户信息
            return customerUserWriterMapper.updateByPrimaryKeySelective(newCustomerUserBean);
        } else {
            throw new BusinessException("传递的用户信息参数不存在");
        }
    }

    /**
     * 根据身份证或者手机号获取用户信息
     *
     * @param customerUserBean
     * @return
     */
    public List<CustomerUserBean> selectByUserCardOrMobile(CustomerUserBean customerUserBean) {
        return customerUserReaderMapper.selectByUserCardOrMobile(customerUserBean);
    }

    /**
     * 根据ID查询用户信息
     *
     * @param customerUserId
     * @return
     */
    public CustomerUserBean selectByPrimaryKey(Long customerUserId) {
        return customerUserReaderMapper.selectByPrimaryKey(customerUserId);
    }

    /**
     * 根据ID查询用户详细信息
     *
     * @param id
     * @return
     */
    public CustomerUserDto selectCustomerUserDetailById(Long id) {
        return customerUserReaderMapper.selectCustomerUserDetailById(id);
    }

    /**
     * 动态字段,根据主键来更新符合条件的数据库记录,customer_user
     *
     * @param record
     */
    public int updateByPrimaryKeySelective(CustomerUserBean record) {
        return customerUserWriterMapper.updateByPrimaryKeySelective(record);
    }
}
