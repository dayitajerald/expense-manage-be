package com.expense.app.dto;

import lombok.Data;

@Data
public class ExpenseDto {
    private String expenseId;
    private String userId;
    private String userName;
    private Float amount;
    private String description;
    private String date;
    private String receipt;
}
