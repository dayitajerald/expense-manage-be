package com.expense.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONObject;

@Service
public class ExpenseForecastingService {

    @Autowired
    private RestTemplate restTemplate;

    public Float predictNextMonthExpense(int month) {
        String url = "http://localhost:5000/predict?month=" + month;
        String response = restTemplate.getForObject(url, String.class);
        JSONObject jsonResponse = new JSONObject(response);
        return jsonResponse.getFloat("prediction");
    }
}
