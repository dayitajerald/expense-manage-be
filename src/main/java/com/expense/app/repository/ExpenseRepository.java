package com.expense.app.repository;

import com.expense.app.entity.ExpenseEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {
    public List<ExpenseEntity> findByUserId(String userId);

    @Transactional
    public void deleteByExpenseId(Integer expenseId);

}
