package com.bizseer.auth.util.database.document;

public class DocumentDBQueryBuilder {
    private static DocumentDBQueryBuilderImplement implement;

    public static void setImplement(DocumentDBQueryBuilderImplement implement) {
        DocumentDBQueryBuilder.implement = implement;
    }

    public static DocumentDBFilter all() {
        return implement.all();
    }

    public static DocumentDBFilter.AndFilter and(DocumentDBFilter... parameters) {
        return implement.and(parameters);
    }

    public static DocumentDBFilter.OrFilter or(DocumentDBFilter... parameters) {
        return implement.or(parameters);
    }

    public static DocumentDBFilter.SingleFilter term(String key) {
        return implement.term(key);
    }

    public static String listField(String field, String elemName, String subField) {
        return implement.listField(field, elemName, subField).toString();
    }

    public static String listField(String elemName, String subField) {
        return implement.listField(elemName, subField).toString();
    }

    public static DocumentDBAggregator aggregate() {
        return implement.aggregate();
    }

    public static DocumentDBAggregator nonAggregate() {
        return implement.nonAggregate();
    }

    public static DocumentDBUpdater update() {
        return implement.update();
    }
}
