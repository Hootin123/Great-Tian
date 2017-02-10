package com.xtr.company.controller.home;

import com.alibaba.fastjson.JSON;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.company.CompanyMembersBean;
import com.xtr.api.domain.company.CompanyMenuVisitRecordBean;
import com.xtr.api.domain.company.CompanysBean;
import com.xtr.api.domain.company.TodoMaterBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.dto.company.CompanyAccountPropertyDto;
import com.xtr.api.dto.company.CompanyMenuDto;
import com.xtr.api.dto.company.CompanyShebaoOrderDto;
import com.xtr.api.dto.customer.CustomerShebaoOrderDto;
import com.xtr.api.dto.customer.CustomersDto;
import com.xtr.api.service.company.CompanyAccountService;
import com.xtr.api.service.company.CompanyMembersService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.salary.PayCycleService;

import com.xtr.api.service.salary.PayrollAccountService;
import com.xtr.api.service.shebao.CompanyShebaoService;
import com.xtr.comm.constant.CompanyConstant;
import com.xtr.comm.constant.CustomerConstants;
import com.xtr.comm.util.DateUtil;
import com.xtr.comm.util.StringUtils;
import com.xtr.company.util.SessionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 新主页
 * @author  zhangshuai
 * @date 2016/8/26.
 */
@Controller
public class HomePageController {
    private static final Logger logger = LoggerFactory.getLogger(HomePageController.class);

    @Resource
    private CompanyMembersService companyMembersService;
    @Resource
    private CustomersService customersService;
    @Resource
    private PayCycleService payCycleService;

    @Resource
    private CompanyAccountService companyAccountService;

    @Resource
    private CompanyShebaoService companyShebaoService;
    /**
     * 跳转到新的首页
     * @param modelAndView
     * @return
     */
    @RequestMapping(value="homePage.htm")
    public ModelAndView toHomePage(ModelAndView modelAndView, HttpServletRequest request){
       //封装参数
        /***
         * 1.获取当前登录的用户
         *
         */


        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        //判断是否第一访问
        Map<String,Object> visitMap = new HashMap<String,Object>();
        visitMap.put("type",1);//首页的类型
        visitMap.put("memberId",companyMembersBean.getMemberId());
        CompanyMenuVisitRecordBean companyMenuVisitRecordBean = companyMembersService.selectVisitRecord(visitMap);
        //页面带一个标识
        int insertFlag = 0;
        if(null==companyMenuVisitRecordBean){
            //说明之前没登陆过
            //插入一条记录
            CompanyMenuVisitRecordBean saveVisitRecord = new CompanyMenuVisitRecordBean();
            saveVisitRecord.setIp(request.getRemoteAddr());
            saveVisitRecord.setMemberId(companyMembersBean.getMemberId());
            saveVisitRecord.setCompanyId(companyMembersBean.getMemberCompanyId());
            saveVisitRecord.setType(1);//首页
            saveVisitRecord.setVisitTime(new Date());
            insertFlag = companyMembersService.saveVisitRecord(saveVisitRecord);
            if(insertFlag==1){
                //带标识去页面
                modelAndView.addObject("firstVisitHomePage","firstVisitHomePage");
            }
         }

        //将该用户名重新放session
        request.getSession().setAttribute("userNameMember",companyMembersBean.getMemberName());
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map <Integer,String> map = this.getMonthFirstDay();
        CustomersDto customersCountDto = new CustomersDto();

        //获取当前公司的计薪周期
        PayCycleBean payCycleBean = payCycleService.selectByCompanyId(companyMembersBean.getMemberCompanyId());
        //开始时间
        //结束时间
        customersCountDto.setCustomerCompanyId(companyMembersBean.getMemberCompanyId());
        customersCountDto.setStartTimeStr(map.get(1));//开始时间
        customersCountDto.setEndTimeStr(map.get(2));//结束时间

        //所有员工数量  (除了离职和删除的)
        customersCountDto.setSelState(CustomerConstants.CUSTOMER_PERSONNUM_ALL);
        long allCount =customersService.selectCustomersCountByCondition(customersCountDto);

        //获取转正员工数量
        customersCountDto.setSelState(CustomerConstants.CUSTOMER_PERSONSTATE_REGULAR);
        long regularCount =customersService.selectCustomersCountByCondition(customersCountDto);

        //获取离职员工数量
        customersCountDto.setSelState(CustomerConstants.CUSTOMER_PERSONSTATE_LEAVE);
        long dimissCount  =customersService.selectCustomersCountByCondition(customersCountDto);

        //获取该用户的所有待办事项
        List<TodoMaterBean> listTodoMater = companyMembersService.selectAllTodomaterByMemberId(companyMembersBean.getMemberId());

        modelAndView.addObject("userMemberName",request.getSession().getAttribute("userNameMember"));//用户
        modelAndView.addObject("hourStr",this.getAMOrPM());//上午 or 下午
        modelAndView.addObject("currentDayArea",this.getCurrentTimeArea());//当月的第一天及当前时间
        //获取当前员工数量  转正员工的数量   离职员工的数量
        modelAndView.addObject("allCount",allCount);
        modelAndView.addObject("regularCount",regularCount);
        modelAndView.addObject("dimissCount",dimissCount);

        if(!StringUtils.isEmpty(payCycleBean)){
            modelAndView.addObject("payCycleId",payCycleBean.getId());
        }
        modelAndView.addObject("companyId",companyMembersBean.getMemberCompanyId());//企业id
        modelAndView.addObject("list",listTodoMater);//待办事项
        modelAndView.setViewName("xtr/homePage");
        return modelAndView;
    }

