package com.bizseer.auth.util.database.document;

import java.util.*;

public abstract class DocumentDBAggregator {
    protected Set<String> excludeKeys;
    protected Set<String> includeKeys;
    protected List<String> descKeys;
    protected List<String> ascKeys;
    protected Integer limit;
    protected Integer skip;

    public DocumentDBAggregator() {
        excludeKeys = new HashSet<>();
        includeKeys = new HashSet<>();
        descKeys = new ArrayList<>();
        ascKeys = new ArrayList<>();
        limit = null;
        skip = null;
    }

    public DocumentDBAggregator exclude(String... keys) {
        return exclude(Arrays.asList(keys));
    }

    public DocumentDBAggregator exclude(List<String> keys) {
        excludeKeys.addAll(keys);
        return this;
    }

    public DocumentDBAggregator excludeIf(boolean predicate, String... keys) {
        if (predicate) {
            return exclude(keys);
        }
        return this;
    }

    public DocumentDBAggregator excludeIf(boolean predicate, List<String> keys) {
        if (predicate) {
            return exclude(keys);
        }
        return this;
    }

    public DocumentDBAggregator include(String... keys) {
        return include(Arrays.asList(keys));
    }

    public DocumentDBAggregator include(List<String> keys) {
        includeKeys.addAll(keys);
        return this;
    }

    public DocumentDBAggregator includeIf(boolean predicate, String... keys) {
        if (predicate) {
            return include(keys);
        }
        return this;
    }

    public DocumentDBAggregator includeIf(boolean predicate, List<String> keys) {
        if (predicate) {
            return include(keys);
        }
        return this;
    }

    public DocumentDBAggregator limit(int limit) {
        this.limit = limit;
        return this;
    }

    public DocumentDBAggregator skip(Integer skip) {
        this.skip = skip;
        return this;
    }

    public DocumentDBAggregator sortByDesc(List<String> keys) {
        descKeys.addAll(keys);
        return this;
    }

    public DocumentDBAggregator sortByDesc(String... keys) {
        return sortByDesc(Arrays.asList(keys));
    }

    public DocumentDBAggregator sortByAsc(List<String> keys) {
        ascKeys.addAll(keys);
        return this;
    }

    public DocumentDBAggregator sortByAsc(String... keys) {
        return sortByAsc(Arrays.asList(keys));
    }
}
