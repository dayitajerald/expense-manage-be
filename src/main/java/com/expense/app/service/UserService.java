package com.expense.app.service;

import com.expense.app.entity.UserEntity;
import com.expense.app.middleware.JwtTokenUtil;
import com.expense.app.model.TokenModel;
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

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


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
            System.out.println("Notification sent");
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

    public UserEntity getProfile(String token) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        if (tokenModel.getRole().equals(0))
            return userRepository.findByAuthId(tokenModel.getId());
        else
            return null;
    }

    public UserEntity editUserProfile(String token, UserEntity entity) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        UserEntity user = userRepository.findByAuthId(tokenModel.getId());
        user.setEmail(entity.getEmail());
        user.setPhone(entity.getPhone());
        user.setName(entity.getName());
        return userRepository.save(user);
    }
}