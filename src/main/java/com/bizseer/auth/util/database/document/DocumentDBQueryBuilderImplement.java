package com.bizseer.auth.util.database.document;

public interface DocumentDBQueryBuilderImplement {
    DocumentDBFilter.AndFilter filter(DocumentDBFilter... parameters);
    DocumentDBFilter all();
    DocumentDBFilter.AndFilter and(DocumentDBFilter... parameters);
    DocumentDBFilter.OrFilter or(DocumentDBFilter... parameters);
    DocumentDBFilter.SingleFilter term(String key);

    String listField(String field, String elemName, String subField);
    String listField(String elemName, String subField);

    DocumentDBAggregator aggregate();
    DocumentDBAggregator nonAggregate();

    DocumentDBUpdater update();
}
