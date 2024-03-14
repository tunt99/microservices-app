package com.microservices.app.runner;

public interface StreamsRunner<K, V> {
    void start();
    default V getValueByKey(K key) {
        return null;
    }
}
