package com.bizseer.auth.util.database.document.mongodb;

import com.bizseer.auth.util.database.document.DocumentDBFilter;
import org.bson.Document;

import java.util.Objects;
import java.util.stream.Collectors;

public interface MongoDBFilter {
    Document makeDocument();

    class NonFilter implements DocumentDBFilter.NonFilter, MongoDBFilter {
        @Override
        public Document makeDocument() {
            return new Document();
        }
    }

    class SingleFilter extends DocumentDBFilter.SingleFilter implements MongoDBFilter {
        public SingleFilter(String key) {
            super(key);
        }

        private String makeOp(DocumentDBFilter.Operator op) {
            switch (op) {
                case EQUAL: return "$eq";
                case NOT_EQUAL: return "$ne";
                case GREATER: return "$gt";
                case GREATER_EQUAL: return "$gte";
                case LESS: return "$lt";
                case LESS_EQUAL: return "$lte";
                case IN: return "$in";
                case NOT_IN: return "$nin";
                case REGEX: return "$regex";
                case EXIST: return "$exists";
                case ELEM_MATCH: return "$elemMatch";
                default: return "$eq";
            }
        }

        @Override
        public Document makeDocument() {
            Document document = new Document();

            getComparators().forEach((op, value) -> {
                if (value != null) {
                    if (value instanceof MongoDBFilter) {
                        document.put(makeOp(op), ((MongoDBFilter) value).makeDocument());
                    } else {
                        document.put(makeOp(op), value);
                    }
                }
            });

            if (document.isEmpty()) {
                return document;
            }

            return new Document(getKey(), document);
        }
    }

    class AndFilter extends DocumentDBFilter.AndFilter implements MongoDBFilter {
        @Override
        public Document makeDocument() {
            Document document = new Document();

            if (getFilters().isEmpty()) {
                return document;
            }

            document.put("$and", getFilters().stream().map(MongoDBFilter::filterToDocument)
                .filter(Objects::nonNull).collect(Collectors.toList()));
            return document;
        }
    }

    class OrFilter extends DocumentDBFilter.OrFilter implements MongoDBFilter {
        @Override
        public Document makeDocument() {
            Document document = new Document();

            if (getFilters().isEmpty()) {
                return document;
            }

            document.put("$or", getFilters().stream().map(MongoDBFilter::filterToDocument)
                .filter(Objects::nonNull).collect(Collectors.toList()));
            return document;
        }
    }

    static Document filterToDocument(DocumentDBFilter filter) {
        if (filter instanceof SingleFilter) {
            return ((SingleFilter) filter).makeDocument();
        } else if (filter instanceof AndFilter) {
            return ((AndFilter) filter).makeDocument();
        } else if (filter instanceof OrFilter) {
            return ((OrFilter) filter).makeDocument();
        } else {
            return null;
        }
    }
}
