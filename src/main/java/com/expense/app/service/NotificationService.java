package com.expense.app.service;
import com.expense.app.entity.BudgetEntity;
import com.expense.app.entity.NotificationEntity;
import com.expense.app.middleware.JwtTokenUtil;
import com.expense.app.model.TokenModel;
import com.expense.app.repository.BudgetRepository;
import com.expense.app.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public NotificationService(BudgetRepository budgetRepository, UserService userService) {
        this.budgetRepository = budgetRepository;
        this.userService = userService;
    }

    //@Scheduled(cron = "0 0/5 * * * ?")
    //@Scheduled(cron = "0 * * * * ?")
    //@Scheduled(cron = "0 0 9 * * *")
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

    public NotificationEntity markAsPaid(String token, Integer id) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String authId = tokenModel.getId();
        NotificationEntity notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        notification.setIsPaid(true);
        return notificationRepository.save(notification);
    }

    public List<NotificationEntity> getAllNotifications(String token) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String authId = tokenModel.getId();
        List<NotificationEntity> notifications = notificationRepository.findByUserId(authId);
        return notifications;
    }
//    public void manuallyTriggerCheck() {
//        logger.info("Manually triggering notification check.");
//        checkBudgetsAndSendNotifications();
//    }
}


