package com.expense.app.service;


import com.expense.app.dto.ExpenseDto;
import com.expense.app.entity.ExpenseEntity;
import com.expense.app.model.TokenModel;
import com.expense.app.repository.ExpenseRepository;
import com.expense.app.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.expense.app.middleware.JwtTokenUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public List<ExpenseDto> getUserExpenses(String token) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        List<ExpenseEntity> expenses =expenseRepository.findByUserId(tokenModel.getId());
        List<ExpenseDto> expenseDtos = new ArrayList<ExpenseDto>();
        for (ExpenseEntity expense : expenses) {
            ExpenseDto expenseDto = new ExpenseDto();
            expenseDto.setExpenseId(expense.getExpenseId());
            expenseDto.setUserId(expense.getUserId());
            expenseDto.setAmount(expense.getAmount());
            expenseDto.setDate(expense.getDate().toString());
            expenseDto.setDescription(expense.getDescription());
            expenseDto.setReceipt(expense.getReceipt());
            expenseDto.setUserName(userRepository.findById(expense.getUserId()).get().getName());
            expenseDtos.add(expenseDto);
        }
        return expenseDtos;
    }

    public ExpenseEntity createUserExpense(String token, ExpenseEntity expense) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        expense.setUserId(tokenModel.getId());
        expense.setDate(LocalDate.now());
        return expenseRepository.save(expense);
    }

    public ExpenseEntity updateUserExpense(String token, ExpenseEntity expense) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        ExpenseEntity expenseEntity = expenseRepository.findById(expense.getExpenseId()).orElse(null);
//        expenseEntity.setAmount(expense.getAmount());
//        expenseEntity.setDate(LocalDate.now());
//        expenseEntity.setDescription(expense.getDescription());
//        expenseEntity.setReceipt(expense.getReceipt());
//        expenseEntity.setUserId(tokenModel.getId());
        BeanUtils.copyProperties(expense, expenseEntity);
        //expenseEntity.setUserId(tokenModel.getId());
        expenseEntity.setCreated_at(LocalDateTime.now());
        expenseEntity.setUpdated_at(LocalDateTime.now());
        System.out.println(expenseEntity);
        return expenseRepository.save(expenseEntity);
    }

    public void deleteUserExpense(String token, Integer id) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        //expense.setUserId(tokenModel.getId());
        System.out.println(id );
        expenseRepository.deleteByExpenseId(id);
    }




}
