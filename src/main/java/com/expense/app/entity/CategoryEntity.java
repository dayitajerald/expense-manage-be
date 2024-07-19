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
    private int categoryId;
    private String name;
    private String type;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "category",orphanRemoval = true)
    private List<CategoryEntity> categories = new ArrayList<>();

}
