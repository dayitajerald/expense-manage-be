package com.expense.app.repository;

import com.expense.app.entity.ExpenseEntity;
import com.expense.app.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {
    //public List<ExpenseEntity> findByUserId(String userId);

    public List<ExpenseEntity> findByUser_AuthId(String authId);

    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.user.authId = :userId")
    Float findSumOfExpensesByUserId(String userId);

    @Query("SELECT e from ExpenseEntity e where e.user.authId = :userId")
    List<ExpenseEntity> findByUserId(@Param("userId") String userId);
    @Transactional
    public void deleteByExpenseId(Integer expenseId);

}
