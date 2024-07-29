package com.expense.app.service;


import com.expense.app.dto.*;
import com.expense.app.entity.BudgetEntity;
import com.expense.app.entity.CategoryEntity;
import com.expense.app.entity.ExpenseEntity;
import com.expense.app.entity.UserEntity;
import com.expense.app.model.TokenModel;
import com.expense.app.repository.BudgetRepository;
import com.expense.app.repository.CategoryRepository;
import com.expense.app.repository.ExpenseRepository;
import com.expense.app.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.expense.app.middleware.JwtTokenUtil;

import javax.swing.text.html.Option;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    public List<ExpenseDto> getUserExpenses(String token) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String authId = tokenModel.getId();
        List<ExpenseEntity> expenses = expenseRepository.findByUserId(authId);
        List<ExpenseDto> expenseDtos = new ArrayList<>();
        for (ExpenseEntity expense : expenses) {
            ExpenseDto expenseDto = new ExpenseDto();
            expenseDto.setExpenseId(expense.getExpenseId());
            expenseDto.setAmount(expense.getAmount());
            expenseDto.setCategory(expense.getCategory().getCategoryId());
            expenseDto.setDate(expense.getDate());
            expenseDto.setDescription(expense.getDescription());
            expenseDtos.add(expenseDto);
        }
        return expenseDtos;
    }

    public List<ExpenseEntity> getExpensesByUserId(String userId) {
        return expenseRepository.findByUserId(userId);
    }


    public ExpenseEntity createUserExpense(String token, ExpenseDto data) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        UserEntity user = userRepository.findById(tokenModel.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        ExpenseEntity expense = new ExpenseEntity();
        BeanUtils.copyProperties(data,expense);
        CategoryEntity category = categoryRepository.findByCategoryId(data.getCategory());
        expense.setCategory(category);
        expense.setUser(user);

        BudgetEntity budgetOptional = budgetRepository.findByIdAndCategoryId(user.getAuthId(), category.getCategoryId());
        if(budgetOptional != null){
            budgetOptional.setAmountSpent(budgetOptional.getAmountSpent()+expense.getAmount());
            budgetRepository.save(budgetOptional);
        }
        return expenseRepository.save(expense);
    }



    public ExpenseEntity updateExpense(String token, Integer id, ExpenseDto expenseDetails) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        UserEntity user = userRepository.findById(tokenModel.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        ExpenseEntity expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));

        CategoryEntity category = categoryRepository.findByCategoryId(expenseDetails.getCategory());
        expense.setCategory(category);
        expense.setAmount(expenseDetails.getAmount());
        expense.setDate(expenseDetails.getDate());
        expense.setDescription(expenseDetails.getDescription());

        return expenseRepository.save(expense);
    }

    public void deleteUserExpense(String token, Integer id) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        UserEntity user = userRepository.findById(tokenModel.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        ExpenseEntity existingExpense = expenseRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
        CategoryEntity category = categoryRepository.findByCategoryId(existingExpense.getCategory().getCategoryId());
        BudgetEntity budgetEntity = budgetRepository.findByIdAndCategoryId(user.getAuthId(), category.getCategoryId());
        if(budgetEntity != null) {
            budgetEntity.setAmountSpent(budgetEntity.getAmountSpent() - existingExpense.getAmount());
            budgetRepository.save(budgetEntity);
        }
        expenseRepository.delete(existingExpense);
    }

    public TotalExpenseDto getTotalExpense(String token){
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String authId = tokenModel.getId();
        TotalExpenseDto ted = new TotalExpenseDto();
        ted.setTotalExpense(expenseRepository.findSumOfExpensesByUserId(authId));
        return ted;
    }


    public List<CategoryExpenseSumDto> getSumOfAmountByCategory(String token){
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String authId = tokenModel.getId();
        return expenseRepository.findSumOfAmountByCategoryForUser(authId);
    }

    public List<CategoryExpenseTrendDto> getCategoryExpenseTrendsForUser(String token,LocalDate startDate, LocalDate endDate) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String authId = tokenModel.getId();
        return expenseRepository.findCategoryExpenseTrendsForUser(authId,startDate, endDate);
    }

    public double getTotalAmountForMonth(String month, String token) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String userId = tokenModel.getId();
        return expenseRepository.getTotalAmountByMonth(month,userId);

    }

    public Map<String, Double> getMonthlyExpensesByCategory(Integer categoryId, String token) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String userId = tokenModel.getId();
        List<Object[]> results = expenseRepository.findMonthlyExpensesByCategory(categoryId, userId);
        Map<String, Double> monthlyExpenses = new HashMap<>();

        for (Object[] result : results) {
            String month = (String) result[0];
            Double totalExpense = (Double) result[1];
            monthlyExpenses.put(month, totalExpense);
        }

        return monthlyExpenses;
    }

    public Map<String, Map<String, Double>> getWeeklyExpenses(String token, int weekOffset) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String userId = tokenModel.getId();
        Map<String, Map<String, Double>> weeklyExpenses = new LinkedHashMap<>();

        LocalDate today = LocalDate.now().minusWeeks(weekOffset);
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);

        for (int i = 0; i < 7; i++) {
            LocalDate day = startOfWeek.plusDays(i);
            Map<String, Double> dailyExpenses = expenseRepository.findByUserIdAndDate(userId, day)
                    .stream()
                    .collect(Collectors.groupingBy(
                            expense -> expense.getCategory().getName(),  // Assuming getCategory().getName() returns the category name
                            Collectors.summingDouble(ExpenseEntity::getAmount)
                    ));
            weeklyExpenses.put(day.getDayOfWeek().name(), dailyExpenses);
        }

        return weeklyExpenses;
    }
}
