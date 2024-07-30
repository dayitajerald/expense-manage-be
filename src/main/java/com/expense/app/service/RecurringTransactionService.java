package com.expense.app.service;

import com.expense.app.dto.RecurringTransactionDto;
import com.expense.app.entity.*;
import com.expense.app.middleware.JwtTokenUtil;
import com.expense.app.model.TokenModel;
import com.expense.app.repository.ExpenseRepository;
import com.expense.app.repository.IncomeRepository;
import com.expense.app.repository.RecurringTransactionRepository;
import com.expense.app.repository.CategoryRepository;
import com.expense.app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecurringTransactionService {
    private static final Logger log = LoggerFactory.getLogger(RecurringTransactionService.class);
    @Autowired
    private RecurringTransactionRepository recurringTransactionRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public RecurringTransactionEntity createRecurringTransaction(String token, RecurringTransactionDto recurringTransactionDto) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        UserEntity user = userRepository.findById(tokenModel.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        RecurringTransactionEntity recurringTransaction = new RecurringTransactionEntity();
        recurringTransaction.setAmount(recurringTransactionDto.getAmount());
        recurringTransaction.setDescription(recurringTransactionDto.getDescription());
        recurringTransaction.setPeriod(recurringTransactionDto.getPeriod());
        recurringTransaction.setStartDate(recurringTransactionDto.getStartDate());
        recurringTransaction.setType(recurringTransactionDto.getType());

        recurringTransaction.setUser(user);

        CategoryEntity category = categoryRepository.findById(recurringTransactionDto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        recurringTransaction.setCategory(category);

        return recurringTransactionRepository.save(recurringTransaction);
    }

    public List<RecurringTransactionEntity> getRecurringTransactions(String userId) {
        return recurringTransactionRepository.findByUserId(userId);
    }

    //@Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    //@Scheduled(cron = "0 * * * * ?") // Runs every minute
    public void processRecurringTransactions() {
        LocalDate today = LocalDate.now();
        List<RecurringTransactionEntity> recurringTransactions = recurringTransactionRepository.findAll();

        for (RecurringTransactionEntity rt : recurringTransactions) {
            if (shouldProcess(rt, today)) {
                if (rt.getType().equalsIgnoreCase("expense")) {
                    ExpenseEntity expense = new ExpenseEntity();
                    expense.setAmount(rt.getAmount());
                    expense.setDate(today);
                    expense.setDescription(rt.getDescription());
                    expense.setCategory(rt.getCategory());
                    expense.setUser(rt.getUser());
                    expenseRepository.save(expense);
                    log.info("Recurring expense processed and saved: " );
                } else if (rt.getType().equalsIgnoreCase("income")) {
                    IncomeEntity income = new IncomeEntity();
                    income.setAmount(rt.getAmount());
                    income.setDate(today);
                    income.setCategory(rt.getCategory());
                    income.setUser(rt.getUser());
                    incomeRepository.save(income);
                    log.info("Recurring income processed and saved: ");
                }

                // Update start date for the next occurrence
                updateStartDate(rt);
                recurringTransactionRepository.save(rt);
                log.info("Recurring transaction start date updated: ");
            }
        }
    }

    private boolean shouldProcess(RecurringTransactionEntity rt, LocalDate today) {
        switch (rt.getPeriod().toLowerCase()) {
            case "daily":
                return true;
            case "weekly":
                return rt.getStartDate().plusWeeks(1).isEqual(today);
            case "monthly":
                return rt.getStartDate().plusMonths(1).isEqual(today);
            default:
                return false;
        }
    }

    private void updateStartDate(RecurringTransactionEntity rt) {
        switch (rt.getPeriod().toLowerCase()) {
            case "daily":
                rt.setStartDate(rt.getStartDate().plusDays(1));
                break;
            case "weekly":
                rt.setStartDate(rt.getStartDate().plusWeeks(1));
                break;
            case "monthly":
                rt.setStartDate(rt.getStartDate().plusMonths(1));
                break;
        }
    }

    private LocalDate getNextTransactionDate(LocalDate startDate, String period) {
        switch (period.toLowerCase()) {
            case "daily":
                return startDate.plusDays(1);
            case "weekly":
                return startDate.plusWeeks(1);
            case "monthly":
                return startDate.plusMonths(1);
            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }
    }
}
