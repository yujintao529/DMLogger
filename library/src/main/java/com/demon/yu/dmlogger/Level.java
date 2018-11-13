package com.demon.yu.dmlogger;

import android.util.Log;

public enum  Level {
    DEBUG(Log.DEBUG,"DEBUG"),
    INFO(Log.INFO,"INFO"),
    WARN(Log.WARN,"WARN"),
    ERROR(Log.ERROR,"ERROR");
    protected int value;
    protected String name;

    Level(int value, String name) {
        this.value = value;
        this.name = name;
    }
}

