package com.demon.yu.dmlogger;

import android.support.annotation.NonNull;
import android.util.Log;

public class PrettyFormatStrategy implements FormatStrategy {


    private static final String SPLIT_LINE = "---------------------------------------------------------------------------";

    private boolean threadInfo = false;
    private int methodCallStack = 0;


    public PrettyFormatStrategy() {

    }

    @Override
    public void layout(MessageEntry messageEntry, Logger logger, LogAdapter logAdapter) {
        final StringBuilder stringBuilder = new StringBuilder(" \n");
        if (threadInfo) {
            stringBuilder.append(createThreadInfo(messageEntry.thread));
            stringBuilder.append("\n");
        }
        stringBuilder.append(messageEntry.content);
        stringBuilder.append("\n");
        if (messageEntry.throwable != null) {
            stringBuilder.append(Log.getStackTraceString(messageEntry.throwable));
            stringBuilder.append("\n");
        }
        int stackCount = methodCallStack;
        int stackOffset = getStackOffset(messageEntry.stackTraceElements);
        collectStack(stringBuilder, messageEntry.stackTraceElements, stackOffset, stackCount);
        stringBuilder.append(SPLIT_LINE);
        logAdapter.log(messageEntry.level, convertTag(messageEntry.tag), stringBuilder.toString());
    }


    private int getStackOffset(@NonNull StackTraceElement[] trace) {
        Utils.checkNotNull(trace);
        for (int i = trace.length-1; i > 0; i--) {
            StackTraceElement e = trace[i];
            if (e.getClassName().equals(LoggerImpl.class.getName())) {
                return i;
            }
        }
        return 0;
    }

    private static void collectStack(StringBuilder stringBuilder, StackTraceElement[] stackTraceElements, int stackOffset, int stackCount) {
        stackCount = Math.min(stackTraceElements.length - stackOffset - 1, stackCount);
        for (int i = stackOffset + 1; i < stackCount + stackOffset + 1; i++) {
            StackTraceElement stackTraceElement = stackTraceElements[i];
            stringBuilder.append("at " + stackTraceElement.getClassName()).append(".").append(stackTraceElement.getMethodName())
                    .append("()(").append(stackTraceElement.getFileName()).append(":").append(stackTraceElement.getLineNumber()).append(")");
            stringBuilder.append("\n");
        }
    }


    private String convertTag(Object o) {
        return o.toString();
    }

    private String createThreadInfo(Thread thread) {
        return "thread - " + thread.getName();
    }


    public void setThreadInfo(boolean threadInfo) {
        this.threadInfo = threadInfo;
    }

    public void setMethodCallStack(int methodCallStack) {
        this.methodCallStack = methodCallStack;
    }


}
