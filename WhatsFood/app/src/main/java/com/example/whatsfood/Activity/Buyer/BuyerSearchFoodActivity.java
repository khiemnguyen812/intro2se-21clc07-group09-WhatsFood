package com.example.whatsfood.Activity.Buyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsfood.Activity.SelectAccountTypeActivity;
import com.example.whatsfood.Adapter.AdapterFood;
import com.example.whatsfood.Adapter.FoodAdapter;
import com.example.whatsfood.Model.Food;
import com.example.whatsfood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BuyerSearchFoodActivity extends AppCompatActivity {

    ImageButton search_button;
    ImageButton back_button;

    private FirebaseAuth mAuth;
    DatabaseReference ref;
    ArrayList <Food> list;
    RecyclerView recyclerView;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_home);
        ref = FirebaseDatabase.getInstance().getReference().child("https://whatsfood-13e7e-default-rtdb.asia-southeast1.firebasedatabase.app/").child("Food");
        recyclerView = findViewById(R.id.rv);
        searchView = findViewById(R.id.searchView);
        //((TextView) findViewById(R.id.header)).setText("WhatsFood");
        //search_button = (ImageButton) findViewById(R.id.search_button);
        //search_button.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View view) {
                //SearchFood();
            //}

        //});

        //back_button = (ImageButton) findViewById(R.id.back_button);
        //back_button.setOnClickListener(new View.OnClickListener() {
           // @Override
            //public void onClick(View view) {
                //Intent intent = new Intent(BuyerSearchFoodActivity.this, BuyerHomeActivity.class);
                //startActivity(intent);
            }
        //});
    //}

    @Override
    protected void onStart() {
        super.onStart();
        if (ref != null) {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        list = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                          list.add(ds.getValue(Food.class));
                        }
                        AdapterFood adapterFood = new AdapterFood(list);
                        recyclerView.setAdapter(adapterFood);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(BuyerSearchFoodActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    Search(s);
                    return true;
                }
            });
        }
    }

    //private void SearchFood() {
        //Intent intent = new Intent(BuyerSearchFoodActivity.this, BuyerSearchFoodActivity.class);
        //startActivity(intent);
         //}

    private void Search(String str) {
        ArrayList<Food> myList = new ArrayList<>();
        for (Food object : list)
        {
           if(object.getDescription().toLowerCase().contains(str.toLowerCase())) {
               myList.add(object);
           }
        }
        AdapterFood adapterFood = new AdapterFood(list);
        recyclerView.setAdapter(adapterFood);

    }

}
