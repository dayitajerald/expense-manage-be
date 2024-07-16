package com.expense.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
//import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user")
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
}
