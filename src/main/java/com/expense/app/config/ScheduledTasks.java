package com.expense.app.config;

import com.expense.app.service.RecurringTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    @Autowired
    private RecurringTransactionService recurringTransactionService;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void processRecurringTransactions() {
        recurringTransactionService.processRecurringTransactions();
    }
}
