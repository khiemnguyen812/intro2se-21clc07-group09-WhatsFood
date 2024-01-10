package com.example.whatsfood.Model;

import android.widget.Adapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Food implements Serializable {
    String foodId, name, description, imageUrl, sellerId, storeName;

    int price, quantity;
    ArrayList<String> comments;

    public Food() {
        this.foodId = "foodId";
        this.name = "name";
        this.description = "description";
        this.price = 0;
        this.imageUrl = "imageUrl";
        this.quantity = 0;
        this.comments = new ArrayList<String>();
        this.comments.add("empty");
        this.sellerId = "sellerId";
        this.storeName = "storeName";
    }

    public Food(String foodId, String name, String description, int price, String imageUrl, int quantity, String sellerId, String storeName, ArrayList<String> comments) {
        this.foodId = foodId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.comments = comments;
        this.sellerId = sellerId;
        this.storeName = storeName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getFoodId() {
        return foodId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSellerId() {
        return sellerId;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

}
