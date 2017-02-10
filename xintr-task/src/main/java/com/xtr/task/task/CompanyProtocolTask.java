package com.xtr.task.task;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyProtocolsBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.dto.company.CompanyProtocolsDto;
import com.xtr.api.service.company.CompanyProtocolsService;
import com.xtr.api.service.station.StationCollaborationService;
import com.xtr.comm.constant.CompanyProtocolConstants;
import com.xtr.comm.constant.StationCollaborationConstants;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.SpringUtils;
import com.xtr.comm.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 企业协议状态自动变更任务
 *
 * @author 王志新 robert.wang@xintairuan.com
 * @createTime:2016/7/19 18:25
 */
public class CompanyProtocolTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyProtocolTask.class);

    private CompanyProtocolsService companyProtocolsService;

    private StationCollaborationService stationCollaborationService;

    /**
     * 创建线程池
     */
//    ExecutorService pool = Executors.newCachedThreadPool();
    public CompanyProtocolTask() {
        super();
        companyProtocolsService = (CompanyProtocolsService) SpringUtils.getBean("companyProtocolsService");
        stationCollaborationService = (StationCollaborationService) SpringUtils.getBean("stationCollaborationService");
    }

    /**
     * 定时任务处理
     */
    public void run() throws Exception {
        //获取当前时间
        String currentTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        LOGGER.info("开始执行【" + currentTime + "】企业协议状态自动变更任务");
        //要更改状态的企业协议集合
        List<CompanyProtocolsBean> modifyStateListAlready = new ArrayList<CompanyProtocolsBean>();
//        List<CompanyProtocolsBean> modifyStateListWill = new ArrayList<CompanyProtocolsBean>();
        //获取所有的企业协议
        List<CompanyProtocolsDto> protocolList = companyProtocolsService.selectProtocolAll();
        if (protocolList != null && protocolList.size() > 0) {
            for (CompanyProtocolsDto companyProtocolBean : protocolList) {
                if (companyProtocolBean.getProtocolId() != null && companyProtocolBean.getProtocolExpireTime() != null) {
                    //获取到期时间
                    String compareTime = new SimpleDateFormat("yyyy-MM-dd").format(companyProtocolBean.getProtocolExpireTime());
                    int differTime = DateUtil.getDiffDaysOfTwoDateByNegative(compareTime, currentTime);
                    LOGGER.info("企业协议状态自动变更任务【" + currentTime + "】,到期时间与当前时间相差" + differTime + "天");
                    if (differTime < 0) {//如果当前时间超过了到期时间,则更改状态为合约到期
                        //只有状态为签约或即将到期或冻结且不为合约到期才可以更改为合约到期
                        if (companyProtocolBean.getProtocolCurrentStatus() != null
                                && (companyProtocolBean.getProtocolCurrentStatus().intValue() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FINISH
                                || companyProtocolBean.getProtocolCurrentStatus().intValue() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_WILL
                                || companyProtocolBean.getProtocolCurrentStatus().intValue() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FREESON)) {
                            CompanyProtocolsBean modifyStateBean = new CompanyProtocolsBean();
                            modifyStateBean.setProtocolId(companyProtocolBean.getProtocolId());
                            modifyStateBean.setProtocolCurrentStatus(CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_ALREADY);
                            modifyStateListAlready.add(modifyStateBean);
                            modifyStateBean = null;
                        }
                    } else if (differTime >= 0 && differTime <= 30) {//如果当前时间离到期时间小于等于三十天,则更改状态为即将到期,只有签约可以改为即将到期
                        //只有状态为签约且不为即将到期的才可以改为即将到期
                        if (companyProtocolBean.getProtocolCurrentStatus() != null && companyProtocolBean.getProtocolCurrentStatus().intValue() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FINISH) {
                            CompanyProtocolsBean modifyStateBean = new CompanyProtocolsBean();
                            modifyStateBean.setProtocolId(companyProtocolBean.getProtocolId());
                            modifyStateBean.setProtocolCurrentStatus(CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_WILL);
                            modifyStateListAlready.add(modifyStateBean);
                            modifyStateBean = null;
                        }
                        //如果签约的代发协议离到期还有整30天并且协议关联的企业ID不为空,则发起合作意向
                        if (companyProtocolBean.getProtocolContractType() != null && companyProtocolBean.getProtocolCurrentStatus() != null
                                && companyProtocolBean.getProtocolContractType().intValue() == CompanyProtocolConstants.PROTOCOL_TYPE_DF
                                && (companyProtocolBean.getProtocolCurrentStatus().intValue() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FINISH
                                || companyProtocolBean.getProtocolCurrentStatus().intValue() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_WILL
                                || companyProtocolBean.getProtocolCurrentStatus().intValue() == CompanyProtocolConstants.PROTOCOL_CURRENTSTATE_FREESON)
                                && differTime == 30
                                && companyProtocolBean.getProtocolCompanyId() != null) {
                            try {
                                CompanysBean companysBean = new CompanysBean();
                                companysBean.setCompanyId(companyProtocolBean.getProtocolCompanyId());
                                if (!StringUtils.isStrNull(companyProtocolBean.getCompanyName())) {
                                    companysBean.setCompanyName(companyProtocolBean.getCompanyName());
                                }
                                if (!StringUtils.isStrNull(companyProtocolBean.getCompanyContactTel())) {
                                    companysBean.setCompanyCorporationPhone(companyProtocolBean.getCompanyContactTel());
                                }
                                ResultResponse collaborationResponse = stationCollaborationService.updateOpeationType(companysBean, StationCollaborationConstants.STATIONCOLLABRATION_TYPE_BEFORETHIRTY);
                                if (!collaborationResponse.isSuccess()) {
                                    LOGGER.error("离到期还有30天,更新合作意向失败,【" + currentTime + "】,失败原因:" + collaborationResponse.getMessage());
                                }

                            } catch (Exception e) {
                                LOGGER.error("离到期还有30天,更新合作意向失败,【" + currentTime + "】,失败原因:" + e.getMessage());
                            }
                        }
                    }
                }
            }
        }
        LOGGER.info("企业协议状态自动变更任务【" + currentTime + "】,要变更的企业协议集合:" + JSON.toJSONString(modifyStateListAlready));
        if (modifyStateListAlready != null && modifyStateListAlready.size() > 0) {
            for (CompanyProtocolsBean paramBean : modifyStateListAlready) {
                // 这边不要事务,更新一个算一个
                int count = companyProtocolsService.updateCurrentStateById(paramBean);
                if (count <= 0) {
                    LOGGER.error("企业协议状态自动变更任务,【" + currentTime + "】,变更失败的协议参数:" + JSON.toJSONString(paramBean));
                }
            }

        }
        modifyStateListAlready = null;
        LOGGER.info("结束执行【" + currentTime + "】企业协议状态自动变更任务");
    }

}
