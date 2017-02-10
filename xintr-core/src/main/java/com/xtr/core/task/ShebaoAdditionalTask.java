package com.xtr.core.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xtr.api.domain.company.CompanyShebaoOrderBean;
import com.xtr.api.domain.customer.CustomerShebaoBean;
import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.service.sbt.SbtService;
import com.xtr.api.service.shebao.CompanyShebaoService;
import com.xtr.api.service.shebao.CustomerShebaoOrderService;
import com.xtr.api.service.shebao.CustomerShebaoService;
import com.xtr.comm.enums.ShebaoTypeEnum;
import com.xtr.comm.jd.util.Base64;
import com.xtr.comm.oss.AliOss;
import com.xtr.comm.sbt.SbtResponse;
import com.xtr.comm.sbt.SheBaoTong;
import com.xtr.comm.sbt.api.*;
import com.xtr.comm.util.PropertyUtils;
import com.xtr.core.persistence.reader.company.CompanyShebaoOrderReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomerShebaoOrderReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomerShebaoReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomersReaderMapper;
import com.xtr.core.persistence.writer.company.CompanyShebaoOrderWriterMapper;
import com.xtr.core.persistence.writer.customer.CustomersWriterMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * 员工社保资料审核任务
 *
 * @author 薛武
 * @createTime: 2016/10/10 9:50.
 */
@Service("shebaoAdditionalTask")
public class ShebaoAdditionalTask extends BaseTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShebaoAdditionalTask.class);

    @Resource
    private CustomersReaderMapper customersReaderMapper;

    @Resource
    private CustomersWriterMapper customersWriterMapper;

    // 社保通sdk
    private SheBaoTong sheBaoTong = new SheBaoTong(true);

    @Override
    public void run() throws Exception{
        List<CustomersBean> customersBeen = customersReaderMapper.selectShebaoAdditionalCustomer();
        LOGGER.info("社保资料未审核的用户：【{}】", JSONObject.toJSON(customersBeen));
        if(customersBeen != null) {
            for (CustomersBean customersBean : customersBeen) {
                try {
                    EmployeeAttach employeeAttach = new EmployeeAttach();
                    if(StringUtils.isBlank(customersBean.getCustomerIdcard()) || StringUtils.isBlank(customersBean.getCustomerTurename())  || StringUtils.isBlank(customersBean.getCustomerIdcardImgFront())) {
                        LOGGER.info("员工【{}】社保资料不全，无法进行资料审核", customersBean);
                        continue;
                    }
                    employeeAttach.setId(customersBean.getCustomerIdcard());
                    employeeAttach.setName(customersBean.getCustomerTurename());
                    String base64Code = Base64.encode(AliOss.downloadFileByte(PropertyUtils.getString("oss.bucketName.img"), customersBean.getCustomerIdcardImgFront()));
                    Attachment attachment = new Attachment(base64Code);
                    employeeAttach.setAttachment(attachment);

                    SbtResponse sbtResponse = sheBaoTong.orderAdditional(employeeAttach);
                    LOGGER.info("提交资料审核接口返回：{}", sbtResponse);

                    JSONObject resultObj = JSON.parseObject(sbtResponse.getData(), JSONObject.class);
                    Integer result = resultObj.getInteger("result");
                    if(new Integer(1).equals(result)) {//提交成功
                        CustomersBean updateBean = new CustomersBean();
                        updateBean.setCustomerId(customersBean.getCustomerId());
                        updateBean.setCustomerSocialApproveState(2);//
                        customersWriterMapper.updateByPrimaryKeySelective(updateBean);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    LOGGER.error("员工社保资料审核异常", e);
                }

            }
        }

    }

}
