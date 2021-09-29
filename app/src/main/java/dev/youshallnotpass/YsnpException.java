package dev.youshallnotpass;

@SuppressWarnings("inheritancefree")
public final class YsnpException extends Exception {

    public YsnpException(final String message) {
        super(message);
    }

    public YsnpException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public YsnpException(final Throwable cause) {
        super(cause);
    }
}
