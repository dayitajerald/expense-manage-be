package com.expense.app.controller;
import com.expense.app.middleware.JwtTokenUtil;
import com.expense.app.model.TokenModel;
import com.expense.app.service.PdfGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/expenses")
public class ReportController {

    @Autowired
    private PdfGenerationService pdfGenerationService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/generate-report")
    public ResponseEntity<byte[]> generateExpenseReport(@RequestHeader("Authorization") String token) {
        // Extract userId from token (assuming you have a method to do this)\
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String userId = tokenModel.getId();
        try {
            byte[] pdfBytes = pdfGenerationService.generateExpenseReportForUser(userId);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/pdf");
            headers.add("Content-Disposition", "attachment; filename=expense-report.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String extractUserIdFromToken(String token) {
        // Extract userId from token logic
        return "exampleUserId"; // Replace with actual extraction logic
    }
}
