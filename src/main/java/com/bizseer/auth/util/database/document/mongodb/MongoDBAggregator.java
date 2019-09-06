package com.bizseer.auth.util.database.document.mongodb;

import com.bizseer.auth.util.database.document.DocumentDBAggregator;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class MongoDBAggregator extends DocumentDBAggregator {
    public MongoDBAggregator() {
        super();
    }

    FindIterable<Document> apply(FindIterable<Document> iterable) {
        includeKeys.removeIf(excludeKeys::contains);

        // projection
        if (!includeKeys.isEmpty()) {
            iterable = iterable.projection(Projections.include(new ArrayList<>(includeKeys)));
        } else if (!excludeKeys.isEmpty()){
            iterable = iterable.projection(Projections.exclude(new ArrayList<>(excludeKeys)));
        }

        // sort
        List<Bson> order = new ArrayList<>();
        if (!ascKeys.isEmpty()) {
            order.add(Sorts.ascending(ascKeys));
        }
        if (!descKeys.isEmpty()) {
            order.add(Sorts.descending(descKeys));
        }
        if (!order.isEmpty()) {
            iterable = iterable.sort(Sorts.orderBy(order));
        }

        // skip
        if (skip != null) {
            iterable = iterable.skip(skip);
        }

        // limit
        if (limit != null) {
            iterable = iterable.limit(limit);
        }

        return iterable;
    }
}
