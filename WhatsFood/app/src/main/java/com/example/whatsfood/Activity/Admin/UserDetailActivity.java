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
import com.example.whatsfood.Model.User;
import com.example.whatsfood.R;
import com.example.whatsfood.UI_Functions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDetailActivity extends AppCompatActivity {
    Button ok_button;
    TextView popup_text;
    ImageButton back_button;
    Button delete_button;
    String UserId;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_profile_detail);
        ((TextView)findViewById(R.id.HEADER)).setText("User Detail");
        Bundle extras = getIntent().getExtras();
        back_button = (ImageButton)findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        delete_button = (Button)findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_button.setEnabled(false);
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("User").child(UserId).removeValue();
                UI_Functions.CreatePopup(UserDetailActivity.this, "Deleted Successfully!");
                finish();
            }
        });

        extras = getIntent().getExtras();
        UserId = extras.getString("UserId");
        user = (User) extras.getSerializable("user");

        ((TextView)findViewById(R.id.username2)).setText("Username: " + user.username);
        ((TextView)findViewById(R.id.address2)).setText("Address: ");
        ((TextView)findViewById(R.id.phone2)).setText("Phone: " );
        ((TextView)findViewById(R.id.email2)).setText("Email: ");
    }
}
