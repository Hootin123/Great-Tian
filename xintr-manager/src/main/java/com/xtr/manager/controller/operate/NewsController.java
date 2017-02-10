package com.xtr.manager.controller.operate;

import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.station.StationNewsBean;
import com.xtr.api.domain.sys.SysUserBean;
import com.xtr.api.service.station.StationNewsService;
import com.xtr.comm.basic.BusinessException;
import com.xtr.comm.util.DateUtil;
import com.xtr.manager.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;

/**
 * <p></p>
 *
 * @author 任齐
 * @createTime: 2016/7/18 10:10
 */
@Controller
@RequestMapping("operate/news")
public class NewsController {

    public static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);

    @Resource
    private StationNewsService stationNewsService;

    /**
     * 新闻列表页面
     *
     * @param mav
     * @return
     */
    @RequestMapping(value = "index.htm")
    public ModelAndView index(HttpServletRequest request, ModelAndView mav){
        mav.setViewName("xtr/operate/news/index");
        return mav;
    }

    @RequestMapping(value = "dataList.htm")
    @ResponseBody
    public ResultResponse dataList(@RequestParam(value = "newsType", required = false) Integer newsType,
                                   @RequestParam(value = "newsState", required = false) Integer newsState,
                                   @RequestParam(value = "newsTitle", required = false) String newsTitle,
                                   @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                                   @RequestParam(value = "pageSize", defaultValue = "6") int pageSize){

        return stationNewsService.selectPageList(newsType, newsState, newsTitle, pageIndex, pageSize);
    }

    /**
     * 发布新闻页面
     *
     * @param mav
     * @return
     */
    @RequestMapping(value = "publish.htm", method = RequestMethod.GET)
    public ModelAndView showPublish(HttpServletRequest request, ModelAndView mav){
        SysUserBean sysUserBean = SessionUtils.getUser(request);
        mav.addObject("uname", sysUserBean.getNickName());
        mav.setViewName("xtr/operate/news/publish");
        return mav;
    }

    /**
     * 发布新闻
     *
     * @param stationNewsBean
     * @return
     */
    @RequestMapping(value = "publish.htm", method = RequestMethod.POST)
    @ResponseBody
    public ResultResponse publish(HttpServletRequest request, StationNewsBean stationNewsBean, @RequestParam("publisTime") String publisTime){
        ResultResponse resultResponse = new ResultResponse();
        try {
            SysUserBean sysUserBean = SessionUtils.getUser(request);
            Long memberId = sysUserBean.getId();

            Date pdate = DateUtil.string2Date(publisTime, "yyyy-MM-dd HH:mm");

            stationNewsBean.setNewsReleasetime(pdate);
            stationNewsBean.setNewsAddMember(memberId);
            stationNewsBean.setNewsReleaseMember(memberId);
            stationNewsService.saveStationNews(stationNewsBean);
            resultResponse.setSuccess(true);
        } catch (ParseException e) {
            resultResponse.setMessage(e.getMessage());
        } catch (BusinessException e) {
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        }
        return resultResponse;
    }

    /**
     * 修改新闻页面
     *
     * @param newid
     * @param request
     * @return
     */
    @RequestMapping(value = "edit/{newid}.htm", method = RequestMethod.GET)
    public ModelAndView update(ModelAndView mav, @PathVariable("newid") Long newid,
                                 HttpServletRequest request){
        StationNewsBean stationNewsBean = stationNewsService.selecyByNewId(newid);
        mav.addObject("newsData", stationNewsBean);
        mav.setViewName("xtr/operate/news/edit");
        return mav;
    }

    /**
     * 修改新闻
     *
     * @param stationNewsBean
     * @return
     */
    @RequestMapping(value = "update.htm", method = RequestMethod.POST)
    @ResponseBody
    public ResultResponse update(HttpServletRequest request, StationNewsBean stationNewsBean, @RequestParam("publisTime") String publisTime){
        ResultResponse resultResponse = new ResultResponse();
        try {
            SysUserBean sysUserBean = SessionUtils.getUser(request);
            Long memberId = sysUserBean.getId();

            Date pdate = DateUtil.string2Date(publisTime, "yyyy-MM-dd HH:mm");

            stationNewsBean.setNewsReleasetime(pdate);
            stationNewsBean.setNewsAddMember(memberId);
            stationNewsBean.setNewsReleaseMember(memberId);
            stationNewsService.updateStationNews(stationNewsBean);
            resultResponse.setSuccess(true);
        } catch (BusinessException e) {
            resultResponse.setMessage(e.getMessage());
            LOGGER.error(e.getMessage(), e);
        } catch (ParseException e) {
            resultResponse.setMessage(e.getMessage());
        }
        return resultResponse;
    }

    /**
     * 修改发布状态
     *
     * @param newsId
     * @param state
     * @return
     */
    @RequestMapping(value = "republish.htm", method = RequestMethod.POST)
    @ResponseBody
    public ResultResponse republish(
            @RequestParam("newsId") Long newsId,
            @RequestParam("state") Integer state) {

        ResultResponse resultResponse = new ResultResponse();
        try {

            stationNewsService.republish(newsId, state);
            resultResponse.setSuccess(true);
        } catch (BusinessException e) {
            resultResponse.setMessage(e.getMessage());
            LOGGER.error("###### 修改发布状态失败 ######", e);
        }
        return resultResponse;
    }

}
