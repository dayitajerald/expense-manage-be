package com.expense.app.dto;

import lombok.Data;

@Data
public class PasswordChangeDto {
    private String currentPassword;
    private String newPassword;
    private String message;
    private String status;
}
