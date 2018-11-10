package com.demon.yu.dmlogger;

import android.os.Handler;

interface LoggerContext {
    void print(MessageEntry messageEntry, Logger logger);

    void addAppender(Appender appender);

    Handler logHandler();
}
