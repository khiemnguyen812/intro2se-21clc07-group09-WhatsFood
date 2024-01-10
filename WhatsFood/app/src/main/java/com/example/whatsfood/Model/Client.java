package com.example.whatsfood.Model;

import android.media.Image;

public class Client extends User {
    public String phone;
    public String address;
    public Client() {
        super();
    }
    public Client(String username, String avatarUrl, String address, String phone) {
        super(username, avatarUrl);
        this.address = address;
        this.phone = phone;
    }
}

