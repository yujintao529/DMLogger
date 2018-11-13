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

                if (BMLogger.getDefaultLogger() == null) {
                    BMLogger.initDefaultLogger(getApplication());
                }
                //尽量直接调用，BMLogger.getDefaultLogger().debug（），如果在包一层堆栈信息会尴尬的～
                // 堆栈默认打印的 BMLogger.getDefaultLogger().debug的调用处
                for (int i = 0; i < 10; i++) {
                    BMLogger.getDefaultLogger().debug(MainActivity.class.getSimpleName(), "test" + i);
                }
            }
        });

        findViewById(R.id.customizeLogger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义一个
                LogcatAppender.Builder builder = new LogcatAppender.Builder();
                PrettyFormatStrategy prettyFormatStrategy=new PrettyFormatStrategy();
                prettyFormatStrategy.setMethodCallStack(2);
                prettyFormatStrategy.setThreadInfo(true);
                builder.withFormatStrategy(prettyFormatStrategy);
                Logger logger = LoggerManager.newLoggerBuilder().disableSystemAppender().loggerName("self logger").addAppender(builder.build()).build();
                logger.debug(MainActivity.class.getSimpleName(), "test logger");
            }
        });


    }
}
