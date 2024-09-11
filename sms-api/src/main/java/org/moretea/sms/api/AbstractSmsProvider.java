package org.moretea.sms.api;

public abstract class AbstractSmsProvider implements SmsProvider {
    private boolean available = true;

    @Override
    public boolean isAvailable() {
        return available;
    }
    
    @Override
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
}