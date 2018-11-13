package com.demon.yu.dmlogger;


public interface Appender extends LogAdapter {


    boolean filter(Object tag);

    void append(MessageEntry messageEntry, Logger logger);

    interface Builder {
        Appender build();
    }
}
