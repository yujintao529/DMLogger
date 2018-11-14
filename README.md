# DMLogger
## 前言

DMLogger是一个简单，可高度定制化的android日志库，可用于解决团队人员日志混乱，查找不易等问题。

## 特点
1. 接入方便，默认支持logcat，文件日志输出。
2. 高度定制化，可配置日志格式，输出方式，同时可以特殊日志进行单独logger配置。
3. （下个版本）暂停其他logger的日志输出，专注自己的内容

## 使用

### 引入
```groovy
implementation 'com.demon.yu:DMLogger:1.0.0'
```
### 默认配置

DMLogger默认初始化了一套基本可以满足大部分项目的logger，使用方式只需要在Application的oncreate的时候初始化一下
```java
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BMLogger.initDefaultLogger(this);
    }
}

//使用
BMLogger.getDefaultLogger().debug/info/warn/error()
```
默认logger使用了两个appender，LogcatAppender和FileAppender，其中fileAppender默认的日志文件存储位置为：``/data/data/[package]/files/logs/``下面。

### 定制logger

> 定制logger使用不需要调用``BMLogger.initDefaultLogger(this)``

目前版本一共有三个appender，FileAppender,DailyFileAppender,logcatAppender。
三个FormatStrategy，TTCCFormatStrategy,LocateFormatStrategy,PrettyFormatStrategy。
一般情况下，可以随意组合，但是最好是logcatAppender与LocateFormatStrategy，PrettyFormatStrategy组合，FileAppender，DailyFileAppender和TTCCFormatStrategy组合。

#### 设置系统全局的appender
全局appender所有logger共享，可以设定logger跳过全局appender

```java
LoggerManager.putAppender(LogcatAppender.defaultLogcatAppender());//设置默认logcatAppender
//或者自定义LogcatAppender
LogcatAppender.Builder builder = new LogcatAppender.Builder();
PrettyFormatStrategy prettyFormatStrategy=new PrettyFormatStrategy();
prettyFormatStrategy.setMethodCallStack(2);//堆栈显示深度
prettyFormatStrategy.setThreadInfo(true);//是否显示线程
builder.withFormatStrategy(prettyFormatStrategy);//设定FormatStrategy
LoggerManager.putAppender(builder.build());
```


#### 初始化logger

```java
LoggerManager.LoggerBuilder loggerBuilder = LoggerManager.newLoggerBuilder();
loggerBuilder.loggerName("base_logger");
//loggerBuilder.disableSystemAppender();//跳过系统默认appender
DailyFileAppender.Builder builder = new DailyFileAppender.Builder();
builder.withFilePath(getApplication().getFilesDir().getAbsolutePath() + "/logs");//路径
builder.withFileName("dailylog.txt");//日志文件名
loggerBuilder.addAppender(builder.build());//logger单独的appender
Logger logger = loggerBuilder.build();
logger.debug(MainActivity.class.getSimpleName(), "我要测试呀");
```

#### 输出效果

logcat:
```
2018-11-14 18:14:40.036 6978-6978/com.demon.yu.dmlogger D/MainActivity:
    thread - main
    我要测试呀
        at com.demon.yu.dmlogger.MainActivity$2.onClick()(MainActivity.java:40)
        at android.view.View.performClick()(View.java:6329)
    ---------------------------------------------------------------------------

```
dailyFile:
```
2018-11-14 18:04:48.095 [DEBUG] MainActivity$3:onClick():54 - [MainActivity] 我要测试呀
```

## 结构图
结构图直接放在了[processon](https://www.processon.com/view/link/5bd81b82e4b0049901c8ed8e)上了

![屏幕快照 2018-11-14 下午6.29.57.png](https://i.loli.net/2018/11/14/5bebf930090b5.png)


## 总结

DMLogger除了方便使用外，更重要的是高可定制化，不仅解决网络日志，业务日志等等全都在同一个日志文件中不容易查看问题。而且也可以解决同组开发人员日志混乱等问题。

## License
<pre>
Copyright 2018 yujintao

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>
