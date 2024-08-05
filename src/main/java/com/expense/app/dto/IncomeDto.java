package com.expense.app.dto;

import lombok.Data;

@Data
public class IncomeDto {
    private Integer incomeId;
    private Float amount;
    private Integer category;
    private String date;
}
