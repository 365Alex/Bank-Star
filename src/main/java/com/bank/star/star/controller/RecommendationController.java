package com.bank.star.star.controller;


import com.bank.star.star.DTO.RecommendationResponse;
import com.bank.star.star.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController (RecommendationService recommendationService){
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    public RecommendationResponse getRecommendations(@PathVariable UUID userId) {
            return recommendationService.getRecommendations(userId);
    }
}
