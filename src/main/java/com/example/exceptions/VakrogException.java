package com.example.exceptions;

/**
 * Created by zonoise on 2017/08/27.
 */
public class VakrogException extends RuntimeException {
    public VakrogException() {
    }

    public VakrogException(String message) {
        super(message);
    }

    public VakrogException(String message, Throwable cause) {
        super(message, cause);
    }

    public VakrogException(Throwable cause) {
        super(cause);
    }
}
