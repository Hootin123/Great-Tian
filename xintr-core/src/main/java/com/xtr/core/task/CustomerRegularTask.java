package com.xtr.core.task;

import com.xtr.api.domain.customer.CustomersBean;
import com.xtr.api.domain.customer.CustomersRecordBean;
import com.xtr.api.domain.customer.CustomersStationBean;
import com.xtr.api.service.customer.CustomerUpdateSalaryService;
import com.xtr.api.service.customer.CustomersRecordService;
import com.xtr.api.service.customer.CustomersService;
import com.xtr.api.service.customer.CustomersStationService;
import com.xtr.comm.constant.CustomerConstants;
import com.xtr.comm.util.StringUtils;
import com.xtr.core.persistence.writer.company.CompanyDepsWriterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 员工更新转正状态
 *@author  zhangshuai
 *@date  2016/8/18.
 */
@Service("customerRegularTask")
public class CustomerRegularTask extends  BaseTask{

    private Logger LOGGER = LoggerFactory.getLogger(CustomerRegularTask.class);

    @Resource
    private CustomersStationService customersStationService;

    @Resource
    private CustomersRecordService customersRecordService;

    @Resource
    private CustomerUpdateSalaryService customerUpdateSalaryService;

    @Resource
    private CustomersService customersService;

    @Resource
    private CompanyDepsWriterMapper companyDepsWriterMapper;



     /**
     * 定时任务
     * @throws Exception
     */

    @Override
    public void run() throws Exception {

        Date currentTime = new Date();
        LOGGER.info("当前时间为:::::::"+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(currentTime)+"员工自动转正任务开始执行！！！");
        //查询状态为未转正的员工
        Map map1 = new HashMap();
        map1.put("startTimeStr",this.getPreviousDay().get(1));//前一天的开始时间
        map1.put("endTimeStr",this.getPreviousDay().get(2));//前一天的结束时间

        List<CustomersStationBean> list = customersStationService.selectByNoRegular(map1);
        boolean flag =false;
        int updateCustomerStation=0;
        CustomersStationBean cus = null;//岗位对象
        CustomersRecordBean cusr = null;//记录对象
        if(list!=null&&list.size()>0){
               for (CustomersStationBean cust:list){
                   try{
                   //如果该员工无转正时间 不操作
                   if(StringUtils.isEmpty(cust.getStationRegularTime())){
                       LOGGER.info("当前员工【" +cust.getStationCustomerId() +"】转正日期为空!!!!");
                      continue;
                   }
                    else{

                       //判断当前时间是否过了转正日期
                       flag= isRegulr(cust);
                       if(flag){
                           //更新岗位表状态
                           cus = new CustomersStationBean();
                           cus.setStationCustomerId(cust.getStationCustomerId());//员工id
                           cus.setStationCustomerState(CustomerConstants.CUSTOMER_PERSONSTATE_REGULAR);//转正
                           cus.setStationUpdateTime(new Date());//更新时间
                           updateCustomerStation = customersStationService.updateStateByCustomerId(cus);
                           if(updateCustomerStation!=1||updateCustomerStation<0){
                               break;
                           }
                           //插入员工记录表信息
                          cusr =new CustomersRecordBean();
                          cusr.setRecordCustomerId(cust.getStationCustomerId());//员工id
                          cusr.setRecordOperationType(CustomerConstants.CUSTOMER_RECORDTYPE_REGULAR);//转正
                          cusr.setRecordCreateTime(new Date());//创建时间
                          cusr.setRecordOperationTime(new Date());//操作时间
                           customersRecordService.insert(cusr);


                           //刷新转正后的工资
                           try {
                               customerUpdateSalaryService.calculateSalary(cust.getStationCustomerId());
                           }catch ( Exception e ){
                               LOGGER.info("当前转正调用定薪接口出现错误 ："+e.getMessage());
                           }


                       }
                   }


                  }catch ( Exception  e ){

                    LOGGER.error("当前该条员工信息自动转正操作出现异常："+e.getMessage());

            }

               }

        }

        LOGGER.info("当前时间为:::::::"+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())+"员工自动转正任务结束！！！");






