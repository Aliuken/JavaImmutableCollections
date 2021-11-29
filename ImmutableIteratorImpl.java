package myjava.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class ImmutableIteratorImpl<E> implements ImmutableIterator<E> {
    private final Iterator<E> iterator;

    public ImmutableIteratorImpl(Collection<E> collection) {
        this.iterator = collection.iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public E next() {
        return iterator.next();
    }

    @Override
    public int hashCode() {
        return Objects.hash(iterator);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        ImmutableIteratorImpl that = (ImmutableIteratorImpl) obj;
        return Objects.equals(this.iterator, that.iterator);
    }

    @Override
    public String toString() {
        return "ImmutableIteratorImpl[" +
                "iterator=" + iterator + ']';
    }
}