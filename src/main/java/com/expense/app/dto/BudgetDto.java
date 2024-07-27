package com.expense.app.dto;

import lombok.Data;

@Data
public class BudgetDto {
    private Integer budgetId;
    private Integer categoryId;
    private String categoryName;
    private Float amountSpent;
    private Float budgetAmount;

    public BudgetDto(Integer budgetId, Integer categoryId, String categoryName, Float amountSpent, Float budgetAmount) {
        this.budgetId = budgetId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.amountSpent = amountSpent;
        this.budgetAmount = budgetAmount;
    }
    public BudgetDto() {}

    public BudgetDto(Integer categoryId, String categoryName, Float amountSpent, Float budgetAmount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.amountSpent = amountSpent;
        this.budgetAmount = budgetAmount;
    }
}
