package com.expense.app.dto;

import lombok.Data;

@Data
public class Recommendation {
    private String category;
    private String suggestion;
    private String example;
    public Recommendation(String category, String suggestion, String example) {
        this.category = category;
        this.suggestion = suggestion;
        this.example = example;
    }

    public Recommendation() {
    }
}
