package com.expense.app.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseDto {
    private Integer expenseId;
    private Float amount;
    private Integer category;
    private String description;
    private LocalDate date;
}
