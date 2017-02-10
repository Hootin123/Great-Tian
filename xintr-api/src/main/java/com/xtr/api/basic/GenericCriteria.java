package com.xtr.api.basic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>MyBatis复杂条件筛选</p>
 *
 * @author 任齐
 * @createTime: 2016/7/1 17:03
 */
public class GenericCriteria implements Serializable {


    private boolean distinct;

    private List<Criteria> generic;

    public GenericCriteria() {
        generic = new ArrayList<Criteria>();
    }

    public void or(Criteria criteria) {
        generic.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        generic.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (generic.size() == 0) {
            generic.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        generic.clear();
        distinct = false;
    }

    public List<Criteria> getGeneric() {
        return generic;
    }

    public void setGeneric(List<Criteria> generic) {
        this.generic = generic;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value,
                                    String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property
                        + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1,
                                    Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property
                        + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria isNull(String property) {
            addCriterion(property + " is null");
            return (Criteria) this;
        }

        public Criteria isNotNull(String property) {
            addCriterion(property + " is not null");
            return (Criteria) this;
        }

        public Criteria equalTo(String property, Object value) {
            addCriterion(property + " =", value, property);
            return (Criteria) this;
        }

        public Criteria notEqualTo(String property, Object value) {
            addCriterion(property + " <>", value, property);
            return (Criteria) this;
        }

        public Criteria greaterThan(String property, Object value) {
            addCriterion(property + " >", value, property);
            return (Criteria) this;
        }

        public Criteria greaterThanOrEqualTo(String property, Object value) {
            addCriterion(property + " >=", value, property);
            return (Criteria) this;
        }

        public Criteria lessThan(String property, Object value) {
            addCriterion(property + " <", value, property);
            return (Criteria) this;
        }

        public Criteria lessThanOrEqualTo(String property, Object value) {
            addCriterion(property + " <=", value, property);
            return (Criteria) this;
        }

        public Criteria like(String property, Object value) {
            addCriterion(property + " like", value, property);
            return (Criteria) this;
        }

        public Criteria notLike(String property, Object value) {
            addCriterion(property + " not like", value, property);
            return (Criteria) this;
        }

        public Criteria in(String property, List<Object> values) {
            addCriterion(property + " in", values, property);
            return (Criteria) this;
        }

        public Criteria notIn(String property, List<Object> values) {
            addCriterion(property + " not in", values, property);
            return (Criteria) this;
        }

        public Criteria between(String property, Object value1, Object value2) {
            addCriterion(property + " between", value1, value2, property);
            return (Criteria) this;
        }

        public Criteria notBetween(String property, Object value1, Object value2) {
            addCriterion(property + " between", value1, value2, property);
            return (Criteria) this;
        }

    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }

        public Criteria likeInsensitive(String property, String value) {
            addCriterion("upper(" + property + ") like", value.toUpperCase(),
                    property);
            return this;
        }

        public Criteria findInSet(String property, String value) {
            addCriterion("find_in_set(" + value + ", " + property + ") and 1 =", "1", property);
            return this;
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue,
                            String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}
