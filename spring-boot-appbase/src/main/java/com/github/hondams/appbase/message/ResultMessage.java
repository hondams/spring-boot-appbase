package com.github.hondams.appbase.message;

import java.io.Serializable;
import org.springframework.util.Assert;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Message which has a code or a text and args.<br>
 * There are 3 way to create a message.
 * <hr>
 * Way1. to specify message code and args
 *
 * <pre>
 * <code>
 * ResultMessage message = ResultMessage.fromCode(code, args);
 * </code>
 * </pre>
 * <hr>
 * Way2. to specify message text directly
 *
 * <pre>
 * <code>
 * ResultMessage message = ResultMessage.fromText(text);
 * </code>
 * </pre>
 * <hr>
 * Way3. if you want to set code (+ args) and message text (as default text used when code is not
 * found)
 *
 * <pre>
 * <code>
 * ResultMessage message = new ResultMessage(code, args, defaultText);
 * </code>
 * </pre>
 */
@Getter
@EqualsAndHashCode
@ToString
public class ResultMessage implements Serializable {
    /**
     * serial version UID.
     */
    private static final long serialVersionUID = -2020904640866275166L;

    /**
     * emtpy array object
     */
    private static final Object[] EMPTY_ARRAY = {};

    /**
     * message code
     */
    private final String code;

    /**
     * message arguments
     */
    private final Object[] args;

    /**
     * message text
     */
    private final String text;

    /**
     * Constructor.<br>
     *
     * @param code message code
     * @param args replacement values of message format
     * @param text default message
     */
    public ResultMessage(String code, Object[] args, String text) {
        this.code = code;
        this.args = args == null ? EMPTY_ARRAY : args;
        this.text = text;
    }

    /**
     * create <code>ResultMessage</code> instance which has the given code and args<br>
     * <p>
     * <code>text</code> is <code>null</code>
     * </p>
     *
     * @param code message code (must not be null)
     * @param args replacement values of message format
     * @return ResultMessage instance
     */
    public static ResultMessage fromCode(String code, Object... args) {
        Assert.notNull(code, "code must not be null");
        return new ResultMessage(code, args, null);
    }

    /**
     * create <code>ResultMessage</code> instance which has the given text<br>
     * <p>
     * <code>code</code> is <code>null</code>
     * </p>
     *
     * @param text message tet (must not be null)
     * @return ResultMessage instance
     */
    public static ResultMessage fromText(String text) {
        Assert.notNull(text, "text must not be null");
        return new ResultMessage(null, EMPTY_ARRAY, text);

    }
}
