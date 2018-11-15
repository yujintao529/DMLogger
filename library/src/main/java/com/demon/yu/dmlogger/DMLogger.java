package com.demon.yu.dmlogger;


import android.app.Application;

public class DMLogger {

    private static Logger defaultLogger;


    public static void initDefaultLogger(Application application) {
        LoggerManager.putAppender(LogcatAppender.defaultLogcatAppender());
        FileAppender.Builder builder = new FileAppender.Builder();
        LoggerManager.putAppender(builder.withFilePath(application.getFilesDir().getAbsolutePath() + "/logs").build());
        LoggerManager.LoggerBuilder loggerBuilder = LoggerManager.newLoggerBuilder();
        loggerBuilder.loggerName("DMLogger");
        defaultLogger = loggerBuilder.build();
    }

    public static Logger getDefaultLogger() {
        if (defaultLogger == null) {
            throw new DMLoggerExcetion("defaultLogger not initialization");
        }
        return defaultLogger;
    }

}
