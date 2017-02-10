package com.xtr.core.service.order;

import com.xtr.api.service.order.IdGeneratorService;
import com.xtr.comm.constant.ActivityReceiveConstant;
import com.xtr.comm.constant.CompanyBorrowConstant;
import com.xtr.comm.constant.CompanyRechargeConstant;
import com.xtr.comm.constant.CustomerRechargeConstant;
import com.xtr.comm.enums.BusinessEnum;
import com.xtr.comm.util.DateUtil;
import com.xtr.core.persistence.reader.company.CompanyBorrowBillsReaderMapper;
import com.xtr.core.persistence.reader.company.CompanyRechargesReaderMapper;
import com.xtr.core.persistence.reader.company.CompanysReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomerRechargesReaderMapper;
import com.xtr.core.persistence.writer.company.CompanysWriterMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * <p>ID生成服务</p>
 *
 * @author 任齐
 * @createTime: 2016/8/4 13:23
 */
@Service("idGeneratorService")
public class IdGeneratorServiceImpl implements IdGeneratorService {

    /**
     * 随机种子
     */
    private static final Random R = new Random();

    /**
     * 每日订单当前存储量, 暂时10个业务
     *
     * 存储结构
     * {
     *     20160804_01 : 2,
     *     20160804_02 : 1
     * }
     *
     */
    private static final Map<String, Integer> ORDER_COUNT_POOL = new HashMap<>(10);

    @Resource
    private CompanyRechargesReaderMapper companyRechargesReaderMapper;

    @Resource
    private CustomerRechargesReaderMapper customerRechargesReaderMapper;

    @Resource
    private CompanyBorrowBillsReaderMapper companyBorrowBillsReaderMapper;

    @Resource
    private CompanysReaderMapper companysReaderMapper;

    /**
     * 获取订单id
     *
     * @param businessEnum  业务类型
     * @return
     */
    @Override
    public String getOrderId(BusinessEnum businessEnum){

        String date = DateUtil.getCurrDate("yyyyMMdd");
        String key = businessEnum.getCode() + "_" + date;

        synchronized(IdGeneratorServiceImpl.class){
            Integer num = ORDER_COUNT_POOL.get(key);
            if(num == null){
                // 企业充值
                if(businessEnum.equals(BusinessEnum.COMPANY_RECHARGE)){
                    num = companyRechargesReaderMapper.getTodayOrderNum(CompanyRechargeConstant.RECHARGE_TYPE) + 1;
                }
                // 企业提现
                if(businessEnum.equals(BusinessEnum.COMPANY_WITHDRAWALS)){
                    num = companyRechargesReaderMapper.getTodayOrderNum(CompanyRechargeConstant.WITHDRAW_TYPE) + 1;
                }
                // 企业发工资
                if(businessEnum.equals(BusinessEnum.SEND_SALARY)){
                    num = companyRechargesReaderMapper.getTodayOrderNum(CompanyRechargeConstant.SEND_SALARY_TYPE) + 1;
                }
                // 个人提现
                if(businessEnum.equals(BusinessEnum.PERSONAL_WITHDRAWALS)){
                    num = customerRechargesReaderMapper.getTodayOrderNum(CustomerRechargeConstant.WITHDRAW_TYPE) + 1;
                }

                // 企业借款
                if(businessEnum.equals(BusinessEnum.COMPANY_ADVACE_BORROW)){
                    num = companyBorrowBillsReaderMapper.getTodayOrderNum(CompanyBorrowConstant.ADVACE) + 1;
                }
                if(businessEnum.equals(BusinessEnum.COMPANY_FINANCE_BORROW)){
                    num = companyBorrowBillsReaderMapper.getTodayOrderNum(CompanyBorrowConstant.FINANCE) + 1;
                }
                if(businessEnum.equals(BusinessEnum.COMPANY_PAYROLL_BORROW)){
                    num = companyBorrowBillsReaderMapper.getTodayOrderNum(CompanyBorrowConstant.PAYROLL) + 1;
                }
                // 5元注册红包
                if(businessEnum.equals(BusinessEnum.COMPANY_WECHAT_FIVE_REDS)) {//微信红包
                    num = companysReaderMapper.getTodayWechatOrderNum(ActivityReceiveConstant.FIVE_RED) + 1;
                }
                // 20元注册红包
                if(businessEnum.equals(BusinessEnum.COMPANY_WECHAT_ADUIT_REDS)) {//微信红包
                    num = companysReaderMapper.getTodayWechatOrderNum(ActivityReceiveConstant.AUDIT_RED) + 1;
                }
                // 20元注册红包
                if(businessEnum.equals(BusinessEnum.COMPANY_SHEBAO_ORDER)) {//微信红包
                    num = companysReaderMapper.getTodayWechatOrderNum(Integer.valueOf(BusinessEnum.COMPANY_SHEBAO_ORDER.getCode())) + 1;
                }

            } else{
                num += 1;
            }
            ORDER_COUNT_POOL.put(key, num);
            String seqStr = String.format("%05d", num);
            int random = R.nextInt(10)%(10);
            return date + seqStr + random + businessEnum.getCode();
        }

    }

}
