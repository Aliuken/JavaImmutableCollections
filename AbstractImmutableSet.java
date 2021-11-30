package myjava.util;

import java.util.Collection;
import java.util.Set;

public abstract sealed class AbstractImmutableSet<E> extends AbstractImmutableCollection<E> implements ImmutableSet<E> permits AbstractNonSealedImmutableSet, ImmutableSetImpl {
    protected AbstractImmutableSet() {
    }

    // Comparison and hashing

    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Set))
            return false;
        Collection<?> c = (Collection<?>) o;
        if (c.size() != size())
            return false;
        try {
            return containsAll(c);
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
    }

    public int hashCode() {
        int h = 0;
        ImmutableIterator<E> i = iterator();
        while (i.hasNext()) {
            E obj = i.next();
            if (obj != null)
                h += obj.hashCode();
        }
        return h;
    }

}
