package com.expense.app.service;

import com.expense.app.dto.ExpenseDto;
import com.expense.app.dto.IncomeDto;
import com.expense.app.dto.TotalIncomeDto;
import com.expense.app.entity.CategoryEntity;
import com.expense.app.entity.ExpenseEntity;
import com.expense.app.entity.IncomeEntity;
import com.expense.app.entity.UserEntity;
import com.expense.app.middleware.JwtTokenUtil;
import com.expense.app.model.TokenModel;
import com.expense.app.repository.CategoryRepository;
import com.expense.app.repository.IncomeRepository;
import com.expense.app.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class IncomeService {
    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


//    public List<IncomeDto> getUserExpenses(String token) {
//        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
//        String authId = tokenModel.getId();
//        List<IncomeEntity> incomes = incomeRepository.findByUserId(authId);
//        List<IncomeDto> incomeDtos = new ArrayList<>();
//        for (IncomeEntity income : incomes) {
//            IncomeDto incomeDto = new IncomeDto();
//            incomeDto.setIncomeId(income.getIncomeId());
//            incomeDto.setAmount(income.getAmount());
//            incomeDto.setCategory(income.getCategoryId());
//            incomeDto.setDate(income.getDate().toString());
//            incomeDto.setUserName(income.getUser().getName());
//            incomeDtos.add(incomeDto);
//        }
//        return incomeDtos;
//    }

    public List<IncomeEntity> getUserIncomes(String token){
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String authId = tokenModel.getId();
        List<IncomeEntity> incomes = incomeRepository.findByUserId(authId);
        return incomes;
    }


    public IncomeEntity createUserExpense(String token, IncomeDto data) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        UserEntity user = userRepository.findById(tokenModel.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        IncomeEntity income = new IncomeEntity();
        BeanUtils.copyProperties(data,income);
        CategoryEntity category = categoryRepository.findByCategoryId(data.getCategory());
        income.setCategory(category);
        income.setUser(user);
        income.setDate(LocalDate.now());
        return incomeRepository.save(income);
    }

    public IncomeEntity updateIncomeField(Integer incomeId, String newValue) {
        IncomeEntity income = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        try {
                    income.setAmount(Float.parseFloat(newValue));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Error in api");
        }

        return incomeRepository.save(income);
    }

    public void deleteUserExpense(String token, Integer id) {
        IncomeEntity existingIncome = incomeRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
        incomeRepository.delete(existingIncome);
    }

    public TotalIncomeDto getTotalIncome(String token) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String authId = tokenModel.getId();
        TotalIncomeDto tid = new TotalIncomeDto();
        tid.setTotalIncome(incomeRepository.findSumOfIncomesByUserId(authId));
        return tid;
    }

    public double getTotalAmountForMonth(String month, String token) {
        TokenModel tokenModel = jwtTokenUtil.getTokenModelfromToken(token.split(" ")[1]);
        String userId = tokenModel.getId();
        return incomeRepository.getTotalAmountByMonth(month,userId);

    }
}
