package com.expense.app.service;


import com.expense.app.dto.CategoryExpenseSumDto;
import com.expense.app.dto.ExpenseDto;
import com.expense.app.dto.TotalExpenseDto;
import com.expense.app.entity.CategoryEntity;
import com.expense.app.entity.ExpenseEntity;
import com.expense.app.entity.UserEntity;
import com.expense.app.model.TokenModel;
import com.expense.app.repository.CategoryRepository;
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
    @Autowired
    private CategoryRepository categoryRepository;

    private static final List<String> VALID_CATEGORIES = List.of("Food", "Movie", "Hospital");

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
            expenseDto.setReceipt(expense.getReceipt());
            expenseDtos.add(expenseDto);
        }
        return expenseDtos;
    }

//    public List<ExpenseEntity> getUserExpenses(String token){
//        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
//        String authId = tokenModel.getId();
//        List<ExpenseEntity> expenses = expenseRepository.findByUserId(authId);
//        return expenses;
//    }

    public ExpenseEntity createUserExpense(String token, ExpenseDto data) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        UserEntity user = userRepository.findById(tokenModel.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        ExpenseEntity expense = new ExpenseEntity();
        BeanUtils.copyProperties(data,expense);
        CategoryEntity category = categoryRepository.findByCategoryId(data.getCategory());
        expense.setCategory(category);
        expense.setUser(user);
        //expense.setDate(LocalDate.now());
        return expenseRepository.save(expense);
    }

//    public ExpenseEntity updateExpenseField(Integer expenseId, String fieldName, String newValue) {
//        ExpenseEntity expense = expenseRepository.findById(expenseId)
//                .orElseThrow(() -> new RuntimeException("Expense not found"));
//        try {
//            switch (fieldName) {
//                case "amount":
//                    expense.setAmount(Float.parseFloat(newValue));
//                    break;
//                case "category":
//                    CategoryEntity category = categoryRepository.findByCategoryId(Integer.parseInt(newValue));
//                    expense.setCategory(category);
//                    break;
//                case "date":
//                    expense.setDate(LocalDate.parse(newValue));
//                    break;
//                case "description":
//                    expense.setDescription(newValue);
//                    break;
//                case "receipt":
//                    expense.setReceipt(newValue);
//                    break;
//                default:
//                    throw new RuntimeException("Invalid field name");
//            }
//        } catch (NumberFormatException e) {
//            throw new RuntimeException("Invalid format for field: " + fieldName, e);
//        }
//
//        return expenseRepository.save(expense);
//    }
    public ExpenseEntity updateExpense(Integer id, ExpenseDto expenseDetails) {
        ExpenseEntity expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));

        // Validate category
//        if (!VALID_CATEGORIES.contains(expenseDetails.getCategory())) {
//            throw new IllegalArgumentException("Invalid category: " + expenseDetails.getCategory());
//        }

        CategoryEntity category = categoryRepository.findByCategoryId(expenseDetails.getCategory());
        expense.setCategory(category);
        expense.setAmount(expenseDetails.getAmount());
        expense.setDate(expenseDetails.getDate());
        expense.setDescription(expenseDetails.getDescription());
        expense.setReceipt(expenseDetails.getReceipt());

        return expenseRepository.save(expense);
    }

    public void deleteUserExpense(String token, Integer id) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        ExpenseEntity existingExpense = expenseRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
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
}
