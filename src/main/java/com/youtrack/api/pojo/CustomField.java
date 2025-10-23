package com.youtrack.api.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomField {

    @JsonProperty("name")
    private String name;

    @JsonProperty("$type")
    private String type;

    @JsonProperty("value")
    private Object value;

    // Constructors
    public CustomField() {}

    public CustomField(String name, String type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
