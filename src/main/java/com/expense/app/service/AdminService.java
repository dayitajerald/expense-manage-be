package com.expense.app.service;

import com.expense.app.entity.ExpenseEntity;
import com.expense.app.entity.UserEntity;
import com.expense.app.middleware.JwtTokenUtil;
import com.expense.app.model.TokenModel;
import com.expense.app.repository.ExpenseRepository;
import com.expense.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public int getNumberofUsers(String token){
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String authId = tokenModel.getId();
        Integer role = tokenModel.getRole();
        if(role == 1){
            List<UserEntity> users = userRepository.findAll();
            return users.size();
        }
        else{
            System.err.println("Not admin");
            return 0;
        }
    }

    public int getExpensePastWeek(String token){
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String authId = tokenModel.getId();
        Integer role = tokenModel.getRole();
        if(role == 1){
            List<ExpenseEntity> expenses = expenseRepository.findAll();
            return expenses.size();
        }
        else{
            System.err.println("Not admin");
            return 0;
        }
    }

    public Map<LocalDate, Long> getUserRegistrations(String token) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String authId = tokenModel.getId();
        Integer role = tokenModel.getRole();
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .collect(Collectors.groupingBy(
                        user -> user.getCreated_at().toLocalDate(),
                        Collectors.counting()
                ));
    }
}
