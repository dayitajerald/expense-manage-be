package com.expense.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "expense")
@JsonIgnoreProperties({"user","category"}) // Ignore user field during serialization
public class ExpenseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer expenseId;
    private Float amount;
    private String description;
    private LocalDate date;

    @CreationTimestamp
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "authId")
    private UserEntity user;

    @ManyToOne()
    @JoinColumn(name = "category_id", referencedColumnName = "categoryId")
    private CategoryEntity category;

    public CategoryEntity getCategory() {
        return category;
    }
}
