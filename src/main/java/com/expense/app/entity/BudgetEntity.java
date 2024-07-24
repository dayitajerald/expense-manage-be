package com.expense.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "budget")
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
}
