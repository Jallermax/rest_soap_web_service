package main;

import java.util.HashMap;

@SuppressWarnings("unchecked")
public class SafeHashMap<K, V extends String> extends HashMap<K, V> {
    @Override
    public V put(K key, V value) {
        return super.put(key, value == null ? (V) "null" : value);
    }
}