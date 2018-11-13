package com.demon.yu.dmlogger;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.ArrayList;
import java.util.List;

public class LoggerContextImpl implements LoggerContext {


    private Handler contextHandler;

    public LoggerContextImpl() {
        HandlerThread handlerThread = new HandlerThread("LoggerContextThread");
        handlerThread.start();
        contextHandler = new Handler(handlerThread.getLooper());
    }

    private List<Appender> appenders = new ArrayList<>();

    @Override
    public void print(MessageEntry messageEntry, Logger logger) {
        boolean enableAppendrs = true;
        if (logger instanceof LoggerImpl) {
            LoggerImpl loggerImpl = (LoggerImpl) logger;
            for (Appender appender : loggerImpl.getAppenders()) {
                appender.append(messageEntry, logger);
            }
            enableAppendrs = loggerImpl.isEnableSystemAppender();
        }
        if (enableAppendrs) {
            for (Appender appender : appenders) {
                if (appender.filter(messageEntry.tag)) {
                    appender.append(messageEntry, logger);
                }
            }
        }


        messageEntry.recycle();
    }

    @Override
    public void addAppender(Appender appender) {
        if (!appenders.contains(appender)) {
            appenders.add(appender);
        }
    }

    @Override
    public Handler logHandler() {
        return contextHandler;
    }


}
