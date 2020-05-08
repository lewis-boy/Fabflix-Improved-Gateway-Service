package edu.uci.ics.luisae.service.gateway.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GatewayResponse {
    @JsonProperty(value = "transaction_id", required = true)
    private String transaction_id;
    @JsonProperty(value = "response", required = true)
    private String response;
    @JsonProperty(value = "http_status", required = true)
    private int http_status;

    public GatewayResponse(){}

    public GatewayResponse(@JsonProperty(value = "transaction_id", required = true) String transaction_id,
                           @JsonProperty(value = "response", required = true) String response,
                           @JsonProperty(value = "http_status", required = true) int http_status){
        this.transaction_id = transaction_id;
        this.response = response;
        this.http_status = http_status;
    }

    public int getHttp_status() {
        return http_status;
    }

    public void setHttp_status(int http_status) {
        this.http_status = http_status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }
}
