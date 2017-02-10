package com.xtr.comm.sbt.api;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>社保缴交类型基数</p>
 *
 * @author 任齐
 * @createTime: 2016/9/8 14:11.
 */
public class SocialBase implements Serializable {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
    private static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM");

    private String type;
    private String name;
    private String desc;
    private String bchgmonth;
    private String obmonth;
    private String oemonth;
    private long efdate;
    private List<IncType> inc;
    private double min;
    private double max;

    private double empSum;
    private double orgSum;
    public SocialBase() {
    }

    public static SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }

    public static void setSimpleDateFormat(SimpleDateFormat simpleDateFormat) {
        SocialBase.simpleDateFormat = simpleDateFormat;
    }

    public static SimpleDateFormat getSimpleDateFormat2() {
        return simpleDateFormat2;
    }

    public static void setSimpleDateFormat2(SimpleDateFormat simpleDateFormat2) {
        SocialBase.simpleDateFormat2 = simpleDateFormat2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBchgmonth() {
        try {
            if(null != bchgmonth && bchgmonth.indexOf('-') != -1){
                return bchgmonth;
            }
            Date parse = simpleDateFormat.parse(bchgmonth);
            return simpleDateFormat2.format(parse);
        } catch (Exception e) {
            return null;
        }
    }


    public String getObmonth() {
        try {
            if(null != obmonth && obmonth.indexOf('-') != -1){
                return obmonth;
            }
            Date parse = simpleDateFormat.parse(obmonth);
            return simpleDateFormat2.format(parse);
        } catch (Exception e) {
            return null;
        }
    }

    public void setObmonth(String obmonth) {
        this.obmonth = obmonth;
    }

    public String getOemonth() {
        try {
            if(null != oemonth && oemonth.indexOf('-') != -1){
                return oemonth;
            }
            Date parse = simpleDateFormat.parse(oemonth);
            return simpleDateFormat2.format(parse);
        } catch (Exception e) {
            return null;
        }
    }

    public void setOemonth(String oemonth) {
        this.oemonth = oemonth;
    }

    public long getEfdate() {
        return efdate;
    }

    public void setEfdate(long efdate) {
        this.efdate = efdate;
    }

    public List<IncType> getInc() {
        return inc;
    }

    public void setInc(List<IncType> inc) {
        this.inc = inc;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getEmpSum() {
        return empSum;
    }

    public void setEmpSum(double empSum) {
        this.empSum = empSum;
    }

    public double getOrgSum() {
        return orgSum;
    }

    public void setOrgSum(double orgSum) {
        this.orgSum = orgSum;
    }

    public void setBchgmonth(String bchgmonth) {
        this.bchgmonth = bchgmonth;
    }

    @Override
    public String toString() {
        return "SocialBase{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", bchgmonth='" + bchgmonth + '\'' +
                ", obmonth='" + obmonth + '\'' +
                ", oemonth='" + oemonth + '\'' +
                ", efdate=" + efdate +
                ", inc=" + inc +
                ", min=" + min +
                ", max=" + max +
                ", empSum=" + empSum +
                ", orgSum=" + orgSum +
                '}';
    }

}
