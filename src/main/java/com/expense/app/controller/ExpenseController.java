package com.expense.app.controller;

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
    public ExpenseEntity createUserExpense(@RequestHeader("Authorization") String token, @RequestBody ExpenseEntity expense){
        return expenseService.createUserExpense(token,expense);
    }

    @PatchMapping("/{expenseId}")
    public ResponseEntity<ExpenseEntity> updateExpenseField(@PathVariable Integer expenseId, @RequestParam String fieldName, @RequestParam String newValue) {
        try {
            ExpenseEntity updatedExpense = expenseService.updateExpenseField(expenseId, fieldName, newValue);
            logger.info("Expense updated successfully");
            return ResponseEntity.ok(updatedExpense);

        } catch (RuntimeException e) {
            logger.error("Error updating expense: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/remove")
    public void deleteUserExpense(@RequestHeader("Authorization") String token, @RequestParam Integer id){
        expenseService.deleteUserExpense(token,id);
    }

    @GetMapping("/total")
    public TotalExpenseDto getTotalExpense(@RequestHeader("Authorization") String token){
        return expenseService.getTotalExpense(token);
    }
}
