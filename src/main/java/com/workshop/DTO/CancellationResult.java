package com.workshop.DTO;

public class CancellationResult {
    private boolean allowed;
    private String message;
    private String status;

    public CancellationResult(boolean allowed, String message, String status) {
        this.allowed = allowed;
        this.message = message;
        this.status = status;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
