package com.expense.app.controller;

import com.expense.app.dto.IncomeDto;
import com.expense.app.entity.IncomeEntity;
import com.expense.app.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incomes")
public class IncomeController {
    @Autowired
    private IncomeService incomeService;

    @GetMapping()
    public List<IncomeDto> getUserIncomes(@RequestHeader("Authorization") String token) {
        return incomeService.getUserExpenses(token);
    }

    @PostMapping()
    public IncomeEntity createUserIncome(@RequestHeader("Authorization") String token,
            @RequestBody IncomeEntity income) {
        return incomeService.createUserExpense(token, income);
    }

    // @PutMapping()
    // public IncomeEntity updateUserIncome(@RequestHeader("Authorization") String
    // token, @RequestBody IncomeEntity income) {
    // return incomeService.updateUserExpense(token, income);
    // }

    @DeleteMapping()
    public void deleteUserIncome(@RequestHeader("Authorization") String token, @RequestBody Integer id) {
        incomeService.deleteUserExpense(token, id);
    }

}