    /**
     * 新增待办事项
     * @param request
     * @return
     */
    @RequestMapping(value="addTodoMater.htm")
    @ResponseBody
   public ResultResponse  addTodoMater(HttpServletRequest request,String materContent){

       ResultResponse resultResponse = new ResultResponse();
       try{
           if(StringUtils.isEmpty(materContent)){
               resultResponse.setSuccess(false);
               resultResponse.setMessage("请输入待办事项");
               return resultResponse;
           }
           materContent=new String(materContent.getBytes("iso8859-1"),"utf-8");
           CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
           TodoMaterBean materBean = new TodoMaterBean();
           materBean.setMaterMemberId(companyMembersBean.getMemberId());
           materBean.setMaterCompanyId(companyMembersBean.getMemberCompanyId());
           materBean.setMaterContent(materContent);
           materBean.setMaterCreateTime(new Date());
           materBean.setMaterUpdateTime(new Date());
           materBean.setMaterStatus(0);//未删除
           TodoMaterBean result = companyMembersService.addCompanyMemberMater(materBean);
           //logger.info(JSON.toJSONString(result));
           if(!StringUtils.isEmpty(result.getMaterId())) {
               resultResponse.setSuccess(true);
               resultResponse.setData(result.getMaterId());
               resultResponse.setMessage("操作成功");
           }else{
               resultResponse.setMessage("操作失败");}
       }catch ( Exception  e){
           logger.info("新增待办事项出现错误 ："+e.getMessage());
           resultResponse.setMessage("操作失败");
       }
       return resultResponse;
   }

    /**
     * 更新新增事项
     * @param request
     * @return
     */
    @RequestMapping(value="updateTodoMater.htm")
    @ResponseBody
  public ResultResponse updateTodoMater(HttpServletRequest request,Integer materId ,String content) throws UnsupportedEncodingException {
      ResultResponse resultResponse = new ResultResponse();
      if(StringUtils.isEmpty(materId)||StringUtils.isStrNull(content)) {
          resultResponse.setMessage("当前内容不能为空");
          return resultResponse;
         }
        content = new String(content.getBytes("iso8859-1"),"utf-8");
        TodoMaterBean todoMaterBean = new TodoMaterBean();
        todoMaterBean.setMaterId(materId);
        todoMaterBean.setMaterContent(content);
        todoMaterBean.setMaterUpdateTime(new Date());
       int updateResult = companyMembersService.updateMaterById(todoMaterBean);
        if(updateResult==1){
            resultResponse.setMessage("操作成功");
            resultResponse.setSuccess(true);
        }
        return resultResponse;
  }


