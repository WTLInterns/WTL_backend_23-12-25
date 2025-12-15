package com.workshop.WhatsAppPackage;


public class WhatsAppServiceException extends RuntimeException {
    public WhatsAppServiceException(String message) {
        super(message);
    }

    public WhatsAppServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
