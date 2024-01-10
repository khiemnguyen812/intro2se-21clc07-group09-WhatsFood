package com.example.whatsfood.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.whatsfood.R;

public class ChangeProfileActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView profile_name = findViewById(R.id.profile_name);
        TextView profile_phone = findViewById(R.id.profile_phone);
        TextView profile_address = findViewById(R.id.profile_address);
        TextView profile_email = findViewById(R.id.profile_email);
        ImageView profile_avatar = findViewById(R.id.profile_avatar);

        /* TODO: set appropriate data
        profile_name.setText();
        profile_phone.setText();
        profile_address.setText();
        profile_email.setText();
        profile_avatar.setImageURI();
        */

        Button change_button = findViewById(R.id.change_profile_button);
        change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: commit change
            }
        });
    }
}
