package myjava.util;

import java.util.*;

public abstract sealed class AbstractImmutableList<E> extends AbstractImmutableCollection<E> implements ImmutableList<E> permits AbstractImmutableList.SubList, ImmutableListImpl {
    protected AbstractImmutableList() {
    }

    public abstract E get(int index);

    // Search Operations

    public int indexOf(Object o) {
        ImmutableListIterator<E> it = listIterator();
        if (o==null) {
            while (it.hasNext())
                if (it.next()==null)
                    return it.previousIndex();
        } else {
            while (it.hasNext())
                if (o.equals(it.next()))
                    return it.previousIndex();
        }
        return -1;
    }

    public int lastIndexOf(Object o) {
        ImmutableListIterator<E> it = listIterator(size());
        if (o==null) {
            while (it.hasPrevious())
                if (it.previous()==null)
                    return it.nextIndex();
        } else {
            while (it.hasPrevious())
                if (o.equals(it.previous()))
                    return it.nextIndex();
        }
        return -1;
    }

    // Iterators

    public ImmutableIterator<E> iterator() {
        return new AbstractImmutableList.Itr();
    }

    public ImmutableListIterator<E> listIterator() {
        return listIterator(0);
    }

    public ImmutableListIterator<E> listIterator(final int index) {
        rangeCheckForAdd(index);

        return new AbstractImmutableList.ListItr(index);
    }

    private class Itr implements ImmutableIterator<E> {
        int cursor = 0;
        int lastRet = -1;
        int expectedModCount = modCount;

        public boolean hasNext() {
            return cursor != size();
        }

        public E next() {
            checkForComodification();
            try {
                int i = cursor;
                E next = get(i);
                lastRet = i;
                cursor = i + 1;
                return next;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException(e);
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    private class ListItr extends Itr implements ImmutableListIterator<E> {
        ListItr(int index) {
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public E previous() {
            checkForComodification();
            try {
                int i = cursor - 1;
                E previous = get(i);
                lastRet = cursor = i;
                return previous;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException(e);
            }
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor-1;
        }
    }

    public ImmutableList<E> subList(int fromIndex, int toIndex) {
        subListRangeCheck(fromIndex, toIndex, size());
        return new AbstractImmutableList.SubList<>(this, fromIndex, toIndex);
    }

    static void subListRangeCheck(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                    ") > toIndex(" + toIndex + ")");
    }

    // Comparison and hashing

    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof List))
            return false;

        ImmutableListIterator<E> e1 = listIterator();
        ImmutableListIterator<?> e2 = ((ImmutableList<?>) o).listIterator();
        while (e1.hasNext() && e2.hasNext()) {
            E o1 = e1.next();
            Object o2 = e2.next();
            if (!(o1==null ? o2==null : o1.equals(o2)))
                return false;
        }
        return !(e1.hasNext() || e2.hasNext());
    }

    public int hashCode() {
        int hashCode = 1;
        ImmutableIterator<E> iterator = this.iterator();
        while(iterator.hasNext()) {
            E e = iterator.next();
            hashCode = 31*hashCode + (e==null ? 0 : e.hashCode());
        }
        return hashCode;
    }

    protected transient int modCount = 0;

    private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size();
    }

    public final static class SubList<E> extends AbstractImmutableList<E> {
        private final AbstractImmutableList<E> root;
        private final AbstractImmutableList.SubList<E> parent;
        private final int offset;
        protected int size;

        public SubList(AbstractImmutableList<E> root, int fromIndex, int toIndex) {
            this.root = root;
            this.parent = null;
            this.offset = fromIndex;
            this.size = toIndex - fromIndex;
            this.modCount = root.modCount;
        }

        protected SubList(AbstractImmutableList.SubList<E> parent, int fromIndex, int toIndex) {
            this.root = parent.root;
            this.parent = parent;
            this.offset = parent.offset + fromIndex;
            this.size = toIndex - fromIndex;
            this.modCount = root.modCount;
        }

        @Override
        public ListType getListType() {
            return root.getListType();
        }

        public E get(int index) {
            Objects.checkIndex(index, size);
            checkForComodification();
            return root.get(offset + index);
        }

        public int size() {
            checkForComodification();
            return size;
        }

        public ImmutableIterator<E> iterator() {
            return listIterator();
        }

        public ImmutableListIterator<E> listIterator(int index) {
            checkForComodification();
            rangeCheckForAdd(index);

            return new ImmutableListIterator<E>() {
                private final ImmutableListIterator<E> i =
                        root.listIterator(offset + index);

                public boolean hasNext() {
                    return nextIndex() < size;
                }

                public E next() {
                    if (hasNext())
                        return i.next();
                    else
                        throw new NoSuchElementException();
                }

                public boolean hasPrevious() {
                    return previousIndex() >= 0;
                }

                public E previous() {
                    if (hasPrevious())
                        return i.previous();
                    else
                        throw new NoSuchElementException();
                }

                public int nextIndex() {
                    return i.nextIndex() - offset;
                }

                public int previousIndex() {
                    return i.previousIndex() - offset;
                }
            };
        }

        public ImmutableList<E> subList(int fromIndex, int toIndex) {
            subListRangeCheck(fromIndex, toIndex, size);
            return new AbstractImmutableList.SubList<>(this, fromIndex, toIndex);
        }

        private void rangeCheckForAdd(int index) {
            if (index < 0 || index > size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private String outOfBoundsMsg(int index) {
            return "Index: "+index+", Size: "+size;
        }

        private void checkForComodification() {
            if (root.modCount != this.modCount)
                throw new ConcurrentModificationException();
        }
    }
}
