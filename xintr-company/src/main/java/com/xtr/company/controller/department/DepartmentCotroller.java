package com.xtr.company.controller.department;


import com.xtr.api.basic.BaseController;
import org.springframework.stereotype.Controller;

/**
 * Created by abiao on 2016/6/21.
 */
@Controller
public class DepartmentCotroller extends BaseController {

//    private static final Logger log = LoggerFactory.getLogger(DepartmentCotroller.class);
//
//    @Resource
//    private CompanyMembersService companyMembersService;
//    @Resource
//    private CompanyDepsService companyDepsService;
//    @Resource
//    private CompanysService companysService;
//    @Resource
//    private CustomersService customersService;
//    @Resource
//    private CompanyMemberLogItemsService companyMemberLogItemsService;
//    @Resource
//    private CustomerItemsService customerItemsService;
//    @Resource
//    private CustomerJobsService customerJobsService;
//
//    /**
//     * 保存用户
//     * @param request
//     * @param response
//     * @return
//     */
//    @RequestMapping(value="saveCustomer.htm")
//    @ResponseBody
//    public Object saveCustomer(HttpServletRequest request, HttpServletResponse response){
//
//        Map<String,Object> result=new HashMap<String,Object>();
//        result.put("data", "");
//
//        try{
//
//            String memberid=request.getSession().getAttribute("memberid")==null?"":request.getSession().getAttribute("memberid").toString();
//
//            if(memberid.length()<1){
////                this.clearLoginSession(request);
//                SessionUtils.removeUser(request);
//
//                result.put("code", "1");
//                result.put("msg", "请先登录");
//                return result;
//            }
//
//            Long customerid=Long.parseLong(this.getparam(request, "customerid", "0"));
//            String customername=this.getparam(request, "customername", "");
//            String customerplacelevel=this.getparam(request, "customerplacelevel", "");
//            int customersex=Integer.parseInt(this.getparam(request, "customersex", "1"));
//            String customerphone=this.getparam(request, "customerphone", "");
//            String customerplace=this.getparam(request, "customerplace", "");
//            long customerdep=Long.parseLong(this.getparam(request, "customerdep", "-1"));
//            String customerbanknumber=this.getparam(request, "customerbanknumber", "");
//            String customerbank=this.getparam(request, "customerbank", "");
//
//
//            String customeridcard=this.getparam(request, "customeridcard", "");
//            String customerbirthday=this.getparam(request, "customerbirthday", "");
//            String customerbirthdaymonth=this.getparam(request, "customerbirthdaymonth", "");
//            String customerregistry=this.getparam(request, "customerregistry", "");
//            String customermarriage=this.getparam(request, "customermarriage", "");
//            String customerpolitical=this.getparam(request, "customerpolitical", "");
//            String customerbirthstate=this.getparam(request, "customerbirthstate", "");
//            String customerschool=this.getparam(request, "customerschool", "");
//            String customerdegree=this.getparam(request, "customerdegree", "");
//            String customermajor=this.getparam(request, "customermajor", "");
//            String customergraduatedate=this.getparam(request, "customergraduatedate", "");
//            String customerfirstjobdate=this.getparam(request, "customerfirstjobdate", "");
//            String customerjointime=this.getparam(request, "customerjointime", "");
//            String customeragreementstartdate=this.getparam(request, "customeragreementstartdate", "");
//            String customeragreementenddate=this.getparam(request, "customeragreementenddate", "");
//            String customeragreementtype=this.getparam(request, "customeragreementtype", "");
//            String customerprobation=this.getparam(request, "customerprobation", "");
//            String customeraddress=this.getparam(request, "customeraddress", "");
//            String customerregistryaddress=this.getparam(request, "customerregistryaddress", "");
//            String customercontacts=this.getparam(request, "customercontacts", "");
//            int mmqd=Integer.parseInt(this.getparam(request, "mmqd", "0"));
//
//            if(customername.length()<1){
//                result.put("code", "3");
//                result.put("msg", "请填写员工姓名");
//                return result;
//            }
//
//            if(customerphone.length()<11){
//                result.put("code", "4");
//                result.put("msg", "请填写正确的员工电话");
//                return result;
//            }
//
//            if(customerdep<0){
//                result.put("code", "5");
//                result.put("msg", "请选择部门");
//                return result;
//            }
//
//            if(customerbanknumber.length()<1){
//                result.put("code", "6");
//                result.put("msg", "请填写员工工资卡号");
//                return result;
//            }
//
//            if(customerbank.length()<1){
//                result.put("code", "7");
//                result.put("msg", "请填写员工工资卡开户行");
//                return result;
//            }
//
//            if(customeridcard.length()!=15 && customeridcard.length()!=18){
//                result.put("code", "11");
//                result.put("msg", "请填写正确的员工身份证");
//                return result;
//            }
//
//            if(customerjointime.length()<1){
//                result.put("code", "12");
//                result.put("msg", "请选择员工入职时间");
//                return result;
//            }
//
//
//            //用户信息
//            Map<String,Object> par=new HashMap<String,Object>();
//            par.put("member_id", memberid);
//
//            Map<String,Object> member=companyMembersService.find(par);
//            if(null==member){
//
//                result.put("code", "13");
//                result.put("msg", "用户信息不存在");
//                return result;
//            }
//
//            long depid=member.get("member_dep_id")==null?0:Long.parseLong(member.get("member_dep_id").toString());
//            long companyid=member.get("member_company_id")==null?0:Long.parseLong(member.get("member_company_id").toString());
//            int membersign=member.get("member_sign")==null?0:Integer.parseInt(member.get("member_sign").toString());
//            String memberpowerids=member.get("member_power_ids")==null?"":member.get("member_power_ids").toString();
//            int memberisdefault=member.get("member_isdefault")==null?0:Integer.parseInt(member.get("member_isdefault").toString());
//
//
//
//
//            if(membersign==0){
//                result.put("code", "14");
//                result.put("msg", "用户已禁用");
//                return result;
//            }
//
//            if(depid<1){
//                result.put("code", "15");
//                result.put("msg", "用户部门信息异常");
//                return result;
//            }
//
//            if(  memberpowerids.length()<1){
//                result.put("code", "16");
//                result.put("msg", "没有权限");
//                return result;
//            }
//
//            if(customerid<1){
//                if(!memberpowerids.contains(",10010015,")){
//                    result.put("code", "17");
//                    result.put("msg", "没有权限");
//                    return result;
//                }
//            }else{
//                if(!memberpowerids.contains(",10010011,")){
//                    result.put("code", "18");
//                    result.put("msg", "没有权限");
//                    return result;
//                }
//            }
//
//
//            //企业信息
//            par=new HashMap<String,Object>();
//            par.put("company_id", companyid);
//            par.put("company_sign", 1);
//
//            Map<String,Object> company=companysService.findOne(par);
//            if(null==company){
//
//                result.put("code", "19");
//                result.put("msg", "企业单位不存在");
//                return result;
//            }
//
//
//            par=new HashMap<String,Object>();
//            par.put("dep_id", depid);
//            par.put("dep_sign", 1);
//
//            List<Map<String,Object>> toplist=companyDepsService.findList(par);
//
//            if(null==toplist || toplist.size()<1){
//                result.put("code", "20");
//                result.put("msg", "用户单位信息异常");
//                return result;
//            }
//
//            par=new HashMap<String,Object>();
//            par.put("toplist", toplist);
//            par.put("sign", 1);
//
//            List<Map<String,Object>> deplist=companyDepsService.getDepTree(par);
//
//
//            //判断用户所在部门是否在
//            boolean depparflag=false;
//            boolean depflag=false;
//            for(Map<String,Object> item : deplist){
//                long did=Long.parseLong(item.get("dep_id").toString());
//
//                if(customerdep==did){
//                    depflag=true;
//                    break;
//                }
//            }
//
//            if(!depflag){
//                result.put("code", "21");
//                result.put("msg", "没有权限");
//                return result;
//            }
//
//
//
//            Map<String,Object> dp=null;
//
//            par=new HashMap<String,Object>();
//            par.put("dep_id", customerdep);
//            par.put("dep_sign", 1);
//
//            dp=companyDepsService.find(par);
//            if(null==dp){
//                result.put("code", "22");
//                result.put("msg", "员工所在部门不存在");
//                return result;
//            }
//
//            Map<String,Object> dpp=null;
//            int dptype=Integer.parseInt(dp.get("dep_type").toString());
//            if(dptype==1){
//                dpp=dp;
//            }else{
//                par=new HashMap<String,Object>();
//                par.put("parentid", dp.get("dep_parent_id"));
//                dpp=companyDepsService.getParentUnit(par);
//            }
//
//
//
//
//
//
//            par=new HashMap<String,Object>();
//            par.put("customer_phone", customerphone);
//            par.put("customer_sign", 1);
//
//            if(customerid>0)
//                par.put("not_customer_id", customerid);
//
//            Map<String,Object> customer=customersService.find(par);
//
//            if(customer!=null){
//                result.put("code", "24");
//                result.put("msg", "员工电话重复");
//                return result;
//            }
//
//
//            if(customerid<1){
//                par=new HashMap<String,Object>();
//                par.put("customer_idcard", customeridcard);
//                par.put("customer_sign", 1);
//
//                customer=customersService.find(par);
//
//                if(customer!=null){
//                    result.put("code", "25");
//                    result.put("msg", "员工身份证重复");
//                    return result;
//                }
//            }
//
//
//            if(customerid<1){
//                par=new HashMap<String,Object>();
//                par.put("customer_banknumber", customerbanknumber);
//                par.put("customer_sign", 1);
//
//                if(customerid>0)
//                    par.put("not_customer_id", customerid);
//
//                customer=customersService.find(par);
//
//                if(customer!=null){
//                    result.put("code", "26");
//                    result.put("msg", "员工工资卡重复");
//                    return result;
//                }
//            }
//
//
//
//
//            customer=null;
//
//            if(customerid>0){
//                par=new HashMap<String,Object>();
//                par.put("customer_id", customerid);
//                par.put("customer_sign", 1);
//
//                customer=customersService.find(par);
//
//                if(customer==null){
//                    result.put("code", "27");
//                    result.put("msg", "编辑员工不存在");
//                    return result;
//                }
//            }
//
//
//
//            List<Map<String,Object>> cusitems=new ArrayList<Map<String,Object>>();
//
//
//
//            Date now= StringTools.GetDate();
//
//            //添加员工
//            par=new HashMap<String,Object>();
//            par.put("customer_company_id", company.get("company_id"));
//            par.put("customer_company_name", company.get("company_name"));
//            par.put("customer_dep_parentid", dpp.get("dep_id"));
//            par.put("customer_dep_parentname", dpp.get("dep_name"));
//            par.put("customer_dep_id", dp.get("dep_id"));
//            par.put("customer_bank", customerbank);
//            par.put("customer_banknumber", customerbanknumber);
//
//            par.put("customer_dep_name", dp.get("dep_name"));
//            par.put("customer_logname", customerphone);
//
//            if(customerid<1){
//                String customerpwd=customeridcard.substring(customeridcard.length()-6);
//                customerpwd=StringTools.getMD5(customerpwd);
//                customerpwd= StringTools.getMD5(customerpwd);
//
//                par.put("customer_password", customerpwd);
//            }
//
//            par.put("customer_password_strength", mmqd);
//            par.put("customer_turename", customername);
//            par.put("customer_join_time", customerjointime);
//            par.put("customer_salary", 0);
//
//            if(customerplace.length()>0)
//                par.put("customer_place", customerplace);
//
//            par.put("customer_sex", customersex);
//
//            if(customerid<1)
//                par.put("customer_idcard", customeridcard);
//
//            par.put("customer_phone", customerphone);
//            par.put("customer_sign", 1);
//            par.put("customer_ispay", 1);
//            par.put("customer_activity", 0);
//            par.put("customer_client", 1);
//            par.put("customer_addtime", StringTools.GetDate(now, "yyyy-MM-dd HH:mm:ss"));
//
//            if(customerid<1)
//                par.put("customer_job_status", 2);
//
//            par.put("customer_addtype", 1);
//
//            if(customerplacelevel.length()>0)
//                par.put("customer_place_level", customerplacelevel);
//
//            if(customerbirthday.length()>0)
//                par.put("customer_birthday", customerbirthday);
//
//            if(customerbirthdaymonth.length()>0)
//                par.put("customer_birthday_month", customerbirthdaymonth);
//
//            if(customerregistry.length()>0)
//                par.put("customer_registry", customerregistry);
//
//            if(customermarriage.length()>0)
//                par.put("customer_marriage", customermarriage);
//
//            if(customerbirthstate.length()>0)
//                par.put("customer_birth_state", customerbirthstate);
//
//            if(customerpolitical.length()>0)
//                par.put("customer_political", customerpolitical);
//
//            if(customerschool.length()>0)
//                par.put("customer_school", customerschool);
//
//            if(customerdegree.length()>0)
//                par.put("customer_degree", customerdegree);
//
//            if(customermajor.length()>0)
//                par.put("customer_major", customermajor);
//
//            if(customergraduatedate.length()>0)
//                par.put("customer_graduate_date", customergraduatedate);
//
//            if(customerfirstjobdate.length()>0)
//                par.put("customer_first_job_date", customerfirstjobdate);
//
//            if(customeragreementstartdate.length()>0)
//                par.put("customer_agreement_startdate", customeragreementstartdate);
//
//            if(customeragreementenddate.length()>0)
//                par.put("customer_agreement_enddate", customeragreementenddate);
//
//            if(customeragreementtype.length()>0)
//                par.put("customer_agreement_type", customeragreementtype);
//
//            if(customerprobation.length()>0)
//                par.put("customer_probation", customerprobation);
//
//            if(customeraddress.length()>0)
//                par.put("customer_address", customeraddress);
//
//            if(customerregistryaddress.length()>0)
//                par.put("customer_registry_address", customerregistryaddress);
//
//            if(customercontacts.length()>0)
//                par.put("customer_contacts", customercontacts);
//
//
//
//
//            if(customerid<1){
//
//                par.put("customer_trueName_isAuthentication", 1);
//                par.put("customer_phone_isAuthentication", 1);
//
//                Random random=new Random(System.currentTimeMillis());
//                par.put("customer_promote", RandomNumber.getRandomStringByLength(random, 8));
//                customersService.addReturnId(par);
//            }else{
//
//                par.put("customer_id", customerid);
//                customersService.update(par);
//
//
//                if(customerphone.length()>0){
//                    if(!customerphone.equals(customer.get("customer_phone")==null?"":customer.get("customer_phone").toString())){
//                        if((customer.get("customer_phone")==null?"":customer.get("customer_phone").toString()).length()>0){
//                            Map<String,Object> cusitem=new HashMap<String,Object>();
//                            cusitem.put("item_customer_id", customerid);
//                            cusitem.put("item_cont", customer.get("customer_phone"));
//                            cusitem.put("item_type", 1);
//                            cusitem.put("item_sign", 1);
//                            cusitem.put("item_addtime", StringTools.GetDate(now, "yyyy-MM-dd HH:mm:ss"));
//
//                            cusitems.add(cusitem);
//                        }
//                    }
//                }
//
//				/*
//				if(customerbank.length()>0 || customerbanknumber.length()>0){
//					if(!customerbank.equals(customer.get("customer_bank")==null?"":customer.get("customer_bank").toString()) || !customerbanknumber.equals(customer.get("customer_banknumber")==null?"":customer.get("customer_banknumber").toString())){
//
//						if((customer.get("customer_bank")==null?"":customer.get("customer_bank").toString()).length()>0 || (customer.get("customer_banknumber")==null?"":customer.get("customer_banknumber").toString()).length()>0){
//
//							Map<String,Object> cusitem=new HashMap<String,Object>();
//							cusitem.put("item_customer_id", customerid);
//							cusitem.put("item_title", customer.get("customer_bank"));
//							cusitem.put("item_cont", customer.get("customer_banknumber"));
//							cusitem.put("item_type", 2);
//							cusitem.put("item_sign", 1);
//							cusitem.put("item_addtime", StringTools.GetDate(now, "yyyy-MM-dd HH:mm:ss"));
//
//							cusitems.add(cusitem);
//						}
//					}
//				}
//				*/
//            }
//
//
//            //异动记录
//            if(customerid>0){
//                long cdeppid=Long.parseLong(customer.get("customer_dep_parentid")==null?"-1":customer.get("customer_dep_parentid").toString());
//                long cdepid=Long.parseLong(customer.get("customer_dep_id")==null?"-1":customer.get("customer_dep_id").toString());
//                String cplace=customer.get("customer_place")==null?"":customer.get("customer_place").toString();
//
//
//                if(cdepid!=customerdep || !cplace.equals(customerplace)){
//                    par=new HashMap<String,Object>();
//                    par.put("job_customer_id", customerid);
//                    par.put("job_addtime", StringTools.GetDate(now, "yyyy-MM-dd HH:mm:ss"));
//                    par.put("job_type", 3);
//                    par.put("job_company_id", company.get("company_id"));
//                    par.put("job_company_name", company.get("company_name"));
//                    par.put("job_dep_parentid", cdeppid);
//                    par.put("job_dep_id", cdepid);
//                    par.put("job_dep_name", customer.get("customer_dep_name"));
//                    par.put("job_place", customer.get("customer_place"));
//                    par.put("job_dep_name_new", dp.get("dep_name"));
//                    par.put("job_place_new", customerplace);
//
//                    customerJobsService.add(par);
//                }
//
//            }
//
//
//            if(cusitems.size()>0){
//                par=new HashMap<String,Object>();
//                par.put("item_list", cusitems);
//
//                customerItemsService.addList(par);
//            }
//
//
//
//            //日志
//            String word="";
//            if(customerid<1){
//                word="添加用户";
//            }else{
//                word="修改用户";
//            }
//            String membername=request.getSession().getAttribute("membername")==null?"":request.getSession().getAttribute("membername").toString();
//            long logid=request.getSession().getAttribute("logid")==null?0:Long.parseLong(request.getSession().getAttribute("logid").toString());
//
//            Map<String,Object> logitem=new HashMap<String,Object>();
//            logitem.put("item_log_id", logid);
//            logitem.put("item_des", membername+"("+memberid+")"+word+customername+"("+customeridcard+")");
//            logitem.put("item_type", 12);
//            logitem.put("item_addtime", StringTools.GetDate(now, "yyyy-MM-dd HH:mm:ss"));
//
//            companyMemberLogItemsService.add(logitem);
//
//
//
//            result.put("code", "0");
//            result.put("msg", "ok");
//
//
//        }catch(Exception e){
//            e.printStackTrace();
//            result.put("code", "-1");
//            result.put("msg", "网络异常");
//            return result;
//        }
//
//        return result;
//    }
}
