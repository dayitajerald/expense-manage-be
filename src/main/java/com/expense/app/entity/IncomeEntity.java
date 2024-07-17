package com.expense.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "income")
public class IncomeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer incomeId;
    private String userId;
    private Float amount;
    private String source;
    private String description;
    private LocalDate date;

    @CreationTimestamp
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;


}
