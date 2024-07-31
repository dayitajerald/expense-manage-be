package com.expense.app.repository;

import com.expense.app.dto.CategoryExpenseSumDto;
import com.expense.app.dto.CategoryExpenseTrendDto;
import com.expense.app.dto.TransactionDto;
import com.expense.app.entity.ExpenseEntity;
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

    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE EXTRACT( YEAR_MONTH FROM e.date ) = :month AND e.user.authId = :userId")
    double getTotalAmountByMonth(@Param("month") String month, String userId);

    @Query("SELECT e.expenseId AS id, e.amount AS amount, c.name AS categoryName, 'Expense' AS categoryType, e.date AS date " +
            "FROM ExpenseEntity e JOIN e.category c where e.user.authId = :userId")
    List<TransactionDto> findAllExpensesAsTransactions(@Param("userId") String userId);

    @Query("SELECT MONTH(e.date) as month, SUM(e.amount) as totalExpense FROM ExpenseEntity e WHERE e.user.authId = :userId GROUP BY MONTH(e.date)")
    List<Object[]> findMonthlyExpenses(@Param("userId") String userId);

    @Query("SELECT DATE_FORMAT(e.date, '%Y-%m') AS month, SUM(e.amount) FROM ExpenseEntity e " +
            "WHERE e.category.categoryId = :categoryId AND e.user.authId = :userId " +
            "GROUP BY DATE_FORMAT(e.date, '%Y-%m')")
    List<Object[]> findMonthlyExpensesByCategory(@Param("categoryId") Integer categoryId, @Param("userId") String userId);

    @Query("SELECT e FROM ExpenseEntity e WHERE e.user.authId = :userId AND e.date BETWEEN :startDate AND :endDate")
    List<ExpenseEntity> findExpensesByUserIdAndDateBetween(@Param("userId") String userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT e FROM ExpenseEntity e WHERE e.user.authId = :userId AND e.date = :date")
    List<ExpenseEntity> findByUserIdAndDate(@Param("userId") String userId, @Param("date") LocalDate date);

    @Transactional
    public void deleteByExpenseId(Integer expenseId);



}
