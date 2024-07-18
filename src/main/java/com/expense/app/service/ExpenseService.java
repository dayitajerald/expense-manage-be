package com.expense.app.service;


import com.expense.app.dto.ExpenseDto;
import com.expense.app.entity.ExpenseEntity;
import com.expense.app.entity.UserEntity;
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
        String authId = tokenModel.getId();
        List<ExpenseEntity> expenses = expenseRepository.findByUser_AuthId(authId);
        List<ExpenseDto> expenseDtos = new ArrayList<>();
        for (ExpenseEntity expense : expenses) {
            ExpenseDto expenseDto = new ExpenseDto();
            expenseDto.setExpenseId(expense.getExpenseId());
            expenseDto.setAmount(expense.getAmount());
            expenseDto.setCategory(expense.getCategory());
            expenseDto.setDate(expense.getDate().toString());
            expenseDto.setDescription(expense.getDescription());
            expenseDto.setReceipt(expense.getReceipt());
            expenseDto.setUserName(expense.getUser().getName());
            expenseDtos.add(expenseDto);
        }
        return expenseDtos;
    }

    public ExpenseEntity createUserExpense(String token, ExpenseEntity expense) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        UserEntity user = userRepository.findById(tokenModel.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        expense.setUser(user);
        expense.setDate(LocalDate.now());
        return expenseRepository.save(expense);
    }

    public ExpenseEntity updateExpenseField(Integer expenseId, String fieldName, String newValue) {
        ExpenseEntity expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        try {
            switch (fieldName) {
                case "amount":
                    expense.setAmount(Float.parseFloat(newValue));
                    break;
                case "category":
                    expense.setCategory(Integer.parseInt(newValue));
                    break;
                case "date":
                    expense.setDate(LocalDate.parse(newValue));
                    break;
                case "description":
                    expense.setDescription(newValue);
                    break;
                case "receipt":
                    expense.setReceipt(newValue);
                    break;
                default:
                    throw new RuntimeException("Invalid field name");
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid format for field: " + fieldName, e);
        }

        return expenseRepository.save(expense);
    }

    public void deleteUserExpense(String token, Integer id) {
        ExpenseEntity existingExpense = expenseRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
        expenseRepository.delete(existingExpense);
    }

    public Float getTotalExpense(String token){
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String authId = tokenModel.getId();
        return expenseRepository.findSumOfExpensesByUserId(authId);
    }


}
