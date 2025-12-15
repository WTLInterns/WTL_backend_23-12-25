package com.workshop.WhatsAppPackage;


import com.fasterxml.jackson.annotation.JsonProperty;

class WhatsAppMessage {
    @JsonProperty("id")
    private String id;

    @JsonProperty("message_status")
    private String messageStatus;

    public WhatsAppMessage() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }
}
