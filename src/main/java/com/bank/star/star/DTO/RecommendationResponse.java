package com.bank.star.star.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RecommendationResponse{

    @JsonProperty("user_id")
    private String userId;

    private List<ProductRecommendation> recommendations;

    public RecommendationResponse(String userId, List<ProductRecommendation> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }

    public String getUserId() {
        return userId; }
    public void setUserId(String userId) {
        this.userId = userId; }

    public List<ProductRecommendation> getRecommendations() {
        return recommendations; }
    public void setRecommendations(List<ProductRecommendation> recommendations) {
        this.recommendations = recommendations;
    }
}
