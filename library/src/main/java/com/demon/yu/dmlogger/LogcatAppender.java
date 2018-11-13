package com.demon.yu.dmlogger;

import android.util.Log;


public class LogcatAppender implements Appender {


    private FormatStrategy formatStrategy;


    private LogcatAppender(FormatStrategy formatStrategy) {
        this.formatStrategy = formatStrategy;
    }

    @Override
    public void log(Level level, String tag, String message) {
        Log.println(level.value, tag, message);
    }

    @Override
    public boolean filter(Object tag) {
        return true;
    }

    @Override
    public void append(MessageEntry messageEntry, Logger logger) {
        formatStrategy.layout(messageEntry, logger, this);
    }

    public static class Builder implements Appender.Builder {
        private FormatStrategy formatStrategy;

        public Builder withFormatStrategy(FormatStrategy formatStrategy) {
            Utils.checkNotNull(formatStrategy);
            this.formatStrategy = formatStrategy;
            return this;
        }

        @Override
        public Appender build() {
            return new LogcatAppender(formatStrategy);
        }

    }

    public static Appender defaultLogcatAppender() {
        return new Builder().withFormatStrategy(new LogcatFormatStrategy()).build();
    }
    public static Appender defaultPrettyLogcatAppender() {
        return new Builder().withFormatStrategy(new PrettyFormatStrategy()).build();
    }

}

