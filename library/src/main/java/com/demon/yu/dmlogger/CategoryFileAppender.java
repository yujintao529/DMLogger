package com.demon.yu.dmlogger;

import android.util.Log;


public class CategoryFileAppender implements Appender {


    private FormatStrategy formatStrategy;
    private String filePath;
    private String fileName;

    private CategoryFileAppender(FormatStrategy formatStrategy, String filePath, String fileName) {
        this.formatStrategy = formatStrategy;
        this.filePath = filePath;
        this.fileName = fileName;
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

        private String filePath;
        private String fileName;

        public Builder withFormatStrategy(FormatStrategy formatStrategy) {
            Utils.checkNotNull(formatStrategy);
            this.formatStrategy = formatStrategy;
            return this;
        }

        public Builder withFileName(String fileName) {
            Utils.checkNotNull(fileName);
            this.fileName = fileName;
            return this;
        }

        public Builder withFilePath(String filePath) {
            Utils.checkNotNull(filePath);
            this.filePath = filePath;
            return this;
        }


        @Override
        public Appender build() {
            if (formatStrategy == null) {
                formatStrategy = new TTCCFormatStrategy();
            }
            if (fileName == null) {
                fileName = "log.txt";
            }
            if (filePath == null) {
                filePath = "/";
            }
            return new CategoryFileAppender(formatStrategy, filePath, fileName);
        }

    }

    public static Appender defaultFileAppender() {
        return new Builder().build();
    }

}

