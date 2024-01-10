package com.example.whatsfood.Activity.Buyer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whatsfood.Activity.ChangePasswordActivity;
import com.example.whatsfood.Activity.LoginActivity;
import com.example.whatsfood.Model.Buyer;
import com.example.whatsfood.R;
import com.example.whatsfood.UI_Functions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class BuyerViewProfileActivity extends Fragment {

    Button ok_button;
    TextView popup_text;
    ImageButton back_button;
    Button change_password_button, edit_profile_button;
    String seller_id;
    Buyer buyer;
    View profile_view;

    AppCompatButton logout_button;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_buyer_view_profile, null);
        profile_view = view;
        setHasOptionsMenu(true);
        ((TextView)view.findViewById(R.id.header)).setText("Buyer Detail");
        ((ImageButton)view.findViewById(R.id.back_button)).setVisibility(View.GONE);
        change_password_button = (Button)view.findViewById(R.id.change_password_button);
        edit_profile_button = (Button)view.findViewById(R.id.edit_profile_button);
        logout_button = (AppCompatButton) view.findViewById(R.id.logout_button_buyer_view_profile);

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        change_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        edit_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), BuyerEditProfileActivity.class);
                intent.putExtra("buyer", (Serializable) buyer);
                startActivity(intent);
                UpdateInformation(profile_view);
            }
        });
        UpdateInformation(view);
        return view;
    }

    private void UpdateInformation(View view) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Buyer");
        FirebaseUser fb_user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase.child(fb_user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    if (task.getException() != null) {
                        Log.w("firebase", "Buyer Profile View - "
                                + task.getException().toString());
                    }
                }
                else {
                    buyer = task.getResult().getValue(Buyer.class);
                    if (buyer != null) {
                        String username_string = "Username: " + buyer.username;
                        String address_string = "Address: " + buyer.address;
                        String phone_string = "Phone: " + buyer.phone;
                        String email_string = "Email: " + fb_user.getEmail();

                        ((TextView) view.findViewById(R.id.username)).setText(username_string);
                        ((TextView) view.findViewById(R.id.fullname)).setText(buyer.fullname);
                        ((TextView) view.findViewById(R.id.address)).setText(address_string);
                        ((TextView) view.findViewById(R.id.phone)).setText(phone_string);
                        ((TextView) view.findViewById(R.id.email)).setText(email_string);
                        UI_Functions.LoadImageToImageView((ImageView) view.findViewById(R.id.avatar), buyer.avatarUrl);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        UpdateInformation(getView());
    }
}