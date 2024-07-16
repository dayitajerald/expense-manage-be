package com.expense.app.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private int status;
    private String message;
    private String username;
    private String password;
    private Integer role;
    private String name;
    private String email;
    private String phone;

}
