package com.expense.app.dto;

import lombok.Data;

@Data
public class PercentageChangeDto {
    private double currentMonthTotal;
    private double previousMonthTotal;
    private double percentageChange;
}
