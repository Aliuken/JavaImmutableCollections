package myjava.util;

import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Stream;

public final class ImmutableListImpl<E> extends AbstractImmutableList<E> implements ImmutableList<E> {
    private final ListType listType;
    private final List<E> list;

    public ImmutableListImpl(ListType listType, Collection<? extends E> elements) {
        Objects.requireNonNull(listType, "null listType not allowed");
        this.listType = listType;
        switch(listType) {
            case ARRAY_LIST:
                this.list = new ArrayList<>(elements);
                break;
            case LINKED_LIST:
                this.list = new LinkedList<>(elements);
                break;
            default:
                throw new IllegalArgumentException("invalid listType: " + listType);
        }
    }

    @Override
    public ListType getListType() {
        return listType;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public ImmutableIterator<E> iterator() {
        return new ImmutableIteratorImpl(list);
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return list.toArray(generator);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean containsAll(ImmutableCollection<?> c) {
        boolean result;
        if(c != null) {
            result = true;
            ImmutableIterator<?> iterator = c.iterator();
            while(iterator.hasNext()) {
                Object object = iterator.next();
                if(!list.contains(object)) {
                    result = false;
                    break;
                }
            }
        } else {
            result = list.containsAll(null);
        }

        return result;
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ImmutableListIterator<E> listIterator() {
        return new ImmutableListIteratorImpl(list);
    }

    @Override
    public ImmutableListIterator<E> listIterator(int index) {
        return new ImmutableListIteratorImpl(list, index);
    }

    @Override
    public ImmutableList<E> subList(int fromIndex, int toIndex) {
        List<E> subList = list.subList(fromIndex, toIndex);

        ImmutableList<E> result;
        if(subList != null) {
            result = new ImmutableListImpl<>(ListType.ARRAY_LIST, subList);
        } else {
            result = null;
        }

        return result;
    }

    @Override
    public Stream<E> stream() {
        return list.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return list.parallelStream();
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        ImmutableListImpl that = (ImmutableListImpl) obj;
        return Objects.equals(this.list, that.list);
    }

    @Override
    public String toString() {
        return "ImmutableListImpl[" +
                "list=" + list + ']';
    }
}