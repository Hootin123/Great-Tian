package com.xtr.core.service.company;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.*;
import com.xtr.api.dto.company.CompanyProtocolsDto;
import com.xtr.api.dto.company.CompanyRechargeDto;
import com.xtr.api.dto.company.CompanysDto;
import com.xtr.api.dto.gateway.response.NotifyResponse;
import com.xtr.api.dto.hongbao.LastHongbaoDto;
import com.xtr.api.service.company.*;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.constant.CompanyProtocolConstants;
import com.xtr.comm.jd.util.CodeConst;
import com.xtr.core.persistence.reader.company.CompanysReaderMapper;
import com.xtr.core.persistence.writer.company.CompanysWriterMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by abiao on 2016/6/22.
 */
@Service("companysService")
class CompanysServiceImpl implements CompanysService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CompanysServiceImpl.class);

    @Resource
    private CompanysReaderMapper companysReaderMapper;

    @Resource
    private CompanyRechargesService companyRechargesService;

    @Resource
    private CompanySalaryExcelService companySalaryExcelService;

    @Resource
    private CompanysWriterMapper companysWriterMapper;

    @Resource
    private CompanyProtocolsService companyProtocolsService;

    @Resource
    private CompanyDeptService companyDeptService;




    public int insert(CompanysBean record) {
        return companysWriterMapper.insert(record);
    }

    /**
     * 插入企业信息返回企业ID
     * @param companysBean
     * @return
     */
    public Long insertByCompanyId(CompanysBean companysBean) {
        companysWriterMapper.insert(companysBean);
        LOGGER.info("插入企业信息,返回企业ID:"+companysBean.getCompanyId());

        //增加部门root层
        companyDeptService.initDept(companysBean);

        return companysBean.getCompanyId();
    }

    @Override
    public List<Map<String, Object>> getfileResourcesByFileType(Map<String, Object> map) {
        return companysReaderMapper.getfileResourcesByFileType(map);
    }

    /**
     * 根据公司名称验证企业是否存在
     *
     * @param companyName
     * @return
     */
    public ResultResponse checkCompanyByName(String companyName) throws BusinessException {
        if (StringUtils.isBlank(companyName)) {
            throw new BusinessException("公司名称不能为空");
        }
        ResultResponse resultResponse = new ResultResponse();
        List<CompanysBean> list = companysReaderMapper.checkCompanyByName(companyName);
        resultResponse.setData(list);
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 根据过滤条件获取企业信息
     * @param companysDto
     * @return
     */
    public ResultResponse selectCompanyInfoPageList(CompanysDto companysDto){
        LOGGER.info("获取企业信息列表,传递参数:"+ JSON.toJSONString(companysDto));
        ResultResponse resultResponse = new ResultResponse();
        if (companysDto != null) {
            PageBounds pageBounds = new PageBounds(companysDto.getPageIndex(), companysDto.getPageSize());
            long startTime=new Date().getTime();
            List<CompanysDto> list = companysReaderMapper.selectCompanyInfoBetter(companysDto, pageBounds);
            LOGGER.info("获取所有企业所耗时间:"+String.valueOf(new Date().getTime()-startTime));
//            List<CompanysDto> countList=companysReaderMapper.selectCompanyInfoPageList(companysDto);
            int count = selectCountForCompanyList(companysDto);
            LOGGER.info("获取所有企业数量所耗时间:"+String.valueOf(new Date().getTime()-startTime));

            //有效的签约状态有可能有多个,重新拼装返回的结果集LIST
            List<Long> longList=new ArrayList<>();
            if(list!=null && list.size()>0){
                //获取结果集LIST的每个企业的签约状态拼装
                for(CompanysDto companysDtoResult:list){
                    longList.add(companysDtoResult.getCompanyId());
                }
                List<CompanyProtocolsBean> companyProtocolsList=companyProtocolsService.selectUsefulProtocolsBatch(longList);
                Map<Long,String> protocolsMap=new HashMap<>();
                if(companyProtocolsList!=null && companyProtocolsList.size()>0){
                    for(CompanyProtocolsBean protocolsBean:companyProtocolsList){
                        if(protocolsBean.getProtocolContractType()!=null){
                            for (Map.Entry<Integer, String> entry: CompanyProtocolConstants.PROTOCOLTYPEMAPHAVE.entrySet()) {
                                if(protocolsBean.getProtocolContractType().intValue()== entry.getKey().intValue()){
                                    if(StringUtils.isNotBlank(protocolsMap.get(protocolsBean.getProtocolCompanyId()))){
                                        protocolsMap.put(protocolsBean.getProtocolCompanyId(),protocolsMap.get(protocolsBean.getProtocolCompanyId())+entry.getValue()+",");
                                    }else{
                                        protocolsMap.put(protocolsBean.getProtocolCompanyId(),entry.getValue()+",");
                                    }
                                }
                            }
                        }
                    }
                }
                //为查询的结果集LIST添加签约状态字段
                if(protocolsMap!=null && protocolsMap.size()>0){
                    for(CompanysDto companysDtoResult:list){
                        String protocolStates=protocolsMap.get(companysDtoResult.getCompanyId());
                        if(!com.xtr.comm.util.StringUtils.isStrNull(protocolStates)){
                            companysDtoResult.setProtocolState(protocolStates.substring(0,protocolStates.length()-1));
                        }

                    }
                }
            }
            LOGGER.info("拼装企业信息所耗时间:"+String.valueOf(new Date().getTime()-startTime));
            resultResponse.setData(list);
            //由于分页组件存在BUG,手动分页
            Paginator paginator=new Paginator(companysDto.getPageIndex(),companysDto.getPageSize(),count);
//            Paginator paginator = ((PageList) list).getPaginator();
            resultResponse.setPaginator(paginator);
            resultResponse.setSuccess(true);
        } else {
            resultResponse.setMessage("参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 根据企业编号获取企业详细信息
     * @param companyId
     * @return
     */
    public ResultResponse selectCompanyInfoDetail(long companyId){
        LOGGER.info("获取企业详细信息,传递企业编号参数:"+companyId);
        ResultResponse resultResponse = new ResultResponse();
        List<CompanysDto> list = companysReaderMapper.selectCompanyInfoDetail(companyId);
        if(list!=null && list.size()>0 ){
            CompanysDto companysDto=list.get(0);
            //获取企业的有效协议
            String protocolState=queryCompanyUserfulProtocol(companyId);
            if(!com.xtr.comm.util.StringUtils.isStrNull(protocolState)){
                companysDto.setProtocolState(protocolState.substring(0,protocolState.length()-1));
            }
            resultResponse.setData(companysDto);
            resultResponse.setSuccess(true);
        }else{
            resultResponse.setMessage("获取不到信息");
        }
        return resultResponse;
    }



    public ResultResponse selectCompanyByCompanyNumber(String companyNumber){
        LOGGER.info("获取企业详细信息,传递企业编号参数:"+companyNumber);
        ResultResponse resultResponse = new ResultResponse();
        CompanysBean companysBean = companysReaderMapper.selectCompanyByCompanyNumber(companyNumber);
        if(companysBean!=null){
            resultResponse.setData(companysBean);
            resultResponse.setSuccess(true);
        }else{
            resultResponse.setMessage("获取不到信息");
        }
        return resultResponse;
    }

    /**
     * 根据企业编号获取企业充值或提现记录信息
     * @param companyRechargeDto
     * @return
     */
    public ResultResponse selectCompanyRechargeList(CompanyRechargeDto companyRechargeDto){
        LOGGER.info("根据企业编号获取充值或提现信息,传递参数:"+companyRechargeDto);
        ResultResponse resultResponse = new ResultResponse();
        if (companyRechargeDto != null) {
            resultResponse=companyRechargesService.selectRechargeList(companyRechargeDto,companyRechargeDto.getPageIndex(),companyRechargeDto.getPageSize());
        } else {
            resultResponse.setMessage("参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 根据企业编号获取发工资信息
     * @param companySalaryExcelBean
     * @return
     */
    public ResultResponse selectCompanySalaryList(CompanySalaryExcelBean companySalaryExcelBean){
        LOGGER.info("根据企业编号获取发工资信息,传递参数:"+companySalaryExcelBean);
        ResultResponse resultResponse = new ResultResponse();
        if (companySalaryExcelBean != null) {
            resultResponse=companySalaryExcelService.selectSalaryPageList(companySalaryExcelBean);
        } else {
            resultResponse.setMessage("参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 根据企业编号查询企业信息
     *
     * @param companyId
     * @return
     */
    @Override
    public CompanysBean selectCompanyByCompanyId(Long companyId) {
        if(null != companyId){
            return companysReaderMapper.selectByPrimaryKey(companyId);
        }
        return null;
    }

    /**
     * 新写入数据库记录,companys
     *
     * @param companysBean
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int addCompanys(CompanysBean companysBean) throws BusinessException {
        if (null == companysBean) {
            throw new BusinessException("垫付工资参数为空");
        }
        LOGGER.info("接受参数：" + JSON.toJSONString(companysBean));
        companysBean.setCompanyAddime(new Date());
        int result = companysWriterMapper.insertSelectiveById(companysBean);
        if (result <= 0) {
            throw new BusinessException("垫付工资保存失败");
        }
        result=Integer.parseInt(companysBean.getCompanyId().toString());
        return result;
    }

    /**
     * 修改垫付信息
     *
     * @param companysBean
     * @return
     */
    public void updateCompanysBeanId(CompanysBean companysBean) throws BusinessException {
        int result = companysWriterMapper.updateByPrimaryKeySelective(companysBean);
        if(StringUtils.isNotBlank(companysBean.getCompanyName()))
            companyDeptService.updateRootDeptName(companysBean.getCompanyId(), companysBean.getCompanyName());
        if (result <= 0) {
            throw new BusinessException("修改垫付认证失败");
        }
    }

    @Override
    public Map<String, Object> find(Map<String, Object> param) {
        return  companysReaderMapper.find(param);
    }

    @Override
    public boolean stationNewsClicked(long nId) {
        return companysReaderMapper.stationNewsClickAdd(nId, 1) == 1;
    }

    @Override
    public List<Map<String, Object>> findListPage(Map<String, Object> param) {
        return companysReaderMapper.findListPage(param);
    }

    @Override
    public int findListPageCount(Map<String, Object> param) {
        return companysReaderMapper.findListPageCount(param);
    }


    @Override
    public Map<String, Object> stationCollaborationFind(Map<String, Object> map) {
        return companysReaderMapper.stationCollaborationFind(map);
    }

    @Override
    public int stationCollaborationAdd(Map<String, Object> map) {
        return companysWriterMapper.stationCollaborationAdd(map);
    }

    @Override
    public int stationCollaborationUpdate(Map<String, Object> map) {
        return companysWriterMapper.stationCollaborationUpdate(map);
    }

    /**
     * 企业协议管理时更改企业相关信息
     * @param companysBean
     * @return
     */
    public int updateByProtocolModify(CompanysBean companysBean)throws BusinessException{
        return companysWriterMapper.updateByProtocolModify(companysBean);
    }

    @Override
    public int addFileResources(FileResourcesBean fileResourcesBean) throws BusinessException {
        if (null == fileResourcesBean) {
            throw new BusinessException("上传文件参数为空");
        }
        LOGGER.info("接受参数：" + JSON.toJSONString(fileResourcesBean));
        int result = companysWriterMapper.insertFileResourcesById(fileResourcesBean);
        if (result <= 0) {
            throw new BusinessException("上传文件保存失败");
        }
        result=Integer.parseInt(fileResourcesBean.getId().toString());
        return result;
    }

    @Override
    public List<Map<String, Object>> getfileResources(Map<String, Object> map) {
        return companysReaderMapper.getfileResources(map);
    }

    /**
     * 删除资源文件
     *
     * @param id
     * @return
     */
    public void deletefileResources(Long id) throws BusinessException {
        if (id == null) {
            throw new BusinessException("主键不能为空");
        }
        int result = companysWriterMapper.deletefileByPrimaryKey(id);
        if (result <= 0) {
            throw new BusinessException("资源文件删除失败");
        }
    }

    /**
     * 更改企业信息
     * @param companysBean
     * @return
     * @throws BusinessException
     */
    public int  updateByPrimaryKeySelective(CompanysBean companysBean) {
        return companysWriterMapper.updateByPrimaryKeySelective(companysBean);
    }

    @Override
    public ResultResponse selectPrepaidPage(CompanysBean companysBean, String kw) {
        PageBounds pageBounds = new PageBounds(companysBean.getPageIndex(), companysBean.getPageSize());
        PageList<Map<String, Object>> pageList = companysReaderMapper.selectPrepaidPage(companysBean.getCompanyDatumStatus(), companysBean.getCompanyIsauth(), kw, pageBounds);

        if(pageList != null)
            for (Map<String, Object> stringObjectMap : pageList) {
                stringObjectMap.put("isCanSignDf", companyProtocolsService.isCanSignDf((long)stringObjectMap.get("company_id")));
            }

        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setData(pageList);
        resultResponse.setPaginator(pageList.getPaginator());
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    @Override
    public boolean auditPrepaid(long cId, String cause, boolean b, Long memberId) {
        return companysWriterMapper.updatePrepaidStatus(cId, cause, b, memberId) > 0;
    }

    @Override
    public boolean doJdResponse(NotifyResponse response) {
        if(response == null)
            return false;
        String tradeStatus = response.getTradeStatus();
        if(CodeConst.TRADE_WPAR.equals(tradeStatus) || CodeConst.TRADE_BUID.equals(tradeStatus) || CodeConst.TRADE_ACSU.equals(tradeStatus)){
            LOGGER.info("订单等待中，暂不处理");
        }else if (CodeConst.TRADE_FINI.equals(tradeStatus)) {//交易成功
            LOGGER.info("订单交易成功");
            companysWriterMapper.updatePayStatus(response.getOutTradeNo(), 1, null);
            return true;
        } else if (CodeConst.TRADE_REFUND.equals(tradeStatus)) {
            //成功后退款处理
            LOGGER.info("订单退款");
            companysWriterMapper.updatePayStatus(response.getOutTradeNo(), 3, response.getResponseMessage());
        } else {
            LOGGER.info("订单失败");
            companysWriterMapper.updatePayStatus(response.getOutTradeNo(), 2, response.getResponseMessage());
        }
        return false;
    }

    /**
     * 获取企业的有效协议
     * @param companyId
     * @return
     */
    private String queryCompanyUserfulProtocol(Long companyId){
        String protocolState="";
        //获取企业的有效协议
        CompanyProtocolsBean companyProtocolsBean=new CompanyProtocolsBean();
        companyProtocolsBean.setProtocolCompanyId(companyId);
        List<CompanyProtocolsBean> companyProtocolsBeanList=companyProtocolsService.selectUsefulProtocolsByState(companyProtocolsBean);
        if(companyProtocolsBeanList!=null && companyProtocolsBeanList.size()>0){
            for(CompanyProtocolsBean protocolsBean:companyProtocolsBeanList){
                if(protocolsBean!=null && protocolsBean.getProtocolContractType()!=null){
//                    if(protocolsBean.getProtocolContractType()== CompanyProtocolConstants.PROTOCOL_TYPE_DF){
//                        protocolState+="代发协议,";
//                    }else if(protocolsBean.getProtocolContractType()== CompanyProtocolConstants.PROTOCOL_TYPE_FF){
//                        protocolState+="垫发协议,";
//                    }else if(protocolsBean.getProtocolContractType()== CompanyProtocolConstants.PROTOCOL_TYPE_DJ){
//                        protocolState+="代缴社保协议,";
//                    }
                    for (Map.Entry<Integer, String> entry: CompanyProtocolConstants.PROTOCOLTYPEMAPHAVE.entrySet()) {
                        if(protocolsBean.getProtocolContractType().intValue()== entry.getKey().intValue()){
                            protocolState+=entry.getValue()+",";
                        }
                    }
                }
            }
        }
        return protocolState;
    }

    /**
     * 查询当前领取红包的hr人数
     * @return
     */
    @Override
    public long selectActivityRedCounts() {
        return companysReaderMapper.selectActivityRedCounts();
    }

    /**
     * 查询领取完善信息红包的人数
     * @return
     */
    @Override
    public long selectCountsOfPerfectInfo() {
        return companysReaderMapper.selectselectCountsOfPerfectInfo();
    }

    /**
     * 查询当前登录用户已领取的红包金额
     * @param memberId
     * @return
     */
    @Override
    public long selectHasReceiveAccount(Long memberId) {
        return companysReaderMapper.selectHasReceiveAccount(memberId);
    }



    /**
     * 查询当前注册账户是否之前已经领取过5元或10元完善信息红包
     * @param params
     * @return
     */
    @Override
    public CompanyActivityBean selectReds(Map<String, Object> params) {
        return companysReaderMapper.selectReds(params);
    }

    /**
     *保存当前发送红包的信息记录
     * @param activity
     * @return
     */
    @Override
    public CompanyActivityBean saveCompanyActivity(CompanyActivityBean activity) {
        companysWriterMapper.saveCompanyActivity(activity);
        return activity;
    }

    /**
     * 更新红包
     * @param updateMap
     * @return
     */
    @Transactional
    @Override
    public int updateActivity(Map updateMap) {
        return companysWriterMapper.updateActivity(updateMap);
    }

    /**
     * 获取企业总数
     * @param companysDto
     * @return
     */
    public Integer selectCountForCompanyList(CompanysDto companysDto){
        return companysReaderMapper.selectCountForCompanyList(companysDto);
    }

    /**
     * 查询入职须知
     * @param wechatCompanyId
     * @return
     */
    @Override
    public CompanysBean selectCompanyEnterRequireById(Long wechatCompanyId) {
        return companysReaderMapper.selectCompanyEnterRequireById(wechatCompanyId);
    }

    @Override
    public int updateCollectInfo(Map map) {
        return companysWriterMapper.updateCollectInfo(map);
    }

    /**
     * 简化版红包注册页面之前保存手机号
     * @param lastHongbaoDto
     * @return
     */
    @Override
    public ResultResponse saveCompanyPhone(LastHongbaoDto lastHongbaoDto) throws  Exception {
        ResultResponse  resultResponse = new ResultResponse();
        int insertFlag = companysWriterMapper.saveCompanyPhone(lastHongbaoDto);
        if(insertFlag==1){
            resultResponse.setSuccess(true);
        }
        return resultResponse;
    }
}
