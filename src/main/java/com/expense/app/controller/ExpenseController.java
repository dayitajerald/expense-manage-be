package com.expense.app.controller;

import com.expense.app.dto.ExpenseDto;
import com.expense.app.entity.ExpenseEntity;
import com.expense.app.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping()
    public List<ExpenseDto> getUserExpenses(@RequestHeader("Authorization") String token){
        return expenseService.getUserExpenses(token);
    }

    @PostMapping()
    public ExpenseEntity createUserExpense(@RequestHeader("Authorization") String token, @RequestBody ExpenseEntity expense){
        return expenseService.createUserExpense(token,expense);
    }

    @PutMapping()
    public ExpenseEntity updateUserExpense(@RequestHeader("Authorization") String token, @RequestBody ExpenseEntity expense){
        return expenseService.updateUserExpense(token,expense);
    }

    @DeleteMapping()
    public void deleteUserExpense(@RequestHeader("Authorization") String token, @RequestParam Integer id){
        expenseService.deleteUserExpense(token,id);
    }
}
