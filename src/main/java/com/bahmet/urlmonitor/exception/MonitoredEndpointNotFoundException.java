package com.bahmet.urlmonitor.exception;

public class MonitoredEndpointNotFoundException extends RuntimeException {
    public MonitoredEndpointNotFoundException(String message) {
        super(message);
    }
}
