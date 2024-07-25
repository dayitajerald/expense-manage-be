package com.expense.app.service;
import com.expense.app.entity.BudgetEntity;
import com.expense.app.repository.BudgetRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final BudgetRepository budgetRepository;
    private final UserService userService;

    public NotificationService(BudgetRepository budgetRepository, UserService userService) {
        this.budgetRepository = budgetRepository;
        this.userService = userService;
    }

    //@Scheduled(cron = "0 0/5 * * * ?")
    @Scheduled(cron = "0 0 9 * * *")
    public void checkBudgetsAndSendNotifications() {
        try {
            List<BudgetEntity> budgets = budgetRepository.findAll();
            for (BudgetEntity budget : budgets) {
                float amountSpent = budget.getAmountSpent();
                float budgetAmount = budget.getBudgetAmount();
                float amountLeft = budgetAmount - amountSpent;

                if (amountLeft <= 50) {
                    String message = String.format("You have only $%.2f left in your budget for %s.",
                            amountLeft, budget.getCategory().getName());
                    userService.sendNotification(budget.getUser().getAuthId(), message);
                }
            }
            logger.info("Notification check completed successfully.");
        } catch (Exception e) {
            logger.error("Error during notification check", e);
        }
    }

//    public void manuallyTriggerCheck() {
//        logger.info("Manually triggering notification check.");
//        checkBudgetsAndSendNotifications();
//    }
}


