package com.example.whatsfood.Model;

import android.media.Image;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class Seller extends Client implements Serializable {
    public String storeName;
    public String storeDescription;
    public int warningLevel;

    public Seller() {
        super();
    }

    public Seller(String username, String avatarUrl, String address, String phone, String storeName, String storeDescription, int warningLevel) {
        super(username, avatarUrl, address, phone);
        this.storeName = storeName;
        this.storeDescription = storeDescription;
        this.warningLevel = warningLevel;
    }

    public void SendRegisterRequest() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("SellerRegister").child(user.getUid()).setValue(this);
        FirebaseAuth.getInstance().signOut();
    }

    public boolean UpdateDataToServer() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Seller").child(user.getUid()).setValue(this);
        return true;
    }
}