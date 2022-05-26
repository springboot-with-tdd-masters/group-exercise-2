package com.advancejava.groupexercise1.model;

public class AccountRequest {

    private String name;

    private String type;

    public AccountRequest() {

    }

    public AccountRequest(String name, String type) {
        this.name = name;
        this.type = type;
    }

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
}
