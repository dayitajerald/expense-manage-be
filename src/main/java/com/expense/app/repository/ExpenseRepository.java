package com.expense.app.repository;

import com.expense.app.entity.ExpenseEntity;
import com.expense.app.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {
    //public List<ExpenseEntity> findByUserId(String userId);

    public List<ExpenseEntity> findByUser_AuthId(String authId);

    @Transactional
    public void deleteByExpenseId(Integer expenseId);

}
