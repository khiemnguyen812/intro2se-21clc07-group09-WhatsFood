package com.example.whatsfood.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsfood.Activity.Buyer.RegisterBuyerActivity;
import com.example.whatsfood.Activity.Seller.RegisterSellerActivity;
import com.example.whatsfood.R;

public class SelectAccountTypeActivity extends AppCompatActivity {
    ImageButton back_button;
    Button seller_button;
    Button buyer_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_select_type);
        back_button = (ImageButton)findViewById(R.id.back_button);
        seller_button = (Button)findViewById(R.id.Seller);
        buyer_button = (Button)findViewById(R.id.Buyer);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        seller_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectAccountTypeActivity.this, RegisterSellerActivity.class);
                startActivity(intent);
            }
        });
        buyer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectAccountTypeActivity.this, RegisterBuyerActivity.class);
                startActivity(intent);
            }
        });
    }
}
