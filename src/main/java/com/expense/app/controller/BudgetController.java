package com.expense.app.controller;


import com.expense.app.dto.BudgetDto;
import com.expense.app.dto.ExpenseDto;
import com.expense.app.entity.BudgetEntity;
import com.expense.app.repository.BudgetRepository;
import com.expense.app.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping("/category")
    public List<BudgetDto> getBudgetCategory(@RequestHeader("Authorization") String token) {
        return budgetService.getBudgetCategory(token);
    }

    @PostMapping("/create")
    public BudgetEntity createBudget(@RequestHeader("Authorization") String token, @RequestBody BudgetDto budget){
        return budgetService.createUserBudget(token,budget);
    }

    @PutMapping("/{id}")
    public BudgetEntity changeBudget(@RequestHeader("Authorization") String token,@PathVariable Integer id, @RequestBody BudgetDto budget){
        return budgetService.updateUserBudget(token,id,budget);
    }

    @DeleteMapping("/{budgetId}")
    public void deleteUserBudget(@RequestHeader("Authorization") String token, @PathVariable Integer budgetId){
        budgetService.deleteUserBudget(token,budgetId);
    }

}
