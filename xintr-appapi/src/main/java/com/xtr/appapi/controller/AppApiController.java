package com.xtr.appapi.controller;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.AppResponse;
import com.xtr.api.basic.SendMsgResponse;
import com.xtr.api.domain.account.SubAccountBean;
import com.xtr.api.domain.app.AppQuestionFeedbackBean;
import com.xtr.api.domain.app.AppVersionBean;
import com.xtr.api.domain.app.CustomerUserBean;
import com.xtr.api.domain.customer.CustomerMoneyRecordsBean;
import com.xtr.api.domain.customer.CustomerMsgsBean;
import com.xtr.api.domain.customer.CustomerSalarysBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.dto.account.SubAccountDto;
import com.xtr.api.dto.app.CustomerUserDto;
import com.xtr.api.service.account.SubAccountService;
import com.xtr.api.service.app.AppQuestionFeedbackService;
import com.xtr.api.service.app.AppVersionService;
import com.xtr.api.service.app.CustomerUserService;
import com.xtr.api.service.customer.CustomerMoneyRecordsService;
import com.xtr.api.service.customer.CustomerMsgsService;
import com.xtr.api.service.customer.CustomerSalarysService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.send.SendMsgService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.comm.util.RandomDataUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>app接口</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/7/8 11:17
 */
