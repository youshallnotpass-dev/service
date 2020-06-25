package com.iwillfailyou.cactoos;

import com.iwillfailyou.IwfyException;
import org.cactoos.Scalar;
import org.cactoos.scalar.Checked;

public final class IwfyScalar<T> implements Scalar<T> {

    private final Scalar<T> origin;

    public IwfyScalar(final Scalar<T> scalar) {
        this.origin = scalar;
    }

    @Override
    public T value() throws IwfyException {
        return new Checked<>(
            this.origin,
            IwfyException::new
        ).value();
    }

}