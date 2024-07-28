package com.expense.app.controller;

import com.expense.app.dto.RecurringTransactionDto;
import com.expense.app.entity.RecurringTransactionEntity;
import com.expense.app.service.RecurringTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/recurring-transactions")
public class RecurringTransactionController {
    @Autowired
    private RecurringTransactionService recurringTransactionService;

    @PostMapping("/create")
    public RecurringTransactionEntity createRecurringTransaction(@RequestHeader("Authorization") String token, @RequestBody RecurringTransactionDto recurringTransactionDto) {
        return recurringTransactionService.createRecurringTransaction(token,recurringTransactionDto);
    }

    @GetMapping("/{userId}")
    public List<RecurringTransactionEntity> getRecurringTransactions(@PathVariable String userId) {
        return recurringTransactionService.getRecurringTransactions(userId);
    }

    @PostMapping("/trigger")
    public void triggerRecurringTransactions() {
        recurringTransactionService.processRecurringTransactions();
    }
}
