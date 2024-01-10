package com.example.whatsfood.Activity.Buyer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.whatsfood.Adapter.OrderAdapterForBuyer;
import com.example.whatsfood.Model.Order;
import com.example.whatsfood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BuyerOrderListActivity extends Fragment {

    ListView listView;
    ArrayList<Order> ordersList=new ArrayList<>();
    OrderAdapterForBuyer orderAdapterForBuyer;
    private String buyerId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_buyer_order_list, null);
        requireActivity().setTitle("Order List");
        setHasOptionsMenu(true);

        ((TextView) v.findViewById(R.id.order_list_header)).setText("Order List");

        listView=(ListView) v.findViewById(R.id.order_list);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Order");
        buyerId  = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.orderByChild("buyerId").equalTo(buyerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order=orderSnapshot.getValue(Order.class);
                    ordersList.add(order);
                }
                orderAdapterForBuyer =new OrderAdapterForBuyer(getActivity(),R.layout.order_placeholder_buyer,ordersList);
                listView.setAdapter(orderAdapterForBuyer);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error: " + databaseError.getMessage());
            }
        });
        return v;
    }
}