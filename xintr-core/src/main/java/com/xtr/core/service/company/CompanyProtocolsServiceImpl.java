package com.xtr.core.service.company;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.account.SubAccountBean;
import com.xtr.api.domain.company.CompanyProtocolsBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.company.FileResourcesBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.domain.salary.PayRuleBean;
import com.xtr.api.domain.station.StationCollaborationBean;
import com.xtr.api.dto.company.CompanyProtocolsDto;
import com.xtr.api.service.account.SubAccountService;
import com.xtr.api.service.company.CompanyProtocolsService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.api.service.salary.CustomerPayrollService;
import com.xtr.api.service.salary.PayRuleService;
import com.xtr.api.service.station.StationCollaborationService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CompanyConstant;
import com.xtr.comm.constant.CompanyProtocolConstants;
import com.xtr.comm.enums.FileResourcesEnum;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.comm.util.StringUtils;
import com.xtr.core.persistence.reader.company.CompanyProtocolsReaderMapper;
import com.xtr.core.persistence.reader.salary.PayCycleReaderMapper;
import com.xtr.core.persistence.writer.company.CompanyProtocolsWriterMapper;
import com.xtr.core.persistence.writer.salary.PayCycleWriterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/11 17:48
 */
@Service("companyProtocolsService")
public class CompanyProtocolsServiceImpl implements CompanyProtocolsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyProtocolsServiceImpl.class);

    @Resource
    private CompanyProtocolsWriterMapper companyProtocolsWriterMapper;

    @Resource
    private CompanyProtocolsReaderMapper companyProtocolsReaderMapper;

    @Resource
    private CompanysService companysService;

    @Resource
    private SubAccountService subAccountService;

    @Resource
    private StationCollaborationService stationCollaborationService;

    @Resource
    private PayRuleService payRuleService;

    @Resource
    private PayCycleReaderMapper payCycleReaderMapper;

    //    @Resource
