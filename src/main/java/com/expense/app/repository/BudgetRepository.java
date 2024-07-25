package com.expense.app.repository;

import com.expense.app.dto.BudgetDto;
import com.expense.app.entity.BudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<BudgetEntity,Integer> {

    BudgetEntity findById(int id);

    @Query("SELECT b from BudgetEntity b where b.category.categoryId = :category")
    Optional<BudgetEntity> findByCategoryId(@Param("category") Integer category);

    @Query("SELECT new com.expense.app.dto.BudgetDto(b.budgetId, c.categoryId, c.name, b.amountSpent, b.budgetAmount) " +
            "FROM BudgetEntity b JOIN b.category c " +
            "WHERE b.user.authId = :userId ")
    List<BudgetDto> findBudgets(String userId);

}
