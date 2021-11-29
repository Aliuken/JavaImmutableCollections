package myjava.util;

import java.util.*;

public abstract sealed class AbstractImmutableMap<K,V> implements ImmutableMap<K,V> permits ImmutableMapImpl {
    protected AbstractImmutableMap() {
    }

    // Query Operations
    public int size() {
        return entrySet().size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsValue(Object value) {
        ImmutableIterator<Entry<K,V>> i = entrySet().iterator();
        if (value==null) {
            while (i.hasNext()) {
                Entry<K,V> e = i.next();
                if (e.getValue()==null)
                    return true;
            }
        } else {
            while (i.hasNext()) {
                Entry<K,V> e = i.next();
                if (value.equals(e.getValue()))
                    return true;
            }
        }
        return false;
    }

    public boolean containsKey(Object key) {
        ImmutableIterator<Entry<K,V>> i = entrySet().iterator();
        if (key==null) {
            while (i.hasNext()) {
                Entry<K,V> e = i.next();
                if (e.getKey()==null)
                    return true;
            }
        } else {
            while (i.hasNext()) {
                Entry<K,V> e = i.next();
                if (key.equals(e.getKey()))
                    return true;
            }
        }
        return false;
    }

    public V get(Object key) {
        ImmutableIterator<Entry<K,V>> i = entrySet().iterator();
        if (key==null) {
            while (i.hasNext()) {
                Entry<K,V> e = i.next();
                if (e.getKey()==null)
                    return e.getValue();
            }
        } else {
            while (i.hasNext()) {
                Entry<K,V> e = i.next();
                if (key.equals(e.getKey()))
                    return e.getValue();
            }
        }
        return null;
    }

    // Views
    transient ImmutableSet<K> keySet;
    transient ImmutableCollection<V> values;

    public ImmutableSet<K> keySet() {
        ImmutableSet<K> ks = keySet;
        if (ks == null) {
            ks = new AbstractNonSealedImmutableSet<K>() {
                public ImmutableIterator<K> iterator() {
                    return new ImmutableIterator<K>() {
                        private ImmutableIterator<Entry<K,V>> i = entrySet().iterator();

                        public boolean hasNext() {
                            return i.hasNext();
                        }

                        public K next() {
                            return i.next().getKey();
                        }
                    };
                }

                @Override
                public SetType getSetType() {
                    return entrySet().getSetType();
                }

                public int size() {
                    return AbstractImmutableMap.this.size();
                }

                public boolean isEmpty() {
                    return AbstractImmutableMap.this.isEmpty();
                }

                public boolean contains(Object k) {
                    return AbstractImmutableMap.this.containsKey(k);
                }
            };
            keySet = ks;
        }
        return ks;
    }

    public ImmutableCollection<V> values() {
        ImmutableCollection<V> vals = values;
        if (vals == null) {
            vals = new AbstractNonSealedImmutableCollection<V>() {
                public ImmutableIterator<V> iterator() {
                    return new ImmutableIterator<V>() {
                        private ImmutableIterator<Entry<K,V>> i = entrySet().iterator();

                        public boolean hasNext() {
                            return i.hasNext();
                        }

                        public V next() {
                            return i.next().getValue();
                        }
                    };
                }

                public int size() {
                    return AbstractImmutableMap.this.size();
                }

                public boolean isEmpty() {
                    return AbstractImmutableMap.this.isEmpty();
                }

                public boolean contains(Object v) {
                    return AbstractImmutableMap.this.containsValue(v);
                }
            };
            values = vals;
        }
        return vals;
    }

    public abstract ImmutableSet<Entry<K,V>> entrySet();

    // Comparison and hashing
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Map<?, ?> m))
            return false;
        if (m.size() != size())
            return false;

        try {
            ImmutableSet<Entry<K, V>> entryImmutableSet = entrySet();
            ImmutableIterator<Entry<K, V>> iterator = entryImmutableSet.iterator();

            while(iterator.hasNext()) {
                Entry<K, V> e = iterator.next();
                K key = e.getKey();
                V value = e.getValue();
                if (value == null) {
                    if (!(m.get(key) == null && m.containsKey(key)))
                        return false;
                } else {
                    if (!value.equals(m.get(key)))
                        return false;
                }
            }
        } catch (ClassCastException unused) {
            return false;
        } catch (NullPointerException unused) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int h = 0;
        ImmutableSet<Entry<K, V>> entryImmutableSet = entrySet();
        ImmutableIterator<Entry<K, V>> iterator = entryImmutableSet.iterator();

        while(iterator.hasNext()) {
            Entry<K, V> entry = iterator.next();
            h += entry.hashCode();
        }
        return h;
    }

    public String toString() {
        ImmutableIterator<Entry<K,V>> i = entrySet().iterator();
        if (! i.hasNext())
            return "{}";

        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (;;) {
            Entry<K,V> e = i.next();
            K key = e.getKey();
            V value = e.getValue();
            sb.append(key   == this ? "(this Map)" : key);
            sb.append('=');
            sb.append(value == this ? "(this Map)" : value);
            if (! i.hasNext())
                return sb.append('}').toString();
            sb.append(',').append(' ');
        }
    }

    protected Object clone() throws CloneNotSupportedException {
        AbstractImmutableMap<?,?> result = (AbstractImmutableMap<?,?>)super.clone();
        result.keySet = null;
        result.values = null;
        return result;
    }

    private static boolean eq(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    public static class SimpleImmutableEntry<K,V>
            implements Entry<K,V>, java.io.Serializable
    {
        @java.io.Serial
        private static final long serialVersionUID = 7138329143949025155L;

        @SuppressWarnings("serial") // Not statically typed as Serializable
        private final K key;
        @SuppressWarnings("serial") // Not statically typed as Serializable
        private final V value;

        public SimpleImmutableEntry(K key, V value) {
            this.key   = key;
            this.value = value;
        }

        public SimpleImmutableEntry(Entry<? extends K, ? extends V> entry) {
            this.key   = entry.getKey();
            this.value = entry.getValue();
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public boolean equals(Object o) {
            return o instanceof Entry<?, ?> e
                    && eq(key, e.getKey())
                    && eq(value, e.getValue());
        }

        public int hashCode() {
            return (key   == null ? 0 :   key.hashCode()) ^
                    (value == null ? 0 : value.hashCode());
        }

        public String toString() {
            return key + "=" + value;
        }
    }
}
