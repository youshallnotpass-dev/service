package com.iwillfailyou;

@SuppressWarnings("inheritancefree")
public final class IwfyException extends Exception {

    public IwfyException(final String message) {
        super(message);
    }

    public IwfyException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public IwfyException(final Throwable cause) {
        super(cause);
    }
}
