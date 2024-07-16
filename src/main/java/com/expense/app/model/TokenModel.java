package com.expense.app.model;

import lombok.Data;

@Data
public class TokenModel {
    private String id;
    private Integer role;

    public TokenModel() {

    }

    public TokenModel(String id, Integer role) {
        this.id = id;
        this.role = role;
    }

    public TokenModel(String token) {
        parse(token);
    }

    public String toString() {
        return String.format(id + "=" + role);
    }

    public void parse(String str) {
        this.id = str.split("=")[0];
        this.role = Integer.parseInt(str.split("=")[1]);
    }
}
