package com.expense.app.controller;

import com.expense.app.entity.NotificationEntity;
import com.expense.app.repository.NotificationRepository;
import com.expense.app.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationService notificationService;

    @PutMapping("/{id}/mark-as-paid")
    public NotificationEntity markAsPaid(@RequestHeader("Authorization") String token,@PathVariable Integer id) {
        return notificationService.markAsPaid(token,id);
    }
}
