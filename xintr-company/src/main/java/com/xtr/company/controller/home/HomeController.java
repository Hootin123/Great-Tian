package com.xtr.company.controller.home;

import com.xtr.api.basic.BaseController;
import com.xtr.api.domain.company.*;
import com.xtr.api.dto.company.CompanyMenuDto;
import com.xtr.api.service.company.CompanyMembersService;
import com.xtr.api.service.company.CompanyMsgsService;
import com.xtr.api.service.company.CompanyProtocolsService;
import com.xtr.api.service.company.CompanysService;
import com.xtr.comm.constant.CompanyConstant;
import com.xtr.comm.constant.CompanyProtocolConstants;
import com.xtr.comm.jd.util.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>类说明</p>
 *
 * @author 张峰 zfvip_it@163.com
 * @createTime: 2016/6/23 18:49
 */
@Controller
public class HomeController extends BaseController {

    @Resource
    private CompanysService companysService;

    @Resource
    private CompanyMembersService companyMembersService;

    @Resource
    private CompanyProtocolsService companyProtocolsService;

    @Resource
    private CompanyMsgsService companyMsgsService;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    /**
     * 主页面
     *
     * @param mav
     * @return
     */
    @RequestMapping({"home.htm"})
    public ModelAndView home(ModelAndView mav, HttpServletRequest request) {
        CompanysBean loginCompanyObj2 = getLoginCompanyObj(request);
        CompanyMembersBean loginUserObj = getLoginUserObj(request);
//        CompanyMembersBean loginUserObj = SessionUtils.getUser(request);
//        CompanysBean loginCompanyObj = SessionUtils.getCompany(request);


        CompanysBean loginCompanyObj = companysService.selectCompanyByCompanyId(loginCompanyObj2.getCompanyId());
        List<CompanyMenuBean> list = null;
        if (loginUserObj.getMemberIsdefault() == 1) {
            list = companyMembersService.selectMenuByUserId(null);
        } else {
            list = companyMembersService.selectMenuByUserId(loginUserObj.getMemberId());
        }

        //如果没有有效的协议,则我的账户菜单不显示,如果没有有效的报销管理协议,则报销管理菜单不显示
        list=assemMenuList(list,loginCompanyObj2.getCompanyId());

        if(StringUtils.isBlank(loginCompanyObj.getCompanyName()) || StringUtils.isBlank(loginCompanyObj.getCompanyAddress()) ||
                StringUtils.isBlank(loginCompanyObj.getCompanyNumber()) || StringUtils.isBlank(loginCompanyObj.getCompanyOrganizationImg()) ||
                StringUtils.isBlank(loginCompanyObj.getCompanyContactTel()) || loginCompanyObj.getCompanyChannel() == null ){
            mav.addObject("companyInfoIsComplete","0");
        }else{
            mav.addObject("companyInfoIsComplete","1");
        }

        mav.addObject("memberIsDefault",loginUserObj.getMemberIsdefault());
        mav.addObject("listCompanyMenuBean",list);
        mav.addObject("memberLogname",loginUserObj.getMemberLogname());
        mav.addObject("companyIsAuth",loginCompanyObj.getCompanyIsauth());
        mav.addObject("companyName",loginCompanyObj.getCompanyName());
        mav.addObject("auditStatus",loginCompanyObj.getCompanyAuditStatus());
        mav.addObject("companyAuditRemark",loginCompanyObj.getCompanyAuditRemark());
        mav.setViewName("xtr/home");
        return mav;
    }

    @RequestMapping({"index.htm", "", "/"})
    public ModelAndView index(ModelAndView mav) {
       // mav.setViewName("xtr/homeIndex");
        mav.setViewName("xtr/index");
        return mav;
    }
    @RequestMapping({"about.htm"})
    public ModelAndView about(ModelAndView mav) {
        mav.setViewName("xtr/about");
        return mav;
    }

    @RequestMapping({"agree.htm"})
    public ModelAndView agree(ModelAndView mav) {
        mav.setViewName("xtr/agree");
        return mav;
    }

