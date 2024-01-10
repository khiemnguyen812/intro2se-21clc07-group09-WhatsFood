package com.example.whatsfood.Activity.Seller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsfood.Activity.AfterRegisterActivity;
import com.example.whatsfood.Activity.Buyer.RegisterBuyerActivity;
import com.example.whatsfood.FormatTextWatcher;
import com.example.whatsfood.Model.Seller;
import com.example.whatsfood.Model.User;
import com.example.whatsfood.R;
import com.example.whatsfood.UI_Functions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.EnumSet;

public class RegisterSellerActivity extends AppCompatActivity {
    final int GALLERY_REQ_CODE = 1000;
    ImageButton back_button, avatar_button;
    Button submit_button;
    private FirebaseAuth mAuth;
    EditText username, password, confirm_password, email, store_name, store_description, address, phone;
    ImageView avatar;
    Uri imgUri;
    boolean valid_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);
        ((TextView)findViewById(R.id.header)).setText("Seller Register");
        mAuth = FirebaseAuth.getInstance();
        //EditTexts
        username = (EditText)findViewById(R.id.username);
        avatar_button = (ImageButton)findViewById(R.id.avatar_button);
        password = (EditText)findViewById(R.id.password);
        confirm_password = (EditText)findViewById(R.id.confirm_password);
        email = (EditText)findViewById(R.id.email);
        store_name = (EditText)findViewById(R.id.store_name);
        store_description = (EditText)findViewById(R.id.store_description);
        address = (EditText)findViewById(R.id.address);
        phone = (EditText)findViewById(R.id.phone);
        //Buttons
        back_button = (ImageButton)findViewById(R.id.back_button);
        submit_button = (Button)findViewById(R.id.submit_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify_information();
            }
        });
        avatar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQ_CODE);
            }
        });
        avatar = (ImageView)findViewById(R.id.avatar);
        //Add TextWatchers
        FormatTextWatcher watcher1 = new FormatTextWatcher(username);
        watcher1.minLength = 6;
        watcher1.maxLength = 20;
        watcher1.modes = EnumSet.of(FormatTextWatcher.mode.CHECK_LENGTH, FormatTextWatcher.mode.CHECK_EMPTY);

        FormatTextWatcher watcher2 = new FormatTextWatcher(password);
        watcher2.minLength = 6;
        watcher2.maxLength = 20;
        watcher2.modes = EnumSet.of(FormatTextWatcher.mode.CHECK_LENGTH, FormatTextWatcher.mode.CHECK_EMPTY, FormatTextWatcher.mode.CHECK_HAS_LOWERCASE, FormatTextWatcher.mode.CHECK_HAS_UPPERCASE, FormatTextWatcher.mode.CHECK_HAS_NUMBER, FormatTextWatcher.mode.CHECK_NO_SPECIAL_CHARACTER);

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

        FormatTextWatcher watcher3 = new FormatTextWatcher(email);
        watcher3.minLength = 6;
        watcher3.maxLength = 254;
        watcher3.modes = EnumSet.of(FormatTextWatcher.mode.CHECK_LENGTH, FormatTextWatcher.mode.CHECK_EMPTY);

        FormatTextWatcher watcher4 = new FormatTextWatcher(store_name);
        watcher4.minLength = 6;
        watcher4.maxLength = 30;
        watcher4.modes = EnumSet.of(FormatTextWatcher.mode.CHECK_LENGTH, FormatTextWatcher.mode.CHECK_EMPTY);

        FormatTextWatcher watcher5 = new FormatTextWatcher(address);
        watcher5.modes = EnumSet.of(FormatTextWatcher.mode.CHECK_EMPTY);

        FormatTextWatcher watcher6 = new FormatTextWatcher(phone);
        watcher6.minLength = 10;
        watcher6.maxLength = 11;
        watcher6.modes = EnumSet.of(FormatTextWatcher.mode.CHECK_LENGTH, FormatTextWatcher.mode.CHECK_EMPTY, FormatTextWatcher.mode.CHECK_ONLY_NUMBER);

        FormatTextWatcher watcher7 = new FormatTextWatcher(store_description);
        watcher7.modes = EnumSet.of(FormatTextWatcher.mode.CHECK_EMPTY);
    }

    private void verify_information() {
        submit_button.setEnabled(false);

        String str_username = username.getText().toString();
        String str_password = password.getText().toString();
        String str_confirm_password = confirm_password.getText().toString();
        String str_email = email.getText().toString();
        String str_store_name = store_name.getText().toString();
        String str_store_description = store_description.getText().toString();
        String str_address = address.getText().toString();
        String str_phone = phone.getText().toString();

        //Check empty fields
        if (str_username.isEmpty() ||
                str_password.isEmpty() ||
                str_confirm_password.isEmpty() ||
                str_email.isEmpty() ||
                str_store_name.isEmpty() ||
                str_store_description.isEmpty() ||
                str_address.isEmpty()
        ) {
            UI_Functions.CreatePopup(RegisterSellerActivity.this, getString(R.string.missing_information));
            submit_button.setEnabled(true);
            return;
        }
        if (!str_password.equals(str_confirm_password)) {
            UI_Functions.CreatePopup(RegisterSellerActivity.this, getString(R.string.password_not_match));
            submit_button.setEnabled(true);
            return;
        }

        //Check invalid fields
        if (username.getError() != null ||
                password.getError() != null ||
                confirm_password.getError() != null ||
                email.getError() != null ||
                store_name.getError() != null ||
                store_description.getError() != null ||
                address.getError() != null ||
                phone.getError() != null) {
            UI_Functions.CreatePopup(RegisterSellerActivity.this, getString(R.string.invalid_field));
            submit_button.setEnabled(true);
            return;
        }
        //Check duplicated data
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
                        if (username.equals(str_username)) {
                            UI_Functions.CreatePopup(RegisterSellerActivity.this, "Username already exists");
                            hasUser = true;
                            break;
                        }
                    }
                    if (!hasUser) {
                        //Try to create account
                        mAuth.createUserWithEmailAndPassword(str_email, str_password)
                                .addOnCompleteListener(RegisterSellerActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            if (user != null) {
                                                // User is signed in
                                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                                String avatarPath = User.UploadImage(imgUri, "Avatar");
                                                Seller seller = new Seller(str_username, avatarPath, str_address, str_phone, str_store_name, str_store_description, 0);
                                                mDatabase.child("User").child(user.getUid()).child("role").setValue("seller_register");
                                                mDatabase.child("User").child(user.getUid()).child("username").setValue(str_username);
                                                mDatabase.child("User").child(user.getUid()).child("email").setValue(str_email);
                                                seller.SendRegisterRequest();
                                                Intent intent = new Intent(RegisterSellerActivity.this, AfterRegisterActivity.class);
                                                intent.putExtra("popup_text", getString(R.string.seller_register));
                                                startActivity(intent);
                                            } else {
                                                // No user is signed in
                                                submit_button.setEnabled(true);
                                            }
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            FirebaseAuthException exception = (FirebaseAuthException) task.getException();
                                            if (exception != null) {
                                                Log.w("firebase", exception.getErrorCode());
                                                if (exception.getErrorCode() == "ERROR_EMAIL_ALREADY_IN_USE") {
                                                    UI_Functions.CreatePopup(RegisterSellerActivity.this, getString(R.string.ERROR_EMAIL_ALREADY_IN_USE));
                                                }
                                                else if (exception.getErrorCode() == "ERROR_INVALID_EMAIL") {
                                                    UI_Functions.CreatePopup(RegisterSellerActivity.this, getString(R.string.invalid_email));
                                                }
                                                submit_button.setEnabled(true);
                                            }
                                        }
                                    }
                                });
                    }
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQ_CODE) {
            imgUri = data.getData();
            avatar.setImageURI(imgUri);
            ViewGroup.LayoutParams layoutParams = avatar.getLayoutParams();
            layoutParams.height = 0;
            avatar.setLayoutParams(layoutParams);
        }
    }
}
