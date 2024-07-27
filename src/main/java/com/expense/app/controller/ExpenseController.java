package com.expense.app.controller;

import com.expense.app.dto.*;
import com.expense.app.entity.ExpenseEntity;
import com.expense.app.service.ExpenseService;
import com.expense.app.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    private final NotificationService notificationService;

    @Autowired
    public ExpenseController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/all")
    public List<ExpenseDto> getUserExpenses(@RequestHeader("Authorization") String token){
        return expenseService.getUserExpenses(token);
    }

    @PostMapping("/create")
    public ExpenseEntity createUserExpense(@RequestHeader(value = "Authorization") String token, @RequestBody ExpenseDto expense){
         return expenseService.createUserExpense(token,expense);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseEntity> updateExpense(@RequestHeader(value = "Authorization") String token, @PathVariable Integer id, @RequestBody ExpenseDto expenseDetails) {
        ExpenseEntity updatedExpense = expenseService.updateExpense(token,id, expenseDetails);
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

    @GetMapping("/expense-trends")
    public List<CategoryExpenseTrendDto> getExpenseTrends(@RequestHeader("Authorization") String token,
                                                          @RequestParam("startDate") String startDate,
                                                          @RequestParam("endDate") String endDate) {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return expenseService.getCategoryExpenseTrendsForUser(token, start, end);
    }

    @GetMapping("/percentage-change")
    public ResponseEntity<PercentageChangeDto> getPercentageChange(
            @RequestParam("currentMonth") String currentMonth,
            @RequestParam("previousMonth") String previousMonth) {

        double currentMonthTotal = expenseService.getTotalAmountForMonth(currentMonth);
        double previousMonthTotal = expenseService.getTotalAmountForMonth(previousMonth);

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


//    @PostMapping("/trigger-notifications")
//    public ResponseEntity<String> triggerNotifications() {
//        try {
//            notificationService.manuallyTriggerCheck();
//            return ResponseEntity.ok("Notification check triggered successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to trigger notification check.");
//        }
//    }

}