    @RequestMapping({"indextop.htm"})
    public ModelAndView indextop(ModelAndView mav) {
        mav.setViewName("xtr/index");
        return mav;
    }

    @RequestMapping({"hrcenter.htm"})
    public ModelAndView hrcenter(ModelAndView mav) {
        mav.setViewName("xtr/hrcenter");
        return mav;
    }

    @RequestMapping({"newsdetailpage.htm"})
    public ModelAndView newsdetailpage(ModelAndView mav,HttpServletRequest request) {
        try {
            if (org.apache.commons.lang3.StringUtils.isNumeric(request.getParameter("id"))){
                long id=Long.parseLong(request.getParameter("id"));
                Map<String,Object> par=new HashMap<String,Object>();
                par.put("news_id", id);
                par.put("news_state", 1);
                Map<String,Object> map=companysService.find(par);
                mav.addObject("news_title",map.get("news_title"));
                mav.addObject("news_keywords",map.get("news_keywords"));
                mav.addObject("news_intro",map.get("news_intro"));
                mav.setViewName("xtr/news_detail");

                companysService.stationNewsClicked(id);
            }else{
                LOGGER.error("新闻参数不正确id=" + request.getParameter("id"));
                mav.setViewName("xtr/index");
            }
        }catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
            mav.setViewName("xtr/index");
        }



