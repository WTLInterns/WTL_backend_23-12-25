package com.workshop.WhatsAppPackage;


import com.fasterxml.jackson.annotation.JsonProperty;

public class WhatsAppMessageRequest {

    @JsonProperty("messaging_product")
    private String messagingProduct;

    @JsonProperty("to")
    private String to;

    @JsonProperty("type")
    private String type;

    @JsonProperty("text")
    private WhatsAppText text;

    // Constructors
    public WhatsAppMessageRequest() {}

    // Getters and Setters
    public String getMessagingProduct() {
        return messagingProduct;
    }

    public void setMessagingProduct(String messagingProduct) {
        this.messagingProduct = messagingProduct;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public WhatsAppText getText() {
        return text;
    }

    public void setText(WhatsAppText text) {
        this.text = text;
    }
}
