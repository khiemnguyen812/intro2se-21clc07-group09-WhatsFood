package com.example.whatsfood.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsfood.Activity.Admin.AdminBottomNavigationActivity;
import com.example.whatsfood.Activity.Buyer.BuyerBottomNavigationActivity;
import com.example.whatsfood.Activity.Buyer.BuyerViewSelectedFoodActivity;
import com.example.whatsfood.Activity.Buyer.RegisterBuyerActivity;
import com.example.whatsfood.Activity.Seller.SellerBottomNavigationActivity;
import com.example.whatsfood.FormatTextWatcher;
import com.example.whatsfood.Model.Seller;
import com.example.whatsfood.R;
import com.example.whatsfood.UI_Functions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.EnumSet;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    Dialog dialog;
    Button login_button;
    TextView register_button;
    EditText username_edittext;
    EditText password_edittext;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dialog = new Dialog(this);
        mAuth = FirebaseAuth.getInstance();
        login_button = (Button)findViewById(R.id.login_button);
        register_button = (TextView)findViewById(R.id.register_text);
        username_edittext = (EditText)findViewById(R.id.store_name);
        password_edittext = (EditText)findViewById(R.id.password);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SelectAccountTypeActivity.class);
                startActivity(intent);
            }
        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        //Add TextWatchers
        FormatTextWatcher username_watcher = new FormatTextWatcher(username_edittext);
        username_watcher.modes = EnumSet.of(FormatTextWatcher.mode.CHECK_EMPTY);

        FormatTextWatcher password_watcher = new FormatTextWatcher(password_edittext);
        password_watcher.modes = EnumSet.of(FormatTextWatcher.mode.CHECK_EMPTY);

    }
    private void login() {
        login_button.setEnabled(false);
        String username_email = username_edittext.getText().toString();
        String password = password_edittext.getText().toString();
        if (username_email.isEmpty() || password.isEmpty()) {
            UI_Functions.CreatePopup(LoginActivity.this, getString(R.string.missing_information));
            login_button.setEnabled(true);
            return;
        }
        if (username_edittext.getError() != null || password_edittext.getError() != null) {
            UI_Functions.CreatePopup(LoginActivity.this, getString(R.string.invalid_field));
            login_button.setEnabled(true);
            return;
        }
        if (username_email.contains("@")) {
            firebase_login(username_email, password);
        }
        else {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");
            mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                    }
                    else {
                        boolean hasUser = false;
                        for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                            String username = String.valueOf(dataSnapshot.child("username").getValue());
                            if (username.equals(username_email)) {
                                String email = String.valueOf(dataSnapshot.child("email").getValue());
                                firebase_login(email, password);
                                hasUser = true;
                                break;
                            }
                        }
                        if (!hasUser) {
                            UI_Functions.CreatePopup(LoginActivity.this, "Account not exists");
                        }
                    }
                }
            });
        }
        //Call sign in method from mAuth

    }

    private void firebase_login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseUser fb_user = FirebaseAuth.getInstance().getCurrentUser();
                            if (fb_user == null) {
                                login_button.setEnabled(true);
                                return;
                            }
                            //Navigate to corresponding home activity
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("User").child(fb_user.getUid()).child("role").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        login_button.setEnabled(true);
                                    }
                                    else {
                                        String role = String.valueOf(task.getResult().getValue());
                                        switch (role) {
                                            case "seller": {
                                                Intent intent = new Intent(LoginActivity.this, SellerBottomNavigationActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                break;
                                            }
                                            case "buyer": {
                                                Intent intent = new Intent(LoginActivity.this, BuyerBottomNavigationActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                break;
                                            }
                                            case "admin": {
                                                Intent intent = new Intent(LoginActivity.this, AdminBottomNavigationActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                break;
                                            }
                                            case "seller_register":
                                                UI_Functions.CreatePopup(LoginActivity.this, "Your account is currently awaiting confirmation");
                                                login_button.setEnabled(true);
                                                FirebaseAuth.getInstance().signOut();
                                                break;
                                        }
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            FirebaseAuthException exception = (FirebaseAuthException) task.getException();
                            if (exception != null) {
                                Log.w("firebase", exception.getErrorCode());
                                if (exception.getErrorCode() == "ERROR_USER_NOT_FOUND") {
                                    UI_Functions.CreatePopup(LoginActivity.this, "Account not exists");
                                }
                                else if (exception.getErrorCode() == "ERROR_WRONG_PASSWORD") {
                                    UI_Functions.CreatePopup(LoginActivity.this, "Wrong password");
                                }
                            }
                            login_button.setEnabled(true);
                        }
                    }
                });
    }
}


