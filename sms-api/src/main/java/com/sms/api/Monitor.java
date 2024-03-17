package com.sms.api;

public interface Monitor {
    void logInfo(String message);
    void logError(String message);
    void logWarning(String message);
    void startTimer(String name);
    void stopTimer(String name);
    // 更多的方法...
}