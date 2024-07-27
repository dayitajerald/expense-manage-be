package com.expense.app.service;

// TransactionService.java
import com.expense.app.dto.TransactionDto;
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

    public List<TransactionDto> getAllTransactions() {
        List<TransactionDto> expenses = expenseRepository.findAllExpensesAsTransactions();
        List<TransactionDto> incomes = incomeRepository.findAllIncomesAsTransactions();

        List<TransactionDto> transactions = new ArrayList<>();
        transactions.addAll(expenses);
        transactions.addAll(incomes);

        return transactions;
    }
}
