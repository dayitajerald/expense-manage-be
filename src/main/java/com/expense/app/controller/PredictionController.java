package com.expense.app.controller;

import com.expense.app.dto.ExpenseDto;
import com.expense.app.dto.Recommendation;
import com.expense.app.entity.ExpenseEntity;
import com.expense.app.middleware.JwtTokenUtil;
import com.expense.app.model.TokenModel;
import com.expense.app.service.ExpenseService;
import com.expense.app.service.GeminiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173")
public class PredictionController {

    @Autowired
    private GeminiService geminiService;
    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/gemini")
    public List<Recommendation> gemini(@RequestHeader("Authorization") String token) throws IOException {
        List<ExpenseDto> expenses = expenseService.getUserExpenses(token);
        return geminiService.getRecommendations(expenses);
    }


}
