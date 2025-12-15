package com.workshop.WhatsAppPackage;


import com.fasterxml.jackson.annotation.JsonProperty;

class WhatsAppContact {
    @JsonProperty("input")
    private String input;

    @JsonProperty("wa_id")
    private String waId;

    public WhatsAppContact() {}

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getWaId() {
        return waId;
    }

    public void setWaId(String waId) {
        this.waId = waId;
    }
}

