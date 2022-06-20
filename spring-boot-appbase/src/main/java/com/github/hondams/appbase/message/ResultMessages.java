package com.github.hondams.appbase.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.springframework.util.StringUtils;
import lombok.Getter;
import lombok.ToString;

/**
 * Messages which have {@link ResultMessageType} and list of {@link ResultMessage}
 */
@Getter
@ToString
public class ResultMessages implements Serializable, Iterable<ResultMessage> {
    /**
     * serial version UID.
     */
    private static final long serialVersionUID = -7323120914608188540L;

    /**
     * message type
     */
    private final ResultMessageType type;

    /**
     * message list
     */
    private final List<ResultMessage> list = new ArrayList<>();

    /**
     * default attribute name for ResultMessages
     */
    public static final String DEFAULT_MESSAGES_ATTRIBUTE_NAME =
            StringUtils.uncapitalize(ResultMessages.class.getSimpleName());

    /**
     * Constructor.
     *
     * @param type message type
     */
    public ResultMessages(ResultMessageType type) {
        this(type, (ResultMessage[]) null);
    }

    /**
     * Constructor.
     *
     * @param type message type
     * @param messages messages to add
     */
    public ResultMessages(ResultMessageType type, ResultMessage... messages) {
        if (type == null) {
            throw new IllegalArgumentException("type must not be null!");
        }
        this.type = type;
        if (messages != null) {
            addAll(messages);
        }
    }

    /**
     * add a ResultMessage
     *
     * @param message ResultMessage instance
     * @return this result messages
     */
    public ResultMessages add(ResultMessage message) {
        if (message != null) {
            this.list.add(message);
        } else {
            throw new IllegalArgumentException("message must not be null");
        }
        return this;
    }

    /**
     * add code to create and add ResultMessages
     *
     * @param code message code
     * @return this result messages
     */
    public ResultMessages add(String code) {
        if (code != null) {
            this.add(ResultMessage.fromCode(code));
        } else {
            throw new IllegalArgumentException("code must not be null");
        }
        return this;
    }

    /**
     * add code and args to create and add ResultMessages
     *
     * @param code message code
     * @param args replacement values of message format
     * @return this result messages
     */
    public ResultMessages add(String code, Object... args) {
        if (code != null) {
            this.add(ResultMessage.fromCode(code, args));
        } else {
            throw new IllegalArgumentException("code must not be null");
        }
        return this;
    }

    /**
     * add all messages (excludes <code>null</code> message)<br>
     * <p>
     * if <code>messages</code> is <code>null</code>, no message is added.
     * </p>
     *
     * @param messages messages to add
     * @return this messages
     */
    public ResultMessages addAll(ResultMessage... messages) {
        if (messages != null) {
            for (ResultMessage message : messages) {
                add(message);
            }
        } else {
            throw new IllegalArgumentException("messages must not be null");
        }
        return this;
    }

    /**
     * add all messages (excludes <code>null</code> message)<br>
     * <p>
     * if <code>messages</code> is <code>null</code>, no message is added.
     * </p>
     *
     * @param messages messages to add
     * @return this messages
     */
    public ResultMessages addAll(Collection<ResultMessage> messages) {
        if (messages != null) {
            for (ResultMessage message : messages) {
                add(message);
            }
        } else {
            throw new IllegalArgumentException("messages must not be null");
        }
        return this;
    }

    /**
     * returns whether messages are not empty.
     *
     * @return whether messages are not empty
     */
    public boolean isNotEmpty() {
        return !this.list.isEmpty();
    }

    /**
     * Returns {@link Iterator} instance that iterates over a list of {@link ResultMessage}
     *
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<ResultMessage> iterator() {
        return this.list.iterator();
    }
}
