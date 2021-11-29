package myjava.util;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiConsumer;

public sealed interface ImmutableMap<K, V> permits AbstractImmutableMap, ImmutableMapImpl {
    MapType getMapType();

    // Query Operations
    int size();
    boolean isEmpty();
    boolean containsKey(Object key);
    boolean containsValue(Object value);
    V get(Object key);

    // Views
    ImmutableSet<K> keySet();
    ImmutableCollection<V> values();
    ImmutableSet<ImmutableMap.Entry<K, V>> entrySet();

    interface Entry<K, V> {
        K getKey();
        V getValue();
        boolean equals(Object o);
        int hashCode();
    }

    // Comparison and hashing
    boolean equals(Object o);
    int hashCode();

    // Defaultable methods
    V getOrDefault(Object key, V defaultValue);
    void forEach(BiConsumer<? super K, ? super V> action);
}
