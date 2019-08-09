package com.bizseer.auth.util.database.document;

import com.bizseer.auth.config.ConfigHelper;
import com.bizseer.auth.util.database.document.mongodb.MongoDBConnector;
import com.bizseer.auth.util.database.document.mongodb.MongoDBQueryBuilderImplement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Repository("DocumentDBHelper")
public class DocumentDBHelper {
    private static final String TYPE_MONGO = "mongo";

    @Value("${database.document.type}")
    String type;

    private volatile DocumentDBConnector documentDB;

    public DocumentDBConnector getDocDB() {
        return documentDB;
    }

    @SuppressWarnings("unused")
    @PostConstruct
    private void init() {
        if (TYPE_MONGO.equals(type)) {
            DocumentDBQueryBuilder.setImplement(new MongoDBQueryBuilderImplement());
            documentDB = new MongoDBConnector(ConfigHelper.getConfig().mongoUri, ConfigHelper.getConfig().mongoDB);
            documentDB.init();
        }
    }

    @SuppressWarnings("unused")
    @PreDestroy
    private void close() {
        documentDB.close();
    }
}
