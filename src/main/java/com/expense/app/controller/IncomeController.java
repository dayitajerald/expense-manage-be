package com.expense.app.controller;

import com.expense.app.dto.CategoryExpenseSumDto;
import com.expense.app.dto.IncomeDto;
import com.expense.app.dto.PercentageChangeDto;
import com.expense.app.dto.TotalIncomeDto;
import com.expense.app.entity.ExpenseEntity;
import com.expense.app.entity.IncomeEntity;
import com.expense.app.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/incomes")
public class IncomeController {
    @Autowired
    private IncomeService incomeService;

    @GetMapping()
    public List<IncomeDto> getUserIncomes(@RequestHeader("Authorization") String token) {
        return incomeService.getUserIncomes(token);
    }

    @PostMapping()
    public IncomeEntity createUserIncome(@RequestHeader("Authorization") String token,
            @RequestBody IncomeDto income) {
        return incomeService.createUserIncome(token, income);
    }

    @PatchMapping("/{expenseId}")
    public ResponseEntity<IncomeEntity> updateUserIncome(@PathVariable Integer expenseId, @RequestParam String newValue) {
        try {
            IncomeEntity updatedIncome = incomeService.updateIncomeField(expenseId,newValue);
            //logger.info("Expense updated successfully");
            return ResponseEntity.ok(updatedIncome);

        } catch (RuntimeException e) {
            //logger.error("Error updating expense: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping()
    public void deleteUserIncome(@RequestHeader("Authorization") String token, @RequestBody Integer id) {
        incomeService.deleteUserExpense(token, id);
    }

    @GetMapping("/total")
    public TotalIncomeDto getTotalIncome(@RequestHeader("Authorization") String token) {
        return incomeService.getTotalIncome(token);
    }

    @GetMapping("/percentage-change")
    public ResponseEntity<PercentageChangeDto> getPercentageChange(
            @RequestParam("currentMonth") String currentMonth,
            @RequestParam("previousMonth") String previousMonth,@RequestHeader("Authorization") String token) {

        double currentMonthTotal = incomeService.getTotalAmountForMonth(currentMonth,token);
        double previousMonthTotal = incomeService.getTotalAmountForMonth(previousMonth,token);

        double percentageChange = 0;
        if (previousMonthTotal > 0) {
            percentageChange = ((currentMonthTotal - previousMonthTotal) / previousMonthTotal) * 100;
        }

        PercentageChangeDto dto = new PercentageChangeDto();
        dto.setCurrentMonthTotal(currentMonthTotal);
        dto.setPreviousMonthTotal(previousMonthTotal);
        dto.setPercentageChange(percentageChange);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/sum-by-category")
    public List<CategoryExpenseSumDto> getSumofAmountByCategory(@RequestHeader("Authorization") String token){
        return incomeService.getSumOfAmountByCategory(token);
    }
}
