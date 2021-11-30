package myjava.util;

import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Stream;

public final class ImmutableSetImpl<E> extends AbstractImmutableSet<E> implements ImmutableSet<E> {
    private final SetType setType;
    private final Set<E> set;

    public ImmutableSetImpl(SetType setType, Collection<? extends E> elements) {
        Objects.requireNonNull(setType, "null setType not allowed");
        switch(setType) {
            case HASH_SET -> {
                this.setType = setType;
                this.set = new HashSet<>(elements);
            }

            case LINKED_HASH_SET -> {
                this.setType = setType;
                this.set = new LinkedHashSet<>(elements);
            }

            case TREE_SET -> {
                this.setType = setType;
                this.set = new TreeSet<>(elements);
            }

            default -> {
                throw new IllegalArgumentException("invalid setType: " + setType);
            }
        }
    }

    @Override
    public SetType getSetType() {
        return setType;
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public ImmutableIterator<E> iterator() {
        return new ImmutableIteratorImpl(set);
    }

    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return set.toArray(a);
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return set.toArray(generator);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }

    @Override
    public boolean containsAll(ImmutableCollection<?> c) {
        boolean result;
        if(c != null) {
            result = true;
            ImmutableIterator<?> iterator = c.iterator();
            while(iterator.hasNext()) {
                Object object = iterator.next();
                if(!set.contains(object)) {
                    result = false;
                    break;
                }
            }
        } else {
            result = set.containsAll(null);
        }

        return result;
    }

    @Override
    public Stream<E> stream() {
        return set.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return set.parallelStream();
    }

    @Override
    public int hashCode() {
        return Objects.hash(set);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        ImmutableSetImpl that = (ImmutableSetImpl) obj;
        return Objects.equals(this.set, that.set);
    }

    @Override
    public String toString() {
        return "ImmutableSetImpl[" +
                "set=" + set + ']';
    }
}