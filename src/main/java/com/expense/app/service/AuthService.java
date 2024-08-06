package com.expense.app.service;

import com.expense.app.dto.AuthDto;
import com.expense.app.dto.PasswordChangeDto;
import com.expense.app.dto.RegisterDto;
import com.expense.app.entity.AuthEntity;
import com.expense.app.entity.UserEntity;
import com.expense.app.middleware.JwtTokenUtil;
import com.expense.app.model.TokenModel;
import com.expense.app.repository.AuthRepository;
import com.expense.app.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private UserService userService;

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

    public ResponseEntity<RegisterDto> register(RegisterDto user) throws Exception {
        if (authRepo.existsByUsername(user.getUsername())) {
            user.setStatus(401);
            user.setMessage("Username already exists");
        } else {
            AuthEntity auth = new AuthEntity();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            BeanUtils.copyProperties(user, auth);
            auth.setPassword(passwordEncoder.encode(auth.getPassword()));
            auth = authRepo.save(auth);
            System.out.println(user);

            if (auth.getRole() == 0) {
                UserEntity customer = new UserEntity();
                BeanUtils.copyProperties(user, customer);
                customer.setAuthId(auth.getId());
                userRepo.save(customer);
                user.setStatus(201);
                user.setMessage("User registered successfully");

                // Send welcome email
                String welcomeMessage = "Welcome to Expensio! We're glad to have you. Get ready to manage your expenses like a pro.";
                String gifPath = "C:/Users/javis/OneDrive/Desktop/expensemanage/expense-manage-be/moneyanim.gif"; // Change to the path of your GIF
                userService.sendWelcomeEmail(user.getEmail(), welcomeMessage, gifPath);

            } else if (auth.getRole() == 1) {
                UserEntity admin = new UserEntity();
                BeanUtils.copyProperties(user, admin);
                admin.setAuthId(auth.getId());
                userRepo.save(admin);
                user.setStatus(201);
                user.setMessage("Admin registered successfully");
            }else {
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

    public ResponseEntity<PasswordChangeDto> changePassword(PasswordChangeDto data, String token){
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String authId = tokenModel.getId();
        AuthEntity auth = authRepo.findById(authId).get();
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        boolean pwdCheck = bcrypt.matches(data.getCurrentPassword(), auth.getPassword());
        if (!pwdCheck) {
            data.setStatus("error");
            data.setMessage("Current password is incorrect.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(data);
        }

        auth.setPassword(bcrypt.encode(data.getNewPassword()));
        authRepo.save(auth);
        data.setStatus("success");
        data.setMessage("Password changed successfully.");
        return ResponseEntity.ok(data);
    }

    public void forgotPasswordMail(String email) {
         userService.sendMailForgotPassword(email,"You can change your password in this link http://localhost:5173/forgotpassword ");
    }
}
