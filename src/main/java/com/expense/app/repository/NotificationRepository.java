package com.expense.app.repository;

import com.expense.app.entity.ExpenseEntity;
import com.expense.app.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {

    @Query("SELECT n from NotificationEntity n where n.user.authId = :userId")
    List<NotificationEntity> findByUserId(@Param("userId") String userId);

}
