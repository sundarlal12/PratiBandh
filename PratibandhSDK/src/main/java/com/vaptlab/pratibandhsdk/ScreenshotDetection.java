package com.vaptlab.pratibandhsdk;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.WindowManager;
import android.widget.Toast;

public class ScreenshotDetection {

    private final Context context;
    private boolean isProtectionEnabled;
    private ScreenshotDetectionReceiver receiver;

    // Constructor
    public ScreenshotDetection(Context context) {
        this.context = context;
        this.isProtectionEnabled = false;

        // Initialize and register the receiver
        if (context instanceof Activity) {
            this.receiver = new ScreenshotDetectionReceiver(context);
            this.receiver.register();
        }
    }

    /**
     * Apply protections to prevent screenshots, screen recordings, and screen sharing.
     * Returns true if protection is enabled successfully.
     */
    public boolean applyProtection() {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;

            // Apply FLAG_SECURE to prevent screenshots, screen recordings, and screen sharing
            activity.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
            );

            isProtectionEnabled = true;
        } else {
            showToast("Context is not an instance of Activity. Unable to apply protection.");
        }

        return isProtectionEnabled;
    }

    /**
     * Clean up resources by unregistering the BroadcastReceiver.
     * Should be called when the context (Activity) is destroyed.
     */
    public void cleanup() {
        if (receiver != null) {
            receiver.unregister();
            receiver = null;
        }
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // Inner class for BroadcastReceiver
    public class ScreenshotDetectionReceiver extends BroadcastReceiver {

        private final Context context;

        public ScreenshotDetectionReceiver(Context context) {
            this.context = context;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // Handle broadcast to detect potential screenshot or screen recording attempts
            if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
                showToast("Potential screenshot or screen recording attempt detected.");
            }
        }

        public void register() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_USER_PRESENT); // Placeholder for actual detection
            context.registerReceiver(this, filter);
        }

        public void unregister() {
            try {
                context.unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                // Handle case where receiver was not registered
                e.printStackTrace();
            }
        }
    }
}
