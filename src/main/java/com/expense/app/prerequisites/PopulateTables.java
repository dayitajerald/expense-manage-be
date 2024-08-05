package com.expense.app.prerequisites;


import com.expense.app.dto.Recommendation;
import com.expense.app.repository.RecommendationRepository;
import com.expense.app.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

@Component
public class PopulateTables {

    @Autowired
    RecommendationRepository recommendationRepo;

    @Autowired
    GeminiService recommendationService;


    @Value("${base.url}")
    private String baseUrl;


    public void initializeRecommendation() {
        // Add test users
        recommendationRepo.truncateTable();
        List<Recommendation> testRecommendation = new ArrayList<>();

        Recommendation recom1 = new Recommendation();
        recom1.setCategory("Grocery Shopping");
        recom1.setSuggestion("Consider meal planning to reduce food waste and potentially lower grocery bills. You could also explore discount grocery stores or use coupons to save money.");
        recom1.setExample("Instead of buying items on impulse, create a weekly meal plan and only purchase what you need.");
        testRecommendation.add(recom1);

        Recommendation recom2 = new Recommendation();
        recom2.setCategory("Fuel");
        recom2.setSuggestion("Combine errands to minimize driving and look for gas stations with lower prices. Carpooling or using public transportation when possible can also help.");
        recom2.setExample("Plan your errands to avoid multiple trips and use apps to compare gas prices in your area.");
        testRecommendation.add(recom2);

        Recommendation recom3 = new Recommendation();
        recom3.setCategory("Dining Out");
        recom3.setSuggestion("Cook more meals at home to cut down on dining out expenses. If you do go out, consider less expensive options like lunch menus or sharing dishes.");
        recom3.setExample("Try cooking dinner at home 3 nights a week instead of eating out.");
        testRecommendation.add(recom3);

        Recommendation recom4 = new Recommendation();
        recom4.setCategory("Cloths");
        recom4.setSuggestion("Shop for clothes during sales or clearance events. Consider buying quality items that last longer to reduce the need for frequent purchases.");
        recom4.setExample("Wait for seasonal sales or shop at secondhand stores to find affordable clothes.");
        testRecommendation.add(recom4);

        Recommendation recom5 = new Recommendation();
        recom5.setCategory("Transport");
        recom5.setSuggestion("Explore alternative transportation options like cycling or walking for shorter distances. Consider using ride-sharing services or public transportation when cost-effective.");
        recom5.setExample("Use public transport for your commute instead of driving if it's available and convenient.");
        testRecommendation.add(recom5);

        Recommendation recom6 = new Recommendation();
        recom6.setCategory("Others");
        recom6.setSuggestion("Identify unnecessary spending in this category and try to reduce or eliminate it. Consider creating a budget to track spending and identify areas where savings are possible.");
        recom6.setExample("Review your subscription services and cancel any you don't actively use.");
        testRecommendation.add(recom6);


        for (Recommendation recommendation : testRecommendation) {
            recommendationService.addRecommendation(recommendation);
        }

    }

    public void initialize() {
        initializeRecommendation();
    }
}
