package com.vaptlab.pratibandhsdk;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class CustomAlertDialogFragment extends DialogFragment {

    private String message;
    private String description;
    private CountDownTimer countDownTimer;

    public static CustomAlertDialogFragment newInstance(String message, String description) {
        CustomAlertDialogFragment fragment = new CustomAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("message", message);
        args.putString("description", description);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Retrieve arguments
        message = getArguments() != null ? getArguments().getString("message") : "";
        description = getArguments() != null ? getArguments().getString("description") : "";

        Activity activity = getActivity();

        // Ensure the activity is valid before proceeding
        if (activity == null || activity.isFinishing()) {
            return new Dialog(requireContext());
        }

        // Inflate the custom layout
        LayoutInflater inflater = activity.getLayoutInflater();
        View customView = inflater.inflate(R.layout.custom_alert_dialog, null);

        // Create a new Dialog
        Dialog dialog = new Dialog(activity);

        // Remove the default title bar
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Set the custom view
        dialog.setContentView(customView);

        // Set the dialog's background to transparent
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }

        // Make the dialog non-cancelable
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        // Bind the views
        TextView titleTextView = customView.findViewById(R.id.dialogTitle);
        ImageView appIconImageView = customView.findViewById(R.id.appIcon);
        TextView appNameTextView = customView.findViewById(R.id.appName);
        TextView messageTextView = customView.findViewById(R.id.dialogMessage);
        TextView descriptionTextView = customView.findViewById(R.id.dialogDescription);
        Button closeButton = customView.findViewById(R.id.closeButton);

        // Set the title, message, and description
        titleTextView.setText("Security Alert!");
        messageTextView.setText(message);
        descriptionTextView.setText(description);

        // Set App Icon and Name with proper error handling
        PackageManager pm = activity.getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(activity.getPackageName(), 0);
            String appName = pm.getApplicationLabel(appInfo).toString();
            Drawable appIcon = pm.getApplicationIcon(appInfo);

            appNameTextView.setText(appName != null ? appName : "Unknown App");
            appIconImageView.setImageDrawable(appIcon != null ? appIcon : activity.getDrawable(android.R.drawable.sym_def_app_icon));
        } catch (Exception e) {
            appNameTextView.setText("Unknown App");
            appIconImageView.setImageResource(android.R.drawable.sym_def_app_icon);
        }

        // Initialize countdown timer for the close button
        countDownTimer = new CountDownTimer(10000, 1000) {
            int secondsLeft = 10;

            @Override
            public void onTick(long millisUntilFinished) {
                closeButton.setText("CLOSE (" + secondsLeft + "s)");
                secondsLeft--;
            }

            @Override
            public void onFinish() {
                closeButton.setText("EXIT");
                closeApp(activity); // Automatically close the app when the timer ends
            }
        };
        countDownTimer.start();

        // Close button click listener
        closeButton.setOnClickListener(v -> {
            if (countDownTimer != null) {
                countDownTimer.cancel(); // Cancel the timer if the button is clicked
            }
            closeApp(activity);
        });

        // Handle Back button press
        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                closeApp(activity);
                return true; // Consume the back button event
            }
            return false;
        });

        return dialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Ensure the timer is canceled if the dialog is destroyed
        }
    }

    private void closeApp(Activity activity) {
        // Close the app only if it's not already finishing
        if (activity != null && !activity.isFinishing()) {
            activity.finish(); // Close the activity
            activity.finishAffinity();
            System.exit(0); // Ensure the app is fully terminated
        }
    }
}
