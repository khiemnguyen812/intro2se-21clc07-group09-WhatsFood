package com.example.whatsfood.Activity.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsfood.Activity.LoginActivity;
import com.example.whatsfood.Model.Seller;
import com.example.whatsfood.R;
import com.example.whatsfood.UI_Functions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterDetailActivity extends AppCompatActivity {
    Button ok_button;
    TextView popup_text;
    ImageButton back_button;
    Button accept_button, reject_button;
    String seller_id;
    Seller seller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_seller_register__request_details);
        ((TextView)findViewById(R.id.header)).setText("Store Detail");
        Bundle extras = getIntent().getExtras();
        back_button = (ImageButton)findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        accept_button = (Button)findViewById(R.id.accept_button);
        accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accept_button.setEnabled(false);
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("SellerRegister").child(seller_id).removeValue();
                mDatabase.child("User").child(seller_id).child("role").setValue("seller");
                mDatabase.child("Seller").child(seller_id).setValue(seller);
                UI_Functions.CreatePopup(RegisterDetailActivity.this, "Accepted Successfully!");
                finish();
            }
        });
        reject_button = (Button)findViewById(R.id.reject_button);
        reject_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reject_button.setEnabled(false);
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("SellerRegister").child(seller_id).removeValue();
                mDatabase.child("User").child(seller_id).removeValue();
                UI_Functions.CreatePopup(RegisterDetailActivity.this, "Rejected Successfully!");
                finish();
            }
        });

        extras = getIntent().getExtras();
        seller_id = extras.getString("seller_id");
        seller = (Seller)extras.getSerializable("seller");

        ((TextView)findViewById(R.id.username)).setText("Username: " + seller.username);
        ((TextView)findViewById(R.id.store_name)).setText(seller.storeName);
        ((TextView)findViewById(R.id.description)).setText("Description: " + seller.storeDescription);
        ((TextView)findViewById(R.id.address)).setText("Address: " + seller.address);
        ((TextView)findViewById(R.id.phone)).setText("Phone: " + seller.phone);
        FirebaseUser fb_user = FirebaseAuth.getInstance().getCurrentUser();
        ((TextView)findViewById(R.id.email)).setText("Email: "+ fb_user.getEmail());
    }
}
