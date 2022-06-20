package com.github.hondams.appbase.message;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Defines the standard result message types. <br>
 * Classified into following types:<br>
 * <ul>
 * <li><code>success</code></li>
 * <li><code>info</code></li>
 * <li><code>warning(Added from 5.0.0)</code></li>
 * <li><code>error</code></li>
 * <li><code>danger</code></li>
 * <li><code>primary(Added from 5.7.0)</code></li>
 * <li><code>secondary(Added from 5.7.0)</code></li>
 * <li><code>light(Added from 5.7.0)</code></li>
 * <li><code>dark(Added from 5.7.0)</code></li>
 * </ul>
 * The level of <code>danger</code> is as same as <code>error</code> and <code>danger</code> is
 * usually used as alias for <code>error</code>.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum StandardResultMessageType implements ResultMessageType {
    /**
     * message type is <code>success</code>.
     */
    SUCCESS("success"),
    /**
     * message type is <code>info</code>.
     */
    INFO("info"),
    /**
     * message type is <code>warning</code>.
     *
     * @since 5.0.0
     */
    WARNING("warning"),
    /**
     * message type is <code>error</code>.
     */
    ERROR("error"),
    /**
     * message type is <code>danger</code>.
     */
    DANGER("danger"),
    /**
     * message type is <code>primary</code>.
     *
     * @since 5.7.0
     */
    PRIMARY("primary"),
    /**
     * message type is <code>secondary</code>.
     *
     * @since 5.7.0
     */
    SECONDARY("secondary"),
    /**
     * message type is <code>light</code>.
     *
     * @since 5.7.0
     */
    LIGHT("light"),
    /**
     * message type is <code>dark</code>.
     *
     * @since 5.7.0
     */
    DARK("dark");

    /**
     * message type
     */
    private final String type;
}
