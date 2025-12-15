package com.workshop.WhatsAppPackage;


import com.fasterxml.jackson.annotation.JsonProperty;

class WhatsAppText {

    @JsonProperty("body")
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
