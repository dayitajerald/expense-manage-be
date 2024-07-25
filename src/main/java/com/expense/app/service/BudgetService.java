package com.expense.app.service;

import com.expense.app.dto.BudgetDto;
import com.expense.app.entity.BudgetEntity;
import com.expense.app.entity.CategoryEntity;
import com.expense.app.entity.ExpenseEntity;
import com.expense.app.entity.UserEntity;
import com.expense.app.middleware.JwtTokenUtil;
import com.expense.app.model.TokenModel;
import com.expense.app.repository.BudgetRepository;
import com.expense.app.repository.CategoryRepository;
import com.expense.app.repository.IncomeRepository;
import com.expense.app.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetService {
    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;


    public List<BudgetDto> getBudgetCategory(String token){
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String authId = tokenModel.getId();
        return budgetRepository.findBudgets(authId);
    }

    public BudgetEntity createUserBudget(String token, BudgetDto budgetDto){
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        UserEntity user = userRepository.findById(tokenModel.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        BudgetEntity budgetEntity = new BudgetEntity();
        BeanUtils.copyProperties(budgetDto,budgetEntity);
        CategoryEntity category = categoryRepository.findByCategoryId(budgetDto.getCategoryId());
        budgetEntity.setCategory(category);
        budgetEntity.setUser(user);
        return budgetRepository.save(budgetEntity);



//        CategoryEntity cat = categoryRepository.findByCategoryId(budgetDto.getCategoryId());
//        BudgetEntity bud = budgetRepository.findByCategoryId(cat);

    }



}
