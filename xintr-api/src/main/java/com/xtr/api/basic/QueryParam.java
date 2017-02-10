package com.xtr.api.basic;

import com.xtr.comm.jd.util.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * <p>自定义查询封装</p>
 *
 * @author 任齐
 * @createTime: 2016/8/22 13:12.
 */
public class QueryParam implements Serializable {

    private Map<String, ValueItem> whereParams = new HashMap<>();

    private String orderBy;
    private String groupBy;

    class ValueItem {
        Object first = null;
        Object second = null;
        Object[] inParams = null;

        public ValueItem(Object[] inParams) {
            this.inParams = inParams;
        }

        public ValueItem(Object value) {
            this.first = value;
        }

        public ValueItem(Object value1, Object value2) {
            this.first = value1;
            this.second = value2;
        }

        public Object getFirst() {
            return first;
        }

        public void setFirst(Object first) {
            this.first = first;
        }

        public Object getSecond() {
            return second;
        }

        public void setSecond(Object second) {
            this.second = second;
        }

        public Object[] getInParams() {
            return inParams;
        }

        public void setInParams(Object[] inParams) {
            this.inParams = inParams;
        }
    }

    public QueryParam() {
    }

    public QueryParam orderBy(String orderBy) {
        if(StringUtils.isNotBlank(orderBy)){
            this.orderBy = orderBy;
        }
        return this;
    }

    public QueryParam groupBy(String groupBy) {
        if(StringUtils.isNotBlank(orderBy)){
            this.groupBy = groupBy;
        }
        return this;
    }

    public QueryParam eq(String field, Object value) {
        if(StringUtils.isNotBlank(field) && null != value){
            whereParams.put(field + " = ", new ValueItem(value));
        }
        return this;
    }

    public QueryParam in(String field, Object... values) {
        if(StringUtils.isNotBlank(field) && null != values){
            whereParams.put(field + " in ", new ValueItem(values));
        }
        return this;
    }

    public QueryParam notEq(String field, Object value) {
        if(StringUtils.isNotBlank(field) && null != value){
            whereParams.put(field + " <> ", new ValueItem(value));
        }
        return this;
    }

    public QueryParam gt(String field, Object value) {
        if(StringUtils.isNotBlank(field) && null != value){
            whereParams.put(field + " > ", new ValueItem(value));
        }
        return this;
    }

    public QueryParam gte(String field, Object value) {
        if(StringUtils.isNotBlank(field) && null != value){
            whereParams.put(field + " >= ", new ValueItem(value));
        }
        return this;
    }

    public QueryParam lt(String field, Object value) {
        if(StringUtils.isNotBlank(field) && null != value){
            whereParams.put(field + " < ", new ValueItem(value));
        }
        return this;
    }

    public QueryParam lte(String field, Object value) {
        if(StringUtils.isNotBlank(field) && null != value){
            whereParams.put(field + " <= ", new ValueItem(value));
        }
        return this;
    }

    public QueryParam like(String field, Object value) {
        if(StringUtils.isNotBlank(field) && null != value){
            whereParams.put(field + " like ", new ValueItem(value));
        }
        return this;
    }

    public QueryParam between(String field, Object value1, Object value2) {
        if(StringUtils.isNotBlank(field) && null != value1 && null != value2){
            whereParams.put(field + " between ", new ValueItem(value1, value2));
        }
        return this;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public Map<String, ValueItem> getWhereParams() {
        return whereParams;
    }

    public void setWhereParams(Map<String, ValueItem> whereParams) {
        this.whereParams = whereParams;
    }
}
