package com.xtr.company.controller.payday;

import com.xtr.api.basic.BaseController;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanyMsgsBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.service.company.CompanyMsgsService;
import com.xtr.company.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 消息盒子
 *
 * @author zhangshuai
 * @date 2016/8/10.
 */
@Controller
@RequestMapping("messageBox")
public class MessageBoxController extends BaseController {

    Logger logger = LoggerFactory.getLogger(MessageBoxController.class);
    @Resource
    private CompanyMsgsService companyMsgsService;


    /**
     * 查看所有未读的消息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "seeMessageBox.htm")
    @ResponseBody
    public ResultResponse MessageList(HttpServletRequest request) {

        ResultResponse resultResponse = new ResultResponse();

        try {
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            //根据企业ID获取企业信息
            //获取公司id
            Long companyId = companyMembersBean.getMemberCompanyId();
            List<CompanyMsgsBean> msgList = companyMsgsService.findCompanyMsgsBeanListByCompanyId(companyId);
            resultResponse.setSuccess(true);
            resultResponse.setData(msgList);
        } catch (Exception e) {
            logger.error("消息盒子查询所有未读消息：" + e.getMessage());
            resultResponse.setSuccess(false);
            resultResponse.setMessage(e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 查看消息详情
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "findMessageDetail.htm")
    @ResponseBody
    public ResultResponse findMessageDetailByMsgId(HttpServletRequest request) {

        ResultResponse resultResponse = new ResultResponse();
        try {
            Long msgId = Long.valueOf(request.getParameter("msgId"));

            //查询
            CompanyMsgsBean companyMsgsBean = companyMsgsService.selectCompanyMsgsBeanByMsgId(msgId);


            if (null != companyMsgsBean) {
                //将当前的消消息改为已读
                if(companyMsgsBean.getMsgSign()==1) {
                    int updateHasRead = companyMsgsService.updateHasRead(msgId);//标记为已读
                    logger.info("更新消息状态为已读标识："+updateHasRead);
                }
            }
            resultResponse.setData(companyMsgsBean);
            resultResponse.setSuccess(true);

        } catch (Exception e) {

            logger.error("消息盒子查询消息详情：" + e.getMessage());
            resultResponse.setSuccess(false);
            resultResponse.setMessage(e.getMessage());

        }
        return resultResponse;
    }


    /**
     * 逻辑删除当前消息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "deleteMessage.htm")
    @ResponseBody
    public ResultResponse deleteMessageByMsgId(HttpServletRequest request) {


        ResultResponse resultResponse = new ResultResponse();
        try {
            Long msgId = Long.valueOf(request.getParameter("msgId"));

            //判断当前是否存在
            CompanyMsgsBean companyMsgsBean = companyMsgsService.selectCompanyMsgsBeanByMsgId(msgId);
            if (companyMsgsBean == null) {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("该条消息不存在或已删除");
                return resultResponse;
            }
            int result = companyMsgsService.deleteCompanyMsgsBeanByMsgId(msgId);
            if (result == 1) {
                //更新成功
                resultResponse.setSuccess(true);
                resultResponse.setMessage("成功删除");
                logger.info("消息成功删除");
            } else {
                resultResponse.setSuccess(false);
                resultResponse.setMessage("消息删除出现错误");
                logger.info("消息成功删除");
            }

        } catch (Exception e) {

            logger.error("消息盒子删除消息出现异常：" + e.getMessage());
            resultResponse.setSuccess(false);
            resultResponse.setMessage(e.getMessage());
        }
        return resultResponse;
    }


    /**
     * 获取当前用户的未读消息的数量
     *
     * @return
     */
    @RequestMapping(value = "getUnReadMsgsCount.htm")
    @ResponseBody
    public ResultResponse getUnReadMsgsCount(HttpServletRequest request) {

        ResultResponse resultResponse = new ResultResponse();
        //获取当前登录用户的companyId
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        if (companyMembersBean == null || companyMembersBean.getMemberCompanyId() == null) {
            resultResponse.setData(0);//当前未读的消息数量
        } else {
            //根据公司id查询未读的消息个数
            long unReadCount = companyMsgsService.getUnReadCounts(companyMembersBean.getMemberCompanyId());

            resultResponse.setData(unReadCount);//当前未读的消息数量
        }
        resultResponse.setSuccess(true);
        return resultResponse;
    }

    /**
     * 新版消息盒子 查询所有的已读消息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "seeAllReadMsgs.htm")
    @ResponseBody
    public ResultResponse queryAllReadMsgs(HttpServletRequest request) {
        ResultResponse resultResponse = new ResultResponse();
        try {
            CompanysBean companysBean = SessionUtils.getCompany(request);
            Long companyId = companysBean.getCompanyId();

            List<CompanyMsgsBean> readMsgs = companyMsgsService.selectCompanyMsgs(companyId, 2);//查询已读消息
            resultResponse.setSuccess(true);
            resultResponse.setData(readMsgs);
        } catch (Exception e) {
            logger.error("新版消息盒子查询所有已读消息出现异常错误：" + e.getMessage(), e);
        }
        return resultResponse;
    }


}
