package com.expense.app.controller;


import com.expense.app.dto.BudgetDto;
import com.expense.app.dto.ExpenseDto;
import com.expense.app.entity.BudgetEntity;
import com.expense.app.repository.BudgetRepository;
import com.expense.app.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping("/category")
    public List<BudgetDto> getBudgetCategory(@RequestHeader("Authorization") String token){
        return budgetService.getBudgetCategory(token);
    }

    @PostMapping("/create")
    public BudgetEntity createBudget(@RequestHeader("Authorization") String token, @RequestBody BudgetDto budget){
        return budgetService.reateUserBudget(token,budget);
    }


}
