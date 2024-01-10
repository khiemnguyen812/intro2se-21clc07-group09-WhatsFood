package com.example.whatsfood.Activity.Buyer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.whatsfood.Model.Food;
import com.example.whatsfood.R;
import com.example.whatsfood.Adapter.FoodAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class BuyerHomeActivity extends Fragment {

    ListView listView;
    ArrayList<Food> foodList;
    FoodAdapter foodAdapter;

    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_buyer_home, null);
        requireActivity().setTitle("Home");
        setHasOptionsMenu(true);

        listView = (ListView) view.findViewById(R.id.list_view_buyer_home);
        foodList = new ArrayList<Food>();

        databaseRef.child("Food").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Food food = snapshot.getValue(Food.class);
                if (food != null) {
                    foodList.add(food);
                    foodAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Food food = snapshot.getValue(Food.class);
                if (food != null && !foodList.isEmpty()) {
                    for (int i = 0; i < foodList.size(); i++) {
                        if (Objects.equals(foodList.get(i).getFoodId(), food.getFoodId())) {
                            foodList.set(i, food);
                        }
                    }
                    foodAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Food food = snapshot.getValue(Food.class);
                if (food != null && !foodList.isEmpty()) {
                    for (int i = 0; i < foodList.size(); i++) {
                        if (Objects.equals(foodList.get(i).getFoodId(), food.getFoodId())) {
                            foodList.remove(i);
                            break;
                        }
                    }
                    foodAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        foodAdapter = new FoodAdapter(getContext(), R.layout.food_holder_buyer, foodList);
        listView.setAdapter(foodAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), BuyerViewSelectedFoodActivity.class);
                intent.putExtra("foodId", foodList.get(i).getFoodId());
                startActivity(intent);
            }
        });


        return view;
    }
}