package com.github.hondams.appbase.message;

public class StandardResultMessagesFactory {


    /**
     * factory method for success messages.
     *
     * @return success messages
     */
    public static ResultMessages success() {
        return new ResultMessages(StandardResultMessageType.SUCCESS);
    }

    /**
     * factory method for info messages.
     *
     * @return info messages
     */
    public static ResultMessages info() {
        return new ResultMessages(StandardResultMessageType.INFO);
    }

    /**
     * factory method for warning messages.
     *
     * @return warning messages
     * @since 5.0.0
     */
    public static ResultMessages warning() {
        return new ResultMessages(StandardResultMessageType.WARNING);
    }

    /**
     * factory method for error messages.
     *
     * @return error messages
     */
    public static ResultMessages error() {
        return new ResultMessages(StandardResultMessageType.ERROR);
    }

    /**
     * factory method for danger messages.
     *
     * @return danger messages
     */
    public static ResultMessages danger() {
        return new ResultMessages(StandardResultMessageType.DANGER);
    }

    /**
     * factory method for primary messages.
     *
     * @return primary messages
     * @since 5.7.0
     */
    public static ResultMessages primary() {
        return new ResultMessages(StandardResultMessageType.PRIMARY);
    }

    /**
     * factory method for secondary messages.
     *
     * @return secondary messages
     * @since 5.7.0
     */
    public static ResultMessages secondary() {
        return new ResultMessages(StandardResultMessageType.SECONDARY);
    }

    /**
     * factory method for light messages.
     *
     * @return light messages
     * @since 5.7.0
     */
    public static ResultMessages light() {
        return new ResultMessages(StandardResultMessageType.LIGHT);
    }

    /**
     * factory method for dark messages.
     *
     * @return dark messages
     * @since 5.7.0
     */
    public static ResultMessages dark() {
        return new ResultMessages(StandardResultMessageType.DARK);
    }
}
