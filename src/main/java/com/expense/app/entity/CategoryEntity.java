package com.expense.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "category")
@JsonIgnoreProperties({"categories"})
public class CategoryEntity {
    @Id
    private Integer categoryId;
    private String name;
    private String type;


    @OneToMany(mappedBy = "category")
    private List<ExpenseEntity> expenses;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "category",orphanRemoval = true)
    private List<IncomeEntity> incomes ;

}
