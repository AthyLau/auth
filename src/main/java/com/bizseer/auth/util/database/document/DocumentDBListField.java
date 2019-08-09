package com.bizseer.auth.util.database.document;

import lombok.Getter;

@Getter
public abstract class DocumentDBListField {
    private String field;
    private String elemName;
    private String subField;
    private boolean asFilter;

    public DocumentDBListField(String field, String elemName, String subField) {
        this.field = field;
        this.elemName = elemName;
        this.subField = subField;
        asFilter = false;
    }

    public DocumentDBListField(String elemName, String subField) {
        this.elemName = elemName;
        this.subField = subField;
        asFilter = true;
    }

    @Override
    public String toString() {
        return asFilter ? asFilterString() : asUpdateString();
    }

    protected abstract String asUpdateString();

    protected abstract String asFilterString();
}
