package com.example.whatsfood.Model;

import com.example.whatsfood.Model.Food;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Order implements Serializable {


    public String orderId, buyerId, buyerName, sellerId, ship_to, status, denialReason;
    public int totalMoney;
    public ArrayList<CartDetail> foodList;

    public Order() {
        this.orderId = null;
        this.buyerId = null;
        this.buyerName = null;
        this.sellerId = null;
        this.ship_to = null;
        this.status = null;
        this.denialReason = null;
        this.totalMoney = 0;
        this.foodList = new ArrayList<CartDetail>();
    }
    public Order(String orderId, String buyerId, String buyerName, String sellerId, String ship_to, String status, String denialReason, int totalMoney, ArrayList<CartDetail> foodList) {
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.sellerId = sellerId;
        this.ship_to = ship_to;
        this.status = status;
        this.denialReason = denialReason;
        this.totalMoney = totalMoney;
        this.foodList = foodList;
    }

    public Order(String orderId, String buyerId, String buyerName, String sellerId, String ship_to, int totalMoney) {
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.sellerId = sellerId;
        this.ship_to = ship_to;
        this.totalMoney = totalMoney;
        this.status = "Waiting";
        this.denialReason = "212313";
        this.foodList = new ArrayList<CartDetail>();
        this.foodList.add(new CartDetail());
    }

    public Order(String orderId, String buyerId, String buyerName, String sellerId, String ship_to, int totalMoney, ArrayList<CartDetail> foodList) {
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.sellerId = sellerId;
        this.ship_to = ship_to;
        this.totalMoney = totalMoney;
        this.foodList = foodList;
        this.status = "Waiting";
        this.denialReason = "212313";
    }

    public String getOrderId() {
        return orderId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getShip_to() {
        return ship_to;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public ArrayList<CartDetail> getFoodList() {
        return foodList;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) { this.status = status; }
    public String getDenialReason() {return denialReason;}
    public void setDenialReason(String denialReason) { this.denialReason = denialReason; }

    public boolean UpdateDataToServer() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Order").child(this.orderId).setValue(this);
        return true;
    }
}
