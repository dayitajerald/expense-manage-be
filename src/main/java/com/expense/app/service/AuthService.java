package com.expense.app.service;

import com.expense.app.dto.AuthDto;
import com.expense.app.dto.RegisterDto;
import com.expense.app.entity.AuthEntity;
import com.expense.app.entity.UserEntity;
import com.expense.app.middleware.JwtTokenUtil;
import com.expense.app.model.TokenModel;
import com.expense.app.repository.AuthRepository;
import com.expense.app.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthRepository authRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public ResponseEntity<AuthDto> login(AuthEntity user) {
        AuthEntity found = authRepo.findByUsername(user.getUsername());
        AuthDto response = new AuthDto();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (found == null) {
            response.setStatus(401);
            response.setMessage("User not found");
        } else {

            if (passwordEncoder.matches(user.getPassword(), found.getPassword())) {
                response.setRole(found.getRole());
                response.setStatus(200);
                response.setMessage("User logged in");
                response.setToken(jwtTokenUtil.generateToken(found));
            } else {
                response.setStatus(401);
                response.setMessage("Invalid Username or password");
            }
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    public ResponseEntity<RegisterDto> register(RegisterDto user) {
        if (authRepo.existsByUsername(user.getUsername())) {
            user.setStatus(401);
            user.setMessage("Username already exists");
        } else {
            AuthEntity auth = new AuthEntity();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            BeanUtils.copyProperties(user, auth);
            auth.setPassword(passwordEncoder.encode(auth.getPassword()));
            auth = authRepo.save(auth);

            if (auth.getRole() == 0) {
                UserEntity customer = new UserEntity();
                BeanUtils.copyProperties(user, customer);
                customer.setAuthId(auth.getId());
                userRepo.save(customer);
                user.setStatus(200);
                user.setMessage("User registered successfully");

            } else {
                user.setStatus(401);
                user.setMessage("Invalid role");
            }
        }
        return ResponseEntity.status(user.getStatus()).body(user);
    }

    public boolean validateToken(String token) {
        if (jwtTokenUtil.isTokenExpired(token)) {
            return false;
        } else {
            TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token);
            if (tokenModel == null) {
                return false;
            }
            if (tokenModel.getRole() == 0) {
                return userRepo.existsByAuthId(tokenModel.getId());
            } else {
                return false;
            }
        }
    }
}
