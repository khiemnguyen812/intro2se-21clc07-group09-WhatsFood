package com.example.whatsfood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.whatsfood.Model.Food;
import com.example.whatsfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodAdapter extends BaseAdapter {
    Context context;
    private int layout;
    public List<Food> foodList;

    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();


    public FoodAdapter(Context context, int layout, List<Food> foodList) {
        this.context = context;
        this.layout = layout;
        this.foodList = foodList;
    }

    public FoodAdapter() {
    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class FoodViewHolder {
        TextView foodName, store, price, quantity;
        ImageView foodImageView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        FoodViewHolder foodViewHolder = null;
        if (view == null) {
            foodViewHolder = new FoodViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            if (layout == R.layout.food_holder_buyer) {
                foodViewHolder.foodImageView = (ImageView) view.findViewById(R.id.img);
                foodViewHolder.foodName = (TextView) view.findViewById(R.id.food_name);
                foodViewHolder.store = (TextView) view.findViewById(R.id.store);
                foodViewHolder.price = (TextView) view.findViewById(R.id.price);
            } else if (layout == R.layout.item_suborder) {
                foodViewHolder.foodImageView = (ImageView) view.findViewById(R.id.image_view_item_suborder);
                foodViewHolder.foodName = (TextView) view.findViewById(R.id.food_name_text_box_item_suborder);
                foodViewHolder.quantity = (TextView) view.findViewById(R.id.quantity_item_suborder);
                foodViewHolder.price = (TextView) view.findViewById(R.id.suporder_price);
            } else if (layout == R.layout.shop_item_holder) {
                foodViewHolder.foodImageView = (ImageView) view.findViewById(R.id.item_image_view_shop_item_holder);
                foodViewHolder.foodName = (TextView) view.findViewById(R.id.item_name_text_view_shop_item_holder);
                foodViewHolder.price = (TextView) view.findViewById(R.id.price_text_view_shop_item_holder);
            }
            view.setTag(foodViewHolder);
        } else {
            foodViewHolder = (FoodViewHolder) view.getTag();
        }

        Food food = foodList.get(i);

        if (layout == R.layout.food_holder_buyer) {
            foodViewHolder.store.setText(food.getStoreName());

        } else if (layout == R.layout.item_suborder) {
            foodViewHolder.quantity.setText("" + food.getQuantity());

        }

        foodViewHolder.foodName.setText(food.getName());
        foodViewHolder.price.setText(food.getPrice() + " VND");
        Picasso.get()
                .load(food.getImageUrl())
                .fit()
                .into(foodViewHolder.foodImageView);
        return view;
    }
}
