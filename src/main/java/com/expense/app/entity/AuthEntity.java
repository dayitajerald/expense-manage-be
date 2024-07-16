package com.expense.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

@Data
@Entity
@Table(name = "auth")
public class AuthEntity {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private String id;
    private String username;
    private String password;
    private int role;
}
