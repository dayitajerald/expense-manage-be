package com.expense.app.dto;

import lombok.Data;

@Data
public class IncomeDto {
    private Integer incomeId;
    private String userId;
    private String userName;
    private Float amount;
    private Integer category;
    private String date;
}
