package com.expense.app.service;

import com.expense.app.entity.CategoryEntity;
import com.expense.app.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    public List<CategoryEntity> findAll() {
        return categoryRepository.findAll();
    }
}
