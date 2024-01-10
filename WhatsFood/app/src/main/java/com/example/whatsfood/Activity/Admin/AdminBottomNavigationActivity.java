package com.example.whatsfood.Activity.Admin;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.whatsfood.Activity.ProfileActivity;
import com.example.whatsfood.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminBottomNavigationActivity extends AppCompatActivity {

    Fragment fragment;
    BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bottom_navigation);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.admin_bottom_navigation);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.admin_fragment_container, new AdminHomeActivity())
                .commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                fragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.admin_navigation_users) {
                    fragment = new AdminHomeActivity();
                } else if (itemId == R.id.admin_navigation_register) {
                    fragment = new AdminSellerRegisterRequestsActivity();
                } else if (itemId == R.id.admin_navigation_report) {
                    fragment = new AdminReportsListActivity();
                }
                if (fragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.admin_fragment_container, fragment)
                            .commit();
                    return true;
                }
                return false;
            }
        });

    }
}
