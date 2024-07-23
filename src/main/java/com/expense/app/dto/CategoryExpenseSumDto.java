package com.expense.app.dto;

import lombok.Data;

@Data
public class CategoryExpenseSumDto {
    private String categoryName;
    private Double totalAmount;

    public CategoryExpenseSumDto(String categoryName, Double totalAmount) {
        this.categoryName = categoryName;
        this.totalAmount = totalAmount;
    }
}
