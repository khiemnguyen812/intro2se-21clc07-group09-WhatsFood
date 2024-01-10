package com.example.whatsfood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.whatsfood.Activity.Admin.AdminBottomNavigationActivity;
import com.example.whatsfood.Activity.Buyer.BuyerBottomNavigationActivity;
import com.example.whatsfood.Activity.Seller.SellerBottomNavigationActivity;
import com.example.whatsfood.Activity.Seller.SellerOrderDetailsActivity;
import com.example.whatsfood.Activity.Seller.SellerViewSelectedFoodActivity;
import com.example.whatsfood.R;
import com.example.whatsfood.UI_Functions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView logo = (TextView) findViewById(R.id.main_activity_logo);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(fadeIn);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onStart();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        }, 600);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            FirebaseDatabase.getInstance().getReference().child("User").child(currentUser.getUid()).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String role = snapshot.getValue(String.class);
                    switch (role) {
                        case "seller": {
                            Intent intent = new Intent(MainActivity.this, SellerBottomNavigationActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            break;
                        }
                        case "buyer": {
                            Intent intent = new Intent(MainActivity.this, BuyerBottomNavigationActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            break;
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}