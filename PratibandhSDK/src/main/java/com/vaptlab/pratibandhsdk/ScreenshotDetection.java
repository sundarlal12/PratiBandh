package com.vaptlab.pratibandhsdk;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.FileObserver;
import android.view.View;
import android.widget.Toast;
import android.util.Log;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.File;

public class ScreenshotDetection {

//    private final Context context;
//    private final SecuritySDK securitySDK;
    private FileObserver screenshotObserver;
    private boolean isScreenshotDetected = false;

    // Constructor for using Context with SecuritySDK
//    public ScreenshotDetection(Context context, SecuritySDK securitySDK) {
//        if (context == null) {
//            throw new IllegalArgumentException("Context cannot be null");
//        }
//        if (securitySDK == null) {
//            throw new IllegalArgumentException("SecuritySDK cannot be null");
//        }
//        this.context = context;
//        this.securitySDK = securitySDK;
//    }

    // Constructor for using Activity (if you need to handle Activity-specific cases)
//    public ScreenshotDetection(Activity activity) {
//        if (activity == null) {
//            throw new IllegalArgumentException("Activity cannot be null");
//        }
//        this.context = activity;  // Activity can be used as a context
//        this.securitySDK = null;  // Not using SecuritySDK in this constructor
//    }

//    public void enableScreenshotDetection() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//            registerScreenshotCallback();
//        } else {
//            startFileObserverDetection();
//        }
//    }

//    public void disableScreenshotDetection() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//            unregisterScreenshotCallback();
//        } else {
//            stopFileObserverDetection();
//        }
//    }

//    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
//    private void registerScreenshotCallback() {
//        if (context instanceof Activity) {
//            Activity activity = (Activity) context;
//            // Register screenshot detection callback for API >= 34
//            Log.d("ScreenshotDetection", "Screenshot detection registered for API >= 34");
//            // Example: activity.registerScreenCaptureCallback(activity.getMainExecutor(), this);
//            // Implement actual screenshot detection logic for API level 34 or higher here
//        } else {
//            Log.e("ScreenshotDetection", "Context is not an Activity. Cannot register screenshot callback.");
//        }
//    }

//    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
//    private void unregisterScreenshotCallback() {
//        if (context instanceof Activity) {
//            Activity activity = (Activity) context;
//            // Unregister screenshot detection callback for API >= 34
//            Log.d("ScreenshotDetection", "Screenshot detection unregistered for API >= 34");
//            // Example: activity.unregisterScreenCaptureCallback(this);
//            // Implement actual screenshot callback unregistration for API level 34 or higher here
//        } else {
//            Log.e("ScreenshotDetection", "Context is not an Activity. Cannot unregister screenshot callback.");
//        }
//    }

//    private void startFileObserverDetection() {
//        File screenshotDir = new File(android.os.Environment.getExternalStoragePublicDirectory(
//                android.os.Environment.DIRECTORY_PICTURES), "Screenshots");
//        if (screenshotDir.exists()) {
//            screenshotObserver = new FileObserver(screenshotDir.getPath(), FileObserver.CREATE) {
//                @Override
//                public void onEvent(int event, String path) {
//                    if (event == FileObserver.CREATE) {
//                        Log.d("ScreenshotDetection", "Screenshot detected: " + path);
//                        isScreenshotDetected = true;
////                        if (securitySDK != null) {
////                            securitySDK.onScreenshotDetected();
////                        } else {
////                            Log.e("ScreenshotDetection", "SecuritySDK is not initialized.");
////                        }
//                    }
//                }
//            };
//            screenshotObserver.startWatching();
//            Log.d("ScreenshotDetection", "File observer started for screenshot detection");
//        } else {
//            Log.e("ScreenshotDetection", "Screenshot directory does not exist. Cannot start file observer.");
//        }
//    }
//
//    private void stopFileObserverDetection() {
//        if (screenshotObserver != null) {
//            screenshotObserver.stopWatching();
//            Log.d("ScreenshotDetection", "File observer stopped for screenshot detection");
//        }
//    }
//
//    public boolean isScreenshotDetected() {
//        return isScreenshotDetected;
//    }


//    private Context context;
//    private View rootView;
//    private boolean isScreenshotAttemptDetected;
//
//    public ScreenshotDetection(Context context, View rootView) {
//        this.context = context;
//        this.rootView = rootView;
//        this.isScreenshotAttemptDetected = false;
//    }
//
//    /**
//     * Detect and prevent screenshot attempts.
//     * Returns true if a screenshot attempt is detected, false otherwise.
//     */
//    public boolean detectAndPreventScreenshot() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {  // Android 14 (UpsideDownCake)
//            rootView.setOnApplyWindowInsetsListener((v, insets) -> {
//                if (insets.isVisible(android.view.WindowInsets.Type.statusBars())) {
//                    isScreenshotAttemptDetected = true;
//                    showToast("Screenshot is disabled for security reasons.");
//                }
//                return insets;
//            });
//        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//            // For versions below Android 14
//            ((Activity) context).getWindow().setFlags(
//                    android.view.WindowManager.LayoutParams.FLAG_SECURE,
//                    android.view.WindowManager.LayoutParams.FLAG_SECURE
//            );
//            // FLAG_SECURE automatically prevents screenshots and screen recording.
//            isScreenshotAttemptDetected = true;
//        }
//
//        return isScreenshotAttemptDetected;
//    }
//
//    private void showToast(String message) {
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//    }



    private Context context;
    private boolean isProtectionEnabled;
    private ScreenshotDetectionReceiver receiver;

    public ScreenshotDetection(Context context) {
        this.context = context;
        this.isProtectionEnabled = false;
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
         //   showToast("Protection against screenshots, screen recordings, and screen sharing is enabled.");
        } else {
            showToast("Context is not an instance of Activity. Unable to apply protection.");
        }

        return isProtectionEnabled;
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // Inner class for BroadcastReceiver
    public class ScreenshotDetectionReceiver extends BroadcastReceiver {

        private Context context;

        public ScreenshotDetectionReceiver(Context context) {
            this.context = context;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // Placeholder for detecting potential screenshot or screen recording attempts
            if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
                showToast("Potential screenshot or screen recording attempt detected.");
                isProtectionEnabled = true;
            }
        }

        public void register() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_USER_PRESENT); // Placeholder for actual detection
            context.registerReceiver(this, filter);
        }

        public void unregister() {
            context.unregisterReceiver(this);
        }
    }

    // Clean up receiver to avoid memory leaks
    public void cleanup() {
        if (receiver != null) {
            receiver.unregister();
        }
    }
}
