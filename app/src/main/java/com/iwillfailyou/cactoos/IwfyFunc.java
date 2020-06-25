package com.iwillfailyou.cactoos;

import com.iwillfailyou.IwfyException;
import org.cactoos.Func;

public final class IwfyFunc<X, Y> implements Func<X, Y> {

    private final Func<X, Y> func;

    public IwfyFunc(final Func<X, Y> fnc) {
        this.func = fnc;
    }

    @Override
    public Y apply(final X input) throws IwfyException {
        return new IwfyScalar<>(() -> this.func.apply(input)).value();
    }

}
