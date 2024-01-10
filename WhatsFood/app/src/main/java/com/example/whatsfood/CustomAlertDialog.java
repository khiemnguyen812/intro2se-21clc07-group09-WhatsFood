package com.example.whatsfood;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

public class CustomAlertDialog {
    Dialog dialog = null;
    Context context = null;

    public CustomAlertDialog() {

    }

    public CustomAlertDialog(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_alert_dialog);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        this.dialog.setCancelable(false);
    }

    public void setMessage(String message) {
        if (dialog != null) {
            TextView messageTextView = (TextView) dialog.findViewById(R.id.message_custom_alert_dialog);
            messageTextView.setText(message);
        }
    }

    public void setTitle(String title) {
        if (dialog != null) {
            TextView titleTextView = (TextView) dialog.findViewById(R.id.title_custom_alert_dialog);
            titleTextView.setText(title);
        }
    }

    public void setAcceptEvent( View.OnClickListener accept) {
        if (dialog != null) {
            //Set button
            AppCompatButton acceptBtn = (AppCompatButton) dialog.findViewById(R.id.accept_button_custom_alert_dialog);
            acceptBtn.setOnClickListener(accept);
        }

    }

    public void setCancelEvent( View.OnClickListener cancel) {
        if (dialog != null) {
            //Set button
            AppCompatButton cancelBtn = (AppCompatButton) dialog.findViewById(R.id.cancel_button_custom_alert_dialog);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancel.onClick(view);
                    dialog.dismiss();
                }
            });

        }
    }

    public  void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public  void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
