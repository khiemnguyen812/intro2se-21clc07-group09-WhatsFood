package com.example.whatsfood.Model;

import java.io.Serializable;

public class Report implements Serializable {
    String buyerId,content,foodId,reportId,sellerId;

    public Report(String buyerId, String content, String foodId, String reportId, String sellerId) {
        this.buyerId = buyerId;
        this.content = content;
        this.foodId = foodId;
        this.reportId = reportId;
        this.sellerId = sellerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
