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
import com.example.whatsfood.Adapter.ViewUserAdminAdapter;
import com.example.whatsfood.Model.Seller;
import com.example.whatsfood.Model.User;
import com.example.whatsfood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class AdminHomeActivity extends Fragment {
    private ArrayList<Pair<String, User >> users;
    private ViewUserAdminAdapter adapter;
    private ListView listView;
    private TextView header;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_admin_home, null);
        header = (TextView) view.findViewById(R.id.header);
        header.setText("User accounts");
        listView = (ListView)view.findViewById(R.id.users);
        setHasOptionsMenu(true);
        users = new ArrayList<>();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("User");
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                }
                else {
                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        users.add(new Pair<>(dataSnapshot.getKey(), dataSnapshot.getValue(User.class)));
                        UpdateAdapter();
                    }
                }
            }

        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(requireActivity(), UserDetailActivity.class);
                intent.putExtra("UserId", users.get(i).first);
                intent.putExtra("user", (Serializable) users.get(i).second);
                startActivity(intent);
            }
        });
        return view;
    }

    private void UpdateAdapter() {
        adapter = new ViewUserAdminAdapter(getActivity(), R.layout.user_holder, users);
        listView.setAdapter(adapter);
    }
}

//Here