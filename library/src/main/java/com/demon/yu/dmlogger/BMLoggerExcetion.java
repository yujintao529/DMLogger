package com.demon.yu.dmlogger;

public class BMLoggerExcetion  extends RuntimeException{
    public BMLoggerExcetion() {
    }

    public BMLoggerExcetion(String message) {
        super(message);
    }

    public BMLoggerExcetion(String message, Throwable cause) {
        super(message, cause);
    }
}
