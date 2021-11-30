package myjava.util;

import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public sealed interface ImmutableCollection<E> extends ImmutableIterable<E> permits AbstractImmutableCollection, ImmutableList, ImmutableSet {
    // Query Operations
    int size();
    boolean isEmpty();
    boolean contains(Object o);
    ImmutableIterator<E> iterator();
    Object[] toArray();
    <T> T[] toArray(T[] a);

    default <T> T[] toArray(IntFunction<T[]> generator) {
        return toArray(generator.apply(0));
    }

    // Bulk Operations
    boolean containsAll(Collection<?> c);
    boolean containsAll(ImmutableCollection<?> c);

    // Comparison and hashing
    boolean equals(Object o);
    int hashCode();

    private Spliterator<E> spliterator() {
        Collection<E> collection = this.toCollection();
        return Spliterators.spliterator(collection, 0);
    }

    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    default Collection<E> toCollection() {
        ImmutableIterator<E> iterator = this.iterator();

        Collection<E> collection = null;
        if(iterator != null) {
            if(this instanceof ImmutableList immutableList) {
                ListType listType = immutableList.getListType();
                collection = switch(listType) {
                    case ARRAY_LIST -> {
                        List<E> list = new ArrayList<>();
                        yield list;
                    }

                    case LINKED_LIST -> {
                        List<E> list = new LinkedList<>();
                        yield list;
                    }

                    default -> {
                        throw new IllegalArgumentException("listType: " + listType);
                    }
                };
            } else if(this instanceof ImmutableSet immutableSet) {
                SetType setType = immutableSet.getSetType();
                collection = switch(setType) {
                    case HASH_SET -> {
                        Set<E> set = new HashSet<>();
                        yield set;
                    }

                    case LINKED_HASH_SET -> {
                        Set<E> set = new LinkedHashSet<>();
                        yield set;
                    }

                    case TREE_SET -> {
                        Set<E> set = new TreeSet<>();
                        yield set;
                    }

                    default -> {
                        throw new IllegalArgumentException("setType: " + setType);
                    }
                };
            }
            if(collection != null) {
                while (iterator.hasNext()) {
                    E element = iterator.next();
                    collection.add(element);
                }
            }
        } else {
            collection = null;
        }

        return collection;
    }
}