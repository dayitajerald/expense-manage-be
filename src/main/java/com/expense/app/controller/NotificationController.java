package com.expense.app.controller;

import com.expense.app.entity.NotificationEntity;
import com.expense.app.repository.NotificationRepository;
import com.expense.app.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/all")
    public List<NotificationEntity> getAllNotifications(@RequestHeader("Authorization") String token) {
        return notificationService.getAllNotifications(token);

    }

    @PutMapping("/mark-as-paid/{id}")
    public NotificationEntity markAsPaid(@RequestHeader("Authorization") String token,@PathVariable Integer id) {
        return notificationService.markAsPaid(token,id);
    }
}