@Controller
@RequestMapping("api")
public class AppApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppApiController.class);

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private AppVersionService appVersionService;

    @Resource
    private CustomerUserService customerUserService;

    @Resource
    private CustomersService customersService;

    @Resource
    private SendMsgService sendMsgService;

    @Resource
    private SubAccountService subAccountService;

    @Resource
    private CustomerSalarysService customerSalarysService;

    @Resource
    private CustomerMoneyRecordsService customerMoneyRecordsService;

    @Resource
    private CustomerMsgsService customerMsgsService;

    @Resource
    private AppQuestionFeedbackService appQuestionFeedbackService;

    /**
     * 获取App版本号
     *
     * @return
     */
    @RequestMapping(value = "getAppVersion.htm", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse getAppVersion(@RequestParam("appType") Integer appType) {
        AppResponse appResponse = new AppResponse();
        try {
            AppVersionBean appVersionBean = appVersionService.selectAppVersion(appType);
            appResponse.setData(appVersionBean);
            appResponse.setSuccess(true);
        } catch (Exception e) {
            appResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        return appResponse;
    }

    /**
     * 判断用户是否已经注册
     * 返回false 未注册  返回true 已注册
     *
     * @param mobile
     * @return
     */
    @RequestMapping(value = "isRegister.htm", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse isRegister(@RequestParam("mobile") String mobile) {
        AppResponse appResponse = new AppResponse();
        CustomerUserBean customerUserBean = customerUserService.selectUserByMobile(mobile);
        if (customerUserBean == null) {
            //未注册
            appResponse.setSuccess(true);
        } else {
            appResponse.setMessage("该手机号已被注册");
        }
        return appResponse;
    }


    /**
     * 用户注册
     *
     * @param customerUserBean
     * @return
     */
    @RequestMapping(value = "register.htm", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse register(CustomerUserBean customerUserBean) {
        AppResponse appResponse = new AppResponse();
        try {
            CustomerUserBean customerUserBean1 = customerUserService.selectUserByMobile(customerUserBean.getMobilePhone());
            if (customerUserBean1 == null) {
                int resutl = customerUserService.insert(customerUserBean);
                if (resutl > 0) {
                    CustomerUserDto customerUserDto = customerUserService.selectCustomerUserInfo(customerUserBean.getMobilePhone());
                    appResponse.setData(customerUserDto);
                    appResponse.setSuccess(true);
                    String toKen = UUID.randomUUID().toString();
                    Long id = customerUserDto.getId();
                    //添加toKen到缓存中 --只保存30分钟
                    redisTemplate.opsForValue().set(PropertyUtils.getString("mobile.toKen") + id, toKen, 60 * 30, TimeUnit.SECONDS);
                    appResponse.setToKen(toKen);
                } else {
                    appResponse.setMessage("用户注册失败,请重试");
                }
            } else {
                appResponse.setMessage("改手机号已被注册");
            }
        } catch (Exception e) {
            appResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        return appResponse;
    }


    /**
     * 用户登录
     *
     * @param mobile
     * @param pwd
     * @param appType
     * @return
     */
    @RequestMapping(value = "login.htm", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse login(@RequestParam("mobile") String mobile, @RequestParam("pwd") String pwd,
                             @RequestParam("appType") Integer appType) {
        AppResponse appResponse = new AppResponse();
        try {
            appResponse = customerUserService.login(mobile, pwd, appType);
            if (appResponse.isSuccess()) {
                String toKen = UUID.randomUUID().toString();
                Long id = ((CustomerUserDto) appResponse.getData()).getId();
                //添加toKen到缓存中 --只保存30分钟
                redisTemplate.opsForValue().set(PropertyUtils.getString("mobile.toKen") + id, toKen, 60 * 30, TimeUnit.SECONDS);
                appResponse.setToKen(toKen);
            }
        } catch (Exception e) {
            appResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        return appResponse;
    }

    /**
     * 用户登出
     *
     * @param id
     */
    @RequestMapping(value = "logOut.htm", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse logOut(@RequestParam("id") Long id, @RequestParam("toKen") String toKen) {
        AppResponse appResponse = new AppResponse();
        if (checkToKen(id, toKen)) {
            redisTemplate.delete(PropertyUtils.getString("mobile.toKen") + id);
            appResponse.setSuccess(true);
        } else {
            appResponse.setMessage("非法请求");
        }
        return appResponse;
    }

    /**
     * 修改用户密码
     *
     * @param id
     * @param pwd
     * @param oldPwd
     * @return
     */
    @RequestMapping(value = "updatePwd.htm", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse updatePwd(@RequestParam("id") Long id,
                                 @RequestParam("pwd") String pwd,
                                 @RequestParam("oldPwd") String oldPwd) {
        AppResponse appResponse = new AppResponse();
//        String toKenId = UUID.randomUUID().toString();
        try {
            if (id != null && StringUtils.isNotBlank(pwd)) {
                customerUserService.updatePwd(id, pwd, oldPwd);
                appResponse.setSuccess(true);
            } else {
                appResponse.setMessage("参数不能为空");
            }
        } catch (Exception e) {
            appResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        //添加toKen到缓存中 --只保留30分钟
//        redisTemplate.opsForValue().set(PropertyUtils.getString("mobile.toKen") + mobile, toKenId, 60 * 30, TimeUnit.SECONDS);
//        appResponse.setToKen(toKenId);
        return appResponse;
    }

    /**
     * 发送短信验证码
     *
     * @param mobile
     * @return
     */
    @RequestMapping(value = "sendVerificationCode.htm", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse sendVerificationCode(@RequestParam("mobile") String mobile) {
        AppResponse appResponse = new AppResponse();
        try {
            if (StringUtils.isNotBlank(mobile)) {
                String verificationCode = RandomDataUtil.getRandomID(6);
                String paraSmsCode = PropertyUtils.getString("environ.istest");    //短信验证码生成环境，如果是test测试环境，永远是111111
                if ("1".equals(paraSmsCode)) {
                    verificationCode = "111111";
                }
                LOGGER.info("验证码：" + verificationCode);
                SendMsgResponse sendMsgResponse = sendMsgService.sendMsg(mobile, "验证码：" + verificationCode);
                if (!sendMsgResponse.isSuccess()) {
                    appResponse.setMessage(sendMsgResponse.getError());
                } else {
                    //将验证码放入缓存服务器 --只保留30分钟
                    redisTemplate.opsForValue().set(PropertyUtils.getString("mobile.verification.code") + mobile, verificationCode, 60 * 30, TimeUnit.SECONDS);
                    appResponse.setSuccess(true);
                }
            } else {
                appResponse.setMessage("手机号码不能为空");
            }
        } catch (Exception e) {
            appResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        return appResponse;
    }

    /**
     * 验证码校验
     *
     * @param verificationCode
     * @return
     */
    @RequestMapping(value = "validateVerificationCode.htm", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse validateVerificationCode(@RequestParam("mobile") String mobile,
                                                @RequestParam("verificationCode") String verificationCode) {
        AppResponse appResponse = new AppResponse();
        if (StringUtils.isNotBlank(verificationCode) && StringUtils.isNotBlank(mobile)) {
            Object code = redisTemplate.opsForValue().get(PropertyUtils.getString("mobile.verification.code") + mobile);
            if (code != null && StringUtils.equals(verificationCode.toLowerCase(), String.valueOf(code).toLowerCase())) {
                appResponse.setSuccess(true);
            }
        } else {
            appResponse.setMessage("参数不能为空");
        }
        return appResponse;
    }

    /**
     * 获取账户余额
     *
     * @param id
     * @param toKen
     * @return
     */
    @RequestMapping(value = "getUserBalance.htm", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse getUserBalance(@RequestParam("id") Long id, @RequestParam("toKen") String toKen) {
        AppResponse appResponse = new AppResponse();
        try {
            //检查toKen
            if (checkToKen(id, toKen)) {
                CustomerUserBean customerUserBean = customerUserService.selectByPrimaryKey(id);
                if (customerUserBean != null) {
                    SubAccountBean subAccountBean = subAccountService.selectByCustId(customerUserBean.getCustomerId(), 1);
                    SubAccountDto emptySubAccountDto = new SubAccountDto();
                    SubAccountDto subAccountDto = copy(subAccountBean, emptySubAccountDto);
                    if (subAccountDto == null) {
                        subAccountDto = new SubAccountDto();
                    }
                    //是否有未读信息
                    CustomerMsgsBean customerMsg = new CustomerMsgsBean();
                    customerMsg.setMsgCustomerId(customerUserBean.getCustomerId());
                    customerMsg.setMsgSign(1);
                    List<CustomerMsgsBean> customerMsgsList = customerMsgsService.selectNoReaderMsgByCustomerId(customerMsg);
                    if (customerMsgsList != null && customerMsgsList.size() > 0) {
                        subAccountDto.setIsNewMessage(1);
                    } else {
                        subAccountDto.setIsNewMessage(0);
                    }
                    //是否关联企业员工 0-未关联，1-已关联
                    if (customerUserBean.getIsMatching().intValue() == 1) {
                        //当月是否有工资
                        CustomerSalarysBean customerSalarysBean = new CustomerSalarysBean();
                        //获取当月
                        int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
                        int month = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
                        customerSalarysBean.setSalaryYear(year);
                        customerSalarysBean.setSalaryMonth(month);
                        customerSalarysBean.setSalaryCustomerId(customerUserBean.getCustomerId());
                        List<CustomerSalarysBean> customerSalaryList = customerSalarysService.selectCustomerSalarys(customerSalarysBean);
                        if (customerSalaryList != null && customerSalaryList.size() > 0) {
                            subAccountDto.setIsHaveSaraly(1);
                            subAccountDto.setRealSaraly(customerSalaryList.get(0).getSalaryActual() == null ? new BigDecimal(0) : customerSalaryList.get(0).getSalaryActual());
                        } else {
                            subAccountDto.setIsHaveSaraly(0);
                            subAccountDto.setRealSaraly(new BigDecimal(0));
                        }
                    } else {
                        subAccountDto.setIsHaveSaraly(0);
                        subAccountDto.setRealSaraly(new BigDecimal(0));
                    }
                    appResponse.setData(subAccountDto);
                    appResponse.setSuccess(true);
                    //        String toKenId = UUID.randomUUID().toString();
                    //添加toKen到缓存中 --只保留30分钟
                    redisTemplate.opsForValue().set(PropertyUtils.getString("mobile.toKen") + id, toKen, 60 * 30, TimeUnit.SECONDS);
                    appResponse.setToKen(toKen);
                } else {
                    appResponse.setMessage("账户不存在");
                }
            } else {
                appResponse.setMessage("非法请求");
            }
        } catch (Exception e) {
            appResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }

        return appResponse;
    }

    /**
     * 余额自动转入设置
     *
     * @param id
     * @param toKen
     * @param autoType
     * @return
     */
    @RequestMapping(value = "updateAutoTransfer.htm", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse updateAutoTransfer(@RequestParam("id") Long id, @RequestParam("toKen") String toKen,
                                          @RequestParam("autoType") Integer autoType) {
        AppResponse appResponse = new AppResponse();
        try {
            if (autoType != null) {
                //检查toKen
                if (checkToKen(id, toKen)) {
                    CustomerUserBean customerUserBean = customerUserService.selectByPrimaryKey(id);
                    if (customerUserBean != null) {
                        //更新账户余额设置
                        appResponse.setData(subAccountService.updateAutoTransfer(customerUserBean.getCustomerId(), 1, autoType));
                        appResponse.setSuccess(true);
                        //        String toKenId = UUID.randomUUID().toString();
                        //添加toKen到缓存中 -- 只保留30分钟
                        redisTemplate.opsForValue().set(PropertyUtils.getString("mobile.toKen") + id, toKen, 60 * 30, TimeUnit.SECONDS);
                        appResponse.setToKen(toKen);
                    } else {
                        appResponse.setMessage("账户不存在");
                    }
                } else {
                    appResponse.setMessage("非法请求");
                }
            } else {
                appResponse.setMessage("自动转入类型不能为空");
            }
        } catch (Exception e) {
            appResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }

        return appResponse;
    }


    /**
     * 密码重置
     *
     * @param mobile
     * @param verificationCode
     * @return
     */
    @RequestMapping(value = "resetPwd.htm", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse resetPwd(@RequestParam("mobile") String mobile, @RequestParam("verificationCode") String verificationCode, @RequestParam("newPwd") String newPwd) {
        AppResponse appResponse = new AppResponse();
        Long id = 0L;
        try {
            //检查验证码
            Object code = redisTemplate.opsForValue().get(PropertyUtils.getString("mobile.verification.code") + mobile);
            if (code != null && StringUtils.equals(verificationCode.toLowerCase(), String.valueOf(code).toLowerCase())) {
                //判断用户是否存在
                CustomerUserBean customerUserBean = customerUserService.selectUserByMobile(mobile);
                if (customerUserBean != null) {
//                    新密码
//                    String newPwd = RandomDataUtil.getRandomID(6);
                    id = customerUserBean.getId();
                    LOGGER.info("新密码：" + newPwd);
                    SendMsgResponse sendMsgResponse = sendMsgService.sendMsg(mobile, "您的登录密码已被重置，新密码：" + newPwd);
                    if (sendMsgResponse.isSuccess()) {
                        //更新密码
                        customerUserService.updatePwd(mobile, newPwd);
                        appResponse.setSuccess(true);
                        String toKenId = UUID.randomUUID().toString();
                        //添加toKen到缓存中 -- 只保留30分钟
                        redisTemplate.opsForValue().set(PropertyUtils.getString("mobile.toKen") + id, toKenId, 60 * 30, TimeUnit.SECONDS);
                        appResponse.setToKen(toKenId);
                    } else {
                        appResponse.setMessage(sendMsgResponse.getError());
                    }
                } else {
                    appResponse.setMessage("用户不存在");
                }
            } else {
                appResponse.setMessage("验证码错误");
            }
        } catch (Exception e) {
            appResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        return appResponse;
    }

    /**
     * 提现申请
     *
     * @param id
     * @param toKen
     * @param amount
     * @return
     */
    @RequestMapping(value = "withDrawals.htm", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse withDrawals(@RequestParam("id") Long id, @RequestParam("toKen") String toKen,
                                   @RequestParam("amount") BigDecimal amount, @RequestParam("cardNo") String cardNo) {
        AppResponse appResponse = new AppResponse();
        try {
            if (checkToKen(id, toKen)) {
                //提现申请
                customerUserService.withDrawals(id, amount, cardNo);
                //        String toKenId = UUID.randomUUID().toString();
                appResponse.setSuccess(true);
                //添加toKen到缓存中 -- 只保留30分钟
                redisTemplate.opsForValue().set(PropertyUtils.getString("mobile.toKen") + id, toKen, 60 * 30, TimeUnit.SECONDS);
                appResponse.setToKen(toKen);
            } else {
                appResponse.setMessage("非法请求");
            }
        } catch (Exception e) {
            appResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }

        return appResponse;
    }


    /**
     * 查工资
     *
     * @param id
     * @param toKen
     * @param year
     * @param month
     * @return
     */
    @RequestMapping(value = "querySalary.htm", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse querySalary(@RequestParam("id") Long id, @RequestParam("toKen") String toKen,
                                   @RequestParam("year") Integer year, @RequestParam("month") Integer month) {
        AppResponse appResponse = new AppResponse();
        try {
            if (checkToKen(id, toKen)) {
                //获取用户信息
                CustomerUserBean customerUserBean = customerUserService.selectByPrimaryKey(id);
                if (customerUserBean != null) {
                    CustomerSalarysBean customerSalarysBean = new CustomerSalarysBean();
                    customerSalarysBean.setSalaryCustomerId(customerUserBean.getCustomerId());
                    customerSalarysBean.setSalaryYear(year);
                    customerSalarysBean.setSalaryMonth(month);
                    List list = customerSalarysService.selectCustomerSalarys(customerSalarysBean);
                    if (list.isEmpty()) {
                        throw new BusinessException("该月工资单不存在");
                    } else {
                        appResponse.setData(list.get(0));
                        appResponse.setSuccess(true);
                        //        String toKenId = UUID.randomUUID().toString();
                        //添加toKen到缓存中 -- 只保留30分钟
                        redisTemplate.opsForValue().set(PropertyUtils.getString("mobile.toKen") + id, toKen, 60 * 30, TimeUnit.SECONDS);
                        appResponse.setToKen(toKen);
                    }
                } else {
                    throw new BusinessException("该用户不存在");
                }
            } else {
                throw new BusinessException("非法请求");
            }
        } catch (Exception e) {
            appResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }

        return appResponse;
    }


    /**
     * 校验口令
     *
     * @param id
     * @param toKen
     * @return
     */
    private boolean checkToKen(Long id, String toKen) {
        Object oldToKen = redisTemplate.opsForValue().get(PropertyUtils.getString("mobile.toKen") + id);
        return oldToKen != null && StringUtils.equals(toKen, String.valueOf(oldToKen));
    }

    /**
     * 修改用户头像,姓名等信息
     *
     * @param customerUserBean
     * @return
     */
    @RequestMapping(value = "updateCustomerHeader.htm", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse updateCustomerHeader(CustomerUserBean customerUserBean, @RequestParam("toKen") String toKen) {
        AppResponse appResponse = new AppResponse();
        try {
            if (checkToKen(customerUserBean.getId(), toKen)) {
                LOGGER.info("头像URL:" + JSON.toJSONString(customerUserBean));
                int resultCount = customerUserService.updateCustomerHeader(customerUserBean);
                if (resultCount > 0) {
                    CustomerUserDto customerUserDto = customerUserService.selectCustomerUserDetailById(customerUserBean.getId());
                    if (StringUtils.isNotBlank(customerUserDto.getHeadUrl())) {
                        customerUserDto.setHeadUrl(PropertyUtils.getString("oss.download.url") + customerUserDto.getHeadUrl());
                    }
                    appResponse.setData(customerUserDto);
                    appResponse.setSuccess(true);
                    //添加toKen到缓存中 -- 只保留30分钟
                    redisTemplate.opsForValue().set(PropertyUtils.getString("mobile.toKen") + customerUserBean.getId(), toKen, 60 * 30, TimeUnit.SECONDS);
                    appResponse.setToKen(toKen);
                } else {
                    appResponse.setMessage("用户更新失败,请重试");
                }
            } else {
                throw new BusinessException("非法请求");
            }
        } catch (BusinessException e) {
            appResponse.setMessage(e.getMessage());
        } catch (Exception e) {
            appResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        return appResponse;
    }

    /**
     * 客户资金列表
     *
     * @param id
     * @param toKen
     * @param customerMoneyRecordsBean
     * @return
     * @pa
     */
    @RequestMapping(value = "queryMoneyRecords.htm", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse queryMoneyRecords(@RequestParam("id") Long id, @RequestParam("toKen") String toKen, CustomerMoneyRecordsBean customerMoneyRecordsBean,
                                         @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "pagesize", defaultValue = "10") Integer pagesize) {
        AppResponse appResponse = new AppResponse();
        try {
            if (checkToKen(id, toKen)) {
                if (id == null || id <= 0) {
                    throw new BusinessException("传递的用户编号参数不存在");
                }
                //获取用户信息
                CustomerUserBean customerUserBean = customerUserService.selectByPrimaryKey(id);
                if (customerUserBean != null) {
                    CustomerMoneyRecordsBean newCustomerMoneyRecordsBean = new CustomerMoneyRecordsBean();
                    if (customerMoneyRecordsBean != null && customerMoneyRecordsBean.getRecordType() != null && customerMoneyRecordsBean.getRecordType().intValue() != 0) {
                        newCustomerMoneyRecordsBean.setRecordType(customerMoneyRecordsBean.getRecordType());
                    }
                    if (customerMoneyRecordsBean != null && customerMoneyRecordsBean.getRecordSource() != null && customerMoneyRecordsBean.getRecordSource().intValue() != 0) {
                        newCustomerMoneyRecordsBean.setRecordSource(customerMoneyRecordsBean.getRecordSource());
                    }
                    if (customerMoneyRecordsBean != null && customerMoneyRecordsBean.getRecordAddtime() != null) {
//                        newCustomerMoneyRecordsBean.setRecordAddtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(openData));
                        customerMoneyRecordsBean.setRecordAddtime(customerMoneyRecordsBean.getRecordAddtime());
                    }
                    newCustomerMoneyRecordsBean.setRecordCustomerId(customerUserBean.getCustomerId());
                    newCustomerMoneyRecordsBean.setPageIndex(page);
                    newCustomerMoneyRecordsBean.setPageSize(pagesize);
                    //查询信息
                    appResponse = customerMoneyRecordsService.selectByCustomerCondition(newCustomerMoneyRecordsBean);
                    LOGGER.info("获取客户资金列表结果:" + JSON.toJSONString(appResponse));

                    //添加toKen到缓存中 -- 只保留30分钟
                    redisTemplate.opsForValue().set(PropertyUtils.getString("mobile.toKen") + id, toKen, 60 * 30, TimeUnit.SECONDS);
                    appResponse.setToKen(toKen);
                } else {
                    throw new BusinessException("该用户不存在");
                }
            } else {
                throw new BusinessException("非法请求");
            }
        } catch (Exception e) {
            appResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }

        return appResponse;
    }

    /**
     * 我的消息列表
     *
     * @param id
     * @param toKen
     * @param customerMsgsBean
     * @return
     */
    @RequestMapping(value = "queryCustomerMsgList.htm", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse queryCustomerMsgList(@RequestParam("id") Long id, @RequestParam("toKen") String toKen, CustomerMsgsBean customerMsgsBean,
                                            @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "pagesize", defaultValue = "10") Integer pagesize) {
        AppResponse appResponse = new AppResponse();
        try {
            if (checkToKen(id, toKen)) {
                if (id == null || id <= 0) {
                    throw new BusinessException("传递的用户编号参数不存在");
                }
                //获取用户信息
                CustomerUserBean customerUserBean = customerUserService.selectByPrimaryKey(id);
                if (customerUserBean != null) {
                    CustomerMsgsBean newCustomerMsgsBean = new CustomerMsgsBean();
                    if (customerMsgsBean != null && customerMsgsBean.getMsgType() != null && customerMsgsBean.getMsgType().intValue() != 0) {
                        newCustomerMsgsBean.setMsgType(customerMsgsBean.getMsgType());
                    }
                    if (customerMsgsBean != null && customerMsgsBean.getMsgSign() != null && customerMsgsBean.getMsgSign().intValue() != 0) {
                        newCustomerMsgsBean.setMsgSign(customerMsgsBean.getMsgSign());
                    }
                    newCustomerMsgsBean.setMsgCustomerId(customerUserBean.getCustomerId());
                    newCustomerMsgsBean.setPageIndex(page);
                    newCustomerMsgsBean.setPageSize(pagesize);
                    //查询信息
                    appResponse = customerMsgsService.selectMsgByCustomerCondition(newCustomerMsgsBean);
                    //添加toKen到缓存中 -- 只保留30分钟
                    redisTemplate.opsForValue().set(PropertyUtils.getString("mobile.toKen") + id, toKen, 60 * 30, TimeUnit.SECONDS);
                    appResponse.setToKen(toKen);
                    LOGGER.info("获取我的消息列表结果:" + JSON.toJSONString(appResponse));
                } else {
                    throw new BusinessException("该用户不存在");
                }
            } else {
                throw new BusinessException("非法请求");
            }
        } catch (Exception e) {
            appResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }

        return appResponse;
    }

    /**
     * 我的消息单条信息
     *
     * @param id 消息ID
     * @return
     */
    @RequestMapping(value = "queryCustomerMsgOne.htm", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse queryCustomerMsgOne(@RequestParam("id") Long id) {
        AppResponse appResponse = new AppResponse();
        try {
//            if (checkToKen(id, toKen)) {@RequestParam("toKen") String toKen
            if (id == null || id <= 0) {
                throw new BusinessException("传递的消息编号参数不存在");
            }
            //查询信息
            CustomerMsgsBean customerMsgsBean = customerMsgsService.selectByPrimaryKey(id);
            LOGGER.info("获取我的单条消息结果:" + JSON.toJSONString(customerMsgsBean));
            appResponse.setData(customerMsgsBean);
            appResponse.setSuccess(true);
//            } else {
//                throw new BusinessException("非法请求");
//            }
        } catch (Exception e) {
            appResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
//        String toKenId = UUID.randomUUID().toString();
//        //添加toKen到缓存中 -- 只保留30分钟
//        redisTemplate.opsForValue().set(PropertyUtils.getString("mobile.toKen") + id, toKenId, 60 * 30, TimeUnit.SECONDS);
//        appResponse.setToKen(toKenId);
        return appResponse;
    }

    /**
     * 新增问题反馈
     *
     * @param appQuestionFeedbackBean
     * @return
     */
    @RequestMapping(value = "addAppQuestionFeedback.htm", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse addAppRequestFeedback(AppQuestionFeedbackBean appQuestionFeedbackBean) {
        AppResponse appResponse = new AppResponse();
        try {
            if (appQuestionFeedbackBean != null) {
                appQuestionFeedbackBean.setFeedbackCreatetime(new Date());
                appQuestionFeedbackBean.setFeedbackUpdatetime(new Date());
                int resutl = appQuestionFeedbackService.insert(appQuestionFeedbackBean);
                if (resutl > 0) {
                    appResponse.setSuccess(true);
                } else {
                    appResponse.setMessage("新增问题反馈失败");
                }
            } else {
                throw new BusinessException("传递的参数不存在");
            }
        } catch (Exception e) {
            appResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        return appResponse;
    }

    /**
     * banner
     *
     * @return
     */
    @RequestMapping(value = "queryBannerList.htm", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse queryBannerList() {
        AppResponse appResponse = new AppResponse();
        List<String> bannerList = new ArrayList<String>();
        if (StringUtils.isNotBlank(PropertyUtils.getString("app.banner1"))) {
            bannerList.add(PropertyUtils.getString("app.banner1"));
        }
        if (StringUtils.isNotBlank(PropertyUtils.getString("app.banner2"))) {
            bannerList.add(PropertyUtils.getString("app.banner2"));
        }
        if (StringUtils.isNotBlank(PropertyUtils.getString("app.banner3"))) {
            bannerList.add(PropertyUtils.getString("app.banner3"));
        }


//        bannerList.add("http://192.168.1.26:8080/xintr-company/resource/img/logo.png");
//        bannerList.add("http://192.168.1.26:8080/xintr-company/resource/img/lobg.png");
        appResponse.setSuccess(true);
        appResponse.setData(bannerList);
        return appResponse;
    }

    //将一个对象的值复制到另一个对象
    public SubAccountDto copy(SubAccountBean src, SubAccountDto dest) {
        if (src != null) {
            dest.setAmout(src.getAmout());
            dest.setAmount(src.getAmout());
            dest.setCashAmout(src.getCashAmout());
            dest.setCheckValue(src.getCheckValue());
            dest.setCreateTime(src.getCreateTime());
            dest.setCustId(src.getCustId());
            dest.setFreezeCashAmount(src.getFreezeCashAmount());
            dest.setHash(src.getHash());
            dest.setId(src.getId());
            dest.setUpdateTime(src.getUpdateTime());
            dest.setState(src.getState());
            dest.setUncashAmount(src.getUncashAmount());
            dest.setProperty(src.getProperty());
            dest.setIsAutoTransfer(src.getIsAutoTransfer());
            dest.setFreezeUncashAmount(src.getFreezeUncashAmount());
        }

        return dest;
    }

    /**
     * 用户认证
     *
     * @param id
     * @param toKen
     * @param brankNumber
     * @return
     */
    @RequestMapping(value = "realNameAuthentication.htm", method = RequestMethod.POST)
    @ResponseBody
    public AppResponse realNameAuthentication(@RequestParam("id") Long id, @RequestParam("toKen") String toKen, @RequestParam("brankNumber") String brankNumber) {
        AppResponse appResponse = new AppResponse();
        try {
            if (checkToKen(id, toKen)) {
                CustomerUserBean customerUserBean = customerUserService.selectByPrimaryKey(id);
                if (customerUserBean != null) {
                    //根据身份证号获取员工信息
                    CustomersBean customersBean = new CustomersBean();
                    customersBean.setCustomerIdcard(customerUserBean.getIdCard());
                    customersBean.setCustomerTurename(customerUserBean.getFullName());
                    customersBean.setCustomerSign(Integer.valueOf(1));
                    List<CustomersBean> list = customersService.getCustomers(customersBean);
                    if (!list.isEmpty()) {
                        String customerBankNumber = list.get(0).getCustomerBanknumber();
                        if (!StringUtils.equals(customerBankNumber, brankNumber)) {
                            throw new BusinessException("未能匹配到工资卡");
                        }
                        //认证通过
                        customerUserBean.setIsMatching(new Integer(1));
                        customerUserBean.setCustomerId(list.get(0).getCustomerId());
                        customerUserService.updateByPrimaryKeySelective(customerUserBean);
                        CustomerUserDto customerUserDto = customerUserService.selectCustomerUserInfo(customerUserBean.getMobilePhone());
                        appResponse.setData(customerUserDto);
                        appResponse.setSuccess(true);
                        redisTemplate.opsForValue().set(PropertyUtils.getString("mobile.toKen") + id, toKen, 60 * 30, TimeUnit.SECONDS);
                        appResponse.setToKen(toKen);
                    } else {
                        throw new BusinessException("用户认证失败，姓名【" + customersBean.getCustomerTurename() + "】身份证号【" + customersBean.getCustomerIdcard() + "】未匹配到员工信息");
                    }
                } else {
                    throw new BusinessException("该用户不存在");
                }
            } else {
                throw new BusinessException("非法请求");
            }
        } catch (Exception e) {
            appResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        return appResponse;
    }


}
