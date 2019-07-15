package com.wang.store;

public class StoreAccessException extends Exception {

    public StoreAccessException(String message) {
        super(message);
    }

    public StoreAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public StoreAccessException(Throwable cause) {
        super(cause);
    }
}
