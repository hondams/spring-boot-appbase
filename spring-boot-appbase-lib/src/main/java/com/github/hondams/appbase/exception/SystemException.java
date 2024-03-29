package com.github.hondams.appbase.exception;

public class SystemException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public SystemException() {
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }
}
