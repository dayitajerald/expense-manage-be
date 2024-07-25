package com.expense.app.service;

import com.expense.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final JavaMailSender emailSender;

    @Autowired
    private UserRepository userRepository;


    public UserService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendNotification(String userId, String message) {
        String userEmail = getUserEmailById(userId);

        if (userEmail != null) {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(userEmail);
            mailMessage.setSubject("Budget Alert");
            mailMessage.setText(message);

            emailSender.send(mailMessage);
        } else {
            System.err.println("Email not found for user ID: " + userId);
        }
    }

    private String getUserEmailById(String userId) {
        if(userRepository.existsById(userId)) {
            return userRepository.findById(userId).get().getEmail();
        }
        else {
            return null;
        }
    }
}
