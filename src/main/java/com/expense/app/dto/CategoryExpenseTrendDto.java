package com.expense.app.dto;

import lombok.Data;

@Data
public class CategoryExpenseTrendDto {
    private String categoryName;
    private int month;
    private double totalAmount;

    public CategoryExpenseTrendDto(String categoryName, int month, double totalAmount) {
        this.categoryName = categoryName;
        this.month = month;
        this.totalAmount = totalAmount;
    }

}
