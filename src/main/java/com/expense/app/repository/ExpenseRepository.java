package com.expense.app.repository;

import com.expense.app.dto.CategoryExpenseSumDto;
import com.expense.app.dto.CategoryExpenseTrendDto;
import com.expense.app.entity.CategoryEntity;
import com.expense.app.entity.ExpenseEntity;
import com.expense.app.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {
    //public List<ExpenseEntity> findByUserId(String userId);

    public List<ExpenseEntity> findByUser_AuthId(String authId);

    ExpenseEntity findByExpenseId(Integer expenseId);

    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.user.authId = :userId")
    Float findSumOfExpensesByUserId(String userId);

    @Query("SELECT e from ExpenseEntity e where e.user.authId = :userId")
    List<ExpenseEntity> findByUserId(@Param("userId") String userId);

    @Query("SELECT new com.expense.app.dto.CategoryExpenseSumDto(c.name, SUM(e.amount)) " +
            "FROM ExpenseEntity e JOIN e.category c " +
            "WHERE e.user.authId = :userId " +
            "GROUP BY c.name")
    List<CategoryExpenseSumDto> findSumOfAmountByCategoryForUser(String userId);


    @Query("SELECT new com.expense.app.dto.CategoryExpenseTrendDto(e.category.name, MONTH(e.date), SUM(e.amount)) " +
            "FROM ExpenseEntity e WHERE e.user.authId = :userId AND e.date BETWEEN :startDate AND :endDate " +
            "GROUP BY e.category.name, MONTH(e.date)")
    List<CategoryExpenseTrendDto> findCategoryExpenseTrendsForUser(String userId, LocalDate startDate, LocalDate endDate);

    @Transactional
    public void deleteByExpenseId(Integer expenseId);



}
