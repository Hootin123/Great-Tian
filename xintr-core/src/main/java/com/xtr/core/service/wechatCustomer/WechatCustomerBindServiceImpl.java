package com.xtr.core.service.wechatCustomer;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.customer.CustomerWechatBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.dto.customer.*;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.wechatCustomer.WechatCustomerBindService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.comm.util.StringUtils;
import com.xtr.core.persistence.reader.customer.CustomerWechatReaderMapper;
import com.xtr.core.persistence.writer.customer.CustomerWechatWriterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:zhangshuai
 * @date: 2016/9/7.
 */
@Service("wechatCustomerBindService")
public class WechatCustomerBindServiceImpl implements WechatCustomerBindService {



    private static final Logger LOGGER = LoggerFactory.getLogger(WechatCustomerBindServiceImpl.class);

    @Resource
    private CustomerWechatReaderMapper customerWechatReaderMapper;

    @Resource
    private CustomerWechatWriterMapper customerWechatWriterMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CustomersService customersService;
    /**
     *
     * @param phone     手机号
     * @param phoneCode 短信验证码
     * @param map       微信号信息map
     * @return
     */
    @Transactional
    @Override
    public ResultResponse customerBind(String phone, String phoneCode, Map<String, String> map) throws Exception{
        ResultResponse resultResponse = new ResultResponse();
        if(StringUtils.isStrNull(phone)){
            throw new BusinessException("请输入手机号");
        }
        if(StringUtils.isStrNull(phoneCode)){
            throw new BusinessException("请输入短信验证码");
        }
        if(StringUtils.isEmpty(map)||map.size()<=0){
            throw new BusinessException("map参数为空值");
        }
        //判断当前手机验证码是否正确
        Object code = redisTemplate.opsForValue().get(PropertyUtils.getString("mobile.verification.code") + phone);
        if (code == null || !org.apache.commons.lang.StringUtils.equals(phoneCode.toLowerCase(), String.valueOf(code).toLowerCase())) {
            LOGGER.info("员工绑定微信手机验证码输入错误");
            resultResponse.setMessage("手机验证码输入错误");
            return resultResponse;
        }
        //判断是否已绑定
        String openId = map.get("openId");
        CustomerWechatBean customerWechat= customersService.selectCustomerWechatByOpenId(openId);
        if(customerWechat!=null){
            resultResponse.setMessage("您微信号已绑定过员工信息，请退出重新点击员工管理");
        }
        //查询最新入职的员工
        List <CustomersDto> list = customersService.selectUnbindCustomers(phone);
        if(null==list||list.size()<=0){
            resultResponse.setData("noInput");//没录入
            resultResponse.setMessage("您好！请联系贵公司人事添加您的手机号");
        }else {
          //员工绑定
            CustomersDto customersDto =null;
            for (CustomersDto customer:list){
                if(customer.getStationCustomerState()==1||customer.getStationCustomerState()==2){
                    customersDto = customer;
                    break;
                }
            }
            if(customersDto==null){
                customersDto = list.get(0);
            }
            if(customersDto.getCustomerSign()==0){
                resultResponse.setMessage("你还不能绑定，已经离职被删除了");
                return resultResponse;
            }
            LOGGER.error("员工绑定参数：customerId"+customersDto.getCustomerId());
           CustomerWechatBean customerWechatBean = new CustomerWechatBean();
          customerWechatBean.setWechatCustomerPhone(phone);//员工手机号
          customerWechatBean.setWechatCustomerId(customersDto.getCustomerId());//员工id
          customerWechatBean.setWechatBindTime(new Date());//员工绑定时间
            //LOGGER.info("员工绑定参数,获取nickName:"+new String(map.get("nickName")));
            if(!StringUtils.isStrNull(map.get("nickName"))) {
                customerWechatBean.setWechatNickName(new String(map.get("nickName").getBytes("iso8859-1"), "utf-8"));//员工绑定的微信号
            }

            customerWechatBean.setWechatOpenId(map.get("openId"));  //员工绑定的openid
          customerWechatBean.setWechatCompanyId(customersDto.getCustomerCompanyId());//企业id
          int insertResult= customerWechatWriterMapper.saveCustomerBind(customerWechatBean);
            if(insertResult!=1){
               LOGGER.info("插入员工绑定信息出现错误");
               throw new BusinessException("绑定失败");
            }
            Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("customerId",customersDto.getCustomerId());//员工id
            resultMap.put("companyId",customersDto.getCustomerCompanyId());//企业id
            resultMap.put("customerName",customersDto.getCustomerTurename());
            resultResponse.setData(resultMap);//员工id
            resultResponse.setSuccess(true);
            resultResponse.setMessage("操作成功");
        }
        return resultResponse;
    }

    /**
     * 查询员工信息
     * @param map
     * @return
     * @throws BusinessException
     */
    @Override
    public CustomerWechatBindDto selectCustomerInfoByCondition(Map<String, Object> map) throws BusinessException {
        return customerWechatReaderMapper.selectCustomerInfoByCondition(map);
    }

    /**
     * 查询当前员工本年度的工资列表
     * @param map
     * @return
     */
    @Override
    public CustomerYearSalaryDto selectCustomerYearSalarys(Map<String, Object> map) {
        return customerWechatReaderMapper.selectCustomerYearSalarys(map);
    }

    /**
     * 查询工资详情
     * @param map
     * @return
     */
    @Override
    public CustomerPayrollWechatBindDto selectSalaryDetail(Map<String, Object> map) {
        return customerWechatReaderMapper.selectSalaryDetail(map);
    }

    /**
     * 查询当前的工资明细
      * @param payrollId
     * @return
     */
    @Override
    public List<CustomerPayrollWechatBindDto> selectPayrollDetailByPayrollId(Long payrollId) {
        return customerWechatReaderMapper.selectPayrollDetailByPayrollId(payrollId);
    }

    /**
     * 微信员工查询年度工资单
     * @param companyId
     * @param customerId
     * @param year
     * @return
     */
    @Override
    public List<CustomerWechatBindDto> selectYearPayorders(String companyId, String customerId, String year) throws Exception{
         Long companyIdL =Long.parseLong(companyId);
         Long customerIdL = Long.parseLong(customerId);
        Map map =  new HashMap();
        map.put("companyId",companyIdL);
        map.put("customerId",customerIdL);
        map.put("year",year);
        return customerWechatReaderMapper.selectYearPayorders(map);
    }

    /**
     * 新版员工微信查看工资单详情
     * @param payrollId
     * @param companyId
     * @param customerId
     * @return
     */
    @Override
    public CustomerPayrollWechatBindDto selectSalaryDetailBy(String payrollId, String companyId, String customerId,String payCycleId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("payrollId",Long.parseLong(payrollId));
        map.put("companyId",Long.parseLong(companyId));
        map.put("customerId",Long.parseLong(customerId));
        map.put("payCycleId",Long.parseLong(payCycleId));
        return customerWechatReaderMapper.selectSalaryDetailBy(map);
    }

    /**
     * 员工查看信息
     * @param customerId
     * @return
     */
    @Override
    public CustomerWechatDto selectCustomerInfo(Long customerId) {
        return customerWechatReaderMapper.selectCustomerInfo(customerId);
    }
}
