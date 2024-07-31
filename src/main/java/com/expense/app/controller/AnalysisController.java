package com.expense.app.controller;

import com.expense.app.service.ExpenseForecastingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    @Autowired
    private ExpenseForecastingService expenseForecastingService;

    @GetMapping("/forecast")
    public ResponseEntity<Float> getExpenseForecast(@RequestParam("month") int month) {
        Float forecast = expenseForecastingService.predictNextMonthExpense(month);
        return ResponseEntity.ok(forecast);
    }
}
