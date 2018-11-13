package com.demon.yu.dmlogger;

import android.app.Application;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BMLogger.initDefaultLogger(this);
    }
}
