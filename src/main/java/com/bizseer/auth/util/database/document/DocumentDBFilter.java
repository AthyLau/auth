package com.bizseer.auth.util.database.document;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public interface DocumentDBFilter {
    enum Operator {
        /**
         * 比较运算
         */
        EQUAL,
        NOT_EQUAL,
        GREATER,
        GREATER_EQUAL,
        LESS,
        LESS_EQUAL,
        IN,
        NOT_IN,

        /**
         * 元素运算
         */
        EXIST,

        /**
         * 估值运算
         */
        REGEX,

        /**
         * 数组查询
         */
        ELEM_MATCH
    }

    interface NonFilter extends DocumentDBFilter {

    }

    @Getter
    abstract class SingleFilter implements DocumentDBFilter {
        private String key;
        private HashMap<Operator, Object> comparators = new HashMap<>(8);

        public SingleFilter(String key) {
            this.key = key;
        }

        private SingleFilter addComparators(Operator op, Object value) {
            comparators.put(op, value);
            return this;
        }

        public SingleFilter eq(Object value) {
            return addComparators(Operator.EQUAL, value);
        }

        public SingleFilter ne(Object value) {
            return addComparators(Operator.NOT_EQUAL, value);
        }

        public SingleFilter gt(Object value) {
            return addComparators(Operator.GREATER, value);
        }

        public SingleFilter gte(Object value) {
            return addComparators(Operator.GREATER_EQUAL, value);
        }

        public SingleFilter lt(Object value) {
            return addComparators(Operator.LESS, value);
        }

        public SingleFilter lte(Object value) {
            return addComparators(Operator.LESS_EQUAL, value);
        }

        public SingleFilter in(List value) {
            return addComparators(Operator.IN, value);
        }

        public SingleFilter in(Object[] values) {
            if (values != null) {
                return in(Arrays.asList(values));
            }
            return this;
        }

        public SingleFilter nin(List value) {
            return addComparators(Operator.NOT_IN, value);
        }

        public SingleFilter regex(Object value) {
            return addComparators(Operator.REGEX, value);
        }

        public SingleFilter exist() {
            return addComparators(Operator.EXIST, true);
        }

        public SingleFilter notExist() {
            return addComparators(Operator.EXIST, false);
        }

        public SingleFilter elemMatch(DocumentDBFilter filter) {
            return addComparators(Operator.ELEM_MATCH, filter);
        }
    }

    @Getter
    abstract class AndFilter implements DocumentDBFilter {
        private List<DocumentDBFilter> filters = new ArrayList<>();

        public AndFilter with(DocumentDBFilter filter) {
            filters.add(filter);
            return this;
        }

        public AndFilter with(DocumentDBFilter... filters) {
            return with(Arrays.asList(filters));
        }

        public AndFilter with(List<DocumentDBFilter> filters) {
            this.filters.addAll(filters);
            return this;
        }
    }

    @Getter
    abstract class OrFilter implements DocumentDBFilter {
        private List<DocumentDBFilter> filters = new ArrayList<>();

        public OrFilter with(DocumentDBFilter filter) {
            filters.add(filter);
            return this;
        }

        public OrFilter with(DocumentDBFilter... filters) {
            return with(Arrays.asList(filters));
        }

        public OrFilter with(List<DocumentDBFilter> filters) {
            this.filters.addAll(filters);
            return this;
        }
    }
}
