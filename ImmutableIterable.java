package myjava.util;

import java.util.Objects;
import java.util.function.Consumer;

public interface ImmutableIterable<T> {
    ImmutableIterator<T> iterator();

    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        ImmutableIterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            T t = iterator.next();
            action.accept(t);
        }
    }
}
