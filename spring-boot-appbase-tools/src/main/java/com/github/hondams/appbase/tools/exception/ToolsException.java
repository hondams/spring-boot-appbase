package com.github.hondams.appbase.tools.exception;

public class ToolsException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ToolsException() {
    }

    public ToolsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToolsException(String message) {
        super(message);
    }

    public ToolsException(Throwable cause) {
        super(cause);
    }
}