    /**
     * 逻辑删除待办事项
     * @return
     */
    @RequestMapping(value="deleteMater.htm")
    @ResponseBody
    public ResultResponse deleteMater(HttpServletRequest request,long materId){
        ResultResponse resultResponse = new ResultResponse();
        try{

            if(StringUtils.isEmpty(materId)){
                resultResponse.setSuccess(false);
                resultResponse.setMessage("参数为空");
                return resultResponse;
            }

            //根据id逻辑删除更改状态
            int  deleteResult = companyMembersService.deleteMaterById(materId);
            if(deleteResult==1){
                resultResponse.setSuccess(true);
                resultResponse.setMessage("操作成功");
            }
        }catch ( Exception e){
            resultResponse.setMessage(e.getMessage());
        }

        return  resultResponse;
    }


    /**
     * 判断当前用户点击按钮是否有权限
     * @param request
     * @return
     */
    @RequestMapping(value="hasAuthority.htm")
    @ResponseBody
    public  ResultResponse  hasAuthority(HttpServletRequest request,String menu){
        //获取用户信息
        CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
        ResultResponse  resultResponse =  new ResultResponse();
        boolean flag = false;
       try{

           if(StringUtils.isEmpty(menu)){
               resultResponse.setMessage("传参错误");
               resultResponse.setSuccess(false);
               return resultResponse;
           }
//           menu = new String(menu.getBytes("iso8859-1"),"utf-8");
           //判断是否有管理员权限
           if(companyMembersBean.getMemberIsdefault()!=null && companyMembersBean.getMemberIsdefault().intValue()==1){//管理员有一切的访问权限
             flag =true;
           }else{
               long menuId=companyMembersService.selectMenuIdByMenuName(menu);
               int role = companyMembersService.selectCountForMemberVisitMenu(companyMembersBean.getMemberId(),menuId);

               if(role>0)
                   flag=true;
           }
           resultResponse.setData(flag);
           resultResponse.setSuccess(true);
           resultResponse.setMessage("操作成功");
       } catch ( Exception e ){
           resultResponse.setMessage("访问权限出现异常错误");
       }


       return resultResponse;
    }

    /**
     * 跳转到无权限页面
     * @param modelAndView
     * @return
     */
    @RequestMapping(value="toNoAuthorityPage.htm")
    public ModelAndView jumpToNoAuthorityPage(ModelAndView modelAndView){
        modelAndView.setViewName("xtr/payday/paynoinfo");
        return modelAndView;
    }



    /**
     * 判别当前时间是上午还是下午
     * @return
     */
    public String getAMOrPM(){
        GregorianCalendar ca = new GregorianCalendar();
        String str ;
        if(ca.get(GregorianCalendar.AM_PM)==0)
            str ="上午好，";
        else
          str ="下午好，";
        return str;
    }

    /**
     * 获取当月第一天 及当前时间
     * @return
     */
  public  String getCurrentTimeArea(){
      SimpleDateFormat sm = new SimpleDateFormat("MM.dd");
      Calendar c = Calendar.getInstance();
      c.add(Calendar.MONTH, 0);
      c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
      String first = sm.format(c.getTime());
      //获取当前时间
      c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
      String end = sm.format(c.getTime());
     return "（"+first+"-"+end+"）";


  };


    /**
     * 获取当前时间 跟本月第一天的map
     * @return
     */
    public Map getMonthFirstDay(){
        Map<Integer,String> map = new HashMap<Integer,String>();
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        String startTime = sm.format(c.getTime());
        //获取本月最后一天
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endTime = sm.format(c.getTime());
        map.put(1,startTime+" 00:00:00");//开始时间
        map.put(2,endTime+" 23:59:59");//结束时间
        return map;
    }

