package com.expense.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "expense")
public class ExpenseEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private String expenseId;
    private String userId;
    private Float amount;
    private String description;
    private LocalDate date;
    private String receipt;

    @CreationTimestamp
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;
}
