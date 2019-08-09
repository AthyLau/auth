package com.bizseer.auth.util;

public interface Connector extends AutoCloseable {
    boolean init();
    void close();
    default boolean refresh() {
        close();
        return init();
    }
}