//    private CustomerPayrollWriterMapper customerPayrollWriterMapper;
    @Resource
    private CustomerPayrollService customerPayrollService;

    @Resource
    private PayCycleWriterMapper payCycleWriterMapper;

    /**
     * 根据企业ID更改协议
     *
     * @param companyProtocolsBean
     * @return
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateByCompanyAndType(CompanyProtocolsBean companyProtocolsBean) throws BusinessException {
        return companyProtocolsWriterMapper.updateByCompanyAndType(companyProtocolsBean);
    }

    /**
     * 新增企业协议
     *
     * @param companyProtocolsBean
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int insert(CompanyProtocolsBean companyProtocolsBean) throws BusinessException {
        return companyProtocolsWriterMapper.insert(companyProtocolsBean);
    }

    /**
     * 点击确认按钮,更新企业信息,新增企业协议信息
     *
     * @param companyProtocolsBean
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultResponse updateProtocolAndUpdateCompany(CompanyProtocolsBean companyProtocolsBean, CompanysBean companysBean, Long userId) throws Exception {
        LOGGER.info("点击确认按钮,更新企业信息,新增企业协议信息，获取传递参数：企业信息:" + JSON.toJSONString(companysBean) + ",协议信息:" + JSON.toJSONString(companyProtocolsBean));
        //验证数据是否正确
        if (companysBean == null || companysBean.getCompanyId() == null) {
            throw new BusinessException("传递的企业信息不能为空");
        }
        if (companyProtocolsBean == null) {
            throw new BusinessException("传递的协议信息不能为空");
        }
        //当前时间
        Date nowDate = new Date();
        //如果新增垫发协议,该公司必须有签约\即将到期\冻结的代发协议;
        //垫发协议的到期时间不能晚于签约\即将到期\冻结的代发协议的到期时间
        if (companyProtocolsBean.getProtocolContractType().intValue() == CompanyProtocolConstants.PROTOCOL_TYPE_FF) {
            //获取签约\即将到期\冻结的代发协议
            CompanyProtocolsBean queryCompanyProtocolsBean = new CompanyProtocolsBean();
            queryCompanyProtocolsBean.setProtocolCompanyId(companysBean.getCompanyId());
            queryCompanyProtocolsBean.setProtocolContractType(CompanyProtocolConstants.PROTOCOL_TYPE_DF);
            List<CompanyProtocolsBean> protocolList = selectDfInfoByState(queryCompanyProtocolsBean);
            if (protocolList != null && protocolList.size() > 0) {
                for (CompanyProtocolsBean checkBean : protocolList) {
                    if (checkBean.getProtocolExpireTime() != null && companyProtocolsBean.getProtocolExpireTime() != null) {
                        //获取签约\即将到期\冻结的代发协议的到期时间
                        int dfTime = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(checkBean.getProtocolExpireTime()));
                        //获取垫发的到期时间
                        int ffTime = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(companyProtocolsBean.getProtocolExpireTime()));
                        if (ffTime > dfTime) {
                            throw new BusinessException("垫发协议过期时间晚于代发协议过期时间！这种情况会导致企业的借款无法通过工资发出去");
                        }
                    }
                }
            } else {
                throw new BusinessException("垫发协议没有对应的代发协议！这种情况会导致企业的借款无法通过工资发出去");
            }
        }
        //存储sub_account,先查询是否存在,如果不存在,则存储
        SubAccountBean subAccountBean = subAccountService.selectByCustId(companysBean.getCompanyId(), CompanyProtocolConstants.SUBACCOUNT_PROPERTY_COMPANY);
        if (subAccountBean == null) {
            subAccountBean = new SubAccountBean();
            subAccountBean.setCustId(companysBean.getCompanyId());
            subAccountBean.setProperty(CompanyProtocolConstants.SUBACCOUNT_PROPERTY_COMPANY);
            int count = subAccountService.insert(subAccountBean);
            if (count <= 0) {
                throw new BusinessException("添加子账户信息失败");
            }
        }

        ResultResponse resultResponse = new ResultResponse();
        //更新企业信息
        //更新时间
        companysBean.setCompanyEditime(nowDate);
        //更新人ID
        companysBean.setCompanyEditMember(userId);
        int updateCount = companysService.updateByProtocolModify(companysBean);
        if (updateCount <= 0) {
            throw new BusinessException("更新企业信息失败");
        }
        //新增协议信息
        //生效状态
        companyProtocolsBean.setProtocolStatus(CompanyProtocolConstants.PROTOCOL_USEFUL_YES);
        //创建时间
        companyProtocolsBean.setProtocolCreateTime(nowDate);
        //更新时间
        companyProtocolsBean.setProtocolUpdateTime(nowDate);
        //协议状态,新增都为签约状态
        companyProtocolsBean.setProtocolCurrentStatus(CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FINISH);
        //企业ID
        companyProtocolsBean.setProtocolCompanyId(companysBean.getCompanyId());
        int insertCount = insert(companyProtocolsBean);
        if (insertCount <= 0) {
            throw new BusinessException("新增企业协议失败");
        }
        //更改意向状态为签约
        ResultResponse autoResponse = stationCollaborationService.updateStateAuto(companyProtocolsBean.getProtocolContractType(), companysBean.getCompanyId());
        if (!autoResponse.isSuccess()) {
            throw new BusinessException(autoResponse.getMessage());
        }
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 根据企业ID查询协议
     *
     * @param protocolCompanyId
     * @return
     */
    public List<CompanyProtocolsBean> selectByCompanyId(Long protocolCompanyId) {
        return companyProtocolsReaderMapper.selectByCompanyId(protocolCompanyId);
    }

    /**
     * 企业详细修改企业协议
     *
     * @param dtype
     * @param ftype
     * @param dno
     * @param fno
     * @param companyId
     * @return
     * @throws BusinessException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultResponse updateCompanyProtocol(int dtype, int ftype, String dno, String fno, Long companyId) throws Exception {
        ResultResponse resultResponse = new ResultResponse();
        List<CompanyProtocolsBean> protocolList = selectByCompanyId(companyId);
        LOGGER.info("企业详细信息修改协议，获取原协议：" + JSON.toJSONString(protocolList));
        boolean isAcceptProtocol = false;//是否代发,true代表代发
        boolean isPaidProtocol = false;//是否垫发,true代表垫发
        if (protocolList != null && protocolList.size() > 0) {
            for (CompanyProtocolsBean companyProtocolsBean : protocolList) {
                if (companyProtocolsBean.getProtocolContractType() == CompanyConstant.COMPANY_PROTOCOLTYPE_ACCEPT) {
                    isAcceptProtocol = true;
                } else if (companyProtocolsBean.getProtocolContractType() == CompanyConstant.COMPANY_PROTOCOLTYPE_PAID) {
                    isPaidProtocol = true;
                }
            }
        }
        //如果原来没有代发协议或者垫发协议,则添加协议;如果原来有代发或垫发协议,则更改协议
        //更改规则:如果原来有代发或垫发协议,这时取消协议,则更改协议状态为无效,其它按普通更改进行更改
        CompanyProtocolsBean companyProtocolsBean = new CompanyProtocolsBean();
        companyProtocolsBean.setProtocolCompanyId(companyId);
        companyProtocolsBean.setProtocolContractType(CompanyConstant.COMPANY_PROTOCOLTYPE_ACCEPT);

        CompanyProtocolsBean companyProtocolsBean_paid = new CompanyProtocolsBean();
        companyProtocolsBean_paid.setProtocolCompanyId(companyId);
        companyProtocolsBean_paid.setProtocolContractType(CompanyConstant.COMPANY_PROTOCOLTYPE_PAID);
        if (isAcceptProtocol) {
            if (dtype == CompanyConstant.COMPANY_PROTOCOLTYPE_CANCEL) {//取消代发
                companyProtocolsBean.setProtocolStatus(CompanyConstant.COMPANY_PROTOCOL_NO);
                int count = updateByCompanyAndType(companyProtocolsBean);
                if (count <= 0) {
                    throw new BusinessException("取消代发协议失败");
                }
            } else {//更改代发
                companyProtocolsBean.setProtocolPaperNo(dno);
                companyProtocolsBean.setProtocolStatus(CompanyConstant.COMPANY_PROTOCOL_YES);
                int count = updateByCompanyAndType(companyProtocolsBean);
                if (count <= 0) {
                    throw new BusinessException("更改代发协议失败");
                }
            }
        } else {
            if (dtype == CompanyConstant.COMPANY_PROTOCOLTYPE_ACCEPT) {//新增代发
                companyProtocolsBean.setProtocolPaperNo(dno);
                companyProtocolsBean.setProtocolStatus(CompanyConstant.COMPANY_PROTOCOL_YES);
                int count = insert(companyProtocolsBean);
                if (count <= 0) {
                    throw new BusinessException("新增代发协议失败");
                }
                //存储sub_account,先查询是否存在,如果不存在,则存储
                SubAccountBean subAccountBean = subAccountService.selectByCustId(companyId, CompanyProtocolConstants.SUBACCOUNT_PROPERTY_COMPANY);
                if (subAccountBean == null) {
                    subAccountBean = new SubAccountBean();
                    subAccountBean.setCustId(companyId);
                    subAccountBean.setProperty(CompanyProtocolConstants.SUBACCOUNT_PROPERTY_COMPANY);
                    int subCount = subAccountService.insert(subAccountBean);
                    if (subCount <= 0) {
                        throw new BusinessException("新增代发时,添加子账户信息失败");
                    }
                }
            }
        }
        if (isPaidProtocol) {
            if (ftype == CompanyConstant.COMPANY_PROTOCOLTYPE_CANCEL) {//取消垫发
                companyProtocolsBean_paid.setProtocolStatus(CompanyConstant.COMPANY_PROTOCOL_NO);
                int count = updateByCompanyAndType(companyProtocolsBean_paid);
                if (count <= 0) {
                    throw new BusinessException("取消垫发协议失败");
                }
            } else {//更改垫发
                companyProtocolsBean_paid.setProtocolPaperNo(fno);
                companyProtocolsBean_paid.setProtocolStatus(CompanyConstant.COMPANY_PROTOCOL_YES);
                int count = updateByCompanyAndType(companyProtocolsBean_paid);
                if (count <= 0) {
                    throw new BusinessException("更改垫发协议失败");
                }
            }
        } else {
            if (ftype == CompanyConstant.COMPANY_PROTOCOLTYPE_PAID) {//新增垫发
                companyProtocolsBean_paid.setProtocolPaperNo(fno);
                companyProtocolsBean_paid.setProtocolStatus(CompanyConstant.COMPANY_PROTOCOL_YES);
                int count = insert(companyProtocolsBean_paid);
                if (count <= 0) {
                    throw new BusinessException("新增垫发协议失败");
                }
            }
        }
        resultResponse.setSuccess(true);
        return resultResponse;
    }

//    /**
//     * 根据过滤条件查询企业协议
//     * @param companyProtocolsDto
//     * @return
//     */
//    public ResultResponse selectProtocolByCondition(CompanyProtocolsDto companyProtocolsDto){
//        LOGGER.info("根据过滤条件查询企业协议,传递参数:"+JSON.toJSONString(companyProtocolsDto));
//        ResultResponse resultResponse = new ResultResponse();
//        if (companyProtocolsDto != null) {
//            PageBounds pageBounds = new PageBounds(companyProtocolsDto.getPageIndex(), companyProtocolsDto.getPageSize());
//            List<CompanyProtocolsDto> list = companyProtocolsReaderMapper.selectProtocolByCondition(companyProtocolsDto, pageBounds);
//            //判断前台是否能签约,判断规则如下:
//            //代发未到期,垫发未到期,则不能签约;代发未到期,垫发到期或无垫发,则可垫发签约;代发到期或者无代发,则代发签约(因为垫发永远比代发先到期)
//            Map<Long,List<Integer>> dfMap=new HashMap<Long,List<Integer>>();//Integer 1待审批 2签约 3即将到期 4合约到期 5冻结
//            Map<Long,List<Integer>> ffMap=new HashMap<Long,List<Integer>>();
//            //获取所有的企业协议数据,这样才能获取各个企业的所有代发与垫发的情况
//            List<CompanyProtocolsDto> allList = companyProtocolsReaderMapper.selectProtocolByCondition(new CompanyProtocolsDto());
//            if(allList!=null && allList.size()>0){
//                for(CompanyProtocolsDto resultDto:allList){
//                    List<Integer> dfList=null;//代发列表,true代表到期,false代表未到期,没有值则代表没有代发签约
//                    if(dfMap.get(resultDto.getCompanyId())!=null){
//                        dfList=dfMap.get(resultDto.getCompanyId());
//                    }else{
//                        dfList=new ArrayList<Integer>();
//                    }
//                    List<Integer> ffList=null;//垫发列表,true代表到期,false代表未到期,没有值则代表没有垫发签约
//                    if(ffMap.get(resultDto.getCompanyId())!=null){
//                        ffList=ffMap.get(resultDto.getCompanyId());
//                    }else{
//                        ffList=new ArrayList<Integer>();
//                    }
//                    if(resultDto!=null && resultDto.getProtocolContractType()!=null && resultDto.getProtocolContractType().intValue()==CompanyProtocolConstants.PROTOCOL_TYPE_DF){//有代发
//                        if(resultDto!=null && resultDto.getProtocolCurrentStatus()!=null && resultDto.getProtocolCurrentStatus().intValue()== CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_WAIT){//待审批
//                            dfList.add(1);
//                            dfMap.put(resultDto.getCompanyId(),dfList);
//                        }else if(resultDto!=null && resultDto.getProtocolCurrentStatus()!=null && resultDto.getProtocolCurrentStatus().intValue()== CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FINISH){//签约
//                            dfList.add(2);
//                            dfMap.put(resultDto.getCompanyId(),dfList);
//                        }else if(resultDto!=null && resultDto.getProtocolCurrentStatus()!=null && resultDto.getProtocolCurrentStatus().intValue()== CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_WILL){//即将到期
//                            dfList.add(3);
//                            dfMap.put(resultDto.getCompanyId(),dfList);
//                        }else if(resultDto!=null && resultDto.getProtocolCurrentStatus()!=null && resultDto.getProtocolCurrentStatus().intValue()== CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_ALREADY){//合约到期
//                            dfList.add(4);
//                            dfMap.put(resultDto.getCompanyId(),dfList);
//                        }else if(resultDto!=null && resultDto.getProtocolCurrentStatus()!=null && resultDto.getProtocolCurrentStatus().intValue()== CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FREESON){//冻结
//                            dfList.add(5);
//                            dfMap.put(resultDto.getCompanyId(),dfList);
//                        }
//                    }else if(resultDto!=null && resultDto.getProtocolContractType()!=null && resultDto.getProtocolContractType().intValue()==CompanyProtocolConstants.PROTOCOL_TYPE_FF){//有垫发
//                        if(resultDto!=null && resultDto.getProtocolCurrentStatus()!=null && resultDto.getProtocolCurrentStatus().intValue()== CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_WAIT){//待审批
//                            ffList.add(1);
//                            ffMap.put(resultDto.getCompanyId(),ffList);
//                        }else if(resultDto!=null && resultDto.getProtocolCurrentStatus()!=null && resultDto.getProtocolCurrentStatus().intValue()== CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FINISH){//签约
//                            ffList.add(2);
//                            ffMap.put(resultDto.getCompanyId(),ffList);
//                        }else if(resultDto!=null && resultDto.getProtocolCurrentStatus()!=null && resultDto.getProtocolCurrentStatus().intValue()== CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_WILL){//即将到期
//                            ffList.add(3);
//                            ffMap.put(resultDto.getCompanyId(),ffList);
//                        }else if(resultDto!=null && resultDto.getProtocolCurrentStatus()!=null && resultDto.getProtocolCurrentStatus().intValue()== CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_ALREADY){//合约到期
//                            ffList.add(4);
//                            ffMap.put(resultDto.getCompanyId(),ffList);
//                        }else if(resultDto!=null && resultDto.getProtocolCurrentStatus()!=null && resultDto.getProtocolCurrentStatus().intValue()== CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FREESON){//冻结
//                            ffList.add(5);
//                            ffMap.put(resultDto.getCompanyId(),ffList);
//                        }
//                    }
//                }
//            }
//            LOGGER.info("根据过滤条件查询企业协议,获取企业代发垫发数据情况:代发:"+JSON.toJSONString(dfMap)+";垫发:"+JSON.toJSONString(ffMap));
//            for(CompanyProtocolsDto resultDto:list) {
//                List<Integer> resultDFList = dfMap.get(resultDto.getCompanyId());
//                boolean dfresult = true;//到期
//                if (resultDFList != null && resultDFList.size() > 0) {
//                    if(resultDFList.contains(1)){//代发待审核,不显示签约
//                        resultDto.setOperationShowState(CompanyProtocolConstants.PROTOCOL_OPERATIONSHOW_NOSHOW);
//                        continue;
//                    }
//                    if (resultDFList.contains(1) || resultDFList.contains(2) || resultDFList.contains(3) || resultDFList.contains(5) ) {//代发未到期
//                        dfresult = false;
//                    }
//                }else{//无代发,则显示代发签约
//                    resultDto.setOperationShowState(CompanyProtocolConstants.PROTOCOL_OPERATIONSHOW_SHOWDF);
//                    continue;
//                }
//                if(dfresult){//如果代发到期,则显示代发签约
//                    resultDto.setOperationShowState(CompanyProtocolConstants.PROTOCOL_OPERATIONSHOW_SHOWDF);
//                    continue;
//                }else{//代发未到期:垫发到期或垫发为空,可垫发签约;垫发未到期,不可显示签约
//                    //垫发到期或垫发为空,可垫发签约
//                    if(ffMap!=null && ffMap.size()>0){
//                        List<Integer> resultFFList=ffMap.get(resultDto.getCompanyId());
//                        if(resultFFList!=null && resultFFList.size()>0){
//                            if(resultFFList.contains(1) || resultFFList.contains(2)||resultFFList.contains(3)||resultFFList.contains(5)){//垫发未到期,不可显示签约
//                                resultDto.setOperationShowState(CompanyProtocolConstants.PROTOCOL_OPERATIONSHOW_NOSHOW);
//                                continue;
//                            }else{//垫发到期,可垫发签约
//                                resultDto.setOperationShowState(CompanyProtocolConstants.PROTOCOL_OPERATIONSHOW_SHOWFF);
//                                continue;
//                            }
//                        }else{//垫发为空,可垫发签约
//                            resultDto.setOperationShowState(CompanyProtocolConstants.PROTOCOL_OPERATIONSHOW_SHOWFF);
//                            continue;
//                        }
//                    }else{//垫发为空,可垫发签约
//                        resultDto.setOperationShowState(CompanyProtocolConstants.PROTOCOL_OPERATIONSHOW_SHOWFF);
//                        continue;
//                    }
//                }
//            }
//
//            resultResponse.setData(list);
//            Paginator paginator = ((PageList) list).getPaginator();
//            resultResponse.setPaginator(paginator);
//            resultResponse.setSuccess(true);
//        } else {
//            resultResponse.setMessage("参数不能为空");
//        }
//        return resultResponse;
//    }

    /**
     * 根据过滤条件查询企业协议
     *
     * @param companyProtocolsDto
     * @return
     */
    public ResultResponse selectProtocolByCondition(CompanyProtocolsDto companyProtocolsDto) {
        LOGGER.info("根据过滤条件查询企业协议,传递参数:" + JSON.toJSONString(companyProtocolsDto));
        ResultResponse resultResponse = new ResultResponse();
        if (companyProtocolsDto != null) {
            PageBounds pageBounds = new PageBounds(companyProtocolsDto.getPageIndex(), companyProtocolsDto.getPageSize());
            List<CompanyProtocolsDto> list = companyProtocolsReaderMapper.selectProtocolByCondition(companyProtocolsDto, pageBounds);
            resultResponse.setData(list);
            Paginator paginator = ((PageList) list).getPaginator();
            resultResponse.setPaginator(paginator);
            resultResponse.setSuccess(true);
        } else {
            resultResponse.setMessage("参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 搜索第二个列表企业协议
     *
     * @param companyProtocolsDto
     * @return
     */
    public ResultResponse queryCompanysProtocol(CompanyProtocolsDto companyProtocolsDto) {
        LOGGER.info("搜索第二个列表企业协议,传递参数:" + JSON.toJSONString(companyProtocolsDto));
        ResultResponse resultResponse = new ResultResponse();
        if (companyProtocolsDto != null) {

            //根据企业名称或者无名称查询过滤后或者无过滤的结果列表
            long startTime = new Date().getTime();
            List<CompanyProtocolsDto> companyProtocolDtoList = companyProtocolsReaderMapper.selectCompanyProtocolAllByCreateTime(companyProtocolsDto);
            LOGGER.info("获取所有企业协议所耗时间:" + String.valueOf(new Date().getTime() - startTime));

            //将查询的所有企业信息按企业ID分类,并且企业ID对应按协议类型分类的MAP
            Map<Long, Map<Integer, CompanyProtocolsDto>> companyProtocolMaps = new TreeMap<>();
            //获取所有企业的信息,这儿是为了方便后面拼装最新数据时,将企业名称和企业法人手机填进去
            Map<Long, CompanyProtocolsDto> companyMaps = new HashMap<>();
            //将查询的所有企业信息按企业ID分类,并且企业ID对应按协议类型分类的MAP
            assemProtocolAllMap(companyProtocolDtoList, companyProtocolMaps, companyMaps);
            LOGGER.info("搜索第二个列表企业协议,获取已经存在的企业协议的不同类型的最新一条数据:" + JSON.toJSONString(companyProtocolMaps));
            LOGGER.info("获取已经存在的企业协议的不同类型的最新一条数据所耗时间:" + String.valueOf(new Date().getTime() - startTime));

            //重新拼装最新的数据,并按顺序返回LIST
            List<CompanyProtocolsDto> resultDtoList = new ArrayList<>();
            //将封装好的MAP重新拼装成前台显示的列表LIST
            assemProtocolSortList(companyProtocolMaps, companyMaps, resultDtoList);
            LOGGER.info("搜索第二个列表企业协议,获取拼装完的所有列表:" + JSON.toJSONString(resultDtoList));
            LOGGER.info("获取拼装完的所有列表所耗时间:" + String.valueOf(new Date().getTime() - startTime));

            //分页
            if (companyProtocolsDto.getPageIndex() == 0) {
                companyProtocolsDto.setPageIndex(1);
            }
            int startIndex = companyProtocolsDto.getPageSize() * (companyProtocolsDto.getPageIndex() - 1 < 0 ? 0 : companyProtocolsDto.getPageIndex() - 1);
            int lastIndex = companyProtocolsDto.getPageIndex() * companyProtocolsDto.getPageSize();
            List<CompanyProtocolsDto> resultDtoList_new = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = startIndex; i < lastIndex; i++) {
                //不能超出范围
                if ((i + 1) > resultDtoList.size()) {
                    break;
                }
                CompanyProtocolsDto resultDto = resultDtoList.get(i);
                //拼装企业协议列表有效期限\操作列数据
                assemProtocolList(resultDto, sdf, companyProtocolMaps, resultDtoList_new);
            }
            LOGGER.info("搜索第二个列表企业协议,获取分页展示的列表:" + JSON.toJSONString(resultDtoList_new));
            LOGGER.info("获取拼装完的分页列表所耗时间:" + String.valueOf(new Date().getTime() - startTime));
            Paginator paginator = new Paginator(companyProtocolsDto.getPageIndex(), companyProtocolsDto.getPageSize(), resultDtoList.size());
            resultResponse.setData(resultDtoList_new);
            resultResponse.setPaginator(paginator);
            resultResponse.setSuccess(true);
        } else {
            resultResponse.setMessage("参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 重新拼装最新的数据,并按顺序返回LIST
     *
     * @param companyProtocolMaps
     * @param companyMaps
     * @param resultDtoList
     */
    private void assemProtocolSortList(Map<Long, Map<Integer, CompanyProtocolsDto>> companyProtocolMaps, Map<Long, CompanyProtocolsDto> companyMaps, List<CompanyProtocolsDto> resultDtoList) {
        for (Map.Entry<Long, Map<Integer, CompanyProtocolsDto>> entry : companyProtocolMaps.entrySet()) {
            Long key = entry.getKey();
            Map<Integer, CompanyProtocolsDto> value = entry.getValue();
            //如果value是空,则需要插入三个协议,如果value有三个,则不需要插入协议
            for (int i = 0; i < CompanyProtocolConstants.PROTOCOLTYPEARRAY.length; i++) {//遍历所有的协议
                if (value != null && value.size() > 0) {
                    if (value.get(CompanyProtocolConstants.PROTOCOLTYPEARRAY[i]) != null) {
                        resultDtoList.add(value.get(CompanyProtocolConstants.PROTOCOLTYPEARRAY[i]));
                    } else {
                        resultDtoList.add(createProtocolDtoBean(companyMaps.get(key), key, CompanyProtocolConstants.PROTOCOLTYPEARRAY[i]));
                    }
                } else {//没有协议,添加所有类型的协议
                    resultDtoList.add(createProtocolDtoBean(companyMaps.get(key), key, CompanyProtocolConstants.PROTOCOLTYPEARRAY[i]));
                }
            }
        }
    }

    /**
     * 将查询的所有企业信息按企业ID分类,并且企业ID对应按协议类型分类的MAP
     *
     * @param companyProtocolDtoList
     * @param companyProtocolMaps
     * @param companyMaps
     */
    private void assemProtocolAllMap(List<CompanyProtocolsDto> companyProtocolDtoList, Map<Long, Map<Integer, CompanyProtocolsDto>> companyProtocolMaps, Map<Long, CompanyProtocolsDto> companyMaps) {
        for (CompanyProtocolsDto conditionDto : companyProtocolDtoList) {
            //根据企业ID的KEY获取MAP的值
            Map<Integer, CompanyProtocolsDto> companyProtocolMap = companyProtocolMaps.get(conditionDto.getCompanyId());
            //如果该企业信息的协议类型有值,判断该企业MAP存储的信息是否有值,如果有值,判断是否存在该类型,如果存在,则不添加,如果不存在,则添加,如果无值,则MAP添加该企业
            if (conditionDto.getProtocolContractType() != null) {
                if (companyProtocolMap != null && companyProtocolMap.size() > 0) {
                    if (companyProtocolMap.get(conditionDto.getProtocolContractType()) == null) {
                        companyProtocolMap.put(conditionDto.getProtocolContractType(), conditionDto);
                        companyProtocolMaps.put(conditionDto.getCompanyId(), companyProtocolMap);
                    }
                } else {
                    companyProtocolMap = new HashMap<>();
                    companyProtocolMap.put(conditionDto.getProtocolContractType(), conditionDto);
                    companyProtocolMaps.put(conditionDto.getCompanyId(), companyProtocolMap);
                }
            } else {
                //如果该企业信息的协议类型无值,判断该企业MAP存储的信息是否有值,如果有值,不管,如果无值,MAP添加该企业,这样做的原因是因为可能只有一个协议,但协议类型为空,但有数据,所以前台也要显示该协议
                if (companyProtocolMap == null || companyProtocolMap.size() <= 0) {
                    companyProtocolMaps.put(conditionDto.getCompanyId(), null);
                }
            }
            //填入companyMaps数据
            if (companyMaps.get(conditionDto.getCompanyId()) == null) {
                companyMaps.put(conditionDto.getCompanyId(), conditionDto);
            }
        }
    }

    /**
     * 拼装企业协议列表数据
     *
     * @param resultDto
     * @param sdf
     * @param companyProtocolMaps
     * @param resultDtoList_new
     */
    private void assemProtocolList(CompanyProtocolsDto resultDto, SimpleDateFormat sdf, Map<Long, Map<Integer, CompanyProtocolsDto>> companyProtocolMaps, List<CompanyProtocolsDto> resultDtoList_new) {
        //拼装有效期限
        if (resultDto.getProtocolContractTime() != null && resultDto.getProtocolExpireTime() != null) {
            resultDto.setUseFulTime(sdf.format(resultDto.getProtocolContractTime()) + "~" + sdf.format(resultDto.getProtocolExpireTime()));
        }
        //拼装签约操作列,规则如下:
        // 代发显示栏:   1.代发协议状态为空和到期,则显示未签约,点击签约;
        //               2.代发协议状态为签约\即将到期\冻结,则显示各自的状态
        // 垫发显示栏:   1.代发协议和代缴社保协议状态都为空和到期,则显示未签约,点击签约并置灰
        //               2.代发协议或代缴社保协议状态为签约\即将到期\冻结,如果垫发协议状态为空或到期,则显示未签约,点击签约;如果垫发协议状态为签约\即将到期\冻结,则显示各自的状态
        //代缴社保显示栏:和代发显示栏相同
        //报销管理显示栏:1.代发协议状态为空和到期,则显示未签约,点击签约并置灰
        //               2.代发协议状态为签约\即将到期\冻结,如果报销管理协议状态为空或到期,则显示未签约,点击签约;如果报销管理协议状态为签约\即将到期\冻结,则显示各自的状态
        if (resultDto.getProtocolContractType() == CompanyProtocolConstants.PROTOCOL_TYPE_BX) {//报销管理显示栏
            //获取代发协议状态
            Map<Integer, CompanyProtocolsDto> addProtocolMap = companyProtocolMaps.get(resultDto.getCompanyId());
            if (addProtocolMap != null && addProtocolMap.size() > 0) {
                CompanyProtocolsDto addProtocolDto = addProtocolMap.get(CompanyProtocolConstants.PROTOCOL_TYPE_DF);
                if (addProtocolDto != null && addProtocolDto.getProtocolCurrentStatus() != null) {
                    //协议本身到期与未到期显示的操作列信息
                    assemOperationLine(addProtocolDto, resultDto);
                } else {//代发协议状态为空
                    resultDto.setAddProtocolShowState(CompanyProtocolConstants.PROTOCOL_ADDPROTOCOL_NO);
                }
            } else {//代发协议状态为空
                resultDto.setAddProtocolShowState(CompanyProtocolConstants.PROTOCOL_ADDPROTOCOL_NO);
            }
        }
        if (resultDto.getProtocolContractType() == CompanyProtocolConstants.PROTOCOL_TYPE_FF) {//垫发显示栏
            //获取代发协议和代缴社保协议状态
            Map<Integer, CompanyProtocolsDto> addProtocolMap = companyProtocolMaps.get(resultDto.getCompanyId());
            if (addProtocolMap != null && addProtocolMap.size() > 0) {
                CompanyProtocolsDto dfProtocolDto = addProtocolMap.get(CompanyProtocolConstants.PROTOCOL_TYPE_DF);
                CompanyProtocolsDto djProtocolDto = addProtocolMap.get(CompanyProtocolConstants.PROTOCOL_TYPE_DJ);

                if (dfProtocolDto != null && dfProtocolDto.getProtocolCurrentStatus() != null) {
                    //协议本身到期与未到期显示的操作列信息
                    assemOperationLine(dfProtocolDto, resultDto);
                } else {//代发协议状态为空
                    //代缴社保状态不为空
                    if (djProtocolDto != null && djProtocolDto.getProtocolCurrentStatus() != null) {
                        //协议本身到期与未到期显示的操作列信息
                        assemOperationLine(djProtocolDto, resultDto);
                    } else {//代发和代缴都为空
                        resultDto.setAddProtocolShowState(CompanyProtocolConstants.PROTOCOL_ADDPROTOCOL_NO);
                    }
                }
            } else {//代发协议状态为空
                resultDto.setAddProtocolShowState(CompanyProtocolConstants.PROTOCOL_ADDPROTOCOL_NO);
            }
        } else {
            if (resultDto.getProtocolCurrentStatus() == null || resultDto.getProtocolCurrentStatus() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_ALREADY) {
                resultDto.setAddProtocolShowState(CompanyProtocolConstants.PROTOCOL_ADDPROTOCOL_YES);
            } else {
                resultDto.setAddProtocolShowState(CompanyProtocolConstants.PROTOCOL_ADDPROTOCOL_SHOW);
            }
        }
        resultDtoList_new.add(resultDto);
    }

    /**
     * 协议本身到期与未到期显示的操作列信息
     *
     * @param protocolDto
     * @param resultDto
     */
    private void assemOperationLine(CompanyProtocolsDto protocolDto, CompanyProtocolsDto resultDto) {
        if (protocolDto.getProtocolCurrentStatus().intValue() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_ALREADY) {//要验证的协议状态为到期
            resultDto.setAddProtocolShowState(CompanyProtocolConstants.PROTOCOL_ADDPROTOCOL_NO);
        } else {//要验证的协议状态为签约\即将到期\冻结
            if (resultDto.getProtocolCurrentStatus() == null || resultDto.getProtocolCurrentStatus() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_ALREADY) {//协议本身到期
                resultDto.setAddProtocolShowState(CompanyProtocolConstants.PROTOCOL_ADDPROTOCOL_YES);
            } else {//协议本身为签约\即将到期\冻结
                resultDto.setAddProtocolShowState(CompanyProtocolConstants.PROTOCOL_ADDPROTOCOL_SHOW);
            }
        }
    }

    /**
     * 查询所有的企业协议
     *
     * @return
     */
    public List<CompanyProtocolsDto> selectProtocolAll() {
        return companyProtocolsReaderMapper.selectProtocolAll();
    }

    /**
     * 根据协议ID更新协议状态
     *
     * @param companyProtocolsBean
     * @return
     */
    public int updateCurrentStateById(CompanyProtocolsBean companyProtocolsBean) {
        return companyProtocolsWriterMapper.updateCurrentStateById(companyProtocolsBean);
    }

    /**
     * 根据协议ID查询协议和企业信息
     *
     * @param protocolId
     */
    public CompanyProtocolsDto selectProtocolAndCompanyById(Long protocolId) {
        List<CompanyProtocolsDto> protocolDtoList = companyProtocolsReaderMapper.selectProtocolAndCompanyById(protocolId);
        if (protocolDtoList != null && protocolDtoList.size() > 0) {
            return protocolDtoList.get(0);
        }
        return new CompanyProtocolsDto();
    }

    /**
     * 修改协议状态业务
     *
     * @param companyProtocolsBean
     * @return
     */
    public ResultResponse updateCurrentStateByIdBusiness(CompanyProtocolsBean companyProtocolsBean) throws BusinessException {
        LOGGER.info("修改协议状态业务,传递参数:" + JSON.toJSONString(companyProtocolsBean));
        ResultResponse resultResponse = new ResultResponse();
        //如果状态为解冻,则需要还原到最新的状态
        if (companyProtocolsBean.getProtocolCurrentStatus() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_UNFREESON) {
            if (companyProtocolsBean.getProtocolExpireTime() != null) {
                //获取当前时间
                String currentTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String compareTime = new SimpleDateFormat("yyyy-MM-dd").format(companyProtocolsBean.getProtocolExpireTime());
                int differTime = DateUtil.getDiffDaysOfTwoDateByNegative(compareTime, currentTime);
                if (differTime < 0) {//如果当前时间超过了到期时间,则更改状态为合约到期
                    companyProtocolsBean.setProtocolCurrentStatus(CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_ALREADY);
                } else if (differTime >= 0 && differTime <= 30) {
                    companyProtocolsBean.setProtocolCurrentStatus(CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_WILL);
                } else {
                    companyProtocolsBean.setProtocolCurrentStatus(CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FINISH);
                }
            } else {
                throw new BusinessException("协议解冻,但是协议本身没有到期时间");
            }
        }
//        List<CompanyProtocolsBean> list=new ArrayList<CompanyProtocolsBean>();
//        list.add(companyProtocolsBean);
        int count = updateProtocolInfoById(companyProtocolsBean);
        if (count <= 0) {
            throw new BusinessException("修改企业协议状态失败");
        }
        //获取要修改的企业协议信息,为了后面修改发工资等状态
        CompanyProtocolsDto protocolsDto = selectProtocolAndCompanyById(companyProtocolsBean.getProtocolId());
        if (protocolsDto != null && protocolsDto.getProtocolContractType() != null && protocolsDto.getProtocolContractType().intValue() == CompanyProtocolConstants.PROTOCOL_TYPE_DJ) {
            if (companyProtocolsBean.getProtocolCurrentStatus() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FREESON) {//冻结
                updateSalaryState(protocolsDto.getProtocolCompanyId(), protocolsDto.getProtocolContractType(), 0);
            } else if (companyProtocolsBean.getProtocolCurrentStatus() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_ALREADY) {//解冻后合约到期
                updateSalaryState(protocolsDto.getProtocolCompanyId(), protocolsDto.getProtocolContractType(), 0);
            } else if (companyProtocolsBean.getProtocolCurrentStatus() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_WILL
                    || companyProtocolsBean.getProtocolCurrentStatus() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FINISH) {//解冻后合约即将到期或还是签约状态
                updateSalaryState(protocolsDto.getProtocolCompanyId(), protocolsDto.getProtocolContractType(), 1);
            }
        }
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 更改发工资相关状态
     *
     * @param protocolCompanyId
     * @param protocolContractType
     * @param type                 1更改是否计算社保公积金状态为是 其它的值代表更改为否
     */
    public void updateSalaryState(Long protocolCompanyId, Integer protocolContractType, int type) {
        //更新计薪规则为计算公积金社保
        if (protocolContractType != null && protocolContractType.intValue() == CompanyProtocolConstants.PROTOCOL_TYPE_DJ && protocolCompanyId != null) {
            //更新员工工资单状态
            PayCycleBean payCycleBean = payCycleReaderMapper.selectByCompanyId(protocolCompanyId);
            if (payCycleBean != null) {
                customerPayrollService.updatePayRollStatusByCycle(payCycleBean.getId());
                payCycleWriterMapper.updateGenerateState(0, payCycleBean.getId());
            }
            if (type == 1) {//更新是否计算社保公积金
                PayRuleBean payRuleBean = new PayRuleBean();
                payRuleBean.setIsSocialSecurity("1");
                payRuleBean.setCompanyId(protocolCompanyId);
                payRuleService.updateIsSocialSecurity(payRuleBean);
            }

        }
    }

    /**
     * 根据企业ID查询签约\即将到期\冻结的协议
     *
     * @param companyProtocolsBean
     * @return
     */
    public List<CompanyProtocolsBean> selectDfInfoByState(CompanyProtocolsBean companyProtocolsBean) {
        return companyProtocolsReaderMapper.selectDfInfoByState(companyProtocolsBean);
    }

    /**
     * 批量更新状态
     *
     * @param list
     * @return
     */
    public int updateCurrentStateByIdList(List<CompanyProtocolsBean> list) {
        return companyProtocolsWriterMapper.updateCurrentStateByIdList(list);
    }

    /**
     * 根据协议ID更新协议
     *
     * @param companyProtocolsBean
     */
    public int updateProtocolInfoById(CompanyProtocolsBean companyProtocolsBean) {
        return companyProtocolsWriterMapper.updateProtocolInfoById(companyProtocolsBean);
    }

    /**
     * 根据企业ID查询协议(分页)
     *
     * @param companyProtocolsBean
     * @return
     */
    public ResultResponse selectPageListByCompanyId(CompanyProtocolsBean companyProtocolsBean) {
        LOGGER.info("根据企业ID查询协议(分页),传递参数:" + companyProtocolsBean);
        ResultResponse resultResponse = new ResultResponse();
        if (companyProtocolsBean != null && companyProtocolsBean.getProtocolCompanyId() != null) {
            PageBounds pageBounds = new PageBounds(companyProtocolsBean.getPageIndex(), companyProtocolsBean.getPageSize());
            List list = companyProtocolsReaderMapper.selectByCompanyId(companyProtocolsBean.getProtocolCompanyId(), pageBounds);
            resultResponse.setData(list);
            Paginator paginator = ((PageList) list).getPaginator();
            resultResponse.setPaginator(paginator);
            resultResponse.setSuccess(true);
        } else {
            resultResponse.setMessage("根据企业ID查询协议(分页),参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 查询征信资料
     *
     * @param companyId
     * @return
     */
    public ResultResponse queryCreditInformations(String currentPath, Long companyId) throws BusinessException, IOException {
        LOGGER.info("查询征信资料,传递企业编号：" + companyId);
        ResultResponse resultResponse = new ResultResponse();
        //根据企业ID获取征信资料
        Map<String, Object> mapParam = new HashMap<>();
        mapParam.put("companyId", companyId);
        List<Map<String, Object>> resultMapList = companysService.getfileResources(mapParam);
        if (resultMapList == null || resultMapList.size() <= 0) {//验证该企业是否有征信资料
            resultResponse.setMessage("该企业没有征信资料");
            return resultResponse;
        }
        //重新拼装征信资料,按文件夹序号存放文件实例
        Map<Integer, List<FileResourcesBean>> assembliedMap = assemblyCreditInfoList(resultMapList);
        LOGGER.info("查询征信资料,获取拼装征信信息：" + JSON.toJSONString(assembliedMap));
        //创建征集资料临时目录
        if (createDir(currentPath)) {
            LOGGER.info("企业征信资料临时目录创建成功");
        } else {
            throw new BusinessException("企业征信资料临时目录创建失败！");
        }
        fileToZip(assembliedMap, currentPath);
        resultResponse.setSuccess(true);
        resultResponse.setData(CompanyProtocolConstants.PROTOCOL_CREDIT_TEMPDIRNAME + CompanyProtocolConstants.PROTOCOL_CREDIT_ENDNAME);
        return resultResponse;
    }

    /**
     * 将征信资料按文件夹分类进行拼装
     *
     * @param resultMapList
     * @return
     */
    private Map<Integer, List<FileResourcesBean>> assemblyCreditInfoList(List<Map<String, Object>> resultMapList) {
        Map<Integer, List<FileResourcesBean>> creditMap = new HashMap<Integer, List<FileResourcesBean>>();
        //初始化二十个文件夹对应的URL列表(因为数据库中根本没有文件夹的数量,所以如果动态的添加,根本不知道添加了多少个,所以这儿暂定限制二十个)
        for (int i = 1; i <= CompanyProtocolConstants.PROTOCOL_CREDIT_MAXNUM; i++) {
            List<FileResourcesBean> creditBeanList = new ArrayList<FileResourcesBean>();
            creditMap.put(i, creditBeanList);
        }
        for (Map<String, Object> queryMap : resultMapList) {//遍历征信资料
            //获取文件夹分类序号
            int fileType = queryMap.get("file_type") == null ? 0 : (Integer) queryMap.get("file_type");
            //获取文件夹序号对应的URL文件名称
            String fileUrl = queryMap.get("file_url") == null ? "" : (String) queryMap.get("file_url");
            //将文件路径名称放入MAP对应的文件夹序号中
            if (creditMap.get(fileType) != null && !StringUtils.isStrNull(fileUrl)) {
                FileResourcesBean fileResourcesBean = new FileResourcesBean();
                fileResourcesBean.setFileUrl(fileUrl);
                creditMap.get(fileType).add(fileResourcesBean);
            }
        }
        return creditMap;
    }

    /**
     * 将拼装好的MAP打成ZIP包
     *
     * @param assembliedMap
     * @return
     * @throws IOException
     */
    private void fileToZip(Map<Integer, List<FileResourcesBean>> assembliedMap, String currentPath) throws IOException {
        //打ZIP包的路径
        FileOutputStream fos = new FileOutputStream(currentPath + CompanyProtocolConstants.PROTOCOL_CREDIT_TEMPDIRNAME + CompanyProtocolConstants.PROTOCOL_CREDIT_ENDNAME);
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));
        for (int i = 1; i <= assembliedMap.size(); i++) {
            //获取每个文件夹中的URL列表
            List<FileResourcesBean> fileBeanList = assembliedMap.get(i);
            if (fileBeanList != null && fileBeanList.size() > 0) {

                InputStream[] downloadStreams = new InputStream[fileBeanList.size()];//文件流数组
                String[] fileNames = new String[fileBeanList.size()];//文件名称数组
                for (int j = 0; j < fileBeanList.size(); j++) {
                    FileResourcesBean fileResourcesBean = fileBeanList.get(j);
                    if (fileResourcesBean != null && !StringUtils.isStrNull(fileResourcesBean.getFileUrl())) {
                        //根据文件类型获取URL对应的文件流,并封装到数组中
                        InputStream downloadStream = null;
                        if (fileResourcesBean.getFileUrl().toLowerCase().endsWith(".png")
                                || fileResourcesBean.getFileUrl().toLowerCase().endsWith(".jpg")
                                || fileResourcesBean.getFileUrl().toLowerCase().endsWith(".jpeg")
                                || fileResourcesBean.getFileUrl().toLowerCase().endsWith(".bmp")
                                || fileResourcesBean.getFileUrl().toLowerCase().endsWith(".gif")) {//图片
                            downloadStream = AliOss.downloadFile(PropertyUtils.getString("oss.bucketName.img"), fileResourcesBean.getFileUrl());
                        } else {
                            downloadStream = AliOss.downloadFile(PropertyUtils.getString("oss.bucketName.img"), fileResourcesBean.getFileUrl());
//                            downloadStream= AliOss.downloadFile(PropertyUtils.getString("oss.bucketName.file"),fileResourcesBean.getFileUrl());
                        }
                        if (downloadStream != null) {
                            downloadStreams[j] = downloadStream;
                            fileNames[j] = fileResourcesBean.getFileUrl();
                        }
                    }
                }
                //将文件打入ZIP包
                byte[] bufs = new byte[1024 * 10];
                if (downloadStreams != null && downloadStreams.length > 0) {
                    //获取文件夹名称
                    String fileName = queryFileNameByResource(i);
                    for (int k = 0; k < downloadStreams.length; k++) {
                        InputStream downloadStream = downloadStreams[k];
                        if (downloadStream != null) {
                            ZipEntry zipEntry = new ZipEntry(fileName + File.separator + fileNames[k]);
                            zos.putNextEntry(zipEntry);
                            int read = 0;
                            while ((read = downloadStream.read(bufs, 0, 1024 * 10)) != -1) {
                                zos.write(bufs, 0, read);
                            }
                        }
                    }
                } else {
                    ZipEntry zipEntry = new ZipEntry(i + File.separator);
                    zos.putNextEntry(zipEntry);
                }
//                fileToDirZip(downloadStreams,destDirName,"test",fileNames);
            }
        }
        if (null != zos) {
            zos.close();
            fos.close();
        }
    }

    /**
     * 创建目录
     *
     * @param destDirName
     * @return
     */
    public boolean createDir(String destDirName) {
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        File dir = new File(destDirName);
        if (dir.exists()) {
            //删除临时文件
//            File tempDir=new File(currentPath);
            deleteFile(dir);
//            FileUtils.deleteQuietly(dir);
//            throw new BusinessException("创建目录" + destDirName + "失败，目标目录已经存在");
        }
        //创建目录
//        try{
//            File dir2 = new File(destDirName);
//            dir2.mkdirs();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return true;
        return dir.mkdirs();
    }

    /**
     * 根据协议类型及指定时间判断协议是否还有效
     *
     * @param protocolCompanyId    企业ID
     * @param protocolContractType 协议类型 1代发协议 2垫发协议
     * @param checkTime            指定时间
     * @return null 代表没有有效的协议
     */
    public CompanyProtocolsBean selectIsUserFulByTypeAndTime(Long protocolCompanyId, Integer protocolContractType, Date checkTime) {
        if (protocolCompanyId != null && protocolContractType != null && checkTime != null) {
            CompanyProtocolsBean companyProtocolsBean = new CompanyProtocolsBean();
            companyProtocolsBean.setProtocolCompanyId(protocolCompanyId);
            companyProtocolsBean.setProtocolContractType(protocolContractType);
            companyProtocolsBean.setProtocolExpireTime(checkTime);
            return companyProtocolsReaderMapper.selectIsUserFulByTypeAndTime(companyProtocolsBean);
        }
        //如果传递的参数有一个为空,则返回null
        return null;
    }

    /**
     * 根据类型返回文件夹名称
     *
     * @param num
     * @return
     */
    private String queryFileNameByResource(int num) {
        switch (num) {
            case 1:
                return FileResourcesEnum.COMPANYSYNOPSIS.getMessage();
            case 2:
                return FileResourcesEnum.COMPANYVOUCHER.getMessage();
            case 3:
                return FileResourcesEnum.COMPANYLEGAL.getMessage();
            case 4:
                return FileResourcesEnum.COMPANYCOPYPAPER.getMessage();
            case 5:
                return FileResourcesEnum.COMPANYCRDITREPORT.getMessage();
            case 6:
                return FileResourcesEnum.COMPANYREPORTS.getMessage();
            case 7:
                return FileResourcesEnum.COMPANYCONTRACT.getMessage();
            case 8:
                return FileResourcesEnum.COMPANYACCOUNTS.getMessage();
            case 9:
                return FileResourcesEnum.COMPANYPAYMENT.getMessage();
            case 10:
                return FileResourcesEnum.COMPANYSPENDING.getMessage();
            case 11:
                return FileResourcesEnum.COMPANYFINANCE.getMessage();
            case 12:
                return FileResourcesEnum.COMPANYBANKSWIFT.getMessage();
            case 13:
                return FileResourcesEnum.COMPANYSELFSWIFT.getMessage();
            default:
                return "-";
        }
    }

    /**
     * 删除文件夹
     *
     * @param file
     * @return
     */
    private boolean deleteFile(File file) {
        if (file == null) {
            return false;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File delFile : files) {
                    if (!delFile.isDirectory()) {
                        delFile.delete();
                    } else {
                        deleteFile(delFile);
                    }
                }
            }
            file.delete();
        } else {
            file.delete();
        }
        return true;
    }

    /**
     * 创建Dtobean
     *
     * @param dto       企业信息
     * @param companyId 企业ID
     * @param type      1代发协议 2垫发协议 3社保代缴协议
     * @return
     */
    private CompanyProtocolsDto createProtocolDtoBean(CompanyProtocolsDto dto, Long companyId, int type) {
        CompanyProtocolsDto companyProtocolsDto_new = new CompanyProtocolsDto();//代发协议
        companyProtocolsDto_new.setCompanyId(companyId);
        companyProtocolsDto_new.setProtocolContractType(type);
        if (dto != null) {
            companyProtocolsDto_new.setCompanyName(dto.getCompanyName());
            companyProtocolsDto_new.setCompanyCorporationPhone(dto.getCompanyCorporationPhone());
            companyProtocolsDto_new.setCompanyContactTel(dto.getCompanyContactTel());
            companyProtocolsDto_new.setProtocolContractTime(dto.getProtocolContractTime());
            companyProtocolsDto_new.setProtocolExpireTime(dto.getProtocolExpireTime());
        }
        return companyProtocolsDto_new;
    }


    /**
     * 添加企业协议
     *
     * @param companyProtocolsBean
     * @param memberId
     * @return
     * @throws Exception
     */
    @Transactional
    public ResultResponse addCompanyProtocol(CompanyProtocolsBean companyProtocolsBean, Long memberId) throws Exception {
        LOGGER.info("添加企业协议，获取传递参数：协议信息:" + JSON.toJSONString(companyProtocolsBean));
        //验证数据是否正确
        if (companyProtocolsBean == null || companyProtocolsBean.getProtocolCompanyId() == null) {
            throw new BusinessException("传递的协议信息不能为空");
        }
        //当前时间
        Date nowDate = new Date();
        //如果新增垫发协议,该公司必须有签约\即将到期\冻结的代发协议;
        //垫发协议的到期时间不能晚于签约\即将到期\冻结的代发协议的到期时间
        if (companyProtocolsBean.getProtocolContractType().intValue() == CompanyProtocolConstants.PROTOCOL_TYPE_FF) {
            //获取签约\即将到期\冻结的代发协议
            CompanyProtocolsBean queryCompanyProtocolsBean = companyProtocolsReaderMapper.selectDJAndFFInfoByState(companyProtocolsBean.getProtocolCompanyId());
            if (queryCompanyProtocolsBean != null) {
                if (queryCompanyProtocolsBean.getProtocolExpireTime() != null && companyProtocolsBean.getProtocolExpireTime() != null) {
                    //获取签约\即将到期\冻结的代发或社保协议的到期时间
                    int dfTime = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(queryCompanyProtocolsBean.getProtocolExpireTime()));
                    //获取垫发的到期时间
                    int ffTime = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(companyProtocolsBean.getProtocolExpireTime()));
                    if (ffTime > dfTime) {
                        throw new BusinessException("垫发协议过期时间晚于代发或社保协议过期时间！这种情况会导致企业的借款无法通过工资发出去");
                    }
                }
            } else {
                throw new BusinessException("垫发协议没有对应的代发协议或社保协议！这种情况会导致企业的借款无法通过工资发出去");
            }
        } else if (companyProtocolsBean.getProtocolContractType().intValue() == CompanyProtocolConstants.PROTOCOL_TYPE_BX) {
            //获取签约\即将到期\冻结的代发协议
            CompanyProtocolsBean queryCompanyProtocolsBean = new CompanyProtocolsBean();
            queryCompanyProtocolsBean.setProtocolCompanyId(companyProtocolsBean.getProtocolCompanyId());
            queryCompanyProtocolsBean.setProtocolContractType(CompanyProtocolConstants.PROTOCOL_TYPE_DF);
            List<CompanyProtocolsBean> protocolList = selectDfInfoByState(queryCompanyProtocolsBean);
            if (protocolList == null || protocolList.size() <= 0) {
                throw new BusinessException("报销管理协议没有对应的代发协议！这种情况会导致企业无法报销");
            }
        }
        //存储sub_account,先查询是否存在,如果不存在,则存储
        SubAccountBean subAccountBean = subAccountService.selectByCustId(companyProtocolsBean.getProtocolCompanyId(), CompanyProtocolConstants.SUBACCOUNT_PROPERTY_COMPANY);
        if (subAccountBean == null) {
            subAccountBean = new SubAccountBean();
            subAccountBean.setCustId(companyProtocolsBean.getProtocolCompanyId());
            subAccountBean.setProperty(CompanyProtocolConstants.SUBACCOUNT_PROPERTY_COMPANY);
            int count = subAccountService.insert(subAccountBean);
            if (count <= 0) {
                throw new BusinessException("添加子账户信息失败");
            }
        }

        ResultResponse resultResponse = new ResultResponse();
        //新增协议信息
        //生效状态
        companyProtocolsBean.setProtocolStatus(CompanyProtocolConstants.PROTOCOL_USEFUL_YES);
        //创建时间
        companyProtocolsBean.setProtocolCreateTime(nowDate);
        //更新时间
        companyProtocolsBean.setProtocolUpdateTime(nowDate);
        //协议状态,新增都为签约状态
        companyProtocolsBean.setProtocolCurrentStatus(CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FINISH);
        //企业ID
        companyProtocolsBean.setProtocolCompanyId(companyProtocolsBean.getProtocolCompanyId());
        //生成协议编号
        String protocolCode = "XY" + companyProtocolsBean.getProtocolCompanyId() + new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date()) + (int) (Math.random() * 10000);
        companyProtocolsBean.setProtocolCode(protocolCode);
        int insertCount = insert(companyProtocolsBean);
        if (insertCount <= 0) {
            throw new BusinessException("新增企业协议失败");
        }
        //更改意向状态为签约
        ResultResponse autoResponse = stationCollaborationService.updateStateAuto(companyProtocolsBean.getProtocolContractType(), companyProtocolsBean.getProtocolCompanyId());
        if (!autoResponse.isSuccess()) {
            throw new BusinessException(autoResponse.getMessage());
        }
        resultResponse.setSuccess(true);

        //修改垫发协议申请为通过
        try {
            if (companyProtocolsBean.getProtocolContractType().intValue() == CompanyProtocolConstants.PROTOCOL_TYPE_FF) {
                companysService.auditPrepaid(companyProtocolsBean.getProtocolCompanyId(), null, true, memberId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //更新计薪规则为计算公积金社保
        updateSalaryState(companyProtocolsBean.getProtocolCompanyId(), companyProtocolsBean.getProtocolContractType(), 1);
        return resultResponse;
    }

    /**
     * 根据企业ID查询签约\即将到期\冻结的协议
     *
     * @param companyProtocolsBean
     * @return
     */
    public List<CompanyProtocolsBean> selectUsefulProtocolsByState(CompanyProtocolsBean companyProtocolsBean) {
        return companyProtocolsReaderMapper.selectUsefulProtocolsByState(companyProtocolsBean);
    }

    public boolean isCanSignDf(long companyId) {
        //获取签约\即将到期\冻结的代发协议
        CompanyProtocolsBean queryCompanyProtocolsBean = new CompanyProtocolsBean();
        queryCompanyProtocolsBean.setProtocolCompanyId(companyId);
        queryCompanyProtocolsBean.setProtocolContractType(CompanyProtocolConstants.PROTOCOL_TYPE_DF);
        List<CompanyProtocolsBean> protocolList = selectDfInfoByState(queryCompanyProtocolsBean);
        return protocolList != null && protocolList.size() > 0;
    }

    @Override
    public List<CompanyProtocolsBean> selectByContractType(CompanyProtocolsBean companyProtocolsBean) {
        return companyProtocolsReaderMapper.selectByContractType(companyProtocolsBean);
    }

    @Override
    public List<CompanyProtocolsBean> selectUsefulProtocolsByAddTime(CompanyProtocolsBean companyProtocolsBean) {
        return companyProtocolsReaderMapper.selectUsefulProtocolsByAddTime(companyProtocolsBean);
    }

    /**
     * 根据企业ID查询签约\即将到期\冻结的协议的数量
     *
     * @param companyProtocolsBean
     * @return
     */
    public int selectCountByUserfulProtocol(CompanyProtocolsBean companyProtocolsBean) {
        return companyProtocolsReaderMapper.selectCountByUserfulProtocol(companyProtocolsBean);
    }

    /**
     * 批量获取企业协议有效信息
     *
     * @param list
     * @return
     */
    public List<CompanyProtocolsBean> selectUsefulProtocolsBatch(List<Long> list) {
        return companyProtocolsReaderMapper.selectUsefulProtocolsBatch(list);
    }

    /**
     * 根据企业ID获取最新的签约记录
     *
     * @param protocolCompanyId
     * @return
     */
    public List<CompanyProtocolsDto> selectProtocolByCompanyId(Long protocolCompanyId) {
        LOGGER.info("根据企业ID获取最新的签约记录,传递参数" + protocolCompanyId);
        //获取最新的签约记录,倒序分组根据协议类型排序
        List<CompanyProtocolsDto> list = companyProtocolsReaderMapper.selectProtocolByCompanyId(protocolCompanyId);
        LOGGER.info("根据企业ID获取最新的签约记录,获取记录信息" + JSON.toJSONString(list));
        //获取企业信息,为了后面拼装最新的签约记录,存储初始的企业信息
        CompanysBean companysBean = companysService.selectCompanyByCompanyId(protocolCompanyId);
        CompanyProtocolsDto dto = null;
        if (companysBean != null) {
            dto = new CompanyProtocolsDto();
            if (!StringUtils.isStrNull(companysBean.getCompanyName())) {
                dto.setCompanyName(companysBean.getCompanyName());
            }
            if (!StringUtils.isStrNull(companysBean.getCompanyCorporationPhone())) {
                dto.setCompanyCorporationPhone(companysBean.getCompanyCorporationPhone());
            }
            if (!StringUtils.isStrNull(companysBean.getCompanyContactTel())) {
                dto.setCompanyContactTel(companysBean.getCompanyContactTel());
            }
        }
        LOGGER.info("根据企业ID获取最新的签约记录,获取企业信息" + JSON.toJSONString(dto));

        //拼装最新的签约记录,如果缺少某个协议,则补上
        list = assemNewProtocolList(protocolCompanyId, list, dto);

        //拼装申请状态:1 已签约 2 未签约 3 已提交签约申请 4 已到期
        list = assemApplyState(protocolCompanyId, list);

        LOGGER.info("根据企业ID获取最新的签约记录,获取拼装后的结果信息" + JSON.toJSONString(list));
        return list;
    }

    /**
     * 拼装最新的签约记录,如果缺少某个协议,则补上
     *
     * @param protocolCompanyId
     * @param list
     * @param dto
     * @return
     */
    private List<CompanyProtocolsDto> assemNewProtocolList(Long protocolCompanyId, List<CompanyProtocolsDto> list, CompanyProtocolsDto dto) {
        for (int i = 0; i < CompanyProtocolConstants.PROTOCOLTYPEARRAY.length; i++) {//遍历所有的协议
            if (list != null && list.size() > 0) {
                boolean isHaveProtocol = false;//查询的结果列表是否存在此协议,false代表不存在
                for (CompanyProtocolsDto protocolDto : list) {
                    if (protocolDto.getProtocolContractType() != null && protocolDto.getProtocolContractType().intValue() == CompanyProtocolConstants.PROTOCOLTYPEARRAY[i]) {
                        isHaveProtocol = true;
                        break;
                    }
                }
                if (!isHaveProtocol) {//不存在此协议,则添加上
                    list.add(createProtocolDtoBean(dto, protocolCompanyId, CompanyProtocolConstants.PROTOCOLTYPEARRAY[i]));
                }
            } else {//没有协议,添加所有类型的协议
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(createProtocolDtoBean(dto, protocolCompanyId, CompanyProtocolConstants.PROTOCOLTYPEARRAY[i]));
            }
        }
        return list;
    }

    /**
     * //拼装申请状态
     *
     * @param protocolCompanyId
     * @param list
     * @return
     */
    private List<CompanyProtocolsDto> assemApplyState(Long protocolCompanyId, List<CompanyProtocolsDto> list) {
        //获取企业下已经申请提交的协议
        List<StationCollaborationBean> collaborationList = stationCollaborationService.selectInfoForAlreadyApply(protocolCompanyId);
        if (collaborationList != null && collaborationList.size() > 0) {
            for (CompanyProtocolsDto resultDto : list) {
                boolean assemFlag = false;//是否是已经申请提交状态,false代表没有申请提交
                for (StationCollaborationBean station : collaborationList) {
                    if (station.getCollaborationContractType() != null && station.getCollaborationContractType().intValue() == resultDto.getProtocolContractType().intValue()) {//申请提交状态
                        resultDto.setApplyState(CompanyProtocolConstants.PROTOCOL_APPLYSTATE_YES);
                        assemFlag = true;
                    }
                }
                if (!assemFlag) {
                    if (resultDto.getProtocolCurrentStatus() != null &&
                            (resultDto.getProtocolCurrentStatus().intValue() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FINISH
                                    || resultDto.getProtocolCurrentStatus().intValue() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_WILL
                                    || resultDto.getProtocolCurrentStatus().intValue() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FREESON)) {//已签约
                        resultDto.setApplyState(CompanyProtocolConstants.PROTOCOL_APPLYSTATE_FINISH);
                    } else if(resultDto.getProtocolCurrentStatus() != null &&
                            resultDto.getProtocolCurrentStatus().intValue() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_ALREADY){
                        resultDto.setApplyState(CompanyProtocolConstants.PROTOCOL_APPLYSTATE_RENEW);//续约

                    }else {//未签约
                        resultDto.setApplyState(CompanyProtocolConstants.PROTOCOL_APPLYSTATE_NO);
                    }
                }
            }
        } else {
            for (CompanyProtocolsDto resultDto : list) {
                if (resultDto.getProtocolCurrentStatus() != null &&
                        (resultDto.getProtocolCurrentStatus().intValue() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FINISH
                                || resultDto.getProtocolCurrentStatus().intValue() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_WILL
                                || resultDto.getProtocolCurrentStatus().intValue() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FREESON)) {//已签约
                    resultDto.setApplyState(CompanyProtocolConstants.PROTOCOL_APPLYSTATE_FINISH);
                } else if(resultDto.getProtocolCurrentStatus() != null &&
                        resultDto.getProtocolCurrentStatus().intValue() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_ALREADY){
                    resultDto.setApplyState(CompanyProtocolConstants.PROTOCOL_APPLYSTATE_RENEW);//续约

                }else {//未签约
                    resultDto.setApplyState(CompanyProtocolConstants.PROTOCOL_APPLYSTATE_NO);
                }
            }
        }
        return list;
    }

    /**
     * 根据企业id，协议类型查询签约/即将到期的协议
     *
     * @param companyId
     * @param protocolContractType
     * @return
     */
    public CompanyProtocolsBean selectByCorpProtocol(Long companyId, int protocolContractType) {
        return companyProtocolsReaderMapper.selectByCorpProtocol(companyId, protocolContractType);
    }

    /**
     * 获取协议最新的一条数据
     *
     * @param protocolCompanyId
     * @param protocolContractType
     * @return
     */
    public CompanyProtocolsBean selectLastData(long protocolCompanyId, int protocolContractType) {
        return companyProtocolsReaderMapper.selectLastData(protocolCompanyId, protocolContractType);
    }

    /**
     * 获取企业有效协议的数量
     * @param companyId
     * @return
     */
    public int selectUsefulProtocolForCompany(long companyId){
        return companyProtocolsReaderMapper.selectUsefulProtocolForCompany(companyId);
    }

    /**
     * 获取企业有效协议的协议
     * @param companyId
     * @return
     */
    public List<CompanyProtocolsBean> selectUsefulListForCompanyId( long companyId){
        return companyProtocolsReaderMapper.selectUsefulListForCompanyId(companyId);
    }


    /**
     * 续约
     * @param companyId
     * @param contractType
     * @return
     */
    @Override
    public int renewSureProtocol(Map map) {
        return companyProtocolsWriterMapper.renewSureProtocol(map);
    }
}
