package com.example.whatsfood.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsfood.Activity.Buyer.RegisterBuyerActivity;
import com.example.whatsfood.FormatTextWatcher;
import com.example.whatsfood.R;
import com.example.whatsfood.UI_Functions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.EnumSet;

public class ChangePasswordActivity extends AppCompatActivity {
    Button confirm_button;
    ImageButton back_button;
    EditText current_password, password, confirm_password;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ((TextView)findViewById(R.id.header)).setText("Change password");
        confirm_button = (Button)findViewById(R.id.confirm_button);
        back_button = (ImageButton)findViewById(R.id.back_button);
        current_password = (EditText)findViewById(R.id.current_password);
        password = (EditText)findViewById(R.id.password);
        confirm_password = (EditText)findViewById(R.id.confirm_password);
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_current_password = current_password.getText().toString();
                String str_password = password.getText().toString();
                String str_confirm_password = confirm_password.getText().toString();
                if (str_password.isEmpty() || str_confirm_password.isEmpty()) {
                    UI_Functions.CreatePopup(ChangePasswordActivity.this, getString(R.string.missing_information));
                    return;
                }
                if (!str_password.equals(str_confirm_password)) {
                    UI_Functions.CreatePopup(ChangePasswordActivity.this, getString(R.string.password_not_match));
                    return;
                }
                //Check invalid fields
                if (current_password.getError() != null || password.getError() != null || confirm_password.getError() != null) {
                    UI_Functions.CreatePopup(ChangePasswordActivity.this, getString(R.string.invalid_field));
                    return;
                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), str_current_password);
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(str_password)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        finish();
                                                    }
                                                    else {
                                                        UI_Functions.CreatePopup(ChangePasswordActivity.this, getString(R.string.error));
                                                    }
                                                }
                                            });
                                }
                                else {
                                    UI_Functions.CreatePopup(ChangePasswordActivity.this, "Incorrect old password");
                                }
                            }
                        });

            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        FormatTextWatcher watcher = new FormatTextWatcher(current_password);
        watcher.modes = EnumSet.of(FormatTextWatcher.mode.CHECK_EMPTY, FormatTextWatcher.mode.CHECK_HAS_LOWERCASE, FormatTextWatcher.mode.CHECK_HAS_UPPERCASE, FormatTextWatcher.mode.CHECK_HAS_NUMBER, FormatTextWatcher.mode.CHECK_NO_SPECIAL_CHARACTER);

        FormatTextWatcher watcher2 = new FormatTextWatcher(password);
        watcher2.modes = EnumSet.of(FormatTextWatcher.mode.CHECK_EMPTY, FormatTextWatcher.mode.CHECK_HAS_LOWERCASE, FormatTextWatcher.mode.CHECK_HAS_UPPERCASE, FormatTextWatcher.mode.CHECK_HAS_NUMBER, FormatTextWatcher.mode.CHECK_NO_SPECIAL_CHARACTER);
        TextWatcher confirm_password_watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!confirm_password.getText().toString().equals(password.getText().toString())) {
                    confirm_password.setError("Password not match");
                }
                else {
                    confirm_password.setError(null);
                }
            }
        };
        confirm_password.addTextChangedListener(confirm_password_watcher);
    }
}
