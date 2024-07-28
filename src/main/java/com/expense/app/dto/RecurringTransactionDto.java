package com.expense.app.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RecurringTransactionDto {
    private Float amount;
    private Integer categoryId;
    private LocalDate startDate;
    private String period; // daily, weekly, monthly
    private String description;
    private String type; // expense or income

    // Getters and Setters
}
