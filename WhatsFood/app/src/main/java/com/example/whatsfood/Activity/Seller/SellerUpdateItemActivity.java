package com.example.whatsfood.Activity.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whatsfood.Model.Buyer;
import com.example.whatsfood.Model.Food;
import com.example.whatsfood.R;
import com.example.whatsfood.UI_Functions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SellerUpdateItemActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 3;
    ImageButton backBtn;
    AppCompatButton updateBtn;
    ImageView foodImageView;
    TextInputLayout foodNameTextInputLayout, descriptionTextInputLayout, priceTextInputLayout, quantityTextInputLayout;
    String foodId, foodName, description, price, quantity, sellerId, storeName;
    Uri imageUri = null;

    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_update_item);

        Food food = (Food) getIntent().getSerializableExtra("Food");
        foodId = food.getFoodId();

        backBtn = (ImageButton) findViewById(R.id.back_button_seller_update_item);
        updateBtn = (AppCompatButton) findViewById(R.id.update_button_seller_update_item);
        foodImageView = (ImageView) findViewById(R.id.image_view_seller_update_item);
        foodNameTextInputLayout = (TextInputLayout) findViewById(R.id.food_name_text_input_layout_seller_update_item);
        descriptionTextInputLayout = (TextInputLayout) findViewById(R.id.description_text_input_layout_seller_update_item);
        priceTextInputLayout = (TextInputLayout) findViewById(R.id.price_text_input_layout_seller_update_item);
        quantityTextInputLayout = (TextInputLayout)  findViewById(R.id.quantity_text_input_layout_seller_update_item);

        Picasso.get().load(food.getImageUrl()).fit().into(foodImageView);
        foodNameTextInputLayout.getEditText().setText(food.getName());
        descriptionTextInputLayout.getEditText().setText(food.getDescription());
        priceTextInputLayout.getEditText().setText("" + food.getPrice());
        quantityTextInputLayout.getEditText().setText("" + food.getQuantity());

        refeshTextInputLayout(foodNameTextInputLayout);
        refeshTextInputLayout(descriptionTextInputLayout);
        refeshTextInputLayout(priceTextInputLayout);
        refeshTextInputLayout(quantityTextInputLayout);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        foodImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodName = foodNameTextInputLayout.getEditText().getText().toString().trim();
                description = descriptionTextInputLayout.getEditText().getText().toString().trim();
                price = priceTextInputLayout.getEditText().getText().toString().trim();
                quantity = quantityTextInputLayout.getEditText().getText().toString().trim();

                if (isValid()) {
                    ProgressDialog progressDialog = new ProgressDialog(SellerUpdateItemActivity.this);
                    progressDialog.setTitle("UPDATE FOOD!");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();



                    if (imageUri != null) {
                        StorageReference imageStorageReference =  FirebaseStorage.getInstance().getReferenceFromUrl(food.getImageUrl());
                        imageStorageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                String foodId = databaseRef.child("Food").push().getKey();
                                storageRef.child("Food").child(foodId).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        storageRef.child("Food").child(foodId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                updateData(food.getFoodId(), foodName, description, Integer.valueOf(price), Integer.valueOf(quantity), String.valueOf(uri));
                                                progressDialog.dismiss();
                                                Dialog dialog = new Dialog(SellerUpdateItemActivity.this);
                                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                dialog.setContentView(R.layout.popup);
                                                Window window = dialog.getWindow();
                                                if (window == null) {
                                                    return;
                                                }
                                                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                WindowManager.LayoutParams windowAttributes = window.getAttributes();
                                                windowAttributes.gravity = Gravity.CENTER;
                                                window.setAttributes(windowAttributes);
                                                dialog.setCancelable(false);
                                                //Set text
                                                TextView popup_text = dialog.findViewById(R.id.popup_text);
                                                popup_text.setText("Your changes have been updated");
                                                //Set button
                                                Button ok_button = dialog.findViewById(R.id.yes);
                                                ok_button.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        dialog.dismiss();
                                                        finish();
                                                    }
                                                });

                                                dialog.show();
                                            }
                                        });

                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                        progressDialog.setMessage("Please waiting... " + snapshot.getBytesTransferred() * 100.0 / snapshot.getTotalByteCount() + " %");
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                UI_Functions.CreatePopup(SellerUpdateItemActivity.this, "Let's try to change at another time");
                            }
                        });
                    } else {
                        updateData(food.getFoodId(), foodName, description, Integer.valueOf(price), Integer.valueOf(quantity), food.getImageUrl());
                        progressDialog.dismiss();
                        finish();
                    }
                }

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).fit().centerCrop().into(foodImageView);
        }
    }

    private void updateData(String foodId, String foodName, String description, int price, int quantity, String imageUrl) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/quantity/", quantity);
        childUpdates.put("/price/", price);
        childUpdates.put("/name/", foodName);
        childUpdates.put("/description/", description);
        childUpdates.put("/imageUrl/", imageUrl);
        databaseReference.child("Food").child(foodId).updateChildren(childUpdates);

        childUpdates.remove("/quantity/");
        childUpdates.remove("/description/");
        databaseReference.child("Buyer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ss : snapshot.getChildren()) {
                    Buyer buyer = ss.getValue(Buyer.class);
                    for (int j = 0; j < buyer.cartDetailList.size(); j++) {
                        if (buyer.cartDetailList.get(j).getFoodId().equals(foodId)) {
                            databaseReference.child("Buyer").child(ss.getKey()).child("cartDetailList").child("" + j).updateChildren(childUpdates);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private boolean isValid() {
        foodNameTextInputLayout.setErrorEnabled(false);
        foodNameTextInputLayout.setError("");

        descriptionTextInputLayout.setErrorEnabled(false);
        descriptionTextInputLayout.setError("");

        priceTextInputLayout.setErrorEnabled(false);
        priceTextInputLayout.setError("");

        quantityTextInputLayout.setErrorEnabled(false);
        quantityTextInputLayout.setError("");

        boolean validFoodName = true, validDescription = true, validPrice = true, validQuantity = true;

        if (foodName.isEmpty()) {
            foodNameTextInputLayout.setErrorEnabled(true);
            foodNameTextInputLayout.setError("Food Name is required!");
            validFoodName = false;
        }

        if (description.isEmpty()) {
            descriptionTextInputLayout.setErrorEnabled(true);
            descriptionTextInputLayout.setError("Description is required!");
            validDescription = false;
        }

        if (price.isEmpty()) {
            priceTextInputLayout.setErrorEnabled(true);
            priceTextInputLayout.setError("Price is required!");
            validPrice = false;
        } else {
            if (!isNumeric(price)) {
                priceTextInputLayout.setErrorEnabled(true);
                priceTextInputLayout.setError("Your price is not valid!");
                validPrice = false;
            }
        }

        if (quantity.isEmpty()) {
            quantityTextInputLayout.setErrorEnabled(true);
            quantityTextInputLayout.setError("Quantity is required!");
            validQuantity = false;
        } else {
            if (!isNumeric(quantity)) {
                quantityTextInputLayout.setErrorEnabled(true);
                quantityTextInputLayout.setError("Your quantity is not valid!");
                validQuantity = false;
            }
        }

        return validFoodName && validDescription && validPrice && validQuantity;
    }

    private boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) < '0' || str.charAt(i) > '9') {
                return false;
            }
        }
        return true;
    }
    private void refeshTextInputLayout(TextInputLayout input) {
        input.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                input.setErrorEnabled(true);
                input.setError("");
            }
        });
    }
}