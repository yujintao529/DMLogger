package com.demon.yu.dmlogger;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * 控制管理logger
 */
public class LoggerManager implements LoggerContext {


    private static LoggerManager loggerManager = new LoggerManager();

    /**
     * 添加appender
     *
     * @param appender
     */
    public static void putAppender(Appender appender) {
        loggerManager.addAppender(appender);
    }


    public static LoggerBuilder newLoggerBuilder() {
        return loggerManager.newLoggerBuilderInterval();
    }

    //创建的所有logger实例
    private List<Logger> loggerList;

    private List<Logger> activeLoggers;

    //LoggerContext的实现
    private LoggerContext mBaseLoggerContext;


    private LoggerManager() {
        loggerList = new ArrayList<>();
        activeLoggers = new ArrayList<>();
        mBaseLoggerContext = new LoggerContextImpl();

    }


    @Override
    public void print(MessageEntry messageEntry, Logger logger) {
        mBaseLoggerContext.print(messageEntry, logger);
    }

    @Override
    public void addAppender(Appender appender) {
        mBaseLoggerContext.addAppender(appender);
    }

    @Override
    public Handler logHandler() {
        return mBaseLoggerContext.logHandler();
    }


    public LoggerBuilder newLoggerBuilderInterval() {
        return new LoggerBuilder(this);
    }


    public static class LoggerBuilder {
        private LoggerManager loggerManager;
        private String name;
        private List<Appender> appenders;
        private boolean useSystemAppenders;

        private LoggerBuilder(LoggerManager loggerManager) {
            this.loggerManager = loggerManager;
            appenders = new ArrayList<>();
            useSystemAppenders = true;
        }

        public LoggerBuilder loggerName(String name) {
            Utils.checkNotNull(name);
            this.name = name;
            return this;
        }

        public LoggerBuilder disableSystemAppender() {
            useSystemAppenders = false;
            return this;
        }

        public LoggerBuilder addAppender(Appender appender) {
            appenders.add(appender);
            return this;
        }


        public Logger build() {
            if (Utils.isEmpty(this.name)) {
                throw new DMLoggerExcetion("logger name must not be null,please call loggerName to set name");
            }
            LoggerImpl logger = new LoggerImpl(loggerManager, name);
            for (Appender appender : appenders) {
                logger.addAppender(appender);
            }
            if (!useSystemAppenders) {
                logger.disableSystemAppender();
            }

            loggerManager.loggerList.add(logger);
            return logger;
        }
    }
}
