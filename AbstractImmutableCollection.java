package myjava.util;

import java.util.Collection;

public abstract sealed class AbstractImmutableCollection<E> implements ImmutableCollection<E> permits AbstractNonSealedImmutableCollection, AbstractImmutableList, AbstractImmutableSet {
    protected AbstractImmutableCollection() {
    }

    // Query Operations
    public abstract ImmutableIterator<E> iterator();

    public abstract int size();

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(Object o) {
        ImmutableIterator<E> it = iterator();
        if (o==null) {
            while (it.hasNext())
                if (it.next()==null)
                    return true;
        } else {
            while (it.hasNext())
                if (o.equals(it.next()))
                    return true;
        }
        return false;
    }

    public Object[] toArray() {
        Collection<E> collection = this.toCollection();
        return collection.toArray();
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        Collection<E> collection = this.toCollection();
        return collection.toArray(a);
    }

    // Bulk Operations

    public boolean containsAll(Collection<?> c) {
        for (Object e : c)
            if (!contains(e))
                return false;
        return true;
    }

    public boolean containsAll(ImmutableCollection<?> c) {
        ImmutableIterator<?> iterator = c.iterator();

        boolean result;
        if(iterator != null) {
            result = true;
            while(iterator.hasNext()) {
                Object element = iterator.next();
                if (!contains(element)) {
                    result = false;
                    break;
                }
            }
        } else {
            result = false;
        }

        return result;
    }

    //  String conversion

    public String toString() {
        ImmutableIterator<E> it = iterator();
        if (! it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (! it.hasNext())
                return sb.append(']').toString();
            sb.append(',').append(' ');
        }
    }
}