        /*****  2、员工自动调岗开始 *****/

//        LOGGER.info("当前时间为》》》》》"+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())+"员工自动调岗任务开始执行！！！");
//
//        //查询记录表 获取都是待调岗的员工
//        Map map =  new HashMap();
//        map.put("startTimeStr",this.getPreviousDay().get(1));
//        map.put("endTimeStr",this.getPreviousDay().get(2));
//        List <CustomersRecordBean> recordBeanList = customersRecordService.selectTranferPosition(map);
//        if(recordBeanList!=null&&recordBeanList.size()>0){
//        for (CustomersRecordBean customersRecordBean :recordBeanList){
//            try{
//               //更新员工基本信息表
//                CustomersBean customer = new CustomersBean();
//                customer.setCustomerId(customersRecordBean.getRecordCustomerId());
//                customer.setCustomerDepId(customersRecordBean.getRecordDeptId());//部门id
//                customer.setCustomerPlace(customersRecordBean.getRecordStationNameAfter());//岗位
//                customer.setCustomerDepName(customersRecordBean.getRecordDeptName());//部门名称
//                //更新
//                 customersService.updateCustomersByCustomerId(customer);
//
//                //更新员工岗位表
//                CustomersStationBean cu = new CustomersStationBean();
//                cu.setStationCustomerId(customersRecordBean.getRecordCustomerId());
//                cu.setStationDeptId(customersRecordBean.getRecordDeptId());//部门id
//                cu.setStationStationName(customersRecordBean.getRecordStationNameAfter());//职位
//                cu.setStationUpdateTime(new Date());//更新时间
//                customersStationService.updateByCustomerId(cu);
//
//            }catch ( Exception e ){
//                LOGGER.error("当前该条员工信息自动调岗操作出现异常："+e.getMessage());
//            }
//
//          }
//        }
//        LOGGER.info("当前时间为》》》》》"+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())+"员工自动调岗任务执行结束！！！");

        /*****  2、员工自动调岗结束  *****/


       /*** 3、员工自动离职开始  ****/
        LOGGER.info("当前时间为==========="+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())+"员工自动离职任务开始！！！");
        //查询当前待离职的员工
        Map mapDis  = new HashMap();
        mapDis.put("startTimeStr",this.getPreviousDay().get(1));
        mapDis.put("endTimeStr",this.getPreviousDay().get(2));
        List<CustomersRecordBean>  listRecord = customersRecordService.selectDimission(mapDis);
        if(listRecord!=null&&listRecord.size()>0){
            for (CustomersRecordBean recordBean : listRecord){
                try{

                    //删除部门领导信息
                    companyDepsWriterMapper.clearLeader(recordBean.getRecordCustomerId());

                    //更新岗位表
                    CustomersStationBean cu = new CustomersStationBean();
                    cu.setStationCustomerId(recordBean.getRecordCustomerId());
                    cu.setStationCustomerState(3);//离职
                    cu.setStationDimissingTime(recordBean.getRecordDimissingTime());//离职日期
                    cu.setStationUpdateTime(new  Date());//修改日期
                    customersStationService.updateByCustomerId(cu);

                }catch(Exception e){
                    LOGGER.error("当前该条员工信息自动离职操作出现异常："+e.getMessage());
                }

            }

        }

        LOGGER.info("当前时间为=========="+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())+"员工自动离职任务结束！！！");
        /*** 3、员工自动离职结束  ****/

    }




    //当前时间与转正日期比较
    public boolean isRegulr(CustomersStationBean customersStationBean){
        boolean flag = false;
        if(System.currentTimeMillis()-customersStationBean.getStationRegularTime().getTime()>=0){
            flag=true;//到了转正日期
        } else{
            flag=false;
        }
        return flag;
    }

  public Map<Integer,String> getPreviousDay(){
      Map<Integer,String> map =  new HashMap<Integer,String>();
      Calendar calendar = Calendar.getInstance();
      //calendar.add(Calendar.DATE, -1); //得到前一天
      Date date = calendar.getTime();
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
      String startTimeStr = df.format(date)+" 00:00:00";
      String endTimeStr = df.format(date)+" 23:59:59";
      map.put(1,startTimeStr);
      map.put(2,endTimeStr);
      return map;
  }
}
