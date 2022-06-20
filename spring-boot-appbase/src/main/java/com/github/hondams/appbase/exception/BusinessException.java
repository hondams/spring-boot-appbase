package com.github.hondams.appbase.exception;

import com.github.hondams.appbase.message.ResultMessage;
import com.github.hondams.appbase.message.ResultMessages;
import com.github.hondams.appbase.message.StandardResultMessagesFactory;
import lombok.Getter;

/**
 * Exception to inform you that it has detected a violation of business rules.<br>
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Instance of {@link ResultMessages}
     */
    private final ResultMessages resultMessages;

    /**
     * Constructor for specify a message.
     * <p>
     * generate a {@link ResultMessages} instance of error type and add a message.
     * </p>
     *
     * @param message result message
     */
    public BusinessException(String message) {
        this(StandardResultMessagesFactory.error().add(ResultMessage.fromText(message)));
    }

    /**
     * Constructor for specify messages.
     * <p>
     * Takes multiple {@code String} messages as argument.
     * </p>
     *
     * @param messages {@link ResultMessages} instance
     */
    public BusinessException(ResultMessages messages) {
        this(messages, null);
    }

    /**
     * Constructor for specify messages and exception.
     * <p>
     * Takes multiple {@code String} messages and cause of exception as argument.
     * </p>
     *
     * @param messages {@link ResultMessages} instance
     * @param cause {@link Throwable} instance
     */
    public BusinessException(ResultMessages messages, Throwable cause) {
        super(cause);
        if (messages == null) {
            throw new IllegalArgumentException("messages must not be null");
        }
        this.resultMessages = messages;
    }

    /**
     * Returns the messages in String format
     *
     * @return String messages
     */
    @Override
    public String getMessage() {
        return this.resultMessages.toString();
    }
}
