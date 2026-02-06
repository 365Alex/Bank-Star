package com.bank.star.star.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceInfoResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("version")
    private String version;

    public ServiceInfoResponse() {}

    public ServiceInfoResponse(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
}