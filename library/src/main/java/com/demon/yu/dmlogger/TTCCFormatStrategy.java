package com.demon.yu.dmlogger;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 使用标准的log文件格式
 * 2018-06-09 00:48:31,849 INFO LogAspect:methodName():65 - [TAG] message
 */
public class TTCCFormatStrategy implements FormatStrategy {

    private static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);


    @Override
    public void layout(MessageEntry messageEntry, Logger logger, LogAdapter logAdapter) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SIMPLE_DATE_FORMAT.format(new Date())).append(" [");
        stringBuilder.append(messageEntry.level.name).append("] ");
        StackTraceElement stackTraceElement = getCaller(messageEntry.stackTraceElements);
        if (stackTraceElement != null) {
            String qualifiedName = stackTraceElement.getClassName();
            String[] strings = qualifiedName.split("\\.");
            if (strings.length == 0) {
                stringBuilder.append(qualifiedName);
            } else {
                stringBuilder.append(strings[strings.length - 1]);
            }
            stringBuilder.append(":").append(stackTraceElement.getMethodName()).append("()");
            stringBuilder.append(":").append(stackTraceElement.getLineNumber());
        }
        stringBuilder.append(" - ").append("[").append(getTag(messageEntry.tag)).append("]").append(" ");
        stringBuilder.append(messageEntry.content);
        if (messageEntry.throwable != null) {
            stringBuilder.append("\n");
            stringBuilder.append(Log.getStackTraceString(messageEntry.throwable));
        }
        logAdapter.log(messageEntry.level, getTag(messageEntry.tag), stringBuilder.toString());
    }

    private String getTag(Object o) {
        return o.toString();
    }

    private StackTraceElement getCaller(StackTraceElement[] stackTraceElements) {
        for (int i = stackTraceElements.length - 1; i >= 0; i--) {
            if (stackTraceElements[i].getClassName().equals(LoggerImpl.class.getName())) {
                return stackTraceElements[i + 1];
            }
        }
        return null;
    }
}
