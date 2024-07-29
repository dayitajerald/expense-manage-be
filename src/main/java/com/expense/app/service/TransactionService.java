package com.expense.app.service;

// TransactionService.java
import com.expense.app.dto.TransactionDto;
import com.expense.app.middleware.JwtTokenUtil;
import com.expense.app.model.TokenModel;
import com.expense.app.repository.ExpenseRepository;
import com.expense.app.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public List<TransactionDto> getAllTransactions(String token) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String authId = tokenModel.getId();
        List<TransactionDto> expenses = expenseRepository.findAllExpensesAsTransactions(authId);
        List<TransactionDto> incomes = incomeRepository.findAllIncomesAsTransactions(authId);

        List<TransactionDto> transactions = new ArrayList<>();
        transactions.addAll(expenses);
        transactions.addAll(incomes);

        return transactions;
    }
}
