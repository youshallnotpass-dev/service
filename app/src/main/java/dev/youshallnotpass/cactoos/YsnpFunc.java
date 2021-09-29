package dev.youshallnotpass.cactoos;

import dev.youshallnotpass.YsnpException;
import org.cactoos.Func;

public final class YsnpFunc<X, Y> implements Func<X, Y> {

    private final Func<X, Y> func;

    public YsnpFunc(final Func<X, Y> fnc) {
        this.func = fnc;
    }

    @Override
    public Y apply(final X input) throws YsnpException {
        return new YsnpScalar<>(() -> this.func.apply(input)).value();
    }

}
