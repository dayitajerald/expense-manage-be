package com.expense.app.repository;

import com.expense.app.entity.RecurringTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecurringTransactionRepository extends JpaRepository<RecurringTransactionEntity, Integer> {

    @Query("SELECT r from RecurringTransactionEntity r where r.user.authId = :userId")
    List<RecurringTransactionEntity> findByUserId(@Param("userId") String userId);
}
