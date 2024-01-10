package com.example.whatsfood.Activity.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.whatsfood.Activity.LoginActivity;
import com.example.whatsfood.Adapter.SellerRegisterRequestAdapter;
import com.example.whatsfood.Model.Seller;
import com.example.whatsfood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class AdminSellerRegisterRequestsActivity extends Fragment {
    private ArrayList<Pair<String, Seller>> sellers;
    private SellerRegisterRequestAdapter adapter;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_admin_seller_register_requests, null);
        ((TextView)view.findViewById(R.id.header)).setText("Registrations");
        listView = (ListView)view.findViewById(R.id.listView);
        setHasOptionsMenu(true);
        sellers = new ArrayList<>();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("SellerRegister");
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                }
                else {
                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        sellers.add(new Pair<>(dataSnapshot.getKey(), dataSnapshot.getValue(Seller.class)));
                        UpdateAdapter();
                    }
                }
            } 
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(requireActivity(), RegisterDetailActivity.class);
                intent.putExtra("seller_id", sellers.get(i).first);
                intent.putExtra("seller", (Serializable) sellers.get(i).second);
                startActivity(intent);
            }
        });
        return view;
    }

    private void UpdateAdapter() {
        adapter = new SellerRegisterRequestAdapter(getActivity(), R.layout.seller_register_request_holder, sellers);
        listView.setAdapter(adapter);
    }
}
