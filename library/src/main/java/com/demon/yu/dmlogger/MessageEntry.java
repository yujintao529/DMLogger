package com.demon.yu.dmlogger;


public class MessageEntry {


    public static final Object syncObject = new Object();

    private static MessageEntry sCurrentMessage;

    private static volatile int sPoolSize = 0;

    private static final int MAX_POOL_SIZE = 50;


    private MessageEntry() {
    }





    Level level;

    Object tag;

    String content;

    Throwable throwable;

    Thread thread;

    StackTraceElement[] stackTraceElements;

    private MessageEntry next;

    void recycle() {
        level = null;
        tag = null;
        content = null;
        thread = null;
        throwable = null;
        stackTraceElements = null;
        synchronized (syncObject) {
            if (sPoolSize < MAX_POOL_SIZE) {
                sPoolSize++;
                next = sCurrentMessage;
                sCurrentMessage = this;
            }
        }
    }
    static MessageEntry obtain() {
        synchronized (syncObject) {
            if (sCurrentMessage != null) {
                MessageEntry messageEntry = sCurrentMessage;
                sCurrentMessage = sCurrentMessage.next;
                sPoolSize--;
                return messageEntry;
            }
        }
        return new MessageEntry();
    }
}
