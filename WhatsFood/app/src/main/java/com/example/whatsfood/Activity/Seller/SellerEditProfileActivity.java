package com.example.whatsfood.Activity.Seller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsfood.Activity.AfterRegisterActivity;
import com.example.whatsfood.FormatTextWatcher;
import com.example.whatsfood.Model.Buyer;
import com.example.whatsfood.Model.Seller;
import com.example.whatsfood.Model.User;
import com.example.whatsfood.R;
import com.example.whatsfood.UI_Functions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.EnumSet;
import java.util.UUID;

public class SellerEditProfileActivity extends AppCompatActivity {
    final int GALLERY_REQ_CODE = 1000;
    ImageButton back_button, avatar_button;
    Button apply_button;
    private FirebaseAuth mAuth;
    EditText username, store_name, description, address, phone;
    ImageView avatar;
    Uri imgUri;
    Seller seller;
    boolean imgChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_edit_profile);
        ((TextView)findViewById(R.id.header)).setText("Edit Profile");
        mAuth = FirebaseAuth.getInstance();
        //EditTexts
        username = (EditText)findViewById(R.id.username);
        store_name = (EditText)findViewById(R.id.store_name);
        description = (EditText)findViewById(R.id.description);
        address = (EditText)findViewById(R.id.address);
        phone = (EditText)findViewById(R.id.phone);
        //Buttons
        back_button = (ImageButton)findViewById(R.id.back_button);
        apply_button = (Button)findViewById(R.id.apply_button);
        avatar_button = (ImageButton)findViewById(R.id.avatar_button);
        avatar = (ImageView)findViewById(R.id.avatar);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        apply_button.setOnClickListener(new View.OnClickListener() {
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
        //Add TextWatchers

        FormatTextWatcher watcher3 = new FormatTextWatcher(description);
        watcher3.modes = EnumSet.of(FormatTextWatcher.mode.CHECK_EMPTY);

        FormatTextWatcher watcher4 = new FormatTextWatcher(store_name);
        watcher4.modes = EnumSet.of(FormatTextWatcher.mode.CHECK_EMPTY);

        FormatTextWatcher watcher5 = new FormatTextWatcher(address);
        watcher5.modes = EnumSet.of(FormatTextWatcher.mode.CHECK_EMPTY);

        FormatTextWatcher watcher6 = new FormatTextWatcher(phone);
        watcher6.modes = EnumSet.of(FormatTextWatcher.mode.CHECK_EMPTY, FormatTextWatcher.mode.CHECK_ONLY_NUMBER);

        Bundle extras = getIntent().getExtras();
        seller = (Seller)extras.getSerializable("seller");
        store_name.setText(seller.storeName);
        description.setText(seller.storeDescription);
        address.setText(seller.address);
        phone.setText(seller.phone);
        UI_Functions.LoadImageToImageView(avatar, seller.avatarUrl);
        ViewGroup.LayoutParams layoutParams = avatar.getLayoutParams();
        layoutParams.height = 0;
        avatar.setLayoutParams(layoutParams);

        imgChange = false;
    }

    private void verify_information() {
        apply_button.setEnabled(false);

        String str_store_name = store_name.getText().toString();
        String str_description = description.getText().toString();
        String str_address = address.getText().toString();
        String str_phone = phone.getText().toString();

        //Check empty fields
        if (str_store_name.isEmpty() || str_description.isEmpty() || str_address.isEmpty() || str_phone.isEmpty() ||avatar.getDrawable() == null
        ) {
            UI_Functions.CreatePopup(SellerEditProfileActivity.this, getString(R.string.missing_information));
            apply_button.setEnabled(true);
            return;
        }

        //Check invalid fields
        if (store_name.getError() != null || description.getError() != null || address.getError() != null || phone.getError() != null) {
            UI_Functions.CreatePopup(SellerEditProfileActivity.this, getString(R.string.invalid_field));
            apply_button.setEnabled(true);
            return;
        }

        seller = new Seller(seller.username, seller.avatarUrl, str_address, str_phone, str_store_name, str_description, seller.warningLevel);
        seller.UpdateDataToServer();
        if (imgChange) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imgRef = storageRef.child(seller.avatarUrl);
            UploadTask uploadTask = imgRef.putFile(imgUri);
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                }
            });
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQ_CODE) {
            imgUri = data.getData();
            avatar.setImageURI(imgUri);
            imgChange = true;
        }
    }
}