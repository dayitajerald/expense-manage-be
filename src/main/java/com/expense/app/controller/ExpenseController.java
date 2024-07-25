package com.expense.app.controller;

import com.expense.app.dto.CategoryExpenseSumDto;
import com.expense.app.dto.ExpenseDto;
import com.expense.app.dto.TotalExpenseDto;
import com.expense.app.entity.ExpenseEntity;
import com.expense.app.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/expenses")
public class ExpenseController {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/all")
    public List<ExpenseDto> getUserExpenses(@RequestHeader("Authorization") String token){
        return expenseService.getUserExpenses(token);
    }

    @PostMapping("/create")
    public ExpenseEntity createUserExpense(@RequestHeader(value = "Authorization") String token, @RequestBody ExpenseDto expense){
         return expenseService.createUserExpense(token,expense);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseEntity> updateExpense(@PathVariable Integer id, @RequestBody ExpenseDto expenseDetails) {
        ExpenseEntity updatedExpense = expenseService.updateExpense(id, expenseDetails);
        return ResponseEntity.ok(updatedExpense);
    }

    @DeleteMapping("/{expenseId}")
    public void deleteUserExpense(@RequestHeader("Authorization") String token, @PathVariable Integer expenseId){
        expenseService.deleteUserExpense(token,expenseId);
    }

    @GetMapping("/total")
    public TotalExpenseDto getTotalExpense(@RequestHeader("Authorization") String token){
        return expenseService.getTotalExpense(token);
    }

    @GetMapping("/sum-by-category")
    public List<CategoryExpenseSumDto> getSumofAmountByCategory(@RequestHeader("Authorization") String token){
        return expenseService.getSumOfAmountByCategory(token);
    }
}
