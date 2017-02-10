package com.xtr.core.service.company;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.SubAccountBean;
import com.xtr.api.domain.company.CompanySalaryExcelBean;
import com.xtr.api.dto.company.CompanyAccountBorrowDto;
import com.xtr.api.dto.company.CompanyAccountPropertyDto;
import com.xtr.api.dto.company.CompanyRepayAccountDto;
import com.xtr.api.service.account.RedEnvelopeService;
import com.xtr.api.service.company.CompanyAccountService;
import com.xtr.api.service.company.CompanyBorrowBillsService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.core.persistence.reader.account.SubAccountReaderMapper;
import com.xtr.core.persistence.reader.company.CompanyBorrowBillsReaderMapper;
import com.xtr.core.persistence.reader.company.CompanySalaryExcelReaderMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>我的账户</p>
 *
 * @author 任齐
 * @createTime: 2016/7/1 10:38
 */
@Service("companyAccountService")
public class CompanyAccountServiceImpl implements CompanyAccountService {

    @Resource
    private CompanyBorrowBillsReaderMapper companyBorrowBillsReaderMapper;

    @Resource
    private CompanyBorrowBillsService companyBorrowBillsService;

    @Resource
    private SubAccountReaderMapper subAccountReaderMapper;

    @Resource
    private CompanySalaryExcelReaderMapper companySalaryExcelReaderMapper;

    @Resource
    private RedEnvelopeService redEnvelopeService;

    /**
     * 根据企业id查询企业账户资产
     * <p>
     * 账户余额 = 冻结金额 + 非冻结金额
     * 冻结金额 = 垫发借款
     * 可用余额 = 账户余额 - 冻结金额
     *
     * @param companyId
     * @return
     */
    public CompanyAccountPropertyDto selectAccountProperty(Long companyId) throws BusinessException {
        if (null == companyId) {
            throw new BusinessException("企业id不能为空");
        }

        CompanyAccountPropertyDto companyAccountPropertyDto = new CompanyAccountPropertyDto();

        // 查询红包账户余额
        BigDecimal redAmout = redEnvelopeService.getAmout(companyId, null);
        companyAccountPropertyDto.setRedAmout(redAmout);

        SubAccountBean subAccountBean = subAccountReaderMapper.selectByCustId(companyId, 2);

        if (null != subAccountBean) {
            // 查询冻结金额
            companyAccountPropertyDto.setBlock(subAccountBean.getFreezeCashAmount().add(subAccountBean.getFreezeUncashAmount()));
            // 查询可用金额
            companyAccountPropertyDto.setAvailable(subAccountBean.getCashAmout());
            // 查询账户余额
            companyAccountPropertyDto.setBalance(subAccountBean.getAmout());
            // 借款金额
            companyAccountPropertyDto.setBorrow(subAccountBean.getUncashAmount());

            // 计算余额百分比
            Double blockPercent = companyAccountPropertyDto.getBlock().doubleValue() / companyAccountPropertyDto.getBalance().doubleValue();
            Double availablePercent = companyAccountPropertyDto.getAvailable().doubleValue() / companyAccountPropertyDto.getBalance().doubleValue();

            if(blockPercent != null && blockPercent >= 0 && null != availablePercent && availablePercent >= 0){
                BigDecimal big1 = new BigDecimal(blockPercent * 100);
                double d1 = big1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                BigDecimal big2 = new BigDecimal(availablePercent * 100);
                double d2 = big2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                companyAccountPropertyDto.setBlockPercent(d1);
                companyAccountPropertyDto.setAvailablePercent(d2);
            }
        }

        // 查询最后三次发工资
        List<CompanySalaryExcelBean> companySalaryExcelBeens = companySalaryExcelReaderMapper.getLastSalary(companyId, 3);
        companyAccountPropertyDto.setPayDates(companySalaryExcelBeens);

        return companyAccountPropertyDto;
    }

    /**
     * 根据企业id查询企业应付款数据
     *
     * @param companyId
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws BusinessException
     */
    public ResultResponse selectAccountBorrow(Long companyId, int pageIndex, int pageSize) throws BusinessException {
        if (null == companyId) {
            throw new BusinessException("企业id不能为空");
        }

        ResultResponse resultResponse = new ResultResponse();

        CompanyAccountBorrowDto companyAccountBorrowDto = new CompanyAccountBorrowDto();

        // 应还总额
        BigDecimal repayAll = companyBorrowBillsService.selectRepayMoney(companyId, null);
        companyAccountBorrowDto.setRepaymentTotal(repayAll);

        // 最近30天待还款
        BigDecimal monthRepay = companyBorrowBillsService.selectRepayMoney(companyId, 1);
        companyAccountBorrowDto.setMonthRepayment(monthRepay);

        // 待还款订单
        PageList<CompanyRepayAccountDto> list = companyBorrowBillsReaderMapper.selectRepayPageList(companyId, new PageBounds(pageIndex, pageSize));
        companyAccountBorrowDto.setBills(list);

        resultResponse.setData(companyAccountBorrowDto);

        Paginator paginator = list.getPaginator();
        resultResponse.setPaginator(paginator);
        resultResponse.setSuccess(true);

        return resultResponse;
    }

    /**
     * 查询账户金额（不包含冻结金额） 与 借款金额（不包含冻结金额）
     * @param memberCompanyId
     * @return
     */
    @Override
    public CompanyAccountPropertyDto selectAccountInfo(Long memberCompanyId) throws Exception{
        if(null==memberCompanyId){
            throw new BusinessException("公司id参数为空");
        }
        CompanyAccountPropertyDto companyAccountPropertyDto = new CompanyAccountPropertyDto();
        SubAccountBean subAccountBean = subAccountReaderMapper.selectByCustId(memberCompanyId, 2);
        if(null!=subAccountBean){
            //账户金额
             companyAccountPropertyDto.setBalance(subAccountBean.getCashAmout());
            //借款金额
            companyAccountPropertyDto.setBorrow(subAccountBean.getUncashAmount());
        }
        return companyAccountPropertyDto;
    }

}
