package com.expense.app.dto;

import lombok.Data;

@Data
public class AuthDto {
    private String token;
    private int role;
    private int status;
    private String message;
}
