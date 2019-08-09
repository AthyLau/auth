package com.bizseer.auth.util.database.document.mongodb;

import com.bizseer.auth.util.database.document.*;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.elasticsearch.common.Strings;

import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Updates.combine;

/**
 * @author flyingx
 */
@Slf4j
public class MongoDBConnector implements DocumentDBConnector {
    private static final String SERVER_STATUS_QUERY = "serverStatus";
    private static final String DB_STATUS_QUERY = "dbStatus";
    private static final int STATUS_PARAM = 1;
    private static final String DEFAULT_ID_FIELD = "_id";

    private static final String TCMALLOC = "tcmalloc";
    private static final String GENERIC = "generic";
    private static final String CURRENT_ALLOCATED_BYTES = "current_allocated_bytes";
    private static final String MEMORY_USED = "memoryUsed";
    private static final String NONE = "N/A";
    private static final String MB = " MB";

    private static final String CONNECTIONS = "connections";
    private static final String CURRENT = "current";
    private static final String CURRENT_NUM = "currentNum";
    private static final String DATA_SIZE = "dataSize";

    private static final String STORAGE_SIZE = "storageSize";

    private String uri;
    private String db;
    private volatile MongoClient mongoClient;

    public MongoDBConnector(String uri, String db) {
        this.uri = (Strings.isNullOrEmpty(uri)) ? "mongodb://aiops:aiops@127.0.0.1:27017" : uri;
        this.db = (Strings.isNullOrEmpty(db)) ? "aiops" : db;
    }

    @Override
    public synchronized boolean init() {
        if (mongoClient == null) {
            try {
                mongoClient = new MongoClient(new MongoClientURI(uri, MongoClientOptions.builder().socketTimeout(60000)));
                return true;
            } catch (Exception e) {
                log.error("Failed to init mongo device, reason: {}", e.getMessage());
            }
        }
        return false;
    }

