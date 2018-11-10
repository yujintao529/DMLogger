package com.demon.yu.dmlogger;


import android.app.Application;

public class BMLogger {

    private static Logger defaultLogger;


    public static void initDefaultLogger(Application application) {
        LoggerManager.putAppender(LogcatAppender.defaultLogcatAppender());
        FileAppender.Builder builder = new FileAppender.Builder();
        LoggerManager.putAppender(builder.withFilePath(application.getFilesDir().getAbsolutePath() + "/logs").build());
        LoggerManager.LoggerBuilder loggerBuilder = LoggerManager.newLoggerBuilder();
        loggerBuilder.loggerName("BMLogger");
        defaultLogger = loggerBuilder.build();
    }

    public static Logger getDefaultLogger() {
        if (defaultLogger == null) {
            throw new BMLoggerExcetion("defaultLogger not initialization");
        }
        return defaultLogger;
    }

}
