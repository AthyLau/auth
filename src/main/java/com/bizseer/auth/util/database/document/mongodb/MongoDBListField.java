package com.bizseer.auth.util.database.document.mongodb;

import com.bizseer.auth.util.database.document.DocumentDBListField;

public class MongoDBListField extends DocumentDBListField {
    public MongoDBListField(String field, String elemName, String subField) {
        super(field, elemName, subField);
    }

    public MongoDBListField(String elemName, String subField) {
        super(elemName, subField);
    }

    @Override
    protected String asUpdateString() {
        return String.format("%s.$[%s].%s", getField(), getElemName(), getSubField());
    }

    @Override
    protected String asFilterString() {
        return String.format("%s.%s", getElemName(), getSubField());
    }
}
