package myjava.util;

public interface ImmutableListIterator<E> extends ImmutableIterator<E> {
    // Query Operations
    boolean hasNext();
    E next();
    boolean hasPrevious();
    E previous();
    int nextIndex();
    int previousIndex();
}
