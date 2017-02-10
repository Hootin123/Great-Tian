package com.xtr.manager.controller.operate;

import com.github.miemiedev.mybatis.paginator.domain.Pager;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.api.service.company.CompanysService;
import com.xtr.manager.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by xuewu on 2016/8/9.
 */
@Controller
@RequestMapping("/operate/prepaid")
public class PrepaidController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrepaidController.class);

    @Resource
    private CompanysService companysService;


    /**
     * 垫付申请页面
     * @return
     */
    @RequestMapping("/index.htm")
    public ModelAndView index(){
        return new ModelAndView("xtr/operate/prepaid/index");
    }

    /**
     * 驳回垫付申请
     * @return
     */
    @RequestMapping("/reject.htm")
    public ModelAndView reject(){
        return new ModelAndView("xtr/operate/prepaid/reject");
    }


    /**
     * 垫付查询分页
     * @param companysBean
     * @param kw
     * @return
     */
    @RequestMapping("/page.htm")
    @ResponseBody
    public ResultResponse page(CompanysBean companysBean, String kw){
        Pager pageBounds = new Pager(companysBean.getPageIndex(), companysBean.getPageSize());
        return companysService.selectPrepaidPage(companysBean, kw);
    }

    /**
     * 驳回垫付申请
     * @param cause
     * @param cId
     * @param request
     * @return
     */
    @RequestMapping(value = "/reject.htm", method = RequestMethod.POST)
    @ResponseBody
    public ResultResponse toReject(String cause, long cId, HttpServletRequest request){
        ResultResponse rr = new ResultResponse();
        try {
            SysUserBean sysUserBean = SessionUtils.getUser(request);
            Long memberId = sysUserBean.getId();
            rr.setSuccess(companysService.auditPrepaid(cId, cause, false, memberId));
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            rr.setMessage("操作失败");
        }

        return rr;
    }


}
