package com.bank.star.star.controller;


import com.bank.star.star.DTO.RecommendationResponse;
import com.bank.star.star.service.RecommendationService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<RecommendationResponse> getRecommendations(@PathVariable String userId) {
        try {
            UUID uuid = UUID.fromString(userId);
            RecommendationResponse response = recommendationService.getRecommendations(uuid);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // В случае неверного формата UUID возвращаем пустой список рекомендаций
            RecommendationResponse response = new RecommendationResponse(userId, new java.util.ArrayList<>());
            return ResponseEntity.ok(response);
        }
    }
}
