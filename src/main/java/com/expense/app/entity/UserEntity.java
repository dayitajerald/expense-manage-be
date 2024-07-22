package com.expense.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "user")
@JsonIgnoreProperties({"expenses","incomes"})// Ignore expenses field during serialization

public class UserEntity {
    @Id
    private String authId;
    private String name;
    private String email;
    private String phone;

    @CreationTimestamp
    private LocalDateTime created_at;

    @UpdateTimestamp
    private LocalDateTime updated_at;

    @OneToMany(mappedBy = "user")
    private List<ExpenseEntity> expenses;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user",orphanRemoval = true)
    private List<IncomeEntity> incomes ;

}
