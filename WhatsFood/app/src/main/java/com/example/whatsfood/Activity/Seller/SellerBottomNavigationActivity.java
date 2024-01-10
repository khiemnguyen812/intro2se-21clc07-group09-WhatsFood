package com.example.whatsfood.Activity.Seller;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.whatsfood.Activity.ProfileActivity;
import com.example.whatsfood.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SellerBottomNavigationActivity extends AppCompatActivity {

    Fragment fragment;
    BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_bottom_navigation);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.seller_bottom_navigation);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.seller_fragment_container, new SellerHomeActivity())
                .commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                fragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.seller_home) {
                    fragment = new SellerHomeActivity();
                } else if (itemId == R.id.seller_food_list) {
                    fragment = new SellerItemListActivity();
                } else if (itemId == R.id.seller_notification) {
                    fragment = new SellerNotificationActivity();
                } else if (itemId == R.id.seller_profile) {
                    fragment = new SellerViewProfileActivity();
                }
                if (fragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.seller_fragment_container, fragment)
                            .commit();
                    return true;
                }
                return false;
            }
        });

    }
}
