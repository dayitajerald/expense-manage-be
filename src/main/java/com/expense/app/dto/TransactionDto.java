package com.expense.app.dto;

import java.time.LocalDate;

public interface TransactionDto {
    Integer getId();
    Float getAmount();
    String getCategoryName();
    String getCategoryType();
    LocalDate getDate();
}
