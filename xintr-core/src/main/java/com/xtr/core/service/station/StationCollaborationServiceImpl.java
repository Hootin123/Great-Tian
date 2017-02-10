package com.xtr.core.service.station;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyProtocolsBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.station.StationCollaborationBean;
import com.xtr.api.dto.customer.CustomerReachrgeDto;
import com.xtr.api.service.company.CompanyProtocolsService;
import com.xtr.api.service.station.StationCollaborationService;
import com.xtr.comm.constant.CompanyProtocolConstants;
import com.xtr.comm.constant.StationCollaborationConstants;
import com.xtr.comm.excel.ExcelDoc;
import com.xtr.comm.excel.ExcelExporter;
import com.xtr.comm.excel.WorkModel;
import com.xtr.comm.excel.WorkModelStation;
import com.xtr.comm.util.StringUtils;
import com.xtr.core.persistence.reader.station.StationCollaborationReaderMapper;
import com.xtr.core.persistence.writer.station.StationCollaborationWriterMapper;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/13 13:21
 */
@Service("stationCollaborationService")
public class StationCollaborationServiceImpl implements StationCollaborationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StationCollaborationServiceImpl.class);

    @Resource
    private StationCollaborationReaderMapper stationCollaborationReaderMapper;

    @Resource
    private StationCollaborationWriterMapper stationCollaborationWriterMapper;

    @Resource
    private CompanyProtocolsService companyProtocolsService;

    /**
     * 根据过滤条件获取合作意向信息
     * @param stationCollaborationBean
     * @return
     */
    public ResultResponse selectStationCollaborationInfoPageList(StationCollaborationBean stationCollaborationBean){
        LOGGER.info("根据过滤条件获取合作意向的参数信息:"+ JSON.toJSONString(stationCollaborationBean));
        ResultResponse resultResponse = new ResultResponse();
        if (stationCollaborationBean != null) {
            List<StationCollaborationBean> list =null;
            //有企业关联的，显示关联的企业名称和手机号，没有企业关联的，显示自己的企业名称和手机号，这儿手动处理分页
            if(stationCollaborationBean!=null && !StringUtils.isStrNull(stationCollaborationBean.getItemName())){//企业名称和手机过滤条件不为空
                List<StationCollaborationBean> queryList= stationCollaborationReaderMapper.selectStationCollaborationInfoPageList(stationCollaborationBean);
                list=new ArrayList<>();//最终要显示的LIST
                List<StationCollaborationBean> useFulCollabrationList=new ArrayList<>();//拼装符合条件的LIST
                if(queryList!=null && queryList.size()>0){
                    for(StationCollaborationBean collaborationBean:queryList){
                        if(collaborationBean.getCollaborationCompanyId()!=null){//有企业关联的意向
                            if((!StringUtils.isStrNull(collaborationBean.getCompanyName()) && collaborationBean.getCompanyName().contains(stationCollaborationBean.getItemName()))
                                    ||(!StringUtils.isStrNull(collaborationBean.getMemberPhone()) && collaborationBean.getMemberPhone().contains(stationCollaborationBean.getItemName()))
                                    ||(!StringUtils.isStrNull(String.valueOf(collaborationBean.getCollaborationCompanyId())) && String.valueOf(collaborationBean.getCollaborationCompanyId()).contains(stationCollaborationBean.getItemName()))
                                    ){
                                useFulCollabrationList.add(collaborationBean);
                            }
                        }else{//无企业关联的意向
                            LOGGER.info(collaborationBean.getItemName()+","+collaborationBean.getMemberPhone()+","+stationCollaborationBean.getItemName());
                            if((!StringUtils.isStrNull(collaborationBean.getItemName())) && (collaborationBean.getItemName().contains(stationCollaborationBean.getItemName()))){
                                useFulCollabrationList.add(collaborationBean);
                            }else if((!StringUtils.isStrNull(collaborationBean.getItemMobile())) && (collaborationBean.getItemMobile().contains(stationCollaborationBean.getItemName()))){
                                useFulCollabrationList.add(collaborationBean);
                            }
                        }
                    }
                }
                //分页
                if (stationCollaborationBean.getPageIndex() == 0){
                    stationCollaborationBean.setPageIndex(1);
                }
                int startIndex=stationCollaborationBean.getPageSize()*(stationCollaborationBean.getPageIndex()-1<0?0:stationCollaborationBean.getPageIndex()-1);
                int lastIndex=stationCollaborationBean.getPageIndex()*stationCollaborationBean.getPageSize();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                for(int i=startIndex;i<lastIndex;i++){
                    //不能超出范围
                    if((i+1)>useFulCollabrationList.size()){
                        break;
                    }
                    list.add(useFulCollabrationList.get(i));
                }
                LOGGER.info("根据过滤条件获取合作意向信息,获取分页展示的列表:"+JSON.toJSONString(list));
                Paginator paginator=new Paginator(stationCollaborationBean.getPageIndex(),stationCollaborationBean.getPageSize(),list.size());
                resultResponse.setData(list);
                resultResponse.setPaginator(paginator);
                resultResponse.setSuccess(true);
            }else{
                PageBounds pageBounds = new PageBounds(stationCollaborationBean.getPageIndex(), stationCollaborationBean.getPageSize());
                list= stationCollaborationReaderMapper.selectStationCollaborationInfoPageList(stationCollaborationBean,pageBounds);
                resultResponse.setData(list);
                Paginator paginator = ((PageList) list).getPaginator();
                resultResponse.setPaginator(paginator);
                resultResponse.setSuccess(true);
            }
        } else {
            resultResponse.setMessage("参数不能为空");
        }
        return resultResponse;
    }

    /**
     * 根据ID查询合作意向信息
     * @param itemId
     * @return
     */
    public StationCollaborationBean selectStationCollaborationInfoById(Long itemId){
        return stationCollaborationReaderMapper.selectStationCollaborationInfoById(itemId);
    }

    /**
     *  根据ID更新合作意向数据
     * @param record
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateByPrimaryKeySelective(StationCollaborationBean record){
        return stationCollaborationWriterMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据企业ID和意向类型查询合作意向信息
     * @param stationCollaborationBean
     * @return
     */
    public List<StationCollaborationBean> selectInfoByCompanyIdAndType(StationCollaborationBean stationCollaborationBean){
        return stationCollaborationReaderMapper.selectInfoByCompanyIdAndType(stationCollaborationBean);
    }

    /**
     * 新增合作意向
     * @param record
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int insertSelective(StationCollaborationBean record){
        return stationCollaborationWriterMapper.insertSelective(record);
    }

    /**
     * 埋点更新发起次数或新增合作意向
     * @param companysBean
     * @return
     */
    public ResultResponse updateOpeationType(CompanysBean companysBean,int type)throws Exception{
        ResultResponse resultResponse=new ResultResponse();
        //拼接日志信息
        String logStr="";
        if(type==StationCollaborationConstants.STATIONCOLLABRATION_TYPE_NEWUSER){//新用户注册
            logStr="新用户注册发起合作意向";
        }else if(type==StationCollaborationConstants.STATIONCOLLABRATION_TYPE_COMMITSARLY){//提交工资单
            logStr="提交工资单发起合作意向";
        }else if(type==StationCollaborationConstants.STATIONCOLLABRATION_TYPE_BEFORETHIRTY){//签约到期前30天
            logStr="签约到期前30天发起合作意向";
        }else if(type==StationCollaborationConstants.STATIONCOLLABRATION_TYPE_INTENT){//融资意向
            logStr="融资意向发起合作意向";
        }
        LOGGER.info(logStr+",传递参数:"+JSON.toJSONString(companysBean)+",类型:"+type);
        if(companysBean!=null && companysBean.getCompanyId()!=null){
            //企业ID
            Long companyId=companysBean.getCompanyId();
            //企业名称
            String companyName=companysBean.getCompanyName();
            //企业手机号
            String companyPhone=companysBean.getCompanyCorporationPhone();
            //要更新或新增的合作意向实体类
            StationCollaborationBean stationCollaborationBean=new StationCollaborationBean();
            stationCollaborationBean.setCollaborationCompanyId(companyId);
            stationCollaborationBean.setItemType(type);

            //根据企业ID和意向类型查询合作意向信息
            List<StationCollaborationBean> stationCollaborationList= selectInfoByCompanyIdAndType(stationCollaborationBean);
            LOGGER.info(logStr+",获取原先的意向结果:"+JSON.toJSONString(stationCollaborationList));
            if(stationCollaborationList!=null && stationCollaborationList.size()>0){//该企业已经存在意向,发起次数+1
                StationCollaborationBean resultCollaborationBean=stationCollaborationList.get(0);
                if(resultCollaborationBean!=null && resultCollaborationBean.getItemId()!=null){
                    int itemTimes=resultCollaborationBean.getItemTimes();
                    int itemSign=resultCollaborationBean.getSign();
                    if(itemSign!=StationCollaborationConstants.STATIONCOLLABRATION_STATE_FINISH){//如果合作意向状态为未签约才能更改次数
                        stationCollaborationBean=new StationCollaborationBean();
                        stationCollaborationBean.setItemId(resultCollaborationBean.getItemId());
                        stationCollaborationBean.setItemTimes(itemTimes+1);
                        if(itemSign==StationCollaborationConstants.STATIONCOLLABRATION_STATE_CLOSE){//如果该合作意向状态为关闭,则还要更新关闭后的更改次数
                            int closeItems=resultCollaborationBean.getCollaborationCloseItems()==null?1:resultCollaborationBean.getCollaborationCloseItems();
                            //如果加上这次为第十一次(因为是从1开始计数的),则更改状态由关闭更改为待联系,并且关闭后的更改次数重新为1
                            if(closeItems+1==11){
                                stationCollaborationBean.setCollaborationCloseItems(1);
                                stationCollaborationBean.setSign(StationCollaborationConstants.STATIONCOLLABRATION_STATE_WILL);
                            }else{
                                stationCollaborationBean.setCollaborationCloseItems(closeItems+1);
                            }
                        }
                        int count=updateByPrimaryKeySelective(stationCollaborationBean);
                        if(count<=0){
                            throw new Exception(logStr+",更新发起次数失败");
                        }
                    }
                }else{
                    throw new Exception(logStr+",获取不到合作意向信息");
                }
            }else{//为该企业新增意向
                //融资意向有签约的垫发协议,则不再新增,其它的除了签到前30天,只要有状态的代发协议,则不再新增
                boolean isAddFlag=true;//是否新增,true新增,false不新增
                List<CompanyProtocolsBean> companyProtocolList=companyProtocolsService.selectByCompanyId(companyId);
                if(type==StationCollaborationConstants.STATIONCOLLABRATION_TYPE_INTENT){//融资意向
                    for(CompanyProtocolsBean companyProtocolsBean:companyProtocolList){
                        if(companyProtocolsBean!=null && companyProtocolsBean.getProtocolContractType()!=null
                                && companyProtocolsBean.getProtocolContractType()== CompanyProtocolConstants.PROTOCOL_TYPE_FF
                                &&companyProtocolsBean.getProtocolCurrentStatus()!=null
                                && (companyProtocolsBean.getProtocolCurrentStatus()==CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FINISH
                                || companyProtocolsBean.getProtocolCurrentStatus()==CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_WILL)){//有签约的垫发协议
                            isAddFlag=false;
                            break;
                        }
                    }
                }else if(type==StationCollaborationConstants.STATIONCOLLABRATION_TYPE_NEWUSER
                        ||type==StationCollaborationConstants.STATIONCOLLABRATION_TYPE_COMMITSARLY){//其它的除了签到前30天意向
                    for(CompanyProtocolsBean companyProtocolsBean:companyProtocolList){
                        if(companyProtocolsBean!=null && companyProtocolsBean.getProtocolContractType()!=null
                                && companyProtocolsBean.getProtocolContractType()== CompanyProtocolConstants.PROTOCOL_TYPE_DF
                                &&companyProtocolsBean.getProtocolCurrentStatus()!=null){//有签约的代发协议
//                            && (companyProtocolsBean.getProtocolCurrentStatus()==CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FINISH
//                                    || companyProtocolsBean.getProtocolCurrentStatus()==CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_WILL
//                                    || companyProtocolsBean.getProtocolCurrentStatus()==CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_WILL)
                            isAddFlag=false;
                            break;
                        }
                    }
                }
                if(isAddFlag){//新增意向
                    if(!StringUtils.isStrNull(companyPhone)){
                        stationCollaborationBean.setItemMobile(companyPhone);
                    }
                    if(!StringUtils.isStrNull(companyName)){
                        stationCollaborationBean.setItemName(companyName);
                    }
                    stationCollaborationBean.setAdddate(new Date());
                    stationCollaborationBean.setSign(StationCollaborationConstants.STATIONCOLLABRATION_STATE_WILL);
                    stationCollaborationBean.setItemTimes(1);
                    int count=insertSelective(stationCollaborationBean);
                    if(count<=0){
                        throw new Exception(logStr+",新增失败");
                    }
                }
            }
        }else{
            throw new Exception(logStr+",传递参数为空");
        }

        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 企业签约,企业意向自动变为签约状态
     * @param protocolType
     * @param companyId
     * @return
     * @throws Exception
     */
    public ResultResponse updateStateAuto(Integer protocolType,Long companyId)throws Exception{
        ResultResponse resultResponse=new ResultResponse();
        if(protocolType!=null && companyId!=null){
            //根据企业ID获取所有的合作意向
            StationCollaborationBean queryBean=new StationCollaborationBean();
            queryBean.setCollaborationCompanyId(companyId);
            List<StationCollaborationBean> stationCollaborationList= selectInfoByCompanyIdAndType(queryBean);
            if(stationCollaborationList!=null && stationCollaborationList.size()>0){
                for(StationCollaborationBean stationCollaborationBean:stationCollaborationList){
                    if(stationCollaborationBean!=null && stationCollaborationBean.getItemType()!=null && stationCollaborationBean.getSign()!=null
                            && stationCollaborationBean.getSign().intValue()!=StationCollaborationConstants.STATIONCOLLABRATION_STATE_FINISH
                            && stationCollaborationBean.getSign().intValue()!=StationCollaborationConstants.STATIONCOLLABRATION_STATE_CLOSE){
                        //如果是代发协议签约,则新用户注册\提交工资单的意向状态变更为签约,前提是意向本身状态不是签约和关闭
                        //如果是垫发协议签约,则融资意向的状态变更为签约,前提是意向状态本身不是签约和关闭
                        if((protocolType.intValue()==CompanyProtocolConstants.PROTOCOL_TYPE_DF
                                && (stationCollaborationBean.getItemType().intValue()==StationCollaborationConstants.STATIONCOLLABRATION_TYPE_NEWUSER
                                || stationCollaborationBean.getItemType().intValue()==StationCollaborationConstants.STATIONCOLLABRATION_TYPE_COMMITSARLY
                                || stationCollaborationBean.getItemType().intValue()==StationCollaborationConstants.STATIONCOLLABRATION_TYPE_SURE))
                            ||(protocolType.intValue()==CompanyProtocolConstants.PROTOCOL_TYPE_FF
                                && (stationCollaborationBean.getItemType().intValue()==StationCollaborationConstants.STATIONCOLLABRATION_TYPE_INTENT
                                || stationCollaborationBean.getItemType().intValue()==StationCollaborationConstants.STATIONCOLLABRATION_TYPE_SURE))
                            ||(protocolType.intValue()==CompanyProtocolConstants.PROTOCOL_TYPE_DJ
                                && stationCollaborationBean.getItemType().intValue()==StationCollaborationConstants.STATIONCOLLABRATION_TYPE_SURE)){
                            StationCollaborationBean modifyBean=new StationCollaborationBean();
                            modifyBean.setItemId(stationCollaborationBean.getItemId());
                            modifyBean.setSign(StationCollaborationConstants.STATIONCOLLABRATION_STATE_FINISH);
                            int count=stationCollaborationWriterMapper.updateByPrimaryKeySelective(modifyBean);
                            if(count<=0){
                                throw new Exception("企业签约,企业意向自动变为签约状态,更新状态失败");
                            }
                        }
                    }
                }
            }
            resultResponse.setSuccess(true);
        }else{
            resultResponse.setMessage("企业签约,企业意向自动变为签约状态,传递参数不正确");
        }
        return resultResponse;
    }

    /**
     * 获取批量导出信息
     * @param stationCollaborationBean
     * @return
     */
    public List<StationCollaborationBean> selectInfoByBatch(StationCollaborationBean stationCollaborationBean){
        return stationCollaborationReaderMapper.selectInfoByBatch(stationCollaborationBean);
    }


    /**
     * 批量导出意向
     * @param webRoot
     * @param payTemplatePath
     * @param downloadPath
     * @return
     */
    public File generatorSalaryExcel(String webRoot, String payTemplatePath, String downloadPath,Date startTime,Date endTime)throws Exception {

        String payTempaltePath = webRoot + payTemplatePath;
        StationCollaborationBean stationCollaborationBean=new StationCollaborationBean();
        if(startTime!=null){
            stationCollaborationBean.setStartTime(startTime);
        }
        if(endTime!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String endStr = sdf.format(endTime);
            endStr = endStr.substring(0, 10);
            endStr = endStr + " 23:59:59";
            Date endDate = sdf.parse(endStr);
            stationCollaborationBean.setEndTime(endDate);
        }
        List<StationCollaborationBean> list = selectInfoByBatch(stationCollaborationBean);
        List<WorkModelStation> dataset = new ArrayList<WorkModelStation>();
        if (!CollectionUtils.isEmpty(list)) {
            int count = 1;
            for (StationCollaborationBean item : list) {
                //企业名称
                String companyName = item.getCompanyName();
                //企业
                String companyId = String.valueOf(item.getCollaborationCompanyId());
                //注册手机号
                String memberPhone="";
                if(item.getItemType()!=null && item.getItemType()==StationCollaborationConstants.STATIONCOLLABRATION_TYPE_WORKER){//官网取自己的手机号
                    memberPhone = item.getItemMobile();
                }else{
                    memberPhone = item.getMemberPhone();
                }
                //首次发起时间
                String firstTime = item.getAdddate()!=null?new SimpleDateFormat("yyyy-MM-dd").format(item.getAdddate()):"";
                //发起次数
                String itemTimes=String.valueOf(item.getItemTimes());
                //来源渠道
                String sourceStr="";
                if(!StringUtils.isStrNull(item.getCompanyContactPlace())){
                    sourceStr="H5红包";
                }else if(item.getItemType()!=null && item.getItemType()==StationCollaborationConstants.STATIONCOLLABRATION_TYPE_WORKER){
                    sourceStr="官网";
                }
                //状态
                String stateStr="";
                if(item.getSign()!=null && item.getSign()==StationCollaborationConstants.STATIONCOLLABRATION_STATE_WILL){
                    stateStr="待联系";
                }else if(item.getSign()!=null && item.getSign()==StationCollaborationConstants.STATIONCOLLABRATION_STATE_ALREADY){
                    stateStr="已联系";
                }else if(item.getSign()!=null && item.getSign()==StationCollaborationConstants.STATIONCOLLABRATION_STATE_FINISH){
                    stateStr="已签约";
                }else if(item.getSign()!=null && item.getSign()==StationCollaborationConstants.STATIONCOLLABRATION_STATE_CLOSE){
                    stateStr="关闭";
                }
                dataset.add(new WorkModelStation(companyName, companyId,memberPhone, firstTime, itemTimes, sourceStr,stateStr));
                count++;
            }
        }
        String filePath = webRoot + downloadPath + "/stationCollaboration_" + System.currentTimeMillis() + ".xls";

        File file = new File(filePath);

        ExcelDoc excelDoc = new ExcelDoc("合作意向");
//                ExcelDoc excelDoc = new ExcelDoc(companyName + "_2016年5月员工工资单");
        excelDoc.setTemplateHead(payTempaltePath);
        excelDoc.setStartRow(1);
        excelDoc.setDateSet(dataset);
        excelDoc.setOutPath(file.getPath());

        HSSFWorkbook workbook = ExcelExporter.workbook(excelDoc);
        HSSFCellStyle cellStyle = workbook.createCellStyle();

        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        cellStyle.setFont(font);

        excelDoc.setRowCellStyle(cellStyle);
        excelDoc.setRowHeight((short) 450);

        ExcelExporter.export(excelDoc, workbook);

        return file;
    }

    /**
     * 获取已提交申请的明确签约意向
     * @param collaborationCompanyId
     * @return
     */
    public List<StationCollaborationBean> selectInfoForAlreadyApply(Long collaborationCompanyId){
        return stationCollaborationReaderMapper.selectInfoForAlreadyApply(collaborationCompanyId);
    }

    /**
     * 撤销申请
     * @param companyId
     * @param contractType
     * @return
     */
    @Override
    public int deleteSureProtocol(Map map) throws Exception{
        return stationCollaborationWriterMapper.deleteSureProtocol(map);
    }
}
