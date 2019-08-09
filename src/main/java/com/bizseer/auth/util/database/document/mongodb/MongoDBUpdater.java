package com.bizseer.auth.util.database.document.mongodb;

import com.bizseer.auth.util.database.document.DocumentDBUpdater;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MongoDBUpdater extends DocumentDBUpdater {
    Bson makeDocument() {
        return Updates.combine(updateList.stream().map(item -> {
            switch (item.op) {
                case INC: return Updates.inc(item.key, (Number) item.value);
                case MIN: return Updates.min(item.key, item.value);
                case MAX: return Updates.max(item.key, item.value);
                case MUL: return Updates.mul(item.key, (Number) item.value);
                case RENAME: return Updates.rename(item.key, (String) item.value);
                case SET: return Updates.set(item.key, item.value);
                case UNSET: return Updates.unset(item.key);
                case ADD_EACH_TO_SET: return Updates.addEachToSet(item.key, (List<? extends Object>) item.value);
                case ADD_TO_SET: return Updates.addToSet(item.key, item.value);
                case POP_FIRST: return Updates.popFirst(item.key);
                case POP_LAST: return Updates.popLast(item.key);
                case PUSH: return Updates.push(item.key, item.value);
                case PULL: return Updates.pull(item.key, item.value);
                case PULL_ALL: return Updates.pullAll(item.key, (List<? extends Object>) item.value);
                case PULL_BY_FILTER:
                default: {
                    MongoDBFilter filter = (MongoDBFilter) item.value;
                    Document document = filter.makeDocument();
                    return Updates.pullByFilter(new Document(item.key, document));
                }
            }
        }).collect(Collectors.toList()));
    }

    List<Bson> makeArrayFilters() {
        if (arrayFilters == null || arrayFilters.isEmpty()) {
            return Collections.emptyList();
        }
        return arrayFilters.stream().map(filter -> (MongoDBFilter) filter)
            .map(MongoDBFilter::makeDocument).collect(Collectors.toList());
    }
}
