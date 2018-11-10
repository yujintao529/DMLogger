package com.demon.yu.dmlogger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 100; i++) {
            BMLogger.getDefaultLogger().debug(this.getClass().getSimpleName(), "test" + i);
        }

        //自定义一个
        LogcatAppender.Builder builder = new LogcatAppender.Builder();
        builder.withFormatStrategy(new PrettyFormatStrategy());
        Logger logger = LoggerManager.newLoggerBuilder().disableSystemAppders().loggerName("self logger").addAppender(builder.build()).build();
        logger.debug(getClass().getSimpleName(), "test logger");
    }
}
