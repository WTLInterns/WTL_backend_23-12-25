package com.workshop.WhatsAppPackage;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WhatsAppResponse {
    @JsonProperty("messaging_product")
    private String messagingProduct;

    @JsonProperty("contacts")
    private List<WhatsAppContact> contacts;

    @JsonProperty("messages")
    private List<WhatsAppMessage> messages;

    public WhatsAppResponse() {}

    public String getMessagingProduct() {
        return messagingProduct;
    }

    public void setMessagingProduct(String messagingProduct) {
        this.messagingProduct = messagingProduct;
    }

    public List<WhatsAppContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<WhatsAppContact> contacts) {
        this.contacts = contacts;
    }

    public List<WhatsAppMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<WhatsAppMessage> messages) {
        this.messages = messages;
    }
}
