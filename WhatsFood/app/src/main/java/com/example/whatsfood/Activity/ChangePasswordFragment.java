package com.example.whatsfood.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.whatsfood.R;
import com.google.android.material.textfield.TextInputEditText;

public class ChangePasswordFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment_view = inflater.inflate(R.layout.activity_change_password, container);
        requireActivity().setTitle("Change Password");
        return fragment_view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputEditText current_password = view.findViewById(R.id.store_name);
        TextInputEditText new_password = view.findViewById(R.id.new_password);
        TextInputEditText confirm_password = view.findViewById(R.id.confirm_password);
        Button confirm_change_button = view.findViewById(R.id.confirm_create_admin_button);

        confirm_change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_pass = current_password.getText() != null ? current_password.getText().toString() : "";
                String new_pass = "";

                if (new_password.getText() == confirm_password.getText()) {
                    new_pass = new_password.getText() != null ? new_password.getText().toString() : "";
                }

                // TODO: Send to server
                boolean success = true;
                if (success) {

                }
                else {

                }
            }
        });
    }
}
