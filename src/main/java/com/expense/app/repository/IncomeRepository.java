package com.expense.app.repository;

import com.expense.app.dto.TransactionDto;
import com.expense.app.entity.ExpenseEntity;
import com.expense.app.entity.IncomeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<IncomeEntity, Integer> {
    //public List<IncomeEntity> findByUserId(Integer userId);

    public List<IncomeEntity> findByUser_AuthId(String authId);

    @Query("SELECT i from IncomeEntity i where i.user.authId = :userId")
    List<IncomeEntity> findByUserId(@Param("userId") String userId);

    @Query("SELECT SUM(i.amount) FROM IncomeEntity i WHERE i.user.authId = :userId")
    Float findSumOfIncomesByUserId(String userId);

    @Query("SELECT i.incomeId AS id, i.amount AS amount, c.name AS categoryName, 'Income' AS categoryType, i.date AS date " +
            "FROM IncomeEntity i JOIN i.category c where i.user.authId = :userId")
    List<TransactionDto> findAllIncomesAsTransactions(@Param("userId") String userId);

    @Transactional
    public void deleteByIncomeId(Integer incomeId);
}
