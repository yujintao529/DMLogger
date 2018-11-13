package com.demon.yu.dmlogger;

public interface FormatStrategy {
    void layout(MessageEntry messageEntry, Logger logger, LogAdapter logAdapter);
}
