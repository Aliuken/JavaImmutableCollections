package myjava.util;

import java.util.Collection;

public sealed interface ImmutableList<E> extends ImmutableCollection<E> permits AbstractImmutableList, ImmutableListImpl {
    ListType getListType();

    // Query Operations
    int size();
    boolean isEmpty();
    boolean contains(Object o);
    ImmutableIterator<E> iterator();
    Object[] toArray();
    <T> T[] toArray(T[] a);

    // Bulk Modification Operations
    boolean containsAll(Collection<?> c);
    boolean containsAll(ImmutableCollection<?> c);

    // Comparison and hashing
    boolean equals(Object o);
    int hashCode();

    // Positional Access Operations
    E get(int index);

    // Search Operations
    int indexOf(Object o);
    int lastIndexOf(Object o);

    // List Iterators
    ImmutableListIterator<E> listIterator();
    ImmutableListIterator<E> listIterator(int index);

    // View
    ImmutableList<E> subList(int fromIndex, int toIndex);
}
