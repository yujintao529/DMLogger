package com.demon.yu.dmlogger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.defaultLogger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (DMLogger.getDefaultLogger() == null) {
                    DMLogger.initDefaultLogger(getApplication());
                }
                //尽量直接调用，DMLogger.getDefaultLogger().debug（），如果在包一层堆栈信息会尴尬的～
                // 堆栈默认打印的 DMLogger.getDefaultLogger().debug的调用处
                for (int i = 0; i < 10; i++) {
                    DMLogger.getDefaultLogger().debug(MainActivity.class.getSimpleName(), "test" + i);
                }
            }
        });

        findViewById(R.id.customizeLogger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义一个
                LogcatAppender.Builder builder = new LogcatAppender.Builder();
                PrettyFormatStrategy prettyFormatStrategy = new PrettyFormatStrategy();
                prettyFormatStrategy.setMethodCallStack(2);
                prettyFormatStrategy.setThreadInfo(true);
                builder.withFormatStrategy(prettyFormatStrategy);
                Logger logger = LoggerManager.newLoggerBuilder().disableSystemAppender().loggerName("self logger").addAppender(builder.build()).build();
                logger.debug(MainActivity.class.getSimpleName(), "test logger");
            }
        });
        findViewById(R.id.customizeWithFileLogger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoggerManager.LoggerBuilder loggerBuilder = LoggerManager.newLoggerBuilder();
                loggerBuilder.loggerName("base_logger");
                loggerBuilder.disableSystemAppender();//跳过系统默认appender
                DailyFileAppender.Builder builder = new DailyFileAppender.Builder();
                builder.withFilePath(getApplication().getFilesDir().getAbsolutePath() + "/logs");
                builder.withFileName("dailylog.txt");
                loggerBuilder.addAppender(builder.build());
                Logger logger = loggerBuilder.build();
                logger.debug(MainActivity.class.getSimpleName(), "我要测试呀");
                logger.error(MainActivity.class.getSimpleName(),new RuntimeException("测试异常"),"抛出异常");
            }
        });


    }
}
