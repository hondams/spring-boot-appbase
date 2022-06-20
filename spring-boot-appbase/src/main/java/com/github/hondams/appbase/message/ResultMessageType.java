package com.github.hondams.appbase.message;

/**
 * An interface to show type of {@link ResultMessage} <br>
 * <p>
 * The {@link StandardResultMessageType} enumeration type defines the <i>standard</i> types.
 * </p>
 */
public interface ResultMessageType {
    /**
     * Returns message type as String.<br>
     *
     * @return message type
     */
    String getType();
}