    /**
     * 新版首页
     * @param modelAndView
     * @return
     */
    @RequestMapping(value="newHomePage.htm")
    public ModelAndView  jumpToNewHomePage(ModelAndView modelAndView,HttpServletRequest request){
        modelAndView.setViewName("xtr/index/homeIndex");
        /***
         * 1.获取当前登录的用户
         *
         */

        try{
            CompanyMembersBean companyMembersBean = SessionUtils.getUser(request);
            long companyId = companyMembersBean.getMemberCompanyId();
            logger.info("参数companyId："+companyId);

            //判断是否第一访问
            Map<String,Object> visitMap = new HashMap<String,Object>();
            visitMap.put("type",1);//首页的类型
            visitMap.put("memberId",companyMembersBean.getMemberId());
            CompanyMenuVisitRecordBean companyMenuVisitRecordBean = companyMembersService.selectVisitRecord(visitMap);
            //页面带一个标识
            int insertFlag = 0;
            if(null==companyMenuVisitRecordBean){
                //说明之前没登陆过
                //插入一条记录
                CompanyMenuVisitRecordBean saveVisitRecord = new CompanyMenuVisitRecordBean();
                saveVisitRecord.setIp(request.getRemoteAddr());
                saveVisitRecord.setMemberId(companyMembersBean.getMemberId());
                saveVisitRecord.setCompanyId(companyId);
                saveVisitRecord.setType(1);//首页
                saveVisitRecord.setVisitTime(new Date());
                insertFlag = companyMembersService.saveVisitRecord(saveVisitRecord);
                if(insertFlag==1){
                    //带标识去页面
                    modelAndView.addObject("firstVisitHomePage","firstVisitHomePage");
                }
            }
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map <Integer,String> map = this.getMonthFirstDay();
            CustomersDto customersCountDto = new CustomersDto();

            //获取当前公司的计薪周期
            PayCycleBean payCycleBean = payCycleService.selectByCompanyId(companyId);
            //开始时间
            //结束时间
            customersCountDto.setCustomerCompanyId(companyId);
            customersCountDto.setStartTimeStr(map.get(1));//开始时间
            customersCountDto.setEndTimeStr(map.get(2));//结束时间

            //当前所有的在职员工数量
            customersCountDto.setSelState(CustomerConstants.CUSTOMER_PERSONNUM_ALL);
            long allCount =customersService.selectCustomersCountByCondition(customersCountDto);

            //本月入职员工数量
            customersCountDto.setSelState(CustomerConstants.CUSTOMER_PERSONSTATE_ENTER);
            long enterCount =customersService.selectCustomersCountByCondition(customersCountDto);

            //本月离职员工的数量
            customersCountDto.setSelState(CustomerConstants.CUSTOMER_PERSONSTATE_LEAVE);
            long dimissCount  =customersService.selectCustomersCountByCondition(customersCountDto);


            //获取当前员工数量  转正员工的数量   离职员工的数量
            modelAndView.addObject("allCount",allCount);
            modelAndView.addObject("enterCount",enterCount);
            modelAndView.addObject("dimissCount",dimissCount);

            if(!StringUtils.isEmpty(payCycleBean)){
                modelAndView.addObject("payCycleId",payCycleBean.getId());
            }
            modelAndView.addObject("companyId",companyId);//企业id


            //资金账户信息   1、账户余额(不包含冻结金额)   2、借款金额（不包含冻结金额）
            CompanyAccountPropertyDto companyAccountPropertyDto = companyAccountService.selectAccountInfo(companyId);
            modelAndView.addObject("cashAmount",companyAccountPropertyDto.getBalance());//账户余额
            modelAndView.addObject("uncashAmount",companyAccountPropertyDto.getBorrow());//借款金额

            //代付款账单数量
            long  noPayCount =companyShebaoService.selectUnpayOrders(companyId);
            modelAndView.addObject("noPayCount",noPayCount);

            //查询最近的地区账单关闭还剩多少天
            List<CompanyShebaoOrderDto> companyShebaoOrders = companyShebaoService.selectLastOrder(companyId);
            Map<Integer,String> diffDaysMap = new HashMap<Integer,String>();
            Date nowDate=new Date();
            if(companyShebaoOrders!=null && companyShebaoOrders.size()>0){
                for(CompanyShebaoOrderDto dto:companyShebaoOrders){
                    Date lastTime=dto.getOrderLastTime();
                    if(lastTime!=null&&(lastTime.getTime()-nowDate.getTime()>=0)){
                        String lastTimeStr= DateUtil.dateFormatter.format(lastTime);
                        String nowTimeStr=DateUtil.dateFormatter.format(nowDate);
                        int diffCount=DateUtil.getDiffDaysOfTwoDateByNegative(lastTimeStr,nowTimeStr);
                        diffCount=diffCount+1;//当天的也算
                        diffDaysMap.put(diffCount,dto.getJoinCityName());
                    }
                }
            }
            int key= getMinDiffCount(diffDaysMap);
            modelAndView.addObject("diffCount",key);
            if(null!=diffDaysMap&&diffDaysMap.size()>0)
                modelAndView.addObject("joinCityName", diffDaysMap.get(key));
            else
               modelAndView.addObject("joinCityName","");
            String customerAuth =null; //员工管理权限
            String shebaoAuth=null;   //社保权限
            String expenseAuth=null;  //报销权限
            //判断首页上的权限
            List<CompanyMenuDto> listDto = (List<CompanyMenuDto>) request.getSession().getAttribute("menuList");
            if(null!=listDto&&listDto.size()>0){
                for (CompanyMenuDto co:listDto){
                    if("员工".equals(co.getMenuName())){
                        customerAuth="has";
                    }
                    if("社保".equals(co.getMenuName())){
                        shebaoAuth="has";
                    }
                    if("报销".equals(co.getMenuName())){
                        expenseAuth="has";
                    }
                }
            }
           modelAndView.addObject("customerAuth",customerAuth);
           modelAndView.addObject("shebaoAuth",shebaoAuth);
           modelAndView.addObject("expenseAuth",expenseAuth);

        }catch (Exception e){
             logger.error("跳转至首页出现异常错误："+e.getMessage(),e);
        }
        return modelAndView;
    }

