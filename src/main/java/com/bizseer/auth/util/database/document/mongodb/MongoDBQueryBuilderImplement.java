package com.bizseer.auth.util.database.document.mongodb;

import com.bizseer.auth.util.database.document.*;

public class MongoDBQueryBuilderImplement implements DocumentDBQueryBuilderImplement {
    private static final MongoDBFilter.NonFilter ALL = new MongoDBFilter.NonFilter();

    @Override
    public DocumentDBFilter.AndFilter filter(DocumentDBFilter... filters) {
        return new MongoDBFilter.AndFilter().with(filters);
    }

    @Override
    public DocumentDBFilter.NonFilter all() {
        return ALL;
    }

    @Override
    public DocumentDBFilter.AndFilter and(DocumentDBFilter... parameters) {
        return new MongoDBFilter.AndFilter().with(parameters);
    }

    @Override
    public DocumentDBFilter.OrFilter or(DocumentDBFilter... parameters) {
        return new MongoDBFilter.OrFilter().with(parameters);
    }

    @Override
    public DocumentDBFilter.SingleFilter term(String key) {
        return new MongoDBFilter.SingleFilter(key);
    }

    @Override
    public String listField(String field, String elemName, String subField) {
        return new MongoDBListField(field, elemName, subField).toString();
    }

    @Override
    public String listField(String elemName, String subField) {
        return new MongoDBListField(elemName, subField).toString();
    }

    @Override
    public DocumentDBAggregator aggregate() {
        return new MongoDBAggregator();
    }

    @Override
    public DocumentDBAggregator nonAggregate() {
        return new MongoDBAggregator();
    }

    @Override
    public DocumentDBUpdater update() {
        return new MongoDBUpdater();
    }
}
