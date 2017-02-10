package com.xtr.company.controller.home;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanyMsgsBean;
import com.xtr.api.service.company.CompanyMsgsService;
import com.xtr.company.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by xuewu on 2016/7/26.
 * 用户消息
 */
@Controller
public class MessageController {

    @Resource
    private CompanyMsgsService companyMsgsService;

    @RequestMapping("/message/all")
    @ResponseBody
    public ResultResponse home(HttpServletRequest request) {
        CompanyMembersBean user = SessionUtils.getUser(request);
        List<CompanyMsgsBean> allUnReadMsg = companyMsgsService.findAllUnReadMsg((long) user.getMemberCompanyId(), 12);

        return ResultResponse.buildSuccess(allUnReadMsg);
    }

}
