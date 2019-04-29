package com.iwillfailyou;

public class IwfyException extends Exception {

    public IwfyException(String message) {
        super(message);
    }

    public IwfyException(String message, Throwable cause) {
        super(message, cause);
    }
}
