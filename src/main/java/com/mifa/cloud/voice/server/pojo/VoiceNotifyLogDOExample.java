package com.mifa.cloud.voice.server.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VoiceNotifyLogDOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Integer offset;

    public VoiceNotifyLogDOExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getOffset() {
        return offset;
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

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNotifyIsNull() {
            addCriterion("notify is null");
            return (Criteria) this;
        }

        public Criteria andNotifyIsNotNull() {
            addCriterion("notify is not null");
            return (Criteria) this;
        }

        public Criteria andNotifyEqualTo(String value) {
            addCriterion("notify =", value, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyNotEqualTo(String value) {
            addCriterion("notify <>", value, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyGreaterThan(String value) {
            addCriterion("notify >", value, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyGreaterThanOrEqualTo(String value) {
            addCriterion("notify >=", value, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyLessThan(String value) {
            addCriterion("notify <", value, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyLessThanOrEqualTo(String value) {
            addCriterion("notify <=", value, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyLike(String value) {
            addCriterion("notify like", value, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyNotLike(String value) {
            addCriterion("notify not like", value, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyIn(List<String> values) {
            addCriterion("notify in", values, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyNotIn(List<String> values) {
            addCriterion("notify not in", values, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyBetween(String value1, String value2) {
            addCriterion("notify between", value1, value2, "notify");
            return (Criteria) this;
        }

        public Criteria andNotifyNotBetween(String value1, String value2) {
            addCriterion("notify not between", value1, value2, "notify");
            return (Criteria) this;
        }

        public Criteria andDataIsNull() {
            addCriterion("data is null");
            return (Criteria) this;
        }

        public Criteria andDataIsNotNull() {
            addCriterion("data is not null");
            return (Criteria) this;
        }

        public Criteria andDataEqualTo(String value) {
            addCriterion("data =", value, "data");
            return (Criteria) this;
        }

        public Criteria andDataNotEqualTo(String value) {
            addCriterion("data <>", value, "data");
            return (Criteria) this;
        }

        public Criteria andDataGreaterThan(String value) {
            addCriterion("data >", value, "data");
            return (Criteria) this;
        }

        public Criteria andDataGreaterThanOrEqualTo(String value) {
            addCriterion("data >=", value, "data");
            return (Criteria) this;
        }

        public Criteria andDataLessThan(String value) {
            addCriterion("data <", value, "data");
            return (Criteria) this;
        }

        public Criteria andDataLessThanOrEqualTo(String value) {
            addCriterion("data <=", value, "data");
            return (Criteria) this;
        }

        public Criteria andDataLike(String value) {
            addCriterion("data like", value, "data");
            return (Criteria) this;
        }

        public Criteria andDataNotLike(String value) {
            addCriterion("data not like", value, "data");
            return (Criteria) this;
        }

        public Criteria andDataIn(List<String> values) {
            addCriterion("data in", values, "data");
            return (Criteria) this;
        }

        public Criteria andDataNotIn(List<String> values) {
            addCriterion("data not in", values, "data");
            return (Criteria) this;
        }

        public Criteria andDataBetween(String value1, String value2) {
            addCriterion("data between", value1, value2, "data");
            return (Criteria) this;
        }

        public Criteria andDataNotBetween(String value1, String value2) {
            addCriterion("data not between", value1, value2, "data");
            return (Criteria) this;
        }

        public Criteria andCalledIsNull() {
            addCriterion("called is null");
            return (Criteria) this;
        }

        public Criteria andCalledIsNotNull() {
            addCriterion("called is not null");
            return (Criteria) this;
        }

        public Criteria andCalledEqualTo(String value) {
            addCriterion("called =", value, "called");
            return (Criteria) this;
        }

        public Criteria andCalledNotEqualTo(String value) {
            addCriterion("called <>", value, "called");
            return (Criteria) this;
        }

        public Criteria andCalledGreaterThan(String value) {
            addCriterion("called >", value, "called");
            return (Criteria) this;
        }

        public Criteria andCalledGreaterThanOrEqualTo(String value) {
            addCriterion("called >=", value, "called");
            return (Criteria) this;
        }

        public Criteria andCalledLessThan(String value) {
            addCriterion("called <", value, "called");
            return (Criteria) this;
        }

        public Criteria andCalledLessThanOrEqualTo(String value) {
            addCriterion("called <=", value, "called");
            return (Criteria) this;
        }

        public Criteria andCalledLike(String value) {
            addCriterion("called like", value, "called");
            return (Criteria) this;
        }

        public Criteria andCalledNotLike(String value) {
            addCriterion("called not like", value, "called");
            return (Criteria) this;
        }

        public Criteria andCalledIn(List<String> values) {
            addCriterion("called in", values, "called");
            return (Criteria) this;
        }

        public Criteria andCalledNotIn(List<String> values) {
            addCriterion("called not in", values, "called");
            return (Criteria) this;
        }

        public Criteria andCalledBetween(String value1, String value2) {
            addCriterion("called between", value1, value2, "called");
            return (Criteria) this;
        }

        public Criteria andCalledNotBetween(String value1, String value2) {
            addCriterion("called not between", value1, value2, "called");
            return (Criteria) this;
        }

        public Criteria andCallTimeIsNull() {
            addCriterion("call_time is null");
            return (Criteria) this;
        }

        public Criteria andCallTimeIsNotNull() {
            addCriterion("call_time is not null");
            return (Criteria) this;
        }

        public Criteria andCallTimeEqualTo(Date value) {
            addCriterion("call_time =", value, "callTime");
            return (Criteria) this;
        }

        public Criteria andCallTimeNotEqualTo(Date value) {
            addCriterion("call_time <>", value, "callTime");
            return (Criteria) this;
        }

        public Criteria andCallTimeGreaterThan(Date value) {
            addCriterion("call_time >", value, "callTime");
            return (Criteria) this;
        }

        public Criteria andCallTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("call_time >=", value, "callTime");
            return (Criteria) this;
        }

        public Criteria andCallTimeLessThan(Date value) {
            addCriterion("call_time <", value, "callTime");
            return (Criteria) this;
        }

        public Criteria andCallTimeLessThanOrEqualTo(Date value) {
            addCriterion("call_time <=", value, "callTime");
            return (Criteria) this;
        }

        public Criteria andCallTimeIn(List<Date> values) {
            addCriterion("call_time in", values, "callTime");
            return (Criteria) this;
        }

        public Criteria andCallTimeNotIn(List<Date> values) {
            addCriterion("call_time not in", values, "callTime");
            return (Criteria) this;
        }

        public Criteria andCallTimeBetween(Date value1, Date value2) {
            addCriterion("call_time between", value1, value2, "callTime");
            return (Criteria) this;
        }

        public Criteria andCallTimeNotBetween(Date value1, Date value2) {
            addCriterion("call_time not between", value1, value2, "callTime");
            return (Criteria) this;
        }

        public Criteria andCallResponseIsNull() {
            addCriterion("call_response is null");
            return (Criteria) this;
        }

        public Criteria andCallResponseIsNotNull() {
            addCriterion("call_response is not null");
            return (Criteria) this;
        }

        public Criteria andCallResponseEqualTo(String value) {
            addCriterion("call_response =", value, "callResponse");
            return (Criteria) this;
        }

        public Criteria andCallResponseNotEqualTo(String value) {
            addCriterion("call_response <>", value, "callResponse");
            return (Criteria) this;
        }

        public Criteria andCallResponseGreaterThan(String value) {
            addCriterion("call_response >", value, "callResponse");
            return (Criteria) this;
        }

        public Criteria andCallResponseGreaterThanOrEqualTo(String value) {
            addCriterion("call_response >=", value, "callResponse");
            return (Criteria) this;
        }

        public Criteria andCallResponseLessThan(String value) {
            addCriterion("call_response <", value, "callResponse");
            return (Criteria) this;
        }

        public Criteria andCallResponseLessThanOrEqualTo(String value) {
            addCriterion("call_response <=", value, "callResponse");
            return (Criteria) this;
        }

        public Criteria andCallResponseLike(String value) {
            addCriterion("call_response like", value, "callResponse");
            return (Criteria) this;
        }

        public Criteria andCallResponseNotLike(String value) {
            addCriterion("call_response not like", value, "callResponse");
            return (Criteria) this;
        }

        public Criteria andCallResponseIn(List<String> values) {
            addCriterion("call_response in", values, "callResponse");
            return (Criteria) this;
        }

        public Criteria andCallResponseNotIn(List<String> values) {
            addCriterion("call_response not in", values, "callResponse");
            return (Criteria) this;
        }

        public Criteria andCallResponseBetween(String value1, String value2) {
            addCriterion("call_response between", value1, value2, "callResponse");
            return (Criteria) this;
        }

        public Criteria andCallResponseNotBetween(String value1, String value2) {
            addCriterion("call_response not between", value1, value2, "callResponse");
            return (Criteria) this;
        }
    }

    /**
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
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

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
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