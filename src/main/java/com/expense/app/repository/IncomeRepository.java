package com.expense.app.repository;

import com.expense.app.entity.ExpenseEntity;
import com.expense.app.entity.IncomeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<IncomeEntity, Integer> {
    //public List<IncomeEntity> findByUserId(Integer userId);

    public List<IncomeEntity> findByUser_AuthId(String authId);

    @Transactional
    public void deleteByIncomeId(Integer incomeId);
}
