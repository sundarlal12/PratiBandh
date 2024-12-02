package com.vaptlab.pratibandhsdk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
public class CustomAlertDialogFragment extends DialogFragment {

    private String message;
    private String description;
    private int countdownSeconds = 10; // Countdown time in seconds
    private CountDownTimer countDownTimer;
    private boolean isCloseButtonClicked = false; // Track if the close button was clicked

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
            return new AlertDialog.Builder(requireContext())
                    .setTitle("Error")
                    .setMessage("Unable to display dialog. Invalid activity context.")
                    .setPositiveButton("OK", (dialog, which) -> dismiss())
                    .create();
        }

        // Inflate the custom layout
        LayoutInflater inflater = activity.getLayoutInflater();
        View customView = inflater.inflate(R.layout.custom_alert_dialog, null);

        // Build the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(customView);
        builder.setCancelable(false); // Prevent dismissing by tapping outside

        // Prevent dismiss on touch outside
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false); // Prevent dismissing by touch outside

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
        } catch (PackageManager.NameNotFoundException e) {
            appNameTextView.setText("Unknown App");
            appIconImageView.setImageResource(android.R.drawable.sym_def_app_icon);
        } catch (Exception e) {
            appNameTextView.setText("Error retrieving app info");
            appIconImageView.setImageResource(android.R.drawable.sym_def_app_icon);
        }

        // Countdown logic for the button
        countDownTimer = new CountDownTimer(countdownSeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                closeButton.setText("EXIT (" + millisUntilFinished / 1000 + ")");
            }

            @Override
            public void onFinish() {
                closeButton.setText("CLOSE APP");
                // Close app when countdown finishes, if the button wasn't clicked
                if (!isCloseButtonClicked) {
                    closeApp(activity);
                }
            }
        };
        countDownTimer.start();

        // Close button click listener
        closeButton.setOnClickListener(v -> {
            isCloseButtonClicked = true; // Mark the button as clicked
            if (activity != null && !activity.isFinishing()) {
                closeApp(activity);
            }
        });

        return alertDialog;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        // Override the default cancel behavior to prevent dismiss when clicking outside
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Cancel the timer to prevent memory leaks
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void closeApp(Activity activity) {
        // Close the app only if it's not already finishing
        if (activity != null && !activity.isFinishing()) {
            activity.finish(); // Close the activity
        }
    }
}
