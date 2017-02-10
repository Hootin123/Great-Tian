package com.xtr.core.service.company;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyDepositBean;
import com.xtr.api.dto.company.CompanyDepositDto;
import com.xtr.api.service.company.CompanyDepositService;
import com.xtr.core.persistence.reader.company.CompanyDepositReaderMapper;
import com.xtr.core.persistence.writer.company.CompanyDepositWriterMapper;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/8/5 14:43
 */
@Service("companyDepositService")
public class CompanyDepositServiceImpl implements CompanyDepositService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CompanyDepositServiceImpl.class);

    @Resource
    private CompanyDepositReaderMapper companyDepositReaderMapper;

    @Resource
    private CompanyDepositWriterMapper companyDepositWriterMapper;

    /**
     *  根据主键删除数据库的记录,company_deposit
     *
     * @param depositId
     */
    public int deleteByPrimaryKey(Long depositId){
        return companyDepositWriterMapper.deleteByPrimaryKey(depositId);
    }

    /**
     *  新写入数据库记录,company_deposit
     *
     * @param record
     */
    public int insert(CompanyDepositBean record){
        return companyDepositWriterMapper.insert(record);
    }

    /**
     *  动态字段,写入数据库记录,company_deposit
     *
     * @param record
     */
    public int insertSelective(CompanyDepositBean record){
        return companyDepositWriterMapper.insertSelective(record);
    }

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录,company_deposit
     *
     * @param record
     */
    public int updateByPrimaryKeySelective(CompanyDepositBean record){
        return companyDepositWriterMapper.updateByPrimaryKeySelective(record);
    }

    /**
     *  根据主键来更新符合条件的数据库记录,company_deposit
     *
     * @param record
     */
    public int updateByPrimaryKey(CompanyDepositBean record){
        return companyDepositWriterMapper.updateByPrimaryKey(record);
    }

    /**
     *  根据指定主键获取一条数据库记录,company_deposit
     *
     * @param depositId
     */
    public CompanyDepositBean selectByPrimaryKey(Long depositId){
        return companyDepositReaderMapper.selectByPrimaryKey(depositId);
    }

    /**
     * 根据企业ID获取提现账户
     * @param companyId
     * @return
     */
    public CompanyDepositBean selectByCompanyId(Long companyId){
        return companyDepositReaderMapper.selectByCompanyId(companyId);
    }

    /**
     * 根据过滤条件获取所有用户提现账户
     * @param companyDepositDto
     * @return
     */
    public ResultResponse selectByCondition(CompanyDepositDto companyDepositDto){
        LOGGER.info("根据过滤条件获取所有用户提现账户,传递参数:"+ JSON.toJSONString(companyDepositDto));
        ResultResponse resultResponse = new ResultResponse();
        if (companyDepositDto != null) {
            PageBounds pageBounds = new PageBounds(companyDepositDto.getPageIndex(), companyDepositDto.getPageSize());
            List<CompanyDepositDto> list = companyDepositReaderMapper.selectByCondition(companyDepositDto, pageBounds);
            List<CompanyDepositDto> listCount = companyDepositReaderMapper.selectByCondition(companyDepositDto);
            resultResponse.setData(list);
            //由于分页组件存在BUG,手动分页
            Paginator paginator=new Paginator(companyDepositDto.getPageIndex(),companyDepositDto.getPageSize(),listCount.size());
//            Paginator paginator = ((PageList) list).getPaginator();
            resultResponse.setPaginator(paginator);
            resultResponse.setSuccess(true);
        } else {
            resultResponse.setMessage("参数不能为空");
        }
        return resultResponse;
    }
}
