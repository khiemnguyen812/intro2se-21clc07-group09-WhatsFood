package com.example.whatsfood.Model;

import java.io.Serializable;

public class CartDetail implements Serializable {
    private String foodId;

    private String imageUrl;
    private String name;
    private String sellerId;

    private int price;
    private int number;

    public CartDetail() {
        this.foodId = "temp@#$%!";
        this.imageUrl = "temp";
        this.name = "temp";
        this.sellerId = "temp";
        this.price = 0;
        this.number = 0;
    }


    public CartDetail(String foodId, String imageUrl, String name, String sellerId, int price, int number) {
        this.foodId = foodId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.sellerId = sellerId;
        this.price = price;
        this.number = number;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


}
