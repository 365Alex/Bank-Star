package com.bank.star.star.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductRecommendation {

    private String name;
    private String id;
    private String text;

    public ProductRecommendation(String name, String id, String text) {
        this.name = name;
        this.id = id;
        this.text = text;
    }

    @JsonProperty("name")
    public String getName() {
        return name; }
    public void setName(String name) {
        this.name = name; }

    @JsonProperty("id")
    public String getId() {
        return id; }
    public void setId(String id) {
        this.id = id; }

    @JsonProperty("text")
    public String getText() {
        return text; }
    public void setText(String text) {
        this.text = text; }

}
