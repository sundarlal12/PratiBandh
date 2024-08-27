package com.vaptlab.pratibandhsdk;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.FileObserver;
import android.util.Log;

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
}