        return mav;
    }

    @RequestMapping({"jedis.htm"})
    public ModelAndView jedis(ModelAndView mav) {
        try {
            String host = "redis.rds.aliyuncs.com";//控制台显示访问地址
            int port = 6379;
            Jedis jedis = new Jedis(host, port);
            //鉴权信息由用户名:密码拼接而成
            jedis.auth("ad9cb604596e4f7c:xtrRds123");//instance_id:password
            String key = "redis";
            String value = "aliyun-redis";
            //select db默认为0
            jedis.select(1);
            //set一个key
            jedis.set(key, value);
            System.out.println("Set Key " + key + " Value: " + value);
            //get 设置进去的key
            String getvalue = jedis.get(key);
            System.out.println("Get Key " + key + " ReturnValue: " + getvalue);
            jedis.quit();
            //jedis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mav.setViewName("xtr/test");
        return mav;
    }

    @RequestMapping({"beha.htm"})
    public ModelAndView beha(ModelAndView mav) {
        mav.setViewName("xtr/beha");
        return mav;
    }

    @RequestMapping({"pad.htm"})
    public ModelAndView pad(ModelAndView mav) {
        mav.setViewName("xtr/pad");
        return mav;
    }

    @RequestMapping({"social.htm"})
    public ModelAndView social(ModelAndView mav) {
        mav.setViewName("xtr/social");
        return mav;
    }

    @RequestMapping({"physical.htm"})
    public ModelAndView physical(ModelAndView mav) {
        mav.setViewName("xtr/physical");
        return mav;
    }

    /**
     * 如果没有有效的协议,则我的账户菜单不显示,如果没有有效的报销管理协议,则报销管理菜单不显示
     * @param list
     * @param companyId
     * @return
     */
    private List<CompanyMenuBean> assemMenuList(List<CompanyMenuBean> list,Long companyId){
        CompanyProtocolsBean companyProtocolsBean=new CompanyProtocolsBean();
        companyProtocolsBean.setProtocolCompanyId(companyId);
        List<CompanyProtocolsBean> protocolList=companyProtocolsService.selectUsefulProtocolsByState(companyProtocolsBean);
        if(null != protocolList && protocolList.size()>0){
            boolean bxCheckFlag=false;//判断是否有有效的报销管理协议,false代表没有
            for(CompanyProtocolsBean protocolBean:protocolList){
                if(protocolBean.getProtocolContractType()!=null && protocolBean.getProtocolContractType().intValue()== CompanyProtocolConstants.PROTOCOL_TYPE_BX){
                    bxCheckFlag=true;
                    break;
                }
            }
            if(!bxCheckFlag){
                for(CompanyMenuBean menu:list){
                    if(!com.xtr.comm.util.StringUtils.isStrNull(menu.getMenuName())
                            && CompanyConstant.COMPANYMEMBER_VISITMENU_BXMANAGER.equals(menu.getMenuName())){
                        list.remove(menu);
                        break;
                    }
                }
            }
        }else{//协议都没有,则我的账户菜单和报销管理菜单都不显示
            if(list!=null && list.size()>0){
                for(CompanyMenuBean menu:list){
                    if(!com.xtr.comm.util.StringUtils.isStrNull(menu.getMenuName())
                            && CompanyConstant.COMPANYMEMBER_VISITMENU_VALIDATENAME.equals(menu.getMenuName())){
                        list.remove(menu);
                        break;
                    }
                }
                for(CompanyMenuBean menu:list){
                    if(!com.xtr.comm.util.StringUtils.isStrNull(menu.getMenuName())
                            && CompanyConstant.COMPANYMEMBER_VISITMENU_BXMANAGER.equals(menu.getMenuName())){
                        list.remove(menu);
                        break;
                    }
                }
            }
        }
        return list;
    }


    /**
     * 如果没有有效的协议,则我的账户菜单不显示,如果没有有效的报销管理协议,则报销管理菜单不显示
     * @param list
     * @param companyId
     * @return
     */
    private List<CompanyMenuDto> assemMenuListDto (List<CompanyMenuDto> list,Long companyId){
        CompanyProtocolsBean companyProtocolsBean=new CompanyProtocolsBean();
        companyProtocolsBean.setProtocolCompanyId(companyId);
        List<CompanyProtocolsBean> protocolList=companyProtocolsService.selectUsefulProtocolsByState(companyProtocolsBean);
        if(null != protocolList && protocolList.size()>0){
            boolean bxCheckFlag=false;//判断是否有有效的报销管理协议,false代表没有
            for(CompanyProtocolsBean protocolBean:protocolList){
                if(protocolBean.getProtocolContractType()!=null && protocolBean.getProtocolContractType().intValue()== CompanyProtocolConstants.PROTOCOL_TYPE_BX){
                    bxCheckFlag=true;
                    break;
                }
            }
            if(!bxCheckFlag){
                for(CompanyMenuDto menu:list){
                    if(!com.xtr.comm.util.StringUtils.isStrNull(menu.getMenuName())
                            && CompanyConstant.COMPANYMEMBER_VISITMENU_BXMANAGER.equals(menu.getMenuName())){
                        list.remove(menu);
                        break;
                    }
                }
            }
        }else{//协议都没有,则我的账户菜单和报销管理菜单都不显示
            if(list!=null && list.size()>0){
                for(CompanyMenuDto menu:list){
                    if(!com.xtr.comm.util.StringUtils.isStrNull(menu.getMenuName())
                            && CompanyConstant.COMPANYMEMBER_VISITMENU_VALIDATENAME.equals(menu.getMenuName())){
                        list.remove(menu);
                        break;
                    }
                }
                for(CompanyMenuDto menu:list){
                    if(!com.xtr.comm.util.StringUtils.isStrNull(menu.getMenuName())
                            && CompanyConstant.COMPANYMEMBER_VISITMENU_BXMANAGER.equals(menu.getMenuName())){
                        list.remove(menu);
                        break;
                    }
                }
            }
        }
        return list;
    }
    @RequestMapping({"error500.htm"})
    public ModelAndView error500(ModelAndView mav) {
        mav.setViewName("comm/500");
        return mav;
    }

    @RequestMapping({"error404.htm"})
    public ModelAndView error404(ModelAndView mav) {
        mav.setViewName("comm/404");
        return mav;
    }

    /**
     * 跳转至新的首页
     * @param modelAndView
     * @return
     */
    @RequestMapping(value="newHome.htm")
    public ModelAndView jumpToNewHome(ModelAndView modelAndView,HttpServletRequest request){

        try{
            modelAndView.setViewName("xtr/newHome");

            CompanysBean loginCompanyObj2 = getLoginCompanyObj(request);
            CompanyMembersBean loginUserObj = getLoginUserObj(request);

            CompanysBean loginCompanyObj = companysService.selectCompanyByCompanyId(loginCompanyObj2.getCompanyId());

            List<CompanyMenuDto> listDto = null;
            //管理员权限
            if(loginUserObj.getMemberIsdefault()==1){
                listDto = companyMembersService.selectMenusByUserId(null);
            }else{
                listDto = companyMembersService.selectMenusByUserId(loginUserObj.getMemberId());
            }

            //如果没有有效的协议,则我的账户菜单不显示,如果没有有效的报销管理协议,则报销管理菜单不显示
            listDto=assemMenuListDto(listDto,loginCompanyObj2.getCompanyId());

            request.getSession().setAttribute("menuList",listDto);


            if(StringUtils.isBlank(loginCompanyObj.getCompanyName()) || StringUtils.isBlank(loginCompanyObj.getCompanyAddress()) ||
                    StringUtils.isBlank(loginCompanyObj.getCompanyNumber()) || StringUtils.isBlank(loginCompanyObj.getCompanyOrganizationImg()) ||
                    StringUtils.isBlank(loginCompanyObj.getCompanyContactTel()) || loginCompanyObj.getCompanyChannel() == null ){
                modelAndView.addObject("companyInfoIsComplete","0");
            }else{
                modelAndView.addObject("companyInfoIsComplete","1");
            }

            modelAndView.addObject("memberIsDefault",loginUserObj.getMemberIsdefault());
            modelAndView.addObject("listCompanyMenuBean",listDto);
            modelAndView.addObject("memberLogname",loginUserObj.getMemberLogname());
            //公司logo
            if(com.xtr.comm.util.StringUtils.isStrNull(loginCompanyObj.getCompanyLogo())){

                modelAndView.addObject("companyLogoStr","");
                modelAndView.addObject("companyLogo","");
            }else{

                modelAndView.addObject("companyLogoStr",loginCompanyObj.getCompanyLogo());
                modelAndView.addObject("companyLogo","http://xintairuan-img.oss-cn-hangzhou.aliyuncs.com/"+loginCompanyObj.getCompanyLogo());
            }

            Integer isauth =null;
            if(com.xtr.comm.util.StringUtils.isEmpty(loginCompanyObj.getCompanyIsauth())||loginCompanyObj.getCompanyIsauth()!=1){
                isauth=2;
            }else{
                isauth=1;
            }
            modelAndView.addObject("companyIsAuth",isauth);
            modelAndView.addObject("companyName",loginCompanyObj.getCompanyName());
            modelAndView.addObject("auditStatus",loginCompanyObj.getCompanyAuditStatus());
            modelAndView.addObject("companyAuditRemark",loginCompanyObj.getCompanyAuditRemark());

            //查询消息盒子所有的未读消息
            List<CompanyMsgsBean> notReadMsgs = companyMsgsService.selectCompanyMsgs(loginCompanyObj2.getCompanyId(),1);//未读消息列表
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
            String addTimeStr="";
            List<CompanyMsgsBean> listCom= new ArrayList<CompanyMsgsBean>();
            for (CompanyMsgsBean c:notReadMsgs){
                if(!com.xtr.comm.util.StringUtils.isEmpty(c.getMsgAddtime())){
                    addTimeStr=sm.format(c.getMsgAddtime());
                    c.setAddTimeStr(addTimeStr);
                    listCom.add(c);
                }

            }

            if(null!=notReadMsgs && notReadMsgs.size()>0){
                modelAndView.addObject("notReadMsgs",listCom);
            }

        }catch (Exception e){
            LOGGER.error("新版首页出现异常错误："+e.getMessage(),e);
        }
        return  modelAndView;
    }
}


