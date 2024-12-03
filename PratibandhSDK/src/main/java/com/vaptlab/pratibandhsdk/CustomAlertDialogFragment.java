package com.vaptlab.pratibandhsdk;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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
        message = getArguments() != null ? getArguments().getString("message", "") : "";
        description = getArguments() != null ? getArguments().getString("description", "") : "";

        Activity activity = getActivity();
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return new Dialog(requireContext());
        }

        LayoutInflater inflater = activity.getLayoutInflater();
        View customView = inflater.inflate(R.layout.custom_alert_dialog, null);

        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(customView);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        dialog.setCancelable(false);  // Prevent closing by touch outside
        dialog.setCanceledOnTouchOutside(false);  // Prevent touch outside

        // Prevent the back button from closing the dialog
        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                return true;  // Returning true prevents the back button from closing the dialog
            }
            return false;
        });

        TextView titleTextView = customView.findViewById(R.id.dialogTitle);
        ImageView appIconImageView = customView.findViewById(R.id.appIcon);
        TextView appNameTextView = customView.findViewById(R.id.appName);
        TextView messageTextView = customView.findViewById(R.id.dialogMessage);
        TextView descriptionTextView = customView.findViewById(R.id.dialogDescription);
        Button closeButton = customView.findViewById(R.id.closeButton);

        closeButton.setBackgroundColor(Color.parseColor("#ff4e37"));
        closeButton.setTextColor(Color.WHITE);
        closeButton.setClipToOutline(false);

        titleTextView.setText("Security Alert!");
        messageTextView.setText(message);
        descriptionTextView.setText(description);

        PackageManager pm = activity.getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(activity.getPackageName(), 0);
            String appName = pm.getApplicationLabel(appInfo).toString();
            appNameTextView.setText(appName != null ? appName : "Unknown App");
            appIconImageView.setImageDrawable(pm.getApplicationIcon(appInfo));
        } catch (Exception e) {
            appNameTextView.setText("Unknown App");
            appIconImageView.setImageResource(android.R.drawable.sym_def_app_icon);
        }

        // Countdown timer for close button and auto-close after 10 seconds
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
                closeApp(activity);
            }
        };
        countDownTimer.start();

        // Close button logic
        closeButton.setOnClickListener(v -> {
            if (countDownTimer != null) {
                countDownTimer.cancel(); // Cancel the timer if the button is clicked
            }
            closeApp(activity);
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

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }



    @Override
    public void onPause() {
        super.onPause();
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Ensure the timer is canceled if the dialog is paused
        }
    }

    private void closeApp(Activity activity) {
        // Close the app only if it's not already finishing
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
          //  activity.finishAffinity();
            System.exit(0); // Ensures the app is fully terminated
          //  android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
