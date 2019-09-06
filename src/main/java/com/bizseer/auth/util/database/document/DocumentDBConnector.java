package com.bizseer.auth.util.database.document;

import com.bizseer.auth.util.Connector;

import java.util.List;
import java.util.Map;

public interface DocumentDBConnector extends Connector {
    Map<String, Object> getDbStatus();

    void insert(String tableName, Map<String, Object> object);

    void insert(String tableName, List<Map<String, Object>> insertList);

    Map<String, Object> findOne(String tableName, DocumentDBFilter filter, DocumentDBAggregator aggregator);

    List<Map<String, Object>> find(String tableName, DocumentDBFilter filter, DocumentDBAggregator aggregator);

    List<Map<String, Object>> findAll(String tableName);

    List<Map<String, Object>> update(String tableName, DocumentDBFilter filter, DocumentDBUpdater updater);

    Map<String, Object> updateOne(String tableName, DocumentDBFilter filter, DocumentDBUpdater updater);

    Map<String, Object> updateOne(String tableName, DocumentDBFilter filter, DocumentDBUpdater updater, DocumentDBAggregator documentDBAggregator);

    long delete(String tableName, DocumentDBFilter filter);

    long deleteOne(String tableName, DocumentDBFilter filter);

    long count(String tableName, DocumentDBFilter filter);

    boolean exist(String tableName, DocumentDBFilter filter);
}
