package com.expense.app.dto;

import lombok.Data;

@Data
public class ExpenseDto {
    private Integer expenseId;
    private String userId;
    private String userName;
    private Float amount;
    private Integer category;
    private String description;
    private String date;
    private String receipt;
}
