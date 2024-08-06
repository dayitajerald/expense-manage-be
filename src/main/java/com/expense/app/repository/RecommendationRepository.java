package com.expense.app.repository;

import com.expense.app.dto.CategoryExpenseSumDto;
import com.expense.app.dto.Recommendation;
import com.expense.app.entity.RecommendationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<RecommendationEntity, Integer> {

    @Query("SELECT new com.expense.app.dto.Recommendation(r.category,r.suggestion,r.example) " +
            "FROM RecommendationEntity r ")
    List<Recommendation> findRecommendationFromTable();

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE recommendation", nativeQuery = true)
    public void truncateTable();

}
