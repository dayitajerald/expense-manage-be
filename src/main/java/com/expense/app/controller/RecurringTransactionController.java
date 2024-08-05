package com.expense.app.controller;

import com.expense.app.dto.ExpenseDto;
import com.expense.app.dto.RecurringTransactionDto;
import com.expense.app.entity.ExpenseEntity;
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

    @GetMapping("/all")
    public List<RecurringTransactionDto> getRecurringTransaction(@RequestHeader("Authorization") String token){
        return recurringTransactionService.getRecurringTransaction(token);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecurringTransactionEntity> updateRecurringTransaction(@RequestHeader(value = "Authorization") String token, @PathVariable Integer id, @RequestBody RecurringTransactionDto recurringTransactionDto) {
        RecurringTransactionEntity updatedRecurringTransaction = recurringTransactionService.updateRecurringTranscation(token,id, recurringTransactionDto);
        return ResponseEntity.ok(updatedRecurringTransaction);
    }

    @DeleteMapping("/{id}")
    public void deleteRecurringTransaction(@RequestHeader("Authorization") String token, @PathVariable Integer id){
        recurringTransactionService.deleteRecurringTransaction(token,id);
    }

    @PostMapping("/create")
    public RecurringTransactionEntity createRecurringTransaction(@RequestHeader("Authorization") String token, @RequestBody RecurringTransactionDto recurringTransactionDto) {
        return recurringTransactionService.createRecurringTransaction(token,recurringTransactionDto);
    }

    @PostMapping("/trigger")
    public void triggerRecurringTransactions() {
        recurringTransactionService.processRecurringTransactions();
    }
}
