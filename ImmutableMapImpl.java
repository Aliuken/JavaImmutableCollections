package myjava.util;

import java.util.*;
import java.util.function.BiConsumer;

public final class ImmutableMapImpl<K, V> extends AbstractImmutableMap<K,V> implements ImmutableMap<K, V> {
    private final MapType mapType;
    private final Map<K, V> map;

    public ImmutableMapImpl(MapType mapType, Map<? extends K,? extends V> entries) {
        Objects.requireNonNull(mapType, "null mapType not allowed");
        switch(mapType) {
            case HASH_MAP -> {
                this.mapType = mapType;
                this.map = new HashMap<>(entries);
            }

            case LINKED_HASH_MAP -> {
                this.mapType = mapType;
                this.map = new LinkedHashMap<>(entries);
            }

            case TREE_MAP -> {
                this.mapType = mapType;
                this.map = new TreeMap<>(entries);
            }

            default -> {
                throw new IllegalArgumentException("invalid mapType: " + mapType);
            }
        }
    }

    @Override
    public MapType getMapType() {
        return mapType;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public ImmutableSet<K> keySet() {
        return new ImmutableSetImpl<>(SetType.HASH_SET, map.keySet());
    }

    @Override
    public ImmutableCollection<V> values() {
        return new ImmutableListImpl<>(ListType.ARRAY_LIST, map.values());
    }

    @Override
    public ImmutableSet<ImmutableMap.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entries = map.entrySet();

        ImmutableSet<ImmutableMap.Entry<K, V>> result;
        if(entries != null) {
            Set<ImmutableMap.Entry<K, V>> immutableEntries = new HashSet<>();
            for(Map.Entry<K, V> entry: entries) {
                immutableEntries.add(new AbstractImmutableMap.SimpleImmutableEntry<K, V>(entry.getKey(), entry.getValue()));
            }
            result = new ImmutableSetImpl<ImmutableMap.Entry<K, V>>(SetType.HASH_SET, immutableEntries);
        } else {
            result = null;
        }

        return result;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        map.forEach(action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        ImmutableMapImpl that = (ImmutableMapImpl) obj;
        return Objects.equals(this.map, that.map);
    }

    @Override
    public String toString() {
        return "ImmutableMapImpl[" +
                "map=" + map + ']';
    }
}