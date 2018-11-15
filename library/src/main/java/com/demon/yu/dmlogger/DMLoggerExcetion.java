package com.demon.yu.dmlogger;

public class DMLoggerExcetion extends RuntimeException{
    public DMLoggerExcetion() {
    }

    public DMLoggerExcetion(String message) {
        super(message);
    }

    public DMLoggerExcetion(String message, Throwable cause) {
        super(message, cause);
    }
}
