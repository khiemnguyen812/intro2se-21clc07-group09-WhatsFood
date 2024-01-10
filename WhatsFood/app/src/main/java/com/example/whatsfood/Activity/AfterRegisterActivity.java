package com.example.whatsfood.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsfood.R;

public class AfterRegisterActivity extends AppCompatActivity {
    Button ok_button;
    TextView popup_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_register);
        Bundle extras = getIntent().getExtras();
        String text = extras.getString("popup_text", " ");
        popup_text = (TextView)findViewById(R.id.popup_text);
        popup_text.setText(text);
        ok_button = (Button)findViewById(R.id.yes);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AfterRegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
