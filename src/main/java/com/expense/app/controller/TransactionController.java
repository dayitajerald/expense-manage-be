package com.expense.app.controller;

// TransactionController.java
import com.expense.app.dto.TransactionDto;
import com.expense.app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/all")
    public ResponseEntity<List<TransactionDto>> getAllTransactions(@RequestHeader("Authorization") String token) {
        List<TransactionDto> transactions = transactionService.getAllTransactions(token);
        return ResponseEntity.ok(transactions);
    }
}
