package com.expense.app.service;

import com.expense.app.middleware.JwtTokenUtil;
import com.expense.app.model.TokenModel;
import com.expense.app.repository.ExpenseRepository;
import com.expense.app.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public Map<String, Object> getMonthlyIncomeAndExpenses(String token) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String userId = tokenModel.getId();

        List<Object[]> expenses = expenseRepository.findMonthlyExpenses(userId);
        List<Object[]> incomes = incomeRepository.findMonthlyIncomes(userId);

        Map<Integer, Double> expenseMap = new HashMap<>();
        Map<Integer, Double> incomeMap = new HashMap<>();

        for (Object[] expense : expenses) {
            expenseMap.put((Integer) expense[0], (Double) expense[1]);
        }

        for (Object[] income : incomes) {
            incomeMap.put((Integer) income[0], (Double) income[1]);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("expenses", expenseMap);
        result.put("incomes", incomeMap);

        return result;
    }
}