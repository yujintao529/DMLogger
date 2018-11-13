package com.demon.yu.dmlogger;

public interface Logger {
    void debug(Object tag, String message);

    void debug(Object tag, String message, Object... o);

    void info(Object tag, String message);

    void info(Object tag, String message, Object... o);

    void warn(Object tag, String message);

    void warn(Object tag, String message, Object... o);

    void error(Object tag, Throwable throwable, String message);

    void error(Object tag, Throwable throwable, String message, Object... o);

    String name();

    LoggerContext getContext();




}
