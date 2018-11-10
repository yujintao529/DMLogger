package com.demon.yu.dmlogger;

import android.util.Log;

public class LogcatFormatStrategy implements FormatStrategy {


    public LogcatFormatStrategy() {

    }

    @Override
    public void layout(MessageEntry messageEntry, Logger logger, LogAdapter logAdapter) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(messageEntry.content);
        if(messageEntry.throwable!=null){
            stringBuilder.append("\n");
            stringBuilder.append(Log.getStackTraceString(messageEntry.throwable));
        }
        logAdapter.log(messageEntry.level,convertTag(messageEntry.tag),stringBuilder.toString());
    }

    private String convertTag(Object o){
        return o.toString();
    }




}
