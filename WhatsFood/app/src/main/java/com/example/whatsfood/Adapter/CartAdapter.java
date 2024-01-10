package com.example.whatsfood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.whatsfood.Model.CartDetail;
import com.example.whatsfood.Model.Food;
import com.example.whatsfood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class CartAdapter extends BaseAdapter {

    private Context context;
    private int layout;

    private List<CartDetail> food_cartlist;

    public CartAdapter(Context context, int layout, List<CartDetail> foodlist) {
        this.context = context;
        this.layout = layout;
        this.food_cartlist = foodlist;
    }

    @Override
    public int getCount() {
        return food_cartlist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView ten, gia, number;
        ImageView img, minus, plus;
        Button remove;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(layout,null);
            viewHolder.ten = (TextView) view.findViewById(R.id.food_name);
            viewHolder.gia = (TextView) view.findViewById(R.id.price);
            viewHolder.number = (TextView) view.findViewById(R.id.number);
            viewHolder.img = (ImageView) view.findViewById(R.id.food_image);
            viewHolder.minus=(ImageView) view.findViewById(R.id.minus);
            viewHolder.plus=(ImageView) view.findViewById(R.id.plus);
            viewHolder.remove = (Button) view.findViewById(R.id.remove);
            view.setTag(viewHolder);
        } else {
            viewHolder = (CartAdapter.ViewHolder) view.getTag();
        }

        CartDetail list = food_cartlist.get(position);
        viewHolder.ten.setText(list.getName());
        viewHolder.gia.setText(String.valueOf(list.getPrice()));
        viewHolder.number.setText(String.valueOf(list.getNumber()));

        Picasso.get().load(list.getImageUrl()).into(viewHolder.img);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference().child("Buyer").child(userId).child("cartDetailList");

        // Thiết lập sự kiện cho nút "minus"
        viewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ss: snapshot.getChildren()) {
                            String key = ss.getKey();

                            CartDetail cartDetail = ss.getValue(CartDetail.class);
                            if (cartDetail.getFoodId() == list.getFoodId()) {
                                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                                if (cartDetail.getNumber() == 1) {
                                    hashMap.put("/number/" , 1);
                                } else {
                                    hashMap.put("/number/" , cartDetail.getNumber() - 1);
                                }
                                databaseReference.child(key).updateChildren(hashMap);
                                break;

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                int currentNumber = list.getNumber();
//                if (currentNumber > 1) {
//                    currentNumber--;
//                    list.setNumber(currentNumber);
//                    notifyDataSetChanged(); // Cập nhật giao diện sau khi thay đổi số lượng
//                }
            }
        });


        // Thiết lập sự kiện cho nút "plus"
        viewHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ss: snapshot.getChildren()) {
                            String key = ss.getKey();

                            CartDetail cartDetail = ss.getValue(CartDetail.class);
                            if (cartDetail.getFoodId() == list.getFoodId()) {
                                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                                hashMap.put("/number/" , cartDetail.getNumber() + 1);
                                databaseReference.child(key).updateChildren(hashMap);
                                break;

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                int currentNumber = list.getNumber();
//                currentNumber++;
//                list.setNumber(currentNumber);
//                notifyDataSetChanged(); // Cập nhật giao diện sau khi thay đổi số lượng

            }
        });
        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ss: snapshot.getChildren()) {
                                String key = ss.getKey();

                                CartDetail cartDetail = ss.getValue(CartDetail.class);
                                if (cartDetail.getFoodId() == list.getFoodId()) {
                                    databaseReference.child(key).removeValue();
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        return view;
    }
}
