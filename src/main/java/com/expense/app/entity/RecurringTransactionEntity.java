package com.expense.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDate;

@Data
@Entity
@Table(name = "recurringtransactions")
@JsonIgnoreProperties({"user","category"})
public class RecurringTransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recurringTransactionId;
    private Float amount;
    private LocalDate startDate;
    private String period; // daily, weekly, monthly
    private String description;
    private String type; // expense or income

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "authId")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "categoryId")
    private CategoryEntity category;
}
