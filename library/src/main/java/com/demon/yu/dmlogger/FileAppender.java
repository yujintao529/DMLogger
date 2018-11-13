package com.demon.yu.dmlogger;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class FileAppender implements Appender, Handler.Callback {


    private FormatStrategy formatStrategy;
    private File logFile;
    private File logPath;
    private long maxLength = 4 * 1024 * 1024 * 8;//4m

    private Handler handler;

    private FileAppender(FormatStrategy formatStrategy, String filePath, String fileName) {
        this.formatStrategy = formatStrategy;
        this.logPath = new File(filePath);
        this.logFile = new File(logPath, fileName);
        HandlerThread handlerThread = new HandlerThread("FileAppender");
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


    public void setFileMaxSize(int maxSize) {
        maxLength = maxSize;
    }

    @Override
    public boolean handleMessage(Message msg) {
        guaranteeFileSize();
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

    private final void guaranteeFileSize() {
        File temp = getFile();
        if (temp.length() >= maxLength) {
            boolean success = temp.renameTo(new File(temp.getParentFile(), Utils.createBakFileName(new Date()) + ".txt"));
            if (!success) {
                temp.deleteOnExit();
            }
        }
    }


    private final File getFile() {
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
        private int fileMaxSize;

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

        public Builder withFileMaxSize(int fileMaxSize) {
            this.fileMaxSize = fileMaxSize;
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
            FileAppender fileAppender = new FileAppender(formatStrategy, filePath, fileName);
            if (fileMaxSize != 0) {
                fileAppender.setFileMaxSize(fileMaxSize);
            }
            return fileAppender;
        }

    }

    public static Appender defaultFileAppender() {
        return new Builder().build();
    }

}

