package com.bizseer.auth.util.database.document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class DocumentDBUpdater {
    protected enum Operator {
        /**
         * 更新操作
         */
        INC,
        MIN,
        MAX,
        MUL,
        RENAME,
        SET,
        UNSET,
        ADD_EACH_TO_SET,
        ADD_TO_SET,
        POP_FIRST,
        POP_LAST,
        PUSH,
        PULL,
        PULL_BY_FILTER,
        PULL_ALL
    }

    protected static class UpdateItem {
        public String key;
        public Operator op;
        public Object value;
        public List<Object> listValue;

        UpdateItem(String key, Operator op) {
            this.key = key;
            this.op = op;
        }

        UpdateItem(String key, Operator op, Object value) {
            this.key = key;
            this.op = op;
            this.value = value;
        }

        UpdateItem(String key, Operator op, Number value) {
            this.key = key;
            this.op = op;
            this.value = value;
        }

        UpdateItem(String key, Operator op, String value) {
            this.key = key;
            this.op = op;
            this.value = value;
        }

        UpdateItem(String key, Operator op, List<Object> value) {
            this.key = key;
            this.op = op;
            this.value = value;
        }

        UpdateItem(String key, Operator op, DocumentDBFilter filter) {
            this.key = key;
            this.op = op;
            this.value = filter;
        }
    }

    protected List<UpdateItem> updateList = new ArrayList<>();
    private boolean upsert = false;
    protected List<DocumentDBFilter> arrayFilters = null;

    public boolean isUpsert() {
        return upsert;
    }

    public DocumentDBUpdater upsert() {
        upsert = true;
        return this;
    }

    public DocumentDBUpdater arrayFilter(DocumentDBFilter... arrayFilters) {
        this.arrayFilters = Arrays.asList(arrayFilters);
        return this;
    }

    public DocumentDBUpdater inc(String key, Number value) {
        updateList.add(new UpdateItem(key, Operator.INC, value));
        return this;
    }

    public DocumentDBUpdater min(String key, Object value) {
        updateList.add(new UpdateItem(key, Operator.MIN, value));
        return this;
    }

    public DocumentDBUpdater max(String key, Object value) {
        updateList.add(new UpdateItem(key, Operator.MAX, value));
        return this;
    }

    public DocumentDBUpdater mul(String key, Number value) {
        updateList.add(new UpdateItem(key, Operator.MUL, value));
        return this;
    }

    public DocumentDBUpdater rename(String key, String newKey) {
        updateList.add(new UpdateItem(key, Operator.RENAME, newKey));
        return this;
    }

    public DocumentDBUpdater set(String key, Object value) {
        updateList.add(new UpdateItem(key, Operator.SET, value));
        return this;
    }

    public DocumentDBUpdater setEach(Map<String, Object> values) {
        values.forEach((key, value) -> {
            if (value != null) {
                updateList.add(new UpdateItem(key, Operator.SET, value));
            }
        });
        return this;
    }

    public DocumentDBUpdater unset(String key) {
        updateList.add(new UpdateItem(key, Operator.UNSET));
        return this;
    }

    public DocumentDBUpdater addEachToSet(String key, List<Object> value) {
        updateList.add(new UpdateItem(key, Operator.ADD_EACH_TO_SET, value));
        return this;
    }

    public DocumentDBUpdater addToSet(String key, Object value) {
        updateList.add(new UpdateItem(key, Operator.ADD_TO_SET, value));
        return this;
    }

    public DocumentDBUpdater popFirst(String key) {
        updateList.add(new UpdateItem(key, Operator.POP_LAST));
        return this;
    }

    public DocumentDBUpdater popLast(String key) {
        updateList.add(new UpdateItem(key, Operator.POP_LAST));
        return this;
    }

    public DocumentDBUpdater push(String key, Object value) {
        updateList.add(new UpdateItem(key, Operator.PUSH, value));
        return this;
    }

    public DocumentDBUpdater pull(String key, Object value) {
        updateList.add(new UpdateItem(key, Operator.PULL, value));
        return this;
    }

    public DocumentDBUpdater pull(String key, DocumentDBFilter filter) {
        updateList.add(new UpdateItem(key, Operator.PULL_BY_FILTER, filter));
        return this;
    }

    public DocumentDBUpdater pullAll(String key, List<Object> value) {
        updateList.add(new UpdateItem(key, Operator.PULL_ALL, value));
        return this;
    }
}
