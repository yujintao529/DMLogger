package com.demon.yu.dmlogger;


import java.util.ArrayList;
import java.util.List;

public class LoggerImpl implements Logger {

    private LoggerContext loggerContext;
    private String name;

    private List<Appender> selfAppenders;

    private boolean enableSystemAppender;

    public LoggerImpl(LoggerContext loggerContext, String name) {
        this.loggerContext = loggerContext;
        this.name = name;
        selfAppenders = new ArrayList<>();
        enableSystemAppender = true;
    }


    private void collect(Level level, Object tag, String message, Throwable throwable, Object... args) {
        MessageEntry messageEntry = MessageEntry.obtain();
        messageEntry.level = level;
        if (args != null && args.length != 0) {
            messageEntry.content = Utils.stringFormat(message, args);
        } else {
            messageEntry.content = message;
        }
        messageEntry.throwable = throwable;
        messageEntry.tag = tag;
        messageEntry.thread = Thread.currentThread();
        messageEntry.stackTraceElements = messageEntry.thread.getStackTrace();
        printMessage(messageEntry);

    }

    protected void addAppender(Appender appender) {
        selfAppenders.add(appender);
    }

    protected List<Appender> getAppenders() {
        return selfAppenders;
    }

    protected boolean isEnableSystemAppender() {
        return enableSystemAppender;
    }


    protected void disableSystemAppender() {
        enableSystemAppender = false;
    }

    private void printMessage(MessageEntry messageEntry) {
        loggerContext.print(messageEntry, this);
    }

    @Override
    public void debug(Object tag, String message) {
        collect(Level.DEBUG, tag, message, null);
    }

    @Override
    public void debug(Object tag, String message, Object... args) {
        collect(Level.DEBUG, tag, message, null, args);
    }

    @Override
    public void info(Object tag, String message) {
        collect(Level.INFO, tag, message, null);
    }

    @Override
    public void info(Object tag, String message, Object... args) {
        collect(Level.INFO, tag, message, null, args);
    }

    @Override
    public void warn(Object tag, String message) {
        collect(Level.WARN, tag, message, null);
    }

    @Override
    public void warn(Object tag, String message, Object... args) {
        collect(Level.WARN, tag, message, null, args);
    }

    @Override
    public void error(Object tag, Throwable throwable, String message) {
        collect(Level.ERROR, tag, message, throwable);
    }

    @Override
    public void error(Object tag, Throwable throwable, String message, Object... args) {
        collect(Level.ERROR, tag, message, throwable, args);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public LoggerContext getContext() {
        return loggerContext;
    }
}