    /**
     * 返回最小的那个key值
     * @param diffDaysMap
     * @return
     * @throws Exception
     */
    private int getMinDiffCount(Map<Integer, String> diffDaysMap) throws Exception {
        if(null!=diffDaysMap&&diffDaysMap.size()>0){
        List<Integer> list = new ArrayList<Integer>();
        for(Integer a:diffDaysMap.keySet()){
            list.add(a);
        }
        Collections.sort(list);
        return list.get(0);}
        return 0;
    }

    /**
     * 首页echarts数据统计
     * @return
     */
    @RequestMapping(value="testDatas.htm")
    @ResponseBody
    public ResultResponse testData(HttpServletRequest request){
        ResultResponse resultResponse = new ResultResponse();
        try{
            CompanysBean companysBean =SessionUtils.getCompany(request);
            Long  companyId = companysBean.getCompanyId();
            //查询该年 1-12月每个月的员工数量
            Long [] peopleCounts={0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L,0L};//12个月该公司员工人数 初始化都是0
            String[]  monthArr ={"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"};//横坐标
            Double[] accountsArr ={0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00};//收入初始化

            //获取该公司  该年度每个月的员工在职人数
            int year = Calendar.getInstance().get(Calendar.YEAR);
            List<CustomersDto> list = customersService.selectEveryMonthCustomerCounts(companyId,year);
            if(list!=null&&list.size()>0){
                int month;
                for(CustomersDto customersDto:list){
                    if(!StringUtils.isStrNull(customersDto.getMonth())){
                        month=Integer.valueOf(customersDto.getMonth());
                        peopleCounts[month-1]=customersDto.getPeopleCount();
                    }
                }

            }

            //获取该公司该年度每个月的发公司总额
            List<PayCycleBean> listPay = payCycleService.selectByCompanyIdAndYear(companyId,String.valueOf(year));
            if(listPay!=null&&listPay.size()>0){
                int month;
                for (PayCycleBean payCycleBean:listPay){
                    if(!StringUtils.isStrNull(payCycleBean.getMonth())){
                        month = Integer.valueOf(payCycleBean.getMonth());
                        accountsArr[month-1]=payCycleBean.getTotalWages().doubleValue();
                    }
                }

            }
            Map<String,Object> map =new HashMap<String,Object>();
            map.put("leftYAxis",peopleCounts);
            map.put("rightYAxis",accountsArr);
            map.put("category",monthArr);
            resultResponse.setSuccess(true);
            resultResponse.setData(map);
        }catch (Exception e){
            logger.error("首页echarts数据封装出现异常错误："+e.getMessage(),e);
        }
        return resultResponse;
    }


}