    @Override
    public synchronized void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }

    public boolean refresh(String uri, String db) {
        if (db != null && !db.equals(this.db)) {
            this.db = db;
        }
        if (uri != null && !uri.equals(this.uri)) {
            this.uri = uri;
            log.debug("Try to refresh mongodb driver to: database-{}, uri-{}", this.db, this.uri);
            return refresh();
        }
        return false;
    }

    @Override
    public Map<String, Object> getDbStatus() {
        Map<String, Object> result = new HashMap<>(4);

        Document serveStatus = mongoClient.getDatabase(db).runCommand(new Document(SERVER_STATUS_QUERY, STATUS_PARAM));
        Document dbStatus = mongoClient.getDatabase(db).runCommand(new Document(DB_STATUS_QUERY, 1));

        try {
            Document tcMalloc = (Document) serveStatus.get(TCMALLOC);
            Document generic = (Document) tcMalloc.get(GENERIC);
            int currentAllocatedBytes = generic.getInteger(CURRENT_ALLOCATED_BYTES);
            result.put(MEMORY_USED, currentAllocatedBytes / (1024 * 1024) + MB);
        } catch (Exception e) {
            result.put(MEMORY_USED, NONE + MB);
        }

        try {
            Document connections = (Document) serveStatus.get(CONNECTIONS);
            int currentNum = connections.getInteger(CURRENT);
            result.put(CURRENT_NUM, Integer.toString(currentNum));
        } catch (Exception e) {
            result.put(CURRENT_NUM, NONE);
        }

        try {
            double storageSize = dbStatus.getDouble(DATA_SIZE);
            result.put(STORAGE_SIZE, Math.ceil(storageSize / (1024 * 1024)) + MB);
        } catch (Exception e) {
            result.put(STORAGE_SIZE, NONE + MB);
        }

        return result;
    }

    private MongoCollection<Document> collection(String tableName) {
        return mongoClient.getDatabase(db).getCollection(tableName);
    }

    @Override
    public void insert(String tableName, Map<String, Object> object) {
        collection(tableName).insertOne(new Document(object));
    }

    @Override
    public void insert(String tableName, List<Map<String, Object>> insertList) {
        collection(tableName).insertMany(insertList.stream().map(Document::new).collect(Collectors.toList()));
    }

    @Override
    public Map<String, Object> findOne(String tableName, DocumentDBFilter filter, DocumentDBAggregator aggregator) {
        MongoDBFilter mongoDBFilter = (MongoDBFilter) filter;
        MongoDBAggregator mongoDBAggregator = (MongoDBAggregator) aggregator;

        return prepareResult(mongoDBAggregator.apply(collection(tableName).find(mongoDBFilter.makeDocument())).first());
    }

    @Override
    public List<Map<String, Object>> find(String tableName, DocumentDBFilter filter, DocumentDBAggregator aggregator) {
        MongoCursor<Document> iterator = findImpl(tableName, filter, aggregator).iterator();
        List<Map<String, Object>> result = new ArrayList<>();
        while (iterator.hasNext()) {
            result.add(prepareResult(iterator.next()));
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> findAll(String tableName) {
        MongoCursor<Document> documents = collection(tableName).find().iterator();
        List<Map<String, Object>> result = new ArrayList<>();
        while (documents.hasNext()) {
            result.add(prepareResult(documents.next()));
        }
        return result;
    }

    private FindIterable<Document> findImpl(String tableName, DocumentDBFilter filter, DocumentDBAggregator aggregator) {
        MongoDBFilter mongoDBFilter = (MongoDBFilter) filter;
        MongoDBAggregator mongoDBAggregator = (MongoDBAggregator) aggregator;
        return mongoDBAggregator.apply(collection(tableName).find(mongoDBFilter.makeDocument()));
    }

    private Map<String, Object> prepareResult(Map<String, Object> result) {
        if (Objects.nonNull(result)) {
            result.remove(DEFAULT_ID_FIELD);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> update(String tableName, DocumentDBFilter filter, DocumentDBUpdater updater) {
        MongoDBFilter mongoDBFilter = (MongoDBFilter) filter;
        MongoDBUpdater mongoDBUpdater = (MongoDBUpdater) updater;
        collection(tableName).updateMany(mongoDBFilter.makeDocument(), combine(mongoDBUpdater.makeDocument()),
            new UpdateOptions().upsert(updater.isUpsert()).arrayFilters(mongoDBUpdater.makeArrayFilters()));
        return find(tableName, filter, DocumentDBQueryBuilder.nonAggregate());
    }

    @Override
    public Map<String, Object> updateOne(String tableName, DocumentDBFilter filter, DocumentDBUpdater updater) {
        return updateOne(tableName, filter, updater, DocumentDBQueryBuilder.nonAggregate());
    }

    @Override
    public Map<String, Object> updateOne(String tableName, DocumentDBFilter filter, DocumentDBUpdater updater, DocumentDBAggregator documentDBAggregator) {
        MongoDBFilter mongoDBFilter = (MongoDBFilter) filter;
        MongoDBUpdater mongoDBUpdater = (MongoDBUpdater) updater;
        collection(tableName).updateOne(mongoDBFilter.makeDocument(), combine(mongoDBUpdater.makeDocument()),
            new UpdateOptions().upsert(updater.isUpsert()).arrayFilters(mongoDBUpdater.makeArrayFilters()));
        return findOne(tableName, filter, documentDBAggregator);
    }

    @Override
    public long delete(String tableName, DocumentDBFilter filter) {
        MongoDBFilter mongoDBFilter = (MongoDBFilter) filter;
        return collection(tableName).deleteMany(mongoDBFilter.makeDocument()).getDeletedCount();
    }

    @Override
    public long deleteOne(String tableName, DocumentDBFilter filter) {
        MongoDBFilter mongoDBFilter = (MongoDBFilter) filter;
        return collection(tableName).deleteOne(mongoDBFilter.makeDocument()).getDeletedCount();
    }

    @Override
    public long count(String tableName, DocumentDBFilter filter) {
        MongoDBFilter mongoDBFilter = (MongoDBFilter) filter;
        return collection(tableName).countDocuments(mongoDBFilter.makeDocument());
    }

    @Override
    public boolean exist(String tableName, DocumentDBFilter filter) {
        return findOne(tableName, filter, DocumentDBQueryBuilder.nonAggregate()) != null;
    }
}
