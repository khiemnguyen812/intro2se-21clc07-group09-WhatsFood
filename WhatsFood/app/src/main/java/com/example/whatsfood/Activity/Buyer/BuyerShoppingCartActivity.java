package com.example.whatsfood.Activity.Buyer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.whatsfood.Adapter.CartAdapter;
//import com.example.whatsfood.Model.CartDetail;
import com.example.whatsfood.Model.Buyer;
import com.example.whatsfood.Model.CartDetail;
import com.example.whatsfood.Model.Order;
import com.example.whatsfood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BuyerShoppingCartActivity extends Fragment {
    ListView listView;
    TextView totalMoneyTextView;
    ArrayList<CartDetail> cartDetailList=new ArrayList<>();
    CartAdapter cartAdapter;
    AppCompatButton orderBtn;
    private int totalMoney = 0;
    private String buyerId, buyerName, sellerId, ship_to;
    HashMap<String, ArrayList<CartDetail>> sellerOrder = new HashMap<String, ArrayList<CartDetail>>();;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_buyer_shopping_cart, null);
        requireActivity().setTitle("Shopping Cart");
        setHasOptionsMenu(true);

        listView= (ListView) v.findViewById(R.id.comment_list);
        orderBtn = (AppCompatButton) v.findViewById(R.id.order_button_buyer_shopping_cart);
        totalMoneyTextView = (TextView) v.findViewById(R.id.totalmoney);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        buyerId  = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("Buyer").child(buyerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Buyer buyer = snapshot.getValue(Buyer.class);
                buyerName = buyer.fullname;
                ship_to = buyer.address;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mDatabase.child("Buyer").child(buyerId).child("cartDetailList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String key = snapshot.getKey();
                if (key.equals("0")) {
                    return;
                }
                CartDetail cartItem = snapshot.getValue(CartDetail.class);

                if (cartItem != null) {
                    cartDetailList.add(cartItem);
                    totalMoney += cartItem.getNumber() * cartItem.getPrice();
                    totalMoneyTextView.setText(String.valueOf((totalMoney)));

                }
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CartDetail cartItem = snapshot.getValue(CartDetail.class);
                if (cartItem != null && cartDetailList.isEmpty() == false) {
                    for (int i = 0; i < cartDetailList.size(); i++) {
                        if (cartDetailList.get(i).getFoodId() == cartItem.getFoodId()) {
                            totalMoney += (cartItem.getNumber() - cartDetailList.get(i).getNumber()) * cartDetailList.get(i).getPrice();
                            cartDetailList.set(i, cartItem);
                            break;
                        }
                    }

                    totalMoneyTextView.setText(String.valueOf(totalMoney));
                    cartAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                CartDetail cartItem = snapshot.getValue(CartDetail.class);
                if (cartItem != null && !cartDetailList.isEmpty()) {
                    for (int i = 0; i < cartDetailList.size(); i++) {
                        if (cartDetailList.get(i).getFoodId() == cartItem.getFoodId()) {
                            totalMoney -= cartDetailList.get(i).getNumber() * cartDetailList.get(i).getPrice();
                            cartDetailList.remove(i);
                            break;
                        }
                    }
                    totalMoneyTextView.setText(String.valueOf(totalMoney));
                    cartAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        cartAdapter=new CartAdapter(getActivity(),R.layout.food_in_cart_detail,cartDetailList);
        listView.setAdapter(cartAdapter);

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (CartDetail cartDetail : cartDetailList) {
                    sellerId = cartDetail.getSellerId();
                    if (sellerOrder.get(sellerId) == null) {
                        sellerOrder.put(sellerId, new ArrayList<CartDetail>());
                    }
                    sellerOrder.get(sellerId).add(cartDetail);
                }

                for (String keySellerId: sellerOrder.keySet()) {
                    if (sellerOrder.get(keySellerId) != null) {
                        String orderId = mDatabase.child("Order").push().getKey();
                        int total_per_shop = 0;
                        for (CartDetail item : sellerOrder.get(keySellerId)) {
                            total_per_shop += item.getPrice() * item.getNumber();
                        }
                        Order newOrder = new Order(orderId, buyerId, buyerName, keySellerId, ship_to, total_per_shop, sellerOrder.get(keySellerId));
                        boolean createOrder = newOrder.UpdateDataToServer();
                    }
                }
                mDatabase.child("Buyer").child(buyerId).child("cartDetailList").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<CartDetail> tempCartDetailList = (ArrayList<CartDetail>) snapshot.getValue();
                        for (int i = 1; i < tempCartDetailList.size(); i++) {
                            mDatabase.child("Buyer").child(buyerId).child("cartDetailList").child("" + i).removeValue();
                        }
                        cartAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        return v;
    }



}