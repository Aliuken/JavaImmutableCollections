package myjava.util;

import java.util.Collection;

public sealed interface ImmutableSet<E> extends ImmutableCollection<E> permits AbstractImmutableSet, ImmutableSetImpl {
    SetType getSetType();

    // Query Operations
    int size();
    boolean isEmpty();
    boolean contains(Object o);
    ImmutableIterator<E> iterator();
    Object[] toArray();
    <T> T[] toArray(T[] a);

    // Bulk Operations
    boolean containsAll(Collection<?> c);
    boolean containsAll(ImmutableCollection<?> c);

    // Comparison and hashing
    boolean equals(Object o);
    int hashCode();
}
