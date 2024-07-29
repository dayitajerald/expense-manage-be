package com.expense.app.controller;


import com.expense.app.entity.UserEntity;
import com.expense.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public UserEntity getProfile(@RequestHeader("Authorization") String token) {
        return userService.getProfile(token);
    }

    @PostMapping("/profile/edit")
    public UserEntity editCustomerProfile(@RequestHeader("Authorization") String token,
                                              @RequestBody UserEntity entity) {
        return userService.editUserProfile(token, entity);
    }
}
