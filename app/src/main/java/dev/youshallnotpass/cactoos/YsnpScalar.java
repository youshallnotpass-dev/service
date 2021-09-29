package dev.youshallnotpass.cactoos;

import dev.youshallnotpass.YsnpException;
import org.cactoos.Scalar;
import org.cactoos.scalar.Checked;

public final class YsnpScalar<T> implements Scalar<T> {

    private final Scalar<T> origin;

    public YsnpScalar(final Scalar<T> scalar) {
        this.origin = scalar;
    }

    @Override
    public T value() throws YsnpException {
        return new Checked<>(
            this.origin,
            YsnpException::new
        ).value();
    }

}