package com.expense.app.repository;

import com.expense.app.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByAuthId(String authId);

    boolean existsByAuthId(String authId);
}
