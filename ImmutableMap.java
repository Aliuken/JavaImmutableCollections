package myjava.util;

import java.util.*;
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

        default Map.Entry<K, V> toMapEntry() {
            K key = this.getKey();
            V value = this.getValue();
            AbstractMap.SimpleEntry<K, V> mapEntry = new AbstractMap.SimpleEntry<>(key, value);
            return mapEntry;
        }
    }

    // Comparison and hashing
    boolean equals(Object o);
    int hashCode();

    // Defaultable methods
    V getOrDefault(Object key, V defaultValue);
    void forEach(BiConsumer<? super K, ? super V> action);

    default Map<K, V> toMap() {
        ImmutableSet<Entry<K, V>> immutableEntrySet = this.entrySet();

        ImmutableIterator<Entry<K, V>> iterator;
        if(immutableEntrySet != null) {
            iterator = immutableEntrySet.iterator();
        } else {
            iterator = null;
        }

        Map<K, V> map;
        if(iterator != null) {
            MapType mapType = this.getMapType();

            map = switch(mapType) {
                case HASH_MAP -> {
                    Map<K, V> hashMap = new HashMap<>();
                    yield hashMap;
                }

                case LINKED_HASH_MAP -> {
                    Map<K, V> linkedHashMap = new LinkedHashMap<>();
                    yield linkedHashMap;
                }

                case TREE_MAP -> {
                    Map<K, V> treeMap = new TreeMap<>();
                    yield treeMap;
                }

                default -> {
                    throw new IllegalArgumentException("mapType: " + mapType);
                }
            };
            if(map != null) {
                while (iterator.hasNext()) {
                    Entry<K, V> immutableEntry = iterator.next();
                    map.put(immutableEntry.getKey(), immutableEntry.getValue());
                }
            }
        } else {
            map = null;
        }

        return map;
    }
}
