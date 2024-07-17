package com.expense.app.repository;

import com.expense.app.entity.IncomeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<IncomeEntity, String> {
    public List<IncomeEntity> findByUserId(String userId);
}
