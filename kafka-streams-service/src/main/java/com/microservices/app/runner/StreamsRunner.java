package com.microservices.app.runner;

public interface StreamsRunner<K, V> {
    default V getValueByKey(K key) {
        return null;
    }
}
