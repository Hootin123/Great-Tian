package com.xtr.core.service.shebao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xtr.BaseTest;
import com.xtr.api.basic.ResultResponse;
import com.xtr.api.domain.customer.CustomerShebaoBean;
import com.xtr.api.domain.salary.PayCycleBean;
import com.xtr.api.dto.customer.CustomerExpenseDto;
import com.xtr.api.dto.shebao.CustomerShebaoSumDto;
import com.xtr.api.service.salary.CustomerExpenseService;
import com.xtr.api.service.shebao.CustomerShebaoOrderService;
import com.xtr.api.service.shebao.CustomerShebaoService;
import com.xtr.comm.basic.Config;
import com.xtr.comm.enums.ShebaoTypeEnum;
import com.xtr.comm.sbt.SheBaoTong;
import com.xtr.comm.sbt.api.Basic;
import com.xtr.comm.sbt.api.City;
import com.xtr.comm.sbt.api.SocialBase;
import com.xtr.comm.util.PropUtils;
import com.xtr.core.persistence.reader.customer.CustomerShebaoOrderDescReaderMapper;
import com.xtr.core.persistence.reader.customer.CustomerShebaoOrderReaderMapper;
import com.xtr.core.persistence.writer.customer.CustomerShebaoOrderDescWriterMapper;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author Xuewu
 * @Date 2016/9/13.
 */
public class ShebaoTest extends BaseTest {
//
    @Resource
    CustomerShebaoService customerShebaoService;

    private SheBaoTong sheBaoTong;

    @Before
    public void before() {
        Config config = PropUtils.load("classpath:config/common.properties");
        String access_token = config.get("sbt.access_token");
        String usercode = config.get("sbt.usercode");
        sheBaoTong = new SheBaoTong(access_token, usercode, true);
    }

//
//    @Test
//    public void testA() throws IOException {
//        Basic b = sheBaoTong.getBasic("shanghai");
//        SocialBase socialBase = customerShebaoService.getSocialBaseByBasic(ShebaoTypeEnum.SHEBAO, b, "shanghaiwuxian");
//        Map cal = customerShebaoService.calBySbt(4000, socialBase);
//        System.out.println("fffffffffffff");
//        System.out.println(cal);
//        Object base = cal.keySet().toArray()[0];
//        List list = (List) cal.get(base);
//        for (int i = 0; i < list.size(); i++) {
//            Map map = (Map) list.get(i);
//            System.out.print(map.get("name") + "-" + map.get("empprop"));
//            System.out.println();
//        }
////        System.out.println(cal);
//    }

//
//    @Test
//    public void testB() throws Exception {
//        CustomerShebaoBean customerShebaoBean = new CustomerShebaoBean();
//        customerShebaoBean.setPageIndex(1);
//        customerShebaoBean.setPageSize(10);
//        customerShebaoBean.setJoinCityCode("123");
//        customerShebaoBean.setCompanyId(456L);
//        ResultResponse resultResponse = customerShebaoService.selectPage(customerShebaoBean);
//        System.out.println(JSONObject.toJSON(resultResponse));
//
//    }

//    @Resource
//    private CustomerExpenseService customerExpenseService;

//    @Test
//    public void testC() throws Exception{
//        CustomerExpenseDto dto = new CustomerExpenseDto();
//        dto.setPayCycleId(110L);
//        dto.setCompanyId(487L);
//        customerExpenseService.selectPage(dto, new Date());
//    }
//    @Resource
//CustomerShebaoOrderDescWriterMapper customerShebaoOrderDescWriterMapper;
//    @Test
//    public void testC() throws Exception{
//        customerShebaoOrderDescWriterMapper.selectByCompanyOrderIdAndCustomerId(1L, 1L);
//    }
//
    @Test
    public void tet(){
        PayCycleBean payCycleBean = new PayCycleBean();
        payCycleBean.setId(1L);
        payCycleBean.setCompanyId(456L);
        payCycleBean.setYear("2015");
        payCycleBean.setMonth("2");
        Map<Long, CustomerShebaoSumDto> customerShebaoBase = customerShebaoOrderService.getCustomerShebaoBase(581l);
        System.out.println(customerShebaoBase);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ShebaoTest.class);


    @Test
    public void test() throws IOException {


        List<City> cities = sheBaoTong.getCities();
        int i=0;
        for (City city : cities) {
            if(!city.getCity().equals("beijing")){
                continue;
            }
            Basic basic = sheBaoTong.getBasic(city.getCity());
            System.out.println(basic);
            //社保
//            List<SocialBase> ins = basic.getIns();
//            for (SocialBase socialBase : ins) {
//                LOGGER.error(city.getPname() + ", "+city.getCname() + ", 社保, " + socialBase.getDesc() + ", " + socialBase.getMin() + ", "+ socialBase.getMax());
//            }
//
//            List<SocialBase> hf = basic.getHf();
//            //公积金
//            for (SocialBase socialBase : hf) {
//                LOGGER.error(city.getPname() + ", "+city.getCname() + ", 公积金, " + socialBase.getDesc() + ", " + socialBase.getMin() + ", "+ socialBase.getMax());
//            }
break;
        }
    }

    @Resource
    CustomerShebaoOrderService customerShebaoOrderService;
    @Test
    public void testReset(){
        SocialBase socialBase = new SocialBase();
        socialBase.setMax(10);
        socialBase.setMin(5);
        CustomerShebaoBean customerShebaoBean = new CustomerShebaoBean();
        customerShebaoBean.setId(1L);
        customerShebaoBean.setSbBase(new BigDecimal(10));
        BigDecimal bigDecimal = customerShebaoOrderService.resetBase(socialBase, customerShebaoBean, ShebaoTypeEnum.SHEBAO);
        System.out.println(bigDecimal);
    }

    @Resource
    CustomerShebaoOrderReaderMapper customerShebaoOrderReaderMapper;
    @Test
    public void ss(){
        customerShebaoOrderReaderMapper.selectDismiss(1L);
    }
//
//    @Resource
//    private CustomerShebaoOrderService customerShebaoOrderService;
//    @Test
//    public void sfsfdsf(){
//        long l = System.currentTimeMillis();
//        Map<Integer, List> orderFaildMsg = customerShebaoOrderService.getOrderFaildMsg(10351L, 215L);
//        System.out.println(System.currentTimeMillis() - l);
//        System.out.println(orderFaildMsg);
//    }


    @Resource
    private RedisTemplate redisTemplate;
    @Test
    public void clean(){
        Set keys = redisTemplate.keys("*ShebaoBase");
        System.out.println(JSON.toJSONString(keys));
        ValueOperations valueOperations = redisTemplate.opsForValue();
//        System.out.println(valueOperations.get(JSON.toJSONString(keys.toArray()[0])));
        redisTemplate.delete(keys);
        Map<Long, CustomerShebaoSumDto> customerShebaoBase = customerShebaoOrderService.getCustomerShebaoBase(580l);
        System.out.println(JSONObject.toJSON(customerShebaoBase));
    }
}
