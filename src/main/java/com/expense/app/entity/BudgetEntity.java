package com.expense.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "budget")
@JsonIgnoreProperties({"user","category"})
public class BudgetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer budgetId;
    private Float budgetAmount;
    private LocalDate date;
    private Float amountSpent;

    @ManyToOne()
    @JoinColumn(name = "category_id", referencedColumnName = "categoryId")
    private CategoryEntity category;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "authId")
    private UserEntity user;

}
