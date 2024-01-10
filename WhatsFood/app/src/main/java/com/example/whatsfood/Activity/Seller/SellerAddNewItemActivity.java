package com.example.whatsfood.Activity.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.whatsfood.Model.Food;
import com.example.whatsfood.R;
import com.example.whatsfood.UI_Functions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
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

import java.util.ArrayList;

public class SellerAddNewItemActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 3;
    ImageButton backBtn;
    AppCompatButton addBtn;
    ImageView foodImageView;
    TextInputLayout foodNameTextInputLayout, descriptionTextInputLayout, priceTextInputLayout, quantityTextInputLayout;
    String foodName, description, price, quantity, sellerId, storeName;
    Uri imageUri = null;

    // Create a Cloud Storage reference from the app
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_item);

        backBtn = (ImageButton) findViewById(R.id.back_button_seller_add_new_item);
        addBtn = (AppCompatButton) findViewById(R.id.add_button_seller_add_new_item);
        foodImageView = (ImageView) findViewById(R.id.image_view_seller_add_new_item);
        foodNameTextInputLayout = (TextInputLayout) findViewById(R.id.food_name_text_input_layout_seller_add_new_item);
        descriptionTextInputLayout = (TextInputLayout) findViewById(R.id.description_text_input_layout_seller_add_new_item);
        priceTextInputLayout = (TextInputLayout) findViewById(R.id.price_text_input_layout_seller_add_new_item);
        quantityTextInputLayout = (TextInputLayout)  findViewById(R.id.quantity_text_input_layout_seller_add_new_item);

        priceTextInputLayout.forceLayout();

        refreshTextInputLayout(foodNameTextInputLayout);
        refreshTextInputLayout(descriptionTextInputLayout);
        refreshTextInputLayout(priceTextInputLayout);
        refreshTextInputLayout(quantityTextInputLayout);

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

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodName = foodNameTextInputLayout.getEditText().getText().toString().trim();
                description = descriptionTextInputLayout.getEditText().getText().toString().trim();
                price = priceTextInputLayout.getEditText().getText().toString().trim();
                quantity = quantityTextInputLayout.getEditText().getText().toString().trim();


                if (isValid()) {
                    if (imageUri == null) {
                        // If sign in fails, display a message to the user.
                        UI_Functions.CreatePopup(SellerAddNewItemActivity.this, "You have to insert food image");
                        addBtn.setEnabled(true);
                    } else {
                        sellerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseRef.child("Seller").child(sellerId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                storeName = (String) snapshot.child("storeName").getValue();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                storeName = "Store";

                            }
                        });


                        String foodId = databaseRef.child("Food").push().getKey();

                        ProgressDialog progressDialog = new ProgressDialog(SellerAddNewItemActivity.this);
                        progressDialog.setTitle("UPLOAD FOOD!");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setCancelable(false);
                        progressDialog.show();


                        storageRef.child("Food").child(foodId).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageRef.child("Food").child(foodId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        progressDialog.dismiss();
                                        ArrayList<String> comments = new ArrayList<String>();
                                        comments.add("empty");
                                        Food food = new Food(foodId, foodName, description, Integer.valueOf(price), String.valueOf(uri), Integer.valueOf(quantity), sellerId, storeName, comments);
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Food").child(foodId);
                                        databaseReference.setValue(food).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                foodNameTextInputLayout.getEditText().setText("");
                                                descriptionTextInputLayout.getEditText().setText("");
                                                quantityTextInputLayout.getEditText().setText("");
                                                priceTextInputLayout.getEditText().setText("");
                                                foodImageView.setImageResource(R.drawable.plus);
                                                UI_Functions.CreatePopup(SellerAddNewItemActivity.this, "Food is posted successfully!");
                                                addBtn.setEnabled(true);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                UI_Functions.CreatePopup(SellerAddNewItemActivity.this, "Fail to post a food!");
                                                addBtn.setEnabled(true);
                                            }
                                        });
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SellerAddNewItemActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                progressDialog.setMessage("Please waiting... " + snapshot.getBytesTransferred() * 100.0 / snapshot.getTotalByteCount() + " %");
                            }
                        });
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

   private void refreshTextInputLayout(TextInputLayout input) {
       input.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View view, boolean b) {
               input.setErrorEnabled(true);
               input.setError("");
           }
       });
   }
}