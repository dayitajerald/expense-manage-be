package com.expense.app.service;

import com.expense.app.dto.ExpenseDto;
import com.expense.app.dto.IncomeDto;
import com.expense.app.entity.ExpenseEntity;
import com.expense.app.entity.IncomeEntity;
import com.expense.app.entity.UserEntity;
import com.expense.app.middleware.JwtTokenUtil;
import com.expense.app.model.TokenModel;
import com.expense.app.repository.IncomeRepository;
import com.expense.app.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class IncomeService {
    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public List<IncomeDto> getUserExpenses(String token) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String authId = tokenModel.getId();
        List<IncomeEntity> incomes = incomeRepository.findByUser_AuthId(authId);
        List<IncomeDto> incomeDtos = new ArrayList<>();
        for (IncomeEntity income : incomes) {
            IncomeDto incomeDto = new IncomeDto();
            incomeDto.setIncomeId(income.getIncomeId());
            incomeDto.setAmount(income.getAmount());
            incomeDto.setCategory(income.getCategory());
            incomeDto.setDate(income.getDate().toString());
            incomeDto.setUserName(income.getUser().getName());
            incomeDtos.add(incomeDto);
        }
        return incomeDtos;
    }

    public IncomeEntity createUserExpense(String token, IncomeEntity income) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        UserEntity user = userRepository.findById(tokenModel.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        income.setUser(user);
        income.setDate(LocalDate.now());
        return incomeRepository.save(income);
    }

    public IncomeEntity updateUserExpense(String token, IncomeEntity income) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        IncomeEntity existingIncome = incomeRepository.findById(income.getIncomeId()).orElseThrow(() -> new RuntimeException("Expense not found"));
        BeanUtils.copyProperties(income, existingIncome, "expenseId", "user", "created_at", "updated_at");
        existingIncome.setUpdated_at(LocalDateTime.now());
        return incomeRepository.save(existingIncome);
    }

    public void deleteUserExpense(String token, Integer id) {
        IncomeEntity existingIncome = incomeRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
        incomeRepository.delete(existingIncome);
    }
}
