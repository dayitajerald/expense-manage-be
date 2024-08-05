package com.expense.app.controller;

import com.expense.app.dto.AuthDto;
import com.expense.app.dto.PasswordChangeDto;
import com.expense.app.dto.RegisterDto;
import com.expense.app.entity.AuthEntity;
import com.expense.app.repository.AuthRepository;
import com.expense.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthDto> handleLogin(@RequestBody AuthEntity body) {
        return authService.login(body);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDto> handleRegister(@RequestBody RegisterDto body) throws Exception {
        return authService.register(body);
    }

    @PostMapping("/changepassword")
    public ResponseEntity<PasswordChangeDto> passwordChange(@RequestBody PasswordChangeDto data, @RequestHeader("Authorization") String token){
        return authService.changePassword(data, token);
    }

    @PostMapping("/forgot-password")
    public void forgotPassword(@RequestParam String email){
         authService.forgotPasswordMail(email);
    }

}
