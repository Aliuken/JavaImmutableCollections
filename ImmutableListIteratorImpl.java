package myjava.util;

import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class ImmutableListIteratorImpl<E> implements ImmutableListIterator<E> {
    private final ListIterator<E> listIterator;

    public ImmutableListIteratorImpl(List<E> list) {
        this.listIterator = list.listIterator();
    }

    public ImmutableListIteratorImpl(List<E> list, int index) {
        this.listIterator = list.listIterator(index);
    }

    @Override
    public boolean hasNext() {
        return listIterator.hasNext();
    }

    @Override
    public E next() {
        return listIterator.next();
    }

    @Override
    public boolean hasPrevious() {
        return listIterator.hasPrevious();
    }

    @Override
    public E previous() {
        return listIterator.previous();
    }

    @Override
    public int nextIndex() {
        return listIterator.nextIndex();
    }

    @Override
    public int previousIndex() {
        return listIterator.previousIndex();
    }

    @Override
    public int hashCode() {
        return Objects.hash(listIterator);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        ImmutableListIteratorImpl that = (ImmutableListIteratorImpl) obj;
        return Objects.equals(this.listIterator, that.listIterator);
    }

    @Override
    public String toString() {
        return "ImmutableListIteratorImpl[" +
                "listIterator=" + listIterator + ']';
    }
}