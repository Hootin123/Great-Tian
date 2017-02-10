package com.xtr.api.dto.shebao;

import com.xtr.api.domain.company.CompanyShebaoOrderBean;
import com.xtr.api.domain.customer.CustomerShebaoBean;
import com.xtr.api.domain.customer.CustomerShebaoOrderBean;
import com.xtr.api.domain.customer.CustomerShebaoOrderDescBean;
import com.xtr.api.domain.customer.CustomersSupplementBean;
import com.xtr.comm.sbt.api.City;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by allycw3 on 2016/10/11.
 */
public class ResultSheBaoDto implements Serializable {

    //员工社保公积金基本信息
    private CustomerShebaoBean customerShebaoBean;
    //企业社保公积金订单信息
    private CompanyShebaoOrderBean companyShebaoOrderBean;
    //社保公积金相关的城市基础数据
    private City city;
    //重新计算的社保与公积金集合
    private Map cal;
    //是否显示已设置停缴信息
    private String alreadyStopInfo;
    //失败人员订单集合
    private List<CustomerShebaoOrderBean> failOrderList;
    //失败人员汇总数据集合
    private List<CustomerShebaoOrderDescBean> failDescList;
    //已设置停缴的日期的字符串形式
    private String alreadyStopDateStr;
    //补差金额
    private BigDecimal priceAddtion;
    //拼装的补收补退信息
    private List<CustomersSupplementBean> supplementList;

    public BigDecimal getPriceAddtion() {
        return priceAddtion;
    }

    public void setPriceAddtion(BigDecimal priceAddtion) {
        this.priceAddtion = priceAddtion;
    }

    public List<CustomersSupplementBean> getSupplementList() {
        return supplementList;
    }

    public void setSupplementList(List<CustomersSupplementBean> supplementList) {
        this.supplementList = supplementList;
    }

    public String getAlreadyStopDateStr() {
        return alreadyStopDateStr;
    }

    public void setAlreadyStopDateStr(String alreadyStopDateStr) {
        this.alreadyStopDateStr = alreadyStopDateStr;
    }

    public List<CustomerShebaoOrderBean> getFailOrderList() {
        return failOrderList;
    }

    public void setFailOrderList(List<CustomerShebaoOrderBean> failOrderList) {
        this.failOrderList = failOrderList;
    }

    public List<CustomerShebaoOrderDescBean> getFailDescList() {
        return failDescList;
    }

    public void setFailDescList(List<CustomerShebaoOrderDescBean> failDescList) {
        this.failDescList = failDescList;
    }

    public String getAlreadyStopInfo() {
        return alreadyStopInfo;
    }

    public void setAlreadyStopInfo(String alreadyStopInfo) {
        this.alreadyStopInfo = alreadyStopInfo;
    }

    public Map getCal() {
        return cal;
    }

    public void setCal(Map cal) {
        this.cal = cal;
    }

    public CustomerShebaoBean getCustomerShebaoBean() {
        return customerShebaoBean;
    }

    public void setCustomerShebaoBean(CustomerShebaoBean customerShebaoBean) {
        this.customerShebaoBean = customerShebaoBean;
    }

    public CompanyShebaoOrderBean getCompanyShebaoOrderBean() {
        return companyShebaoOrderBean;
    }

    public void setCompanyShebaoOrderBean(CompanyShebaoOrderBean companyShebaoOrderBean) {
        this.companyShebaoOrderBean = companyShebaoOrderBean;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
