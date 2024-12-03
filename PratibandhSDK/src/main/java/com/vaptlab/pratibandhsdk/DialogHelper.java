package com.vaptlab.pratibandhsdk;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

public class DialogHelper {

    public static void showCustomAlertDialog(Context context, String title, String message) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;

            // Ensure activity is a FragmentActivity or AppCompatActivity
            if (activity instanceof FragmentActivity) {
                // Pass title and message to the dialog fragment
                CustomAlertDialogFragment dialogFragment = CustomAlertDialogFragment.newInstance(title, message);

                // Show the dialog fragment using FragmentActivity's getSupportFragmentManager
                dialogFragment.show(((FragmentActivity) activity).getSupportFragmentManager(), "CustomAlertDialog");
            } else {
                Log.e("SecuritySDK", "Activity must be an instance of FragmentActivity or AppCompatActivity.");
            }

        } else {
            Log.e("SecuritySDK", "Context is not an instance of Activity, cannot show dialog.");
        }
    }
}
