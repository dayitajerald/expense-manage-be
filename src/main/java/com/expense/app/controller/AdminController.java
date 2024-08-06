package com.expense.app.controller;

import com.expense.app.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/admin")
public class AdminController {
   @Autowired
   private AdminService adminService;

   @GetMapping("/numberusers")
   public Integer getNumberUsers(@RequestHeader("Authorization") String token){
      return adminService.getNumberofUsers(token);
   }

   @GetMapping("/expensespastweek")
   public Integer getExpensePastWeek(@RequestHeader("Authorization") String token){
      return adminService.getExpensePastWeek(token);
   }


}
