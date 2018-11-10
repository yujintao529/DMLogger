package com.demon.yu.dmlogger;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;


public class DailyFileAppender implements Appender, Handler.Callback {


    private FormatStrategy formatStrategy;//格式化策略
    private File logFile;
    private File logPath;

    private Handler handler;

    private DailyFileAppender(FormatStrategy formatStrategy, String filePath, String fileName) {
        this.formatStrategy = formatStrategy;
        this.logPath = new File(filePath);
        this.logFile = new File(logPath, fileName);
        HandlerThread handlerThread = new HandlerThread("DailyFileAppender");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper(), this);
    }


    @Override
    public void log(Level level, String tag, String message) {
        handler.sendMessage(handler.obtainMessage(0, message));
    }

    @Override
    public boolean filter(Object tag) {
        return true;
    }

    @Override
    public void append(MessageEntry messageEntry, Logger logger) {
        formatStrategy.layout(messageEntry, logger, this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        checkFileDaily();
        String message = (String) msg.obj;
        File destFile = getFile();
        BufferedWriter bufferedWriter = null;
        try {
            FileWriter fileWriter = new FileWriter(destFile, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            fileWriter.write(message);
            fileWriter.write("\n");
            fileWriter.flush();
        } catch (IOException e) {
        } finally {
            Utils.closeWriter(bufferedWriter);
        }
        return false;
    }


    private void checkFileDaily() {
        File temp = getFile();
        Date currentDay = new Date();
        long lastTime = temp.lastModified();
        Date fileDate = new Date(lastTime);
        if (Utils.isSameDay(currentDay, fileDate)) {
            return;
        }
        temp.renameTo(new File(temp.getParentFile(), Utils.createDailyFileName(fileDate)));
    }


    private File getFile() {
        if (!logPath.exists()) {
            logPath.mkdirs();
        }
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return logFile;
    }

    public static class Builder implements Appender.Builder {
        private FormatStrategy formatStrategy;

        private String filePath;
        private String fileName;

        public Builder withFormatStrategy(FormatStrategy formatStrategy) {
            Utils.checkNotNull(formatStrategy);
            this.formatStrategy = formatStrategy;
            return this;
        }

        public Builder withFileName(String fileName) {
            Utils.checkNotNull(fileName);
            this.fileName = fileName;
            return this;
        }

        public Builder withFilePath(String filePath) {
            Utils.checkNotNull(filePath);
            this.filePath = filePath;
            return this;
        }

        @Override
        public Appender build() {
            if (formatStrategy == null) {
                formatStrategy = new TTCCFormatStrategy();
            }
            if (fileName == null) {
                fileName = "log.txt";
            }
            if (filePath == null) {
                filePath = "/";
            }
            return new DailyFileAppender(formatStrategy, filePath, fileName);
        }

    }

    public static Appender defaultFileAppender() {
        return new Builder().build();
    }

}

