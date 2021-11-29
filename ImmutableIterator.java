package myjava.util;

import java.util.Objects;
import java.util.function.Consumer;

public interface ImmutableIterator<E> {
    boolean hasNext();
    E next();

    default void forEachRemaining(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        while (hasNext())
            action.accept(next());
    }
}