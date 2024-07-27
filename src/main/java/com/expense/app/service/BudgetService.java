package com.expense.app.service;

import com.expense.app.dto.BudgetDto;
import com.expense.app.dto.ExpenseDto;
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
import java.util.Optional;

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


    public List<BudgetDto> getBudgetCategory(String token) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String authId = tokenModel.getId();
        return budgetRepository.findBudgets(authId).orElse(null);
    }


    public BudgetEntity createUserBudget(String token, BudgetDto budgetDto){
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        UserEntity user = userRepository.findById(tokenModel.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        //System.out.println(budgetDto.getCategoryId());
        Optional<BudgetEntity> bud = budgetRepository.findByCategoryId(budgetDto.getCategoryId());
        Optional<List<BudgetDto>> userBudgets = budgetRepository.findBudgets(user.getAuthId());

        if(userBudgets.isPresent()){
            for(BudgetDto budgetDto1 : userBudgets.get()) {
                if (budgetDto1.getCategoryId().equals(budgetDto.getCategoryId())) {
                    BudgetEntity budgetEntity = budgetRepository.findByIdAndCategoryId(user.getAuthId(), budgetDto1.getCategoryId());
                    budgetEntity.setBudgetAmount(budgetDto.getBudgetAmount() + budgetEntity.getBudgetAmount());
                    return budgetRepository.save(budgetEntity);
                }
            }
            BudgetEntity budgetEntity = new BudgetEntity();
            BeanUtils.copyProperties(budgetDto, budgetEntity);
            CategoryEntity category = categoryRepository.findByCategoryId(budgetDto.getCategoryId());
            budgetEntity.setCategory(category);
            budgetEntity.setUser(user);
            return budgetRepository.save(budgetEntity);

        }
            BudgetEntity budgetEntity = new BudgetEntity();
            BeanUtils.copyProperties(budgetDto, budgetEntity);
            CategoryEntity category = categoryRepository.findByCategoryId(budgetDto.getCategoryId());
            budgetEntity.setCategory(category);
            budgetEntity.setUser(user);
            return budgetRepository.save(budgetEntity);
    }

    public BudgetEntity updateUserBudget(String token, Integer id, BudgetDto budgetDetails){
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        UserEntity user = userRepository.findById(tokenModel.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        BudgetEntity budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));

        CategoryEntity category = categoryRepository.findByCategoryId(budgetDetails.getCategoryId());
        budget.setCategory(category);
        budget.setBudgetAmount(budgetDetails.getBudgetAmount());

        return budgetRepository.save(budget);

    }

    public void deleteUserBudget(String token, Integer id) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        UserEntity user = userRepository.findById(tokenModel.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        BudgetEntity existingBudget = budgetRepository.findById(id).orElseThrow(() -> new RuntimeException("Budget not found"));
        budgetRepository.delete(existingBudget);
    }


}
