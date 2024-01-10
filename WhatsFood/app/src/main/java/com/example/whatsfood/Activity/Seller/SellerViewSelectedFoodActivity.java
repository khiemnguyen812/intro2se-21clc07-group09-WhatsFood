package com.example.whatsfood.Activity.Seller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatsfood.CustomAlertDialog;
import com.example.whatsfood.Model.Food;
import com.example.whatsfood.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class SellerViewSelectedFoodActivity extends AppCompatActivity {

    ImageView foodImageView;
    TextView foodNameTextView, priceTextView, descriptionTextView;

    AppCompatButton updateBtn, deleteBtn;
    ImageButton backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_view_selected_food);

        Food food = (Food) getIntent().getSerializableExtra("Item");

        foodImageView = (ImageView) findViewById(R.id.food_image_view_seller_view_selected_food);
        foodNameTextView = (TextView) findViewById(R.id.food_name_text_view_seller_view_selected_food);
        priceTextView = (TextView) findViewById(R.id.price_text_view_seller_view_selected_food);
        descriptionTextView = (TextView) findViewById(R.id.description_text_view_seller_view_selected_food);
        updateBtn = (AppCompatButton) findViewById(R.id.update_button_seller_view_selected_food);
        deleteBtn = (AppCompatButton) findViewById(R.id.delete_button_seller_view_selected_food);
        backBtn = (ImageButton) findViewById(R.id.image_button_seller_view_seleted_food);



        Picasso.get().load(food.getImageUrl()).fit().into(foodImageView);
        foodNameTextView.setText(food.getName());
        priceTextView.setText("" + food.getPrice());
        descriptionTextView.setText(food.getDescription());

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerViewSelectedFoodActivity.this, SellerUpdateItemActivity.class);
                intent.putExtra("Food", (Serializable) food);
                startActivity(intent);
                finish();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAlertDialog customAlertDialog = new CustomAlertDialog(SellerViewSelectedFoodActivity.this);
                customAlertDialog.setTitle("Warning!");
                customAlertDialog.setMessage("Do you want to delete this item ?");
                customAlertDialog.setAcceptEvent(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance().getReference().child("Food").child(food.getFoodId()).removeValue();
                        FirebaseStorage.getInstance().getReferenceFromUrl(food.getImageUrl()).delete();
                        customAlertDialog.dismiss();
                        finish();
                    }
                });
                customAlertDialog.setCancelEvent(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                customAlertDialog.show();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}